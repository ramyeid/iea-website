<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- data-type refers to the type of the components
     0 refers to a wire
     1 refers to a standalone generator
     2 refers to a receiver
     3 refers to a raspberry pi
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

  <img id = "rpi" src='/images/rpi/rpi.svg' width='300' height='150' usemap="#pimap"/>
  <map name="pimap">
      <area data-pin="rpiV3" shape="circle" coords ="60,14,3" >
      <area data-pin="rpiV5" shape="circle" coords ="60,8,3" >
      <area data-pin="gpio-3" shape="circle" coords ="66,14,3" >
      <area data-pin="rpiV5" shape="circle" coords ="66,8,3" >
      <area data-pin="gpio-5" shape="circle" coords ="72,14,3" >
      <area data-pin="gnd" shape="circle" coords ="72,8,3" >
      <area data-pin="gpio-7" shape="circle" coords ="78,14,3" >
      <area data-pin="gpio-8" shape="circle" coords ="78,8,3" >
      <area data-pin="gnd" shape="circle" coords ="85,14,3" >
      <area data-pin="gpio-10" shape="circle" coords ="85,8,3" >
      <area data-pin="gpio-11" shape="circle" coords ="92,14,3" >
      <area data-pin="gpio-12" shape="circle" coords ="92,8,3" >
      <area data-pin="gpio-13" shape="circle" coords ="98,14,3" >
      <area data-pin="gnd" shape="circle" coords ="98,8,3" >
      <area data-pin="gpio-15" shape="circle" coords ="105,14,3" >
      <area data-pin="gpio-16" shape="circle" coords ="105,8,3" >
      <area data-pin="rpiV3" shape="circle" coords ="111,14,3" >
      <area data-pin="gpio-18" shape="circle" coords ="111,8,3" >
      <area data-pin="gpio-19" shape="circle" coords ="118,14,3" >
      <area data-pin="gnd" shape="circle" coords ="118,8,3" >
      <area data-pin="gpio-21" shape="circle" coords ="124,14,3" >
      <area data-pin="gpio-22" shape="circle" coords ="124,8,3" >
      <area data-pin="gpio-23" shape="circle" coords ="131,14,3" >
      <area data-pin="gpio-24" shape="circle" coords ="131,8,3" >
      <area data-pin="gnd" shape="circle" coords ="137,14,3" >
      <area data-pin="gpio-26" shape="circle" coords ="137,8,3" >
      <area data-pin="gpio-27" shape="circle" coords ="143,14,3" >
      <area data-pin="gpio-28" shape="circle" coords ="143,8,3" >
      <area data-pin="gpio-29" shape="circle" coords ="150,14,3" >
      <area data-pin="gnd" shape="circle" coords ="150,8,3" >
      <area data-pin="gpio-31" shape="circle" coords ="156,14,3" >
      <area data-pin="gpio-32" shape="circle" coords ="156,8,3" >
      <area data-pin="gpio-33" shape="circle" coords ="162,14,3" >
      <area data-pin="gnd" shape="circle" coords ="162,8,3" >
      <area data-pin="gpio-35" shape="circle" coords ="169,14,3" >
      <area data-pin="gpio-36" shape="circle" coords ="169,8,3" >
      <area data-pin="gpio-37" shape="circle" coords ="175,14,3" >
      <area data-pin="gpio-38" shape="circle" coords ="175,8,3" >
      <area data-pin="gnd" shape="circle" coords ="182,14,3" >
      <area data-pin="gpio-40" shape="circle" coords ="182,8,3" >
  </map>

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
          <td>
              <img id ='rpi-icon' src='/images/rpi/rpi.svg' data-type ="3" width='145' height='145' />
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