package activities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Activity1 {

    String ROOT_URI = "https://petstore.swagger.io/v2/pet";

    @Test(priority=1)
    public void addNewPet() {
        // Write the request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 90099);
        reqBody.put("name", "Burfy");
        reqBody.put("status", "alive");

        Response response =
                given().contentType(ContentType.JSON) // Set headers
                        .body(reqBody).when().post(ROOT_URI); // Send POST request

        //Assertions
        response.then().body("id",equalTo(90099));
        response.then().body("name",equalTo("Burfy"));
        response.then().body("status",equalTo("alive"));

        // Print response of POST request
        String body = response.getBody().asPrettyString();
        System.out.println(body);
    }
    @Test(priority=2)
    public void getPet(){
        Response response = given().contentType(ContentType.JSON)
                .when().pathParam("petId","90099")
                .get(ROOT_URI + "/{petId}");

        //Assertions
        response.then().body("id",equalTo(90099));
        response.then().body("name",equalTo("Burfy"));
        response.then().body("status",equalTo("alive"));

        // Print response of GET request
        String body = response.getBody().asPrettyString();
        System.out.println(body);
    }
    @Test(priority=3)
    public void deletePet(){
        Response response = given().contentType(ContentType.JSON)
                .when().pathParam("petId","90099")
                .delete(ROOT_URI + "/{petId}");

    }
}
