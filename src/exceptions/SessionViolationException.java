package exceptions;

public class SessionViolationException extends RuntimeException {
    public SessionViolationException(String message) {
        super(message);
    }

    public SessionViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
