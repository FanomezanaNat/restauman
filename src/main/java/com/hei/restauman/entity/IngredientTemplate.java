package com.hei.restauman.entity;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class IngredientTemplate {
    private int id;
    private String name;
    private double unitPrice;
    private Unit unit;

    public IngredientTemplate(String name, double unitPrice, Unit unit) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.unit = unit;
    }
}
