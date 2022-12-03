package com.example.library.exception;

public class ApiRequestException extends RuntimeException {
    public ApiRequestException(String msg) {
        super(msg);
    }
}
