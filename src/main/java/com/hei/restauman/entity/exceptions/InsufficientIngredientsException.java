package com.hei.restauman.entity.exceptions;

public class InsufficientIngredientsException extends RuntimeException {
    public InsufficientIngredientsException(String message) {
        super(message);
    }
}
