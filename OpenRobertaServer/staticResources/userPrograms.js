
function myLoadFromListing() {
	
	if(userId == "none"){

		
	}else{
		

		
		COMM.json("/blocks", {
			"cmd" : "obtainUserPrograms",
			"accountName" : userAccountName,
			"userId":userId,
		}, showPrograms);
	}
	
}

function saveUPToServer() {
	var xml = Blockly.Xml.Roberta.workspaceToDom(Blockly.mainWorkspace);
	// var xml_text = Blockly.Xml.domToPrettyText (xml);
	var xml_text = Blockly.Xml.domToText(xml);
	var $name = $('#name');
	COMM.json("/blocks", {
		"cmd" : "saveUserP",
		"name" : $name.val(),
		"program" : xml_text
	}, response);
}


function loadUPFromServer(load) {
	var $name = $('#name');
	COMM.json("/blocks", {
		"cmd" : "loadUserP",
		"name" : $name.val()
	}, function(result) {
		showProgram(result, load);
	});
}

function deleteUPOnServer() {
	
	var $name = $('#name');
	COMM.json("/blocks", {
		"cmd" : "deleteUserPN",
		"name" : $name.val()
	}, function(result) {
	});
	
}