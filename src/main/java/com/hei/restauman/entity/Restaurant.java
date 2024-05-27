package com.hei.restauman.entity;

import lombok.*;


@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Restaurant {
    private int id;
    private String localisation;

    public Restaurant(String localisation) {
        this.localisation = localisation;
    }
}
