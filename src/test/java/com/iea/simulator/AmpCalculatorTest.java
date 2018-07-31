package com.iea.simulator;

import com.iea.CircuitTemplates;
import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.Receiver;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AmpCalculatorTest {

    @Test
    public void should_ensure_amp_is_calculated_correctly_in_series_circuit(){
        Circuit circuit=CircuitTemplates.create_circuit_with_three_receivers_series();
        List<Receiver> receiversInClosedCircuit = Validator.validate(circuit);
        double ampResult = AmpCalculator.calculateAmp(circuit.getGenerator(), receiversInClosedCircuit);
        double ampExpected=2.5;

        Assert.assertEquals(ampExpected,ampResult,0.1);
    }

    /**
     * the actual amp of the circuit should be 25 amps, the AmpCalculator will output 2.5 amps
     * due to the assumption that all circuits are series
     * because the parallel use case is not present in this simulation
     */
    @Test
    public void should_ensure_amp_is_calculated_correctly_parallel_circuit(){
        Circuit circuit=CircuitTemplates.create_circuit_with_three_receivers_parallel();
        List<Receiver> receiversInClosedCircuit = Validator.validate(circuit);
        double ampResult = AmpCalculator.calculateAmp(circuit.getGenerator(), receiversInClosedCircuit);
        double ampExpected=2.5;
        Assert.assertEquals(ampExpected,ampResult,0.1);
    }

    /**
     * the actual amp of the circuit should be 25 amps, the AmpCalculator will output 2.5 amps
     * due to the assumption that all circuits are series
     * because the parallel use case is not present in this simulation
     */

    @Test
    public void should_ensure_amp_is_calculated_correctly_series_and_parallel_circuit(){
        Circuit circuit=CircuitTemplates.create_circuit_with_two_receivers_series_one_parallel();
        List<Receiver> receiversInClosedCircuit = Validator.validate(circuit);
        double ampResult = AmpCalculator.calculateAmp(circuit.getGenerator(), receiversInClosedCircuit);
        double ampExpected=2.5;
        Assert.assertEquals(ampExpected,ampResult,0.1);
    }


}