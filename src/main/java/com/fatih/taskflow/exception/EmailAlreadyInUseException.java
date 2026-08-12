package com.fatih.taskflow.exception;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
        super("Bu e-posta adresi zaten kayıtlı: " + email);
    }
}
