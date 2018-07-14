<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%--@elvariable id="text" type="java.lang.string"--%>

<!DOCTYPE HTML>
<html lang="en">
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js"></script>
  <div id="title" style="height: 10%;">
    <h1>Circuit Builder</h1>
  </div>
</head>
<body>
  <div id="main" style=" height : 80%; width:80%;">
      <img id = "rpi" src='img/rpi.png'/>
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
          <img id ='led' src='img/led.png' width='145' height='145'/>
        </td>
        <td>
          <img id ='buz' src='img/buzzer.png' width='145' height='145'/>
        </td>
      </tr>
      <tr>
        <td>
          <img id ='res' src='img/resistor.png' width='145' height='145'/>
        </td>
        <td>
          <img id ='bat' src='img/battery.png' width='145' height='145'/>
        </td>
      </tr>
      <tr>
        <td colspan='2'>
          <img id ='wire' src='img/jumper.png' width='145' height='145' />
        </td>
      </tr>
    </table>
  </div>
</body>

<footer style=" position:absolute; bottom:0; width:100%; height:10%; background:#6cf;">
  <div  id="Submit">
    <input type="button" id="submitbutton" value="Submit">
    <label id="textlabel"></label>
  </div>
</footer>
<script src="scripts/canvas.js" ></script>
</html>