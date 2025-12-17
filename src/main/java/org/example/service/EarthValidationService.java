
package org.example.service;

import org.example.model.Weather;
import org.example.repo.InMemoryEarthWeatherHistoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class EarthValidationService implements ValidationService {

    private final InMemoryEarthWeatherHistoryRepository history;

    public EarthValidationService(InMemoryEarthWeatherHistoryRepository history) {
        this.history = history;
    }

    /** Backward-compatible boolean check */
    @Override
    public boolean isValid(Weather w) {
        return validateAndUpdate(w).isValid();
    }

    /**
     * Validates structrual rules (errors) and emits warnings if the temperature
     * is outside the PRIOR historical bounds. If there are NO errors, the history
     * is updated (i.e., the new temp becomes the new min/max as needed).
     */
    @Override
    public ValidationResult validateAndUpdate(Weather w) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // --- Structural (reject) ---
        if (w.getWindSpeed() < 0) {
            errors.add("windSpeed cannot be negative");
        }
        if (w.getCloudsPercentage() < 0 || w.getCloudsPercentage() > 100) {
            errors.add("cloudsPercentage must be between 0 and 100");
        }
        if (w.getPressure() < 700 || w.getPressure() > 800) {
            errors.add("pressure must be in range 700..800 hPa");
        }

        // --- Historical (warn, do NOT reject) ---
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
