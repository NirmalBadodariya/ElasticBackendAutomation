import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

import java.util.HashMap;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.parser.ParseException;
import org.apache.http.HttpStatus;
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
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/,
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
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/,
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

		Response resFromGenrateedToken = request.body(payload).post("/authenticate");

		String jsonString = resFromGenrateedToken.getBody().asString();


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
	public void getProjectTemp() throws ParseException {
		Map<String ,String> headers =new HashMap<String,String>(){
			private static final long serialVersionUID = 1L;

			{
				put("Accept","*/*");
				put("Authorization","Bearer "+ getToken());
			}

		};
		RequestSpecification request= RestAssured.given().headers(headers);
		request.header("content-type","application/json;charset=UTF-8");
		request.header("accept","application/json, text/plain, */*");
		Response response = request.get("/search-admin/api/projects");
		String result = given()
				.headers(headers)
				.get("search-admin/api/projects")
				.andReturn().asString();
		System.out.println(result);

//		JSONObject jsonObj = new JSONObject(result);
//		System.out.println("jsonOb: "+jsonObj);
		JsonPath jsonPathEvaluator = new JsonPath(result);
		String s = jsonPathEvaluator.getString("result.version");
		System.out.println(s);
	}

	@Test
	public void getProjectCheckAvailability() throws ParseException {
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
				.body("$",hasKey("message")).toString();



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
		.get("https://search-admin-dev-mamb5phriq-uc.a.run.app/search-admin/api/projects/projectListing")
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
		
		given()
		.headers(headers)
		.when()
		.get("https://search-admin-dev-mamb5phriq-uc.a.run.app/search-admin/api/projects/53ca594d-c13b-4367-867f-6c3af8a74c09")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK)
		.log().all();

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
		.get("https://search-admin-dev-mamb5phriq-uc.a.run.app/search-admin/api/projects/53ca594d-c13b-4367-867f-6c3af8a74c09/details")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK)
		.log().all();

	}
	
	
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
		.get("https://search-admin-dev-mamb5phriq-uc.a.run.app/search-admin/api/templates/extension")
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
				put("Authorization","Bearer "+ getToken());
			}	
		};
		
		given()
		.headers(headers)
		.when()
		.get("https://search-admin-dev-mamb5phriq-uc.a.run.app/search-admin/api/projects/6506c7f3-b3f0-4c78-853d-053d253e1dee/filtersDetails")
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
		
		given()
		.headers(headers)
		.when()
		.get("https://search-admin-dev-mamb5phriq-uc.a.run.app/search-admin/api/projects/6506c7f3-b3f0-4c78-853d-053d253e1dee/filtersDetails")
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK)
		.log().all();

	}
	
	
}
