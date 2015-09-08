function init() {
    COMM.json("/toolbox", {
        "cmd" : "loadT",
        "name" : "ev3Brick",
        "owner" : " "
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
        Blockly.inject(document.getElementById('bricklyDiv'), {
            path : '/blockly/',
            toolbox : toolbox.data,
            trashcan : true,
            save : true,
            scrollbars : true
        // check : true,
        });
        COMM.json("/conf", {
            "cmd" : "loadC",
            "name" : "ev3Brick",
            "owner" : " "
        }, initConfigurationEnvironment);
    }
};

function initConfigurationEnvironment(result) {
    // TODO solve this when blockly can have more instances
    if (!result) {
        COMM.json("/conf", {
            "cmd" : "loadC",
            "name" : "ev3Brick",
            "owner" : " "
        }, initConfigurationEnvironment);
    }
    response(result);
    Blockly.getMainWorkspace().clear();
    var xml = Blockly.Xml.textToDom(result.data);
    Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
};

function response(result) {
    LOG.info('result from server: ' + JSON.stringify(result));
    return true;
};

/**
 * Save configuration to server
 * 
 * @param {name}
 *            configuration name
 * 
 */
function getXmlOfConfiguration(name) {
    var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
    var xml_text = Blockly.Xml.domToText(xml);
    return xml_text;
};

/**
 * Show configuration
 * 
 * @param {load}
 *            load configuration
 * @param {data}
 *            data of server call
 */
function showConfiguration(data, load) {
    var xml = Blockly.Xml.textToDom(data);
    if (load) {
        Blockly.mainWorkspace.clear();
    }
    Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
};

/**
 * Show toolbox
 * 
 * @param {result}
 *            result of server call
 */
function showToolbox(result) {
    response(result);
    if (result.rc === 'ok') {
        Blockly.updateToolbox(result.data);
    }
};

function checkProgram() {
    // TODO do we need this here?
};

/**
 * Switch to another language
 * 
 * @param {String}
 *            url of new message file
 */
function switchLanguage(url) {
    var future = $.getScript(url);
    future.then(switchLanguageInBrickly);
};

/**
 * Switch brickly to another language
 */
function switchLanguageInBrickly() {
    var configurationBlocks = null;
    if (Blockly.mainWorkspace !== null) {
        var xmlConfiguration = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
        configurationBlocks = Blockly.Xml.domToText(xmlConfiguration);
    }
// translate configuration tab, inject is not possible!
    COMM.json("/toolbox", {
        "cmd" : "loadT",
        "name" : "ev3Brick",
        "owner" : " "
    }, showToolbox);
    initConfigurationEnvironment({
        "data" : configurationBlocks
    });
};

function saveToServer() {
    parent.saveConfigurationToServer();
};

/**
 * Set modification state.
 * 
 * @param {Boolean}
 *            modified or not.
 */
function setWorkspaceModified(modified) {
    parent.userState.configurationModified = modified;
}

$(document).ready(WRAP.fn3(init, 'brickly init EV3'));
