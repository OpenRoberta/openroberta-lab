$(document).ready(WRAP.fn3(init, 'brickly init EV3'));

function init() {
    COMM.json("/blocks", {
        "cmd" : "loadT",
        "name" : "brickEV3"
    }, injectBrickly);
};

/**
 * Inject Brickly with initial toolbox
 * 
 * @param {response}
 *            toolbox
 */

function injectBrickly(toolbox) {
    response(toolbox);
    if (toolbox.rc === 'ok') {
        Blockly.inject(document.body, {
            path : '/blockly/',
            toolbox : toolbox.data,
            trashcan : true,
            save : true,
            back : true,
            check : true,
            scrollbars : true
        });

        // should this come from the server?
        var text = "<brick_set xmlns='http: // www.w3.org/1999/xhtml'>" + "<instance x='75' y='75'>" + "<block type='robBrick_EV3-Brick'>"
                + "<value name='S1'>" + "<block type='robBrick_touch'></block>" + "</value>" + "<value name='S4'>"
                + "<block type='robBrick_ultrasonic'></block>" + "</value>" + "<value name='MB'>" + "<block type='robBrick_motor_big'>"
                + "<field name='MOTOR_REGULATION'>TRUE</field>" + "<field name='MOTOR_REVERSE'>OFF</field>" + "<field name='MOTOR_DRIVE'>RIGHT</field>"
                + "</block>" + "</value>" + "<value name='MC'>" + "<block type='robBrick_motor_big'>" + "<field name='MOTOR_REGULATION'>TRUE</field>"
                + "<field name='MOTOR_REVERSE'>OFF</field>" + "<field name='MOTOR_DRIVE'>LEFT</field>" + "</block>" + "</value>" + "</block>" + "</instance>"
                + "</brick_set>";
        var xml = Blockly.Xml.textToDom(text);
        Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
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
    var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace, 'brick_set');
    var xml_text = Blockly.Xml.domToText(xml);
    alert("to save: \n" + xml_text);
    LOG.info('save brick configuration');
    back();
    // TODO
    // var $name = $('#programName');
    // COMM.json("/conf", {
    // "cmd" : "saveP",
    // "name" : $name.val(),
    // "program" : xml_text
    // }, response);
}

function loadToolbox(toolbox) {
    COMM.json("/blocks", {
        "cmd" : "loadT",
        "name" : toolbox
    }, showToolbox);
}

function checkProgram() {
    // TODO do we need this here?
    alert("Your configuration will be checked soon ;-)");
}

function back() {
    parent.switchToBlockly();
}
