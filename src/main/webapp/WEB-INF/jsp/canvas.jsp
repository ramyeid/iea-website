<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

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
          <img id ='led' src='/images/led/led0.png' width='145' height='145'/>
        </td>
        <td>
          <img id ='buz' src='/images/buzzer/buz0.png' width='145' height='145'/>
        </td>
      </tr>
      <tr>
        <td>
          <img id ='res' src='/images/resistor/res0.png' width='145' height='145'/>
        </td>
        <td>
          <img id ='bat' src='/images/battery/bat.png' width='145' height='145'/>
        </td>
      </tr>
      <tr>
        <td colspan='2'>
          <img id ='wire' src='/images/jumper.png' width='145' height='145' />
        </td>
      </tr>
    </table>
  </div>
</body>

<footer style=" position:absolute; bottom:0; width:100%; height:10%; background:#6cf;">
  <div  id="Submit">
    <input type="button" id="submitbutton" value="Submit">
    <label id="textLabel"></label>
  </div>
</footer>
<script src="scripts/canvas.js" ></script>
</html>