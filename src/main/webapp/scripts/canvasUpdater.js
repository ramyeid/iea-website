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

var notificationSource = new EventSource("/canvas/notifications");
notificationSource.onmessage = function(event) {
    updateCircuit(event.data);
};

notificationSource.onerror = function(event) {
    alert("Timeout");
};

function submitCircuit(){
    let connectionsString = wiringList.toString();
    let receiversString = receiverList.toString();
    let generatorsString = generatorList.toString();

    $.ajax({
         type: 'POST',
         url: "/canvas/submit",
         data: { generators: generatorsString, receivers: receiversString, connections: connectionsString}
    });

    textLabel.innerHTML = 'Generators: ' + generatorsString + ' ||| Receivers: ' + receiversString + ' ||| Connections: ' + connectionsString;
}

function updateCircuit(statusString)
{
    if (statusString.substring(0,5) === "ERROR") {
        //alert(data);
    }
    else if (statusString !== ""){
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
