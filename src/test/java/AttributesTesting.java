import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.minidev.json.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
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

        String result = given()
                .headers(headers)
                .get("search-admin/api/projects/192233b4-1bd3-44d0-a57e-9a6c0425e58e/schemasDetails/94d299b6-2b42-4c4f-a604-ac9453bdf27f")
//				.then()
//				.assertThat()
//				.statusCode(HttpStatus.SC_OK)
//				.log().all();
                .andReturn().asString();

        System.out.println("Project List : " + result);
        String type = "";
        try {

            JSONObject jsonObject = new JSONObject(result);
            JSONObject schema = jsonObject.getJSONObject("result");

            Iterator<Object> iterator = schema.getJSONArray("attributes").iterator();

            JsonPath jsonPathEvaluator = new JsonPath(schema.toString());
            List allFields = new ArrayList();
            int count=0;
            while (iterator.hasNext()) {

                JSONObject obj = (JSONObject) iterator.next();

                for (String key : obj.keySet()) {
                    allFields.add(key)
                    ;
                }

                System.out.println("Keys are: " + allFields);

                for (int i = 0; i < allFields.size(); i++) {
                    System.out.println("all : "+allFields.get(i));
                    ArrayList getData = jsonPathEvaluator.getJsonObject("attributes." + allFields.get(i));

                        String currentData = "";
                        if (null != getData.get(count)) {
                            currentData = getData.get(count).toString();
                        }

                    if (allFields.get(i).equals("name")) {
                        if (!utilObj.getProjectName(currentData)) {
                            Assert.assertEquals(false, true, "Invalid Project Name");
                        }
                    }
                    if (allFields.get(i).equals("type")) {
                        type = currentData;
                        System.out.println(type);
                        if (!utilObj.getType(currentData)) {
                            Assert.assertEquals(false, true, "Invalid Type");
                        }
                    }

                    if (allFields.get(i).equals("preview_type")) {
                        if (!utilObj.getPreviewType(currentData)) {
                            Assert.assertEquals(false, true, "Invalid Type");
                        }
                    }

                    if (allFields.get(i).equals("is_filterable")) {
                        if (type.equals("OBJECT") && currentData.equals("true")){
                            Assert.assertEquals(false, true, "Invalid for type object.");
                        }

                    }

                    if (allFields.get(i).equals("type_ahead")) {
                        if (!(type.equals("TEXT")) && currentData.equals("true")){
                            Assert.assertEquals(false, true, "should not be true except for text.");
                        }

                    }

                    if (allFields.get(i).equals("copy_to")) {
                        if (!(type.equals("TEXT")) && currentData.equals("true")){
                            Assert.assertEquals(false, true, "should not be true except for text.");
                        }
                       else if (!(type.equals("KEYWORD")) && currentData.equals("true")){
                            Assert.assertEquals(false, true, "should not be true except for keyword.");
                        }

                    }
                    if (allFields.get(i).equals("is_searchable")) {
                        if ((!(type.equals("TEXT")) && ! (type.equals("KEYWORD")) ) && currentData.equals("true")){
                            Assert.assertEquals(false, true, "should not be true except for text or keyword.");
                        }

                    }

                }
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}