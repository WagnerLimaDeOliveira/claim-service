package com.insurance.claim_service.TestHelper;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


public class TestContainerSetupHelper {

    private static PostgreSQLContainer<?> postgresContainer;
    private static KafkaContainer kafkaContainer;

    public static void startContainers() {
        if (postgresContainer == null) {
            postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("claim_db")
                    .withUsername("user")
                    .withPassword("password");
            postgresContainer.start();
        }

        if (kafkaContainer == null) {
            kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
            kafkaContainer.start();
        }
    }

    public static void stopContainers() {
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
        if (kafkaContainer != null) {
            kafkaContainer.stop();
        }
    }

    public static String getPostgresJdbcUrl() {
        return postgresContainer.getJdbcUrl();
    }

    public static String getPostgresUsername() {
        return postgresContainer.getUsername();
    }

    public static String getPostgresPassword() {
        return postgresContainer.getPassword();
    }

    public static String getKafkaBootstrapServers() {
        return kafkaContainer.getBootstrapServers();
    }
}
