package hr.vreva.jackpotservice.service.strategy.contribution;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VariableContributionStrategyTest {

    private final VariableContributionStrategy strategy = new VariableContributionStrategy();

    @ParameterizedTest
    @CsvSource({
        "10000.00, 10000.00, 100.00, 10.0, 5.0, 5.00",   // pool at initial value (ratio = 1.0)
        "10000.00, 20000.00, 100.00, 10.0, 5.0, 0.00",   // pool doubled (ratio = 2.0)
        "10000.00, 30000.00, 100.00, 10.0, 5.0, 0.00",   // pool tripled (ratio = 3.0) - should not go negative
        "10000.00, 5000.00, 100.00, 10.0, 5.0, 7.50"     // pool at 50% (ratio = 0.5)
    })
    void shouldCalculateVariableContribution(
            String initialPoolValue,
            String currentPoolValue,
            String betAmount,
            double contributionPercentage,
            double contributionDecayRate,
            String expectedContribution
    ) {
        // Given
        JackpotEntity jackpot = JackpotEntity.builder()
                .initialPoolValue(new BigDecimal(initialPoolValue))
                .currentPoolValue(new BigDecimal(currentPoolValue))
                .contributionPercentage(contributionPercentage)
                .contributionDecayRate(contributionDecayRate)
                .build();

        // When
        BigDecimal contribution = strategy.calculateContribution(new BigDecimal(betAmount), jackpot);

        // Then
        assertEquals(new BigDecimal(expectedContribution), contribution);
    }
}
