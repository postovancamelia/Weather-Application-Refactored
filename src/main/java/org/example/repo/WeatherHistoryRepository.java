package org.example.repo;


import java.util.OptionalInt;

public interface WeatherHistoryRepository {
    OptionalInt getMinObservedTemperature();
    OptionalInt getMaxObservedTemperature();

    void recordTemperature(int temperature);
}
