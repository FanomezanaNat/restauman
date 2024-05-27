package com.hei.restauman.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class MenuSale {
    private String restaurantName;
    private String menuName;
    private int numMenusSold;
    private double totalQuantitySold;
    private double totalAmountSold;
}
