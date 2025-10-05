package hr.vreva.jackpotservice.service.strategy.reward;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.Random;

@Component
public class VariableRewardStrategy implements RewardStrategy {

    private final Random random = new Random();

    @Override
    public boolean isWinner(JackpotEntity jackpot) {
        if (jackpot.getCurrentPoolValue().compareTo(jackpot.getRewardPoolLimit()) >= 0) {
            return true; // 100% chance when pool limit reached
        }

        // basePercentage + (currentPool / poolLimit) * (100 - basePercentage)
        double poolRatio = jackpot.getCurrentPoolValue()
                .divide(jackpot.getRewardPoolLimit(), 4, RoundingMode.HALF_UP)
                .doubleValue();

        double increaseRange = 100.0 - jackpot.getRewardPercentage();

        double effectivePercentage = jackpot.getRewardPercentage() + (poolRatio * increaseRange);

        double winChance = effectivePercentage / 100.0;
        return random.nextDouble() < winChance;
    }
}
