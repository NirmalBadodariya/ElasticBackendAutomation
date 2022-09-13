import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

public class ElasticDemo {
    private static final String id = "id";
    private static final String date ="published_at";



    public String getId() {
        return id;
    }


    public String getDate() {
        return date;
    }


}
