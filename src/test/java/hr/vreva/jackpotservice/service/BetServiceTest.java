package hr.vreva.jackpotservice.service;

import hr.vreva.jackpotservice.generated.model.BetRequest;
import hr.vreva.jackpotservice.kafka.BetProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BetServiceTest {

    @Mock
    private BetProducer betProducer;

    private BetService betService;

    @BeforeEach
    void setUp() {
        betService = new BetService(betProducer);
    }

    @Test
    void shouldPublishBetToKafka() {
        // Given
        BetRequest betRequest = new BetRequest();
        betRequest.setBetId("bet-001");
        betRequest.setUserId("user-123");
        betRequest.setJackpotId(1L);
        betRequest.setBetAmount(new BigDecimal("100.00"));

        // When
        betService.submitBet(betRequest);

        // Then
        verify(betProducer).publishBet(betRequest);
    }
}
