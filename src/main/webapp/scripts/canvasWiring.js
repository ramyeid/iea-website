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

function performWiring(caller, shiftX, shiftY){
	
	    if (wiringTarget == null) {
	        let pin = checkDipolePin(caller, shiftX);
	        wiringTarget = [caller.id,pin];
	        return false; }
	    else if (wiringTarget[0] == caller.id) {
	        return false;
	    }
	    else {
	        let pin = checkDipolePin(caller, shiftX);
	        let connection = [wiringTarget, [caller.id,pin]];
	        wiringList.push(connection);
            textLabel.innerHTML = "new connection:" + connection;
	        wiring = false;
	        wiringTarget = null;
	        document.body.style.cursor = 'auto';
	        return true;
	    }
		
}