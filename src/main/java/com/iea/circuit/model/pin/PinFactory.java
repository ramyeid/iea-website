package com.iea.circuit.model.pin;

public class PinFactory {

    public static Pin createPositivePin() {
        return new Pin(Pin.Type.POSITIVE);
    }

    public static Pin createNegativePin() {
        return new Pin(Pin.Type.NEGATIVE);
    }

    public static Pin createNeutralPin() {
        return new Pin(Pin.Type.NEUTRAL);
    }

}
