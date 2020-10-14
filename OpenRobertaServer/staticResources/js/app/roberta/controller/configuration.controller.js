define(['exports', 'log', 'util', 'comm', 'message', 'guiState.controller', 'blockly', 'configuration.model', 'confVisualization', 'jquery', 'jquery-validate'], function(exports,
    LOG, UTIL, COMM, MSG, GUISTATE_C, Blockly, CONFIGURATION, CV, $) {

    var $formSingleModal;

    var bricklyWorkspace;
    var confVis;
    var listenToBricklyEvents = true;
    var seen = false;

    function init() {
        initView();
        initEvents();
        initConfigurationForms();
        initConfigurationEnvironment();
    }

    exports.init = init;

    /**
     * Inject Brickly with initial toolbox
     * 
     * @param {response}
     *            toolbox
     */
    function initView() {
        var toolbox = GUISTATE_C.getConfigurationToolbox();
        bricklyWorkspace = Blockly.inject(document.getElementById('bricklyDiv'), {
            path: '/blockly/',
            toolbox: toolbox,
            trashcan: true,
            scrollbars: true,
            media: '../blockly/media/',
            zoom: {
                controls: true,
                wheel: false,
                startScale: 1.0,
                maxScale: 4,
                minScale: .25,
                scaleSpeed: 1.1
            },
            checkInTask: ['-Brick', 'robConf'],
            variableDeclaration: true,
            robControls: true,
            theme: GUISTATE_C.getTheme()
        });
        bricklyWorkspace.setDevice({
            group: GUISTATE_C.getRobotGroup(),
            robot: GUISTATE_C.getRobot()
        });
        // Configurations can't be executed
        bricklyWorkspace.robControls.runOnBrick.setAttribute("style", "display : none");
        GUISTATE_C.setBricklyWorkspace(bricklyWorkspace);
        bricklyWorkspace.robControls.disable('saveProgram');
    }

    function initEvents() {
        $('#tabConfiguration').on('show.bs.tab', function(e) {
            GUISTATE_C.setView('tabConfiguration');
        });

        $('#tabConfiguration').onWrap('shown.bs.tab', function(e) {
            bricklyWorkspace.markFocused();
            if (GUISTATE_C.isConfigurationUsed()) {
                bricklyWorkspace.setVisible(true);
            } else {
                bricklyWorkspace.setVisible(false);
            }
            $(window).resize();
            UTIL.clearAnnotations(bricklyWorkspace);
            if (GUISTATE_C.confAnnos !== undefined) {
                UTIL.annotateBlocks(bricklyWorkspace, GUISTATE_C.confAnnos);
                delete GUISTATE_C.confAnnos;
            }
            confVis.refresh();
        }, 'tabConfiguration clicked');

        $('#tabConfiguration').on('hide.bs.tab', function(e) {
            Blockly.hideChaff(false);
        });;

        $('#tabConfiguration').on('hidden.bs.tab', function(e) {
            var dom = (confVis) ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            GUISTATE_C.setConfigurationXML(xml);
            bricklyWorkspace.setVisible(false);
        });

        Blockly.bindEvent_(bricklyWorkspace.robControls.saveProgram, 'mousedown', null, function(e) {
            LOG.info('saveConfiguration from brickly button');
            saveToServer();
        });

        bricklyWorkspace.addChangeListener(function(event) {
            if (listenToBricklyEvents && event.type != Blockly.Events.UI && GUISTATE_C.isConfigurationSaved()) {
                if (GUISTATE_C.isConfigurationStandard()) {
                    GUISTATE_C.setConfigurationName('');
                }
                GUISTATE_C.setConfigurationSaved(false);
                GUISTATE_C.setProgramSaved(false);
            }
            if (event.type === Blockly.Events.DELETE) {
                if (bricklyWorkspace.getAllBlocks().length === 0) {
                    newConfiguration(true);
                }
            }

        });
    }

    function initConfigurationForms() {
        $formSingleModal = $('#single-modal-form');
    }
    exports.initConfigurationForms = initConfigurationForms;

    /**
     * Save configuration to server
     */
    function saveToServer() {
        $('.modal').modal('hide'); // close all opened popups
        if (GUISTATE_C.isConfigurationStandard() || GUISTATE_C.isConfigurationAnonymous()) {
            LOG.error('saveToServer may only be called with an explicit config name');
            return;
        }
        var dom = (confVis) ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
        var xmlText = Blockly.Xml.domToText(dom);
        CONFIGURATION.saveConfigurationToServer(GUISTATE_C.getConfigurationName(), xmlText, function(result) {
            if (result.rc === 'ok') {
                GUISTATE_C.setConfigurationSaved(true);
                LOG.info('save brick configuration ' + GUISTATE_C.getConfigurationName());
            }
            MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION", result.message, GUISTATE_C.getConfigurationName());
        });
    }
    exports.saveToServer = saveToServer;

    /**
     * Save configuration with new name to server
     */
    function saveAsToServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            $('.modal').modal('hide'); // close all opened popups
            var confName = $('#singleModalInput').val().trim();
            if (GUISTATE_C.getConfigurationStandardName() === confName) {
                LOG.error('saveAsToServer may NOT use the config standard name');
                return;
            }
            var dom = (confVis) ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xmlText = Blockly.Xml.domToText(dom);
            CONFIGURATION.saveAsConfigurationToServer(confName, xmlText, function(result) {
                if (result.rc === 'ok') {
                    result.name = confName;
                    GUISTATE_C.setConfiguration(result);
                    GUISTATE_C.setProgramSaved(false);
                    LOG.info('save brick configuration ' + GUISTATE_C.getConfigurationName());
                }
                MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION_AS", result.message, GUISTATE_C.getConfigurationName());
            });
        }
    }
    exports.saveAsToServer = saveAsToServer;

    /**
     * Load the configuration that was selected in configurations list
     */
    function loadFromListing(conf) {
        LOG.info('loadFromList ' + conf[0]);
        CONFIGURATION.loadConfigurationFromListing(conf[0], conf[1], function(result) {
            if (result.rc === 'ok') {
                result.name = conf[0];
                $('#tabConfiguration').one('shown.bs.tab', function() {
                    showConfiguration(result);
                });
                $('#tabConfiguration').trigger('click');
            }
            MSG.displayInformation(result, "", result.message);
        });
    }
    exports.loadFromListing = loadFromListing;

    function initConfigurationEnvironment() {
        var conf = GUISTATE_C.getConfigurationConf();
        configurationToBricklyWorkspace(conf);
        if (isVisible()) {
            if ($(window).width() < 768) {
                x = $(window).width() / 50;
                y = 25;
            } else {
                x = $(window).width() / 5;
                y = 50;
            }
            var blocks = bricklyWorkspace.getTopBlocks(true);
            for (var i = 0; i < blocks.length; i++) {
                var coord = Blockly.getSvgXY_(blocks[i].svgGroup_, bricklyWorkspace);
                var coordBlock = blocks[i].getRelativeToSurfaceXY();
                blocks[i].moveBy(coordBlock.x - coord.x + x, coordBlock.y - coord.y + y);
            }
            seen = true;
        } else {
            seen = false;
            bricklyWorkspace.setVisible(false);
        }
        var dom = (confVis) ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
        var xml = Blockly.Xml.domToText(dom);
        GUISTATE_C.setConfigurationXML(xml);
    }
    exports.initConfigurationEnvironment = initConfigurationEnvironment;

    function showSaveAsModal() {
        var regexString = new RegExp('^(?!\\b' + GUISTATE_C.getConfigurationStandardName() + '\\b)([a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]*)$');
        $.validator.addMethod("regex", function(value, element, regexp) {
            value = value.trim();
            return value.match(regexp);
        }, "No special Characters allowed here. Use only upper and lowercase letters (A through Z; a through z) and numbers.");

        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg["MENU_SAVE_AS"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_NAME"]);
        }, saveAsToServer, function() {

        }, {
                rules: {
                    singleModalInput: {
                        required: true,
                        regex: regexString
                    }
                },
                errorClass: "form-invalid",
                errorPlacement: function(label, element) {
                    label.insertAfter(element);
                },
                messages: {
                    singleModalInput: {
                        required: jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"]),
                        regex: jQuery.validator.format(Blockly.Msg["MESSAGE_INVALID_CONF_NAME"])
                    }
                }
            });
    }
    exports.showSaveAsModal = showSaveAsModal;

    /**
     * New configuration
     */
    function newConfiguration(opt_further) {
        var further = opt_further || false;
        if (further || GUISTATE_C.isConfigurationSaved()) {
            var result = {};
            result.name = GUISTATE_C.getRobotGroup().toUpperCase() + "basis";
            result.lastChanged = '';
            GUISTATE_C.setConfiguration(result);
            initConfigurationEnvironment();
        } else {
            $('#show-message-confirm').one('shown.bs.modal', function(e) {
                $('#confirm').off();
                $('#confirm').on('click', function(e) {
                    e.preventDefault();
                    newConfiguration(true);
                });
                $('#confirmCancel').off();
                $('#confirmCancel').on('click', function(e) {
                    e.preventDefault();
                    $('.modal').modal('hide');
                });
            });
            if (GUISTATE_C.isUserLoggedIn()) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
            }
        }
    }
    exports.newConfiguration = newConfiguration;

    /**
     * Show configuration
     * 
     * @param {load}
     *            load configuration
     * @param {data}
     *            data of server call
     */
    function showConfiguration(result) {
        if (result.rc == 'ok') {
            configurationToBricklyWorkspace(result.confXML);
            GUISTATE_C.setConfiguration(result);
            LOG.info('show configuration ' + GUISTATE_C.getConfigurationName());
        }
    }
    exports.showConfiguration = showConfiguration;

    function getBricklyWorkspace() {
        return bricklyWorkspace;
    }
    exports.getBricklyWorkspace = getBricklyWorkspace;

    function reloadConf(opt_result) {
        var conf;
        if (opt_result) {
            conf = opt_result.confXML;
        } else {
            conf = GUISTATE_C.getConfigurationXML();
        }
        if (!seen) {
            configurationToBricklyWorkspace(conf);
            var x, y;
            if ($(window).width() < 768) {
                x = $(window).width() / 50;
                y = 25;
            } else {
                x = $(window).width() / 5;
                y = 50;
            }
            var blocks = bricklyWorkspace.getTopBlocks(true);
            for (var i = 0; i < blocks.length; i++) {
                var coord = Blockly.getSvgXY_(blocks[i].svgGroup_, bricklyWorkspace);
                var coordBlock = blocks[i].getRelativeToSurfaceXY();
                blocks[i].moveBy(coordBlock.x - coord.x + x, coordBlock.y - coord.y + y);
            }
        } else {
            configurationToBricklyWorkspace(conf);
        }
    }
    exports.reloadConf = reloadConf;

    function reloadView() {
        if (isVisible()) {
            var dom = (confVis) ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            configurationToBricklyWorkspace(xml);
        } else {
            seen = false;
        }
        var toolbox = GUISTATE_C.getConfigurationToolbox();
        bricklyWorkspace.updateToolbox(toolbox);
    }
    exports.reloadView = reloadView;

    function changeRobotSvg() {
        if (CV.CircuitVisualization.isRobotVisualized(GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getRobot())) {
            bricklyWorkspace.setDevice({
                group: GUISTATE_C.getRobotGroup(),
                robot: GUISTATE_C.getRobot()
            });
            confVis.resetRobot();
        }
    }
    exports.changeRobotSvg = changeRobotSvg;

    function resetView() {
        bricklyWorkspace.setDevice({
            group: GUISTATE_C.getRobotGroup(),
            robot: GUISTATE_C.getRobot()
        });
        initConfigurationEnvironment();
        var toolbox = GUISTATE_C.getConfigurationToolbox();
        bricklyWorkspace.updateToolbox(toolbox);
    }
    exports.resetView = resetView;

    function isVisible() {
        return GUISTATE_C.getView() == 'tabConfiguration';
    }

    function resetConfVisIfAvailable() {
        if (confVis) {
            confVis.dispose();
            confVis = null;
        }
    }

    function configurationToBricklyWorkspace(xml) {
        // removing changelistener in blockly doesn't work, so no other way
        listenToBricklyEvents = false;
        Blockly.hideChaff();
        bricklyWorkspace.clear();
        Blockly.svgResize(bricklyWorkspace);
        var dom = Blockly.Xml.textToDom(xml, bricklyWorkspace);
        resetConfVisIfAvailable();
        if (CV.CircuitVisualization.isRobotVisualized(GUISTATE_C.getRobotGroup(), GUISTATE_C.getRobot())) {
            confVis = CV.CircuitVisualization.domToWorkspace(dom, bricklyWorkspace);
        } else {
            Blockly.Xml.domToWorkspace(dom, bricklyWorkspace);
        }
        bricklyWorkspace.setVersion(dom.getAttribute('xmlversion'));
        var name = xml == GUISTATE_C.getConfigurationConf() ? GUISTATE_C.getRobotGroup().toUpperCase() + "basis" : '';
        GUISTATE_C.setConfigurationName(name);
        GUISTATE_C.setConfigurationSaved(true);
        $('#tabConfigurationName').html(name);
        setTimeout(function() {
            listenToBricklyEvents = true;
        }, 500);
        if (isVisible()) {
            seen = true;
        } else {
            seen = false;
        }
        if (GUISTATE_C.isConfigurationUsed()) {
            bricklyWorkspace.setVisible(true);
        } else {
            bricklyWorkspace.setVisible(false);
        }
    }
    exports.configurationToBricklyWorkspace = configurationToBricklyWorkspace;

});
