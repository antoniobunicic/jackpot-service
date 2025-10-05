package hr.vreva.jackpotservice.service.strategy.contribution;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VariableContributionStrategyTest {

    private final VariableContributionStrategy strategy = new VariableContributionStrategy();

    @Test
    void shouldCalculateMaximumContributionWhenPoolAtInitialValue() {
        // Given - pool at initial value (ratio = 1.0)
        JackpotEntity jackpot = JackpotEntity.builder()
                .initialPoolValue(new BigDecimal("10000.00"))
                .currentPoolValue(new BigDecimal("10000.00"))
                .contributionPercentage(10.0)
                .contributionDecayRate(5.0)
                .build();
        BigDecimal betAmount = new BigDecimal("100.00");

        // When
        BigDecimal contribution = strategy.calculateContribution(betAmount, jackpot);

        // Then - 10% - (1.0 * 5%) = 5% of 100 = 5.00
        assertEquals(new BigDecimal("5.00"), contribution);
    }

    @Test
    void shouldCalculateContributionWithDecay() {
        // Given - pool doubled (ratio = 2.0)
        JackpotEntity jackpot = JackpotEntity.builder()
                .initialPoolValue(new BigDecimal("10000.00"))
                .currentPoolValue(new BigDecimal("20000.00"))
                .contributionPercentage(10.0)
                .contributionDecayRate(5.0)
                .build();
        BigDecimal betAmount = new BigDecimal("100.00");

        // When
        BigDecimal contribution = strategy.calculateContribution(betAmount, jackpot);

        // Then - 10% - (2.0 * 5%) = 0% of 100 = 0.00
        assertEquals(new BigDecimal("0.00"), contribution);
    }

    @Test
    void shouldNotGoNegative() {
        // Given - pool tripled (ratio = 3.0)
        JackpotEntity jackpot = JackpotEntity.builder()
                .initialPoolValue(new BigDecimal("10000.00"))
                .currentPoolValue(new BigDecimal("30000.00"))
                .contributionPercentage(10.0)
                .contributionDecayRate(5.0)
                .build();
        BigDecimal betAmount = new BigDecimal("100.00");

        // When
        BigDecimal contribution = strategy.calculateContribution(betAmount, jackpot);

        // Then - should be 0, not negative
        assertEquals(new BigDecimal("0.00"), contribution);
    }

    @Test
    void shouldCalculateContributionAtHalfPool() {
        // Given - pool at 50% (ratio = 0.5)
        JackpotEntity jackpot = JackpotEntity.builder()
                .initialPoolValue(new BigDecimal("10000.00"))
                .currentPoolValue(new BigDecimal("5000.00"))
                .contributionPercentage(10.0)
                .contributionDecayRate(5.0)
                .build();
        BigDecimal betAmount = new BigDecimal("100.00");

        // When
        BigDecimal contribution = strategy.calculateContribution(betAmount, jackpot);

        // Then - 10% - (0.5 * 5%) = 7.5% of 100 = 7.50
        assertEquals(new BigDecimal("7.50"), contribution);
    }
}
