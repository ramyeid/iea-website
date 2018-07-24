package com.iea.orchestrator;

import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverConfiguration;
import com.iea.circuit.receiver.ReceiverFactory;
import com.iea.utils.Tuple;

public class CircuitTemplates {
    public static Circuit create_circuit_with_three_receivers_series() {
        ReceiverConfiguration ledConfig = new ReceiverConfiguration(2, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 6, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);


        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = ReceiverFactory.createDipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = ReceiverFactory.createDipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = ReceiverFactory.createDipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();
    }

    public static Circuit create_circuit_with_three_receivers_parallel() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 6, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = ReceiverFactory.createDipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = ReceiverFactory.createDipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = ReceiverFactory.createDipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
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

    }

    public static Circuit create_circuit_with_two_receivers_series_one_parallel() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 6, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 10);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = ReceiverFactory.createDipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = ReceiverFactory.createDipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = ReceiverFactory.createDipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(generator.getNegativePin(), generator))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

    }

    public static Circuit create_series_circuit_with_three_receivers_series_motor_low_amper() {
        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(1, 2, 6, 10);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 20);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

    }


    public static Circuit create_series_circuit_with_three_receivers_series_high_resistance_and_low_generator_volt() {

        AmpCalculator ampCalculator = new AmpCalculator();
        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1, 1, 10, 10);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(2, 2, 10, 15);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(1, 1);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();
    }


    public static Circuit create_series_circuit_with_three_receivers_series_receivers_high_amper() {

        ReceiverConfiguration ledConfig = new ReceiverConfiguration(5, 1, 10, 10);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(5, 2, 10, 15);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 20);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

    }
    public static Circuit create_series_circuit_with_three_receivers_series_receivers_receiver_low_maxvolt() {
        ReceiverConfiguration ledConfig = new ReceiverConfiguration(5, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(5, 2, 9, 2);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 20);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();
    }
}
