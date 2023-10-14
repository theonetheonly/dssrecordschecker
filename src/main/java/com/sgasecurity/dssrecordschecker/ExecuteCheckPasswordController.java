package com.sgasecurity.dssrecordschecker;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ExecuteCheckPasswordController {
    @Autowired
    CustomerService customerService;
    @Autowired
    ConfigDataService configDataService;
    Common common = null;
    String smsApiUrl = null;
    String emailApiUrl = null;
    String bitlyToken = null;
    String bitlyUrl = null;
    @Autowired
    AuthDataService authDataService;


    @CrossOrigin
    @ResponseBody
    @GetMapping("/checkpassword")
    public void checkPassword() {

        common = new Common();

        try {
            ConfigData configDataSms = configDataService.getConfigDataByConfigName("SMS_API_URL");
            smsApiUrl = configDataSms.getConfigValue();

            ConfigData configDataEmail = configDataService.getConfigDataByConfigName("EMAIL_API_URL");
            emailApiUrl = configDataEmail.getConfigValue();

            ConfigData configDataBitlyToken = configDataService.getConfigDataByConfigName("BITLY_TOKEN");
            bitlyToken = configDataBitlyToken.getConfigValue();

            ConfigData configDataBitlyUrl = configDataService.getConfigDataByConfigName("BITLY_URL");
            bitlyUrl = configDataBitlyUrl.getConfigValue();

        } catch (Exception e){
            common.logErrors("dssrecordschecker", "CustomerController", "checkPassword", "Password checker", e.toString());
        }

        List<Customer> customerList = customerService.getCustomers();

        System.out.println("Customer list found: " + customerList.size());

        StringBuilder stringBuilder = new StringBuilder();

        if (customerList.size() > 0) {
            for (Customer customer: customerList) {
                AuthData authData = authDataService.getAuthDataBySystemCustomerNo(customer.getSystemCustomerNo());
                if(authData.getPassword() == null || authData.getPassword() == "" || authData.getPassword().isEmpty()){

                    try {
                        Random random = new Random();
                        int randomNumber = 1000 + random.nextInt(9000);
                        String otp = String.valueOf(randomNumber);
                        String salt = BCrypt.gensalt();
                        String hashedPassword = BCrypt.hashpw(otp, salt);

                        String customerNo = customer.getSystemCustomerNo();
                        String email = customer.getEmail();
                        String fullName = customer.getFirstName() + " " + customer.getLastName();
                        String phone = customer.getPhone();
                        String customerPortalUrl = null;

                        authData.setPassword(hashedPassword);
                        authData.setOtp(otp);
                        authDataService.saveAuthData(authData);

                        try {
//                          2. Send OTP email to customer

                            ConfigData configDataSubject = configDataService.getConfigDataByConfigName("SGA_CUSTOMER_OTP_EMAIL_SUBJECT");
                            String emailSubject = configDataSubject.getConfigValue();
                            ConfigData configDataHeader = configDataService.getConfigDataByConfigName("EMAIL_HEADER");
                            String emailHeader = configDataHeader.getConfigValue();
                            ConfigData configDataFooter = configDataService.getConfigDataByConfigName("EMAIL_FOOTER");
                            String emailFooter = configDataFooter.getConfigValue();

                            ConfigData configDataEmail = configDataService.getConfigDataByConfigName("SGA_CUSTOMER_OTP_EMAIL_BODY");

                            ConfigData configDataBitly = configDataService.getConfigDataByConfigName("BITLY_AVAILABILITY");
                            String bitlyAvailability = configDataBitly.getConfigValue();
                            ConfigData configDataPortal = configDataService.getConfigDataByConfigName("CUSTOMER_PORTAL_URL");
                            String urlToBeShortened = configDataPortal.getConfigValue();

                            if (bitlyAvailability.equals("AVAILABLE")) {
                                customerPortalUrl = common.shortenUrl(urlToBeShortened, bitlyToken, bitlyUrl);
                            } else {
                                customerPortalUrl = urlToBeShortened;
                            }

                            String emailBody = configDataEmail.getConfigValue();
                            emailBody = emailBody.replace("#customername", fullName);
                            emailBody = emailBody.replace("#otp", otp);
                            emailBody = emailBody.replace("#customerno", customerNo);
                            emailBody = emailBody.replace("#customerportal", customerPortalUrl);
                            String fullEmail = emailHeader + emailBody + emailFooter;
                            common.sendEmail(email, emailSubject, fullEmail, "1", "", emailApiUrl, "1");
                            Thread.sleep(1000);

                        } catch (Exception e) {
                            common.logErrors("dssrecordschecker", "CustomerController", "checkPassword", "Send OTP email to customer - After Record Checking", e.toString());
                        }

//                      Send OTP SMS to customer

                        try {
                            ConfigData configDataSMS = configDataService.getConfigDataByConfigName("SGA_CUSTOMER_OTP_SMS_BODY");
                            String smsBody = configDataSMS.getConfigValue();
                            smsBody = smsBody.replace("#customername", fullName);
                            smsBody = smsBody.replace("#otp", otp);
                            smsBody = smsBody.replace("#customerno", customerNo);
                            common.sendSMS(phone, smsBody, smsApiUrl);
                            Thread.sleep(1000);

                        } catch (Exception e) {
                            common.logErrors("dssrecordschecker", "CustomerController", "checkPassword", "Send OTP SMS to customer - After Record Checking", e.toString());
                        }
                    } catch (Exception e){
                        System.out.println("Could not search Customer with Customer ID "+customer.getId()+"<br/>");
                    }
                }
            }
        } else {
            System.out.println("No customer found...<br/>");
        }
    }
}
