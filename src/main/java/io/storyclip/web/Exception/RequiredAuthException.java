package io.storyclip.web.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
public class RequiredAuthException extends Exception {
    public RequiredAuthException(String message) {
        this(message, null);
    }

    public RequiredAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
