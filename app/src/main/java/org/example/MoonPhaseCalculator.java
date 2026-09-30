package org.example;

import java.time.LocalDate;


/**
 * Calculates phases of the moon for a given date.
 */
public interface MoonPhaseCalculator {
    String[] moonPhaseStrings = {
            "New Moon",
            "Waxing Crescent",
            "First Quarter",
            "Waxing Gibbous",
            "Full Moon",
            "Waning Gibbous",
            "Last Quarter",
            "Waning Crescent"
    };

    /**
     * Legacy method to determine if the given date falls on a new moon
     * @param date the date to check
     * @return true if new moon, otherwise false
     */
    default boolean isNewMoon(LocalDate date) {
        return "New Moon".equals(getMoonPhase(date));
    }
    /**
     * Determine the phase of the moon on the given date
     * @param date the date to check
     * @return the MoonPhase corresponding to the date
     */
    String getMoonPhase(LocalDate date);
}
