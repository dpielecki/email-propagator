package io.github.dpielecki.emailpropagator.receiver;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ReceiverControllerAdvice {
    
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalArgumentsHandler(IllegalArgumentException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String alreadyExistsHandler(EntityExistsException exception) {
        return "Address already exists.";
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidAddress(ConstraintViolationException exception) {
        return "Invalid address.";
    }

    @ResponseBody
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundHandler(NoSuchElementException exception) {
        return "Element with given ID doesn't exist.";
    }
}
