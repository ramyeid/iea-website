package com.iea.circuit.receiver;

import org.junit.Test;

import static org.junit.Assert.*;

public class LEDTest {

    @Test
    public void should_return_true_when_calculated_volt_positive(){
        LED led =new LED(2);
        assertTrue(led.retrieveStatus(4,5));
    }

}