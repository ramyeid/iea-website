/* JavaScript responsible for everything related to wiring,
 * functions here are called from the main javascript file
 * switchWiring switches the wiring mode on components
 * clearRelatedWiring clears all wiring related to a certain component
 * performComponentWiring is called when a component is selected for wiring
 * and links it to the last selected component (if there are any)
 */

const RASPBERRY_PI_ID = "rpi";
let wiringFromPi = false;

function switchWiring() {
	
	if(wiring) {
		wiring = false;
		wiringTarget = null;
		wiringFromPi = false;
        document.body.style.cursor = 'auto'; }
	else {
		wiring = true;
		wiringTarget = null;
        wiringFromPi = false;
        document.body.style.cursor = 'nesw-resize';}
		
}

function clearRelatedWiring(callerId) {

    for(let i=0; i<wiringList.length;i++)
    {
        if (wiringList[i][0][0] === callerId || wiringList[i][1][0] === callerId)
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

function clearPiWiring() {

    for (let generatorId of generatorList){
        clearRelatedWiring(generatorId);
    }

    clearRelatedWiring("gnd");
}

function performComponentWiring(caller, shiftX, shiftY){
	
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

function performPiWiring(pin){

    if (wiringTarget == null) {
        wiringTarget = preparePiWiringTarget(pin);
        wiringFromPi = true;
        return false; }
    else if (wiringFromPi) { //prevent pi to pi wiring
        return false;
    }
    else {
        let connection = [wiringTarget, preparePiWiringTarget(pin)];
        wiringList.push(connection);
        textLabel.innerHTML = "new connection:" + connection;
        switchWiring();
        return true;
    }

}

function preparePiWiringTarget(raspberryPin) {
    if (raspberryPin === "gnd") {
        return [raspberryPin, "-"];
    }
    else {
        if (generatorList.indexOf(raspberryPin) === -1)
            generatorList.push(raspberryPin);

        return [raspberryPin, "%2B"];
    }
}

function checkPin (object, clickX, clickY){

	switch(object.dataset.conf)
	{
        case "1":
            return checkResistorPin(object, clickX);
        case "2":
            return checkDipolePin(object, clickX);
	}

}


function checkDipolePin(object, clickX) {

    if (clickX > object.width/2)
        return "%2B";
    else
        return "-";
}

function checkResistorPin(object, clickX) {
    if (clickX > object.width/2)
        return "~1";
    else
        return "~2";
}

$("map[name=pimap] area").on('click', function () {

    event.preventDefault();
    if(wiring) {
        let clickedPin = ($(this).attr('data-pin'));
        performPiWiring(clickedPin);
    }

});
