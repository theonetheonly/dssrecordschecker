package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    PackageTypeService packageTypeService;
    PackageType packageType;
    Random random = new Random();
    int randomNumber = random.nextInt(1000000);
    Customer customer = null;
    Map<String, Object> theData;
    @Autowired
    SystemSchedulerService systemSchedulerService;
    @Autowired
    ConfigDataService configDataService;
    ConfigData configData = null;
    Common common = null;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TempPackageAndCalendarRepository tempPackageAndCalendarRepository;
    String smsApiUrl = null;
    String emailApiUrl = null;
    String bitlyToken = null;
    String bitlyUrl = null;

    @CrossOrigin
    @RequestMapping("/save/details")
    @ResponseBody
    public Map<String, Object> saveDetails(@RequestBody Customer theCustomer) {
        common = new Common();
        try {
            System.out.println("Started data");
            theData = new HashMap<>();
            packageType = packageTypeService.getPackageType("SAFE_HOME");
            theData.put("package_type_name", "SAFE_HOME");
            theData.put("package_type_id", packageType.getId());
            double monthlyCostInclusiveSafeHome = packageType.getMonthlyCostInclusive();
            theData.put("safe_home_monthly_cost", monthlyCostInclusiveSafeHome);
            double depositSafeHome = packageType.getDepositAmount();
            theData.put("safe_home_deposit", depositSafeHome);
            double vat = packageType.getVatRate();
            theData.put("vat", vat);

            if (theCustomer.getId() < 1 && Objects.isNull(customerService.getCustomerBySessionNo(theCustomer.getSessionNo()))) {
                   customer = new Customer();
                   customer.setFirstName(theCustomer.getFirstName());
                   customer.setLastName(theCustomer.getLastName());
                   customer.setPrimaryContact(theCustomer.getPrimaryContact());
                   customer.setEmail(theCustomer.getEmail());
                   customer.setPhone(theCustomer.getPhone());
                   customer.setKraPin(theCustomer.getKraPin());
                   customer.setIdPassport(theCustomer.getIdPassport());
                   customer.setOccupation(theCustomer.getOccupation());
                   customer.setSessionNo(theCustomer.getSessionNo());
                   // Use format- TMP000001
                   // Insert TMP customer no...also has to be unique
                    String newTempCustomerNo = "TMP"+getSystemCustomerNumberRandom();
                    customer.setSystemCustomerNo(newTempCustomerNo);

                   if (!Objects.isNull(customerService.saveCustomer(customer))) {
                       theData.put("currentCustomerId",  Integer.toString(customer.getId()));
                       theData.put("errorState", "SUCCESS");
                   } else {
                       common.logErrors("dssv6", "CustomerController", "saveDetails", "Save Customer Basic Details", "Could not save customer basic details (if else block)");
                       theData.put("errorMessage", "Could not add new customer. Reason unknown");
                       theData.put("errorState", "FAIL");
                   }
                    return theData;
               } else {
                   customer = customerService.getCustomerBySessionNo(theCustomer.getSessionNo());
                   if (customer == null) {
                        if (customer.getId() > 0) {
//                         customer = customerRepository.getById(theCustomer.getId());
                            customer = customerService.getCustomerById(theCustomer.getId());
                        }
                   }

                   customer.setFirstName(theCustomer.getFirstName());
                   customer.setLastName(theCustomer.getLastName());
                   customer.setPrimaryContact(theCustomer.getPrimaryContact());
                   customer.setEmail(theCustomer.getEmail());
                   customer.setPhone(theCustomer.getPhone());
                   customer.setKraPin(theCustomer.getKraPin());
                   customer.setIdPassport(theCustomer.getIdPassport());
                   customer.setOccupation(theCustomer.getOccupation());

                   if (!Objects.isNull(customerService.saveCustomer(customer))) {
                       theData.put("currentCustomerId",  Integer.toString(customer.getId()));
                       theData.put("errorState", "SUCCESS");
                   } else {
                       common.logErrors("dssv6", "CustomerController", "saveDetails", "Save Customer Basic Details", "Could not save customer basic details (if else block)");
                       theData.put("errorMessage", "Could re-save customer data");
                       theData.put("errorState", "FAIL");
                   }
                    return theData;
               }
        } catch (Exception e) {
            common.logErrors("dssv6", "CustomerController", "saveDetails", "Save Customer Data", e.toString());
            Map<String,Object> errorMap = new HashMap<>();
            errorMap.put("errorState", "FAIL");
            errorMap.put("errorMessage", e.toString());
            return errorMap;
        }
    }

    public String getSystemCustomerNumberRandom()
    {
            int min = 10000000;
            int max = 99999999;
            Random R = new Random();
            int randomNumber = R.nextInt(max + 1 - min) + min;
            return Integer.toString(randomNumber);
    }

    @CrossOrigin
    @RequestMapping("/save/address")
    @ResponseBody
    public Map<String, Object> saveAddress(@RequestBody Customer theCustomer){
        common = new Common();
        Map<String, Object> responseMap = new HashMap<>();

        try {
            customer = customerService.getCustomerBySessionNo(theCustomer.getSessionNo());
            customer.setEstateName(theCustomer.getEstateName());
            customer.setPostalAddress(theCustomer.getPostalAddress());
            customer.setTownCity(theCustomer.getTownCity());
            customer.setPostalCode(theCustomer.getPostalCode());
            customer.setHomeType(theCustomer.getHomeType());
            customer.setHouseholdNumber(theCustomer.getHouseholdNumber());
            customer.setRoadStreet(theCustomer.getRoadStreet());
            customer.setHouseDoorNo(theCustomer.getHouseDoorNo());
            customer.setHouseOwning(theCustomer.getHouseOwning());

            packageType = packageTypeService.getPackageType("SAFE_HOME");
            responseMap.put("deposit", packageType.getDepositAmount());
            responseMap.put("monthlyCost", packageType.getMonthlyCostInclusive());
            responseMap.put("packageTypeId", packageType.getId());
            responseMap.put("packageTypeName", packageType.getPackageTypeName());
            responseMap.put("customerId", customer.getId());
            responseMap.put("vat", packageType.getVatRate());
            responseMap.put("status", "SUCCESS");

            if (!Objects.isNull(customerService.saveCustomer(customer))) {
                return responseMap;
            } else {
                common.logErrors("dssv6", "CustomerController", "saveAddress", "Save Customer Address Data", "Could not save customer address data (if else block)");
                responseMap.put("status", "FAIL");
            }

        } catch (Exception e) {
            common.logErrors("dssv6", "CustomerController", "saveAddress", "Save Customer Address Data", e.toString());
            responseMap.put("status", "FAIL");
        }
        return responseMap;
    }

    @CrossOrigin
    @RequestMapping("/select/package")
    @ResponseBody
    public Map<String, Object> selectPackage(@RequestParam String selectedPackageType, @RequestParam String currentCustomerId) {
        common = new Common();
        Map<String, Object> responseMap = new HashMap<>();
        try {
            PackageType thePackageType = packageTypeService.getPackageType(selectedPackageType);
            Double deposit = thePackageType.getDepositAmount();
            Double monthlyCost = thePackageType.getMonthlyCostInclusive();
            int packageTypeId = thePackageType.getId();
            double vat = thePackageType.getVatRate();

            responseMap.put("packageTypeName", selectedPackageType);
            responseMap.put("deposit", deposit);
            responseMap.put("monthlyCost", monthlyCost);
            responseMap.put("packageTypeId", packageTypeId);
            responseMap.put("currentCustomerId", currentCustomerId);
            responseMap.put("vat", vat);
            responseMap.put("status", "SUCCESS");
            return responseMap;

        } catch (Exception e) {
            common.logErrors("dssv6", "CustomerController", "selectPackage", "Select Package", e.toString());
            responseMap.put("status", "FAIL");
        }
        return responseMap;
    }

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
                if(customer.getPassword() == null || customer.getPassword() == ""){
                    try {
                        Map<String, String> map = common.generatePassword();
                        String otp = map.get("password");
                        String hashedPassword = map.get("hashedPassword");
                        String email = customer.getEmail();
                        String fullName = customer.getFirstName() + " " + customer.getLastName();
                        String phone = customer.getPhone();
                        String customerPortalUrl = null;

                        customer.setPassword(hashedPassword);
                        customer.setOTP(otp);
                        customerService.saveCustomer(customer);

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
