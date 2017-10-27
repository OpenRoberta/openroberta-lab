'use strict';
// Depending on the URL argument, render as LTR or RTL.
var workspace = null;
var reader = new FileReader();
var re = /INSERT INTO PROGRAM VALUES\((\d*),'\w*',\d*,\d*,'(<block_set xmlns="http:\/\/de\.fhg\.iais\.roberta\.blockly\">.*<\/block_set>)'(,.*)\)/;
//var str = 'INSERT INTO PROGRAM VALUES(3208,\'programacion7\',1548,43,\'<block_set xmlns="http://de.fhg.iais.roberta.blockly"><instance x="370" y="50"><block type="robControls_start" id="408" intask="true" deletable="false"><mutation declare="false"></mutation></block><block type="robActions_motorDiff_on" id="409" inline="false" intask="true"><field name="DIRECTION">FOREWARD</field><value name="POWER"><block type="math_number" id="410" intask="true"><field name="NUM">30</field></block></value></block><block type="robControls_loopForever" id="426" intask="true"><statement name="DO"><block type="robControls_if" id="435" inline="false" intask="true"><value name="IF0"><block type="logic_compare" id="561" inline="true" intask="true"><mutation operator_range="NUM"></mutation><field name="OP">LT</field><value name="A"><block type="sim_ultrasonic_getSample" id="566" intask="true"><mutation mode="DISTANCE"></mutation><field name="MODE">DISTANCE</field><field name="SENSORPORT">4</field></block></value><value name="B"><block type="math_number" id="569" intask="true"><field name="NUM">20</field></block></value></block></value><statement name="DO0"><block type="robActions_motorDiff_on_for" id="452" inline="false" intask="true"><field name="DIRECTION">BACKWARDS</field><value name="POWER"><block type="math_number" id="453" intask="true"><field name="NUM">30</field></block></value><value name="DISTANCE"><block type="math_number" id="454" intask="true"><field name="NUM">20</field></block></value></block><block type="robActions_motorDiff_turn_for" id="484" inline="false" intask="true"><field name="DIRECTION">LEFT</field><value name="POWER"><block type="math_number" id="485" intask="true"><field name="NUM">30</field></block></value><value name="DEGREE"><block type="math_number" id="486" intask="true"><field name="NUM">60</field></block></value></block><block type="robActions_motorDiff_on" id="492" inline="false" intask="true"><field name="DIRECTION">FOREWARD</field><value name="POWER"><block type="math_number" id="493" intask="true"><field name="NUM">30</field></block></value></block></statement></block><block type="robControls_wait_time" id="511" inline="true" intask="true"><value name="WAIT"><block type="math_number" id="512" intask="true"><field name="NUM">1</field></block></value></block></statement></block></instance></block_set>\',\'2016-04-05 16:28:06.738000\',\'2016-04-05 16:28:06.738000\',NULL,NULL,0,NULL,0)';
var allPrograms = new Map();

function parseLine(str) {
    var m = null;
    if ((m = re.exec(str)) !== null) {
        if (m.index === re.lastIndex) {
            re.lastIndex++;
        }
    }
    return m;
}

function test() {
    allPrograms.clear();
    readFile();
    reader.onload = function(progressEvent) {
        var lines = this.result.split('\n');
        parseLines(lines);
        loadProgramsInBlockly();
    };
}

function start() {
    var toolbox = document.getElementById('toolbox');
    workspace = Blockly.inject('blocklyDiv', {
        comments : true,
        disable : true,
        collapse : true,
        grid : {
            spacing : 25,
            length : 3,
            colour : '#ccc',
            snap : true
        },
        maxBlocks : Infinity,
        media : '../media/',
        readOnly : false,
        rtl : false,
        scrollbars : true,
        toolbox : toolbox,
        zoom : {
            controls : true,
            wheel : true,
            startScale : 1.0,
            maxScale : 4,
            minScale : .25,
            scaleSpeed : 1.1
        },
        checkInTask : [ 'start', '_def', 'event', 'EV3' ],
        variableDeclaration : true,
        robControls : true
    });
}

function toXml() {
    var output = document.getElementById('importExport');
    var xml = Blockly.Xml.workspaceToDom(workspace);
    output.value = Blockly.Xml.domToPrettyText(xml);
    output.focus();
    output.select();
}

function fromXml() {
    var input = document.getElementById('importExport');
    var xml = Blockly.Xml.textToDom(input.value);
    Blockly.Xml.domToWorkspace(workspace, xml);
}

function xml2blocks(data) {
    var xml = Blockly.Xml.textToDom(data);
    Blockly.Xml.domToWorkspace(workspace, xml);
}

function readFile() {
    var input = document.getElementById('file-input');
    var file = input.files[0];
    reader.readAsText(file);
}

function parseLines(lines) {
    lines.forEach(function(line) {
        var parsed = parseLine(line);
        if (parsed) {
            allPrograms.set(parsed[1], parsed[2]);
        }
    });
}

function loadProgramsInBlockly() {
    allPrograms.forEach(function(v, k) {
        console.log(k + "---");
        xml2blocks(v);
//        sleep(5000);
        workspace.clear();

    });
}
//Basic sleep function based on ms.
//DO NOT USE ON PUBLIC FACING WEBSITES.
function sleep(ms) {
    var unixtime_ms = new Date().getTime();
    while (new Date().getTime() < unixtime_ms + ms) {
    }
}
