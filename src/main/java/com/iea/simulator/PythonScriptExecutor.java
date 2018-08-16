package com.iea.simulator;

import com.iea.simulator.exception.PythonScriptExecutorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringJoiner;

public class PythonScriptExecutor {

    private static final Logger LOGGER = LogManager.getLogger(PythonScriptExecutor.class);

    public static void runPython(String fileName) throws PythonScriptExecutorException {
        LOGGER.info("Running Python file " + fileName);
        String pathToPythonFile = new StringJoiner(File.separator).add("src").add("main").add("resources").add("python-lib").add(fileName+".py").toString();
        String pathToOutputFile = new StringJoiner(File.separator).add("src").add("main").add("resources").add("python-lib").add(fileName+".txt").toString();
        try{
            Files.deleteIfExists(Paths.get(pathToOutputFile));
            Process exec = Runtime.getRuntime().exec("python " + pathToPythonFile + " " + pathToOutputFile);
            exec.waitFor();
        }
        catch(IOException | InterruptedException exception){
            LOGGER.error("Error while executing python file", exception);
            throw new PythonScriptExecutorException(exception);
        }
    }
}
