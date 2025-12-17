package org.example.service;

import org.example.config.RatingConfig;
import org.example.model.Weather;

public interface RatingService {
    int getRating(Weather weather);
}
