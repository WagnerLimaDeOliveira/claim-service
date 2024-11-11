package com.insurance.claim_service.controller;

import com.insurance.claim_service.TestHelper.TestContainerSetupHelper;
import com.insurance.claim_service.entity.Claim;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

@QuarkusTest
public class ClaimControllerTest {
    @Inject
    EntityManager entityManager;

    final static String POLICY_NUMBER = "012345";

    @BeforeAll
    public static void setUp() {
        TestContainerSetupHelper.startContainers();

        System.setProperty("quarkus.datasource.jdbc.url", TestContainerSetupHelper.getPostgresJdbcUrl());
        System.setProperty("quarkus.datasource.username", TestContainerSetupHelper.getPostgresUsername());
        System.setProperty("quarkus.datasource.password", TestContainerSetupHelper.getPostgresPassword());
        System.setProperty("kafka.bootstrap.servers", TestContainerSetupHelper.getKafkaBootstrapServers());
    }

    @BeforeEach
    @Transactional
    public void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE claim RESTART IDENTITY CASCADE").executeUpdate();
    }

    @AfterAll
    public static void tearDown() {
        TestContainerSetupHelper.stopContainers();
    }

    @Test
    public void createClaim_shouldReturnCreatedClaim() {
        Claim claim = new Claim();
        claim.setAmountApproved(0);
        claim.setAmountRequested(40);
        claim.setDateFiled(Instant.now());
        claim.setDateOfLoss(Instant.now());
        claim.setPolicyNumber(POLICY_NUMBER);
        claim.setDescriptionOfLoss("Water falls");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(claim)
                .when()
                .post("/policy/" + POLICY_NUMBER)
                .then()
                .statusCode(200)
                .body("policyNumber", equalTo(POLICY_NUMBER))
                .body("amountApproved", equalTo(0.0F))
                .body("amountRequested", equalTo(40.0F))
                .body("descriptionOfLoss", equalTo("Water falls"));
    }

    @Test
    public void getClaimById_shouldReturnCreatedClaim() {
        Claim claim = new Claim();
        claim.setAmountApproved(0);
        claim.setAmountRequested(40);
        claim.setDateFiled(Instant.now());
        claim.setDateOfLoss(Instant.now());
        claim.setPolicyNumber(POLICY_NUMBER);
        claim.setDescriptionOfLoss("Water falls");

        Claim createdClaim = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(claim)
                .when()
                .post("/policy/" + POLICY_NUMBER)
                .then()
                .statusCode(200)
                .body("policyNumber", equalTo(POLICY_NUMBER))
                .body("amountApproved", equalTo(0.0F))
                .body("amountRequested", equalTo(40.0F))
                .body("descriptionOfLoss", equalTo("Water falls")).extract().as(Claim.class);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/policy/" + POLICY_NUMBER + "/claim/" + createdClaim.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(createdClaim.getId().intValue()))
                .body("policyNumber", equalTo(POLICY_NUMBER))
                .body("amountApproved", equalTo(0.0F))
                .body("amountRequested", equalTo(40.0F))
                .body("descriptionOfLoss", equalTo("Water falls"));
    }

    @Test
    public void updateClaim_shouldReturnUpdatedCreatedClaim() {
        Claim claim = new Claim();
        claim.setAmountApproved(0);
        claim.setAmountRequested(40);
        claim.setDateFiled(Instant.now());
        claim.setDateOfLoss(Instant.now());
        claim.setPolicyNumber(POLICY_NUMBER);
        claim.setDescriptionOfLoss("Water falls");

        Claim createdClaim = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(claim)
                .when()
                .post("/policy/" + POLICY_NUMBER)
                .then()
                .statusCode(200)
                .body("policyNumber", equalTo(POLICY_NUMBER))
                .body("amountApproved", equalTo(0.0F))
                .body("amountRequested", equalTo(40.0F))
                .body("descriptionOfLoss", equalTo("Water falls")).extract().as(Claim.class);

        createdClaim.setAmountApproved(35);
        createdClaim.setDescriptionOfLoss("No Water falls");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(createdClaim)
                .when()
                .put("/policy/" + POLICY_NUMBER + "/claim/" + createdClaim.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(createdClaim.getId().intValue()))
                .body("policyNumber", equalTo(POLICY_NUMBER))
                .body("amountApproved", equalTo(35.0F))
                .body("amountRequested", equalTo(40.0F))
                .body("descriptionOfLoss", equalTo("No Water falls"));

    }

    @Test
    public void deleteClaimById_shouldDeleteClaimAndReturnTrue() {
        Claim claim = new Claim();
        claim.setAmountApproved(0);
        claim.setAmountRequested(40);
        claim.setDateFiled(Instant.now());
        claim.setDateOfLoss(Instant.now());
        claim.setPolicyNumber(POLICY_NUMBER);
        claim.setDescriptionOfLoss("Water falls");

        Claim createdClaim = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(claim)
                .when()
                .post("/policy/" + POLICY_NUMBER)
                .then()
                .statusCode(200)
                .body("policyNumber", equalTo(POLICY_NUMBER))
                .body("amountApproved", equalTo(0.0F))
                .body("amountRequested", equalTo(40.0F))
                .body("descriptionOfLoss", equalTo("Water falls"))
                .extract()
                .as(Claim.class);

        boolean hasDeleted = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(claim)
                .when()
                .delete("/policy/" + POLICY_NUMBER + "/claim/" + createdClaim.getId())
                .then()
                .statusCode(200).extract().as(Boolean.class);

        assertTrue(hasDeleted);
    }

}
