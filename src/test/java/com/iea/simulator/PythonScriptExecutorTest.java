package com.iea.simulator;

import com.iea.PythonTemplates;
import com.iea.simulator.exception.PythonScriptExecutorException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.StringJoiner;

import static org.junit.Assert.*;

public class PythonScriptExecutorTest {

    private static String fileName = "testing";
    private static String pathToNewPythonFile = new StringJoiner(File.separator).add("src").add("main").add("resources").add("python-lib").add(fileName + ".py").toString();
    private static String pathToOutputFile = new StringJoiner(File.separator).add("src").add("main").add("resources").add("python-lib").add(fileName+".txt").toString();

    @Test
    public void should_verify_the_output_file_of_a_python_with_three_outputs(){
        String pythonCode   = PythonTemplates.createPythonCodeWithThreeOutputs();
        String expected     = PythonTemplates.createPythonCodeWithThreeOutputsResult();
        try {
                Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                String x = PythonScriptExecutor.runPython(fileName);
                String result = new String(Files.readAllBytes(Paths.get(pathToOutputFile)));
                assertEquals(expected,result);
            }
        catch (IOException e) {assert(false);}
        catch (PythonScriptExecutorException e) {assert(false);}
    }

    @Test
    public void should_verify_the_output_file_of_a_python_with_three_inputs(){
        String pythonCode   = PythonTemplates.createPythonCodeWithThreeInputs();
        String expected     = PythonTemplates.createPythonCodeWithThreeInputsResult();
        try {
            Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            PythonScriptExecutor.runPython(fileName);
            String result = new String(Files.readAllBytes(Paths.get(pathToOutputFile)));
            assertEquals(expected,result);
        }
        catch (IOException e) {assert(false);}
        catch (PythonScriptExecutorException e) {assert(false);}
    }

    @Test
    public void should_verify_the_output_file_of_a_python_with_three_outputs_two_high_one_low(){
        String pythonCode   = PythonTemplates.createPythonCodeWithThreeOutputsTwoHighOneLow();
        String expected     = PythonTemplates.createPythonCodeWithThreeOutputsTwoHighOneLowResult();
        try {
            Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            PythonScriptExecutor.runPython(fileName);
            String result = new String(Files.readAllBytes(Paths.get(pathToOutputFile)));
            assertEquals(expected,result);
        }
        catch (IOException e) {assert(false);}
        catch (PythonScriptExecutorException e) {assert(false);}
    }

    @Test
    public void should_verify_the_exception_message_of_a_python_with_setup_pin_1_error(){
        String pythonCode   = PythonTemplates.createPythonCodeWithErrorInSetupPin1();
        String expected     = PythonTemplates.createPythonCodeWithErrorInSetupPin1Exception();
        try {
            Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            String result = PythonScriptExecutor.runPython(fileName);
            assertEquals(expected,result);
        }
        catch (IOException e) {assert(false);}
        catch (PythonScriptExecutorException e) {assert(false);}
    }

    @Test
    public void should_verify_the_exception_message_of_a_python_with_no_setup_error(){
        String pythonCode   = PythonTemplates.createPythonCodeWithErrorInNoSetup();
        String expected     = PythonTemplates.createPythonCodeWithErrorInNoSetupException();
        try {
            Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            String result = PythonScriptExecutor.runPython(fileName);
            assertEquals(expected,result);
        }
        catch (IOException e) {assert(false);}
        catch (PythonScriptExecutorException e) {assert(false);}
    }

    @Test
    public void should_verify_the_exception_message_of_a_python_with_output_pin_3_error(){
        String pythonCode   = PythonTemplates.createPythonCodeWithErrorInOutputPin3();
        String expected     = PythonTemplates.createPythonCodeWithErrorInOutputPin3Exception();
        try {
            Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            String result = PythonScriptExecutor.runPython(fileName);
            assertEquals(expected,result);
        }
        catch (IOException e) {assert(false);}
        catch (PythonScriptExecutorException e) {assert(false);}
    }

    @Test
    public void should_verify_the_exception_message_of_a_python_with_wrong_pin_5_type_error(){
        String pythonCode   = PythonTemplates.createPythonCodeWithErrorInPin5Type();
        String expected     = PythonTemplates.createPythonCodeWithErrorInPin5TypeException();
        try {
            Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            String result = PythonScriptExecutor.runPython(fileName);
            assertEquals(expected,result);
        }
        catch (IOException e) {assert(false);}
        catch (PythonScriptExecutorException e) {assert(false);}
    }

    @Test
    public void should_verify_the_output_file_of_a_python_with_ten_sleeps(){
        String pythonCode   = PythonTemplates.createPythonCodeWithTenSleeps();
        String expected     = PythonTemplates.createPythonCodeWithTenSleepsResult();
        try {
            Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            PythonScriptExecutor.runPython(fileName);
            String result = new String(Files.readAllBytes(Paths.get(pathToOutputFile)));
            assertEquals(expected,result);
        }
        catch (IOException e) {assert(false);}
        catch (PythonScriptExecutorException e) {assert(false);}
    }


    //Need to add exception if we pass an str or anything other then a number to time.sleep()! as it will print to the output file sleep:SomeString.
    @Test
    public void should_verify_the_output_file_of_a_python_with_string_argument_to_sleep(){
        String pythonCode   = PythonTemplates.createPythonCodeWithStringArgToSleep();
        String expected     = PythonTemplates.createPythonCodeWithTenSleepsResult();
        try {
            Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            PythonScriptExecutor.runPython(fileName);
            String result = new String(Files.readAllBytes(Paths.get(pathToOutputFile)));
            assertEquals(expected,result);
        }
        catch (IOException e) {assert(false);}
        catch (PythonScriptExecutorException e) {assert(false);}
    }

}