package hr.vreva.jackpotservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.vreva.jackpotservice.generated.model.BetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BetProducer {

    private static final Logger log = LoggerFactory.getLogger(BetProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.jackpot-bets:jackpot-bets}")
    private String topic;

    public BetProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishBet(BetRequest betRequest) {
        try {
            String message = objectMapper.writeValueAsString(betRequest);
            kafkaTemplate.send(topic, betRequest.getBetId(), message);
            log.info("Published bet {} to Kafka topic {}", betRequest.getBetId(), topic);
        } catch (JsonProcessingException e) {
            log.error("Error serializing bet", e);
            throw new RuntimeException("Failed to publish bet to Kafka", e);
        }
    }
}
