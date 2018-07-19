package com.iea;

import com.iea.circuit.Circuit;

public interface ScreenListener {
    boolean onStart(Circuit circuit);

    void onStop();
}