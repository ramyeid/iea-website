package com.iea.circuit.creator;

import com.iea.circuit.Circuit;
import com.iea.circuit.creator.exception.DuplicateReceiversInCircuitsException;
import com.iea.circuit.creator.exception.InvalidNumberOfGeneratorException;
import com.iea.circuit.model.Connection;
import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.Component;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.pin.Pin;
import com.iea.circuit.utils.exception.MatchingComponentNotFoundException;
import com.iea.jsmapping.exception.InvalidConnectionsStringException;
import com.iea.jsmapping.exception.NoMatchingPinFoundException;
import com.iea.utils.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.iea.circuit.utils.CircuitUtils.retrieveAllReceiversInClosedCircuitWithGenerator;
import static com.iea.jsmapping.JavascriptMapping.*;
import static java.util.stream.Collectors.toList;

public abstract class CircuitCreator {

    private static final Logger LOGGER = LogManager.getLogger(CircuitCreator.class);

    /**
     * Function used to doCreate a circuit out of 3 strings
     *
     * @param generators:  string containing the generator's ID, e.g. "bat0"
     * @param receivers:   string containing a list of receiver IDs, e.g. "res-1,led-2,buz-3"
     * @param connections: string containing lists of quadruples representing the connections
     *                     quadruple format is as follows:
     *                     sourceComponentId, sourcePin, destinationComponentId, destinationPin
     *                     e.g. "bat0,+,res-1,+" connects the positive pin of bat0 to the positive pin of res-1
     *                     <p>
     *                     Pin types: pin types for connections are mapped as follows:
     *                     +  -> positive
     *                     -  -> negative
     *                     ~1  -> neutral 1
     *                     ~2  -> neutral 2
     * @return returns a circuit with the generator, receivers, and connections given
     */
    public Circuit create(String generators, String receivers, String connections) throws NoMatchingPinFoundException, InvalidNumberOfGeneratorException, InvalidConnectionsStringException, MatchingComponentNotFoundException, DuplicateReceiversInCircuitsException {
        LOGGER.info("------------ Deserializer Started ------------ ");
        LOGGER.info("Generator: " + generators);
        LOGGER.info("Receivers: " + receivers);
        LOGGER.info("Connections: " + connections);

        List<Generator> generatorList = stringToGeneratorList(generators);
        List<Receiver> receiverList = stringToReceiversList(receivers);
        List<Connection> connectionsList = stringToConnectionsList(connections, receiverList, generatorList);

        connectComponents(connectionsList);

        Circuit circuit = doCreate(generatorList, receiverList, connectionsList);

        cleanOpenConnections(circuit.getReceivers(), generatorList, receiverList);

        LOGGER.info("------------ Deserializer Finished ------------ ");
        return circuit;
    }

    protected abstract Circuit doCreate(List<Generator> generators, List<Receiver> receivers, List<Connection> connections) throws InvalidNumberOfGeneratorException, DuplicateReceiversInCircuitsException;

    private void connectComponents(List<Connection> connectionsList) {
        connectionsList.forEach(Pin::connectComponents);
    }

    SimpleCircuit buildSimpleCircuitWithGenerator(Generator generator) {
        return SimpleCircuit.Builder.newBuilder()
                .setGenerator(generator)
                .addAllReceivers(retrieveAllReceiversInClosedCircuitWithGenerator(generator))
                .build();
    }

    private void cleanOpenConnections(List<Receiver> receiversInCircuit, List<Generator> allGenerators, List<Receiver> allReceivers) {
        List<Component> components = newArrayList();
        components.addAll(allGenerators);
        components.addAll(receiversInCircuit);
        List<Receiver> unusedReceivers = retrieveReceiversNotInCircuit(receiversInCircuit, allReceivers);
        removeUnusedReceiversFromConnections(unusedReceivers, components);
    }

    private void removeUnusedReceiversFromConnections(List<Receiver> receiversToBeCleaned, List<Component> components) {
        List<String> idReceiversToBeCleaned = receiversToBeCleaned.stream().map(Component::getId).collect(toList());
        for(Component component: components) {
            removeConnectionFrom(component.getFirstPin().getConnections(), idReceiversToBeCleaned);
            removeConnectionFrom(component.getSecondPin().getConnections(), idReceiversToBeCleaned);
        }
    }

    private void removeConnectionFrom(List<Tuple<Pin, Component>> connections, List<String> idReceiversToBeCleaned) {
        List<Tuple<Pin, Component>> firstPinConnection = newArrayList(connections);
        List<Tuple<Pin, Component>> connectionsToBeRemoved = firstPinConnection.stream().filter(componentTuple -> idReceiversToBeCleaned.contains(componentTuple.getSecond().getId())).collect(Collectors.toList());
        connections.removeAll(connectionsToBeRemoved);
    }

    private  List<Receiver> retrieveReceiversNotInCircuit(List<Receiver> receiversInCircuit, List<Receiver> allReceivers) {
        List<String> idReceiversInCircuit = receiversInCircuit.stream().map(Component::getId).collect(toList());

        return allReceivers.stream()
                .filter(receiver -> !idReceiversInCircuit.contains(receiver.getId()))
                .collect(toList());


    }
}