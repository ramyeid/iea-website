/* JavaScript responsible for communicating with the backend and updating the component status
 * It is called upon clicking the submit button and then sends components and wiring information to server
 * the server then replies with a status string with the format: component1Id:Status,component2Id:Status, ...
 * It makes the assumption that all files are present as .png in the corresponding images folder
 * Each component's folder must contain 4 images related to its status ending in "0.png", "1.png", "2.png", "3.png"
 * 0 is OFF
 * 1 is LOW
 * 2 is OPTIMAL
 * 3 is DAMAGED
 */

submitbutton.addEventListener('click',submitCircuit);

function submitCircuit() {
    let connectionsAsString = wiringList.toString();
    let receiversAsString = receiverList.toString();
    let generatorsAsString = generatorList.toString();

    var notificationSource = new EventSource("/canvas/submit?generators="+generatorsAsString+"&receivers="+receiversAsString+"&connections="+connectionsAsString+"&fileName="+fileName);

    notificationSource.onmessage = function(event) {
        if (event.data.substring(0,5) === "Error") {
            alert(event.data);
        }
        else if (event.data.substring(0,3) === "end") {
            notificationSource.close();
            notificationSource = null;
        }
        else {
            updateCircuit(event.data);
        }
    };

    notificationSource.onerror = function(event) {
        alert("Timeout");
    };

    textLabel.innerHTML = 'Generators: ' + generatorsAsString + ' ||| Receivers: ' + receiversAsString + ' ||| Connections: ' + connectionsAsString;
}

function updateCircuit(statusString) {
    if (statusString !== ""){
        let receiverStatus = parseStatusString(statusString);
        updateAllComponents(receiverStatus);
    }
}

function parseStatusString(statusString){
	let statusMap = statusString.split(",");
	let componentStatusMap = [];

	for (let i = 0; i < statusMap.length; i++){
		componentStatusMap [i] = statusMap[i].split(":");
	}

	return componentStatusMap;
}

function updateAllComponents(componentStatusMap){

	for (let i = 0; i < componentStatusMap.length; i++){
		updateComponentStatus(document.getElementById(componentStatusMap[i][0]), componentStatusMap[i][1]);
	}
}

function updateComponentStatus(component, componentStatus){

    let newSrc = component.src.substring(0, component.src.length-5) + componentStatus + ".png";
    if (component.src !== newSrc)
    	component.src = newSrc;
}
