package be.brickbit.lpm.sms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSMSServiceException extends RuntimeException {
    public NoSMSServiceException(String message) {
        super(message);
    }
}
