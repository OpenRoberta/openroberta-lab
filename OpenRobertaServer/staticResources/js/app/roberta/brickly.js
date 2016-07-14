define([ 'exports', 'log', 'util', 'comm', 'message', 'guiState.controller', 'blocks', 'roberta.brick-configuration' ], function(exports, LOG, UTIL, COMM, MSG,
        guiStateController, Blockly, ROBERTA_BRICK_CONFIGURATION) {

    var bricklyWorkspace;

    function init() {
        var view = initView();
        var ready = view.then(function() {
            initEvents();
            //        initProgramForms();
        });
        LOG.info('init configuration view');
        return ready;
    }

    exports.init = init;

    /**
     * Inject Brickly with initial toolbox
     * 
     * @param {response}
     *            toolbox
     */
    function initView() {
        var ready = COMM.json("/toolbox", {
            "cmd" : "loadT",
            "name" : guiStateController.getGuiRobot(),
            "owner" : " "
        }, function(toolbox) {
            UTIL.response(toolbox);
            if (toolbox.rc === 'ok') {
                guiStateController.setConfToolbox(toolbox.data);
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
                bricklyWorkspace.device = guiStateController.getRobot();
            } else {
                MSG.displayInformation(toolbox, "", toolbox.message, "");
            }
        });
        ready.then(function() {
            initConfigurationEnvironment();
        });
        return ready;
    }

    function initEvents() {

        $('#tabConfiguration').onWrap('shown.bs.tab', function(e) {
            guiStateController.setView('tabConfiguration');
            showConfiguration(guiStateController.getConfXML());
            Blockly.hideChaff(true);
            bricklyWorkspace.markFocused();
            bricklyWorkspace.setVisible(true);
            Blockly.svgResize(bricklyWorkspace);
        }, 'tabConfiguration clicked');

        $('#tabConfiguration').on('hide.bs.tab', function(e) {
            var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            guiStateController.setConfXML(xml);
        });

        var moveCounter = 0;
        bricklyWorkspace.addChangeListener(function(event) {
            if (event.type !== 'create' && guiStateController.isProgramSaved() && moveCounter >= 1) {
                moveCounter = 0;
                guiStateController.setProgramSaved(false);
            } else if (event.type !== 'create') {
                moveCounter++;
            }
        });

        // Configurations can't be executed
        bricklyWorkspace.robControls.runOnBrick.setAttribute("style", "display : none");
        bricklyWorkspace.robControls.runInSim.setAttribute("style", "display: none");

        Blockly.bindEvent_(bricklyWorkspace.robControls.saveProgram, 'mousedown', null, function(e) {
            LOG.info('saveProgram from blockly button');
            ROBERTA_BRICK_CONFIGURATION.save();
        });
        bricklyWorkspace.robControls.disable('saveProgram');
    }

    function initConfigurationEnvironment() {
        COMM.json("/conf", {
            "cmd" : "loadC",
            "name" : guiStateController.getGuiRobot(),
            "owner" : " "
        }, function(toolbox) {
            if (toolbox.rc === 'ok' && bricklyWorkspace) {
                guiStateController.setConfXML(toolbox.data);
            } else {
                MSG.displayInformation(configuration, "", configuration.message, "");
            }
        });
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
    function showConfiguration(data) {
        var xml = Blockly.Xml.textToDom(data);
        bricklyWorkspace.clear();
        Blockly.Xml.domToWorkspace(bricklyWorkspace, xml);
        if ($(window).width() < 768) {
            x = $(window).width() / 50;
            y = 25;
        } else {
            x = $(window).width() / 5;
            y = 50;
        }
        var block = bricklyWorkspace.getBlockById(2);
        if (block) {
            var coord = block.getRelativeToSurfaceXY()
            block.moveBy(x - coord.x, y - coord.y);
        }

    }
    exports.showConfiguration = showConfiguration;

    function getBricklyWorkspace() {
        return bricklyWorkspace;
    }

    exports.getBricklyWorkspace = getBricklyWorkspace;

    function reloadView() {
        var toolbox = guiStateController.getConfToolbox();
        var program = Blockly.Xml.workspaceToDom(bricklyWorkspace);
        Blockly.hideChaff();
        bricklyWorkspace.updateToolbox(toolbox);
        bricklyWorkspace.clear();
        Blockly.Xml.domToWorkspace(bricklyWorkspace, program);
    }
    exports.reloadView = reloadView;
});
