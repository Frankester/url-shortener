package com.example.demo.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


//En caso de error con jwt, el ExceptionTranslationFilter ejecuta este entryPoint
// que solo devuelve un 401 y el mensaje de error
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.error( authException.getClass() + ": " + authException.getLocalizedMessage());

        if(authException instanceof InsufficientAuthenticationException){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: Forbbiden");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
        }

    }
}
