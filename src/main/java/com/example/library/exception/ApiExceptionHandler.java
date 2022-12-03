package com.example.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiExceptionHandler {
    @ResponseBody
    @ExceptionHandler(ApiRequestException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(ApiRequestException ex) {
        return "Oops, Not Found Error: " + ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badInputException(IllegalArgumentException ex) {
        return "Error, did you fill correct data? " + ex.getMessage();
    }
}
