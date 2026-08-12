package com.fatih.taskflow.exception;

import java.util.Set;

public class InvalidSortFieldException extends RuntimeException {

    public InvalidSortFieldException(String field, Set<String> allowedFields) {
        super("Geçersiz sıralama alanı: '" + field
                + "'. İzin verilen alanlar: " + String.join(", ", allowedFields));
    }
}
