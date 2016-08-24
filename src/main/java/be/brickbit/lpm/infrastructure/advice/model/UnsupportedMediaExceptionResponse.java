package be.brickbit.lpm.infrastructure.advice.model;

import java.util.List;

import org.springframework.http.MediaType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsupportedMediaExceptionResponse extends ExceptionResponse {
	private List<MediaType> supportedMediaTypes;

    public UnsupportedMediaExceptionResponse() {
    }

    public UnsupportedMediaExceptionResponse(String error, String cause, List<MediaType> supportedMediaTypes) {
        super(error, cause);
        this.supportedMediaTypes = supportedMediaTypes;
    }
}
