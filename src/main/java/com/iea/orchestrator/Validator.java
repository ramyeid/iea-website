package com.iea.orchestrator;

import com.iea.circuit.Circuit;
import com.iea.circuit.Component;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.pin.Pin;
import com.iea.utils.Tuple;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.iea.circuit.pin.Pin.Type.NEGATIVE;
import static com.iea.circuit.pin.Pin.Type.NEUTRAL;
import static com.iea.circuit.pin.Pin.Type.POSITIVE;
import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

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
    public static List<Component> validate(Circuit circuit) {
        // Components that are in the closed circuit
        List<Component> validated = newArrayList();
        // Components that are reachable from the positive pin of the generator
        LinkedList<Component> reachableFromPositive = new LinkedList<>();
        // Components that are reachable from the negative pin of the generator
        LinkedList<Component> reachableFromNegative = new LinkedList<>();
        // List used to generate both ValidatedNeg and ValidatedPos
        LinkedList<Tuple<Pin, Component>> toBeChecked = new LinkedList<>();

        Generator generator = circuit.getGenerator();

        // Generating reachableFromPositive
        generateReachables(POSITIVE, () -> generator.getPositivePin().getConnections(), reachableFromPositive, toBeChecked);
        while (!toBeChecked.isEmpty()) {
            Tuple<Pin, Component> tuple = toBeChecked.removeFirst();
            generateReachables(POSITIVE, () -> retrieveOppositePin(tuple.getSecond(), tuple.getFirst()).getConnections(), reachableFromNegative, toBeChecked);
        }

        // Generating reachableFromNegative
        generateReachables(NEGATIVE, () -> generator.getNegativePin().getConnections(), reachableFromNegative, toBeChecked);
        while (!toBeChecked.isEmpty()) {
            Tuple<Pin, Component> tuple  = toBeChecked.removeFirst();
            generateReachables(NEGATIVE, () -> retrieveOppositePin(tuple.getSecond(), tuple.getFirst()).getConnections(), reachableFromNegative, toBeChecked);
        }

        //Getting the intersection of both ValidatedPos and ValidatedNeg
        for (Component component : reachableFromNegative) {
            if (reachableFromPositive.contains(component)) {
                validated.add(component);
            }
        }

        return reachableFromNegative.stream().filter(reachableFromPositive::contains).collect(Collectors.toList());
    }

    private static void generateReachables(Pin.Type pinType, Supplier<List<Tuple<Pin, Component>>> connections, LinkedList<Component> reachables, LinkedList<Tuple<Pin, Component>> toBeChecked) {
        for (Tuple<Pin, Component> connection : connections.get()) {
            if ((connection.getFirst().getType().equals(pinType) || connection.getFirst().getType().equals(NEUTRAL)) && !reachables.contains(connection.getSecond())) {
                reachables.add(connection.getSecond());
                toBeChecked.add(connection);
            }
        }
    }

    private static Pin retrieveOppositePin(Component component, Pin pin) {
        return component.getSecondPin() == pin ? component.getFirstPin() : component.getSecondPin();
    }

}