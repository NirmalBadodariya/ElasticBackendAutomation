import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.parser.ParseException;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ElasticSI_Project {


//	LOGIN APIS TESTING FOR FAKE CREDENTIALS'
	@Test

	public void fake_email(){
		RestAssured.baseURI ="https://search-admin-dev-mamb5phriq-uc.a.run.app";

		RequestSpecification request=RestAssured.given();

		String payload = "{\n"
				+ "    \"email\": \"Notadmin@weblinktechs.com\",\n"
				+ "    \"password\": \"!adg@u^s5FE43q\"\n"
				+ "}";
		request.header("content-type","application/json;charset=UTF-8");
		request.header("accept","application/json, text/plain, */*");

		Response resFromGenrateedToken = request.body(payload).post("/authenticate");
		int statusCode = resFromGenrateedToken.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode /*actual value*/, 400 /*expected value*/,
				"wrong email entered");

	}
	@Test
	public void fake_pass(){
		RestAssured.baseURI ="https://search-admin-dev-mamb5phriq-uc.a.run.app";

		RequestSpecification request=RestAssured.given();

		String payload = "{\n"
				+ "    \"email\": \"admin@weblinktechs.com\",\n"
				+ "    \"password\": \"fakepass\"\n"
				+ "}";
		request.header("content-type","application/json;charset=UTF-8");
		request.header("accept","application/json, text/plain, */*");

		Response resFromGenrateedToken = request.body(payload).post("/authenticate");
		int statusCode = resFromGenrateedToken.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode /*actual value*/, 400 /*expected value*/,
				"wrong password entered");

	}
	@Test
	public void fake_email_pass(){
		RestAssured.baseURI ="https://search-admin-dev-mamb5phriq-uc.a.run.app";

		RequestSpecification request=RestAssured.given();

		String payload = "{\n"
				+ "    \"email\": \"Notadmin@weblinktechs.com\",\n"
				+ "    \"password\": \"fakepass\"\n"
				+ "}";
		request.header("content-type","application/json;charset=UTF-8");
		request.header("accept","application/json, text/plain, */*");

		Response resFromGenrateedToken = request.body(payload).post("/authenticate");
		int statusCode = resFromGenrateedToken.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/,
				"wrong email and pass entered");

	}

//	LOGIN VALID AND GETTING TOKEN
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
	@Test
	public void getProjects() throws ParseException {
		RestAssured.baseURI ="https://search-admin-dev-mamb5phriq-uc.a.run.app";

		Map<String ,String> headers =new HashMap<String,String>(){
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}

		};

		given()
		.headers(headers)
		.get("search-admin/api/projects")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK).log().all();



	}

	@Test
	public void getProjectCheckDataType() throws ParseException, IOException {
		Map<String ,String> headers =new HashMap<String,String>(){
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}

		};

		String result = given()
				.headers(headers)
				.get("search-admin/api/projects")
				.andReturn().asString();
		System.out.println(result);

		JSONObject obj = new JSONObject(result);


		JsonPath jsonPathEvaluator = new JsonPath(result);
		List allFields = new ArrayList();

			JSONArray array = obj.getJSONArray("result");

			Iterator<Object> iterator = array.iterator();
			while(iterator.hasNext()){
				JSONObject jsonObject = (JSONObject) iterator.next();
				for(String key : jsonObject.keySet()){
					allFields.add(key);
				}
			}
		System.out.println(allFields);
		System.out.println(allFields.get(2));
		ArrayList getData = jsonPathEvaluator.getJsonObject("result."+allFields.get(2));
		String dataOfFirst = getData.get(0).toString();

		System.out.println(dataOfFirst);
		String numRegex   = ".*[0-9].*";
		String alphaRegex = ".*[a-z].*";
//		if (!(dataOfFirst.matches(numRegex) && dataOfFirst.matches(alphaRegex))) {
//			Assert.assertEquals(false, true,"doesn't meet the expected result");
//		}

		for(Object arr :getData)
		{
			if(arr!=null)
			{
				String dataType =arr.getClass().getSimpleName();
				System.out.println(arr + " " + arr.getClass().getSimpleName());
				if (!dataType.equals("String") && !dataType.equals("Boolean") ){
					Assert.assertEquals(true, false,"Not String");
				}
			}

		}



	}

	@Test
	public void getProjectCheckAvailabilityOfData() throws ParseException {
		Map<String ,String> headers =new HashMap<String,String>(){
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}

		};

		given()
				.headers(headers)
				.get("search-admin/api/projects").then()
				.body("$", hasKey("code"))
				.body("$", hasKey("result"))
				.body("$",hasKey("message"));


				

	}
	@Test
	public void getProjectsOptimized() throws ParseException {

		Map<String ,String> headers =new HashMap<String,String>(){
			
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}

		};
		
		given()
		.headers(headers)
		.when()
		.get("search-admin/api/projects/projectListing")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK).log().all();

	}
	
	@Test
	public void getProjectsById() throws ParseException {
		
		Map<String ,String> headers =new HashMap<String,String>(){
			
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}	
		};
		String id = "7c14ed6a-7e73-459b-8ebc-47dab0187795";
		given()
		.headers(headers)
		.when()
		.get("search-admin/api/projects/"+id)
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK)
		.log().all();

	}

	@Test
	public void getProjectsByIdFake() throws ParseException {

		Map<String ,String> headers =new HashMap<String,String>(){

			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}
		};
		String id = "FakeID";
	Response response =	given()
				.headers(headers)
				.when()
				.get("search-admin/api/projects/"+id)
				;

		Assert.assertEquals(response.getStatusCode() /*actual value*/, 404 /*expected value*/,
				"No projects with this id");

	}
	
	@Test
	public void getProjectsByIdOptimized() throws ParseException {
		
		Map<String ,String> headers =new HashMap<String,String>(){
			
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}	
		};

		given()
		.headers(headers)
		.when()
		.get("search-admin/api/projects/53ca594d-c13b-4367-867f-6c3af8a74c09/details")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK)
		.log().all();

	}
	

//	CANT WORK RIGHT NOW
	@Test
	public void getExtension() throws ParseException {
		
		Map<String ,String> headers =new HashMap<String,String>(){
			
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}	
		};
		
	given()
		.headers(headers)
		.when()
			.get("search-admin/api/templates/extension")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK)

		.log().all();

		String result = given()
				.headers(headers)
				.get("search-admin/api/templates/extension")
				.andReturn().asString();
		System.out.println(result);

	}
	
	@Test
	public void getFilterDetails() throws ParseException {
		
		Map<String ,String> headers =new HashMap<String,String>(){

			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}	
		};
		String id = "7ccd9bac-821e-462a-aeef-d1a80bc58633";
		given()
		.headers(headers)
		.when()
		.get("search-admin/api/projects/"+id+"/filtersDetails")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK)
		.log().all();

	}
	
	@Test
	public void getSynonymByProjectId() throws ParseException {
		
		Map<String ,String> headers =new HashMap<String,String>(){
			
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}	
		};
		String id = "7ccd9bac-821e-462a-aeef-d1a80bc58633";
		given()
		.headers(headers)
		.when()
		.get("search-admin/api/projects/"+id+"/synonymsList")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK)
		.log().all();

	}
	@Test
	public void settingsDetails() throws ParseException {
		Map<String ,String> headers =new HashMap<String,String>(){

			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}
		};



		String result = given()
				.headers(headers)
				.get("search-admin/api/settings/settingsDetails")
				.andReturn().asString();
		System.out.println(result);


		JsonPath jsonPathEvaluator = new JsonPath(result);

		ArrayList a = jsonPathEvaluator.getJsonObject("result.id");
		String idOfProject = (String) a.get(0);
		ArrayList aa = jsonPathEvaluator.getJsonObject("result.rules_engine_enabled");

		boolean ruleEngine = (boolean) aa.get(0);
		String numRegex   = ".*[0-9].*";
		String alphaRegex = ".*[a-z].*";

		if (!(idOfProject.matches(numRegex) && idOfProject.matches(alphaRegex))) {
			Assert.assertFalse(true, "doesn't meet the expected result");
		}
		if (("true".equals(ruleEngine) || "false".equals(ruleEngine))){
			Assert.assertFalse(true, "doesn't meet the expected result");
		}
	}
}
