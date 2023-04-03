package practice;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.GetCourses;
import pojo.WebAutomation;

public class OAuthTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] expectedCourses = {"Selenium Webdriver Java","Cypress","Protractor"};
		
		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AVHEtk50qXwyOZDTnhLTzzjFgn9mkXrzzavtTsQJo4_Qdmq7nkZrbP0GoMMZNPn5gOrMoQ&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		
		String partialcode=url.split("code=")[1];

		String code=partialcode.split("&scope")[0];

		System.out.println("Code value is : "+code);
		
		String accesTokenResponse = given()
				.urlEncodingEnabled(false)
				.log().all()
				.queryParams("code",code)
				.queryParams("client_id", "")
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")

                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")

                .queryParams("grant_type", "authorization_code")

                .queryParams("state", "verifyfjdss")

                .queryParams("session_state", "ff4a89d1f7011eb34eef8cf02ce4353316d9744b..7eb8")
                

             // .queryParam("scope", "email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email")

               

                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath jsonPath = new JsonPath(accesTokenResponse);

	    String accessToken = jsonPath.getString("access_token");

	    System.out.println("Access TOKEN IS : "+accessToken);
		
		GetCourses gc = given()
		.queryParam("access_token", accessToken)
		.expect().defaultParser(Parser.JSON)
		.when()
		.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourses.class);
		
		System.out.println(gc.getInstructor());
		
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
		
		//System.out.println(actualResponse);
		
		int courseCount = gc.getCourses().getApi().size();
		
		for(int i = 0; i< courseCount; i++) {
			
			if(gc.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java")) {
				
				System.out.println("Price for the course: "+gc.getCourses().getApi().get(i).getPrice());
				break;
			}
			
		}
		
		//Get course names of Web Automation
		List<String> Courses = Arrays.asList(expectedCourses);
		ArrayList<String> actualCourses = new ArrayList<String>();
		
		List<WebAutomation> webAutomationCourses= gc.getCourses().getWebAutomation();
		for(int i = 0; i<webAutomationCourses.size();i++) {
			System.out.println(webAutomationCourses.get(i).getCourseTitle());
			actualCourses.add(webAutomationCourses.get(i).getCourseTitle());
			
		}
		
		Assert.assertTrue(Courses.equals(actualCourses));
	}

}
