package com.sgasecurity.dssrecordschecker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class InstallationSiteController {
    private static final long serialVersionUID = 1L;
    @Autowired
    InstallationSiteService installationSiteService;
    InstallationSite installationSite = null;
    ConfigData configData = null;
    Common common = null;
    String msg = null;
    @Autowired
    ConfigDataService configDataService;
    @Autowired
    CustomerService customerService;
    @Autowired
    DepositService depositService;
    Deposit deposit = null;
    Random random = new Random();
    int randomNumber = random.nextInt(1000000);
    String systemCustomerNo = "SGA-" + randomNumber;
    Customer customer = null;
    String emailAddress = null;
    String emailSubject = null;
    String emailHeader = null;
    String emailFooter = null;
    String emailBody = null;
    String fullEmail = null;
    String smsBody = null;
    @Autowired
    SystemSchedulerService systemSchedulerService;
    SystemScheduler systemScheduler = null;
    String smsApiUrl = null;
    String emailApiUrl = null;
    String phone = null;
    String bitlyToken = null;
    String bitlyUrl = null;
    @Autowired
    TempPackageAndCalendarRepository tempPackageAndCalendarRepository;
    @Autowired
    PackageTypeService packageTypeService;
    String hppSiteId = null;
    long currentInstallationId = 0;

    @CrossOrigin
    @RequestMapping("/save/schedule")
    @ResponseBody
    public Map<String, Object> saveSchedule(@RequestBody InstallationSite theInstallationSite) throws JsonProcessingException {
//    public String createSite() throws JsonProcessingException {

        common = new Common();
        configData = new ConfigData();

        LocalDate today = LocalDate.now();
        LocalDate after30Days = LocalDate.now().plusDays(30);

        LocalDate installationDate = theInstallationSite.getInstallationDate();

        int customerId = theInstallationSite.getCustomerId();

        Map<String, Object> map = new HashMap<>();

        try {
            installationSite = new InstallationSite();
            customer = customerService.getCustomerById(customerId);

            TempPackageAndCalendar tempPackageAndCalendar = tempPackageAndCalendarRepository.getCurrentUserByCustomerID(String.valueOf(customerId));
            String packageDetails = tempPackageAndCalendar.getPackageDetails();

            ObjectMapper objectMapper = new ObjectMapper();
            PackageInfo packageInfo = objectMapper.readValue(packageDetails, PackageInfo.class);

            String packageName = packageInfo.getPackageName();
            PackageType packageType = packageTypeService.getPackageType(packageName);
            int packageTypeId = packageType.getId();
            String paymentAmountStr = packageInfo.getDeposit();
            double paymentAmount = Double.parseDouble(paymentAmountStr);

           // Unique Site ID
            String uniqueSiteId = customer.getSystemCustomerNo() + "_" + customer.getFirstName() + "_" + customer.getLastName();

            // Hik Formatted Customer Name
            String hikFormattedCustomerSiteName = customer.getFirstName() + " " + customer.getLastName() + " - " + customer.getSystemCustomerNo();

            ConfigData configDataSms = configDataService.getConfigDataByConfigName("SMS_API_URL");
            smsApiUrl = configDataSms.getConfigValue();

            ConfigData configDataEmail = configDataService.getConfigDataByConfigName("EMAIL_API_URL");
            emailApiUrl = configDataEmail.getConfigValue();

            ConfigData configDataBitlyToken = configDataService.getConfigDataByConfigName("BITLY_TOKEN");
            bitlyToken = configDataBitlyToken.getConfigValue();

            ConfigData configDataBitlyUrl = configDataService.getConfigDataByConfigName("BITLY_URL");
            bitlyUrl = configDataBitlyUrl.getConfigValue();

            String dateString = theInstallationSite.getInstallationDate().toString();
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatterDate);

            try {
            String tempHIKID = common.generateRandomValue("TMPHIK");
            installationSite.setSystemCustomerNo(customer.getSystemCustomerNo());
            installationSite.setInstallationDate(installationDate);
            installationSite.setEstate(customer.getEstateName());
            installationSite.setHouseDoorNo(customer.getHouseDoorNo());
            installationSite.setLocality(customer.getLocality());
            installationSite.setDepositAmount(paymentAmount);
            installationSite.setPackageTypeId(packageTypeId);
            installationSite.setPackageTypeName(packageName);
            installationSite.setStreet(customer.getRoadStreet());
            installationSite.setHouseholdNumber(customer.getHouseholdNumber());
            installationSite.setUniqueSiteId(uniqueSiteId);
            installationSite.setHikFormattedCustomerSiteName(hikFormattedCustomerSiteName);
            installationSite.setHppSiteId(tempHIKID);
            installationSite.setCustomerId(customer.getId());
            installationSite.setHomeType(customer.getHomeType());
            installationSite.setPostalAddress(customer.getPostalAddress());
            installationSite.setPostalCode(customer.getPostalCode());
            installationSite.setSessionNo(customer.getSessionNo());
            installationSite.setInstallationStatus("UNDER_PRODUCTION"); // Installation not yet activated
            installationSite.setHandoverStatus("NOT_DONE"); // Handover not yet done
            installationSite.setPaymentStatus(1);
            installationSite.setLastPaymentDate(today);
            installationSite.setNextPaymentDate(after30Days);
            installationSite.setIsAssignedTechnician("NO");
            installationSiteService.saveInstallationSite(installationSite);

            currentInstallationId = installationSite.getId();

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                common.logErrors("dssv6", "InstallationSiteController", "saveSchedule", "Sleep in saving HIK Site ID", e.toString());
            }

            } catch (Exception e){
                common.logErrors("dssv6", "InstallationSiteController", "saveSchedule", "Save InstallationSite Data", e.toString());
                map.put("status", "FAIL");
                map.put("isSaved", "NO");
                map.put("currentCustomerId", customerId);
                map.put("message", "Failed to create site record in central DB \n" + e.toString());

//               Send failure email
                configData = configDataService.getConfigDataByConfigName("FAILURE_EMAIL");
                emailAddress = configData.getConfigValue();
                String emailSubject = "DSS - Failed To Create HIK Site";
                String emailBody = "Sorry. We were not able to create Installation record for central DB site for customer " + customer.getSystemCustomerNo() + " " + customer.getFirstName() + " " + customer.getLastName();
                emailBody.concat("<br/>Regards,<br/>SGA Security Team");
                common.sendEmail(emailAddress, emailSubject, emailBody, "0", "", emailApiUrl, "0");

                return map;
            }

            try {
                String domain = "https://api.hik-partner.com";
                String appKey = "9QrGlY67oN";
                String secretKey = "8nQS9AXWIq";
                String token = common.getSessionToken(domain, appKey, secretKey);
                hppSiteId = searchSiteByName(domain, token, hikFormattedCustomerSiteName);
                Thread.sleep(7000);
                // if search site fails - send techincian email with fauled search site name
                if (hppSiteId.contains("FAIL")) {
                    String message ="Unable to get HIK SITE ID after search. Unique customer ID : "+customer.getSystemCustomerNo();
                    technicianSendFailEmail("FAIL HIK SEARCH INSTALLATION", message);
                    common.logEvents("dssv6", "InstallationSiteController", "saveSchedule", "Create HIK Site ID", "Creating HIK ID FAILED for customer ID:  "+ Integer.toString(customerId));
//
                }
                else {
                    InstallationSite installationSite2 = installationSiteService.getInstallationSiteById(currentInstallationId);
                    installationSite2.setHppSiteId(hppSiteId);
                    installationSiteService.saveInstallationSite(installationSite2);
                    common.logEvents("dssv6", "InstallationSiteController", "saveSchedule", "Create HIK Site ID", "Creating HIK ID SUCCEEDED for customer ID:  "+ Integer.toString(customerId)+ "hpp site id = "+hppSiteId);
                }

            } catch (Exception e){
                common.logErrors("dssv6", "InstallationSiteController", "saveSchedule", "Create HIK Site ID", "    (Customer ID "+Integer.toString(customerId)+ ")\n"+e.toString()+"\n");
                map.put("status", "FAIL");
                map.put("isSaved", "NO");
                map.put("currentCustomerId", customerId);
                map.put("message", "Failed create HIK Site ID \n" + e.toString());

                return map;
            }

//          1. Send registration email (Thank You) to customer for registering with SGA

            String customerPortalUrl = null;

            try {
                emailAddress = customer.getEmail();
                configData = configDataService.getConfigDataByConfigName("SGA_CUSTOMER_REGISTRATION_EMAIL_SUBJECT");
                emailSubject = configData.getConfigValue();
                configData = configDataService.getConfigDataByConfigName("EMAIL_HEADER");
                emailHeader = configData.getConfigValue();
                configData = configDataService.getConfigDataByConfigName("EMAIL_FOOTER");
                emailFooter = configData.getConfigValue();

                configData = configDataService.getConfigDataByConfigName("SGA_CUSTOMER_REGISTRATION_EMAIL_BODY");

                emailBody = configData.getConfigValue();
                emailBody = emailBody.replace("#customername", customer.getFirstName() + " " + customer.getLastName());
                emailBody = emailBody.replace("#amount", String.valueOf(paymentAmount));

                configData = configDataService.getConfigDataByConfigName("BITLY_AVAILABILITY");
                String bitlyAvailability = configData.getConfigValue();
                configData = configDataService.getConfigDataByConfigName("CUSTOMER_PORTAL_URL");
                String urlToBeShortened = configData.getConfigValue();

                if (bitlyAvailability.equals("AVAILABLE")) {
                    customerPortalUrl = common.shortenUrl(urlToBeShortened, bitlyToken, bitlyUrl);
                } else {
                    customerPortalUrl = urlToBeShortened;
                }
                emailBody = emailBody.replace("#customerportal", customerPortalUrl);
                emailBody = emailBody.replace("#scheduledate", installationDate.toString());
                fullEmail = emailHeader + emailBody + emailFooter;
                common.sendEmail(emailAddress, emailSubject, fullEmail, "1", "", emailApiUrl, "1");
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
                common.logErrors("dssv6", "InstallationSiteController", "saveSchedule", "Send customer install email", e.toString());
            }

//          Send SMS
            try {
                configData = configDataService.getConfigDataByConfigName("SGA_CUSTOMER_REGISTRATION_SMS_BODY");
                smsBody = configData.getConfigValue();
                smsBody = smsBody.replace("#customername", customer.getFirstName() + " " + customer.getLastName());
                smsBody = smsBody.replace("#customerportal", customerPortalUrl);
                common.sendSMS(customer.getPhone(), smsBody, smsApiUrl);
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
                common.logErrors("dssv6", "InstallationSiteController", "saveSchedule", "Send customer install SMS", e.toString());
            }

//          2. Send Technical Email
            try {
                configData = configDataService.getConfigDataByConfigName("TECHNICAL_LEAD_EMAIL");
                emailAddress = configData.getConfigValue();
                configData = configDataService.getConfigDataByConfigName("TECHNICAL_LEAD_EMAIL_SUBJECT");
                emailSubject = configData.getConfigValue();
                configData = configDataService.getConfigDataByConfigName("TECHNICAL_LEAD_EMAIL_BODY");
                emailBody = configData.getConfigValue();
                emailBody = emailBody.replace("#firstname", customer.getFirstName());
                emailBody = emailBody.replace("#lastname", customer.getLastName());

                String[] words = packageName.split("_");
                StringBuilder result = new StringBuilder();
                for (String word : words) {
                    String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                    result.append(capitalizedWord).append(" ");
                }

                String thePackageName = result.toString().trim();
                emailBody = emailBody.replace("#service", thePackageName);
                emailBody = emailBody.replace("#location", customer.getEstateName());
                emailBody = emailBody.replace("#houseno", customer.getHouseDoorNo());
                emailBody = emailBody.replace("#date", installationDate.toString());
                common.sendEmail(emailAddress, emailSubject, emailBody, "0", "", emailApiUrl, "0");
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
                common.logErrors("dssv6", "InstallationSiteController", "saveSchedule", "Send technician email error", e.toString());

            }

//          Update count in system scheduler
            updateScheduleCount(localDate, packageName);

            map.put("currentCustomerId", customerId);
            map.put("status", "SUCCESS");
            map.put("isSaved", "YES");
            map.put("currentHIKSiteID", hppSiteId);
            map.put("currentInstallationId", String.valueOf(currentInstallationId));

            return map;

        } catch (Exception e){
            common.logErrors("dssv6", "InstallationSiteController", "saveSchedule", "Save InstallationSite Data Activities - Outer Try Catch Block", e.toString());
            map.put("currentCustomerId", customerId);
            map.put("status", "FAIL");
            map.put("isSaved", "NO");

            return map;
        }
    }

    public void updateScheduleCount(@RequestParam LocalDate scheduledDate, @RequestParam String packageTypeName) {
       try {
           boolean isExists = systemSchedulerService.isDateExisting(scheduledDate);
           if (isExists == true) {
               systemScheduler = systemSchedulerService.getByScheduleDate(scheduledDate);
               if (packageTypeName.equals("SAFE_HOME")) {
                   int safeHomeCounter = systemScheduler.getSafeHomeBookings();
                   safeHomeCounter = safeHomeCounter + 1;
                   systemScheduler.setSafeHomeBookings(safeHomeCounter);
                   systemSchedulerService.saveSystemScheduler(systemScheduler);
               } else {
                   int safeHomePlusCounter = systemScheduler.getSafeHomePlusBookings();
                   safeHomePlusCounter = safeHomePlusCounter + 1;
                   systemScheduler.setSafeHomePlusBookings(safeHomePlusCounter);
                   systemSchedulerService.saveSystemScheduler(systemScheduler);
               }
           } else {
               systemScheduler = new SystemScheduler();
               systemScheduler.setScheduleDate(scheduledDate);
               if (packageTypeName.equals("SAFE_HOME")) {
                   systemScheduler.setSafeHomeBookings(1);
                   systemScheduler.setSafeHomePlusBookings(0);
                   systemSchedulerService.saveSystemScheduler(systemScheduler);
               } else {
                   systemScheduler.setSafeHomeBookings(0);
                   systemScheduler.setSafeHomePlusBookings(1);
                   systemSchedulerService.saveSystemScheduler(systemScheduler);
               }
           }
       }
       catch (Exception e)
       {
           common.logErrors("dssv6", "InstallationSiteController", "updateScheduleCount", "Update Schedule Count", e.toString());
       }
    }


    public String searchSiteByName(String domain, String token, String uniqueCustId) {
        try {
            String apiEndpoint = "/api/hpcgw/v1/site/search";
            String url = domain + apiEndpoint;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            // Request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("page", 1);
            requestBody.put("pageSize", 500);
            requestBody.put("search", uniqueCustId);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            String response = responseEntity.getBody();

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response);
                JsonNode idNode = rootNode.path("data").path("rows").get(0).path("id");
                String idValue = idNode.asText();

                return idValue;

            } catch (Exception e) {
                common.logErrors("dssv6", "InstallationSiteController", "searchSiteByName", "Search Site By Name", e.toString());
                return "FAIL";
            }

        } catch (Exception e) {
            common.logErrors("dssv6", "InstallationSiteController", "searchSiteByName", "Search Site By Name", e.toString());
            return "FAIL";
        }
    }

    private  void technicianSendFailEmail(String topic,String message)
    {
        try{
            Date date = new Date();
        configData = configDataService.getConfigDataByConfigName("FAILURE_EMAIL");
        emailAddress = configData.getConfigValue();
      //  configData = configDataService.getConfigDataByConfigName("TECHNICAL_LEAD_EMAIL_SUBJECT");
        emailSubject = topic+ " Fail!";
        emailBody = topic +" Fail<br />"+message + "<br />at "+date.toString();

        common.sendEmail(emailAddress, emailSubject, emailBody, "0", "", emailApiUrl, "0");

    }catch (Exception e)
        {
            common.logErrors("dssv6", "InstallationSiteController", "technicianSendFailEmail", "Send Email To Technician", e.toString()+"\n");
        }
    }

}
