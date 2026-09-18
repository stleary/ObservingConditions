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
}

