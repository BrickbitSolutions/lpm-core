package be.brickbit.lpm.infrastructure.advice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;

import java.util.List;

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
