package com.example.demo.controller;

import com.example.demo.config.jwt.JwtUtil;
import com.example.demo.exceptions.UserPasswordException;
import com.example.demo.exceptions.UsernameExistsException;
import com.example.demo.models.dto.JwtResponse;
import com.example.demo.models.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private JdbcUserDetailsManager repoUsers;

    @Autowired
    private PasswordEncoder encoder;


    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginReq) throws UserPasswordException {

        // creo el objeto UsernamePasswordAuthenticationToken con las credenciales dadas
        // y lo guardo en el SecurityContext para que los filtros lo usen

        UserDetails user = User.withDefaultPasswordEncoder()
                .username(loginReq.getUsername())
                .password(loginReq.getPassword())
                .roles("USER")
                .build();


        Authentication auth = new UsernamePasswordAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(auth);


        if(repoUsers.userExists(loginReq.getUsername())){
            UserDetails userDb = repoUsers.loadUserByUsername(loginReq.getUsername());

            String pass = userDb.getPassword();


            if(!encoder.matches(loginReq.getPassword(), pass)){
                throw new UserPasswordException("Password incorrect", loginReq.getUsername());
            }
        }

        String jwt = jwtUtil.createTokenJwt(auth);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody LoginRequest loginReq) throws UsernameExistsException {

        if(repoUsers.userExists(loginReq.getUsername())){
            throw new UsernameExistsException("Username already taken!", loginReq.getUsername());
        }


        UserDetails user = User.withDefaultPasswordEncoder()
                .username(loginReq.getUsername())
                .password(loginReq.getPassword())
                .roles("USER")
                .build();



        repoUsers.createUser(user);



        return ResponseEntity.ok("User registered succesfully");

    }
}
