package com.iea;

public class PythonTemplates {

    private static String endLine = System.lineSeparator();
    private static String  tab = "  ";

    public static String createPythonCodeWithThreeOutputs(){
        return "import RPi.GPIO as GPIO\n" +
                "GPIO.setmode(GPIO.BOARD)\n" +
                "GPIO.setup(3,GPIO.OUT)\n" +
                "GPIO.setup(5,GPIO.OUT)\n" +
                "GPIO.setup(7,GPIO.OUT)\n";
    }

    public static String createPythonCodeWithThreeOutputsResult(){
        return "3:out" + endLine +
                "5:out" + endLine +
                "7:out" + endLine;
    }

    public static String createPythonCodeWithErrorInSetupPin1(){
        return "import RPi.GPIO as GPIO\n" +
                "GPIO.setmode(GPIO.BOARD)\n" +
                "GPIO.setup(1,GPIO.OUT)\n" +
                "GPIO.setup(5,GPIO.OUT)\n" +
                "GPIO.setup(7,GPIO.OUT)\n";
    }

    public static String createPythonCodeWithErrorInSetupPin1Exception(){
        return "Traceback (most recent call last):" + endLine +
                tab + "File \"src\\main\\resources\\python-lib\\testing.py\", line 3, in <module>" + endLine +
                tab + tab + "GPIO.setup(1,GPIO.OUT)" + endLine +
                tab + "File \"D:\\dev\\ieawebsite\\src\\main\\resources\\python-lib\\RPi\\GPIO.py\", line 41, in setup" + endLine +
                tab + tab + "raise Exception(\"Pin {} is invalid!\".format(pin))" + endLine +
                "Exception: Pin 1 is invalid!" + endLine;
    }

    public static String createPythonCodeWithErrorInNoSetup(){
        return "import RPi.GPIO as GPIO\n" +
                "GPIO.setup(3,GPIO.OUT)\n" +
                "GPIO.setup(5,GPIO.OUT)\n" +
                "GPIO.setup(7,GPIO.OUT)\n";
    }

    public static String createPythonCodeWithErrorInNoSetupException(){
        return "Traceback (most recent call last):" + endLine +
                tab + "File \"src\\main\\resources\\python-lib\\testing.py\", line 2, in <module>" + endLine +
                tab + tab + "GPIO.setup(3,GPIO.OUT)" + endLine +
                tab + "File \"D:\\dev\\ieawebsite\\src\\main\\resources\\python-lib\\RPi\\GPIO.py\", line 32, in setup" + endLine +
                tab + tab + "raise Exception(\"Pin numbering mode has not been set up!\")" + endLine +
                "Exception: Pin numbering mode has not been set up!" + endLine;
    }


    public static String createPythonCodeWithErrorInOutputPin3(){
        return "import RPi.GPIO as GPIO\n" +
                "GPIO.setmode(GPIO.BOARD)\n" +
                "GPIO.setup(5,GPIO.OUT)\n" +
                "GPIO.setup(7,GPIO.OUT)\n" +
                "GPIO.output(3,GPIO.OUT)\n";
    }

    public static String createPythonCodeWithErrorInOutputPin3Exception(){
        return "Traceback (most recent call last):" + endLine +
                tab + "File \"src\\main\\resources\\python-lib\\testing.py\", line 5, in <module>" + endLine +
                tab + tab + "GPIO.output(3,GPIO.OUT)" + endLine +
                tab + "File \"D:\\dev\\ieawebsite\\src\\main\\resources\\python-lib\\RPi\\GPIO.py\", line 53, in output" + endLine +
                tab + tab + "raise Exception(\"The GPIO channel has not been set up as an OUTPUT\")" + endLine +
                "Exception: The GPIO channel has not been set up as an OUTPUT" + endLine;
    }


    public static String createPythonCodeWithThreeInputs(){
        return "import RPi.GPIO as GPIO\n" +
                "GPIO.setmode(GPIO.BOARD)\n" +
                "GPIO.setup(3,GPIO.IN)\n" +
                "GPIO.setup(5,GPIO.IN)\n" +
                "GPIO.setup(7,GPIO.IN)\n";
    }

    public static String createPythonCodeWithThreeInputsResult(){
        return "3:in" + endLine +
                "5:in" + endLine +
                "7:in" + endLine;
    }

    public static String createPythonCodeWithThreeOutputsTwoHighOneLow(){
        return "import RPi.GPIO as GPIO\n" +
                "GPIO.setmode(GPIO.BOARD)\n" +
                "GPIO.setup(3,GPIO.OUT)\n" +
                "GPIO.setup(5,GPIO.OUT)\n" +
                "GPIO.setup(7,GPIO.OUT)\n" +
                "GPIO.output(3,GPIO.HIGH)\n" +
                "GPIO.output(5,True)\n" +
                "GPIO.output(7,GPIO.LOW)\n";
    }

    public static String createPythonCodeWithThreeOutputsTwoHighOneLowResult(){
        return "3:out" + endLine +
                "5:out" + endLine +
                "7:out" + endLine +
                "3>True" + endLine +
                "5>True" + endLine +
                "7>False" + endLine;
    }

    public static String createPythonCodeWithErrorInPin5Type(){
        return "import RPi.GPIO as GPIO\n" +
                "GPIO.setmode(GPIO.BOARD)\n" +
                "GPIO.setup(3,'in')\n" +
                "GPIO.setup(5,'io')\n";
    }

    public static String createPythonCodeWithErrorInPin5TypeException(){
        return "Traceback (most recent call last):" + endLine +
                tab + "File \"src\\main\\resources\\python-lib\\testing.py\", line 4, in <module>" + endLine +
                tab + tab + "GPIO.setup(5,'io')" + endLine +
                tab + "File \"D:\\dev\\ieawebsite\\src\\main\\resources\\python-lib\\RPi\\GPIO.py\", line 39, in setup" + endLine +
                tab + tab + "raise Exception(\"An invalid direction was passed to setup()\")" + endLine +
                "Exception: An invalid direction was passed to setup()" + endLine;
    }

    public static String createPythonCodeWithTenSleeps(){
        return "import sys\n" +
                "sys.modules['time'] = __import__('time_mock')\n" +
                "import time\n" +
                "for i in range(1,11):\n" +
                "\ttime.sleep(i)\n";
    }

    public static String createPythonCodeWithTenSleepsResult(){
        return "sleep:1" + endLine +
                "sleep:2" + endLine +
                "sleep:3" + endLine +
                "sleep:4" + endLine +
                "sleep:5" + endLine +
                "sleep:6" + endLine +
                "sleep:7" + endLine +
                "sleep:8" + endLine +
                "sleep:9" + endLine +
                "sleep:10" + endLine;
    }

    public static String createPythonCodeWithStringArgToSleep(){
        return "import sys\n" +
                "sys.modules['time'] = __import__('time_mock')\n" +
                "import time\n" +
                "time.sleep('Need Exception!')\n";
    }

    public static String createPythonCodeWithStringArgToSleepException(){
        return "Traceback (most recent call last):" + endLine +
                tab + "File \"src\\main\\resources\\python-lib\\testing.py\", line 4, in <module>" + endLine +
                tab + tab + "time.sleep('Need Exception!')" + endLine +
                tab + "File \"D:\\dev\\ieawebsite\\src\\main\\resources\\python-lib\\time_mock.py\", line 9, in sleep" + endLine +
                tab + tab + "raise Exception(\"Only numbers can be passed to sleep()\")" + endLine +
                "Exception: Only numbers can be passed to sleep()" + endLine;
    }

    public static String createPythonCodeWithInfiniteLoop(){
        return "import sys\n" +
                "sys.modules['time'] = __import__('time_mock')\n" +
                "import time\n" +
                "while True:\n" +
                "\ttime.sleep(5)\n";
    }

}
