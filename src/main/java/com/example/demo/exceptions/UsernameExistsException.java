package com.example.demo.exceptions;

import com.example.demo.models.URLLong;
import lombok.Getter;
import lombok.Setter;

public class UsernameExistsException extends Exception{

    @Getter
    @Setter
    private String username;

    public UsernameExistsException(String message, String username) {
        super(message);
        this.username = username;
    }

    public UsernameExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameExistsException(Throwable cause) {
        super(cause);
    }

    protected UsernameExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
