package com.iea.orchestrator;

import com.iea.circuit.Circuit;
import com.iea.circuit.Component;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.circuit.receiver.ReceiverConfiguration;
import com.iea.utils.Tuple;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;


import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;
import static org.junit.Assert.assertEquals;

public class ValidatorTest {


    //Normal series Circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_complete_series_circuit() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Component> result = Validator.validate(circuit);

        List<Component> expected = newArrayList();
        expected.add(led01);
        expected.add(led02);
        expected.add(motor01);


        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }

    //If one component is off the circuit but the rest are connected
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_ciruit_with_unconnected_receiver() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver mot01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(mot01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(mot01.getPositivePin(), mot01))
                .connectComponents(new Tuple<>(mot01.getNegativePin(), mot01), new Tuple<>(generator.getNegativePin(), generator))
                .connectComponents(new Tuple<>(mot01.getNegativePin(), mot01), new Tuple<>(led02.getPositivePin(), led02))
                .build();

        List<Component> result = Validator.validate(circuit);

        List<Component> expected = newArrayList();
        expected.add(led01);
        expected.add(mot01);


        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }

    //open Circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_an_open_circuit() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .build();

        List<Component> result = Validator.validate(circuit);

        List<Component> expected = newArrayList();


        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }

    //only a generator is in the circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_only_a_generator() {
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Component> result = Validator.validate(circuit);

        List<Component> expected = newArrayList();

        assert (new HashSet<>(result).equals(new HashSet<>(expected)));


    }

    //Normal parralel Circuit
    //all components connected to the generator
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_normal_parallel_circuit() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(generator.getNegativePin(), generator))
                .connectComponents(new Tuple<>(motor01.getPositivePin(), motor01), new Tuple<>(generator.getPositivePin(), generator))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(generator.getNegativePin(), generator))
                .connectComponents(new Tuple<>(led02.getPositivePin(), led02), new Tuple<>(generator.getPositivePin(), generator))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();
        expected.add(led01);
        expected.add(led02);
        expected.add(motor01);


        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }

    //one component is parrallel the others are in series
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_series_circuit_with_one_parallel_receiver() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(generator.getNegativePin(), generator))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();
        expected.add(led01);
        expected.add(led02);
        expected.add(motor01);


        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_reversed_pins_in_series_circuit() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getNegativePin(), led01))
                .connectComponents(new Tuple<>(led01.getPositivePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(generator.getNegativePin(), generator))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();

        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }


    //component pins switched
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_receiver_with_switched_pins() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getNegativePin(), led01))
                .connectComponents(new Tuple<>(led01.getPositivePin(), led01), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();


        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_series_receiver_and_reversed_pins_in_parallel_receiver() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(led02.getNegativePin(), led02))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(generator.getNegativePin(), generator))
                .connectComponents(new Tuple<>(led02.getPositivePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();
        expected.add(led01);
        expected.add(motor01);


        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }


    //one component has the pins switched
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_multiple_receivers_with_switched_pins() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getNegativePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getPositivePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();

        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }


    //open circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_two_receivers_connected_to_each_other() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(led02.getNegativePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(led01.getPositivePin(), led01))
                .build();

        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();


        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }

    //delta connection
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_delta_connection_circuit() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(generator.getNegativePin(), generator))
                .connectComponents(new Tuple<>(led02.getPositivePin(), led02), new Tuple<>(generator.getPositivePin(), generator))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();
        expected.add(led01);
        expected.add(led02);
        expected.add(motor01);


        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }

    //circuit without generator
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_without_generator() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);

        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(led01.getPositivePin(), led01), new Tuple<>(led02.getNegativePin(), led02))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .build();

        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();


        assert (new HashSet<>(result).equals(new HashSet<>(expected)));

    }


    //empty circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_an_empty_circuit() {
        Circuit circuit = Circuit.Builder.newBuilder().build();
        List<Component> result = Validator.validate(circuit);
        List<Component> expected = newArrayList();

        assert (new HashSet<>(result).equals(new HashSet<>(expected)));


    }
}