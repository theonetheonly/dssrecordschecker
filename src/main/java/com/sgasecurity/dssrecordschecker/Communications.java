package com.sgasecurity.dssrecordschecker;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Communications {


    public String SMS_API_URL = "";
    public String error_msg = "";
    CommonFunctions myCommon = new CommonFunctions();

    public void sendSMS(String mobilephone, String message) throws IOException {


        try {
            this.SMS_API_URL = myCommon.getConfigValue("SMS_API_URL");
            String myurl = this.SMS_API_URL; //"http://172.105.99.106/smsapi/sendsms.php?";
            //  myCommon.printToScreen("SMS URL: "+ myurl);

            Map<String, Object> post_data = new LinkedHashMap<>();
            post_data.put("mobile", mobilephone);
            post_data.put("message", message);
            post_data.put("gatewayip", "192.168.0.1");
            post_data.put("accesscode", "4321");
            NetworkActivity networkActivity = new NetworkActivity();
            networkActivity.executeURL(myurl, post_data);
            // this.logComms(mobilephone, message, "sms");

        } catch (Exception ex) {
            error_msg = "Error: (CommonFunctions) (getConfigValue)\n" + ex.toString();
            System.out.println(error_msg);
            return;

        }
    }

    public void sendEmail(String email, String subject, String message, String html, String cc)
    {
        try{
            //myCommon.printToScreen("\n\n\n=================================================\n\n\n");
            myCommon.printToScreen("SENDING EMAIL");
            String  myurl = "https://comms.sgasecurity.com/sgasms/citsendmail.php";
            //myCommon.printToScreen("EMAIL URL: "+ myurl);

            Map<String, Object> post_data =  new LinkedHashMap<>();
            post_data.put("email",email);
            post_data.put("subject",subject);
            post_data.put("message", message);
            post_data.put("html",html);
            post_data.put("accesskey","sga2022now");

            NetworkActivity networkActivity = new NetworkActivity();
            networkActivity.executeURL(myurl, post_data);
//            this.logComms(email, message, "email");

            //myCommon.printToScreen("\n\n\n=================================================\n\n\n");

        }
        catch (Exception ex)
        {
            myCommon.printToScreen("Error Sending Email:\n"+ ex.toString());
        }
    }

    //
    public void sendEmailWithAttachments(String email, String subject, String message, String html, String cc)
    {
        try{
            //myCommon.printToScreen("\n\n\n=================================================\n\n\n");
            myCommon.printToScreen("SENDING EMAIL");
            String  myurl = "https://comms.sgasecurity.com/sgasms/citsendmail.php";

            Map<String, Object> post_data =  new LinkedHashMap<>();
            post_data.put("email",email);
            post_data.put("subject",subject);
            post_data.put("message", message);
            post_data.put("html",html);
            post_data.put("accesskey","sga2022now");

            NetworkActivity networkActivity = new NetworkActivity();
            networkActivity.executeURL(myurl, post_data);

        }
        catch (Exception ex)
        {
            myCommon.printToScreen("Error Sending Email:\n"+ ex.toString());
        }
    }


}

