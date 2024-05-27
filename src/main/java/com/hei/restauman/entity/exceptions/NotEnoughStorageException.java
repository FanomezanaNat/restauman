package com.hei.restauman.entity.exceptions;

public class NotEnoughStorageException extends RuntimeException {
    public NotEnoughStorageException(String message){
        super(message);
    }

}
