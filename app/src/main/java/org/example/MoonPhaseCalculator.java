package org.example;

import java.time.LocalDate;


/**
 * Calculates phases of the moon for a given date.
 */
public interface MoonPhaseCalculator {
    default public boolean isNewMoon(LocalDate date) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
