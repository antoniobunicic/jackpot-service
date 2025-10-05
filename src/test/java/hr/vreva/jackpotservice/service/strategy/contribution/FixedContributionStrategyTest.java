package hr.vreva.jackpotservice.service.strategy.contribution;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FixedContributionStrategyTest {

    private final FixedContributionStrategy strategy = new FixedContributionStrategy();

    @Test
    void shouldCalculateFixedContribution() {
        // Given
        JackpotEntity jackpot = JackpotEntity.builder()
                .contributionPercentage(5.0)
                .build();
        BigDecimal betAmount = new BigDecimal("100.00");

        // When
        BigDecimal contribution = strategy.calculateContribution(betAmount, jackpot);

        // Then
        assertEquals(new BigDecimal("5.00"), contribution);
    }

    @Test
    void shouldCalculateContributionWithDecimalPercentage() {
        // Given
        JackpotEntity jackpot = JackpotEntity.builder()
                .contributionPercentage(2.5)
                .build();
        BigDecimal betAmount = new BigDecimal("50.00");

        // When
        BigDecimal contribution = strategy.calculateContribution(betAmount, jackpot);

        // Then
        assertEquals(new BigDecimal("1.25"), contribution);
    }

    @Test
    void shouldRoundToTwoDecimalPlaces() {
        // Given
        JackpotEntity jackpot = JackpotEntity.builder()
                .contributionPercentage(3.33)
                .build();
        BigDecimal betAmount = new BigDecimal("100.00");

        // When
        BigDecimal contribution = strategy.calculateContribution(betAmount, jackpot);

        // Then
        assertEquals(new BigDecimal("3.33"), contribution);
    }

    @Test
    void shouldHandleZeroPercentage() {
        // Given
        JackpotEntity jackpot = JackpotEntity.builder()
                .contributionPercentage(0.0)
                .build();
        BigDecimal betAmount = new BigDecimal("100.00");

        // When
        BigDecimal contribution = strategy.calculateContribution(betAmount, jackpot);

        // Then
        assertEquals(new BigDecimal("0.00"), contribution);
    }
}
