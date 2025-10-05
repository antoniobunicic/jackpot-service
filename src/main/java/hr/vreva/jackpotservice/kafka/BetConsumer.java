package hr.vreva.jackpotservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.vreva.jackpotservice.generated.model.BetRequest;
import hr.vreva.jackpotservice.service.JackpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BetConsumer {

    private static final Logger log = LoggerFactory.getLogger(BetConsumer.class);

    private final JackpotService jackpotService;
    private final ObjectMapper objectMapper;

    public BetConsumer(JackpotService jackpotService, ObjectMapper objectMapper) {
        this.jackpotService = jackpotService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topic.jackpot-bets:jackpot-bets}", groupId = "${spring.kafka.consumer.group-id:jackpot-service-group}")
    public void consumeBet(String message) {
        try {
            log.info("Received bet message from Kafka: {}", message);
            BetRequest betRequest = objectMapper.readValue(message, BetRequest.class);
            jackpotService.processBet(betRequest);
        } catch (Exception e) {
            log.error("Error processing bet message: {}", message, e);
        }
    }
}
