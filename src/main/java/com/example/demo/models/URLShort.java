package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class URLShort {

    @NotBlank
    private String url;
    @NotBlank
    private String keySite;

    private LocalDate createdAt;
}
