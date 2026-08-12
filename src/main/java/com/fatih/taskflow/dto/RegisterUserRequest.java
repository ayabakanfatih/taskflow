package com.fatih.taskflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterUserRequest {

    @NotBlank(message = "E-posta boş olamaz")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Size(max = 180, message = "E-posta en fazla 180 karakter olabilir")
    private String email;

    @NotBlank(message = "Ad soyad boş olamaz")
    @Size(max = 120, message = "Ad soyad en fazla 120 karakter olabilir")
    private String fullName;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 8, max = 72, message = "Şifre 8-72 karakter arasında olmalıdır")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
