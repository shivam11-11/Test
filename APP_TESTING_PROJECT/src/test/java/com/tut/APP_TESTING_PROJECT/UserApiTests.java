
package com.tut.APP_TESTING_PROJECT;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserApiTests {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://bfhldevapigw.healthrx.co.in";
        RestAssured.basePath = "/automation-campus/create/user";
    }

    @Test
    public void testCreateUserWithValidData() {
        String requestBody = "{\n" +
                "    \"firstName\": \"Ashish\",\n" +
                "    \"lastName\": \"Sharma\",\n" +
                "    \"phoneNumber\": 1234567890,\n" +
                "    \"emailId\": \"ashish.sharma@example.com\"\n" +
                "}";

        RestAssured.given()
                .header("roll-number", "1")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("message", equalTo("User created successfully")); 
    }

    @Test
    public void testCreateUserWithoutRollNumber() {
        String requestBody = "{\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"phoneNumber\": 1234567890,\n" +
                "    \"emailId\": \"john.doe@example.com\"\n" +
                "}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post();

        
        System.out.println("Response Body: " + response.getBody().asString());

        response.then()
                .statusCode(401) 
                .body("error", equalTo("Roll number is required")); 
    }


    @Test
    public void testCreateUserWithMissingRequiredField() {
        String requestBody = "{\n" +
                "    \"lastName\": \"Sharma\",\n" +
                "    \"phoneNumber\": 1234567890,\n" +
                "    \"emailId\": \"ashish.sharma@example.com\"\n" +
                "}";

        RestAssured.given()
                .header("roll-number", "1")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(400) 
                .body("error", equalTo("First name is required")); 
    }

    @Test
    public void testCreateUserWithDuplicatePhoneNumber() {
        String requestBody = "{\n" +
                "    \"firstName\": \"Ashish\",\n" +
                "    \"lastName\": \"Sharma\",\n" +
                "    \"phoneNumber\": 1234567890,\n" +
                "    \"emailId\": \"ashish.sharma@example.com\"\n" +
                "}";

        
        RestAssured.given()
                .header("roll-number", "1")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200);

        
        RestAssured.given()
                .header("roll-number", "2")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("error", equalTo("Phone number already exists")); 
    }

    @Test
    public void testCreateUserWithDuplicateEmailId() {
        String requestBody1 = "{\n" +
                "    \"firstName\": \"Ashish\",\n" +
                "    \"lastName\": \"Sharma\",\n" +
                "    \"phoneNumber\": 9876543210,\n" +
                "    \"emailId\": \"ashish.sharma@example.com\"\n" +
                "}";

        String requestBody2 = "{\n" +
                "    \"firstName\": \"Shivam\",\n" +
                "    \"lastName\": \"Patil\",\n" +
                "    \"phoneNumber\": 1234567890,\n" +
                "    \"emailId\": \"ashish.sharma@example.com\"\n" +
                "}";

   
        RestAssured.given()
                .header("roll-number", "1")
                .contentType(ContentType.JSON)
                .body(requestBody1)
                .when()
                .post()
                .then()
                .statusCode(200);

        
        RestAssured.given()
                .header("roll-number", "2")
                .contentType(ContentType.JSON)
                .body(requestBody2)
                .when()
                .post()
                .then()
                .statusCode(400) 
                .body("error", equalTo("Email ID already exists")); 
    }

    @Test
    public void testCreateUserWithInvalidPhoneNumber() {
        String requestBody = "{\n" +
                "    \"firstName\": \"Ashish\",\n" +
                "    \"lastName\": \"Sharma\",\n" +
                "    \"phoneNumber\": \"invalidNumber\",\n" +
                "    \"emailId\": \"ashish.sharma@example.com\"\n" +
                "}";

        RestAssured.given()
                .header("roll-number", "1")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(400) 
                .body("error", equalTo("Invalid phone number format")); 
    }

    @Test
    public void testCreateUserWithInvalidEmailId() {
        String requestBody = "{\n" +
                "    \"firstName\": \"Ashish\",\n" +
                "    \"lastName\": \"Sharma\",\n" +
                "    \"phoneNumber\": 1234567890,\n" +
                "    \"emailId\": \"invalidEmail\"\n" +
                "}";

        RestAssured.given()
                .header("roll-number", "1")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(400) 
                .body("error", equalTo("Invalid email ID format")); 
    }
}