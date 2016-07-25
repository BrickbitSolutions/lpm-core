package be.brickbit.lpm.core.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class DefaultExceptionHandler {

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler({MissingServletRequestParameterException.class,
            UnsatisfiedServletRequestParameterException.class,
            HttpRequestMethodNotSupportedException.class,
            ServletRequestBindingException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public
    @ResponseBody
    Map<String, Object> handleRequestException(Exception ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "Request Error");
        map.put("cause", ex.getMessage());
        return map;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public
    @ResponseBody
    Map<String, Object> handleUnsupportedMediaTypeException(HttpMediaTypeNotSupportedException ex) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "Unsupported Media Type");
        map.put("cause", ex.getLocalizedMessage());
        map.put("supported", ex.getSupportedMediaTypes());
        return map;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public
    @ResponseBody
    Map<String, Object> handleUncaughtException(Exception ex) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "Internal Server Error");
        if (ex.getCause() != null) {
            map.put("cause", ex.getCause().getMessage());
        } else {
            map.put("cause", ex.getMessage());
        }
        return map;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public
    @ResponseBody
    Map<String, Object> handleValidationError(MethodArgumentNotValidException ex) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "Bad Request");
        map.put("cause", "Validation of object failed");
        Map<String, String> validationErrors = new HashMap<>();
        for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                validationErrors.put("global", objectError.getDefaultMessage());
            }
        }
        map.put("validationMessages", validationErrors);
        return map;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public
    @ResponseBody
    Map<String, Object> handleNotFoundException(Exception ex) throws IOException {
        Map<String, Object> map = new HashMap<>();

        map.put("error", "Resource not found");

        return map;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler({AccessDeniedException.class})
    public
    @ResponseBody
    Map<String, Object> handleUnAuthorized(Exception ex) throws IOException {

        Map<String, Object> map = new HashMap<>();

        map.put("error", "Access Denied");
        map.put("cause", "Bad Credentials or user locked");

        return map;
    }
}
