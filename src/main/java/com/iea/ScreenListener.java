package com.iea;

import com.iea.circuit.Circuit;

public interface ScreenListener {
    void onStart(Circuit circuit);

    void onStop();
}