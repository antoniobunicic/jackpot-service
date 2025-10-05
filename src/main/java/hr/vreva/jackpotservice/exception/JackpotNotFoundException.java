package hr.vreva.jackpotservice.exception;

public class JackpotNotFoundException extends RuntimeException {
    public JackpotNotFoundException(Long jackpotId) {
        super("Jackpot not found: " + jackpotId);
    }
}
