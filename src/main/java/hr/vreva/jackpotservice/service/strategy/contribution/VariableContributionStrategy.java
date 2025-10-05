package hr.vreva.jackpotservice.service.strategy.contribution;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class VariableContributionStrategy implements ContributionStrategy {

    @Override
    public BigDecimal calculateContribution(BigDecimal betAmount, JackpotEntity jackpot) {
        // contributionPercentage - (currentPool / initialPool) * decayRate
        double poolRatio = jackpot.getCurrentPoolValue()
                .divide(jackpot.getInitialPoolValue(), 4, RoundingMode.HALF_UP)
                .doubleValue();

        double decayAmount = poolRatio * jackpot.getContributionDecayRate();

        double effectivePercentage = Math.max(0.0, jackpot.getContributionPercentage() - decayAmount);

        double percentage = effectivePercentage / 100.0;
        return betAmount.multiply(BigDecimal.valueOf(percentage)).setScale(2, RoundingMode.HALF_UP);
    }
}
