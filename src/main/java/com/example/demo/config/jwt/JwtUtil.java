package com.example.demo.config.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

// clase que crea el jwt token, que obtiene el username, y lo valida

@Component
public class JwtUtil {

        private final SecretKey phraseSecret= Keys.secretKeyFor(SignatureAlgorithm.HS256);

        @Value("${app.jwt.expiration}")
        private int jwtExpiration;

        private final Logger log = LoggerFactory.getLogger(JwtUtil.class);


    public String createTokenJwt(Authentication authentication){
        UserDetails user = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(user.getUsername())
                //coloca en el payload del jwt la fecha actual para determinar la antiguedad del jwt (creo)
                .setIssuedAt(new Date())
                // coloca el tiempo de expiracion del token
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))

                .signWith(phraseSecret)
                .compact();
    }

    public String getUsernameFromJwt(String jwt){
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(phraseSecret)
                .build()
                .parseClaimsJws(jwt);

        return jws.getBody().getSubject();
    }

    public boolean isJwtValid(String jwt){

        try{
            Jwts.parserBuilder()
                    //la libreria de jwt provee un generador de secret keys
                    // tiene soporte para varios algoritmos distintos
                    .setSigningKey(phraseSecret)
                    .build()
                    .parseClaimsJws(jwt);

            return true;
        } catch(JwtException  e){
            log.error("JWT Exception: {}", e.getMessage());
        }

        return false;
    }

}
