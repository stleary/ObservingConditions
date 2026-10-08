/*
 * Tests for MoonPhaseCalculator class
 */
package org.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class MoonPhaseCalculatorTest {
    @Test
    void knownNewMoonIsIdentifiedAsNew() {
        MoonPhaseCalculator calculator = new MoonPhaseCalculatorRest();
        assertTrue(calculator.isNewMoon(LocalDate.of(2026, 9, 11)), "Should be a new moon");
    }
    @Test
    void testPhase() {
        //  new           1st qtr       full          last qtr
        // "2026-09-10", "2026-09-18", "2026-09-26", "2026-10-3",
        MoonPhaseCalculator calculator = new MoonPhaseCalculatorRest();
        // assertNull(calculator.getMoonPhase(LocalDate.of(2026, 1, 1)));
        // assertNull(calculator.getMoonPhase(LocalDate.of(2028, 1, 1)));
        assertEquals(MoonPhase.WANING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 9, 10)));
        assertEquals(MoonPhase.WAXING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 9, 12)));
        assertEquals(MoonPhase.WAXING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 9, 18)));
        assertEquals(MoonPhase.WAXING_GIBBOUS, calculator.getMoonPhase(LocalDate.of(2026, 9, 21)));
        assertEquals(MoonPhase.FULL_MOON, calculator.getMoonPhase(LocalDate.of(2026, 9, 26)));
        assertEquals(MoonPhase.WANING_GIBBOUS, calculator.getMoonPhase(LocalDate.of(2026, 10, 2)));
        assertEquals(MoonPhase.WANING_GIBBOUS, calculator.getMoonPhase(LocalDate.of(2026, 10, 3)));
        assertEquals(MoonPhase.WANING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 10, 7)));
        assertEquals(MoonPhase.WANING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2027, 3, 30)));
    }

    // A shortened copy of the real response for 2026-09-27
    private static final String SAMPLE_RESPONSE = """
        {
          "apiversion": "4.0.1",
          "properties": {
            "data": {
              "closestphase": {
                "day": 26,
                "month": 9,
                "phase": "Full Moon",
                "time": "16:49",
                "year": 2026
              },
              "curphase": "Waning Gibbous",
              "day": 27,
              "fracillum": "99%",
              "month": 9,
              "year": 2026
            }
          },
          "type": "Feature"
        }
        """;

    @Test
    void parsesCurphaseFromResponse() {
        MoonPhaseCalculator calculator = new MoonPhaseCalculatorRest();
        assertEquals(MoonPhase.WANING_GIBBOUS, calculator.parseMoonPhase(SAMPLE_RESPONSE));
    }
}

