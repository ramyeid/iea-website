package com.iea.circuit.receiver;

public enum ReceiverStatus {

    OFF(0), LOW(1), OPTIMAL(2), DAMAGED(3);

    private int value;

    private ReceiverStatus(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
