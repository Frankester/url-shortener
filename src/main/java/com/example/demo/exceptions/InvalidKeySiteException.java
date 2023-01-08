package com.example.demo.exceptions;

import com.example.demo.models.URLLong;

public class InvalidKeySiteException extends Exception{

    private String keysite;

    public InvalidKeySiteException(String message, String keysite) {
        super(message);
        this.keysite = keysite;
    }

    public InvalidKeySiteException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidKeySiteException(Throwable cause) {
        super(cause);
    }

    protected InvalidKeySiteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
