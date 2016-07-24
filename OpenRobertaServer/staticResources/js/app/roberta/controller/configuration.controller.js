define([ 'exports', 'log', 'util', 'comm', 'message', 'guiState.controller', 'blocks', 'roberta.brick-configuration' ], function(exports, LOG, UTIL, COMM, MSG,
        guiStateController, Blockly, ROBERTA_BRICK_CONFIGURATION) {

    var bricklyWorkspace;
    var seen = false;

    function init() {
        initView();
        initEvents();
        LOG.info('init configuration view');
    }

    exports.init = init;

    /**
     * Inject Brickly with initial toolbox
     * 
     * @param {response}
     *            toolbox
     */
    function initView() {
        var toolbox = guiStateController.getConfigurationToolbox();
        bricklyWorkspace = Blockly.inject(document.getElementById('bricklyDiv'), {
            path : '/blockly/',
            toolbox : toolbox,
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
        guiStateController.setBricklyWorkspace(bricklyWorkspace);
        initConfigurationEnvironment();
    }

    function initEvents() {

        $('#tabConfiguration').onWrap('shown.bs.tab', function(e) {
            guiStateController.setView('tabConfiguration');
            bricklyWorkspace.markFocused();
            reloadConf();
        }, 'tabConfiguration clicked');

        $('#tabConfiguration').on('hide.bs.tab', function(e) {
            var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            guiStateController.setConfigurationXML(xml);
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
        bricklyWorkspace.clear();
        Blockly.hideChaff();
        Blockly.svgResize(bricklyWorkspace);
        var conf = guiStateController.getConfigurationConf();
        var xml = Blockly.Xml.textToDom(conf);
        Blockly.Xml.domToWorkspace(bricklyWorkspace, xml);
        guiStateController.setConfigurationXML(conf);
        if (isVisible()) {
            if ($(window).width() < 768) {
                x = $(window).width() / 50;
                y = 25;
            } else {
                x = $(window).width() / 5;
                y = 50;
            }
            var blocks = bricklyWorkspace.getTopBlocks(true);
            if (blocks[0]) {
                var coord = blocks[0].getRelativeToSurfaceXY();
                blocks[0].moveBy(x - coord.x, y - coord.y);
            }
            seen = true;
        } else {
            seen = false
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
    function showConfiguration(data) {
        var xml = Blockly.Xml.textToDom(data);
        bricklyWorkspace.clear();
        Blockly.Xml.domToWorkspace(bricklyWorkspace, xml);

    }
    exports.showConfiguration = showConfiguration;

    function getBricklyWorkspace() {
        return bricklyWorkspace;
    }

    exports.getBricklyWorkspace = getBricklyWorkspace;

    function reloadConf() {
        bricklyWorkspace.clear();
        Blockly.hideChaff();
        Blockly.svgResize(bricklyWorkspace);
        var conf = guiStateController.getConfigurationXML();
        var dom = Blockly.Xml.textToDom(conf);
        Blockly.Xml.domToWorkspace(bricklyWorkspace, dom);
        if (!seen) {
            if ($(window).width() < 768) {
                x = $(window).width() / 50;
                y = 25;
            } else {
                x = $(window).width() / 5;
                y = 50;
            }
            var blocks = bricklyWorkspace.getTopBlocks(true);
            if (blocks[0]) {
                var coord = blocks[0].getRelativeToSurfaceXY();
                blocks[0].moveBy(x - coord.x, y - coord.y);
            }
            seen = true;
        }
    }

    function reloadView() {
        if (isVisible()) {
            var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            bricklyWorkspace.clear();
            dom = Blockly.Xml.textToDom(xml);
            Blockly.Xml.domToWorkspace(bricklyWorkspace, dom);
        }
        var toolbox = guiStateController.getConfigurationToolbox();
        bricklyWorkspace.updateToolbox(toolbox);
    }
    exports.reloadView = reloadView;

    function resetView() {
        bricklyWorkspace.device = guiStateController.getRobot();
        initConfigurationEnvironment();
        var toolbox = guiStateController.getConfigurationToolbox();
        bricklyWorkspace.updateToolbox(toolbox);
    }
    exports.resetView = resetView;

    function isVisible() {
        return guiStateController.getView() == 'tabConfiguration';
    }
});
