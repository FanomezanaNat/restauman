package com.hei.restauman.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Unit {
    private int id;
    private String name;

    public Unit(String name) {
        this.name = name;
    }
}
