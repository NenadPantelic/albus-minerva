package rs.ac.kg.fin.albus.minerva.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final int statusCode;

    public ApiException(int statusCode) {
        this.statusCode = statusCode;
    }

    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApiException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
