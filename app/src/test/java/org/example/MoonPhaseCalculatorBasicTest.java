/*
 * Tests for MoonPhaseCalculator class
 */
package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class MoonPhaseCalculatorBasicTest {
    @Test
    void knownNewMoonIsIdentifiedAsNew() {
        MoonPhaseCalculator calculator = new MoonPhaseCalculatorBasic();
        assertTrue(calculator.isNewMoon(LocalDate.of(2026, 9, 10)), "Should be a new moon");
    }
    @Test
    void testPhase() {
        //  new           1st qtr       full          last qtr
        // "2026-09-10", "2026-09-18", "2026-09-26", "2026-10-3",
        MoonPhaseCalculator calculator = new MoonPhaseCalculatorBasic();
        assertNull(calculator.getMoonPhase(LocalDate.of(2026, 1, 1)));
        assertNull(calculator.getMoonPhase(LocalDate.of(2028, 1, 1)));
        assertEquals(MoonQuarter.NEW_MOON, calculator.getMoonPhase(LocalDate.of(2026, 9, 10)));
        assertEquals(MoonTransition.WAXING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 9, 11)));
        assertEquals(MoonQuarter.FIRST_QUARTER, calculator.getMoonPhase(LocalDate.of(2026, 9, 18)));
        assertEquals(MoonTransition.WAXING_GIBBOUS, calculator.getMoonPhase(LocalDate.of(2026, 9, 21)));
        assertEquals(MoonQuarter.FULL_MOON, calculator.getMoonPhase(LocalDate.of(2026, 9, 26)));
        assertEquals(MoonTransition.WANING_GIBBOUS, calculator.getMoonPhase(LocalDate.of(2026, 10, 2)));
        assertEquals(MoonQuarter.LAST_QUARTER, calculator.getMoonPhase(LocalDate.of(2026, 10, 3)));
        assertEquals(MoonTransition.WANING_CRESCENT, calculator.getMoonPhase(LocalDate.of(2026, 10, 7)));
    }
}

