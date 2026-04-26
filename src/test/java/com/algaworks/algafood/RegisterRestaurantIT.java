package com.algaworks.algafood;


import com.algaworks.algafood.domain.model.Kitchen;
import com.algaworks.algafood.domain.model.Restaurant;
import com.algaworks.algafood.domain.repository.KitchenRepository;
import com.algaworks.algafood.domain.repository.RestaurantRepository;
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

import java.math.BigDecimal;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class RegisterRestaurantIT {

    private static final String DATA_INVALID_TITLE = "Data invalid";
    private static final String BUSINESS_RULE_VIOLATION_TITLE = "Violation of rule of business";
    private static final int RESTAURANT_ID_NONEXISTENT = 100;

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private KitchenRepository kitchenRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private String jsonRestaurantCorrect;
    private String jsonRestaurantWithoutShipping;
    private String jsonRestaurantWithoutKitchen;
    private String jsonRestaurantWithKitchenNonExistent;

    private Restaurant burgerTopRestaurant;

    @Before
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/restaurants";

        jsonRestaurantCorrect = ResourceUtils.getContentFromResource(
                "/json/correct/restaurant-new-york-barbecue.json");

        jsonRestaurantWithoutShipping = ResourceUtils.getContentFromResource(
                "/json/incorrect/restaurant-new-york-barbecue-without-shipping.json");

        jsonRestaurantWithoutKitchen = ResourceUtils.getContentFromResource(
                "/json/incorrect/restaurant-new-york-barbecue-without-kitchen.json");

        jsonRestaurantWithKitchenNonExistent = ResourceUtils.getContentFromResource(
                "/json/incorrect/restaurant-new-york-barbecue-with-kitchen-nonexistent.json");

        databaseCleaner.clearTables();
        prepareData();
    }

    @Test
    public void shouldReturn200_WhenQueryRestaurants() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldReturn201_WhenRegisterRestaurant() {
        given()
                .body(jsonRestaurantCorrect)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void shouldReturn400_WhenRegisterRestaurantWithoutShippingFee() {
        given()
                .body(jsonRestaurantWithoutShipping)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo(DATA_INVALID_TITLE));
    }

    @Test
    public void shouldReturn400_WhenRegisterRestaurantWithoutKitchen() {
        given()
                .body(jsonRestaurantWithoutKitchen)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo(DATA_INVALID_TITLE));
    }

    @Test
    public void shouldReturn400_WhenRegisterRestaurantWithNonExistentKitchen() {
        given()
                .body(jsonRestaurantWithKitchenNonExistent)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo(BUSINESS_RULE_VIOLATION_TITLE));
    }

    @Test
    public void shouldReturn200AndCorrectResponse_WhenQueryExistingRestaurant() {
        given()
                .pathParam("restaurantId", burgerTopRestaurant.getId())
                .accept(ContentType.JSON)
                .when()
                .get("/{restaurantId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(burgerTopRestaurant.getName()));
    }

    @Test
    public void shouldReturn404_WhenQueryNonExistentRestaurant() {
        given()
                .pathParam("restaurantId", RESTAURANT_ID_NONEXISTENT)
                .accept(ContentType.JSON)
                .when()
                .get("/{restaurantId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void prepareData() {
        Kitchen kitchenBrazilian = new Kitchen();
        kitchenBrazilian.setName("Brazilian");
        kitchenRepository.save(kitchenBrazilian);

        Kitchen kitchenAmerican = new Kitchen();
        kitchenAmerican.setName("American");
        kitchenRepository.save(kitchenAmerican);

        burgerTopRestaurant = new Restaurant();
        burgerTopRestaurant.setName("Burger Top");
        burgerTopRestaurant.setShippingFee(new BigDecimal(10));
        burgerTopRestaurant.setKitchen(kitchenAmerican);
        restaurantRepository.save(burgerTopRestaurant);

        Restaurant comidaMineiraRestaurant = new Restaurant();
        comidaMineiraRestaurant.setName("Food Mineira");
        comidaMineiraRestaurant.setShippingFee(new BigDecimal(10));
        comidaMineiraRestaurant.setKitchen(kitchenBrazilian);
        restaurantRepository.save(comidaMineiraRestaurant);
    }
}