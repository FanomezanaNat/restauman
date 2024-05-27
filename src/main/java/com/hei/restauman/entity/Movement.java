package com.hei.restauman.entity;

import com.hei.restauman.entity.enums.MovementType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Movement {
    private int id;
    private IngredientMenu ingredientMenu;
    private Instant movementDatetime;
    private MovementType type;
    private Restaurant restaurant;
    private double quantity;

    public Movement(IngredientMenu ingredientMenu, Instant movementDatetime, MovementType type, Restaurant restaurant, double quantity) {
        this.ingredientMenu = ingredientMenu;
        this.movementDatetime = movementDatetime;
        this.type = type;
        this.restaurant = restaurant;
        this.quantity = quantity;
    }

    public Movement(int id, IngredientMenu ingredientMenu, Instant movementDatetime, MovementType type, Restaurant restaurant, double quantity) {
        this.id = id;
        this.ingredientMenu = ingredientMenu;
        this.movementDatetime = movementDatetime;
        this.type = type;
        this.restaurant = restaurant;
        this.quantity = quantity;
    }
}
