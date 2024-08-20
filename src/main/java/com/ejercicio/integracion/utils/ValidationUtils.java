package com.ejercicio.integracion.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
@Component
public class ValidationUtils {

    public boolean isValidEmail(String email) {
        return Pattern.matches(ValidationConstants.EMAIL_REGEX, email);
    }

    public boolean isValidPassword(String password) {
        return Pattern.matches(ValidationConstants.PASSWORD_REGEX, password);
    }
}
