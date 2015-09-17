function init() {
    COMM.json("/toolbox", {
        "cmd" : "loadT",
        "name" : "ev3Brick",
        "owner" : " "
    }, injectBrickly);
}

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
        initConfigurationEnvironment();
    } else {
        window.parent.displayInformation(toolbox, "", toolbox.message, "");
    }
}

function initConfigurationEnvironment(opt_configuration) {
    // TODO solve this when blockly can have more instances
    if (!opt_configuration) {
        COMM.json("/conf", {
            "cmd" : "loadC",
            "name" : "ev3Brick",
            "owner" : " "
        }, initConfigurationEnvironment);
    } else {
        if (opt_configuration.rc === 'ok') {
            var workspace = Blockly.getMainWorkspace();
            if (workspace) {
                workspace.clear();
                var xml = Blockly.Xml.textToDom(opt_configuration.data);
                Blockly.Xml.domToWorkspace(workspace, xml);
            }
            window.parent.userState.bricklyReady = true;
        } else {
            window.parent.displayInformation(configuration, "", configuration.message, "");
        }
    }
}

function response(result) {
    window.parent.LOG.info('result from server: ' + window.parent.UTIL.formatResultLog(result));
    if (result.rc != 'ok') {
        window.parent.displayMessage(result.message, "POPUP", "");
    }
}

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
}

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
}

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
}

function checkProgram() {
    // TODO do we need this here?
}

/**
 * Switch brickly to another language
 */
function switchLanguageInBrickly() {
    var configurationBlocks = null;
    if (Blockly.getMainWorkspace() !== null) {
        var xmlConfiguration = Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace());
        configurationBlocks = Blockly.Xml.domToText(xmlConfiguration);
        COMM.json("/toolbox", {
            "cmd" : "loadT",
            "name" : "ev3Brick",
            "owner" : " "
        }, function(toolbox) {
            showToolbox(toolbox);
            initConfigurationEnvironment({
                "rc" : "ok",
                "data" : configurationBlocks
            });
            window.parent.userState.bricklyTranslated = true;
        });
    }
}

function saveToServer() {
    window.parent.saveConfigurationToServer();
}

/**
 * Set modification state.
 * 
 * @param {Boolean}
 *            modified or not.
 */
function setWorkspaceModified(modified) {
    window.parent.userState.configurationModified = modified;
}

$(document).ready(WRAP.fn3(init, 'brickly init EV3'));
