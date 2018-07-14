//counter used to assign unique IDs to clones
let counter = 0;

//array containing linked components' IDs
let wiringList = [];
let receiverList = [];
let generatorList = [];
let wiring = false;
let wiringTarget = null;

let textlabel =  document.getElementById('textlabel');

let components = document.getElementById("components");
//bind click event to adding components from "components" div in .JSP
components.addEventListener("click", clickedComponent);

function checkDipolePin(object, clickx)
{
    if (clickx > object.width/2)
    {
        return "+";
    }
    else
    {
        return "-";
    }
}

//function called on clicking a component
function clickedComponent(event)
{
	caller = event.target
	
	if (caller.id == "components" || caller.id == "") return; //ignore clicking the div's background
    if (caller.id == "wire")
    {
      if(wiring)
      {
        wiring = false;
        wiringTarget = null;
        document.body.style.cursor = 'auto';
      }
      else
      {
        wiring = true;
        wiringTarget = null;
        document.body.style.cursor = 'nesw-resize';
      }
      return;
    }

	newComponent = caller.cloneNode();
	newComponent.id = caller.id + '-' + counter++;
    if (caller.id == "bat")
        generatorList.push(newComponent.id);
    else
    	receiverList.push(newComponent.id);

	//add events to the clones
	//mouse down for dragging components
	newComponent.addEventListener("mousedown", moveComponent);
    //right click for deleting components
	newComponent.addEventListener("contextmenu", deleteComponent);
	newComponent.ondragstart = function() {return false;}; //disable default drag event

    newComponent.style.position = 'absolute';
    newComponent.style.zIndex = 1000; //place object on top of screen
	document.body.append(newComponent); //add cloned component to body
};

function clearRelatedWiring(caller)
{
    for(var i=0; i<wiringList.length;i++)
    {
      if (wiringList[i][0][0] == caller.id || wiringList[i][1][0] == caller.id)
      {
        wiringList[i].length = 0;
      }
    }

    for(var i=wiringList.length-1; i>=0;i--)
    {
      if (wiringList[i].length == 0)
        wiringList.splice(i,1);
    }
}

//function called on right click to delete component
function deleteComponent(event)
{
	event.preventDefault(); //prevents default context menu from opening
    if (wiring) return; //prevent deletion if currently wiring
    caller = event.target;
    clearRelatedWiring(caller);

    if (caller.id.substring(0,3) == "bat")
        generatorList.splice(generatorList.indexOf(caller.id),1);
    else
        receiverList.splice(receiverList.indexOf(caller.id),1);

	event.target.remove(); //deletes component
}

//function used to move component on drag
function moveComponent(event) {

  if (event.which != 1) //if not left click, ignore
	return false;

  caller = event.target;

  //record initial click offset from object edge for smoother dragging
  let shiftX = event.clientX - caller.getBoundingClientRect().left;
  let shiftY = event.clientY - caller.getBoundingClientRect().top;

  if (wiring)
	{
	    if (wiringTarget == null)
	    {
	        let pin = checkDipolePin(caller, shiftX);
	        wiringTarget = [caller.id,pin];
	        return false;
	    }
	    else if (wiringTarget[0] == caller.id)
	    {
	        return false;
	    }
	    else
	    {
	        let pin = checkDipolePin(caller, shiftX);
	        let connection = [wiringTarget, [caller.id,pin]];
	        wiringList.push(connection);
            textlabel.innerHTML = "new connection:" + connection;
	        wiring = false;
	        wiringTarget = null;
	        document.body.style.cursor = 'auto';
	        return true;
	    }
	}

  caller.style.position = 'absolute';
  caller.style.zIndex = 1000; //place dragged object on top of screen

  document.body.append(caller);

  moveAt(event.pageX, event.pageY);

  //function used to move object center to mouse location
  function moveAt(pageX, pageY) {
    caller.style.left = pageX - shiftX + 'px';
    caller.style.top = pageY - shiftY + 'px';
  }

  //function called on mouse move to move object
  function onMouseMove(event) {
    moveAt(event.pageX, event.pageY);
  }

  document.addEventListener('mousemove', onMouseMove); //adding mouse move event listener

  //function called when mouse click released
  caller.onmouseup = function() {
    document.removeEventListener('mousemove', onMouseMove); //delete mouse move event listener
    caller.onmouseup = null; //delete mouse up event listener
  };
}

submitbutton.onclick = function(){
    let connectionsString = wiringList.toString();
    let receiversString = receiverList.toString();
    let generatorsString = generatorList.toString();

    $.ajax({
         type: 'POST',
         url: "/canvas/update",
         data: { generators: generatorsString, receivers: receiversString, connections: connectionsString} });

    textlabel.innerHTML = 'Generators: ' + generatorsString + ' ||| Receivers: ' + receiversString + ' ||| Connections: ' + connectionsString;
};