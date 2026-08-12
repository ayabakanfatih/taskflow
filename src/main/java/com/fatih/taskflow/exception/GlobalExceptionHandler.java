package com.fatih.taskflow.exception;

import tools.jackson.databind.exc.InvalidFormatException;
import com.fatih.taskflow.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Arrays;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---------- 404: Kaynak bulunamadi ----------

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiError> handleTaskNotFound(
            TaskNotFoundException ex, HttpServletRequest request) {

        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ApiError> handleProjectNotFound(
            ProjectNotFoundException ex, HttpServletRequest request) {

        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest request) {

        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    // ---------- 409: Cakisma ----------

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ApiError> handleEmailInUse(
            EmailAlreadyInUseException ex, HttpServletRequest request) {

        return build(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(
            InvalidCredentialsException ex, HttpServletRequest request) {

        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
    }

    // ---------- 400: Validation hatasi ----------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        return build(
                HttpStatus.BAD_REQUEST,
                "Gönderilen veri geçersiz",
                request,
                details
        );
    }

    // ---------- 400: JSON govdesi okunamadi ----------

@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableBody(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String message = "İstek gövdesi okunamadı. Geçerli bir JSON gönderdiğinizden emin olun.";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife
                && ife.getTargetType() != null
                && ife.getTargetType().isEnum()) {

            String allowedValues = Arrays.stream(ife.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            message = "Geçersiz değer: '" + ife.getValue()
                    + "'. İzin verilen değerler: " + allowedValues;
        }

        return build(HttpStatus.BAD_REQUEST, message, request, null);
    }

    // ---------- 400: URL parametresi yanlis tipte ----------

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String message = "'" + ex.getName() + "' parametresi geçersiz: " + ex.getValue();

        return build(HttpStatus.BAD_REQUEST, message, request, null);
    }

    // ---------- 400: Gecersiz siralama alani ----------

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ApiError> handleInvalidSortField(
            InvalidSortFieldException ex, HttpServletRequest request) {

        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiError> handleInvalidDataAccess(
            InvalidDataAccessApiUsageException ex, HttpServletRequest request) {

        Throwable cause = ex.getCause();
        if (cause instanceof PropertyReferenceException pre) {
            return handleInvalidSortProperty(pre, request);
        }

        return build(HttpStatus.BAD_REQUEST,
                "Geçersiz sorgu parametresi gönderildi", request, null);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiError> handleInvalidSortProperty(
            PropertyReferenceException ex, HttpServletRequest request) {

        String message = "Geçersiz sıralama alanı: '" + ex.getPropertyName()
                + "'. Örnek kullanım: sort=createdAt,desc";

        return build(HttpStatus.BAD_REQUEST, message, request, null);
    }

    // ---------- 500: Beklenmeyen her sey ----------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception ex, HttpServletRequest request) {

        log.error("Beklenmeyen hata: {}", request.getRequestURI(), ex);

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Beklenmeyen bir hata oluştu",
                request,
                null
        );
    }

    // ---------- Ortak yardimci ----------

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            List<String> details) {

        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                details
        );

        return ResponseEntity.status(status).body(body);
    }
}
