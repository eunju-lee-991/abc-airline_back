package com.abcairline.abc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotExistReservationException extends RuntimeException {
    public NotExistReservationException( ) {
        super();
    }
    public NotExistReservationException(String message) {
        super(message);
    }

    public NotExistReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}
