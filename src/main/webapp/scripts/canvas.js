let counter = 0; //counter used to assign unique IDs to clones
let wiringList = []; //array containing linked components' IDs
let receiverList = [];
let generatorList = [];
let wiring = false;
let wiringTarget = null;
let generatorInstantiated = false;
let textLabel =  document.getElementById('textLabel');
let components = document.getElementById('components');
let raspberry = document.getElementById('rpi');
raspberry.style.visibility = "hidden";

const RPI_CIRCUIT_TOKEN = "RPI";
const DEFAULT_CIRCUIT_TOKEN = "DEFAULT";
let circuitType = DEFAULT_CIRCUIT_TOKEN;

//bind click event to adding components from "components" div in .JSP
components.addEventListener("click", function (event){

	let caller = event.target;
	if (caller.id === "components" || caller.id === "") return; //ignore clicking the div's background
    if (caller.dataset.type === "0"){
		switchWiring();
		return;
	}

	if (caller.dataset.type === "1" && !generatorInstantiated){
	    let newComponent = duplicateComponent(caller);
        generatorList.push(newComponent.id);
        generatorInstantiated = true;
        circuitType = DEFAULT_CIRCUIT_TOKEN;
	}

	else if (caller.dataset.type === "3" && !generatorInstantiated) {
        raspberry.style.visibility = "visible";
        generatorInstantiated = true;
        circuitType = RPI_CIRCUIT_TOKEN;
    }

    else if (caller.dataset.type === "2"){
        let newComponent = duplicateComponent(caller);
    	receiverList.push(newComponent.id);
    }

});

function duplicateComponent(caller){

	let newComponent = caller.cloneNode();
	newComponent.id = caller.id + '-' + counter++;

	//add events to the clones
	//mouse down for dragging components
	newComponent.addEventListener("mousedown", mouseDownComponent);
    //right click for deleting components
	newComponent.addEventListener("contextmenu", deleteComponent);
	newComponent.ondragstart = function() {return false;}; //disable default drag event

    newComponent.style.position = 'absolute';
    newComponent.style.zIndex = 1000; //place object on top of screen
	document.body.append(newComponent); //add cloned component to body
	return newComponent;

}

//function attached on right click to delete component
function deleteComponent(event) {
	event.preventDefault(); //prevents default context menu from opening
    if (wiring) return; //prevent deletion if currently wiring
    let caller = event.target;
    clearRelatedWiring(caller.id);

    if (caller.dataset.type === "1") {
        generatorList.splice(generatorList.indexOf(caller.id),1);
        generatorInstantiated = false;
    }
    else if (caller.dataset.type === "2") {
        receiverList.splice(receiverList.indexOf(caller.id),1);
    }

	event.target.remove(); //deletes component
}

//function used to move component on drag
function mouseDownComponent(event) {

  if (event.which !== 1) //if not left click, ignore
	return false;

  let caller = event.target;

  //record initial click offset from object edge for smoother dragging
  let shiftX = event.clientX - caller.getBoundingClientRect().left;
  let shiftY = event.clientY - caller.getBoundingClientRect().top;

  if (wiring){
	  performComponentWiring(caller, shiftX, shiftY);
	  return;}

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

raspberry.addEventListener("contextmenu", function (event) {
    //hide the raspberry pi and delete all the related wiring
    event.preventDefault();
    raspberry.style.visibility = "hidden";
    generatorInstantiated = false;
    clearPiWiring();
    generatorList.length = 0;
});