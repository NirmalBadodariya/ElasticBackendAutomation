import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.minidev.json.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;

public class AttributesTesting {
    Utility utilObj = new Utility();

    @Test
    public void checkAttributes() throws ParseException {
        Map<String, String> headers = new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("Accept", "*/*");
                put("Authorization", "Bearer " + utilObj.getToken());
            }

        };

        Response result = given()
                .headers(headers)
                .get("search-admin/api/projects/b989a5e8-7095-4a0a-a57d-07ee46ba83de/schemas/f0eef600-c8f6-4cda-9cb5-7d50d711708d")
                ;
        System.out.println(result.asString());

        JsonPath j = new JsonPath(result.asString());
        String name = j.getString("result.name");
        String alias = j.getString("result.alias");

        if (name.equals(alias)){
            Assert.assertEquals(true, false, "name and alias name can't be same");
        }
        String attributesData = j.getString("result.attributes.elastic_si_searchable_fields");
        System.out.println(attributesData);
//        JsonPath j1 = new JsonPath(attributesData);
//        String attributesData1 = j1.getString("elastic_si_searchable_fields");
//        System.out.println("sldjnfsdf:   "+attributesData1);


    }
}
