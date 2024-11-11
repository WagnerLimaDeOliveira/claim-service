package com.insurance.claim_service.service;

import com.insurance.claim_service.TestHelper.TestContainerSetupHelper;
import com.insurance.claim_service.entity.Claim;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class ClaimServiceTest {

    @Inject
    EntityManager entityManager;

    @Inject
    ClaimService claimService;

    @BeforeAll
    public static void setUp() {
        TestContainerSetupHelper.startContainers();

        System.setProperty("quarkus.datasource.jdbc.url", TestContainerSetupHelper.getPostgresJdbcUrl());
        System.setProperty("quarkus.datasource.username", TestContainerSetupHelper.getPostgresUsername());
        System.setProperty("quarkus.datasource.password", TestContainerSetupHelper.getPostgresPassword());
        System.setProperty("kafka.bootstrap.servers", TestContainerSetupHelper.getKafkaBootstrapServers());
    }

    @AfterAll
    public static void tearDown() {
        TestContainerSetupHelper.stopContainers();
    }

    @BeforeEach
    @Transactional
    public void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE claim RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    public void createClaim_shouldReturnCreatedClaim() {
        Claim claim = new Claim();
        claim.setAmountApproved(0);
        claim.setAmountRequested(40);
        claim.setDateFiled(Instant.now());
        claim.setDateOfLoss(Instant.now());
        claim.setPolicyNumber("TESTNUMBER");
        claim.setDescriptionOfLoss("Water falls");
        Claim createdClaim = claimService.createClaim(claim);
        assertThat(createdClaim.getAmountApproved(), equalTo(claim.getAmountApproved()));
        assertThat(createdClaim.getAmountRequested(), equalTo(claim.getAmountRequested()));
        assertThat(createdClaim.getDateFiled(), equalTo(claim.getDateFiled()));
        assertThat(createdClaim.getDateOfLoss(), equalTo(claim.getDateOfLoss()));
        assertThat(createdClaim.getPolicyNumber(), equalTo(claim.getPolicyNumber()));
        assertThat(createdClaim.getDescriptionOfLoss(), equalTo(claim.getDescriptionOfLoss()));
        assertThat(createdClaim.getStatus(), equalTo(claim.getStatus()));
    }

    @Test
    public void getClaimById_shouldReturnRequestedClaim() {
        Claim claim = new Claim();
        claim.setAmountApproved(0);
        claim.setAmountRequested(40);
        claim.setDateFiled(Instant.now());
        claim.setDateOfLoss(Instant.now());
        claim.setPolicyNumber("TESTNUMBER");
        claim.setDescriptionOfLoss("Water falls");

        Claim createdClaim = claimService.createClaim(claim);
        assertThat(createdClaim.getAmountApproved(), equalTo(claim.getAmountApproved()));
        assertThat(createdClaim.getAmountRequested(), equalTo(claim.getAmountRequested()));
        assertThat(createdClaim.getDateFiled(), equalTo(claim.getDateFiled()));
        assertThat(createdClaim.getDateOfLoss(), equalTo(claim.getDateOfLoss()));
        assertThat(createdClaim.getPolicyNumber(), equalTo(claim.getPolicyNumber()));
        assertThat(createdClaim.getDescriptionOfLoss(), equalTo(claim.getDescriptionOfLoss()));
        assertThat(createdClaim.getStatus(), equalTo(claim.getStatus()));

        Claim retrievedClaim = claimService.getClaimById(createdClaim.getId());
        assertThat(retrievedClaim.getId(), equalTo(createdClaim.getId()));
        assertThat(retrievedClaim.getAmountApproved(), equalTo(createdClaim.getAmountApproved()));
        assertThat(retrievedClaim.getAmountRequested(), equalTo(createdClaim.getAmountRequested()));
        assertThat(retrievedClaim.getDateFiled().truncatedTo(ChronoUnit.SECONDS), equalTo(createdClaim.getDateFiled().truncatedTo(ChronoUnit.SECONDS)));
        assertThat(retrievedClaim.getDateOfLoss().truncatedTo(ChronoUnit.SECONDS), equalTo(createdClaim.getDateOfLoss().truncatedTo(ChronoUnit.SECONDS)));
        assertThat(retrievedClaim.getPolicyNumber(), equalTo(createdClaim.getPolicyNumber()));
        assertThat(retrievedClaim.getDescriptionOfLoss(), equalTo(createdClaim.getDescriptionOfLoss()));
        assertThat(retrievedClaim.getStatus(), equalTo(createdClaim.getStatus()));
    }

    @Test
    public void updateClaim_shouldReturnUpdatedClaim() {
        Claim claim = new Claim();
        claim.setAmountApproved(0);
        claim.setAmountRequested(40);
        claim.setDateFiled(Instant.now());
        claim.setDateOfLoss(Instant.now());
        claim.setPolicyNumber("TESTNUMBER");
        claim.setDescriptionOfLoss("Water falls");
        Claim createdClaim = claimService.createClaim(claim);

        assertThat(createdClaim.getAmountApproved(), equalTo(claim.getAmountApproved()));
        assertThat(createdClaim.getAmountRequested(), equalTo(claim.getAmountRequested()));
        assertThat(createdClaim.getDateFiled(), equalTo(claim.getDateFiled()));
        assertThat(createdClaim.getDateOfLoss(), equalTo(claim.getDateOfLoss()));
        assertThat(createdClaim.getPolicyNumber(), equalTo(claim.getPolicyNumber()));
        assertThat(createdClaim.getDescriptionOfLoss(), equalTo(claim.getDescriptionOfLoss()));
        assertThat(createdClaim.getStatus(), equalTo(claim.getStatus()));

        createdClaim.setDescriptionOfLoss("No Water falls");
        createdClaim.setAmountApproved(35);

        Claim uptadedClaim = claimService.updateClaim(createdClaim);

        assertThat(uptadedClaim.getId(), equalTo(createdClaim.getId()));
        assertThat(uptadedClaim.getAmountApproved(), equalTo(35.0));
        assertThat(uptadedClaim.getAmountRequested(), equalTo(createdClaim.getAmountRequested()));
        assertThat(uptadedClaim.getDateFiled(), equalTo(createdClaim.getDateFiled()));
        assertThat(uptadedClaim.getDateOfLoss(), equalTo(createdClaim.getDateOfLoss()));
        assertThat(uptadedClaim.getPolicyNumber(), equalTo(createdClaim.getPolicyNumber()));
        assertThat(uptadedClaim.getDescriptionOfLoss(), equalTo("No Water falls"));
        assertThat(uptadedClaim.getStatus(), equalTo(createdClaim.getStatus()));
    }

    @Test
    public void deleteClaim_shouldDeleteClaimAndReturnTrue() {
        Claim claim = new Claim();
        claim.setAmountApproved(0);
        claim.setAmountRequested(40);
        claim.setDateFiled(Instant.now());
        claim.setDateOfLoss(Instant.now());
        claim.setPolicyNumber("TESTNUMBER");
        claim.setDescriptionOfLoss("Water falls");
        Claim createdClaim = claimService.createClaim(claim);

        boolean hasDeletedClaim = claimService.deleteClaimById(createdClaim.getId());
        assertThat(hasDeletedClaim, equalTo(true));

        Claim nullClaim = claimService.getClaimById(createdClaim.getId());
        assertThat(nullClaim, equalTo(null));
    }
}
