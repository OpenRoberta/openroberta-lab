define([ 'require', 'exports', 'log', 'util', 'comm', 'message', 'roberta.user-state', 'blocks' ], function(require, exports, LOG, UTIL, COMM, MSG, userState,
        Blockly) {

    var bricklyWorkspace;

    function init() {
        COMM.json("/toolbox", {
            "cmd" : "loadT",
            "name" : "ev3Brick",
            "owner" : " "
        }, injectBrickly);
    }

    exports.init = init;

    /**
     * Inject Brickly with initial toolbox
     * 
     * @param {response}
     *            toolbox
     */
    function injectBrickly(toolbox) {
        UTIL.response(toolbox);
        if (toolbox.rc === 'ok') {
            bricklyWorkspace = Blockly.inject(document.getElementById('bricklyDiv'), {
                path : '/blockly/',
                toolbox : toolbox.data,
                trashcan : true,
                save : true,
                scrollbars : true,
                zoom : {
                    controls : true,
                    wheel : true,
                    startScale : 1.0,
                    maxScale : 4,
                    minScale : .25,
                    scaleSpeed : 1.1
                },
                checkInTask : [ 'EV3' ],
                variableDeclaration : true
            });
            initConfigurationEnvironment();
        } else {
            MSG.displayInformation(toolbox, "", toolbox.message, "");
        }
    }

    function initConfigurationEnvironment(opt_configuration) {
        // TODO solve this when blockly can have more instances
        if (!opt_configuration || !opt_configuration.data) {
            COMM.json("/conf", {
                "cmd" : "loadC",
                "name" : "ev3Brick",
                "owner" : " "
            }, initConfigurationEnvironment);
        } else {
            if (opt_configuration.rc === 'ok') {
                if (bricklyWorkspace) {
                    bricklyWorkspace.clear();
                    var xml = Blockly.Xml.textToDom(opt_configuration.data);
                    Blockly.Xml.domToWorkspace(bricklyWorkspace, xml);
                }
                userState.bricklyReady = true;
            } else {
                MSG.displayInformation(configuration, "", configuration.message, "");
            }
        }
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
            bricklyWorkspace.clear();
        }
        Blockly.Xml.domToWorkspace(bricklyWorkspace, xml);
    }

    exports.showConfiguration = showConfiguration;
    /**
     * Show toolbox
     * 
     * @param {result}
     *            result of server call
     */
    function showToolbox(result) {
        UTIL.response(result);
        if (result.rc === 'ok') {
            bricklyWorkspace.updateToolbox(result.data);
        }
    }

    function checkProgram() {
        // TODO do we need this here?
    }

    /**
     * Switch brickly to another language
     */
    function switchLanguageInBrickly() {
        if (userState.robot !== 'oraSim') {
            var configurationBlocks = null;
            if (bricklyWorkspace !== null) {
                var xmlConfiguration = Blockly.Xml.workspaceToDom(bricklyWorkspace);
                if (xmlConfiguration.childNodes.length != 0) {
                    configurationBlocks = Blockly.Xml.domToText(xmlConfiguration);
                }

                exports.loadToolboxAndConfiguration({
                    "rc" : "ok",
                    "data" : configurationBlocks
                });
                userState.bricklyTranslated = true;
            }
        }
    }

    exports.switchLanguageInBrickly = switchLanguageInBrickly;

    function loadToolboxAndConfiguration(opt_configurationBlocks) {
        COMM.json("/toolbox", {
            "cmd" : "loadT",
            "name" : "ev3Brick", // TODO do not use a hardcoded name!
            "owner" : " "
        }, function(toolbox) {
            showToolbox(toolbox);
            initConfigurationEnvironment(opt_configurationBlocks);
        });
    }

    exports.loadToolboxAndConfiguration = loadToolboxAndConfiguration;

    function loadToolbox() {
        COMM.json("/toolbox", {
            "cmd" : "loadT",
            "name" : "ev3Brick", // TODO do not use a hardcoded name!
            "owner" : " "
        }, function(toolbox) {
            showToolbox(toolbox);
        });
    }

    exports.loadToolbox = loadToolbox;

    function saveToServer() {
        saveConfigurationToServer();
    }

    /**
     * Set modification state.
     * 
     * @param {Boolean}
     *            modified or not.
     */
    function setWorkspaceModified(modified) {
        userState.configurationModified = modified;
    }

    function getBricklyWorkspace() {
        return bricklyWorkspace;
    }

    exports.getBricklyWorkspace = getBricklyWorkspace;
});
