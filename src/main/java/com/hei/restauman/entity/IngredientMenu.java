package com.hei.restauman.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientMenu {
    private int id;
    private Menu menu;
    private IngredientTemplate ingredientTemplate;
    private double quantity;


    public IngredientMenu(Menu menu, IngredientTemplate ingredientTemplate, double quantity) {
        this.menu = menu;
        this.ingredientTemplate = ingredientTemplate;
        this.quantity = quantity;
    }

    public IngredientMenu(int id, Menu menu, IngredientTemplate ingredientTemplate, double quantity) {
        this.id = id;
        this.menu = menu;
        this.ingredientTemplate = ingredientTemplate;
        this.quantity = quantity;
    }
}
