package be.brickbit.lpm.core.integration.exception;

public class NoSMSServiceException extends RuntimeException {
    public NoSMSServiceException() {
    }

    public NoSMSServiceException(String message) {
        super(message);
    }

    public NoSMSServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
