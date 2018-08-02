package com.iea;

import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.*;
import com.iea.simulator.AmpCalculator;
import com.iea.utils.Tuple;

public class CircuitTemplates {
    private static ReceiverConfiguration redLEDconfig = new ReceiverConfiguration(0.02, 1.3, 2.5, 100);
    private static ReceiverConfiguration greenLEDconfig = new ReceiverConfiguration(0.03, 1.3, 3, 100);
    private static ReceiverConfiguration motorConfig = new ReceiverConfiguration(1, 2, 6, 20);
    private static ReceiverConfiguration resistorConfig = new ReceiverConfiguration(10, 1.5, 2, 150);
    private static ReceiverConfiguration buzzerConfig = new ReceiverConfiguration(0.03, 2, 2.5, 120);
    private static GeneratorConfiguration generatorConfig = new GeneratorConfiguration(1, 5);


    public static Circuit createSeriesCircuitWithOneRedLEDAndOneGreenLED() {

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", redLEDconfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", greenLEDconfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }

    public static Circuit createSeriesCircuitWithOneRedLEDAndOneResistor() {

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", redLEDconfig);
        Receiver res01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "res01", resistorConfig);
        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(res01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(res01.getFirstPin(), res01))
                .connectComponents(new Tuple<>(res01.getSecondPin(), res01), new Tuple<>(generator.getSecondPin(), generator))
                .build();

    }

    public static Circuit createSeriesCircuitWithTwoRedLEDsAndOneGreenLED() {

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", redLEDconfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", redLEDconfig);
        Receiver led03 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led03", greenLEDconfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(led03)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(led03.getFirstPin(), led03))
                .connectComponents(new Tuple<>(led03.getSecondPin(), led03), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();

    }

    public static Circuit createSeriesCircuitWithOneGreenLED() {

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", greenLEDconfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }

    public static Circuit createSeriesCircuitWithOneRedLEDAndTwoResistors() {

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", redLEDconfig);
        Receiver res01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "res01", resistorConfig);
        Receiver res02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "res02", resistorConfig);

        Circuit.Builder.newBuilder();

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(res01)
                .addReceiver(res02)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(res02.getFirstPin(), res02))
                .connectComponents(new Tuple<>(res02.getSecondPin(), res02), new Tuple<>(res01.getFirstPin(), res01))
                .connectComponents(new Tuple<>(res01.getSecondPin(), res01), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }


    public static Circuit createSeriesCircuitWithOneRedLEDAndOneBuzzer() {

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", redLEDconfig);
        Receiver buz01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "buz01", buzzerConfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(buz01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(buz01.getFirstPin(), buz01))
                .connectComponents(new Tuple<>(buz01.getSecondPin(), buz01), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }

    public static Circuit createSeriesCircuitWithThreeBuzzers() {
        Generator generator = new Generator("gen01", generatorConfig);
        Receiver buz01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "buz01", buzzerConfig);
        Receiver buz02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "buz02", buzzerConfig);
        Receiver buz03 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "buz03", buzzerConfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(buz01)
                .addReceiver(buz02)
                .addReceiver(buz03)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(buz01.getFirstPin(), buz01))
                .connectComponents(new Tuple<>(buz01.getSecondPin(), buz01), new Tuple<>(buz03.getFirstPin(), buz03))
                .connectComponents(new Tuple<>(buz03.getSecondPin(), buz03), new Tuple<>(buz02.getFirstPin(), buz02))
                .connectComponents(new Tuple<>(buz02.getSecondPin(), buz02), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }


    public static Circuit createSeriesCircuitWithTwoRedLEDsAndOneGreenLEDInParallel() {

        Generator generator = new Generator("gen01", generatorConfig);
        Receiver led01 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led01", redLEDconfig);
        Receiver led02 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led02", redLEDconfig);
        Receiver led03 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led03", greenLEDconfig);

        Circuit.Builder.newBuilder();
        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(led03)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(generator.getSecondPin(),generator))
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(),generator))
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led03.getFirstPin(), led03))
                .connectComponents(new Tuple<>(led03.getSecondPin(), led03), new Tuple<>(generator.getSecondPin(),generator))
                .build();

    }
}