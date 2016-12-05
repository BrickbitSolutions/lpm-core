package be.brickbit.lpm.sms.exception;

public class NoSMSServiceException extends RuntimeException {
    public NoSMSServiceException(String message) {
        super(message);
    }
}
