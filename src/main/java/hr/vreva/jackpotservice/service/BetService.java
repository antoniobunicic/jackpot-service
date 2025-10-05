package hr.vreva.jackpotservice.service;

import hr.vreva.jackpotservice.generated.model.BetRequest;
import hr.vreva.jackpotservice.kafka.BetProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BetService {

    private static final Logger log = LoggerFactory.getLogger(BetService.class);

    private final BetProducer betProducer;

    public BetService(BetProducer betProducer) {
        this.betProducer = betProducer;
    }

    public void submitBet(BetRequest betRequest) {
        log.info("Submitting bet: {}", betRequest.getBetId());
        betProducer.publishBet(betRequest);
    }
}
