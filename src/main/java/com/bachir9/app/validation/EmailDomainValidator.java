package com.bachir9.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class EmailDomainValidator implements ConstraintValidator<NonDisposableEmail, String> {

    private final Set<String> blocked;

    public EmailDomainValidator(@Value("${app.security.disposable-email}") final List<String> allowedDomains) {
        this.blocked = allowedDomains.stream().map(String::toLowerCase).collect(Collectors.toSet());
    }


    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || !email.contains("@")) {
            return true;
        }
        final var atIndex = email.lastIndexOf('@') + 1;
        final var dotIndex = email.lastIndexOf('.');
        final var domain = email.substring(atIndex, dotIndex).toLowerCase();
        return !this.blocked.contains(domain);
    }
}
