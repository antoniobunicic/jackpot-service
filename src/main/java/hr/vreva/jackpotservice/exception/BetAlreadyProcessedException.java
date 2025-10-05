package hr.vreva.jackpotservice.exception;

public class BetAlreadyProcessedException extends RuntimeException {
    public BetAlreadyProcessedException(String betId) {
        super("Bet " + betId + " has already been processed");
    }
}
