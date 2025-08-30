package com.bachir9.app.handler;

import com.bachir9.app.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.bachir9.app.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(final BusinessException ex) {
        final var body = ErrorResponse.builder()
                .code(ex.getErrorCode()
                        .getCode())
                .message(ex.getMessage())
                .build();
        log.info("Business Exception: {}", body);
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getErrorCode()
                        .getStatus() != null ? ex.getErrorCode()
                        .getStatus() : BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleBusiness() {
        final var body = ErrorResponse.builder()
                .code(ERR_USER_DISABLED.getCode())
                .message(ERR_USER_DISABLED.getDefaultMessage())
                .build();
        return ResponseEntity.status(UNAUTHORIZED)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException exp) {
        final List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        exp.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    final var fieldName = ((FieldError) error).getField();
                    final var errorCode = error.getDefaultMessage();
                    errors.add(ErrorResponse.ValidationError.builder()
                            .field(fieldName)
                            .code(errorCode)
                            .message(errorCode)
                            .build());
                });
        final var errorResponse = ErrorResponse.builder()
                .validationErrors(errors)
                .build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(final BadCredentialsException exception) {
        log.debug(exception.getMessage(), exception);
        final var response = ErrorResponse.builder()
                .message(BAD_CREDENTIALS.getDefaultMessage())
                .code(BAD_CREDENTIALS.getCode())
                .build();
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final EntityNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        final var errorResponse = ErrorResponse.builder()
                .code("TBD")
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final UsernameNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        final var response = ErrorResponse.builder()
                .code(USERNAME_NOT_FOUND.getCode())
                .message(USERNAME_NOT_FOUND.getDefaultMessage())
                .build();
        return new ResponseEntity<>(response,
                NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(final AuthorizationDeniedException exception) {
        log.debug(exception.getMessage(), exception);
        final var response = ErrorResponse.builder()
                .message("You are not authorized to perform this operation")
                .build();
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        final var response = ErrorResponse.builder()
                .code(INTERNAL_EXCEPTION.getCode())
                .message(INTERNAL_EXCEPTION.getDefaultMessage())
                .build();
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

}
