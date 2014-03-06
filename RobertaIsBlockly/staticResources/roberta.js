var incrCounter = function(e) {
    var $counter = $('#counter');
    var counter = Number($counter.text());
    $counter.text('' + (counter + 1));
};

var response = function(response) {
    var r = response['result'];
    $('#r').val('' + r);
    LOG.info('ajax response is: ' + r);
    incrCounter();
};

function saveToServer() {
	var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
//	var xml_text = Blockly.Xml.domToPrettyText (xml);
	var xml_text = Blockly.Xml.domToText (xml);
	COMM.xml("/saveTheBlocks", xml_text, response);
}

var init = function() {
    $('#tabs').tabs({
        heightStyle : 'content',
        active : 0
    });
    $('#toggle').onWrap('click', LOG.toggleVisibility, 'toggle LOG visibility');
    $('#saveBlocks').onWrap('click', saveToServer, 'save the blocks');
};

$(document).ready(WRAP.fn3(init, 'page init'));