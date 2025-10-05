package hr.vreva.jackpotservice.service.strategy.contribution;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ContributionStrategyFactory {

    private final Map<JackpotEntity.ContributionType, ContributionStrategy> strategies;

    public ContributionStrategyFactory(
            FixedContributionStrategy fixedContributionStrategy,
            VariableContributionStrategy variableContributionStrategy) {

        this.strategies = new EnumMap<>(JackpotEntity.ContributionType.class);
        this.strategies.put(JackpotEntity.ContributionType.FIXED, fixedContributionStrategy);
        this.strategies.put(JackpotEntity.ContributionType.VARIABLE, variableContributionStrategy);
    }

    public ContributionStrategy getStrategy(JackpotEntity.ContributionType type) {
        ContributionStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown contribution type: " + type);
        }
        return strategy;
    }
}
