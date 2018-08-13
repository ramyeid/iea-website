<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- data-type refers to the type of the components
     0 refers to a wire
     1 refers to a generator
     2 refers to a receiver
--%>

<%-- data-conf refers to the pin configuration
     2 refers to a dipole (+/- configuration)
     1 refers to a resistor (~1/~2 configuration)
--%>

<!DOCTYPE HTML>
<html lang="en">
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js"></script>
  <div id="title" style="height: 10%;">
    <h1>Circuit Builder</h1>
  </div>
</head>
<body>
  <div style="
      display: inline-block;
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      width: 70vw;
      height: 70vh;
      margin: auto;
      background-color: rgba(185, 185, 185, 0.3);
      visibility: hidden;" id="python">
      <textarea style="
        display: inline-block;
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        width: 50vw;
        height: 50vh;
        margin: auto;
        resize: none;" id = "pythonCode" cols = "50" rows = "20" value=""></textarea>
      <input style="
        display: inline-block;
        position: absolute;
        bottom: 0;
        right: 0;
        margin: auto;" id="submitPython" type="button" value="Submit Python Code">
  </div>
  <div id="main" style=" height : 80%; width:80%;">
      <img id = "rpi" src='/images/rpi/rpi.png'/>
  </div>
  <div id="components" style="position: absolute; left: 79%; top: 0; width: 20%;">
    <table border='2'>
      <tr>
        <th colspan='2'>
          <h3>Components</h3>
        </th>
      </tr>
      <tr>
        <td>
          <img id ='led' src='/images/led/led0.png' data-type ="2" data-conf="2" width='145' height='145' />
        </td>
        <td>
          <img id ='buz' src='/images/buzzer/buz0.png' data-type ="2"  data-conf="2" width='145' height='145' />
        </td>
      </tr>
      <tr>
        <td>
          <img id ='res' src='/images/resistor/res0.png' data-type ="2" data-conf="1" width='145' height='145' />
        </td>
        <td>
          <img id ='bat' src='/images/battery/bat.png' data-type ="1" data-conf="2" width='145' height='145' />
        </td>
      </tr>
      <tr>
        <td colspan='2'>
          <img id ='wire' src='/images/jumper.png' data-type ="0" width='145' height='145' />
        </td>
      </tr>
    </table>
  </div>
</body>

<footer style=" position:absolute; bottom:0; width:100%; height:10%; background:#6cf;">
  <div  id="Submit">
    <table>
        <tr>
            <td>
                <input type="button" id="submitbutton" value="Submit">
            </td>
            <td>
                <label id="textLabel"></label>
            </td>
        </tr>
        <tr>
            <td>
                <input type="button" id="showPython" value="Show Python Code"/>
            </td>
            <td>
                <input type="button" id="hidePython" value="Hide Python Code"/>
            </td>
        </tr>
    </table>
  </div>
</footer>
<script src="scripts/canvas.js"></script>
<script src="scripts/canvasWiring.js"></script>
<script src="scripts/canvasUpdater.js"></script>
<script src="scripts/canvasPythonWriter.js"></script>
</html>