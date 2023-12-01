package com.abcairline.abc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPaymentException extends RuntimeException {
    public InvalidPaymentException( ) {
        super();
    }
    public InvalidPaymentException(String message) {
        super(message);
    }

    public InvalidPaymentException(String message, Throwable cause) {
        super(message, cause);
    }


}
