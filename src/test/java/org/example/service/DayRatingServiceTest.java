package org.example.service;

import org.example.config.RatingConfig;
import org.example.model.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DayRatingServiceTest {
    private Weather wednesday;
    private Weather thursday;
    private RatingConfig ratingConfig;
    private DayRatingService rater;

    @BeforeEach
    void setup() {
        ratingConfig = new RatingConfig(
                10,
                20,
                -1,
                -1,
                -2,
                50
        );

        rater = new DayRatingService(ratingConfig);


    }


    @Test
    void getDayRating() {
        wednesday = new Weather(
                LocalDateTime.of(2025, 12, 8, 12, 12, 12),
                true,
                3,
                22,
                40,
                10_000L,
                750
        );
        int ratingOnWednesday= rater.getDayRating(wednesday);


        thursday = new Weather(
                LocalDateTime.of(2025, 12, 6, 12, 12, 12),
                false,
                0,
                22,
                10,
                10_000L,
                750
        );
        int ratingOnThursday= rater.getDayRating(thursday);

        assertEquals(-49,ratingOnWednesday);
        assertEquals(14,ratingOnThursday);
    }

}