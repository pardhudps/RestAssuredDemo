package practice;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import files.Payload;
import files.ResuableMethods;

public class Basics {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//given() - query parameters, headers and body (all input details)
		//when() - submit the API (eg. using post)
		//then() - validate the response
		
		//Add place -> Update place with new address -> Get place to validate if new address is present in the response

		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		//.body(Payload.addPlace())
		.body(new String(Files.readAllBytes(Paths.get("E:\\Workspace\\RestAssuredDemo\\src\\files\\project.json"))))
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.41 (Ubuntu)")
		.extract().response().asString();
		
		System.out.println(response);
		
		JsonPath js = ResuableMethods.rawToJson(response); // for parsing json
		String placeId = js.getString("place_id");
		
		System.out.println("Place ID: "+placeId);
		
		String newAddress = "70 Winter walk, USA";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.updatePlace(placeId, newAddress))
		.when().put("/maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));
		
		//getPlace
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeId)
		.when().get("/maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = ResuableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		
		System.out.println("Actual Address: "+actualAddress);
		
		Assert.assertEquals(actualAddress, newAddress);
		
	}

}
