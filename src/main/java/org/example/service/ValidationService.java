package org.example.service;

import org.example.model.Weather;

public interface ValidationService {
    boolean isValid(Weather w);

    ValidationResult validateAndUpdate(Weather w);
}
