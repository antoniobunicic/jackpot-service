package hr.vreva.jackpotservice.controller;

import hr.vreva.jackpotservice.generated.model.BetRequest;
import hr.vreva.jackpotservice.generated.model.BetResponse;
import hr.vreva.jackpotservice.generated.model.RewardEvaluationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class JackpotServiceIntegrationTest {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    );

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldProcessBetEndToEnd() throws InterruptedException {
        // Given
        BetRequest request = new BetRequest();
        request.setBetId("e2e-bet-001");
        request.setUserId("user-456");
        request.setJackpotId(2L);
        request.setBetAmount(new BigDecimal("50.00"));

        // Submit bet
        ResponseEntity<BetResponse> submitResponse = restTemplate.postForEntity(
                "/api/bets",
                request,
                BetResponse.class
        );

        // Verify submission
        assertEquals(HttpStatus.OK, submitResponse.getStatusCode());
        assertNotNull(submitResponse.getBody());
        assertEquals("e2e-bet-001", submitResponse.getBody().getBetId());
        assertEquals(BetResponse.StatusEnum.PUBLISHED, submitResponse.getBody().getStatus());

        // Wait for Kafka processing
        Thread.sleep(2000);

        // Eevaluate reward
        ResponseEntity<RewardEvaluationResponse> rewardResponse = restTemplate.postForEntity(
                "/api/bets/e2e-bet-001/evaluate-reward",
                null,
                RewardEvaluationResponse.class
        );

        // Verify evaluation
        assertEquals(HttpStatus.OK, rewardResponse.getStatusCode());
        assertNotNull(rewardResponse.getBody());
        assertEquals("e2e-bet-001", rewardResponse.getBody().getBetId());
        assertNotNull(rewardResponse.getBody().getWon());
    }
}
