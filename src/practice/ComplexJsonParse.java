package practice;

import org.testng.Assert;

import files.Payload;
import files.ResuableMethods;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonPath js = ResuableMethods.rawToJson(Payload.coursePrice());
		//print no of courses returned by API
		int count = js.get("courses.size()");
		System.out.println("courses count: "+count);
		
		//Print Purchase Amount
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("purchase amount: "+totalAmount);
		
		//Print Title of the first course
		System.out.println("first course name: "+js.get("courses[0].title"));
		
		//Print All course titles and their respective Prices
		for(int i =0; i<count; i++) {
			System.out.println(js.get("courses["+i+"].title").toString());
		}
		
		//Print no of copies sold by RPA Course

		for(int i =0; i<count; i++) {
			if(js.get("courses["+i+"].title").toString().equalsIgnoreCase("RPA")) {
				System.out.println("RPA Copies: "+js.get("courses["+i+"].copies").toString());
			}
			
		}
		
		int actualAmount = 0;
		for(int i =0; i<count; i++) {
			
			int amount = (js.getInt("courses["+i+"].price")) * (js.getInt("courses["+i+"].copies"));
			
			actualAmount = actualAmount +amount;
			
		}
		System.out.println(actualAmount);
		
		Assert.assertEquals(totalAmount, actualAmount);
	}
	
		

}
