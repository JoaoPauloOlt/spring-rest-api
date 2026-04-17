package com.algaworks.algafood;

import com.algaworks.algafood.domain.model.Kitchen;
import com.algaworks.algafood.domain.repository.KitchenRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;
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

    private static final int KITCHEN_ID_NONEXISTENT = 100;

    private Kitchen kitchenAmerican;
    private int amountKitchensRegisters;
    private String jsonCorrectKitchenChinese;

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

        jsonCorrectKitchenChinese = ResourceUtils.getContentFromResource(
                "/json/correct/kitchen-chinese.json");

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
    public void mustReturnAmountCorrectKitchens_WhenQueryKitchens(){
        given()
                    .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .body("", hasSize(amountKitchensRegisters));
    }

    @Test
    public void mustReturnStatus201_WhenRegisterKitchen(){
        given()
                    .body(jsonCorrectKitchenChinese)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                .when()
                    .post()
                .then()
                    .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void mustReturnResponseAndStatusCorrect_WhenQueryKitchenExistent(){
        given()
                    .pathParam("kitchenId", kitchenAmerican.getId())
                    .accept(ContentType.JSON)
                .when()
                    .get("/{kitchenId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(kitchenAmerican.getName()));
    }

    public void mustReturnStatus404_WhenQueryKitchenNonExistent(){
        given()
                    .pathParam("kitchenId", KITCHEN_ID_NONEXISTENT)
                    .accept(ContentType.JSON)
                .when()
                    .get("/{kitchenId}")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void prepareData(){
        Kitchen kitchenThai = new Kitchen();
        kitchenThai.setName("Thai");
        kitchenRepository.save(kitchenThai);

        kitchenAmerican = new Kitchen();
        kitchenAmerican.setName("American");
        kitchenRepository.save(kitchenAmerican);

        amountKitchensRegisters = (int) kitchenRepository.count();
    }
}