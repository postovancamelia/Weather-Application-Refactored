package org.example.comparator;

import org.example.model.Weather;
import org.example.service.DayRatingService;

import java.util.Comparator;

public class WeatherDayRatingComparator implements Comparator<Weather> {

    private final DayRatingService rater;
    private final boolean descending; // true = best rating first

    public WeatherDayRatingComparator(DayRatingService rater, boolean descending) {
        this.rater = rater;
        this.descending = descending;
    }

    @Override
    public int compare(Weather a, Weather b) {
        int ra = rater.getDayRating(a);
        int rb = rater.getDayRating(b);

        int cmp = Integer.compare(ra, rb);
        if (descending) cmp = -cmp; // reverse => higher rating first

        // Tie-breaker: by date (newer first)
        if (cmp == 0) {
            cmp = b.getDate().compareTo(a.getDate());
        }

        return cmp;
    }
}
