package practice;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.AddPlaceResponse;
import pojo.Location;

import static io.restassured.RestAssured.*;

import groovyjarjarpicocli.CommandLine.Spec;

public class Revision {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RequestSpecification reqSpec = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123")
				.setContentType(ContentType.JSON)
				.build();
		ResponseSpecification responseSpec = new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectContentType(ContentType.JSON)
				.build();
		
		AddPlace addPlace = new AddPlace();
		addPlace.setAccuracy(50);
		addPlace.setName("Frontline House");
		addPlace.setPhone_number("(+91) 983 893 3937");
		addPlace.setAddress("29, side Layout, cohen 09");
		addPlace.setWebsite("http://google.com");
		addPlace.setLanguage("French-IN");
		
		Location location = new Location();
		location.setLat(-38.383494);
		location.setLng(33.427362);
		
		addPlace.setLocation(location);
		
		RequestSpecification actualRequest = given().spec(reqSpec).body(addPlace);
		
		AddPlaceResponse response = actualRequest.when().log().all()
				.post("/maps/api/place/add/json")
				.then()
				.spec(responseSpec)
				.extract()
				.response()
				.as(AddPlaceResponse.class);
		System.out.println(response.getPlace_id());
		
				
		
	}

}
