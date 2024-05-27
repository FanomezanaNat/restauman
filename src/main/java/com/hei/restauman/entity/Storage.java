package com.hei.restauman.entity;

import lombok.*;

import java.time.Instant;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Storage {
    private int id;
    private IngredientTemplate ingredientTemplate;
    private int idRestaurant;
    private double value;
    private Instant supplyDate;

    public Storage(IngredientTemplate ingredientTemplate, int idRestaurant, double value, Instant supplyDate) {
        this.ingredientTemplate = ingredientTemplate;
        this.idRestaurant = idRestaurant;
        this.value = value;
        this.supplyDate = supplyDate;
    }
}
