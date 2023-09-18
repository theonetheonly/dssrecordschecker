package com.sgasecurity.dssrecordschecker;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.Random;

public class CommonFunctions {


    String error_msg ="";

    public   CommonFunctions()
    {

    }

    public void printToScreen(String STR)
    {
        System.out.println(STR);
    }

    public String getDayOfWeek(Date date) {
        try {
            Locale bLocale = new Locale.Builder().setLanguage("en").setRegion("US").build();
            DateFormat formatter = new SimpleDateFormat("EEEE", bLocale);
            return formatter.format(date);
        }
        catch (Exception EX)
        {
            return "";
        }

    }

    public String stripDoubleQuotes(String string)
    {
        if (string.length() >= 2 && string.charAt(0) == '"' && string.charAt(string.length() - 1) == '"')
        {
            string = string.substring(1, string.length() - 1);
        }
        return  string;
    }


    public String drawModal(String modal_id, String modal_title, String modal_content_id, String js_function_action,String action_button_title)
    {
        String modalhtml = "<div class=\"modal fade\" id=\""+modal_id+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
                "    <div class=\"modal-dialog\" role=\"document\">\n" +
                "        <div class=\"modal-content\">\n" +
                "            <div class=\"modal-header\">\n" +
                "                <h5 class=\"modal-title\" id=\"modal_title\">"+modal_title+"</h5>\n" +
                "                <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                "                    <span aria-hidden=\"true\">&times;</span>\n" +
                "                </button>\n" +
                "            </div>\n" +
                "            <div class=\"modal-body\" id=\""+modal_content_id+"\">\n" +
                "                ...\n" +
                "            </div>\n" +
                "            <div class=\"modal-footer\">\n" +
                "                <button  type=\"button\" class=\"btn btn-primary\" onclick=\""+js_function_action+"\">"+action_button_title+"</button>\n" +
                "                <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Close</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";

        return modalhtml;
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    public  String md5gen(String rawtext)
    {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(rawtext.getBytes());
            byte[] digest = md.digest();
            String myHash = bytesToHex(digest);// , StandardCharsets.UTF_8);

            return myHash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String base64EncodeEx(String stringtoencode) {
        try {
            return   Base64.getEncoder().encodeToString(stringtoencode.getBytes());
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public String base64DecodeEx(String stringtodecode)
    {
        try{
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(stringtodecode);

            String res = new String(bytes);
            return  res;
        }
        catch (Exception ex)
        {
            return null;
        }
    }
    public String getTommorowString() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date today = new java.util.Date();
            Date formattedDate = java.sql.Date.valueOf(df.format(new java.util.Date(today.getTime() + (60 * 60 * 24 * 1000))));
            return formattedDate.toString();
        }
        catch (Exception ex)
        {
            error_msg = "Error: function getTommorowString() in CommonFunctions "+ex.getMessage();
            System.out.println(error_msg);
            return error_msg;
        }
    }

    public Date getToday()
    {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date today = new java.util.Date();
            Date formattedDate = java.sql.Date.valueOf(df.format(new java.util.Date(today.getTime())));
            return formattedDate;
        }
        catch (Exception ex)
        {
            error_msg = "Error: function getTommorow() in CommonFunctions "+ex.getMessage();
            System.out.println(error_msg);
            return null;
        }

    }

    public String generateBreadCrumb(String Item1,String Item2, String Item1_link)
    {
        String autobreadCrumb  = " <ol class=\"breadcrumb\">\n" +
                "            <li class=\"breadcrumb-item\"><a href=\""+Item1_link+"\">"+Item1+"</a></li>\n" +
                "            <li class=\"breadcrumb-item active\" aria-current=\"page\">"+Item2+"</li>\n" +
                "        </ol>\n" +
                " ";
            return  autobreadCrumb;
    }

    public Date getTommorow() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date today = new java.util.Date();
            Date formattedDate = java.sql.Date.valueOf(df.format(new java.util.Date(today.getTime() + (60 * 60 * 24 * 1000))));
            return formattedDate;
        }
        catch (Exception ex)
        {
            error_msg = "Error: function getTommorow() in CommonFunctions "+ex.getMessage();
            System.out.println(error_msg);
            return null;
        }
    }


    public String getCurrentDateTime(String pattern)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }



    public HttpSession currentSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
         return attr.getRequest().getSession(true); // true == allow create
    }

    public String getConfigValue(String config_name)
    {


        try{
        String result ="";
            switch (config_name)
            {
                case "SMS_API_URL":
                    result=  "https://comms.sgasecurity.com/sgasms/fromthirdparty_citapp.php?";

                    break;
                default:
                    result ="No selection made";
                    break;


            }

        return  result;

        }
        catch (Exception ex)
        {
            error_msg = "Error: (CommonFunctions) (getConfigValue)\n"+ex.toString();
            System.out.println(error_msg);
            return error_msg;
        }

    }

    public static String generatePassword(){

        Random random = new Random();

        int specialChar_1 = random.nextInt(47-33) + 33;
        int specialChar_2 = random.nextInt(47-33) + 33;

        int number_1 = random.nextInt(9);
        int number_2 = random.nextInt(9);

        int uppercase = random.nextInt(90-65) + 65;

        int lowercase_1 = random.nextInt(122-97) + 97;


        return String.format("%s%s%s%s%s%s", (char) specialChar_1, (char) specialChar_2, number_1, number_2, (char) uppercase, (char) lowercase_1);
    }

    public static String generatePasswordResetCode(){

        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println(generatedString);

        return generatedString;
    }

    public static String generatePasswordMobile(){

        Random random = new Random();


        int number_1 = random.nextInt(9);
        int number_2 = random.nextInt(9);
        int number_3 = random.nextInt(9);
        int number_4 = random.nextInt(9);
        int number_5 = random.nextInt(9);
        int number_6 = random.nextInt(9);

        StringBuilder sb  = new StringBuilder();

        sb.append(Integer.toString(number_1));
        sb.append(Integer.toString(number_2));
        sb.append(Integer.toString(number_3));
        sb.append(Integer.toString(number_4));
        sb.append(Integer.toString(number_5));
        sb.append(Integer.toString(number_6));

            return  sb.toString();
//        return String.format("%s%s%s%s%s%s", (char) specialChar_1, (char) specialChar_2, number_1, number_2, (char) uppercase, (char) lowercase_1);
    }

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            filePath.toFile().setReadable(true);

        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static void saveReportFile(String uploadDir, String fileName,
                                File file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        InputStream inputStream = new FileInputStream(file);
        try (inputStream) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            filePath.toFile().setReadable(true);

        } catch (IOException ioe) {
            throw new IOException("Could not save report file: " + fileName, ioe);
        }
    }
/*
    public static List<String> getAllUserRoles(SystemUsers systemUser){

        List<String> userRolesList = new ArrayList<>(List.of(systemUser.getUserSubRole().trim().split("\\s*,\\s*")));

        userRolesList.add(systemUser.getUserRole());

        System.out.println("User Roles: " + userRolesList);
        System.out.println("User: " + systemUser);

        return userRolesList;

    }
*/
    public static String generateMessageRandomStamp() {
        try {
            Random rand = new Random();
            int max_val = 1000;
            int min_val = 10;
            int random_index = rand.nextInt(max_val - min_val);
            String SALTCHARS = random_index + "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            while (salt.length() < 18) { // length of the random string.
                int index = (int) (rand.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();
            return saltStr;
        } catch (Exception ex) {
            return "TYH456BOJ767ILEMIK";
        }
    }

}
