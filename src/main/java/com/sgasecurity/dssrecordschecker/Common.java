package com.sgasecurity.dssrecordschecker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class Common {

    public String doBase64Encode(String dataToEncode) {
        try {
            // Encoding
            String encodedString = Base64.getEncoder().encodeToString(dataToEncode.getBytes());
            return encodedString;
        } catch (Exception e){
            logErrors("dssv6", "Common", "doBase64Encode", "Do Base64 Encode", e.toString());
            return "FAIL";
        }
    }

    public String doBase64Decode(String dataToDecode) {
        try {
        // Decoding
        byte[] decodedBytes = Base64.getDecoder().decode(dataToDecode);
        String decodedString = new String(decodedBytes);
        return decodedString;
        } catch (Exception e){
            logErrors("dssv6", "Common", "doBase64Decode", "Do Base64 Decode", e.toString());
            return "FAIL";
        }
    }

    public void sendEmail(String email, String subject, String message, String html, String cc, String url, String base64Encode)
    {
        try {
            if(base64Encode == "1"){
                message = doBase64Encode(message);
            }

            Map<String, Object> post_data =  new LinkedHashMap<>();
            post_data.put("email",email);
            post_data.put("subject",subject);
            post_data.put("message", message);
            post_data.put("html",html);
            post_data.put("accesskey","sga2022now");
            post_data.put("base64decode",base64Encode);

            NetworkActivity networkActivity = new NetworkActivity();
            networkActivity.executeURL(url, post_data);
        }
        catch (Exception e)
        {
            logErrors("dssv6", "Common", "sendEmail", "Send Email", e.toString());
        }
    }

    public void sendSMS(String mobilephone, String message, String url) throws IOException {
        try {
            Map<String, Object> post_data = new LinkedHashMap<>();
            post_data.put("mobile", mobilephone);
            post_data.put("message", message);
            post_data.put("gatewayip", "192.168.0.1");
            post_data.put("accesscode", "4321");
            NetworkActivity networkActivity = new NetworkActivity();
            networkActivity.smsExecuteURL(url, post_data);

        } catch (Exception e) {
            logErrors("dssv6", "Common", "sendSMS", "Send SMS", e.toString());
        }
    }

    public String shortenUrl(String urlToBeShortened, String token, String apiUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);

            String requestBody = "{\"long_url\":\"" + urlToBeShortened + "\"}";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseData = response.getBody();
                JsonNode responseJson = objectMapper.readTree(responseData);
                String shortUrl = responseJson.get("id").asText();
                return shortUrl;
            } else {
                logErrors("dssv6", "Common", "shortenUrl", "Shorten Url", "Bitly API error occurred");
                return "FAIL";
            }
        } catch (Exception e) {
            logErrors("dssv6", "Common", "shortenUrl", "Shorten Url", e.toString());
            return "FAIL";
        }
    }

    public  boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void logErrors(String directory, String file, String function, String errorTitle, String errorMessage) {
        String filePath = "/opt/dssfiles/dsserrors.log";
        try {
            FileWriter writer = new FileWriter(filePath, true);
            LocalDateTime now = LocalDateTime.now();
            writer.write("["+now+"] ["+directory+":"+file+":"+function+"] ["+errorTitle+"] - "+errorMessage);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateRandomValue(String prefix)
    {
        Date date = new Date();
        String dateStr = date.toString();
        int min = 10000000;
        int max = 99999999;
        Random R = new Random();
        int randomNumber = R.nextInt(max + 1 - min) + min;
        String finalRandomValue = prefix + doMD5(Integer.toString(randomNumber) + dateStr);

        return finalRandomValue;
    }

    public String doMD5(String plainText)
    {
        String md5Hex = DigestUtils
                .md5Hex(plainText).toUpperCase();

        return md5Hex;
    }

    public void logEvents(String directory, String file, String function, String errorTitle, String errorMessage) {
        String filePath = "/opt/dssfiles/dssevents.log";
        try {
            FileWriter writer = new FileWriter(filePath, true);
            LocalDateTime now = LocalDateTime.now();
            writer.write("TIME: "+now+" - DIR: "+directory+" - FILE: "+file+" -"+function+"\n---TITLE: "+errorTitle+"\n---MESSAGE: "+errorMessage);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSessionToken(String domain, String appKey, String secretKey) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String apiEndPoint = "/api/hpcgw/v1/token/get";
        String url = domain + apiEndPoint;

        RestTemplate restTemplate = new RestTemplate();

        String requestBody = "{\"appKey\":\"" + appKey + "\",\"secretKey\":\"" + secretKey + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody().toString());
        String accessToken = jsonNode.get("data").get("accessToken").asText();

        return accessToken;
    }

    public Map<String, String> generatePassword() {
        Map<String, String> map = new HashMap<>();

        try {
            Random random = new Random();
            int randomNumber = 1000 + random.nextInt(9000);
            String password = String.valueOf(randomNumber);
            String salt = BCrypt.gensalt();
            String hashedPassword = BCrypt.hashpw(password, salt);
            map.put("password", password);
            map.put("hashedPassword", hashedPassword);
            map.put("status", "SUCCESS");
            return map;
        } catch (Exception e){
            logErrors("dssv6", "Common", "generateHashedPassword", "Generate Hashed Password", e.toString());
            map.put("status", "FAIL: " + e.toString());
            return map;
        }
    }
}
