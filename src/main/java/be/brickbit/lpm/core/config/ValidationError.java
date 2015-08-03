package be.brickbit.lpm.core.config;

public class ValidationError {
    private String field;
    private String message;

    public ValidationError(String someField, String someMessage) {
        field = someField;
        message = someMessage;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
