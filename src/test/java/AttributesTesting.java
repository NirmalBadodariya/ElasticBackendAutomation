import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
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
                .get("search-admin/api/projects/d4f33a63-6482-418f-b556-04f514836806/schemasDetails/7f34859a-9ca0-4c64-b141-92ba82ee6ee1")
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

                    if(allFields.get(i).equals("nested_attributes")){
                        if (currentData != "[]")
                            System.out.println(currentData);
                        JSONObject jsonObject1 = new JSONObject(result);
                    }


                    if (allFields.get(i).equals("name")) {
                        if (!utilObj.getProjectName(currentData)) {
                            Assert.fail("Invalid Project Name");
                        }
                    }
                    if (allFields.get(i).equals("type")) {
                        type = currentData;
                        System.out.println(type);
                        if (!utilObj.getType(currentData)) {
                            Assert.fail("Invalid Type");

                        }
                    }

                    if (allFields.get(i).equals("preview_type")) {
                        if (!utilObj.getPreviewType(currentData)) {
                            Assert.fail("Invalid Type");
                        }
                    }


//                    checking boolean values for all the fields .

                    if (allFields.get(i).equals("is_filterable")) {
                        if (type.equals("OBJECT") && currentData.equals("true")){
                            Assert.fail("Invalid for type object.");
                        }

                    }

                    if (allFields.get(i).equals("type_ahead")) {
                        if (!(type.equals("TEXT")) && currentData.equals("true")){
                            Assert.fail("should not be true except for text.");
                        }

                    }

                    if (allFields.get(i).equals("copy_to")) {
                        if (!(type.equals("TEXT")) && currentData.equals("true")){
                            Assert.fail("should not be true except for text.");
                        }
                       else if (!(type.equals("KEYWORD")) && currentData.equals("true")){
                            Assert.fail("should not be true except for keyword.");
                        }

                    }
                    if (allFields.get(i).equals("is_searchable")) {
                        if ((!(type.equals("TEXT")) && ! (type.equals("KEYWORD")) ) && currentData.equals("true")){
                            Assert.fail("should not be true except for text or keyword.");
                        }

                    }

                }
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void putApiTest() throws ParseException {
        RequestSpecification requestSpecification;
        Response response;
        ValidatableResponse validatableResponse;

//        Map<String, String> headers = new HashMap<String, String>() {
//            private static final long serialVersionUID = 1L;
//
//            {
//                put("Accept", "*/*");
//                put("Authorization", "Bearer " + utilObj.getToken());
//            }
//
//        };
        RequestSpecification httpRequest = RestAssured.given().header("Authorization", "Bearer " + utilObj.getToken()).header("Content-Type", "application/json");




        String json1 = "{\n" +
                "        \"id\": \"7f34859a-9ca0-4c64-b141-92ba82ee6ee1\",\n" +
                "       \"name\": \"best_buy_sample_sep13_lang_1\",\n" +
                "        \"alias\": \"best_buy_sample_sep13_lang_1_alias_another\",\n" +
                "        \"attributes\": [\n" +
                "            {\n" +
                "                \"id\": \"e564dbfc-c24b-41a3-93b4-cc2774631e1f\",\n" +
                "                \"name\": \"@timestamp\",\n" +
                "                \"type\": \"DATE\",\n" +
                "                \"analyzer\": \"NONE\",\n" +
                "                \"mode\": \"NONE\",\n" +
                "                \"published\": true,\n" +
                "                \"enabled\": true,\n" +
                "                \"absoluteName\": \"@timestamp\",\n" +
                "                \"dims\": null,\n" +
                "                \"display_name\": \"@timestamp\",\n" +
                "                \"source_type\": \"DATE\",\n" +
                "                \"nested_attributes\": [],\n" +
                "                \"preview_type\": \"NONE\",\n" +
                "                \"is_searchable\": true,\n" +
                "                \"is_filterable\": true,\n" +
                "                \"elasticsi_name\": \"@timestamp\",\n" +
                "                \"keyword_field\": \"\",\n" +
                "                \"imported_analyzer\": false,\n" +
                "                \"is_imported\": true,\n" +
                "                \"elastic_si_searchable_fields\": null,\n" +
                "                \"elastic_si_searchable_base_fields\": null,\n" +
                "                \"elastic_si_searchable_intragram_fields\": null,\n" +
                "                \"elastic_si_auto_complete_fields\": null,\n" +
                "                \"elastic_si_auto_complete_synonym_fields\": null,\n" +
                "                \"elastic_si_auto_complete_delimiter_fields\": null,\n" +
                "                \"elastic_si_searchable_highlight_fields\": null,\n" +
                "                \"elastic_si_searchable_stem_fields\": null,\n" +
                "                \"elastic_si_searchable_joined_fields\": null,\n" +
                "                \"copy_to\": true,\n" +
                "                \"is_vectorized\": false,\n" +
                "                \"type_ahead\": true,\n" +
                "                \"nlp_enabled\": false,\n" +
                "                \"index_analyzer\": null,\n" +
                "                \"search_relevance\": null\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"956fb9e0-8026-4eaf-9f48-7747f4c75595\",\n" +
                "                \"name\": \"@name\",\n" +
                "                \"type\": \"TEXT\",\n" +
                "                \"analyzer\": \"NONE\",\n" +
                "                \"mode\": \"NONE\",\n" +
                "                \"published\": true,\n" +
                "                \"enabled\": true,\n" +
                "                \"absoluteName\": \"@version\",\n" +
                "                \"dims\": null,\n" +
                "                \"display_name\": \"@version\",\n" +
                "                \"source_type\": \"TEXT\",\n" +
                "                \"nested_attributes\": [],\n" +
                "                \"preview_type\": \"NONE\",\n" +
                "                \"is_searchable\": true,\n" +
                "                \"is_filterable\": false,\n" +
                "                \"elasticsi_name\": \"@version\",\n" +
                "                \"keyword_field\": \"@version.elastic_si_keyword\",\n" +
                "                \"imported_analyzer\": false,\n" +
                "                \"is_imported\": true,\n" +
                "                \"elastic_si_searchable_fields\": null,\n" +
                "                \"elastic_si_searchable_base_fields\": null,\n" +
                "                \"elastic_si_searchable_intragram_fields\": null,\n" +
                "                \"elastic_si_auto_complete_fields\": null,\n" +
                "                \"elastic_si_auto_complete_synonym_fields\": null,\n" +
                "                \"elastic_si_auto_complete_delimiter_fields\": null,\n" +
                "                \"elastic_si_searchable_highlight_fields\": null,\n" +
                "                \"elastic_si_searchable_stem_fields\": null,\n" +
                "                \"elastic_si_searchable_joined_fields\": null,\n" +
                "                \"copy_to\": false,\n" +
                "                \"is_vectorized\": false,\n" +
                "                \"type_ahead\": false,\n" +
                "                \"nlp_enabled\": false,\n" +
                "                \"index_analyzer\": null,\n" +
                "                \"search_relevance\": null\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"d8854465-bb98-4080-a3a3-0306025a9d11\",\n" +
                "                \"name\": \"category\",\n" +
                "                \"type\": \"OBJECT\",\n" +
                "                \"analyzer\": \"NONE\",\n" +
                "                \"mode\": \"NONE\",\n" +
                "                \"published\": true,\n" +
                "                \"enabled\": true,\n" +
                "                \"absoluteName\": \"category\",\n" +
                "                \"dims\": null,\n" +
                "                \"display_name\": \"category\",\n" +
                "                \"source_type\": \"OBJECT\",\n" +
                "                \"nested_attributes\": [\n" +
                "                    {\n" +
                "                        \"id\": \"6d28c239-cc45-40b1-934b-453ece87160b\",\n" +
                "                        \"name\": \"id\",\n" +
                "                        \"type\": \"OBJECT\",\n" +
                "                        \"analyzer\": \"NONE\",\n" +
                "                        \"mode\": \"NONE\",\n" +
                "                        \"published\": true,\n" +
                "                        \"enabled\": true,\n" +
                "                        \"absoluteName\": \"category.id\",\n" +
                "                        \"dims\": null,\n" +
                "                        \"display_name\": \"id\",\n" +
                "                        \"source_type\": \"TEXT\",\n" +
                "                        \"nested_attributes\": [],\n" +
                "                        \"preview_type\": \"NONE\",\n" +
                "                        \"is_searchable\": true,\n" +
                "                        \"is_filterable\": true,\n" +
                "                        \"elasticsi_name\": \"category.id\",\n" +
                "                        \"keyword_field\": \"category.id.elastic_si_keyword\",\n" +
                "                        \"imported_analyzer\": false,\n" +
                "                        \"is_imported\": true,\n" +
                "                        \"elastic_si_searchable_fields\": null,\n" +
                "                        \"elastic_si_searchable_base_fields\": null,\n" +
                "                        \"elastic_si_searchable_intragram_fields\": null,\n" +
                "                        \"elastic_si_auto_complete_fields\": null,\n" +
                "                        \"elastic_si_auto_complete_synonym_fields\": null,\n" +
                "                        \"elastic_si_auto_complete_delimiter_fields\": null,\n" +
                "                        \"elastic_si_searchable_highlight_fields\": null,\n" +
                "                        \"elastic_si_searchable_stem_fields\": null,\n" +
                "                        \"elastic_si_searchable_joined_fields\": null,\n" +
                "                        \"copy_to\": true,\n" +
                "                        \"is_vectorized\": false,\n" +
                "                        \"type_ahead\": true,\n" +
                "                        \"nlp_enabled\": false,\n" +
                "                        \"index_analyzer\": null,\n" +
                "                        \"search_relevance\": null\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": \"1e1142d6-79e9-4d97-ab7e-7b81093e2efc\",\n" +
                "                        \"name\": \"name\",\n" +
                "                        \"type\": \"TEXT\",\n" +
                "                        \"analyzer\": \"NONE\",\n" +
                "                        \"mode\": \"NONE\",\n" +
                "                        \"published\": true,\n" +
                "                        \"enabled\": true,\n" +
                "                        \"absoluteName\": \"category.name\",\n" +
                "                        \"dims\": null,\n" +
                "                        \"display_name\": \"name\",\n" +
                "                        \"source_type\": \"TEXT\",\n" +
                "                        \"nested_attributes\": [],\n" +
                "                        \"preview_type\": \"NONE\",\n" +
                "                        \"is_searchable\": false,\n" +
                "                        \"is_filterable\": false,\n" +
                "                        \"elasticsi_name\": \"category.name\",\n" +
                "                        \"keyword_field\": \"category.name.elastic_si_keyword\",\n" +
                "                        \"imported_analyzer\": false,\n" +
                "                        \"is_imported\": true,\n" +
                "                        \"elastic_si_searchable_fields\": null,\n" +
                "                        \"elastic_si_searchable_base_fields\": null,\n" +
                "                        \"elastic_si_searchable_intragram_fields\": null,\n" +
                "                        \"elastic_si_auto_complete_fields\": null,\n" +
                "                        \"elastic_si_auto_complete_synonym_fields\": null,\n" +
                "                        \"elastic_si_auto_complete_delimiter_fields\": null,\n" +
                "                        \"elastic_si_searchable_highlight_fields\": null,\n" +
                "                        \"elastic_si_searchable_stem_fields\": null,\n" +
                "                        \"elastic_si_searchable_joined_fields\": null,\n" +
                "                        \"copy_to\": false,\n" +
                "                        \"is_vectorized\": false,\n" +
                "                        \"type_ahead\": false,\n" +
                "                        \"nlp_enabled\": false,\n" +
                "                        \"index_analyzer\": null,\n" +
                "                        \"search_relevance\": null\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"preview_type\": \"NONE\",\n" +
                "                \"is_searchable\": true,\n" +
                "                \"is_filterable\": true,\n" +
                "                \"elasticsi_name\": \"objectname\",\n" +
                "                \"keyword_field\": \"\",\n" +
                "                \"imported_analyzer\": false,\n" +
                "                \"is_imported\": true,\n" +
                "                \"elastic_si_searchable_fields\": null,\n" +
                "                \"elastic_si_searchable_base_fields\": null,\n" +
                "                \"elastic_si_searchable_intragram_fields\": null,\n" +
                "                \"elastic_si_auto_complete_fields\": null,\n" +
                "                \"elastic_si_auto_complete_synonym_fields\": null,\n" +
                "                \"elastic_si_auto_complete_delimiter_fields\": null,\n" +
                "                \"elastic_si_searchable_highlight_fields\": null,\n" +
                "                \"elastic_si_searchable_stem_fields\": null,\n" +
                "                \"elastic_si_searchable_joined_fields\": null,\n" +
                "                \"copy_to\": true,\n" +
                "                \"is_vectorized\": false,\n" +
                "                \"type_ahead\": true,\n" +
                "                \"nlp_enabled\": false,\n" +
                "                \"index_analyzer\": null,\n" +
                "                \"search_relevance\": null\n" +
                "            }\n" +
                "        ]\n" +
                "}";
        httpRequest.contentType(ContentType.JSON);

        // Adding body as string
       response =  httpRequest.body(json1).put("https://search-admin-dev-mamb5phriq-uc.a.run.app/search-admin/api/projects/d4f33a63-6482-418f-b556-04f514836806/schemas/7f34859a-9ca0-4c64-b141-92ba82ee6ee1");


        // Let's print response body.
        String responseString = response.prettyPrint();
        validatableResponse = response.then();

        // Get status code
//        validatableResponse.statusCode(200);
    }


    @Test
    public void synonymsOfProject() throws ParseException {
        Map<String, String> headers = new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("Accept", "*/*");
                put("Authorization", "Bearer " + utilObj.getToken());
            }

        };

        String result = given()
                .headers(headers)
                .get("https://search-admin-dev-mamb5phriq-uc.a.run.app/search-admin/api/projects/2b86469a-c8b2-4614-81d1-ee3c49323fa8/synonyms")
                .andReturn().asString();

        System.out.println(result);
        JsonPath j = new JsonPath(result);
        String attributesData = j.getString("result.values.search_terms");
        System.out.println(attributesData);

    }

    @Test
    public void filtersOfProject() throws ParseException {
        Map<String, String> headers = new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("Accept", "*/*");
                put("Authorization", "Bearer " + utilObj.getToken());
            }

        };

        String result = given()
                .headers(headers)
                .get("https://search-admin-dev-mamb5phriq-uc.a.run.app/search-admin/api/projects/2b86469a-c8b2-4614-81d1-ee3c49323fa8/filters")
                .andReturn().asString();

        System.out.println(result);
        JsonPath j = new JsonPath(result);

        String resultData = j.getString("result");

        if (!resultData.equals("[]")) {
            String resultName = j.getString("result.name");
            System.out.println(resultName);
            if (resultName.equals("[]")){
                Assert.fail("name should not be empty");
            }
            String resultType = j.getString("result.type");
            System.out.println(resultType);
            if (!(utilObj.filterType(resultType))) {
                Assert.fail("no matching data found");
            }
        }
    }
}