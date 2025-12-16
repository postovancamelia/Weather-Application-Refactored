package org.example.repo;


import java.util.OptionalInt;

public interface WeatherHistoryRepository {
    OptionalInt getMinObservedTemperature();
    OptionalInt getMaxObservedTemperature();

    // Optionally: update history when you insert new readings
    void recordTemperature(int temperature);
}
