function incrCounter(e) {
    var $counter = $('#counter');
    var counter = Number($counter.text());
    $counter.text('' + (counter + 1));
}

function response(result) {
    LOG.info('result from server: ' + JSON.stringify(result));
    incrCounter();
};

function saveToServer() {
	var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
//	var xml_text = Blockly.Xml.domToPrettyText (xml);
	var xml_text = Blockly.Xml.domToText (xml);
	var $name = $('#name');
	COMM.json("/blocks", {"cmd" : "saveP", "name" : $name.val(), "program" : xml_text}, response);
}

function showProgram(result, load) {
	response(result);
	if (result.rc === 'ok') {
		var xml = Blockly.Xml.textToDom(result.data);
		if (load) {
			Blockly.mainWorkspace.clear();
		}
		Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);		
	}
};

function loadFromServer(load) {
	var $name = $('#name');
	COMM.json("/blocks", {"cmd" : "loadP", "name" : $name.val()}, function(result) {showProgram(result,load);});
}

var toolboxDone = false;

function showToolbox(result) {
	response(result);
	if (result.rc === 'ok') {
		Blockly.inject(document.getElementById('blocklyDiv'),{path: '/blockly', toolbox: result.data});
		$('#toolbox1').attr('disabled', 'disabled');
		$('#toolbox2').attr('disabled', 'disabled');
	}
};

function loadToolbox(number) {
	COMM.json("/blocks", {"cmd" : "loadT", "name" : number}, showToolbox);
}

function init() {
    $('#tabs').tabs({
        heightStyle : 'content',
        active : 0
    });
    $('#toggle').onWrap('click', LOG.toggleVisibility, 'toggle LOG visibility');
    $('#save').onWrap('click', saveToServer, 'save the blocks');
    $('#load').onWrap('click', function() {loadFromServer(true);}, 'load the blocks');
    $('#add').onWrap('click', function() {loadFromServer(false);}, 'add the blocks');
    $('#toolbox1').onWrap('click', function() {loadToolbox('1');}, 'load toolbox 1');
    $('#toolbox2').onWrap('click', function() {loadToolbox('2');}, 'load toolbox 2');
};

$(document).ready(WRAP.fn3(init, 'page init'));