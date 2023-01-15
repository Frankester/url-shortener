package com.example.demo.services;

import com.example.demo.exceptions.InvalidKeySiteException;
import com.example.demo.exceptions.KeySiteInUseException;
import com.example.demo.models.URLLong;
import com.example.demo.models.URLShort;
import com.example.demo.repositories.URLJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class URLService {

    @Autowired
    private URLJPA repo;

    private final String invalidos = ".,*/-{}´()+'¿?\"ñ&%#@!¡°| \\";

    public URLShort shortUrl(URLLong longUrl) throws KeySiteInUseException, InvalidKeySiteException {

        String key = longUrl.getKeySite().toLowerCase();

        Optional<URLLong> longSiteOp = this.repo.findByKeySite(key);

        if(longSiteOp.isPresent()){
            URLLong urlInUse = longSiteOp.get();

            throw new KeySiteInUseException("el keySite: "+urlInUse.getKeySite()+ " ya esta en uso", urlInUse);
        }

        for(Character c : invalidos.toCharArray()){
            CharSequence invalidSeq = c.toString();

            if(key.contains(invalidSeq)){
                throw new InvalidKeySiteException("el KeySite no puede contener ninguno de estos caracteres: "+invalidos, key);
            }
        }


        longUrl.setKeySite(key);

        this.repo.save(longUrl);


        URLShort shortURl = new URLShort();

        shortURl.setUrl(longUrl.getUrl());
        shortURl.setCreatedAt(LocalDate.now());
        shortURl.setKeySite(key);

        return shortURl;
    }

    public URLLong getLongUrl(String key) throws InvalidKeySiteException {
        Optional<URLLong> url = this.repo.findByKeySite(key);

        if(url.isEmpty()){
            throw new InvalidKeySiteException("No existe la url con el keySite: " + key, key);
        }

        return url.get();
    }

}
