package hr.vreva.jackpotservice.service.strategy.contribution;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FixedContributionStrategyTest {

    private final FixedContributionStrategy strategy = new FixedContributionStrategy();

    @ParameterizedTest
    @CsvSource({
        "100.00, 5.0, 5.00",
        "50.00, 2.5, 1.25",
        "100.00, 3.33, 3.33",
        "100.00, 0.0, 0.00"
    })
    void shouldCalculateFixedContribution(String betAmount, double contributionPercentage, String expectedContribution) {
        // Given
        JackpotEntity jackpot = JackpotEntity.builder()
                .contributionPercentage(contributionPercentage)
                .build();

        // When
        BigDecimal contribution = strategy.calculateContribution(new BigDecimal(betAmount), jackpot);

        // Then
        assertEquals(new BigDecimal(expectedContribution), contribution);
    }
}
