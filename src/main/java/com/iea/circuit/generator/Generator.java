package com.iea.circuit.generator;

import com.iea.circuit.Component;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.pin.PinFactory;

import java.util.Objects;


public class Generator extends Component {

    private final double maxAmper;
    private final double volt;

    public Generator(String id, double maxAmper, double volt) {
        super(id, PinFactory.createPositivePin(), PinFactory.createNegativePin());
        this.maxAmper = maxAmper;
        this.volt = volt;
    }

    public Pin getPositivePin() {
        return getFirstPin();
    }

    public Pin getNegativePin() {
        return getSecondPin();
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
        if (!super.equals(o)) return false;
        Generator generator = (Generator) o;
        return Double.compare(generator.maxAmper, maxAmper) == 0 &&
                Double.compare(generator.volt, volt) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), maxAmper, volt);
    }
}
