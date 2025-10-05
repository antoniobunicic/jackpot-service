package hr.vreva.jackpotservice.service;

import hr.vreva.jackpotservice.generated.model.BetRequest;
import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import hr.vreva.jackpotservice.persistence.entity.JackpotContributionEntity;
import hr.vreva.jackpotservice.persistence.entity.JackpotRewardEntity;
import hr.vreva.jackpotservice.exception.BetAlreadyProcessedException;
import hr.vreva.jackpotservice.exception.ContributionNotFoundException;
import hr.vreva.jackpotservice.exception.JackpotNotFoundException;
import hr.vreva.jackpotservice.persistence.repository.JackpotContributionRepository;
import hr.vreva.jackpotservice.persistence.repository.JackpotRepository;
import hr.vreva.jackpotservice.persistence.repository.JackpotRewardRepository;
import hr.vreva.jackpotservice.service.strategy.contribution.ContributionStrategy;
import hr.vreva.jackpotservice.service.strategy.contribution.ContributionStrategyFactory;
import hr.vreva.jackpotservice.service.strategy.reward.RewardStrategy;
import hr.vreva.jackpotservice.service.strategy.reward.RewardStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class JackpotService {

    private static final Logger log = LoggerFactory.getLogger(JackpotService.class);

    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository contributionRepository;
    private final JackpotRewardRepository rewardRepository;

    private final ContributionStrategyFactory contributionStrategyFactory;
    private final RewardStrategyFactory rewardStrategyFactory;

    public JackpotService(JackpotRepository jackpotRepository,
                          JackpotContributionRepository contributionRepository,
                          JackpotRewardRepository rewardRepository,
                          ContributionStrategyFactory contributionStrategyFactory,
                          RewardStrategyFactory rewardStrategyFactory) {
        this.jackpotRepository = jackpotRepository;
        this.contributionRepository = contributionRepository;
        this.rewardRepository = rewardRepository;
        this.contributionStrategyFactory = contributionStrategyFactory;
        this.rewardStrategyFactory = rewardStrategyFactory;
    }

    @Transactional
    public void processBet(BetRequest betRequest) {
        log.info("Processing bet: {}", betRequest);

        validateBetNotAlreadyProcessed(betRequest.getBetId());
        JackpotEntity jackpot = findJackpotWithLock(betRequest.getJackpotId());
        BigDecimal contribution = calculateContribution(betRequest, jackpot);
        updateJackpotPool(jackpot, contribution);
        saveContributionRecord(betRequest, jackpot, contribution);

        log.info("Bet {} contributed {} to jackpot {}. New pool value: {}",
                betRequest.getBetId(), contribution, jackpot.getId(), jackpot.getCurrentPoolValue());
    }

    @Transactional
    public Optional<JackpotRewardEntity> evaluateReward(String betId) {
        log.info("Evaluating reward for bet: {}", betId);

        Optional<JackpotRewardEntity> existingReward = rewardRepository.findByBetId(betId);
        if (existingReward.isPresent()) {
            log.info("Reward already evaluated for bet {}", betId);
            return existingReward;
        }

        JackpotContributionEntity contribution = findContribution(betId);
        JackpotEntity jackpot = findJackpotWithLock(contribution.getJackpotId());

        return evaluateWinAndCreateReward(betId, contribution, jackpot);
    }

    private void validateBetNotAlreadyProcessed(String betId) {
        if (contributionRepository.findByBetId(betId).isPresent()) {
            log.warn("Bet {} already processed", betId);
            throw new BetAlreadyProcessedException(betId);
        }
    }

    private JackpotEntity findJackpotWithLock(Long jackpotId) {
        return jackpotRepository.findByIdWithLock(jackpotId)
                .orElseThrow(() -> {
                    log.warn("Jackpot {} not found", jackpotId);
                    return new JackpotNotFoundException(jackpotId);
                });
    }

    private JackpotContributionEntity findContribution(String betId) {
        return contributionRepository.findByBetId(betId)
                .orElseThrow(() -> {
                    log.warn("No contribution found for bet {}", betId);
                    return new ContributionNotFoundException(betId);
                });
    }

    private BigDecimal calculateContribution(BetRequest betRequest, JackpotEntity jackpot) {
        ContributionStrategy strategy = contributionStrategyFactory.getStrategy(jackpot.getContributionType());
        return strategy.calculateContribution(betRequest.getBetAmount(), jackpot);
    }

    private void updateJackpotPool(JackpotEntity jackpot, BigDecimal contribution) {
        BigDecimal newPoolValue = jackpot.getCurrentPoolValue().add(contribution);
        jackpot.setCurrentPoolValue(newPoolValue);
        jackpotRepository.save(jackpot);
    }

    private void saveContributionRecord(BetRequest betRequest, JackpotEntity jackpot, BigDecimal contribution) {
        JackpotContributionEntity contributionRecord = JackpotContributionEntity.builder()
                .betId(betRequest.getBetId())
                .userId(betRequest.getUserId())
                .jackpotId(betRequest.getJackpotId())
                .stakeAmount(betRequest.getBetAmount())
                .contributionAmount(contribution)
                .currentJackpotAmount(jackpot.getCurrentPoolValue())
                .build();
        contributionRepository.save(contributionRecord);
    }

    private Optional<JackpotRewardEntity> evaluateWinAndCreateReward(String betId, JackpotContributionEntity contribution, JackpotEntity jackpot) {
        RewardStrategy strategy = rewardStrategyFactory.getStrategy(jackpot.getRewardType());
        boolean isWinner = strategy.isWinner(jackpot);

        if (isWinner) {
            return Optional.of(createRewardAndResetPool(betId, contribution, jackpot));
        } else {
            log.info("Bet {} did not win jackpot", betId);
            return Optional.empty();
        }
    }

    private JackpotRewardEntity createRewardAndResetPool(String betId, JackpotContributionEntity contribution, JackpotEntity jackpot) {
        BigDecimal rewardAmount = jackpot.getCurrentPoolValue();

        JackpotRewardEntity reward = JackpotRewardEntity.builder()
                .betId(betId)
                .userId(contribution.getUserId())
                .jackpotId(jackpot.getId())
                .jackpotRewardAmount(rewardAmount)
                .build();
        rewardRepository.save(reward);

        jackpot.setCurrentPoolValue(jackpot.getInitialPoolValue());
        jackpotRepository.save(jackpot);

        log.info("Bet {} won jackpot! Reward: {}. Pool reset to: {}",
                betId, rewardAmount, jackpot.getInitialPoolValue());

        return reward;
    }
}
