/* JavaScript responsible for everything related to wiring,
 * functions here are called from the main javascript file
 * switchWiring switches the wiring mode on components
 * clearRelatedWiring clears all wiring related to a certain component
 * performWiring is called when a component is selected for wiring
 * and links it to the last selected component (if there are any)
 */

function switchWiring() {
	
	if(wiring) {
		wiring = false;
		wiringTarget = null;
		document.body.style.cursor = 'auto'; }
	else {
		wiring = true;
		wiringTarget = null;
		document.body.style.cursor = 'nesw-resize';}
		
}

function clearRelatedWiring(caller) {
	
    for(let i=0; i<wiringList.length;i++)
    {
      if (wiringList[i][0][0] === caller.id || wiringList[i][1][0] === caller.id)
      {
        wiringList[i].length = 0;
      }
    }

    for(let i=wiringList.length-1; i>=0;i--)
    {
      if (wiringList[i].length === 0)
        wiringList.splice(i,1);
    }
	
}

function performWiring(caller, shiftX, shiftY){
	
	    if (wiringTarget == null) {
	        let pin = checkPin(caller, shiftX, shiftY);
	        wiringTarget = [caller.id,pin];
	        return false; }
	    else if (wiringTarget[0] === caller.id) {
	        return false;
	    }
	    else {
	        let pin = checkPin(caller, shiftX, shiftY);
	        let connection = [wiringTarget, [caller.id,pin]];
	        wiringList.push(connection);
            textLabel.innerHTML = "new connection:" + connection;
	        switchWiring();
	        return true;
	    }
		
}

function checkPin (object, clickX, clickY){

	switch(caller.dataset.conf)
	{
        case "1":
            return checkResistorPin(object, clickX);
        case "2":
            return checkDipolePin(object, clickX);
	}

}

function checkDipolePin(object, clickX) {

    if (clickX > object.width/2)
        return "+";
    else
        return "-";
}

function checkResistorPin(object, clickX) {
    if (clickX > object.width/2)
        return "~1";
    else
        return "~2";
}