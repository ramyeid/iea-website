//counter used to assign unique IDs to clones
let counter = 0;

//bind click event to adding components from "components" div in .JSP
components.addEventListener("click", clickedComponent);

//function called on clicking a component
function clickedComponent(event)
{
	caller = event.target
	
	if (caller.id == "components") return; //ignore clicking the div's background
	
	alert("Created new: " + caller.id);
	
	oldComponent = caller;
	newComponent = oldComponent.cloneNode();
	newComponent.id = caller.id + counter++; 

	//add events to the clones
	//mouse down for dragging components
	newComponent.addEventListener("mousedown", moveComponent);
    //right click for deleting components
	newComponent.addEventListener("contextmenu", deleteComponent);
	newComponent.ondragstart = function() {return false;}; //disable default drag event
	document.body.append(newComponent); //add cloned component to body
};

//function called on right click to delete component
function deleteComponent(event)
{
	event.preventDefault(); //prevents default context menu from opening
	event.target.remove(); //deletes component
}

//function used to move component on drag
function moveComponent(event) {
  if (event.which != 1) //if not left click, ignore
	return false;
  caller = event.target;
  caller.style.position = 'absolute';
  caller.style.zIndex = 1000; //place dragged object on top of screen

  document.body.append(caller);

  moveAt(event.pageX, event.pageY);

  //function used to move object center to mouse location
  function moveAt(pageX, pageY) {
    caller.style.left = pageX - caller.offsetWidth / 2 + 'px';
    caller.style.top = pageY - caller.offsetHeight / 2 + 'px';
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
  