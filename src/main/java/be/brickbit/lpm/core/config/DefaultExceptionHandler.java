package be.brickbit.lpm.core.config;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public @ResponseBody
    Map<String, Object> handleRequestException(Exception ex) {
        Map<String, Object>  map = new HashMap<>();
        map.put("error", "Request Error");
        map.put("cause", ex.getMessage());
        return map;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public @ResponseBody Map<String, Object> handleUnsupportedMediaTypeException(HttpMediaTypeNotSupportedException ex) throws IOException {
        Map<String, Object>  map = new HashMap<>();
        map.put("error", "Unsupported Media Type");
        map.put("cause", ex.getLocalizedMessage());
        map.put("supported", ex.getSupportedMediaTypes());
        return map;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Map<String, Object> handleUncaughtException(Exception ex) throws IOException {
        Map<String, Object>  map = new HashMap<>();
        map.put("error", "Unknown Error");
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
    public @ResponseBody Map<String, Object> handleValidationError(MethodArgumentNotValidException ex) throws IOException {
        Map<String, Object>  map = new HashMap<>();
        map.put("error", "Validation of object failed");
        List<ValidationError> validationErrors = new ArrayList<>();
        for(ObjectError objectError : ex.getBindingResult().getAllErrors()){
            if(objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                validationErrors.add(new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()));
            }else{
                validationErrors.add(new ValidationError("global", objectError.getDefaultMessage()));
            }
        }
        map.put("validationMessages", validationErrors);
        return map;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody Map<String, Object> handleNotFoundException(Exception ex) throws IOException{
        Map<String, Object> map = new HashMap<>();

        map.put("error", "Unauthorized");

        return map;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler({AccessDeniedException.class})
    public @ResponseBody Map<String, Object> handleUnAuthorized(Exception ex) throws IOException {
        Map<String, Object> map = new HashMap<>();

        map.put("error", "Resource not found");
        if (ex.getCause() != null) {
            map.put("cause", ex.getCause().getMessage());
        } else {
            map.put("cause", ex.getMessage());
        }

        return map;
    }
}
