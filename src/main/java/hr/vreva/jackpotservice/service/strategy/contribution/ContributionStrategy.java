package hr.vreva.jackpotservice.service.strategy.contribution;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;

import java.math.BigDecimal;

public interface ContributionStrategy {
    BigDecimal calculateContribution(BigDecimal betAmount, JackpotEntity jackpot);
}
