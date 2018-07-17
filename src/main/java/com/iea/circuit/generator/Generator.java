package com.iea.circuit.generator;

import com.iea.circuit.Component;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.pin.PinFactory;

import java.util.Objects;

public class Generator extends Component {
    private final double maxAmper;
    private final double volt;


    private final Pin positivePin;
    private final Pin negativePin;

    public Generator(String id, double amp, double volt) {
        super(id);
        this.maxAmper = amp;
        this.volt = volt;
        this.positivePin = PinFactory.createPositivePin();
        this.negativePin = PinFactory.createNegativePin();

    }

    public Pin getPositivePin() {
        return positivePin;
    }

    public Pin getNegativePin() {
        return negativePin;
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
                Double.compare(generator.volt, volt) == 0 &&
                Objects.equals(positivePin, generator.positivePin) &&
                Objects.equals(negativePin, generator.negativePin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), maxAmper, volt, positivePin, negativePin);
    }
}
