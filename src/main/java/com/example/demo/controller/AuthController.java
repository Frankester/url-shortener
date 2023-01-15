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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private JdbcUserDetailsManager repoUsers;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginReq){

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getUsername(),loginReq.getPassword()));


        String jwt = jwtUtil.createTokenJwt(auth);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody LoginRequest loginReq) throws UsernameExistsException {

        if(repoUsers.userExists(loginReq.getUsername())){
            throw new UsernameExistsException("Username already taken!", loginReq.getUsername());
        }


        UserDetails user = User.builder()
                .username(loginReq.getUsername())
                .password(passwordEncoder.encode(loginReq.getPassword()))
                .roles("USER")
                .build();


        repoUsers.createUser(user);


        return ResponseEntity.ok("User registered succesfully");

    }
}
