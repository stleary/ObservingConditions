package org.example;

public enum MoonQuarter implements MoonPhase {
    NEW_MOON("New Moon"),
    FIRST_QUARTER("First Quarter"),
    FULL_MOON("Full Moon"),
    LAST_QUARTER("Last Quarter");

    private String name;
    MoonQuarter(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
