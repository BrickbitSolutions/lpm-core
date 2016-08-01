package be.brickbit.lpm.infrastructure.advice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import be.brickbit.lpm.infrastructure.advice.model.ExceptionResponse;
import be.brickbit.lpm.infrastructure.advice.model.UnsupportedMediaExceptionResponse;
import be.brickbit.lpm.infrastructure.advice.model.ValidationExceptionResponse;
import be.brickbit.lpm.infrastructure.exception.ServiceException;

@ControllerAdvice
@ResponseBody
public class DefaultExceptionHandler {

	@ExceptionHandler({ MissingServletRequestParameterException.class,
			UnsatisfiedServletRequestParameterException.class,
			HttpRequestMethodNotSupportedException.class,
			ServletRequestBindingException.class,
			HttpMessageNotReadableException.class
	})
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public ExceptionResponse handleRequestException(Exception ex) {
		return new ExceptionResponse(
				"Bad Request",
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public UnsupportedMediaExceptionResponse handleUnsupportedMediaTypeException(HttpMediaTypeNotSupportedException ex) throws IOException {
		return new UnsupportedMediaExceptionResponse(
				"Unsupported Media Type",
				ex.getLocalizedMessage(),
				ex.getSupportedMediaTypes());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionResponse handleUncaughtException(Exception ex) throws IOException {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setError("Internal Server Error");
		if (ex.getCause() != null) {
			exceptionResponse.setCause(ex.getCause().getMessage());
		} else {
			exceptionResponse.setCause(ex.getMessage());
		}
		return exceptionResponse;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ValidationExceptionResponse handleValidationError(MethodArgumentNotValidException ex) throws IOException {
		Map<String, String> validationErrors = new HashMap<>();
		for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
			if (objectError instanceof FieldError) {
				FieldError fieldError = (FieldError) objectError;
				validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
			} else {
				validationErrors.put("global", objectError.getDefaultMessage());
			}
		}

		return new ValidationExceptionResponse(
				"Bad Request",
				"Validation of object failed",
				validationErrors);
	}

	@ExceptionHandler({ NoHandlerFoundException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ExceptionResponse handleNotFoundException(Exception ex) throws IOException {
		return new ExceptionResponse(
				"Not Found",
				"Requested resource was not found");
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ExceptionResponse handleUnAuthorized(Exception ex) throws IOException {
		return new ExceptionResponse(
				"Unauthorized",
				ex.getLocalizedMessage());
	}

	@ExceptionHandler({ ServiceException.class })
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public ExceptionResponse handleServiceException(ServiceException ex) throws IOException {
		return new ExceptionResponse(
				"Conflict",
				ex.getLocalizedMessage());
	}
}
