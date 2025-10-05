package hr.vreva.jackpotservice.service.strategy.reward;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class FixedRewardStrategy implements RewardStrategy {

    private final Random random = new Random();

    @Override
    public boolean isWinner(JackpotEntity jackpot) {
        // Fixed chance: constant percentage
        double winChance = jackpot.getRewardPercentage() / 100.0;
        return random.nextDouble() < winChance;
    }
}
