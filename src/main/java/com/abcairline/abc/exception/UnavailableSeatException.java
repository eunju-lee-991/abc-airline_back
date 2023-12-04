package com.abcairline.abc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnavailableSeatException extends RuntimeException {
    public UnavailableSeatException( ) {
        super();
    }
    public UnavailableSeatException(String message) {
        super(message);
    }

    public UnavailableSeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
