/*
 * Tests for MoonPhaseCalculator class
 */
package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class MoonPhaseCalculatorTest {
    @Test
    void knownNewMoonIsIdentifiedAsNew() {
        MoonPhaseCalculator calculator = new MoonPhaseCalculatorBasic();
        assertTrue(calculator.isNewMoon(LocalDate.of(2026, 9, 10)), "Should be a new moon");
    }
    @Test
    void testPhase() {
        //  new           1st qtr       full          last qtr
        // "2026-09-10", "2026-09-18", "2026-09-26", "2026-10-3",
        MoonPhaseCalculator calculator = new MoonPhaseCalculatorSelenium();
        // assertNull(calculator.getMoonPhase(LocalDate.of(2026, 1, 1)));
        // assertNull(calculator.getMoonPhase(LocalDate.of(2028, 1, 1)));
        assertEquals(MoonPhaseCalculator.WANING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 9, 10)));
        assertEquals(MoonPhaseCalculator.WAXING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 9, 12)));
        assertEquals(MoonPhaseCalculator.WAXING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 9, 18)));
        assertEquals(MoonPhaseCalculator.WAXING_GIBBOUS, calculator.getMoonPhase(LocalDate.of(2026, 9, 21)));
        assertEquals(MoonPhaseCalculator.FULL_MOON, calculator.getMoonPhase(LocalDate.of(2026, 9, 26)));
        assertEquals(MoonPhaseCalculator.WANING_GIBBOUS, calculator.getMoonPhase(LocalDate.of(2026, 10, 2)));
        assertEquals(MoonPhaseCalculator.WANING_GIBBOUS, calculator.getMoonPhase(LocalDate.of(2026, 10, 3)));
        assertEquals(MoonPhaseCalculator.WANING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 10, 7)));
        assertEquals(MoonPhaseCalculator.WANING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2027, 3, 30)));
    }
}

