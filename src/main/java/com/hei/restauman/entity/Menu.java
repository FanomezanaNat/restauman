package com.hei.restauman.entity;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Menu {
    private int id;
    private String name;
    private double price;

    public Menu(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Menu(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
