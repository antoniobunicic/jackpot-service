package hr.vreva.jackpotservice.service.strategy.reward;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VariableRewardStrategyTest {

    private final VariableRewardStrategy strategy = new VariableRewardStrategy();

    @Test
    void shouldAlwaysWinWhenPoolReachesLimit() {
        // Given - pool at limit
        JackpotEntity jackpot = JackpotEntity.builder()
                .currentPoolValue(new BigDecimal("100000.00"))
                .rewardPoolLimit(new BigDecimal("100000.00"))
                .rewardPercentage(0.1)
                .build();

        // When & Then - should always win
        assertTrue(strategy.isWinner(jackpot));
    }

    @Test
    void shouldAlwaysWinWhenPoolExceedsLimit() {
        // Given - pool exceeds limit
        JackpotEntity jackpot = JackpotEntity.builder()
                .currentPoolValue(new BigDecimal("150000.00"))
                .rewardPoolLimit(new BigDecimal("100000.00"))
                .rewardPercentage(0.1)
                .build();

        // When & Then - should always win
        assertTrue(strategy.isWinner(jackpot));
    }

    @Test
    void shouldNeverWinWhenPoolAtZeroAndBasePercentageIsZero() {
        // Given - empty pool, 0% base
        JackpotEntity jackpot = JackpotEntity.builder()
                .currentPoolValue(new BigDecimal("0.00"))
                .rewardPoolLimit(new BigDecimal("100000.00"))
                .rewardPercentage(0.0)
                .build();

        // When & Then - should never win
        assertFalse(strategy.isWinner(jackpot));
    }

    @Test
    void shouldHaveBasePercentageChanceWhenPoolAtZero() {
        // Given - empty pool, 100% base
        JackpotEntity jackpot = JackpotEntity.builder()
                .currentPoolValue(new BigDecimal("0.00"))
                .rewardPoolLimit(new BigDecimal("100000.00"))
                .rewardPercentage(100.0)
                .build();

        // When & Then - should always win with 100% base
        assertTrue(strategy.isWinner(jackpot));
    }

    @Test
    void shouldIncreaseChanceAsPoolGrows() {
        // Given - pool at 50% of limit, base 0.1%
        JackpotEntity jackpot = JackpotEntity.builder()
                .currentPoolValue(new BigDecimal("50000.00"))
                .rewardPoolLimit(new BigDecimal("100000.00"))
                .rewardPercentage(0.1)
                .build();

        // When - run multiple times
        int wins = 0;
        int trials = 10000;
        for (int i = 0; i < trials; i++) {
            if (strategy.isWinner(jackpot)) {
                wins++;
            }
        }

        // Then - effective percentage = 0.1 + (0.5 * (100 - 0.1)) = 50.05%
        // Should win approximately 50% of the time (with tolerance)
        assertTrue(wins > 4500 && wins < 5500,
                "Expected wins between 4500-5500, but got " + wins);
    }
}
