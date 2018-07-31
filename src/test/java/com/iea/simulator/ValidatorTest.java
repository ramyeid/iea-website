package com.iea.simulator;

import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.*;
import com.iea.utils.Tuple;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;
import static org.junit.Assert.assertEquals;

public class ValidatorTest {
    //Normal series Circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_complete_series_circuit() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(motor01.getFirstPin(), motor01))
                .connectComponents(new Tuple<>(motor01.getSecondPin(), motor01), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        List<Receiver> expected = newArrayList();
        expected.add(led01);
        expected.add(led02);
        expected.add(motor01);


        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }

    //If one component is off the circuit but the rest are connected
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_unconnected_receiver() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver mot01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(mot01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(mot01.getFirstPin(), mot01))
                .connectComponents(new Tuple<>(mot01.getSecondPin(), mot01), new Tuple<>(generator.getSecondPin(), generator))
                .connectComponents(new Tuple<>(mot01.getSecondPin(), mot01), new Tuple<>(led02.getFirstPin(), led02))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        List<Receiver> expected = newArrayList();
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
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(motor01.getFirstPin(), motor01))
                .connectComponents(new Tuple<>(motor01.getSecondPin(), motor01), new Tuple<>(led02.getFirstPin(), led02))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        List<Receiver> expected = newArrayList();


        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }

    //only a generator is in the circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_only_a_generator() {
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(generator.getSecondPin(), generator))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        List<Receiver> expected = newArrayList();

        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }

    //Normal parralel Circuit
    //all components connected to the generator
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_normal_parallel_circuit() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(generator.getSecondPin(), generator))
                .connectComponents(new Tuple<>(motor01.getFirstPin(), motor01), new Tuple<>(generator.getFirstPin(), generator))
                .connectComponents(new Tuple<>(motor01.getSecondPin(), motor01), new Tuple<>(generator.getSecondPin(), generator))
                .connectComponents(new Tuple<>(led02.getFirstPin(), led02), new Tuple<>(generator.getFirstPin(), generator))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();

        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();
        expected.add(led01);
        expected.add(led02);
        expected.add(motor01);


        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }

    //one component is parrallel the others are in series
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_series_circuit_with_one_parallel_receiver() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(motor01.getFirstPin(), motor01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(motor01.getSecondPin(), motor01), new Tuple<>(generator.getSecondPin(), generator))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();

        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();
        expected.add(led01);
        expected.add(led02);
        expected.add(motor01);


        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }
    //one component has reversed pins the rest have normal cocnnections
    //series circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_reversed_pins_in_series_circuit() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getSecondPin(), led01))
                .connectComponents(new Tuple<>(led01.getFirstPin(), led01), new Tuple<>(motor01.getFirstPin(), motor01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(motor01.getSecondPin(), motor01), new Tuple<>(generator.getSecondPin(), generator))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();

        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();

        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }


    //component pins switched
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_receiver_with_switched_pins() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getSecondPin(), led01))
                .connectComponents(new Tuple<>(led01.getFirstPin(), led01), new Tuple<>(generator.getSecondPin(), generator))
                .build();

        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();


        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }

    //two components connect in series
    //one component with reversed pins connected in parallel
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_with_series_receiver_and_reversed_pins_in_parallel_receiver() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(motor01.getFirstPin(), motor01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(led02.getSecondPin(), led02))
                .connectComponents(new Tuple<>(motor01.getSecondPin(), motor01), new Tuple<>(generator.getSecondPin(), generator))
                .connectComponents(new Tuple<>(led02.getFirstPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();

        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();
        expected.add(led01);
        expected.add(motor01);


        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }


    //multiple components have the pins switched
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_multiple_receivers_with_switched_pins() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(motor01.getSecondPin(), motor01))
                .connectComponents(new Tuple<>(motor01.getFirstPin(), motor01), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();

        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();

        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }


    //open circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_two_receivers_connected_to_each_other() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(led02.getSecondPin(), led02))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(led01.getFirstPin(), led01))
                .build();

        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();


        assertEquals(new HashSet<>(result), new HashSet<>(expected));
    }

    //delta connection
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_delta_connection_circuit() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(generator.getSecondPin(), generator))
                .connectComponents(new Tuple<>(led02.getFirstPin(), led02), new Tuple<>(generator.getFirstPin(), generator))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(motor01.getFirstPin(), motor01))
                .connectComponents(new Tuple<>(motor01.getSecondPin(), motor01), new Tuple<>(generator.getSecondPin(), generator))
                .build();

        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();
        expected.add(led01);
        expected.add(led02);
        expected.add(motor01);


        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }

    //circuit without generator
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_without_generator() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);

        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01",ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(led01.getFirstPin(), led01), new Tuple<>(led02.getSecondPin(), led02))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(motor01.getFirstPin(), motor01))
                .connectComponents(new Tuple<>(motor01.getSecondPin(), motor01), new Tuple<>(led02.getFirstPin(), led02))
                .build();

        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();
        assertEquals(new HashSet<>(result), new HashSet<>(expected));
    }


    //empty circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_an_empty_circuit() {
        Circuit circuit = Circuit.Builder.newBuilder().build();
        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();

        assertEquals(new HashSet<>(result), new HashSet<>(expected));
    }

    //testing special case circuit
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_without_generatora() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);
        Receiver motor02 =ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot02",motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .addReceiver(motor02)
                .connectComponents(new Tuple<>(generator.getFirstPin(),generator),new Tuple<>(led01.getFirstPin(),led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(),led01),new Tuple<>(generator.getSecondPin(),generator))
                .connectComponents(new Tuple<>(led02.getFirstPin(),led02),new Tuple<>(generator.getSecondPin(),generator))
                .connectComponents(new Tuple<>(generator.getSecondPin(),generator),new Tuple<>(motor02.getSecondPin(),motor02))
                .connectComponents(new Tuple<>(motor02.getFirstPin(),motor02),new Tuple<>(motor01.getSecondPin(),motor01))
                .connectComponents(new Tuple<>(motor01.getFirstPin(),motor01),new Tuple<>(led02.getSecondPin(),led02))
                .build();


        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();
        expected.add(led01);
        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }
    //testing same special case circuit with different connections
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_without_generatoraa() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);
        Receiver motor02 =ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot02",motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .addReceiver(motor02)
                .connectComponents(new Tuple<>(generator.getFirstPin(),generator),new Tuple<>(led01.getFirstPin(),led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(),led01),new Tuple<>(generator.getSecondPin(),generator))
                .connectComponents(new Tuple<>(led02.getFirstPin(),led02),new Tuple<>(generator.getSecondPin(),generator))
                .connectComponents(new Tuple<>(generator.getSecondPin(),generator),new Tuple<>(motor02.getSecondPin(),motor02))
                .connectComponents(new Tuple<>(motor02.getFirstPin(),motor02),new Tuple<>(motor01.getSecondPin(),motor01))
                .connectComponents(new Tuple<>(motor01.getFirstPin(),motor01),new Tuple<>(led02.getSecondPin(),led02))
                .connectComponents(new Tuple<>(led01.getSecondPin(),led01),new Tuple<>(led02.getFirstPin(),led02))
                .build();


        List<Receiver> result = Validator.validate(circuit);
        assertEquals(new HashSet<>(asList(led01, led02, motor01, motor02)), newHashSet(result));
    }
    //testing same special case circuit with another way of connections
    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_without_generatoraaa() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 5, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", ledConfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", ledConfig);
        Receiver motor01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot01", motorConfig);
        Receiver motor02 =ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "mot02",motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .addReceiver(motor02)
                .connectComponents(new Tuple<>(generator.getFirstPin(),generator),new Tuple<>(led01.getFirstPin(),led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(),led01),new Tuple<>(generator.getSecondPin(),generator))
                .connectComponents(new Tuple<>(led02.getFirstPin(),led02),new Tuple<>(generator.getSecondPin(),generator))
                .connectComponents(new Tuple<>(generator.getSecondPin(),generator),new Tuple<>(motor02.getSecondPin(),motor02))
                .connectComponents(new Tuple<>(motor02.getFirstPin(),motor02),new Tuple<>(motor01.getSecondPin(),motor01))
                .connectComponents(new Tuple<>(motor01.getFirstPin(),motor01),new Tuple<>(led02.getSecondPin(),led02))
                .connectComponents(new Tuple<>(motor01.getFirstPin(),motor01),new Tuple<>(led02.getSecondPin(),led02))
                .build();


        List<Receiver> result = Validator.validate(circuit);
        List<Receiver> expected = newArrayList();
        expected.add(led01);
        assertEquals(new HashSet<>(result), new HashSet<>(expected));

    }


}