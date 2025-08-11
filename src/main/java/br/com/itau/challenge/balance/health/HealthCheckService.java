package br.com.itau.challenge.balance.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class HealthCheckService {

    private final JdbcTemplate jdbcTemplate;
    private final SqsAsyncClient sqs;
    private final String queueUrl;

    private static final Duration TIMEOUT = Duration.ofSeconds(2);

    public HealthCheckService(JdbcTemplate jdbcTemplate,
                              SqsAsyncClient sqs,
                              @Value("${app.sqs.queue-url}") String queueUrl) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqs = sqs;
        this.queueUrl = queueUrl;
    }

    public Map<String, Object> checkDb() {
        try {
            Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Map.of("status", "UP", "result", one);
        } catch (Exception e) {
            return Map.of("status", "DOWN", "error", e.getClass().getSimpleName(), "message", e.getMessage());
        }
    }

    public Map<String, Object> checkSqs() {
        try {
            var req = GetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributeNamesWithStrings(
                            "ApproximateNumberOfMessages",
                            "ApproximateNumberOfMessagesNotVisible")
                    .build();

            var attrs = sqs.getQueueAttributes(req).get(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
            return Map.of(
                    "status", "UP",
                    "queueUrl", queueUrl,
                    "approxMessages", attrs.attributes().getOrDefault("ApproximateNumberOfMessages", "0"),
                    "inFlight", attrs.attributes().getOrDefault("ApproximateNumberOfMessagesNotVisible", "0")
            );
        } catch (Exception e) {
            return Map.of("status", "DOWN", "queueUrl", queueUrl,
                    "error", e.getClass().getSimpleName(), "message", e.getMessage());
        }
    }
}
