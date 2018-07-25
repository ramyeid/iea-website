submitbutton.addEventListener("click", function(){
	
    let connectionsString = wiringList.toString();
    let receiversString = receiverList.toString();
    let generatorsString = generatorList.toString();

    $.ajax({
         type: 'POST',
         url: "/canvas/update",
         data: { generators: generatorsString, receivers: receiversString, connections: connectionsString},
         success: function(data)
         {
			let receiverStatus = parseStatusString(data);
            updateAllComponents(receiverStatus); //implement updating logic here
         }
    });

    textLabel.innerHTML = 'Generators: ' + generatorsString + ' ||| Receivers: ' + receiversString + ' ||| Connections: ' + connectionsString;
	
});

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
	
	component.src = component.src.substring(0, component.src.length-5) + componentStatus + ".png";	
	
}
