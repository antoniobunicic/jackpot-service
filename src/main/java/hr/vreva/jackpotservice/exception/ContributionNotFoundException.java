package hr.vreva.jackpotservice.exception;

public class ContributionNotFoundException extends RuntimeException {
    public ContributionNotFoundException(String betId) {
        super("No contribution found for bet: " + betId);
    }
}
