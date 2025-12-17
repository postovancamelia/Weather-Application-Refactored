
package org.example;

import org.example.config.RatingConfig;
import org.example.model.Weather;
import org.example.repo.InMemoryEarthWeatherHistoryRepository;
import org.example.repo.InMemoryMarsWeatherHistoryRepository;
import org.example.repo.WeatherHistoryRepository;
import org.example.service.DayRatingService;
import org.example.service.MarsValidationService;
import org.example.service.ValidationResult;
import org.example.service.EarthValidationService;

import java.time.LocalDateTime;
import java.util.OptionalInt;

public class Main {

    public static void main(String[] args) {
        // --- 1) Repository (history store) ---
        InMemoryEarthWeatherHistoryRepository earthHistoryRepo = new InMemoryEarthWeatherHistoryRepository();

        // Seed history with some past readings so prior bounds exist
        earthHistoryRepo.recordTemperature(-10);
        earthHistoryRepo.recordTemperature(35);
        printHistory("Initial history", earthHistoryRepo);

        // --- 2) Validation service (constructor injection) ---
        EarthValidationService validator = new EarthValidationService(earthHistoryRepo);

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
        printHistory("History after TODAY", earthHistoryRepo);

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
        printHistory("Final history after YESTERDAY", earthHistoryRepo);

        // ---------- MARS ----------
        InMemoryMarsWeatherHistoryRepository marsHistoryRepo = new InMemoryMarsWeatherHistoryRepository();
        // Seed Mars history (Mars is much colder—use values that make sense for your domain)
        marsHistoryRepo.recordTemperature(-60);
        marsHistoryRepo.recordTemperature(5); // occasional warm daytime near equator
        printHistory("MARS: Initial history", marsHistoryRepo);

        MarsValidationService marsValidator = new MarsValidationService(marsHistoryRepo);

        // If Mars should have different rating rules, adjust config; otherwise reuse Earth’s:
        RatingConfig marsRatingConfig = new RatingConfig(
                8,    // clearSkyBonus (tweak for Mars if desired)
                15,   // dryAirBonus
                -2,   // windyPenalty (Mars dust storms harsher?)
                -1,   // rainyPenalty (likely irrelevant on Mars; kept for API symmetry)
                -2,   // snowyPenalty
                40    // comboBonus
        );
        DayRatingService marsRater = new DayRatingService(marsRatingConfig);

        Weather marsToday = new Weather(
                LocalDateTime.now(),
                false,
                10,                 // stronger winds common on Mars
                -20,                // typical cold temperature
                5,                  // very low humidity
                30_000L,            // clear visibility
                700                 // different atmospheric pressure
        );

        System.out.println("\n--- MARS: Validating TODAY ---");
        ValidationResult marsResToday = marsValidator.validateAndUpdate(marsToday);
        if (!marsResToday.isValid()) {
            System.out.println("INVALID (mars today):");
            marsResToday.errors().forEach(System.out::println);
        } else {
            marsResToday.warnings().forEach(w -> System.out.println("WARN (mars today): " + w));
            System.out.println("History updated (mars today): " + marsResToday.historyUpdated());
            int ratingMarsToday = marsRater.getDayRating(marsToday);
            System.out.println("Weather (mars today): " + marsToday);
            System.out.println("Day rating (mars today): " + ratingMarsToday);
        }
        printHistory("MARS: History after TODAY", marsHistoryRepo);

        Weather marsYesterday = new Weather(
                LocalDateTime.now().minusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0),
                false,
                6,
                -5,                 // a relatively warm day on Mars
                3,
                40_000L,
                705
        );

        System.out.println("\n--- MARS: Validating YESTERDAY ---");
        ValidationResult marsResYesterday = marsValidator.validateAndUpdate(marsYesterday);
        if (!marsResYesterday.isValid()) {
            System.out.println("INVALID (mars yesterday):");
            marsResYesterday.errors().forEach(System.out::println);
        } else {
            marsResYesterday.warnings().forEach(w -> System.out.println("WARN (mars yesterday): " + w));
            System.out.println("History updated (mars yesterday): " + marsResYesterday.historyUpdated());
            int ratingMarsYesterday = marsRater.getDayRating(marsYesterday);
            System.out.println("Weather (mars yesterday): " + marsYesterday);
            System.out.println("Day rating (mars yesterday): " + ratingMarsYesterday);
        }
        printHistory("MARS: Final history after YESTERDAY", marsHistoryRepo);
    }

    private static void printHistory(String label, WeatherHistoryRepository repo) {
        OptionalInt minOpt = repo.getMinObservedTemperature();
        OptionalInt maxOpt = repo.getMaxObservedTemperature();
        String minStr = minOpt.isPresent() ? String.valueOf(minOpt.getAsInt()) : "(none)";
        String maxStr = maxOpt.isPresent() ? String.valueOf(maxOpt.getAsInt()) : "(none)";
        System.out.printf("%s → minObservedTemp=%s, maxObservedTemp=%s%n", label, minStr, maxStr);
    }
}
