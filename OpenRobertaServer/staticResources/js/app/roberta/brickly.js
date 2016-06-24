define([ 'exports', 'log', 'util', 'comm', 'message', 'roberta.user-state', 'blocks', 'roberta.brick-configuration' ], function(exports, LOG, UTIL, COMM, MSG,
        userState, Blockly, ROBERTA_BRICK_CONFIGURATION) {

    var bricklyWorkspace;

    function init() {
        COMM.json("/toolbox", {
            "cmd" : "loadT",
            "name" : userState.robot,
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
                scrollbars : true,
                zoom : {
                    controls : true,
                    wheel : true,
                    startScale : 1.0,
                    maxScale : 4,
                    minScale : .25,
                    scaleSpeed : 1.1
                },
                checkInTask : [ '-Brick' ],
                variableDeclaration : true,
                robControls : true
            });
            bricklyWorkspace.addChangeListener(function(event) {
                if (userState.configurationSaved == 'new') {
                    userState.configurationSaved = true;
                } else {
                    if (userState.id !== -1 && userState.configuration !== 'EV3basis') {
                        bricklyWorkspace.robControls.enable('saveProgram');
                        $('#menuSaveConfig').parent().removeClass('disabled');
                    }
                    userState.configurationSaved = false;
                }
            });
            bricklyWorkspace.device = userState.robot;
            // Configurations can't be executed
            bricklyWorkspace.robControls.runOnBrick.setAttribute("style", "display : none");
            bricklyWorkspace.robControls.runInSim.setAttribute("style", "display: none");

            Blockly.bindEvent_(bricklyWorkspace.robControls.saveProgram, 'mousedown', null, function(e) {
                LOG.info('saveProgram from blockly button');
                ROBERTA_BRICK_CONFIGURATION.save();
            });
            bricklyWorkspace.robControls.disable('saveProgram');
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
                "name" : userState.robot,
                "owner" : " "
            }, initConfigurationEnvironment);
        } else {
            if (opt_configuration.rc === 'ok') {
                if (bricklyWorkspace) {
                    bricklyWorkspace.clear();
                    if ($(window).width() < 768) {
                        x = $(window).width() / 50;
                        y = 25;
                    } else {
                        x = $(window).width() / 5;
                        y = 50;
                    }
                    var xml = Blockly.Xml.textToDom(opt_configuration.data);
                    Blockly.Xml.domToWorkspace(bricklyWorkspace, xml);
                    var block = bricklyWorkspace.getBlockById(2);
                    if (block) {
                        var coord = block.getRelativeToSurfaceXY()
                        block.moveBy(x-coord.x, y-coord.y);
                    }
                    $('#menuSaveConfig').parent().addClass('disabled');
                    bricklyWorkspace.robControls.disable('saveProgram');
                    userState.configurationSaved = 'new';
                }
                userState.bricklyReady = true;
                Blockly.svgResize(bricklyWorkspace);
            } else {
                MSG.displayInformation(configuration, "", configuration.message, "");
            }
        }
    }
    exports.initConfigurationEnvironment = initConfigurationEnvironment;

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
            if (bricklyWorkspace !== null && bricklyWorkspace !== undefined) {
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
            "name" : userState.robot,
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
            "name" : userState.robot,
            "owner" : " "
        }, function(toolbox) {
            showToolbox(toolbox);
        });
    }

    exports.loadToolbox = loadToolbox;

    function getBricklyWorkspace() {
        return bricklyWorkspace;
    }

    exports.getBricklyWorkspace = getBricklyWorkspace;
});
