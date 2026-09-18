package org.example;

import java.time.LocalDate;
import java.util.List;

/**
 * Calculates phases of the moon for a given date in a very basic and limited way
 */
public class MoonPhaseCalculatorBasic extends MoonPhaseCalculator {

    private static final String[] newMoonDates = {
        "2026-01-17", "2026-02-16", "2026-03-18", "2026-04-16", "2026-05-16",
        "2026-06-14", "2026-07-14", "2026-08-12", "2026-09-10", "2026-10-10",
        "2026-11-08", "2026-12-08", "2027-01-06", "2027-02-05", "2027-03-07", 
        "2027-04-05", "2027-05-05", "2027-06-03", "2027-07-03", "2027-08-01", 
        "2027-08-31", "2027-09-30", "2027-10-30", "2027-11-28", "2027-12-28"
    }; 
    private static final List<String> newMoonDates2026And2027 = List.of(newMoonDates);

    @Override 
    public boolean isNewMoon(LocalDate date) {
        boolean result = false;
        // convert localdate to a date string in the format yyyy-MM-dd
        String dateString = date.toString();
        if (newMoonDates2026And2027.contains(dateString)) {
            result = true;
        }
        return result;
    }
}
 