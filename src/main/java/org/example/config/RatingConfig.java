package org.example.config;



public final class RatingConfig {
    private final int coeffDayOfWeek, coeffRain, coeffWind, coeffClouds, coeffTemperature, coeffCombo;

    public RatingConfig(int coeffDayOfWeek, int coeffRain, int coeffWind,
                        int coeffClouds, int coeffTemperature, int coeffCombo) {
        this.coeffDayOfWeek = coeffDayOfWeek;
        this.coeffRain = coeffRain;
        this.coeffWind = coeffWind;
        this.coeffClouds = coeffClouds;
        this.coeffTemperature = coeffTemperature;
        this.coeffCombo = coeffCombo;
    }

    public int coeffDayOfWeek() { return coeffDayOfWeek; }
    public int coeffRain() { return coeffRain; }
    public int coeffWind() { return coeffWind; }
    public int coeffClouds() { return coeffClouds; }
    public int coeffTemperature() { return coeffTemperature; }
    public int coeffCombo() { return coeffCombo; }


}
