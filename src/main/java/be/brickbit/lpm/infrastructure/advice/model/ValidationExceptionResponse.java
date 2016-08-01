package be.brickbit.lpm.infrastructure.advice.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationExceptionResponse extends ExceptionResponse {
	private Map<String, String> validationMessages;

	public ValidationExceptionResponse() {
	}

	public ValidationExceptionResponse(String error, String cause, Map<String, String> validationMessages) {
		super(error, cause);
		this.validationMessages = validationMessages;
	}
}
