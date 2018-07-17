/**
 *  Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 *
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.generator;

import java.util.Objects;

import com.iea.circuit.Component;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.pin.PinFactory;


public class Generator extends Component {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields
    //~ ----------------------------------------------------------------------------------------------------------------

    private final double maxAmper;
    private final double volt;

    private final Pin positivePin;
    private final Pin negativePin;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors
    //~ ----------------------------------------------------------------------------------------------------------------

    public Generator(String id, double maxAmper, double volt) {
        super(id);
        this.maxAmper = maxAmper;
        this.volt = volt;
        this.positivePin = PinFactory.createPositivePin();
        this.negativePin = PinFactory.createNegativePin();

    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods
    //~ ----------------------------------------------------------------------------------------------------------------

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
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        if (!super.equals(o))
            return false;
        Generator generator = (Generator) o;
        return (Double.compare(generator.maxAmper, maxAmper) == 0) && (Double.compare(generator.volt, volt) == 0) && Objects.equals(positivePin, generator.positivePin) && Objects.equals(negativePin, generator.negativePin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), maxAmper, volt, positivePin, negativePin);
    }
}
