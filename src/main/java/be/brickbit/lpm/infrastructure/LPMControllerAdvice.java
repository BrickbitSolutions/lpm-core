package be.brickbit.lpm.infrastructure;

import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import be.brickbit.lpm.infrastructure.exception.SecurityException;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@ControllerAdvice
public class LPMControllerAdvice {

//    @ExceptionHandler({ServiceException.class})
    void handleServiceExceptions(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }

//    @ExceptionHandler({EntityNotFoundException.class})
    void handleEntityNotFoundException(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

//    @ExceptionHandler({SecurityException.class})
    void handleSecurityException(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
}
