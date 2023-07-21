package com.hcmut.dacn.exception;

public class AlreadyExistedEntityException extends RuntimeException {
    public AlreadyExistedEntityException(String message){
        super(message);
    }
}
