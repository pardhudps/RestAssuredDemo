package practice;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;

public class DynamicJson {
	
	
	@Test(dataProvider = "BookData")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		
		String response = given().log().all().queryParam("Content-Type", "applicatoin/json")
		.body(Payload.addBook(isbn, aisle))
		.when().post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		System.out.println(response);
	}
	
	@Test(dataProvider = "DeleteBookData")
	public void deleteBook(String ID) {
		
		RestAssured.baseURI = "http://216.10.245.166";
		given().log().all().queryParam("Content-Type", "application/json")
		.body(Payload.deleteBook(ID))
		.when().post("/Library/DeleteBook.php")
		.then().log().all().statusCode(200).extract().response().asString();
		
	}
	
	@DataProvider(name="BookData")
	public Object[][] getData() {
		
		return new Object[][] {{"LOP", "523"}, {"bvj", "489"}, {"vgl", "842"}};
	}

	@DataProvider(name="DeleteBookData")
	public Object[][] getID() {
		
		return new Object[][] {{"LOP523"}, {"bvj489"}, {"vgl842"}};
	}
}
