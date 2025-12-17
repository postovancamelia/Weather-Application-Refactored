
package org.example.service;

import org.example.model.Weather;
import org.example.repo.InMemoryMarsWeatherHistoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * Mars validation with hard-coded final constants.
 * Use only if these ranges are truly invariant for your application.
 */
public class MarsValidationService implements ValidationService {
    private final InMemoryMarsWeatherHistoryRepository history;

    // --- Structural bounds (units must match Weather getters) ---
    private static final int MIN_ALLOWED_PRESSURE = 100;   // example value
    private static final int MAX_ALLOWED_PRESSURE = 1200;  // example value
    private static final int MIN_ALLOWED_CLOUDS = 0;     // %
    private static final int MAX_ALLOWED_CLOUDS = 100;   // %

    public MarsValidationService(InMemoryMarsWeatherHistoryRepository  history) {
        this.history = history;
    }

    @Override
    public boolean isValid(Weather w) {
        return validateAndUpdate(w).isValid();
    }

    @Override
    public ValidationResult validateAndUpdate(Weather w) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // --- Structural (reject) ---
        if (w.getWindSpeed() < 0) {
            errors.add("windSpeed cannot be negative");
        }
        if (w.getCloudsPercentage() < MIN_ALLOWED_CLOUDS
                || w.getCloudsPercentage() > MAX_ALLOWED_CLOUDS) {
            errors.add("cloudsPercentage must be between " + MIN_ALLOWED_CLOUDS
                    + " and " + MAX_ALLOWED_CLOUDS);
        }
        if (w.getPressure() < MIN_ALLOWED_PRESSURE
                || w.getPressure() > MAX_ALLOWED_PRESSURE) {
            errors.add("pressure must be in range "
                    + MIN_ALLOWED_PRESSURE + ".." + MAX_ALLOWED_PRESSURE);
        }

        // --- Historical (warn) ---
        OptionalInt minOpt = history.getMinObservedTemperature();
        OptionalInt maxOpt = history.getMaxObservedTemperature();

        if (minOpt.isPresent() && maxOpt.isPresent()) {
            int priorMin = minOpt.getAsInt();
            int priorMax = maxOpt.getAsInt();
            int t = w.getTemperature();
            if (t < priorMin) {
                warnings.add("temperature below prior min (" + priorMin + "): new record " + t);
            } else if (t > priorMax) {
                warnings.add("temperature above prior max (" + priorMax + "): new record " + t);
            }
        } else {
            warnings.add("no historical bounds available yet");
        }

        // --- Update history only if structurally valid ---
        boolean updated = false;
        if (errors.isEmpty()) {
            history.recordTemperature(w.getTemperature());
            updated = true;
        }
        return new ValidationResult(errors, warnings, updated);
    }
}