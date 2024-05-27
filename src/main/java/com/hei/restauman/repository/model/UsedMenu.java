package com.hei.restauman.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UsedMenu {
    private String ingredientName;
    private String menu;
    private double quantityMax;
    private String unit;
}
