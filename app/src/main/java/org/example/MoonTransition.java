package org.example;

public enum MoonTransition implements MoonPhase{
    WAXING_CRESCENT("Waxing Crescent"),
    WAXING_GIBBOUS("Waxing Gibbous"),
    WANING_GIBBOUS("Waning Gibbous"),
    WANING_CRESCENT("Waning Crescent");

    private String name;
    MoonTransition(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
