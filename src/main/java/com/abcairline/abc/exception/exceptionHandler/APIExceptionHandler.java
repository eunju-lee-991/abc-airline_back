package com.abcairline.abc.exception.exceptionHandler;

import com.abcairline.abc.exception.InvalidPaymentException;
import com.abcairline.abc.exception.InvalidReservationStateException;
import com.abcairline.abc.exception.NotExistReservationException;
import com.abcairline.abc.exception.NotExistUserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        StringBuilder sb = new StringBuilder("Invalid argument ");
        if (e.getBindingResult().getErrorCount() > 0) {
            List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append("[").append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append("] ");
            }
        }

        return new ResponseEntity(new ErrorResult(sb.toString(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> invalidPaymentExceptionHandler(InvalidPaymentException e) {

        StringBuilder sb = new StringBuilder("Invalid payment error: " ).append(e.getMessage());

        return new ResponseEntity(new ErrorResult(sb.toString(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> invalidReservationStatusExceptionHandler(InvalidReservationStateException e) {

        StringBuilder sb = new StringBuilder("Invalid reservation status error: " ).append(e.getMessage());

        return new ResponseEntity(new ErrorResult(sb.toString(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> notExistReservationExceptionHandler(NotExistReservationException e) {

        StringBuilder sb = new StringBuilder("Invalid reservation number error: " ).append(e.getMessage());

        return new ResponseEntity(new ErrorResult(sb.toString(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> notExistUserExceptionHandler(NotExistUserException e) {

        StringBuilder sb = new StringBuilder("User not exist error: " ).append(e.getMessage());

        return new ResponseEntity(new ErrorResult(sb.toString(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
}

