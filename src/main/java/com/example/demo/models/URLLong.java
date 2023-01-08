package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="url")
@Getter
@Setter
public class URLLong extends Persistence{

    @NotBlank
    @Column(name = "llave")
    private String keySite;

    @NotBlank
    @Column(name= "url", columnDefinition = "TEXT")
    private String url;


}
