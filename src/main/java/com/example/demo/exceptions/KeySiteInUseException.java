package com.example.demo.exceptions;

import com.example.demo.models.URLLong;
import lombok.Getter;
import lombok.Setter;

public class KeySiteInUseException extends Exception{

    @Getter
    @Setter
    private URLLong urlLong;

    public KeySiteInUseException(String message, URLLong urlLong) {
        super(message);
        this.urlLong = urlLong;
    }

    public KeySiteInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeySiteInUseException(Throwable cause) {
        super(cause);
    }

    protected KeySiteInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
