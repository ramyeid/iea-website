package com.iea.simulator;

import com.iea.simulator.exception.PythonScriptExecutorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PythonScriptExecutor {

    private static final Logger LOGGER = LogManager.getLogger(PythonScriptExecutor.class);

    private static boolean ProcessFinishedExecution(Process exec){
        try{
            exec.exitValue();
            return true;
        }
        catch(IllegalThreadStateException exception){
            return false;
        }
    }

    public static String runPython(String fileName) throws PythonScriptExecutorException {
        LOGGER.info("Running Python file " + fileName);
        String pathToPythonFile = new StringJoiner(File.separator).add("src").add("main").add("resources").add("python-lib").add(fileName+".py").toString();
        String pathToOutputFile = new StringJoiner(File.separator).add("src").add("main").add("resources").add("python-lib").add(fileName+".txt").toString();
        try{
            Files.deleteIfExists(Paths.get(pathToOutputFile));
            Process exec = Runtime.getRuntime().exec("python " + pathToPythonFile + " " + pathToOutputFile);
            exec.waitFor(4, TimeUnit.SECONDS);
            if (!ProcessFinishedExecution(exec)){
                exec.destroy();
                exec.waitFor();
                throw new PythonScriptExecutorException(new Exception("Python file taking too long to execute"));}
            exec.destroy();
            exec.waitFor();
            if (!ProcessFinishedExecution(exec))
                throw new PythonScriptExecutorException(new Exception("Python file taking too long to execute"));
            return IOUtils.toString(new BufferedReader(new InputStreamReader(exec.getErrorStream())));
        }
        catch(IOException | InterruptedException exception){
            LOGGER.error("Error while executing python file", exception);
            throw new PythonScriptExecutorException(exception);
        }
    }

    private static boolean isAllowed(String library){
        ArrayList<String> allowedLibraries = new ArrayList<String>(Arrays.asList("RPi.GPIO","time"));
        boolean allowed = false;
        for(String allowedLibrary: allowedLibraries)
            if(library.equals(allowedLibrary))
                allowed = true;
        return allowed;
    }

    public static void testPythonSafety(String pythonCode) throws PythonScriptExecutorException {
        ArrayList<String> words = new ArrayList<String>();
        Scanner s = new Scanner(pythonCode);
        while(s.hasNext())
            words.add(s.next());
        for(int i=0;i<words.size()-1;i++){
            if (words.get(i).equals("import") && !isAllowed(words.get(i+1))){
                LOGGER.error("Error while importing libraries in python");
                throw new PythonScriptExecutorException(new Exception("importing non-allowed libraries"));
            }
        }
    }

}
