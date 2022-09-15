import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.parser.ParseException;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class ProjectModuleTestcases {
	Utility utilObj = new Utility();

	@Test
	public void login(){
		RestAssured.baseURI ="https://search-admin-dev-mamb5phriq-uc.a.run.app";

		RequestSpecification request=RestAssured.given();

		String payload = "{\n"
				+ "    \"email\": \""+utilObj.getEmail()+"\",\n"
				+ "    \"password\": \""+utilObj.getPass()+"\"\n"
				+ "}";
		request.header("content-type","application/json;charset=UTF-8");
		request.header("accept","application/json, text/plain, */*");

		Response resFromGenrateedToken = request.body(payload).post("/authenticate");
		int statusCode = resFromGenrateedToken.getStatusCode();
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/,
				"wrong credentials");

	}
//	LOGIN APIS TESTING FOR FAKE CREDENTIALS'
	@Test
	public void fake_email(){
		RestAssured.baseURI ="https://search-admin-dev-mamb5phriq-uc.a.run.app";

		RequestSpecification request=RestAssured.given();

		String payload = "{\n"
				+ "    \"email\": \"Not"+utilObj.getEmail()+"\",\n"
				+ "    \"password\": \""+utilObj.getPass()+"\"\n"
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
				+ "    \"email\": \""+utilObj.getId()+"\",\n"
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
		Assert.assertEquals(statusCode /*actual value*/, 400 /*expected value*/,
				"wrong email and pass entered");

	}
	@Test
	public void getProjectCheckAvailabilityOfData() throws ParseException {
		Map<String ,String> headers =new HashMap<String,String>(){
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ utilObj.getToken());
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
	public void getProjectCheckDataType() throws ParseException, IOException, java.text.ParseException {
		Map<String, String> headers = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;

			{
				put("Accept", "*/*");
				put("Authorization", "Bearer " + utilObj.getToken());
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
		while (iterator.hasNext()) {
			JSONObject jsonObject = (JSONObject) iterator.next();
			for (String key : jsonObject.keySet()) {
				allFields.add(key);
			}
		}
		for (int i = 0; i < allFields.size(); i++) {


			System.out.println("Field name :" + allFields.get(i));
			ArrayList getData = jsonPathEvaluator.getJsonObject("result." + allFields.get(i));
			System.out.println("lola" + getData);

			String dataOfFirst = "";
			if (null != getData.get(0))
			{
				dataOfFirst = getData.get(0).toString();
			}
			System.out.println("data of first: " + dataOfFirst);


//			Status check
			if (allFields.get(i).equals("status")) {
				if (!utilObj.getProjectStatus(dataOfFirst)) {
					System.out.println("Inside If Method");
					Assert.assertEquals(false, true, "wrong Status type");
				}
			}

//version check
			if(allFields.get(i).equals("version")) {
				if (!(utilObj.version(dataOfFirst))) {
					Assert.assertEquals(false, true, "not a version");

				}
			}

//			engine type check
				if(allFields.get(i).equals("engine_type")) {
					if (!(utilObj.getengineType(dataOfFirst))) {
						Assert.assertEquals(false, true, "engine type invalid");
					}
				}

//				search type check.
			if(allFields.get(i).equals("search_type")) {
				if (!(utilObj.getSearchType(dataOfFirst))) {
					Assert.assertEquals(false, true, "search type not valid");
				}
			}

//			support engine check
			if(allFields.get(i).equals("support_engine")) {
				if (!(utilObj.getSupportEngine(dataOfFirst))) {
					Assert.assertEquals(false, true, "support engine type not valid");
				}
			}

//			index mode check
			if(allFields.get(i).equals("index_mode")) {
				if (!(utilObj.getIndexMode(dataOfFirst))) {
					Assert.assertEquals(false, true, "index mode type not valid");
				}
			}

			try {
				UUID uuid = UUID.fromString(dataOfFirst);
				if ((allFields.get(i)).equals(utilObj.getId())) {
					System.out.println("we are assuming this the only id field in the code.");
				} else {
					Assert.assertEquals(true, false, "Not String");
				}
				System.out.println(uuid);
				//do something
			} catch (IllegalArgumentException ignored) {

			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

			try {
				Date a = dateFormat.parse(dataOfFirst);
				if ((allFields.get(i)).equals(utilObj.getDate().trim())) {
					System.out.println("it is a Date field");
				} else {
					Assert.assertEquals(true, false, "Not String");
				}
				System.out.println("parsed Date"+a);

			} catch (Exception ignored) {

			}


			}
		}

	@Test
	public void getProjectsOptimized() throws ParseException {

		Map<String ,String> headers =new HashMap<String,String>(){
			
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ utilObj.getToken());
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
				put("Authorization","Bearer "+ utilObj.getToken());
			}	
		};
		given()
		.headers(headers)
		.when()
		.get("search-admin/api/projects/"+utilObj.getProjectId())
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK);

	}

	@Test
	public void getProjectsByIdFake() throws ParseException {

		Map<String ,String> headers =new HashMap<String,String>(){

			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ utilObj.getToken());
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
				put("Authorization","Bearer "+ utilObj.getToken());
			}	
		};

		given()
		.headers(headers)
		.when()
		.get("search-admin/api/projects/"+utilObj.getProjectId()+"/details")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK);

	}
	

//	CANT WORK RIGHT NOW
	@Test
	public void getExtension() throws ParseException {
		
		Map<String ,String> headers =new HashMap<String,String>(){
			
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ utilObj.getToken());
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


	}
	
	@Test
	public void getFilterDetails() throws ParseException {
		
		Map<String ,String> headers =new HashMap<String,String>(){

			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ utilObj.getToken());
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
				put("Authorization","Bearer "+ utilObj.getToken());
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
				put("Authorization","Bearer "+ utilObj.getToken());
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
		try {
			UUID uuid = UUID.fromString(idOfProject);
		}catch (Exception e){
			Assert.assertEquals(true, false, "Not String");
		}

		if (("true".equals(ruleEngine) || "false".equals(ruleEngine))){
			Assert.assertFalse(true, "doesn't meet the expected result");
		}
	}

	@Test
	public void createProject() throws ParseException {


		Map<String ,String> headers =new HashMap<String,String>(){

			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ utilObj.getToken());
			}
		};

		RequestSpecification request=RestAssured.given();


		String requestBody ="{\n"
				+ "    \"name\": \"Testing Demo 4\",\n"
				+ "    \"description\": \"Testing Demo\",\n"
				+ "    \"version\": \"1.1.1\",\n"
				+ "    \"is_refresh\": false,\n"
				+ "    \"search_type\": \"SINGLE\",\n"
				+ "    \"auto_correct_enabled\": false,\n"
				+ "    \"include_index\": false,\n"
				+ "    \"curations_enabled\": false,\n"
				+ "    \"semantic_search_enabled\": false,\n"
				+ "    \"index_mode\": \"ADVANCED\",\n"
				+ "    \"aiml_enabled\": false,\n"
				+ "    \"es_config_name\": \"Elastic\",\n"
				+ "    \"search_api_url\": \"https://smart-search-api-qa-mamb5phriq-uc.a.run.app\",\n"
				+ "    \"support_engine\": \"ELASTIC\",\n"
				+ "    \"schema_id\": \"3c078a58-c1e9-4629-896b-cf9bd7e328f8\",\n"
				+ "    \"schema_name\": \"best_buy_sample_aug12_3\",\n"
				+ "    \"is_nlp_support\": false,\n"
				+ "    \"engine_type\": \"Ranking Based Engine\",\n"
				+ "    \"is_schema_failed\": false,\n"
				+ "    \"is_rule_failed\": false,\n"
				+ "    \"es_config_id\": \"30c9003d-3ede-4611-a8bf-ced270b312b1\"\n"
				+ "}";



		Response response =
				request.contentType(ContentType.JSON).headers(headers)
						.body(requestBody)
						.post("/search-admin/api/projects");

		AssertJUnit.assertEquals(response.getStatusCode(), 200);
	}


	@Test
	public void updateProjectById() throws ParseException {

		Map<String ,String> headers =new HashMap<String,String>(){

			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ utilObj.getToken());
			}
		};

		RequestSpecification request=RestAssured.given();


		String requestBody ="{\n"
				+ "    \"name\": \"Testing Demo hello\",\n"
				+ "    \"description\": \"Testing Demo\",\n"
				+ "    \"version\": \"1.1.1\",\n"
				+ "    \"is_refresh\": false,\n"
				+ "    \"search_type\": \"SINGLE\",\n"
				+ "    \"auto_correct_enabled\": false,\n"
				+ "    \"include_index\": false,\n"
				+ "    \"curations_enabled\": false,\n"
				+ "    \"semantic_search_enabled\": false,\n"
				+ "    \"index_mode\": \"ADVANCED\",\n"
				+ "    \"aiml_enabled\": false,\n"
				+ "    \"es_config_name\": \"Elastic\",\n"
				+ "    \"search_api_url\": \"https://smart-search-api-qa-mamb5phriq-uc.a.run.app\",\n"
				+ "    \"support_engine\": \"ELASTIC\",\n"
				+ "    \"schema_id\": \"3c078a58-c1e9-4629-896b-cf9bd7e328f8\",\n"
				+ "    \"schema_name\": \"best_buy_sample_aug12_3\",\n"
				+ "    \"is_nlp_support\": false,\n"
				+ "    \"engine_type\": \"Ranking Based Engine\",\n"
				+ "    \"is_schema_failed\": false,\n"
				+ "    \"is_rule_failed\": false,\n"
				+ "    \"es_config_id\": \"30c9003d-3ede-4611-a8bf-ced270b312b1\"\n"
				+ "}";

		Response response =
				request.contentType(ContentType.JSON).headers(headers)
						.body(requestBody)
						.put("/search-admin/api/projects/84cb73fe-f6a7-4289-a285-782823443fc0");

		AssertJUnit.assertEquals(response.getStatusCode(), 200);


	}


	@Test
	public void deleteProjectById() throws ParseException {

		Map<String ,String> headers =new HashMap<String,String>(){

			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ utilObj.getToken());
			}
		};

		given()
				.headers(headers)
				.when()
				.delete("/search-admin/api/projects/"+utilObj.getProjectId())
				.then()
				.assertThat()
				.statusCode(HttpStatus.SC_OK)
				.log().all();

	}

}
