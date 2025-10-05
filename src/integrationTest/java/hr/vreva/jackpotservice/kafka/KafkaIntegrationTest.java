package hr.vreva.jackpotservice.kafka;

import hr.vreva.jackpotservice.generated.model.BetRequest;
import hr.vreva.jackpotservice.persistence.entity.JackpotContributionEntity;
import hr.vreva.jackpotservice.persistence.repository.JackpotContributionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Testcontainers
class KafkaIntegrationTest {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    );

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private BetProducer betProducer;

    @Autowired
    private JackpotContributionRepository contributionRepository;

    @Test
    void shouldProcessBetThroughKafka() {
        // Given
        BetRequest betRequest = new BetRequest();
        betRequest.setBetId("kafka-test-001");
        betRequest.setUserId("user-789");
        betRequest.setJackpotId(1L);
        betRequest.setBetAmount(new BigDecimal("100.00"));

        // When
        betProducer.publishBet(betRequest);

        // Then - wait for async processing
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<JackpotContributionEntity> contribution =
                            contributionRepository.findByBetId("kafka-test-001");
                    Assertions.assertTrue(contribution.isPresent());
                    Assertions.assertEquals("user-789", contribution.get().getUserId());
                    Assertions.assertEquals(1L, contribution.get().getJackpotId());
                });
    }

    @Test
    void shouldCalculateCorrectContribution() {
        // Given
        BetRequest betRequest = new BetRequest();
        betRequest.setBetId("kafka-test-002");
        betRequest.setUserId("user-999");
        betRequest.setJackpotId(1L); // Fixed 5% contribution
        betRequest.setBetAmount(new BigDecimal("200.00"));

        // When
        betProducer.publishBet(betRequest);

        // Then
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<JackpotContributionEntity> contribution =
                            contributionRepository.findByBetId("kafka-test-002");
                    Assertions.assertTrue(contribution.isPresent());
                    Assertions.assertEquals(new BigDecimal("200.00"), contribution.get().getStakeAmount());
                    Assertions.assertEquals(new BigDecimal("10.00"), contribution.get().getContributionAmount());
                });
    }
}
