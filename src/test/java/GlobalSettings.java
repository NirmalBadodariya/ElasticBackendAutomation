import io.restassured.path.json.JsonPath;
import net.minidev.json.parser.ParseException;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GlobalSettings {

    Utility utilObj = new Utility();
    @Test
    public void synonyms() throws ParseException {
        Map<String, String> headers = new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("Accept", "*/*");
                put("Authorization", "Bearer " + utilObj.getToken());
            }

        };

        String result = given()
                .headers(headers)
                .get("search-admin/api/synonyms/synonymsList")
                .andReturn().asString();
        System.out.println(result);
        JsonPath j = new JsonPath(result);
        String attributesData = j.getString("result.values.search_terms");
        System.out.println(attributesData);
    }
}
