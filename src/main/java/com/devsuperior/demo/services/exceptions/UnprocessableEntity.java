package com.devsuperior.demo.services.exceptions;

public class UnprocessableEntity extends RuntimeException {
    public UnprocessableEntity(String msg) {
        super(msg);
    }
}
