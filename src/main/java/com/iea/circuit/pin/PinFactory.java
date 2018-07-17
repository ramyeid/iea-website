package com.iea.circuit.pin;

public class PinFactory {


    public static Pin createPositivePin() {
        return new Pin(Pin.PinType.POSITIVE);
    }

    public static Pin createNegativePin() {
        return new Pin(Pin.PinType.NEGATIVE);
    }

}
