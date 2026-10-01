package org.example;

import java.time.LocalDate;


/**
 * Calculates phases of the moon for a given date.
 */
public interface MoonPhaseCalculator {
    final static String NEW_MOON = "New Moon";
    final static String WAXING_CRESCENT = "Waxing Crescent";
    final static String FIRST_QUARTER = "First Quarter";
    final static String WAXING_GIBBOUS = "Waxing Gibbous";
    final static String FULL_MOON = "Full Moon";
    final static String WANING_GIBBOUS = "Waning Gibbous";
    final static String LAST_QUARTER = "Last Quarter";
    final static String WANING_CRESCENT = "Waning Crescent";

    String[] moonPhaseStrings = {
        NEW_MOON,
        WAXING_CRESCENT,
        FIRST_QUARTER,
        WAXING_GIBBOUS,
        FULL_MOON,
        WANING_GIBBOUS,
        LAST_QUARTER,
        WANING_CRESCENT
    };

    /**
     * Legacy method to determine if the given date falls on a new moon
     * @param date the date to check
     * @return true if new moon, otherwise false
     */
    default boolean isNewMoon(LocalDate date) {
        return NEW_MOON.equals(getMoonPhase(date));
    }
    /**
     * Determine the phase of the moon on the given date
     * @param date the date to check
     * @return the MoonPhase corresponding to the date
     */
    String getMoonPhase(LocalDate date);
}
