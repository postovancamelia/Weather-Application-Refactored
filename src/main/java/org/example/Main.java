
package org.example;

import org.example.config.RatingConfig;
import org.example.model.Weather;
import org.example.repo.InMemoryWeatherHistoryRepository;
import org.example.repo.WeatherHistoryRepository;
import org.example.service.DayRatingService;
import org.example.service.ValidationResult;
import org.example.service.ValidationService;

import java.time.LocalDateTime;
import java.util.OptionalInt;

public class Main {

    public static void main(String[] args) {
        // --- 1) Repository (history store) ---
        WeatherHistoryRepository historyRepo = new InMemoryWeatherHistoryRepository();

        // Seed history with some past readings so prior bounds exist
        historyRepo.recordTemperature(-10);
        historyRepo.recordTemperature(35);
        printHistory("Initial history", historyRepo);

        // --- 2) Validation service (constructor injection) ---
        ValidationService validator = new ValidationService(historyRepo);

        // --- 3) Rating config (penalties negative; combo is a bonus) ---
        RatingConfig ratingConfig = new RatingConfig(
                10,
                20,
                -1,
                -1,
                -2,
                50
        );
        DayRatingService rater = new DayRatingService(ratingConfig);

        // --- 4) Weather #1 (today) ---
        Weather today = new Weather(
                LocalDateTime.now(),
                false,
                3,
                22,
                40,
                10_000L,
                750
        );

        System.out.println("\n--- Validating TODAY ---");
        ValidationResult resToday = validator.validateAndUpdate(today);
        if (!resToday.isValid()) {
            System.out.println("INVALID (today):");
            resToday.errors().forEach(System.out::println);
        } else {
            resToday.warnings().forEach(w -> System.out.println("WARN (today): " + w));
            System.out.println("History updated (today): " + resToday.historyUpdated());
            int ratingToday = rater.getDayRating(today);
            System.out.println("Weather (today): " + today);
            System.out.println("Day rating (today): " + ratingToday);
        }
        printHistory("History after TODAY", historyRepo);

        // --- 5) Weather #2 (yesterday) designed for a good rating (combo: clear/dry/calm, 25°C) ---
        Weather yesterday = new Weather(
                LocalDateTime.now().minusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0),
                false,
                0,
                25,
                0,
                20_000L,
                750
        );

        System.out.println("\n--- Validating YESTERDAY ---");
        ValidationResult resYesterday = validator.validateAndUpdate(yesterday);
        if (!resYesterday.isValid()) {
            System.out.println("INVALID (yesterday):");
            resYesterday.errors().forEach(System.out::println);
        } else {
            resYesterday.warnings().forEach(w -> System.out.println("WARN (yesterday): " + w));
            System.out.println("History updated (yesterday): " + resYesterday.historyUpdated());
            int ratingYesterday = rater.getDayRating(yesterday);
            System.out.println("Weather (yesterday): " + yesterday);
            System.out.println("Day rating (yesterday): " + ratingYesterday);
        }
        printHistory("Final history after YESTERDAY", historyRepo);
    }

    private static void printHistory(String label, WeatherHistoryRepository repo) {
        OptionalInt minOpt = repo.getMinObservedTemperature();
        OptionalInt maxOpt = repo.getMaxObservedTemperature();
        String minStr = minOpt.isPresent() ? String.valueOf(minOpt.getAsInt()) : "(none)";
        String maxStr = maxOpt.isPresent() ? String.valueOf(maxOpt.getAsInt()) : "(none)";
        System.out.printf("%s → minObservedTemp=%s, maxObservedTemp=%s%n", label, minStr, maxStr);
    }
}
