package practice;

import io.restassured.RestAssured;
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



public class SerializationExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
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
		
		RequestSpecification requestSpecification =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.addQueryParam("key", "qaclick123")
		.setContentType(ContentType.JSON).build();
		
		ResponseSpecification response = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification res = given().spec(requestSpecification).body(addPlace);
		
		AddPlaceResponse addPlaceResponse = 
		res.when().post("/maps/api/place/add/json")
		.then().log().all()
		.spec(response)
		.extract().response().as(AddPlaceResponse.class);
		
		System.out.println("Place ID: "+addPlaceResponse.getId());
		
		
	}

}
