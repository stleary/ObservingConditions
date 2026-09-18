package org.example;

import java.time.LocalDate;


/**
 * Calculates phases of the moon for a given date.
 */
public interface MoonPhaseCalculator {
    boolean isNewMoon(LocalDate date);
}
