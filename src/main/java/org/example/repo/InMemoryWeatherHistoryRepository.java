package org.example.repo;

import java.util.OptionalInt;

public class InMemoryWeatherHistoryRepository implements WeatherHistoryRepository {
    private Integer min; // boxed internal, but exposed as OptionalInt
    private Integer max;

    @Override
    public OptionalInt getMinObservedTemperature() {
        return (min == null) ? OptionalInt.empty() : OptionalInt.of(min);
    }

    @Override
    public OptionalInt getMaxObservedTemperature() {
        return (max == null) ? OptionalInt.empty() : OptionalInt.of(max);
    }

    @Override
    public void recordTemperature(int temperature) {
        if (min == null || temperature < min) min = temperature;
        if (max == null || temperature > max) max = temperature;
    }
}
