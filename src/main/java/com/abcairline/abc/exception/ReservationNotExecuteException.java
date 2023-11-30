package com.abcairline.abc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservationNotExecuteException extends RuntimeException {
    public ReservationNotExecuteException(String message) {
        super(message);
    }

    public ReservationNotExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

}
