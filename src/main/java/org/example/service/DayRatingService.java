package org.example.service;

import org.example.config.RatingConfig;
import org.example.model.Weather;

public class DayRatingService {
    private final RatingConfig ratingConfig;
    public DayRatingService(RatingConfig ratingConfig) {
        this.ratingConfig = ratingConfig;
    }

    public int getDayRating(Weather weather){
        int dayOfTheWeekRating = weather.isWeekend() ? 1 :0;
        int rainingRating = weather.isRaining() ? 0 : 1;
        int comboRating = weather.isCombo() ? 1 : 0;


        return dayOfTheWeekRating * ratingConfig.coeffDayOfWeek()
                + rainingRating * ratingConfig.coeffRain()
                + weather.getWindSpeed() * ratingConfig.coeffWind()
                + weather.getCloudsPercentage() * ratingConfig.coeffClouds()
                + (25 - weather.getTemperature()) * ratingConfig.coeffTemperature()
                + comboRating * ratingConfig.coeffCombo();

    }
}
