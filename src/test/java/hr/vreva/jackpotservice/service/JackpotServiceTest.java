package hr.vreva.jackpotservice.service;

import hr.vreva.jackpotservice.exception.BetAlreadyProcessedException;
import hr.vreva.jackpotservice.exception.JackpotNotFoundException;
import hr.vreva.jackpotservice.generated.model.BetRequest;
import hr.vreva.jackpotservice.persistence.entity.JackpotContributionEntity;
import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import hr.vreva.jackpotservice.persistence.entity.JackpotRewardEntity;
import hr.vreva.jackpotservice.persistence.repository.JackpotContributionRepository;
import hr.vreva.jackpotservice.persistence.repository.JackpotRepository;
import hr.vreva.jackpotservice.persistence.repository.JackpotRewardRepository;
import hr.vreva.jackpotservice.service.strategy.contribution.ContributionStrategy;
import hr.vreva.jackpotservice.service.strategy.contribution.ContributionStrategyFactory;
import hr.vreva.jackpotservice.service.strategy.reward.RewardStrategy;
import hr.vreva.jackpotservice.service.strategy.reward.RewardStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JackpotServiceTest {

    @Mock
    private JackpotRepository jackpotRepository;

    @Mock
    private JackpotContributionRepository contributionRepository;

    @Mock
    private JackpotRewardRepository rewardRepository;

    @Mock
    private ContributionStrategyFactory contributionStrategyFactory;

    @Mock
    private RewardStrategyFactory rewardStrategyFactory;

    @Mock
    private ContributionStrategy contributionStrategy;

    @Mock
    private RewardStrategy rewardStrategy;

    private JackpotService jackpotService;

    @BeforeEach
    void setUp() {
        jackpotService = new JackpotService(
                jackpotRepository,
                contributionRepository,
                rewardRepository,
                contributionStrategyFactory,
                rewardStrategyFactory
        );
    }

    @Test
    void shouldProcessBetSuccessfully() {
        // Given
        BetRequest betRequest = new BetRequest();
        betRequest.setBetId("bet-001");
        betRequest.setUserId("user-123");
        betRequest.setJackpotId(1L);
        betRequest.setBetAmount(new BigDecimal("100.00"));

        JackpotEntity jackpot = JackpotEntity.builder()
                .id(1L)
                .currentPoolValue(new BigDecimal("10000.00"))
                .contributionType(JackpotEntity.ContributionType.FIXED)
                .build();

        when(contributionRepository.findByBetId("bet-001")).thenReturn(Optional.empty());
        when(jackpotRepository.findByIdWithLock(1L)).thenReturn(Optional.of(jackpot));
        when(contributionStrategyFactory.getStrategy(JackpotEntity.ContributionType.FIXED))
                .thenReturn(contributionStrategy);
        when(contributionStrategy.calculateContribution(any(), any()))
                .thenReturn(new BigDecimal("5.00"));

        // When
        jackpotService.processBet(betRequest);

        // Then
        verify(contributionRepository).save(any(JackpotContributionEntity.class));
        verify(jackpotRepository).save(any(JackpotEntity.class));
        assertEquals(new BigDecimal("10005.00"), jackpot.getCurrentPoolValue());
    }

    @Test
    void shouldThrowExceptionWhenBetAlreadyProcessed() {
        // Given
        BetRequest betRequest = new BetRequest();
        betRequest.setBetId("bet-001");

        JackpotContributionEntity existingContribution = new JackpotContributionEntity();
        when(contributionRepository.findByBetId("bet-001"))
                .thenReturn(Optional.of(existingContribution));

        // When & Then
        assertThrows(BetAlreadyProcessedException.class, () -> {
            jackpotService.processBet(betRequest);
        });

        verify(jackpotRepository, never()).findByIdWithLock(any());
    }

    @Test
    void shouldThrowExceptionWhenJackpotNotFound() {
        // Given
        BetRequest betRequest = new BetRequest();
        betRequest.setBetId("bet-001");
        betRequest.setJackpotId(999L);

        when(contributionRepository.findByBetId("bet-001")).thenReturn(Optional.empty());
        when(jackpotRepository.findByIdWithLock(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(JackpotNotFoundException.class, () -> {
            jackpotService.processBet(betRequest);
        });
    }

    @Test
    void shouldEvaluateRewardWhenWinning() {
        // Given
        String betId = "bet-001";

        JackpotContributionEntity contribution = JackpotContributionEntity.builder()
                .betId(betId)
                .userId("user-123")
                .jackpotId(1L)
                .build();

        JackpotEntity jackpot = JackpotEntity.builder()
                .id(1L)
                .currentPoolValue(new BigDecimal("10005.00"))
                .rewardType(JackpotEntity.RewardType.FIXED)
                .build();

        when(rewardRepository.findByBetId(betId)).thenReturn(Optional.empty());
        when(contributionRepository.findByBetId(betId)).thenReturn(Optional.of(contribution));
        when(jackpotRepository.findByIdWithLock(1L)).thenReturn(Optional.of(jackpot));
        when(rewardStrategyFactory.getStrategy(JackpotEntity.RewardType.FIXED))
                .thenReturn(rewardStrategy);
        when(rewardStrategy.isWinner(jackpot)).thenReturn(true);

        // When
        Optional<JackpotRewardEntity> result = jackpotService.evaluateReward(betId);

        // Then
        assertTrue(result.isPresent());
        verify(rewardRepository).save(any(JackpotRewardEntity.class));
        verify(jackpotRepository).save(any(JackpotEntity.class));
    }

    @Test
    void shouldReturnEmptyWhenNotWinning() {
        // Given
        String betId = "bet-001";

        JackpotContributionEntity contribution = JackpotContributionEntity.builder()
                .betId(betId)
                .userId("user-123")
                .jackpotId(1L)
                .build();

        JackpotEntity jackpot = JackpotEntity.builder()
                .id(1L)
                .rewardType(JackpotEntity.RewardType.FIXED)
                .build();

        when(rewardRepository.findByBetId(betId)).thenReturn(Optional.empty());
        when(contributionRepository.findByBetId(betId)).thenReturn(Optional.of(contribution));
        when(jackpotRepository.findByIdWithLock(1L)).thenReturn(Optional.of(jackpot));
        when(rewardStrategyFactory.getStrategy(JackpotEntity.RewardType.FIXED))
                .thenReturn(rewardStrategy);
        when(rewardStrategy.isWinner(jackpot)).thenReturn(false);

        // When
        Optional<JackpotRewardEntity> result = jackpotService.evaluateReward(betId);

        // Then
        assertFalse(result.isPresent());
        verify(rewardRepository, never()).save(any());
    }
}
