package hr.vreva.jackpotservice.service.strategy.contribution;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FixedContributionStrategy implements ContributionStrategy {

    @Override
    public BigDecimal calculateContribution(BigDecimal betAmount, JackpotEntity jackpot) {
        double percentage = jackpot.getContributionPercentage() / 100.0;
        return betAmount.multiply(BigDecimal.valueOf(percentage)).setScale(2, RoundingMode.HALF_UP);
    }
}
