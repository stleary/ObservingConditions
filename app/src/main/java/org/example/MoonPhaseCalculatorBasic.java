package org.example;

import java.time.LocalDate;

/**
 * Calculates phases of the moon for a given date in a very basic and limited way
 */
public class MoonPhaseCalculatorBasic implements MoonPhaseCalculator {

    private static final String[] moonPhases = {
        // new         1st qtr       full          last qtr
        "2026-09-10", "2026-09-18", "2026-09-26", "2026-10-03",
        "2026-10-10", "2026-10-18", "2026-10-26", "2026-11-01",
        "2026-11-09", "2026-11-17", "2026-11-24", "2026-12-01",
        "2026-12-09", "2026-12-17", "2026-12-24", "2026-12-30",
        "2027-01-07", "2027-01-15", "2027-01-22", "2027-01-29",
        "2027-02-06", "2027-02-14", "2027-02-20", "2027-02-28",
        "2027-03-08", "2027-03-15", "2027-03-22", "2027-03-30"
    };

    @Override
    public MoonPhase getMoonPhase(LocalDate date) {
        // if date is out of range of our table, just return null
        if (date.isBefore(LocalDate.parse(moonPhases[0])) ||
        date.isAfter(LocalDate.parse(moonPhases[moonPhases.length-1]))) {
            return null;
        }

        for (int i = 0; i < moonPhases.length; i += 4) {
            // check first column (new moon)
            if (date.isEqual(LocalDate.parse(moonPhases[i]))) {
                return MoonPhase.NEW_MOON;
            } else if (isSpanPhase(i, date)) {
                return MoonPhase.WAXING_CRESCENT;
            }
            // check second column (first quarter)
            if (date.isEqual(LocalDate.parse(moonPhases[i+1]))) {
                return MoonPhase.FIRST_QUARTER;
            } else if (isSpanPhase(i+1, date)) {
                return MoonPhase.WAXING_GIBBOUS;
            }
            // check 3rd column (full)
            if (date.isEqual(LocalDate.parse(moonPhases[i+2]))) {
                return MoonPhase.FULL_MOON;
            } else if (isSpanPhase(i+2, date)) {
                return MoonPhase.WANING_GIBBOUS;
            }
            // check 4th column (last quarter)
            if (date.isEqual(LocalDate.parse(moonPhases[i+3]))) {
                return MoonPhase.LAST_QUARTER;
            } else if (isSpanPhase(i+3, date)) {
                return MoonPhase.WANING_CRESCENT;
            }
        }
        return null;
    }

    private boolean isSpanPhase(int i, LocalDate date) {
        return date.isAfter(LocalDate.parse(moonPhases[i])) &&
                date.isBefore(LocalDate.parse(moonPhases[i+1]));
    }
}
 