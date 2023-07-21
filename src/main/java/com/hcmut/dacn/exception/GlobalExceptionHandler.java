package com.hcmut.dacn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundEntityException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleNotFound(NotFoundEntityException e){
        return e.getMessage();
    }

    @ExceptionHandler(AlreadyExistedEntityException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String handleNotFound(AlreadyExistedEntityException e){
        return e.getMessage();
    }
}
