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
        for (int i = 0, phaseIndx = 0; i < moonPhases.length; ++i, phaseIndx=(phaseIndx+2)%8) {
            if (date.isEqual(LocalDate.parse(moonPhases[i]))) {
                return MoonPhase.values()[phaseIndx];
            } else if (isSpanPhase(i, date)) {
                return MoonPhase.values()[phaseIndx+1];
            }
        }
        return null;
    }

    private boolean isSpanPhase(int i, LocalDate date) {
        return date.isAfter(LocalDate.parse(moonPhases[i])) &&
                date.isBefore(LocalDate.parse(moonPhases[i+1]));
    }
}
 