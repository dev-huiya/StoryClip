package io.storyclip.web.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
public class AuthRequiredException extends Exception {

    public AuthRequiredException(String message) { this(message, null); }

    public AuthRequiredException(String message, Throwable cause) { super(message, cause); }
}
