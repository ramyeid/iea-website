package com.iea.circuit.generator;

import java.util.Objects;

public class GeneratorConfiguration {

    private final double maxAmper;
    private final double volt;


    public GeneratorConfiguration(double maxAmper, double volt) {
        this.maxAmper = maxAmper;
        this.volt = volt;
    }

    public double getMaxAmper() {
        return maxAmper;
    }

    public double getVolt() {
        return volt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneratorConfiguration that = (GeneratorConfiguration) o;
        return Double.compare(that.maxAmper, maxAmper) == 0 &&
                Double.compare(that.volt, volt) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(maxAmper, volt);
    }
}
