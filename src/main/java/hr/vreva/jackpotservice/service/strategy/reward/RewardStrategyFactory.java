package hr.vreva.jackpotservice.service.strategy.reward;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class RewardStrategyFactory {

    private final Map<JackpotEntity.RewardType, RewardStrategy> strategies;

    public RewardStrategyFactory(
            FixedRewardStrategy fixedRewardStrategy,
            VariableRewardStrategy variableRewardStrategy) {

        this.strategies = new EnumMap<>(JackpotEntity.RewardType.class);
        this.strategies.put(JackpotEntity.RewardType.FIXED, fixedRewardStrategy);
        this.strategies.put(JackpotEntity.RewardType.VARIABLE, variableRewardStrategy);
    }

    public RewardStrategy getStrategy(JackpotEntity.RewardType type) {
        RewardStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown reward type: " + type);
        }
        return strategy;
    }
}
