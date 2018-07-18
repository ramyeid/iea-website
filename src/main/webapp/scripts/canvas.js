//counter used to assign unique IDs to clones
let counter = 0;

//array containing linked components' IDs
let wiringList = [];
let componentList = [];
let wiring = false;
let wiringTarget = null;

let components = document.getElementById("components");
//bind click event to adding components from "components" div in .JSP
components.addEventListener("click", clickedComponent);

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
	componentList.push(newComponent.id);

	//add events to the clones
	//mouse down for dragging components
	newComponent.addEventListener("mousedown", moveComponent);
    //right click for deleting components
	newComponent.addEventListener("contextmenu", deleteComponent);
	newComponent.ondragstart = function() {return false;}; //disable default drag event
	newComponent.l
	document.body.append(newComponent); //add cloned component to body
};

//function called on right click to delete component
function deleteComponent(event)
{
	event.preventDefault(); //prevents default context menu from opening
    caller = event.target;
    for(var i=0; i<wiringList.length;i++)
    {
      if (wiringList[i][0] == caller.id || wiringList[i][1] == caller.id)
      {
        wiringList[i].length = 0;
      }
    }

    for(var i=wiringList.length-1; i>=0;i--)
    {
      if (wiringList[i].length == 0)
        wiringList.splice(i,1);
    }
	event.target.remove(); //deletes component
}

//function used to move component on drag
function moveComponent(event) {
  if (event.which != 1) //if not left click, ignore
	return false;

  caller = event.target;

  if (wiring)
	{
	    if (wiringTarget == null)
	    {
	        wiringTarget = caller;
	        return false;
	    }
	    else if (wiringTarget == caller)
	    {
	        return false;
	    }
	    else
	    {
	        let connection = [wiringTarget.id, caller.id];
	        wiringList.push(connection);
	        alert("connected:" + wiringTarget.id + " to " + caller.id);
	        wiring = false;
	        wiringTarget = null;
	        document.body.style.cursor = 'auto';
	        return true;
	    }
	}

  //record initial click offset from object edge for smoother dragging
  let shiftX = event.clientX - caller.getBoundingClientRect().left;
  let shiftY = event.clientY - caller.getBoundingClientRect().top;

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

$.ajax({
     type: 'POST',
     url: "/canvas/update",
     data: { name: "maincircuit", connections: connectionsString} });

alert("Circuit Updated!");
};