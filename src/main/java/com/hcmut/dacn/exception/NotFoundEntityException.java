package com.hcmut.dacn.exception;

import lombok.AllArgsConstructor;

public class NotFoundEntityException extends RuntimeException {
    public NotFoundEntityException(String message){
        super(message);
    }
}
