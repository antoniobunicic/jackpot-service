package hr.vreva.jackpotservice.controller;

import hr.vreva.jackpotservice.service.BetService;
import hr.vreva.jackpotservice.service.JackpotService;
import hr.vreva.jackpotservice.generated.model.BetRequest;
import hr.vreva.jackpotservice.generated.model.BetResponse;
import hr.vreva.jackpotservice.generated.model.RewardEvaluationResponse;
import hr.vreva.jackpotservice.persistence.entity.JackpotRewardEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private static final Logger log = LoggerFactory.getLogger(BetController.class);

    private final BetService betService;
    private final JackpotService jackpotService;

    public BetController(BetService betService, JackpotService jackpotService) {
        this.betService = betService;
        this.jackpotService = jackpotService;
    }

    @PostMapping
    public ResponseEntity<BetResponse> submitBet(@RequestBody BetRequest betRequest) {
        log.info("Received bet submission: {}", betRequest);

        betService.submitBet(betRequest);

        BetResponse response = new BetResponse();
        response.setBetId(betRequest.getBetId());
        response.setStatus(BetResponse.StatusEnum.PUBLISHED);
        response.setMessage("Bet successfully published to Kafka");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{betId}/evaluate-reward")
    public ResponseEntity<RewardEvaluationResponse> evaluateReward(@PathVariable String betId) {
        log.info("Evaluating reward for bet: {}", betId);

        Optional<JackpotRewardEntity> reward = jackpotService.evaluateReward(betId);

        RewardEvaluationResponse response = new RewardEvaluationResponse();
        response.setBetId(betId);

        if (reward.isPresent()) {
            JackpotRewardEntity jackpotReward = reward.get();
            response.setWon(true);
            response.setRewardAmount(jackpotReward.getJackpotRewardAmount());
            response.setMessage("Congratulations! You won the jackpot!");
        } else {
            response.setWon(false);
            response.setRewardAmount(BigDecimal.ZERO);
            response.setMessage("Better luck next time!");
        }

        return ResponseEntity.ok(response);
    }
}
