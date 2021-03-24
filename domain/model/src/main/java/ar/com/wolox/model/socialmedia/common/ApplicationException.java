package ar.com.wolox.model.socialmedia.common;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final String code;
    private final HttpStatus status;

    public ApplicationException(HttpStatus status, String message) {
        this(status, message, null);
    }

    public ApplicationException(HttpStatus status, String message, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
