package com.abcairline.abc.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Airplane {
    @Id
    private String model;
    private String manufacturer;
    private String series;
    private int totalSeat;
}
