package org.example.repo;

import org.example.model.Weather;
import org.example.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWeatherHistoryRepositoryTest {

    private WeatherHistoryRepository historyRepo = new InMemoryWeatherHistoryRepository();

    @Test
    void checkMinMaxAfterRecordTemperature() {

        historyRepo.recordTemperature(-10);
        historyRepo.recordTemperature(35);
        assertEquals(35,historyRepo.getMaxObservedTemperature().getAsInt());
        assertEquals(-10,historyRepo.getMinObservedTemperature().getAsInt());
    }

    @Test
    void checkMinMaxAfterRecordTemperatureMultipleTimes() {

        historyRepo.recordTemperature(-10);
        historyRepo.recordTemperature(35);
        historyRepo.recordTemperature(12);
        historyRepo.recordTemperature(37);
        historyRepo.recordTemperature(-13);
        assertEquals(37,historyRepo.getMaxObservedTemperature().getAsInt());
        assertEquals(-13,historyRepo.getMinObservedTemperature().getAsInt());
    }


    @Test
    void checkMinMaxBeforeRecordTemperature() {

        assertEquals(OptionalInt.empty(),historyRepo.getMaxObservedTemperature());
        assertEquals(OptionalInt.empty(),historyRepo.getMinObservedTemperature());
    }

    @Test
    void checkMinMaxAfterRecordTemperatureOnce() {

        historyRepo.recordTemperature(16);
        assertEquals(16,historyRepo.getMaxObservedTemperature().getAsInt());
        assertEquals(16,historyRepo.getMinObservedTemperature().getAsInt());
    }

}