package com.iea.orchestrator;

import com.iea.circuit.Circuit;
import com.iea.circuit.Component;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.Receiver;
import com.iea.utils.Tuple;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.iea.circuit.pin.Pin.Type.*;
import static java.util.stream.Collectors.toList;

public class Validator {

    /**
     * This function returns the list of components that are included in a closed circuit.
     * Thus, returning an empty list when no such component exists (the whole circuit is open).
     * This function is implemented with respect to the following statement:
     * A component is included in a closed circuit if and only if its negative pin
     * is connected to the negative pin of the generator and its positive pin
     * is connected to the positive pin of the generator.
     *
     * @param circuit
     * @return List of receivers in closed circuit.
     */
    public static List<Receiver> validate(Circuit circuit) {

        Set<Component> reachableFromPositive = retrieveComponentsReachableFrom(POSITIVE, () -> circuit.getGenerator().getPositivePin().getConnections());
        Set<Component> reachableFromNegative = retrieveComponentsReachableFrom(NEGATIVE, () -> circuit.getGenerator().getNegativePin().getConnections());

        //Getting the intersection of both reachableFromPositive and reachableFromNegative
        return reachableFromNegative.stream().filter(reachableFromPositive::contains).map(t->(Receiver)t).collect(toList());
    }

    private static Set<Component> retrieveComponentsReachableFrom(Pin.Type pinType, Supplier<List<Tuple<Pin, Component>>> generatorConnections) {
        // Generating reachableFromPin
        LinkedList<Tuple<Pin, Component>> toBeChecked = generateReachables(pinType, generatorConnections);
        // Components that are reachable from the x pin of the generator
        Set<Component> reachableFromPositive = toBeChecked.stream().map(Tuple::getSecond).collect(Collectors.toSet());
        while (!toBeChecked.isEmpty()) {
            Tuple<Pin, Component> tuple = toBeChecked.removeFirst();
            toBeChecked.addAll(generateReachables(pinType, () -> retrieveOppositePin(tuple.getSecond(), tuple.getFirst()).getConnections()));
            reachableFromPositive.addAll(toBeChecked.stream().map(Tuple::getSecond).collect(toList()));
        }
        return reachableFromPositive;
    }

    private static LinkedList<Tuple<Pin, Component>> generateReachables(Pin.Type pinType, Supplier<List<Tuple<Pin, Component>>> generatorConnections) {
        LinkedList<Tuple<Pin, Component>> tupleLinkedList = new LinkedList<>();
        for (Tuple<Pin, Component> connection : generatorConnections.get()) {
            if ((connection.getFirst().getType().equals(pinType) || connection.getFirst().getType().equals(NEUTRAL)) && !doesContainComponent(tupleLinkedList, connection.getSecond())) {
                tupleLinkedList.add(connection);
            }
        }
        return tupleLinkedList;
    }

    private static boolean doesContainComponent(LinkedList<Tuple<Pin, Component>> tupleLinkedList, Component component) {
        return tupleLinkedList.stream().anyMatch(tuple -> tuple.getSecond().equals(component));
    }

    private static Pin retrieveOppositePin(Component component, Pin pin) {
        return component.getSecondPin() == pin ? component.getFirstPin() : component.getSecondPin();
    }

}