package com.iea;

import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverStatus;

import java.util.Map;

public interface ScreenListener {

    Map<Receiver, ReceiverStatus> onStart(Circuit circuit) ;

    void onStop();
}