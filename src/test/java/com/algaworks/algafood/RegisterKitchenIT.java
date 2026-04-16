package com.algaworks.algafood;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Flyway flyway;

    @Before
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/kitchens";

        flyway.migrate();
    }

    @Test
    public void mustReturnStatus200_WhenQueryKitchens(){
            given()
                    .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void mustHave4Kitchens_WhenQueryKitchens(){
        given()
                    .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .body("", hasSize(4))
                    .body("name", hasItems("Indiana", "Thai"));
    }

    public void mustReturnStatus2001_WhenRegisterKitchen(){
        given()
                    .body("{ \"name\": \"Chinese\" }")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                .when()
                    .post()
                .then()
                    .statusCode(HttpStatus.CREATED.value());
    }
}