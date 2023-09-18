package com.sgasecurity.dssrecordschecker;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class NetworkActivity implements Serializable {

    private static final long serialVersionUID =1L;
    //CommonFunctions myCommon  = new CommonFunctions();
    Common common = null;

    public NetworkActivity()
    {

    }

    public String smsExecuteURL(String urlString , Map<String, Object> params) throws IOException {
        common = new Common();
        try {
            URL url = new URL(urlString);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0; )
                sb.append((char) c);
            String response = sb.toString();
            return response;
        }
        catch (Exception ex2)
        {
            common.logErrors("dssv6", "NetworkActivity", "smsExecuteURL", "SMS URL", ex2.toString());
            return ex2.getMessage();
        }
    }

    public String executeURL(String urlString , Map<String, Object> params) throws IOException {
        common = new Common();
        try {
            URL url = new URL(urlString);
//                    Map<String,Object> params = new LinkedHashMap<>();
/*                    params.put("name", "Freddie the Fish");
                    params.put("email", "fishie@seamail.example.com");
                    params.put("reply_to_thread", 10394);
                    params.put("message", "Shark attacks in Botany Bay have gotten out of control. We need more defensive dolphins to protect the schools here, but Mayor Porpoise is too busy stuffing his snout with lobsters. He's so shellfish.");
*/

            //myCommon.printToScreen("EXECUTING URL: "+urlString+"\n");
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            //myCommon.printToScreen("\n\n\nPOST DATA : "+postData.toString()+"\n");
            //myCommon.printToScreen("\n\n\nFULL URL + POST DATA : "+urlString +postData.toString()+"\n");
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0; )
                sb.append((char) c);
            String response = sb.toString();
            //myCommon.printToScreen("\nRESPONSE FROM NETWORK:\n-----\n"+response+"\n-----\n");
            return response;
        }
        catch (Exception ex2)
        {
            common.logErrors("dssv6", "NetworkActivity", "executeURL", "Executing URL", ex2.toString());
            return ex2.getMessage();
        }
    }
}
