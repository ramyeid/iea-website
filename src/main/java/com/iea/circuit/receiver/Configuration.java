package com.iea.circuit.receiver;

import java.util.Objects;

public class Configuration {
    private final double optimalAmper;
    private final double minVolt;
    private final double maxVolt;
    private final double resistance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Double.compare(that.optimalAmper, optimalAmper) == 0 &&
                Double.compare(that.minVolt, minVolt) == 0 &&
                Double.compare(that.maxVolt, maxVolt) == 0 &&
                Double.compare(that.resistance, resistance) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(optimalAmper, minVolt, maxVolt, resistance);
    }

    public Configuration(double optimalAmper, double minVolt, double maxVolt, double resistance) {

        this.optimalAmper = optimalAmper;
        this.minVolt = minVolt;
        this.maxVolt = maxVolt;
        this.resistance = resistance;
    }
}
