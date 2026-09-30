package org.example;

import java.time.LocalDate;


/**
 * Calculates phases of the moon for a given date.
 */
public interface MoonPhaseCalculator {
    /**
     * Legacy method to determine if the given date falls on a new moon
     * @param date the date to check
     * @return true if new moon, otherwise false
     */
    default boolean isNewMoon(LocalDate date) {
        return MoonPhase.NEW_MOON == getMoonPhase(date);
    }
    /**
     * Determine the phase of the moon on the given date
     * @param date the date to check
     * @return the MoonPhase corresponding to the date
     */
    MoonPhase getMoonPhase(LocalDate date);
}
