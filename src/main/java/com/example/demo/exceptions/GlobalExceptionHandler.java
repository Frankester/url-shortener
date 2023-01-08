package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(KeySiteInUseException.class)
        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        String keySiteInUse(KeySiteInUseException ex){
            return ex.getLocalizedMessage();
        }

        @ExceptionHandler(InvalidKeySiteException.class)
        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        String invalidKeySite(InvalidKeySiteException ex){
                return ex.getLocalizedMessage();
        }


        @ExceptionHandler(UsernameExistsException.class)
        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        String usernameExists(UsernameExistsException ex){
                return ex.getLocalizedMessage();
        }


        @ExceptionHandler(UserPasswordException.class)
        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        String usernameExists(UserPasswordException ex){
                return ex.getLocalizedMessage();
        }
}
