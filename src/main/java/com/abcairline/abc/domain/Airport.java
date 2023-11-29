package com.abcairline.abc.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Airport {

    @Id
    private String IATACode;
    private String name;
    private String continent;
    private String country;
    private String city;
    private String imageUrl;
}
