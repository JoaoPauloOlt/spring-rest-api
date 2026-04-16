package com.algaworks.algafood;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterKitchenIT {

    @LocalServerPort
    private int port;

    @Test
    public void mustReturnStatus200_WhenQueryKitchens(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

            given()
                    .basePath("/kitchens")
                    .port(port)
                    .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void mustHave4Kitchens_WhenQueryKitchens(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

            given()
                    .basePath("/kitchens")
                    .port(port)
                    .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .body("", hasSize(4))
                    .body("name", hasItems("Indiana", "Thai"));
    }
}