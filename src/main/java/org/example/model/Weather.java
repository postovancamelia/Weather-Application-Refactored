
package org.example.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Weather {
    private final LocalDateTime date;
    private final boolean raining;
    private final int windSpeed;
    private final int temperature;
    private final int cloudsPercentage;
    private final long distanceVisible;
    private final int pressure;

    public Weather(LocalDateTime date, boolean raining, int windSpeed, int temperature,
                   int cloudsPercentage, long distanceVisible, int pressure) {
        this.date = Objects.requireNonNull(date, "date");
        this.raining = raining;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.cloudsPercentage = cloudsPercentage;
        this.distanceVisible = distanceVisible;
        this.pressure = pressure;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public boolean isRaining() {
        return raining;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getCloudsPercentage() {
        return cloudsPercentage;
    }

    public long getDistanceVisible() {
        return distanceVisible;
    }

    public int getPressure() {
        return pressure;
    }

    // not raining
    // wind (m/s)
    // temperature (°C)
    // clouds (%)
    // visibility (m)
    // pressure (hPa)

    @Override
    public String toString() {
        return "Weather{" +
                "date=" + date +
                ", raining=" + raining +
                ", windSpeed=" + windSpeed +
                ", temperature=" + temperature +
                ", cloudsPercentage=" + cloudsPercentage +
                ", distanceVisible=" + distanceVisible +
                ", pressure=" + pressure +
                '}';
    }

    public boolean isWeekend() {
        return switch (date.getDayOfWeek()) {
            case SATURDAY, SUNDAY -> true;
            default -> false;
        };
    }

    public boolean isCombo(){
        return !raining && cloudsPercentage == 0 && windSpeed == 0;
    }


    public boolean isDayGoodForWalk() {
        boolean tempOk   = getTemperature() >= 15 && getTemperature() <= 30;
        boolean noRain   = !isRaining();
        boolean cloudsOk = getCloudsPercentage() >= 0 && getCloudsPercentage() <= 50;
        boolean windOk   = getWindSpeed() >= 0 && getWindSpeed() <= 3;
        return tempOk && noRain && cloudsOk && windOk;
    }



}