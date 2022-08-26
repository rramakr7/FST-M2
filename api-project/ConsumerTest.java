package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.head;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    // Headers
    Map<String, String> headers = new HashMap<>();
    // Resource Path
    String resourcePath = "/api/users";

    //Create the pact
    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){
        //Headers
        headers.put("Content-Type", "application/json");
        //Request and Response body
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id",2601)
                .stringType("firstName","Ramya")
                .stringType("lastName","Arun")
                .stringType("email","ramar@example.com");
        //Pact
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                    .method("POST")
                    .path(resourcePath)
                    .headers(headers)
                    .body(requestResponseBody)
                .willRespondWith()
                    .status(201)
                    .body(requestResponseBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "9000")
    public void postRequestTest(){
        //Server URL
        String mockServer = "http://localhost:9000";

        //Request Body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id",10001);
        reqBody.put("firstName","Radha");
        reqBody.put("lastName","Raju");
        reqBody.put("email","rara@example.com");

        //Generate Response
        given().body(reqBody).headers(headers)
                .when().post(mockServer + resourcePath)
                .then().statusCode(201).log().all();

    }
}
