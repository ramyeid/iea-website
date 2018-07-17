package com.iea.circuit.receiver;

import org.junit.Test;

import static org.junit.Assert.*;

public class MotorTest {
    @Test
    public void should_return_true_when_calculated_volt_positive(){
        Motor motor =new Motor(2);
        assertTrue(motor.retrieveStatus(4,5));
    }

}