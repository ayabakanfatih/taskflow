package com.fatih.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateProjectRequest {

    @NotBlank(message = "Project adı boş olamaz")
    @Size(max = 100, message = "Project adı en fazla 100 karakter olabilir")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
