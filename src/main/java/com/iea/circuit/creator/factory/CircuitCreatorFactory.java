package com.iea.circuit.creator.factory;

import com.iea.circuit.creator.CircuitCreator;
import com.iea.circuit.creator.DefaultCircuitCreator;
import com.iea.circuit.creator.RpiCircuitCreator;

public class CircuitCreatorFactory {

    public static CircuitCreator create(CircuitType circuitType) {
        switch (circuitType) {
            case DEFAULT_CIRCUIT:
                return new DefaultCircuitCreator();
            case RPI_CIRCUIT:
                return new RpiCircuitCreator();
            default:
                return new DefaultCircuitCreator();
        }
    }
}
