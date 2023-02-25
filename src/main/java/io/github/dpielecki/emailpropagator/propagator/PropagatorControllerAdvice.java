package io.github.dpielecki.emailpropagator.propagator;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;

@ControllerAdvice
public class PropagatorControllerAdvice {
    
    @ResponseBody
    @ExceptionHandler(AuthenticationFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String illegalArgumentsHandler(AuthenticationFailedException exception) {
        return "Service SMTP authentication failed.";
    }

    @ResponseBody
    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalArgumentsHandler(MessagingException exception) {
        return exception.getMessage();
    }
}
