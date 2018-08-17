package com.iea.circuit;

import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverStatus;

import java.util.List;
import java.util.Map;

public interface Circuit {

    List<Receiver> getReceivers();

    Map<Receiver, ReceiverStatus> generateReceiverStatuses();
}