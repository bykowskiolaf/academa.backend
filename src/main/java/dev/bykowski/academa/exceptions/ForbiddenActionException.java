package dev.bykowski.academa.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.FORBIDDEN)
public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) {
        super(message);
    }
}
