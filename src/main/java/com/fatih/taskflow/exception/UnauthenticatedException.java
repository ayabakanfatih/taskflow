package com.fatih.taskflow.exception;

public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException() {
        super("Bu işlem için giriş yapmanız gerekiyor");
    }
}
