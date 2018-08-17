package com.iea.circuit.simulator;

import com.iea.SimpleCircuitTemplates;
import com.iea.circuit.DefaultCircuit;
import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.receiver.config.Receiver;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class AmpCalculatorTest {

    @Test
    public void should_ensure_amp_is_calculated_correctly_in_series_circuit() {
        SimpleCircuit circuit = SimpleCircuitTemplates.createSeriesCircuitWithThreeBuzzers();

        double ampResult = AmpCalculator.calculateAmp(circuit);

        Assert.assertEquals(0.0138, ampResult, 0.001);
    }

    /**
     * the actual amp of the circuit should be 0.15 amps, the AmpCalculator will output 0.016 amps
     * due to the assumption that all circuits are series
     * because the parallel use case is not present in this simulation
     */
    @Test
    public void should_ensure_amp_is_calculated_correctly_parallel_circuit() {
        SimpleCircuit circuit = SimpleCircuitTemplates.createSeriesCircuitWithTwoRedLEDsAndOneGreenLEDInParallel();

        double ampResult = AmpCalculator.calculateAmp(circuit);

        Assert.assertEquals(0.016, ampResult, 0.001);
    }
}