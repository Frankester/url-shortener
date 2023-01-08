package com.example.demo.exceptions;

import lombok.Setter;

public class UserPasswordException extends Exception{


    @Setter
    private String username;

    public UserPasswordException(String message, String username) {
        super(message);
        this.username = username;
    }

    public UserPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserPasswordException(Throwable cause) {
        super(cause);
    }

    protected UserPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
