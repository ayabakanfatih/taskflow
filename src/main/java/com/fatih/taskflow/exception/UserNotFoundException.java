package com.fatih.taskflow.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Kullanıcı bulunamadı. id: " + id);
    }
}
