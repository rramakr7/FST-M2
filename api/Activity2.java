package activities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Activity2 {
    String ROOT_URI = "https://petstore.swagger.io/v2/user";

    @Test(priority=1)
    public void addNewUserFromFile() throws IOException {
        // Import JSON file
        FileInputStream inputJSON = new FileInputStream("src/test/java/activities/userinfo.json");
        //Read JSON file as String
        String reqBody = new String(inputJSON.readAllBytes());
        Response response =
                given().contentType(ContentType.JSON) // Set headers
                        .body(reqBody) //Pass request body from file
                        .when().post(ROOT_URI); // Send POST request
        inputJSON.close();

        //Assertions
        response.then().body("code",equalTo(200));
        response.then().body("message",equalTo("9926"));

        // Print response of POST request
        String body = response.getBody().asPrettyString();
        System.out.println(body);
    }
    @Test(priority=2)
    public void getUserInfo(){
        //Import JSON file to write to
        File outputJSON = new File("src/test/java/activities/userGetResponse.json");

        Response response = given().contentType(ContentType.JSON)
                .when().pathParam("username","coffee")
                .get(ROOT_URI + "/{username}");

        // Print response of GET request
        String responseBody = response.getBody().asPrettyString();
        System.out.println(responseBody);

        try{
            //Create JSON file
            outputJSON.createNewFile();
            //Write response body to external file
            FileWriter writer = new FileWriter(outputJSON.getPath());
            writer.write(responseBody);
            writer.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
        //Assertions
        response.then().body("id",equalTo(9926));
        response.then().body("username",equalTo("coffee"));
        response.then().body("firstName", equalTo("Test"));
        response.then().body("lastName",equalTo("User"));
        response.then().body("email",equalTo("justincase@mail.com"));
        response.then().body("password",equalTo("password123"));
        response.then().body("phone",equalTo("9812763450"));
    }
    @Test(priority=3)
    public void deleteUser() throws IOException {
        Response response = given().contentType(ContentType.JSON)
                .when().pathParam("username","coffee")
                .delete(ROOT_URI + "/{username}");
        //Assertions
        response.then().body("code",equalTo(200));
        response.then().body("message",equalTo("coffee"));

    }
}
