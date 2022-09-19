import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    private static final String id = "id";
    private static final String date ="published_at";

    private static final String email ="admin@weblinktechs.com";
    private static final String pass ="!adg@u^s5FE43q";
    private static final String projectId ="b989a5e8-7095-4a0a-a57d-07ee46ba83de";

    public String getId() {
        return id;
    }
    public String getProjectId() {
        return projectId;
    }

    public String getDate() {
        return date;
    }
    public String getEmail() {
        return email;
    }
    public String getPass() {
        return pass;
    }

//    Generating token for authentication
    public String getToken() throws ParseException {

        RestAssured.baseURI ="https://search-admin-dev-mamb5phriq-uc.a.run.app";

        RequestSpecification request=RestAssured.given();

        String payload = "{\n"
                + "    \"email\": \"admin@weblinktechs.com\",\n"
                + "    \"password\": \"!adg@u^s5FE43q\"\n"
                + "}";

        request.header("content-type","application/json;charset=UTF-8");
        request.header("accept","application/json, text/plain, */*");

        Response resFromGeneratedToken = request.body(payload).post("/authenticate");

        String jsonString = resFromGeneratedToken.getBody().asString();


        System.out.println("JSON String is :"+ jsonString);
        JSONObject jsonObj = new JSONObject(jsonString);
        String token = jsonObj.getJSONObject("result").getString("token");
        return token;
    }


    public boolean version(String s){

        Pattern pattern = Pattern.compile("^[0-9]{1}.[0-9]{1}.[0-9]{1}$");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()){
            return true;
        }
        return false;
    }

    public boolean getProjectStatus(String s){

        ArrayList<String> list = new ArrayList<String>();
        list.add("PUBLISHED");
        list.add("FAILED");
        list.add("ARCHIVED");
        list.add("DISCONNECTED");
        list.add("DRAFT");
        list.add("REPUBLISH");
        list.add("PUBLISHING");

        System.out.println("Get Project Status is: ");
        for (String element : list){
            if (s.equals(element)){
                System.out.println(element);
                return true;
            }
        }
        return false;
    }

    public boolean getengineType(String s){
        ArrayList<String> list = new ArrayList<String>();
        list.add("Precision Based Engine");
        list.add("Custom Relevance Engine");
        list.add("Ranking Based Engine V2");
        for (String element : list){
            if (s.equals(element)){
                System.out.println(element);
                return true;
            }
        }
        return false;
    }

    public boolean getSearchType(String s){

        ArrayList<String> list = new ArrayList<String>();
        list.add("SINGLE");
        list.add("META");

        for (String element : list){
            if (s.equals(element)){
                System.out.println(element);
                return true;
            }
        }
        return false;
    }

    public boolean getSupportEngine(String s){

        ArrayList<String> list = new ArrayList<String>();
        list.add("ELASTIC");
        list.add("AWS");
        list.add("OPEN SEARCH");
        list.add("CHAOS");
        list.add("SOLR");

        for (String element : list){
            if (s.equals(element)){
                System.out.println(element);
                return true;
            }
        }
        return false;
    }

    public boolean getIndexMode(String s){

        ArrayList<String> list = new ArrayList<String>();
        list.add("BASIC");
        list.add("ADVANCED");
        list.add("PRECISION");
        for (String element : list){
            if (s.equals(element)){
                System.out.println(element);
                return true;
            }
        }
        return false;
    }


    public boolean getProjectName(String dataOfFirst) {
        boolean result = dataOfFirst.matches("[a-zA-Z@]+");
//        System.out.println("Original String : " + dataOfFirst + "and result is :"+ result);
        return result;
    }

    public boolean getType(String dataOfFirst) {

        ArrayList<String> list = new ArrayList<String>();
        list.add("TEXT");
        list.add("KEYWORD");
        list.add("LONG");
        list.add("INTEGER");
        list.add("SHORT");
        list.add("BYTE");
        list.add("DOUBLE");
        list.add("FLOAT");
        list.add("HALF_FLOAT");
        list.add("DATE");
        list.add("DATE_NANOS");
        list.add("BOOLEAN");
        list.add("BINARY");
        list.add("OBJECT");
        list.add("NESTED");
        list.add("INTEGER_RANGE");
        list.add("FLOAT_RANGE");
        list.add("DOUBLE_RANGE");
        list.add("LONG_RANGE");
        list.add("DATE_RANGE");
        list.add("GEO_POINT");
        list.add("GEO_SHAPE");

        for (String element : list) {
            if (dataOfFirst.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public boolean getPreviewType(String dataOfFirst) {

        ArrayList<String> list = new ArrayList<String>();

        list.add("NONE");
        list.add("TITLE");
        list.add("TEXT");
        list.add("IMAGE");
        list.add("LINK");

        for (String element : list) {
            if (dataOfFirst.equals(element)) {
                return true;
            }
        }
        return false;
    }
}
