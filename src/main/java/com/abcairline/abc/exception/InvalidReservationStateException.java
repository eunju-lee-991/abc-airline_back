package com.abcairline.abc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidReservationStateException extends RuntimeException {
    public InvalidReservationStateException( ) {
        super();
    }
    public InvalidReservationStateException(String message) {
        super(message);
    }
    public InvalidReservationStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
