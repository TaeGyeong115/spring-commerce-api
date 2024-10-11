package io.taylor.wantedpreonboardingchallengebackend20.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUncaughtExceptions(Exception e) {
        Map<String, Object> body = new HashMap<>();
        body.put("resultCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("resultMsg", Objects.requireNonNull(e.getMessage()));
        body.put("timestamp", System.currentTimeMillis());
        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        body.put("path", ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getRequestURI());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("resultCode", HttpStatus.BAD_REQUEST.value());
        body.put("resultMsg", Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        body.put("timestamp", System.currentTimeMillis());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("path", ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getRequestURI());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleCaughtExceptions(ResponseStatusException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("resultCode", e.getStatusCode().value());
        body.put("resultMsg", e.getReason());
        body.put("timestamp", System.currentTimeMillis());
        body.put("error", e.getReason());
        body.put("path", ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getRequestURI());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("resultCode", HttpStatus.UNAUTHORIZED.value());
        body.put("resultMsg", e.getMessage());
        body.put("timestamp", System.currentTimeMillis());
        body.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        body.put("path", ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getRequestURI());

        return ResponseEntity.badRequest().body(body);
    }
}