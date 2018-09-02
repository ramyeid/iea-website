/**
 *  Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 * 
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.utils;

import com.iea.circuit.model.component.Component;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.pin.Pin;
import com.iea.circuit.utils.exception.MatchingComponentNotFoundException;
import com.iea.utils.Tuple;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.iea.circuit.model.pin.Pin.Type.*;
import static java.util.stream.Collectors.toList;


public class CircuitUtils {

    public static List<Receiver> retrieveAllReceiversInClosedCircuitWithGenerator(Generator generator) {
        Set<Receiver> reachableFromPositive = retrieveComponentsReachableFrom(POSITIVE, generator.getPositivePin().getConnections());
        Set<Receiver> reachableFromNegative = retrieveComponentsReachableFrom(NEGATIVE, generator.getNegativePin().getConnections());

        return reachableFromNegative.stream().filter(reachableFromPositive::contains).collect(toList());
    }

    private static Set<Receiver> retrieveComponentsReachableFrom(Pin.Type pinType, List<Tuple<Pin, Component>> generatorConnections) {
        // Generating reachableFromPin
        LinkedList<Tuple<Pin, Component>> toBeChecked = generateReachablesFrom(pinType, generatorConnections);
        // Components that are reachable from the x pin of the generator
        Set<Component> reachableFromPin = toBeChecked.stream().map(Tuple::getSecond).collect(Collectors.toSet());
        while (!toBeChecked.isEmpty()) {
            Tuple<Pin, Component> tuple = toBeChecked.removeFirst();
            toBeChecked.addAll(generateReachablesFrom(pinType, retrieveOppositePin(tuple.getSecond(), tuple.getFirst()).getConnections()));
            reachableFromPin.addAll(toBeChecked.stream().map(Tuple::getSecond).collect(toList()));
        }
        return reachableFromPin.stream().filter(t -> t instanceof Receiver).map(t -> (Receiver) t).collect(Collectors.toSet());
    }

    public static Component getComponentById(String id, List<Component> components) throws MatchingComponentNotFoundException {
        return components.stream().filter(component -> component.getId().equals(id)).findFirst().orElseThrow(() -> new MatchingComponentNotFoundException(id));
    }

    private static LinkedList<Tuple<Pin, Component>> generateReachablesFrom(Pin.Type pinType, List<Tuple<Pin, Component>> generatorConnections) {
        LinkedList<Tuple<Pin, Component>> tupleLinkedList = new LinkedList<>();
        for (Tuple<Pin, Component> connection : generatorConnections) {
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
        return (component.getSecondPin() == pin) ? component.getFirstPin() : component.getSecondPin();
    }
}
