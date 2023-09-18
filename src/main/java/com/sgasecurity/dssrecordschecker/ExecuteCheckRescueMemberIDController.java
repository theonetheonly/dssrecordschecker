package com.sgasecurity.dssrecordschecker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Controller
public class ExecuteCheckRescueMemberIDController {
    @Autowired
    CustomerService customerService;
    @Autowired
    ConfigDataService configDataService;

    @CrossOrigin
    @ResponseBody
    @GetMapping("/checkrescuememberids")
    public void checkRescueMemberID() {

        List<Customer> customerList = customerService.getCustomers();

        System.out.println("Customers list found: " + customerList.size());

        if (customerList.size() > 0) {
            for (Customer customer : customerList) {
                if(customer.getRescueMemberId() == null || customer.getRescueMemberId().equals("NOT_FOUND") || customer.getRescueMemberId() == ""){
                    try {
                         findPersonId(customer.getSystemCustomerNo(), customer.getId());
                        System.out.println("Searching rescue member ID...");
                    } catch (Exception e){
                        try {
                            createRescueMember(customer);
                            System.out.println("Creating rescue member...");
                        } catch (Exception ex){
                            System.out.println("Error occurred while creating rescue member: "+ex.toString());
                        }
                    }

                }
            }
        }
    }

    public String findPersonId(String customerNo, int customerId) throws JsonProcessingException {
        Common common = new Common();
        try {

            ConfigData configData = configDataService.getConfigDataByConfigName("RESCUECO_TOKEN");
            String rescueToken = configData.getConfigValue();

            Client client = ClientBuilder.newClient();

            Response response = client.target("https://ci-dev-sga-pie7aeth9a.flaredispatch.com/v1/memberships")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Bearer " + rescueToken)
                    .get();

            String jsonResponse = response.readEntity(String.class);
            JsonNode jsonNode = null;
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode[] jsonNodes = objectMapper.readValue(jsonResponse, JsonNode[].class);
            String alternativeIdentifier = null;
            for (JsonNode obj : jsonNodes) {
                alternativeIdentifier = objectMapper
                        .convertValue(obj, Map.class)
                        .toString();

                if (alternativeIdentifier.contains(customerNo.toString())) {
                    jsonNode = obj;
                    break;
                }
            }
            JsonNode rootNode = objectMapper.readTree(jsonNode.toString());
            String rescueMemberId = rootNode.get("mainMember").get("id").asText();

            Customer customer = customerService.getCustomerById(customerId);
            customer.setRescueMemberId(rescueMemberId);

            try {
                customerService.saveCustomer(customer);
                System.out.println("Saving the updated rescue member ID for customer with record ID "+customer.getId()+"...");
            } catch (Exception e){
                common.logErrors("dssrecordchecker", "ExecuteCheckRescueMemberIDController", "findPersonId", "Find Person Rescue Id with Customer Number "+customerNo+" in Check Rescue Member ID Loop", e.toString());
                findPersonId(customerNo, customerId);
                System.out.println("Error in saving rescue member ID for customer with record ID "+customer.getId()+"...");
            }

            return rescueMemberId;

        } catch (Exception e){
            common.logErrors("dssrecordchecker", "ExecuteCheckRescueMemberIDController", "findPersonId", "Find Person Rescue Id with Customer Number "+customerNo+" in Check Rescue Member ID Loop", e.toString());
            System.out.println("Error occurred while creating rescue member: "+e.toString());
            return null;
        }
    }

    public void createRescueMember(Customer theRescueCustomer) throws JsonProcessingException {
        Common common = new Common();
        try {
            ConfigData configData = configDataService.getConfigDataByConfigName("RESCUECO_TOKEN");
            String rescueToken = configData.getConfigValue();

            Client client = ClientBuilder.newClient();
            Entity payload = Entity.json("{\"people\": [{\"alternativeIdentifiers\": [\"" + theRescueCustomer.getSystemCustomerNo() + "\"],\"telephoneNumber\": \"" + theRescueCustomer.getPhone() + "\", \"isMainMember\": true,\"formalFullName\": \"" + theRescueCustomer.getFirstName() + " " + theRescueCustomer.getLastName() + "\",\"dateOfBirth\": \"1990-02-12\",\"sex\": \"MALE\",\"preferredHospitalText\": \"Nairobi Hospital\",\"insurerText\": \"Sanlaam\",\"insurancePolicyNumber\": \"INSURANCE #12345\"}]}");
            Response createRescueMemberResponse = client.target("https://ci-dev-sga-pie7aeth9a.flaredispatch.com/v1/memberships")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Bearer " + rescueToken)
                    .header("User-Agent", "SGA Panic Button")
                    .post(payload);

        } catch (Exception e) {
            common.logErrors("dssrecordchecker", "ExecuteCheckRescueMemberIDController", "createRescueMember", "Create Rescue Member Response", "Customer ID: " + Integer.toString(theRescueCustomer.getId()) + " " + e.toString());
            System.out.println("Error occurred while getting rescue response payload: "+e.toString()+"\n\n Sending failure email to Technical Engineer...");
            sendTechnicalEmail(theRescueCustomer);
        }
    }

    public void sendTechnicalEmail(Customer theRescueCustomer) {
//      Send failure email
        Common common = new Common();
        ConfigData configData = configDataService.getConfigDataByConfigName("FAILURE_EMAIL");
        String emailAddress = configData.getConfigValue();
        configData = configDataService.getConfigDataByConfigName("EMAIL_API_URL");
        String emailApiUrl = configData.getConfigValue();

        String emailSubject = "DSS - Failed To Create Rescue Member Response";
        String emailBody = "Sorry. We were not able to CREATE Rescue member response for " + theRescueCustomer.getSystemCustomerNo() + " " + theRescueCustomer.getFirstName() + " " + theRescueCustomer.getLastName();
        emailBody.concat("<br/>Regards,<br/>SGA Security Team");
        common.sendEmail(emailAddress, emailSubject, emailBody, "0", "", emailApiUrl, "0");
    }
}
