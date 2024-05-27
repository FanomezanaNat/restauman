package com.hei.restauman.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Supply {
    private String ingredientTemplateName;
    private double quantity;
    private int idRestaurant;
}
