package com.iea.jsmapping;

import com.iea.circuit.creator.factory.CircuitType;
import com.iea.circuit.model.Connection;
import com.iea.circuit.model.component.Component;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.pin.Pin;
import com.iea.circuit.utils.exception.MatchingComponentNotFoundException;
import com.iea.jsmapping.exception.InvalidConnectionsStringException;
import com.iea.jsmapping.exception.NoMatchingPinFoundException;
import com.iea.jsmapping.exception.UnrecognisedCircuitTypeException;
import com.iea.jsmapping.exception.UnrecognisedPinRepresentationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.iea.circuit.model.component.generator.GeneratorFactory.createGenerator;
import static com.iea.circuit.model.component.receiver.ReceiverFactory.createReceiver;
import static com.iea.circuit.utils.CircuitUtils.getComponentById;
import static com.iea.jsmapping.Configurations.*;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;

public class JavascriptMapping {

    private static final Logger LOGGER = LogManager.getLogger(JavascriptMapping.class);

    private static final String ID_TOKEN = "-";
    private static final String GROUND_TOKEN = "gnd";
    private static final String ELEMENTS_SEPERATOR = ",";
    private static final String CONNECTIONS_SEPERATOR = ";";
    private static final String DEFAULT_CIRCUIT_TYPE = "DEFAULT";
    private static final String RPI_CIRCUIT_TYPE = "RPI";

    private static final char POSITIVE_PIN_REPRESENTATION = '+';
    private static final char NEGATIVE_PIN_REPRESENTATION = '-';
    private static final char NEUTRAL_PIN_REPRESENTATION = '~';

    public static List<Generator> stringToGeneratorList(String generatorsString) {
        if (!generatorsString.isEmpty()) {
            return stream(generatorsString.split(ELEMENTS_SEPERATOR))
                    .map(generatorId -> createGenerator(generatorId, getGeneratorConfiguration(getRawId(generatorId))))
                    .collect(Collectors.toList());
        }
        return emptyList();
    }

    public static List<Receiver> stringToReceiversList(String receiversString) {
        if (!receiversString.isEmpty()) {
            return stream(receiversString.split(ELEMENTS_SEPERATOR))
                    .map(receiverId -> createReceiver(receiverId, getReceiverType(getRawId(receiverId)), getReceiverConfiguration(getRawId(receiverId))))
                    .collect(Collectors.toList());
        }
        return emptyList();
    }

    public static List<Connection> stringToConnectionsList(String connectionsString, List<Receiver> receiverList, List<Generator> generatorsList) throws NoMatchingPinFoundException, InvalidConnectionsStringException, MatchingComponentNotFoundException {
        List<Component> componentsList = newArrayList();
        componentsList.addAll(receiverList);
        componentsList.addAll(generatorsList);

        if (connectionsString.isEmpty())
            return emptyList();

        List<Connection> connectionsList = newArrayList();
        try {
            for (String connection : connectionsString.split(CONNECTIONS_SEPERATOR)) {
                List<String> connectables = asList(connection.split(ELEMENTS_SEPERATOR));

                if (connectables.get(0).equals(GROUND_TOKEN)) {
                    connectionsList.addAll(
                            generateConnectionsToAllGrounds(connectables.get(2), connectables.get(3), componentsList, generatorsList));
                } else if (connectables.get(2).equals(GROUND_TOKEN)) {
                    connectionsList.addAll(
                            generateConnectionsToAllGrounds(connectables.get(0), connectables.get(1), componentsList, generatorsList));
                } else {
                    connectionsList.add(
                            generateConnection(connectables.get(0), connectables.get(1),
                                    connectables.get(2), connectables.get(3), componentsList));
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidConnectionsStringException(connectionsString);
        }
        return connectionsList;
    }

    public static CircuitType stringToCircuitType(String circuitTypeString) throws UnrecognisedCircuitTypeException {
        switch (circuitTypeString) {
            case DEFAULT_CIRCUIT_TYPE:
                return CircuitType.DEFAULT_CIRCUIT;
            case RPI_CIRCUIT_TYPE:
                return CircuitType.RPI_CIRCUIT;
            default:
                throw new UnrecognisedCircuitTypeException(circuitTypeString);
        }
    }

    /**
     * Function used to decode which pin should be returned from a component based on a string representation
     *
     * @param pinRepresentation: string containing a representation of the concerned pin
     * @param component:         Component object whose pins are being checked
     * @return returns the concerned pin of the component
     */
    private static Pin decodePin(String pinRepresentation, Component component) throws NoMatchingPinFoundException {
        if (pinRepresentation.charAt(0) == NEUTRAL_PIN_REPRESENTATION) {
            if (pinRepresentation.charAt(1) == '1') {
                return component.getFirstPin();
            } else {
                return component.getSecondPin();
            }
        }
        List<Pin> matchingPins = component.getPins().stream().filter(t -> {
            try {
                return t.getType().equals(stringToPinType(pinRepresentation));
            } catch (UnrecognisedPinRepresentationException e) {
                return false;
            }
        }).collect(Collectors.toList());

        if (matchingPins.size() > 1 || matchingPins.isEmpty()) {
            LOGGER.error("No matching pin found for " + pinRepresentation);
            throw new NoMatchingPinFoundException(pinRepresentation);
        }
        return matchingPins.get(0);
    }

    private static Pin.Type stringToPinType(String pinRepresentation) throws UnrecognisedPinRepresentationException {
        switch(pinRepresentation.charAt(0)){
            case NEGATIVE_PIN_REPRESENTATION:
                return Pin.Type.NEGATIVE;
            case POSITIVE_PIN_REPRESENTATION:
                return Pin.Type.POSITIVE;
            case NEUTRAL_PIN_REPRESENTATION:
                return Pin.Type.NEUTRAL;
            default:
                throw new UnrecognisedPinRepresentationException(pinRepresentation);
        }
    }

    private static Connection generateConnection(String sourceComponentId, String sourceComponentPin, String destinationComponentId, String destinationComponentPin, List<Component> componentsList) throws NoMatchingPinFoundException, MatchingComponentNotFoundException {
        Component sourceComponent = getComponentById(sourceComponentId, componentsList);
        Component destinationComponent = getComponentById(destinationComponentId, componentsList);

        return new Connection(sourceComponent, decodePin(sourceComponentPin, sourceComponent),
                destinationComponent, decodePin(destinationComponentPin, destinationComponent));
    }

    private static List<Connection> generateConnectionsToAllGrounds(String targetComponentId, String targetPinRepresentation, List<Component> componentsList, List<Generator> generatorsList) throws NoMatchingPinFoundException, MatchingComponentNotFoundException {
        Component targetComponent = getComponentById(targetComponentId, componentsList);
        Pin targetPin = decodePin(targetPinRepresentation, targetComponent);

        return generatorsList.stream().map(generator -> new Connection(targetComponent, targetPin, generator, generator.getNegativePin())).collect(Collectors.toList());
    }

    private static String getRawId(String generatorId) {
        return generatorId.split(ID_TOKEN)[0];
    }
}
