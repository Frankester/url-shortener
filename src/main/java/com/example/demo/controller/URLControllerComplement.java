package com.example.demo.controller;

import com.example.demo.exceptions.InvalidKeySiteException;
import com.example.demo.exceptions.KeySiteInUseException;
import com.example.demo.models.URLLong;
import com.example.demo.models.URLShort;
import com.example.demo.services.URLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

@RepositoryRestController
public class URLControllerComplement {

    @Autowired
    URLService service;

    @PostMapping(path = {"/urls/", "/urls"})
    public ResponseEntity<Object> shortUrl(@RequestBody URLLong url) throws KeySiteInUseException, InvalidKeySiteException {

        URLShort shortUrl = service.shortUrl(url);

        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping(path = "/urls/{key}")
    public ResponseEntity<Object> getUrl(@PathVariable("key") String key) throws InvalidKeySiteException {

        URLLong url = service.getLongUrl(key);


        //redirects to the URL
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(url.getUrl()))
                .header(HttpHeaders.CONNECTION, "close")
                .build();
    }

}
