package com.iea.circuit.simulator;

import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.generator.GeneratorConfiguration;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * assume generator and receivers already check
 * generator != null and receivers != null and !=isEmpty
 * <p>
 * Precision lost:
 * Test case:
 * Generator volt: 10
 * Receiver1 resistance:1
 * Receiver2 resistance:2
 * Receiver3 resistance:1
 * <p>
 * Series amp calculation: 2.5
 * Parallel amp calculation: 25
 * <p>
 * We treat all circuits as series circuits
 * The parallel circuit use case is not present in this simulation
 */
public class AmpCalculator {

    private static final Logger LOGGER = LogManager.getLogger(AmpCalculator.class.getName());

    static double calculateAmp(SimpleCircuit circuit) {
        GeneratorConfiguration generatorConfiguration = circuit.getGenerator().getGeneratorConfiguration();
        double amp = generatorConfiguration.getVolt() / calculateTotalResistance(circuit.getReceivers());

        LOGGER.info("Circuit Amp: " + amp);
        return amp;
    }


    private static double calculateTotalResistance(List<Receiver> receivers) {
        ReceiverConfiguration receiverConfiguration;
        double resistance = 0;
        for (Receiver receiver : receivers) {
            receiverConfiguration = receiver.getConfiguration();
            resistance += receiverConfiguration.getResistance();
        }
        LOGGER.info("Total Resistance of receivers: " + resistance);
        return resistance;
    }

}
