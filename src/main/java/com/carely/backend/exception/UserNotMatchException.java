package com.carely.backend.exception;

public class UserNotMatchException extends RuntimeException{
    public UserNotMatchException(String message) {
        super(message);
    }
}
