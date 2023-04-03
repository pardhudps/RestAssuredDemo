package practice;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.Payload;
import files.ResuableMethods;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	@Test
	public void sumOfCourses() {
		
		JsonPath js = ResuableMethods.rawToJson(Payload.coursePrice());
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		int count = js.get("courses.size()");
		
		int actualAmount = 0;
		for(int i =0; i<count; i++) {
			
			int amount = (js.getInt("courses["+i+"].price")) * (js.getInt("courses["+i+"].copies"));
			
			actualAmount = actualAmount +amount;
			
		}
		System.out.println(actualAmount);
		
		Assert.assertEquals(totalAmount, actualAmount);
	}
	

}
