package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class GitHubProject {
    //Request and Response specification
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    String sshKey;
    int sshKeyId;

    @BeforeClass
    public void setUp(){
        //Request Specification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com/user/keys")
                .addHeader("Authorization","token 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")
                .setContentType(ContentType.JSON)
                .build();
    }
    @Test
    public void postRequestTest(){
        //Request Body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("title", "TestAPIKey");
        reqBody.put("key", "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDmf/rDp3jEZDNE8L6JvyZf/dK4X6r7uGduCCAZS3lzwy/zF4sOtpNX1QbTafVgbz89GXZpgguaQ9EUEMjznog8+0PuABD1t/L73leCUfSQybTuoqfdziPfF72UacCEC5ZvdEZFnfkaBfu6CS2n2uWIdHY5BQ1MJ4FNFSYqmik2ilhuQJuycKjDB2TXyrGF0qk+EItSqb4OH+ZdvSyEhowj4Fu7iuzK7GSEo5uxqcRG0BcOM7cXT3lfkYxFpLUDwwGXvnYD2d3AfEX/egSXH8ENSMxN0oj1DWnO+i//tKJu+rFBRsDd5U8W2PwplVSg9khz8bEc/5LJ/7RQRdICSMQv");
        //Generate a response
        Response response = given().log().all().spec(requestSpec)
                .body(reqBody)
                .when().post();
        sshKeyId = response.then().extract().body().path("id");
        sshKey = response.then().extract().body().path("key");
        response.then().log().all()
                .body("title",equalTo("TestAPIKey"));
    }
    @Test(dependsOnMethods = "postRequestTest")
    public void getRequestTest(){
        //Generate response
        given().spec(requestSpec).pathParam("key_id",sshKeyId)
                .when().get("/{key_id}")
                .then().log().all()
                .body("title",equalTo("TestAPIKey"));
    }
    @Test(dependsOnMethods = {"postRequestTest","getRequestTest"})
    public void deleteRequestTest(){
        //Generate response
        given().spec(requestSpec).pathParam("key_id",sshKeyId)
                .when().delete("/{key_id}")
                .then().log().all().statusCode(204);
    }




}
