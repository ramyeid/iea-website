package com.iea.orchestrator;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverConfiguration;

import java.util.List;

/**
 *
 * assume generator and receivers already check
 * generator != null and receivers != null and !=isEmpty
 *
 * Precision lost:
 * Test case:
 * Generator volt: 10
 * Receiver1 resistance:1
 * Receiver2 resistance:2
 * Receiver3 resistance:1
 *
 * Series amp calculation: 2.5
 * Parallel amp calculation: 25
 *
 * We treat all circuits as series circuits
 * The parallel circuit use case is not present in this simulation
 */
public class AmpCalculator {

    public static double calculateAmp(Generator generator, List<Receiver> receivers) {
        GeneratorConfiguration generatorConfiguration = generator.getGeneratorConfiguration();
        return  generatorConfiguration.getVolt() / calculateTotalResistance(receivers);
    }


    private static double calculateTotalResistance(List<Receiver> receivers) {
        ReceiverConfiguration receiverConfiguration;
        double resistance = 0;
        for (Receiver receiver : receivers) {
            receiverConfiguration = receiver.getConfiguration();
            resistance += receiverConfiguration.getResistance();
        }

        return resistance;
    }

}
