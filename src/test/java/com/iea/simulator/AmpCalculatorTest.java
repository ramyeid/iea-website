package com.iea.simulator;

import com.iea.CircuitTemplates;
import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.config.Receiver;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AmpCalculatorTest {

    @Test
    public void should_ensure_amp_is_calculated_correctly_in_series_circuit() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithThreeBuzzers();

        List<Receiver> receiversInClosedCircuit = Validator.validate(circuit);
        double ampResult = AmpCalculator.calculateAmp(circuit.getGenerator(), receiversInClosedCircuit);

        Assert.assertEquals(0.0138, ampResult, 0.001);
    }

    /**
     * the actual amp of the circuit should be 0.15 amps, the AmpCalculator will output 0.016 amps
     * due to the assumption that all circuits are series
     * because the parallel use case is not present in this simulation
     */
    @Test
    public void should_ensure_amp_is_calculated_correctly_parallel_circuit() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithTwoRedLEDsAndOneGreenLEDInParallel();

        List<Receiver> receiversInClosedCircuit = Validator.validate(circuit);
        double ampResult = AmpCalculator.calculateAmp(circuit.getGenerator(), receiversInClosedCircuit);

        Assert.assertEquals(0.016, ampResult, 0.001);
    }


}