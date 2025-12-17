package org.example.service;


import org.example.model.Weather;
import org.example.repo.InMemoryEarthWeatherHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    private InMemoryEarthWeatherHistoryRepository historyRepo;
    private EarthValidationService validator;

    @BeforeEach
    void setup() {
        historyRepo = new InMemoryEarthWeatherHistoryRepository();
        validator = new EarthValidationService(historyRepo);

        // Seed historical bounds
        historyRepo.recordTemperature(-10);
        historyRepo.recordTemperature(35);
    }

    @Test
    void invalid_structural_fields_block_update_and_return_errors() {
        Weather bad = new Weather(
                LocalDateTime.of(2025, 12, 8, 12, 12, 12),
                true,
                -1,       //  windSpeed negative -> error
                22,
                150,      // cloudsPercentage > 100 -> error
                10_000L,
                650       // pressure < 700 -> error
        );

        ValidationResult result = validator.validateAndUpdate(bad);

        assertFalse(result.isValid(), "Structural errors should make it invalid");
        assertFalse(result.historyUpdated(), "History must NOT update when invalid");
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("windSpeed")), "Expect windSpeed error");
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("cloudsPercentage")), "Expect clouds error");
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("pressure")), "Expect pressure error");
    }

    @Test
    void new_record_temperature_emits_warning_but_updates_history() {
        Weather hotRecord = new Weather(
                LocalDateTime.of(2025, 12, 8, 12, 12, 12),
                true,
                3,        // valid
                40,       // > prior max 35 -> warning, but still valid
                40,       // valid clouds
                10_000L,
                750       // valid pressure
        );

        ValidationResult result = validator.validateAndUpdate(hotRecord);

        assertTrue(result.isValid());
        assertTrue(result.historyUpdated());
        assertTrue(result.warnings().stream().anyMatch(w -> w.contains("above prior max")),
                "Should warn about new record high");
    }
}