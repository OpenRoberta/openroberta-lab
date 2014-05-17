/**
 * Inject Blockly with initial toolbox
 * 
 * @param {response}
 *            toolbox
 */

function injectBlockly(toolbox) {
	response(toolbox);
	if (toolbox.rc === 'ok') {
		Blockly.inject(document.getElementById('blocklyDiv'), {
			path : '/blockly/',
			toolbox : toolbox.data,
			trash : true
		});
		var block = new Blockly.Block();
		block.initialize(Blockly.mainWorkspace, 'robControls_start');
		// block.setMovable(false);
		block.setDeletable(false);
		block.initSvg();
		block.moveBy(25, 20);
		block.render();
		var blockBox = block.svg_.getRootElement().getBBox();
		block.cached_width_ = blockBox.width;
		block.cached_height_ = blockBox.height;
		block.cached_area_ = blockBox.width * blockBox.height;
		var xml = Blockly.Xml.blockToDom_(block);
		Blockly.Xml.Roberta.domToWorkspace(Blockly.mainWorkspace, xml);
	}
}

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
	var xml = Blockly.Xml.Roberta.workspaceToDom(Blockly.mainWorkspace);
	// var xml_text = Blockly.Xml.domToPrettyText (xml);
	var xml_text = Blockly.Xml.domToText(xml);
	var $name = $('#name');
	COMM.json("/blocks", {
		"cmd" : "saveP",
		"name" : $name.val(),
		"program" : xml_text
	}, response);
}

function showProgram(result, load) {
	response(result);
	if (result.rc === 'ok') {
		var xml = Blockly.Xml.Roberta.textToDom(result.data);
		if (load) {
			Blockly.mainWorkspace.clear();
		}
		Blockly.Xml.Roberta.domToWorkspace(Blockly.mainWorkspace, xml);
	}
};

function loadFromServer(load) {
	var $name = $('#name');
	COMM.json("/blocks", {
		"cmd" : "loadP",
		"name" : $name.val()
	}, function(result) {
		showProgram(result, load);
	});
}

var toolboxDone = false;

function showToolbox(result) {
	response(result);
	if (result.rc === 'ok') {
		Blockly.updateToolbox(result.data);
	}
};

function loadToolbox(number) {
	COMM.json("/blocks", {
		"cmd" : "loadT",
		"name" : number
	}, showToolbox);
}

function init() {
	$('#tabs').tabs({
		heightStyle : 'content',
		active : 0
	});
	$('#toggle').onWrap('click', LOG.toggleVisibility, 'toggle LOG visibility');
	$('#save').onWrap('click', saveToServer, 'save the blocks');
	$('#load').onWrap('click', function() {
		loadFromServer(true);
	}, 'load the blocks');
	$('#add').onWrap('click', function() {
		loadFromServer(false);
	}, 'add the blocks');
	$('#toolbox1').onWrap('click', function() {
		loadToolbox('1');
	}, 'load toolbox 1');
	$('#toolbox2').onWrap('click', function() {
		loadToolbox('2');
	}, 'load toolbox 2');
	COMM.json("/blocks", {
		"cmd" : "loadT",
		"name" : "1"
	}, injectBlockly);
};

$(document).ready(WRAP.fn3(init, 'page init'));