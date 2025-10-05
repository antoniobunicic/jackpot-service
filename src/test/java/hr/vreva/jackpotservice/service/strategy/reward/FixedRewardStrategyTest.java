package hr.vreva.jackpotservice.service.strategy.reward;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedRewardStrategyTest {

    private final FixedRewardStrategy strategy = new FixedRewardStrategy();

    @Test
    void shouldReturnTrueForHundredPercentChance() {
        // Given
        JackpotEntity jackpot = JackpotEntity.builder()
                .rewardPercentage(100.0)
                .build();

        // When & Then - should always win with 100%
        assertTrue(strategy.isWinner(jackpot));
    }

    @Test
    void shouldReturnFalseForZeroPercentChance() {
        // Given
        JackpotEntity jackpot = JackpotEntity.builder()
                .rewardPercentage(0.0)
                .build();

        // When & Then - should never win with 0%
        assertFalse(strategy.isWinner(jackpot));
    }

    @Test
    void shouldProduceVariedResultsForFiftyPercent() {
        // Given
        JackpotEntity jackpot = JackpotEntity.builder()
                .rewardPercentage(50.0)
                .build();

        // When - run multiple times
        int wins = 0;
        int trials = 1000;
        for (int i = 0; i < trials; i++) {
            if (strategy.isWinner(jackpot)) {
                wins++;
            }
        }

        // Then - should win approximately 50% of the time (with some tolerance)
        assertTrue(wins > 400 && wins < 600,
                "Expected wins between 400-600, but got " + wins);
    }
}
