package org.example.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class WeatherTest {

    private Weather monday;
    private Weather tuesday;
    private Weather saturday;

    @BeforeEach
    void setup() {
        monday = new Weather(
                LocalDateTime.of(2025, 12, 8,12,12,12),
                true,
                3,
                22,
                40,
                10_000L,
                750
        );

        tuesday = new Weather(
                LocalDateTime.of(2025, 12, 6,12,12,12),
                false,
                0,
                22,
                10,
                10_000L,
                750
        );
        saturday = new Weather(
                LocalDateTime.of(2025, 12, 6,12,12,12),
                false,
                0,
                22,
                0,
                10_000L,
                750
        );
    }

    @Test
    void isWeekend_returnsFalse_onMonday() {
    assertFalse(monday.isWeekend());
    }

    @Test
    void isWeekend_returnsTrue_onSaturday() {
        assertTrue(saturday.isWeekend());
    }

    @Test
    void isCombo_returnsTrue_whenAllConditionsMatch() {
        assertTrue(saturday.isCombo());
    }


    @Test
    void isCombo_returnsFalse_whenAllConditionsDontMatch() {
        assertFalse(monday.isCombo());
    }

    @Test
    void isCombo_returnsFalse_whenOneConditionDoesntMatch() {
        assertFalse(tuesday.isCombo());
    }

    @Test
    void isDayGoodForWalk_returnsTrue_whenConditionsMatch() {
        assertTrue(tuesday.isDayGoodForWalk());
    }

    @Test
    void isDayGoodForWalk_returnsFalse_whenItRains() {
        assertFalse(monday.isDayGoodForWalk());
    }


}