
package com.iea.circuit;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverConfiguration;
import com.iea.orchestrator.Validator;
import com.iea.utils.Tuple;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

public class CircuitTest {

    GeneratorConfiguration generatorConfig0 = new GeneratorConfiguration(40,6);
    GeneratorConfiguration generatorConfig1 = new GeneratorConfiguration(20,3);
    ReceiverConfiguration receiverConfig = new ReceiverConfiguration(10,1,2,0.2);
    Generator generator0 = new Generator("gen01", generatorConfig0);
    Generator generator1 = new Generator("gen01", generatorConfig0);
    Receiver led01 = new DipoleReceiver("led01", receiverConfig);
    Receiver led11 = new DipoleReceiver("led01", receiverConfig);
    Receiver led02 = new DipoleReceiver("led02", receiverConfig);
    Receiver led12 = new DipoleReceiver("led02", receiverConfig);
    Receiver mot01 = new DipoleReceiver("mot01", receiverConfig);
    Receiver mot11 = new DipoleReceiver("mot01", receiverConfig);

    @Test
    public void should_build_two_identical_simple_circuits_correctly_and_compare_them_true() {
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit1 = Circuit.Builder.setGenerator(generator0)
                .addReceiver(led01)
                .connectComponents(new Tuple<>(generator0.getPositivePin(),generator0), new Tuple<>(((DipoleReceiver) led01).getPositivePin(),led01))
                .connectComponents(new Tuple<>(((DipoleReceiver) led01).getNegativePin(),led01), new Tuple<>(generator0.getNegativePin(),generator0))
                .build();
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit2 = Circuit.Builder.setGenerator(generator1)
                .addReceiver(led11)
                .connectComponents(new Tuple<>(generator1.getPositivePin(),generator1), new Tuple<>(((DipoleReceiver) led11).getPositivePin(),led11))
                .connectComponents(new Tuple<>(((DipoleReceiver) led11).getNegativePin(),led11), new Tuple<>(generator1.getNegativePin(),generator1))
                .build();

        assert (circuit1.equals(circuit2));
        //assert (new HashSet<>(result).equals(new HashSet<>(expected)));
    }

    @Test
    public void should_build_two_identical_circuits_correctly_and_compare_them_true() {
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit1 = Circuit.Builder.setGenerator(generator0)
                .addReceiver(led01)
                .addReceiver(led02)
                .connectComponents(new Tuple<>(generator0.getPositivePin(),generator0), new Tuple<>(((DipoleReceiver) led01).getPositivePin(),led01))
                .connectComponents(new Tuple<>(((DipoleReceiver) led01).getNegativePin(),led01), new Tuple<>(((DipoleReceiver) led02).getPositivePin(),led02))
                .connectComponents(new Tuple<>(((DipoleReceiver) led02).getNegativePin(),led02), new Tuple<>(generator0.getNegativePin(),generator0))
                                    .build();
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit2 = Circuit.Builder.setGenerator(generator1)
                .addReceiver(led11)
                .addReceiver(led12)
                .connectComponents(new Tuple<>(generator1.getPositivePin(),generator1), new Tuple<>(((DipoleReceiver) led11).getPositivePin(),led11))
                .connectComponents(new Tuple<>(((DipoleReceiver) led11).getNegativePin(),led11), new Tuple<>(((DipoleReceiver) led12).getPositivePin(),led12))
                .connectComponents(new Tuple<>(((DipoleReceiver) led12).getNegativePin(),led12), new Tuple<>(generator1.getNegativePin(),generator1))
                .build();

        assert (circuit1.equals(circuit2));
        /*List<Component> result = Validator.validate(circuit1);
        List<Component> expected = newArrayList();
        expected.add(led01); expected.add(led02);
        assert (new HashSet<>(result).equals(new HashSet<>(expected)));*/
    }

    @Test
    public void should_build_two_identical_complex_circuits_correctly_and_compare_them_true() {
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit1 = Circuit.Builder.setGenerator(generator0)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(mot01)
                .connectComponents(new Tuple<>(generator0.getPositivePin(),generator0), new Tuple<>(((DipoleReceiver) led01).getPositivePin(),led01))
                .connectComponents(new Tuple<>(((DipoleReceiver) led01).getNegativePin(),led01), new Tuple<>(((DipoleReceiver) led02).getPositivePin(),led02))
                .connectComponents(new Tuple<>(((DipoleReceiver) led02).getNegativePin(),led02), new Tuple<>(generator0.getNegativePin(),generator0))
                .connectComponents(new Tuple<>(((DipoleReceiver) mot01).getNegativePin(),mot01), new Tuple<>(generator0.getNegativePin(),generator0))
                .build();
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit2 = Circuit.Builder.setGenerator(generator1)
                .addReceiver(led11)
                .addReceiver(led12)
                .addReceiver(mot11)
                .connectComponents(new Tuple<>(generator1.getPositivePin(),generator1), new Tuple<>(((DipoleReceiver) led11).getPositivePin(),led11))
                .connectComponents(new Tuple<>(((DipoleReceiver) led11).getNegativePin(),led11), new Tuple<>(((DipoleReceiver) led12).getPositivePin(),led12))
                .connectComponents(new Tuple<>(((DipoleReceiver) led12).getNegativePin(),led12), new Tuple<>(generator1.getNegativePin(),generator1))
                .connectComponents(new Tuple<>(((DipoleReceiver) mot11).getNegativePin(),mot11), new Tuple<>(generator1.getNegativePin(),generator1))
                .build();

        assert (circuit1.equals(circuit2));
    }

    @Test
    public void should_build_two_different_generator_circuits_correctly_and_compare_them_false() {
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit1 = Circuit.Builder.setGenerator(new Generator("gen01",generatorConfig1))
                .addReceiver(led01)
                .addReceiver(led02)
                .connectComponents(new Tuple<>(generator0.getPositivePin(),generator0), new Tuple<>(((DipoleReceiver) led01).getPositivePin(),led01))
                .connectComponents(new Tuple<>(((DipoleReceiver) led01).getNegativePin(),led01), new Tuple<>(((DipoleReceiver) led02).getPositivePin(),led02))
                .connectComponents(new Tuple<>(((DipoleReceiver) led02).getNegativePin(),led02), new Tuple<>(generator0.getNegativePin(),generator0))
                .build();
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit2 = Circuit.Builder.setGenerator(generator1)
                .addReceiver(led11)
                .addReceiver(led12)
                .connectComponents(new Tuple<>(generator1.getPositivePin(),generator1), new Tuple<>(((DipoleReceiver) led11).getPositivePin(),led11))
                .connectComponents(new Tuple<>(((DipoleReceiver) led11).getNegativePin(),led11), new Tuple<>(((DipoleReceiver) led12).getPositivePin(),led12))
                .connectComponents(new Tuple<>(((DipoleReceiver) led12).getNegativePin(),led12), new Tuple<>(generator1.getNegativePin(),generator1))
                .build();

        assert !(circuit1.equals(circuit2));
    }

    @Test
    public void should_build_two_different_receivers_circuits_correctly_and_compare_them_false() {
        Circuit.Builder.newBuilder().clearComponents();;
        Circuit circuit1 = Circuit.Builder.setGenerator(generator0)
                .addReceiver(led01)
                .connectComponents(new Tuple<>(generator0.getPositivePin(),generator0), new Tuple<>(((DipoleReceiver) led01).getPositivePin(),led01))
                .connectComponents(new Tuple<>(((DipoleReceiver) led01).getNegativePin(),led01), new Tuple<>(((DipoleReceiver) led02).getPositivePin(),led02))
                .connectComponents(new Tuple<>(((DipoleReceiver) led02).getNegativePin(),led02), new Tuple<>(generator0.getNegativePin(),generator0))
                .build();
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit2 = Circuit.Builder.setGenerator(generator1)
                .addReceiver(led11)
                .addReceiver(led12)
                .connectComponents(new Tuple<>(generator1.getPositivePin(),generator1), new Tuple<>(((DipoleReceiver) led11).getPositivePin(),led11))
                .connectComponents(new Tuple<>(((DipoleReceiver) led11).getNegativePin(),led11), new Tuple<>(((DipoleReceiver) led12).getPositivePin(),led12))
                .connectComponents(new Tuple<>(((DipoleReceiver) led12).getNegativePin(),led12), new Tuple<>(generator1.getNegativePin(),generator1))
                .build();

        assert !(circuit1.equals(circuit2));
    }

    @Test
    public void should_build_two_different_wiring_circuits_correctly_and_compare_them_false() {
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit1 = Circuit.Builder.setGenerator(generator0)
                .addReceiver(led01)
                .addReceiver(led02)
                .connectComponents(new Tuple<>(generator0.getPositivePin(),generator0), new Tuple<>(((DipoleReceiver) led01).getPositivePin(),led01))
                .connectComponents(new Tuple<>(((DipoleReceiver) led01).getNegativePin(),led01), new Tuple<>(((DipoleReceiver) led02).getPositivePin(),led02))
                .build();
        Circuit.Builder.newBuilder().clearComponents();
        Circuit circuit2 = Circuit.Builder.setGenerator(generator1)
                .addReceiver(led11)
                .addReceiver(led12)
                .connectComponents(new Tuple<>(generator1.getPositivePin(),generator1), new Tuple<>(((DipoleReceiver) led11).getPositivePin(),led11))
                .connectComponents(new Tuple<>(((DipoleReceiver) led11).getNegativePin(),led11), new Tuple<>(((DipoleReceiver) led12).getPositivePin(),led12))
                .connectComponents(new Tuple<>(((DipoleReceiver) led12).getNegativePin(),led12), new Tuple<>(generator1.getNegativePin(),generator1))
                .build();

        assert !(circuit1.equals(circuit2));
    }

}
