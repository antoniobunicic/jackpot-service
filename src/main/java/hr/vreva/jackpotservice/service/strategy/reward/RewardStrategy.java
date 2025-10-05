package hr.vreva.jackpotservice.service.strategy.reward;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;

public interface RewardStrategy {
    boolean isWinner(JackpotEntity jackpot);
}
