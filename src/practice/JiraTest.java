package practice;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SessionFilter session = new SessionFilter();
		
		RestAssured.baseURI = "http://localhost:8080";
		
		given().log().all().header("Content-Type","application/json").body("{ \"username\": \"pardhudps\", \"password\": \"Daredevil@96\" }")
		.filter(session)
		.when().post("/rest/auth/1/session")
		.then().log().all();
		
		//Add comment
		String expectedComment = "Commenting through Rest API script.";
		String addCommentResponse = given().log().all().header("Content-Type", "application/json").pathParam("issueId", "RSA-3")
		.body("{\r\n"
				+ "    \"body\": \""+expectedComment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session)
		.when().post("/rest/api/2/issue/{issueId}/comment")
		.then().log().all()
		.assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js1 = new JsonPath(addCommentResponse);
		String CommentId = js1.getString("id");
		
		//Add attachment
		given().log().all().header("X-Atlassian-Token", "no-check")
		.header("Content-Type", "multipart/form-data")
		.pathParam("issueId", "RSA-3")
		.multiPart("file",new File(System.getProperty("user.dir")+"/src/files/Attachment.txt"))
		.filter(session)
		.when().post("/rest/api/2/issue/{issueId}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		
		//Get Issue details
		String getCommentResponse = given().log().all().pathParam("issueId", "RSA-3")
		.queryParam("fields", "comment")
		.filter(session)
		.when().get("/rest/api/2/issue/{issueId}")
		.then().log().all().extract().response().asString();
		
		JsonPath js2 = new JsonPath(getCommentResponse);
		
		int noOfComments = js2.getInt("fields.comment.comments.size()");
		
		for(int i=0; i <noOfComments; i++) {
			String commentIdIssue = js2.get("fields.comment.comments["+i+"].id").toString();
			if(CommentId.equalsIgnoreCase(commentIdIssue)) {
				String message = js2.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedComment);
			}
		}
		

	}

}
