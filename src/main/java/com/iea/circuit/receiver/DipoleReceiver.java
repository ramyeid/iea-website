/**
 *  Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 *
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.receiver;

import java.util.Objects;

import com.iea.circuit.pin.Pin;
import com.iea.circuit.pin.PinFactory;


public class DipoleReceiver extends Receiver {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields
    //~ ----------------------------------------------------------------------------------------------------------------

    private final Pin positivePin;
    private final Pin negativePin;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors
    //~ ----------------------------------------------------------------------------------------------------------------

    DipoleReceiver(String id, Configuration configuration) {
        super(id, configuration);
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

    @Override
    public ReceiverStatus retrieveStatus(double amper, double volt) {
        return ReceiverStatus.OPTIMAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        if (!super.equals(o))
            return false;
        DipoleReceiver that = (DipoleReceiver) o;
        return Objects.equals(positivePin, that.positivePin) && Objects.equals(negativePin, that.negativePin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), positivePin, negativePin);
    }
}
