package com.algaworks.algafood;

import com.algaworks.algafood.domain.model.Kitchen;
import com.algaworks.algafood.domain.repository.KitchenRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class RegisterKitchenIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private KitchenRepository kitchenRepository;

    @Before
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/kitchens";

        databaseCleaner.clearTables();
        prepareData();
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
    public void mustHave2Kitchens_WhenQueryKitchens(){
        given()
                    .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .body("", hasSize(2))
                    .body("name", hasItems("Indiana", "Thai"));
    }

    @Test
    public void mustReturnStatus201_WhenRegisterKitchen(){
        given()
                    .body("{ \"name\": \"Chinese\" }")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                .when()
                    .post()
                .then()
                    .statusCode(HttpStatus.CREATED.value());
    }

    private void prepareData(){
        Kitchen kitchen1 = new Kitchen();
        kitchen1.setName("Thai");
        kitchenRepository.save(kitchen1);

        Kitchen kitchen2 = new Kitchen();
        kitchen2.setName("American");
        kitchenRepository.save(kitchen2);
    }
}