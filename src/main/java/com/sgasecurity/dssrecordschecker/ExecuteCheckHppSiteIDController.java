package com.sgasecurity.dssrecordschecker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Controller
public class ExecuteCheckHppSiteIDController {
    @Autowired
    InstallationSiteService installationSiteService;
    @Autowired
    ConfigDataService configDataService;
    @Autowired
    CustomerService customerService;

    @CrossOrigin
    @ResponseBody
    @GetMapping("/checkhppsiteids")
    public void checkHppSiteID() {

        ConfigData configData = configDataService.getConfigDataByConfigName("HIK_URL");
        String domain = configData.getConfigValue();
        configData = configDataService.getConfigDataByConfigName("HIK_APP_KEY");
        String appKey = configData.getConfigValue();
        configData = configDataService.getConfigDataByConfigName("HIK_SECRET_KEY");
        String secretKey = configData.getConfigValue();

        List<InstallationSite> installationSiteList = installationSiteService.getAllInstallationSites();

        System.out.println("Installation sites list found: " + installationSiteList.size());

        StringBuilder stringBuilder = new StringBuilder();

        if (installationSiteList.size() > 0) {
            for (InstallationSite installationSite: installationSiteList) {
                if(installationSite.getHppSiteId() == null || installationSite.getDeviceSerial() == ""){
                    try {
//                        boolean status = searchHppSiteID(installationSite.getId(), installationSite.getHikFormattedCustomerSiteName(), domain, appKey, secretKey);
                          boolean status = searchHppSiteID(installationSite.getId(), "Lewis Munene - KE000575", domain, appKey, secretKey);

                          if(status == true){
                              System.out.println("Installation site with record ID "+installationSite.getId()+" updated with HIK site ID...<br/>");
                          } else {
                              searchHppSiteID(installationSite.getId(), installationSite.getHikFormattedCustomerSiteName(), domain, appKey, secretKey);
                          }

                    } catch (Exception e){
                        System.out.println("Could not search HPP site ID for installation ID "+installationSite.getId()+" with site name "+installationSite.getHikFormattedCustomerSiteName()+"<br/>");
                    }
                }
            }
        } else {
            System.out.println("No installation site found...<br/>");
        }
    }

    public boolean searchHppSiteID(long id, String hikFormattedCustomerSiteName, String domain, String appKey, String secretKey) throws JsonProcessingException {
        Common common = new Common();

        String token = common.getSessionToken(domain, appKey, secretKey);
        String hppSiteId = null;
        InstallationSite installationSite = installationSiteService.getInstallationSiteById(id);

        try {
            hppSiteId = searchSiteByName(domain, token, hikFormattedCustomerSiteName);
            if(hppSiteId != null || hppSiteId != ""){
//              Update DB table with hpp site ID
                installationSite.setHppSiteId(hppSiteId);
                try {
                    System.out.println("Saving HIK site ID for installation site with record ID "+installationSite.getId()+"...");
                    installationSiteService.saveInstallationSite(installationSite);
                } catch (Exception e){
                    System.out.println("Error in saving HIK site ID for installation site with record ID "+installationSite.getId()+"... \n\n"+e.toString());
                    return false;
                }
            } else {
                Customer customer = customerService.getCustomerById(installationSite.getCustomerId());
//              Add site
                JSONObject postFields = new JSONObject();
                postFields.put("siteCity", customer.getLocality());
                postFields.put("siteState", "Kenya");
                postFields.put("siteStreet", customer.getRoadStreet());
                postFields.put("location", customer.getEstateName());
                postFields.put("timeSync", true);
                postFields.put("timeZone", 154);

                try {
                    String response = addSite(domain, token, customer.getSystemCustomerNo(), postFields);
//                    searchHppSiteID(id, hikFormattedCustomerSiteName, domain, appKey, secretKey);

                    if(response == null){
                        common.logErrors("dssrecordchecker", "ExecuteCheckHppSiteIDController", "searchHppSiteID", "Search HPP Site ID in HPPSiteID Loop", "if else block: null add site response");
//                      Send Technical Email
                        System.out.println("Sending failure email to Technical Engineer...");
                        sendTechnicalEmail(customer);
                    }

                    try {
                        System.out.println("Sleeping for 2 seconds...");
                        Thread.sleep(2000);
                    } catch (Exception e) {

                    }

                } catch (Exception e){
                    common.logErrors("dssrecordchecker", "ExecuteCheckHppSiteIDController", "searchHppSiteID", "Search HPP Site ID in HPPSiteID Loop", e.toString());
//                  Send Technical Email
                    System.out.println("Error occurred while adding site for installation with record ID "+installationSite.getId()+" \n\n"+e.toString()+"\n\n Sending failure email to Technical Engineer...");
                    sendTechnicalEmail(customer);
                }
            }
        } catch (Exception e){
            System.out.println("Error occurred while searching site for installation with record ID "+installationSite.getId()+"... \n\n"+e.toString()+"\n\n");
            return false;
        }

        return true;
    }

    public String searchSiteByName(String domain, String token, String uniqueCustId) {
        Common common = new Common();
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

                System.out.println("Returning HIK site ID for customer "+uniqueCustId+"...\n\n");

                return idValue;

            } catch (Exception e) {
                System.out.println("Error in returning HIK site ID... \n\n"+e.toString()+"\n\n");
                return null;
            }

        } catch (Exception e) {
            System.out.println("Returning HIK site ID for customer "+uniqueCustId+"...\n\n"+e.toString()+"\n\n");
            return null;
        }
    }

    public String addSite(String domain, String token, String uniqueCustId, JSONObject postFields) {
        Common common = new Common();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer "+token);

            String apiEndPoint = "/api/hpcgw/v1/site/add";
            String url = domain + apiEndPoint;

            RestTemplate restTemplate = new RestTemplate();

            Customer customer = customerService.getCustomerBySystemCustomerNo(uniqueCustId);
            String name = customer.getFirstName() + " " + customer.getLastName() + " - " + uniqueCustId;
            postFields.put("name", name);

            HttpEntity<String> entity = new HttpEntity<>(postFields.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            System.out.println("Returning add site response for customer "+uniqueCustId+"...");

            return response.getBody();

        } catch (Exception e) {
            common.logErrors("dssrecordchecker", "ExecuteCheckHppSiteIDController", "addSite", "Add Site In Check HPP Site ID Loop", e.toString());
            e.printStackTrace();
            System.out.println("Error adding site for customer "+uniqueCustId+"... \n\n"+e.toString()+"\n\n");
            return null;
        }
    }

    public void sendTechnicalEmail(Customer theCustomer) {
        //     Send failure email
        Common common = new Common();
        ConfigData configData = configDataService.getConfigDataByConfigName("FAILURE_EMAIL");
        String failEmailAddress = configData.getConfigValue();
        configData = configDataService.getConfigDataByConfigName("EMAIL_API_URL");
        String emailApiUrl = configData.getConfigValue();
        String emailSubject = "DSS - Failed To Create HIK Site";
        String emailBody = "Sorry. We were not able to create HIK site for customer " + theCustomer.getSystemCustomerNo() + " " + theCustomer.getFirstName() + " " + theCustomer.getLastName();
        emailBody.concat("<br/>Regards,<br/>SGA Security Team");
        common.sendEmail(failEmailAddress, emailSubject, emailBody, "0", "", emailApiUrl, "0");
    }
}