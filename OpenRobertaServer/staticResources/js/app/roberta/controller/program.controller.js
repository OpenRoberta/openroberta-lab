define([ 'exports', 'comm', 'message', 'log', 'util', 'simulation.simulation', 'guiState.controller', 'rest.program', 'roberta.tour', 'blocks', 'jquery',
        'jquery-validate', 'blocks-msg' ], function(exports, COMM, MSG, LOG, UTIL, SIM, guiStateController, PROGRAM, ROBERTA_TOUR, Blockly, $) {

    var $formSingleModal;

    var blocklyWorkspace;
    /**
     * Inject Blockly with initial toolbox
     */
    function init() {
        initView();
        initEvents();
        initProgramForms();
        LOG.info('init program view');
    }
    exports.init = init;

    function initView() {
        var toolbox = guiStateController.getProgramToolbox();
        blocklyWorkspace = Blockly.inject(document.getElementById('blocklyDiv'), {
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
            checkInTask : [ 'start', '_def', 'event' ],
            variableDeclaration : true,
            robControls : true
        });
        blocklyWorkspace.device = guiStateController.getRobot();
        guiStateController.setBlocklyWorkspace(blocklyWorkspace);
        initProgramEnvironment();
    }

    function initEvents() {
        $('#tabProgram').onWrap('shown.bs.tab', function() {
            guiStateController.setView('tabProgram');
            blocklyWorkspace.markFocused();
            blocklyWorkspace.setVisible(true);
            $('#head-navigation-program-edit').css('display', 'inline');
            $('#head-navigation-configuration-edit').css('display', 'none');
            $('#menuTabProgram').parent().addClass('disabled');
            $('#menuTabConfiguration').parent().removeClass('disabled');
            reloadProgram();
        }, 'tabProgram clicked');

        $('#tabProgram').on('hide.bs.tab', function(e) {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            guiStateController.setProgramXML(xml);
        });

        var moveCounter = 0;
        blocklyWorkspace.addChangeListener(function(event) {
            if (event.type !== 'create' && guiStateController.isProgramSaved() && moveCounter >= 1) {
                moveCounter = 0;
                guiStateController.setProgramSaved(false);
            } else if (event.type !== 'create') {
                moveCounter++;
            }
        });
        bindControl();
    }

    /**
     * Save program to server
     */
    function saveToServer() {
        if (guiStateController.program) {
            var xml = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlText = Blockly.Xml.domToText(xml);
            $('.modal').modal('hide'); // close all opened popups
            PROGRAM.saveProgramToServer(userState.program, userState.programShared ? true : false, userState.programTimestamp, xmlText, function(result) {
                if (result.rc === 'ok') {
                    $('#menuSaveProg').parent().addClass('disabled');
                    blocklyWorkspace.robControls.disable('saveProgram');
                    userState.programSaved = true;
                    LOG.info('save program ' + userState.program + ' login: ' + userState.id);
                    userState.programTimestamp = result.lastChanged;
                }
                MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_PROGRAM", result.message, userState.program);
            });
        }
    }
    exports.saveToServer = saveToServer;

    /**
     * Save program with new name to server
     */
    function saveAsProgramToServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            var xml = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlText = Blockly.Xml.domToText(xml);
            var progName = $('#singleModalInput').val().trim();
            LOG.info('saveAs program ' + userState.program + ' login: ' + userState.id);
            PROGRAM.saveAsProgramToServer(progName, userState.programTimestamp, xmlText, function(result) {
                UTIL.response(result);
                if (result.rc === 'ok') {
                    ROBERTA_USER.setProgram(progName);
                    $('#menuSaveProg').parent().addClass('disabled');
                    blocklyWorkspace.robControls.disable('saveProgram');
                    userState.programSaved = true;
                    userState.programTimestamp = result.lastChanged;
                    MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_PROGRAM_AS", result.message, userState.program);
                }
            });
        }
    }

    function refreshBlocklyProgram(result) {
        var xml = Blockly.Xml.textToDom(result.data);
        blocklyWorkspace.clear();
        Blockly.Xml.domToWorkspace(blocklyWorkspace, xml);
    }

    /**
     * Load the program that was selected in program list
     */
    function loadFromListing(program) {
        var right = 'none';
        LOG.info('loadFromList ' + programName + ' signed in: ' + userState.id);
        PROGRAM.loadProgramFromListing(program[0], program[1], function(result) {
            if (result.rc === 'ok') {
                result.programShared = false;
                var alien = program[1] === userState.accountName ? null : program[1];
                if (alien) {
                    result.programShared = 'READ';
                }
                if (program[2].sharedFrom) {
                    var right = program[2].sharedFrom;
                    result.programShared = right;
                }
                result.name = program[0];
                result.programSaved = true;
                $('#tabProgram').one('shown.bs.tab', function() {
                    showProgram(result, alien);
                });
                $('#tabProgram').trigger('click');
            }
            MSG.displayInformation(result, "", result.message);
        });
    }
    exports.loadFromListing = loadFromListing;

    function initProgramForms() {
        $formSingleModal = $('#single-modal-form');
        $('#buttonCancelFirmwareUpdateAndRun').onWrap('click', function() {
            start();
        });

        // load program
        $('#loadFromListing').onWrap('click', function() {
            ROBERTA_NAVIGATION.activateProgConfigMenu();
            loadFromListing();
        }, 'load blocks from program list');

        // delete program
//        $('#doDeleteProgram').onWrap('click', function() {
//            deleteFromListing();
//            //  $('.modal').modal('hide'); // close all opened popups
//        }, 'delete program');

        // Refresh list of programs
        $('#refreshListing').onWrap('click', function() {
            PROGRAM.refreshList(showPrograms);
        }, 'refresh list of programs');

        // Refresh expamle list of programs
        $('#refreshExamplesListing').onWrap('click', function() {
            PROGRAM.refreshExamplesList(showPrograms);
        }, 'refresh list of programs');

        // confirm program deletion
        $('#deleteFromListing').onWrap('click', function() {
            var $programRow = $('#programNameTable .selected');
            if ($programRow.length > 0) {
                var names = '';
                for (var i = 0; i < $programRow.length; i++) {
                    names += ($programRow[i].children[0].textContent + '</br>');
                }
                $('#confirmDeleteProgramName').html(names);
                $("#confirmDeleteProgram").modal("show");
            }
        }, 'Ask for confirmation to delete programs');

    }
    exports.initProgramForms = initProgramForms;

    function save() {
        saveToServer();
    }
    exports.save = save;

    function showSaveAsModal() {
        $.validator.addMethod("regex", function(value, element, regexp) {
            value = value.trim();
            return value.match(regexp);
        }, "No special Characters allowed here. Use only upper and lowercase letters (A through Z; a through z) and numbers.");

        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg["MENU_SAVE_AS"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_NAME"]);
        }, saveAsProgramToServer, function() {

        }, {
            rules : {
                singleModalInput : {
                    required : true,
                    regex : /^[a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]*$/
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                singleModalInput : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    regex : Blockly.Msg["MESSAGE_INVALID_NAME"]
                }
            }
        });
    }
    exports.showSaveAsModal = showSaveAsModal;

    function initProgramEnvironment() {
        blocklyWorkspace.clear();
        Blockly.hideChaff();
        Blockly.svgResize(blocklyWorkspace);
        var x, y;
        if ($(window).width() < 768) {
            x = $(window).width() / 50;
            y = 25;
        } else {
            x = $(window).width() / 5;
            y = 50;
        }
        var program = guiStateController.getProgramProg();
        var dom = Blockly.Xml.textToDom(program);
        Blockly.Xml.domToWorkspace(blocklyWorkspace, dom);
        var blocks = blocklyWorkspace.getTopBlocks(true);
        if (blocks[0]) {
            var coord = blocks[0].getRelativeToSurfaceXY();
            blocks[0].moveBy(x - coord.x, y - coord.y);
        }
        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xml = Blockly.Xml.domToText(dom);
        guiStateController.setProgramXML(xml);
    }
    exports.initProgramEnvironment = initProgramEnvironment;

    /**
     * New program
     */
    function newProgram(opt_further) {
        var further = opt_further || false;
        if (further || userState.programSaved) {
            var result = {};
            result.name = "NEPOprog"
            result.programSaved = 'new';
            result.programShared = false;
            result.lastChanged = '';
            ROBERTA_USER.setProgram(result);
            initProgramEnvironment();
            //$('#tabProgram').click();
            $('#menuSaveProg').parent().addClass('disabled');
            blocklyWorkspace.robControls.disable('saveProgram');
        } else {
            $('#confirmContinue').data('type', 'program');
            if (userState.id === -1) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
            }
        }
    }
    exports.newProgram = newProgram;

    function showProgram(result, alien) {
        if (result.rc === 'ok') {
            ROBERTA_USER.setProgram(result, alien);
            var xml = Blockly.Xml.textToDom(result.data);
            blocklyWorkspace.clear();
            Blockly.Xml.domToWorkspace(blocklyWorkspace, xml);

            $('#menuSaveProg').parent().addClass('disabled');
            blocklyWorkspace.robControls.disable('saveProgram');

            LOG.info('show program ' + userState.program + ' signed in: ' + userState.id);
        }
    }

    exports.showProgram = showProgram;

    /**
     * Check program
     */
    function checkProgram() {
        LOG.info('check ' + userState.program + ' signed in: ' + userState.id);
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var xmlTextConfiguration = ROBERTA_BRICK_CONFIGURATION.getXmlOfConfiguration();
        MSG.displayMessage("MESSAGE_EDIT_CHECK", "TOAST", userState.program);
        PROGRAM.checkProgramCompatibility(userState.program, userState.configuration, xmlTextProgram, xmlTextConfiguration, function(result) {
            refreshBlocklyProgram(result);
            MSG.displayInformation(result, "", result.message, "");
        });
    }
    exports.checkProgram = checkProgram;

    /**
     * Show program code
     */
    function showCode() {

        //if (userState.robot === 'ev3') {
        LOG.info('show code ' + userState.program + ' signed in: ' + userState.id);
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var xmlTextConfiguration = ROBERTA_BRICK_CONFIGURATION.getXmlOfConfiguration();

        PROGRAM.showSourceProgram(userState.program, userState.configuration, xmlTextProgram, xmlTextConfiguration, function(result) {
            ROBERTA_ROBOT.setState(result);
            if ($(window).width() < 768) {
                width = '0';
            } else {
                width = '30%';
            }
            $('#blocklyDiv').animate({
                width : width
            }, {
                duration : 750,
                step : function() {
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                },
                done : function() {
                    Blockly.svgResize(blocklyWorkspace);
                }
            });
            $('#blocklyDiv').addClass('codeActive');
            $('#codeDiv').addClass('codeActive');
            $('.nav > li > ul > .robotType').addClass('disabled');
            $(".code").removeClass('hide');
            $('#codeContent').html('<pre class="prettyprint linenums">' + prettyPrintOne(result.javaSource, null, true) + '</pre>');
            userState.programSource = result.javaSource
            console.log(prettyPrintOne(result.javaSource, null, true));
        });
    }
    exports.showCode = showCode;

    /**
     * Open a file select dialog to load a blockly program (xml) from local
     * disk.
     */
    function importXml() {
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlProgramSave = Blockly.Xml.domToText(xmlProgram);
        var input = $(document.createElement('input'));
        input.attr("type", "file");
        input.attr("accept", ".xml");
        input.trigger('click'); // opening dialog
        input.change(function(event) {
            var file = event.target.files[0]
            var reader = new FileReader()
            reader.readAsText(file)
            reader.onload = function(event) {
                var name = UTIL.getBasename(file.name);
                PROGRAM.loadProgramFromXML(name, event.target.result, function(result) {
                    if (result.rc == "ok") {
                        // on server side we only test case insensitiv block names, displaying xml can still fail:
                        try {
                            result.programSaved = false;
                            result.programShared = false;
                            result.programTimestamp = '';
                            showProgram(result);
                        } catch (e) {
                            result.data = xmlProgramSave;
                            result.name = userState.program;
                            showProgram(result);
                            result.rc = "error";
                            MSG.displayInformation(result, "", Blockly.Msg.ORA_PROGRAM_IMPORT_ERROR, result.name);
                        }
                    } else {
                        MSG.displayInformation(result, "", result.message, "");
                    }
                });
            }
        })
    }
    exports.importXml = importXml;

    /**
     * Start the program on the brick
     */
    function runOnBrick() {
        if (userState.robotState === '' || userState.robotState === 'disconnected') {
            MSG.displayMessage("POPUP_ROBOT_NOT_CONNECTED", "POPUP", "");
            return;
        } else if (userState.robotState === 'busy') {
            MSG.displayMessage("POPUP_ROBOT_BUSY", "POPUP", "");
            return;
        } else if (ROBERTA_ROBOT.handleFirmwareConflict()) {
            $('#buttonCancelFirmwareUpdate').css('display', 'none');
            $('#buttonCancelFirmwareUpdateAndRun').css('display', 'inline');
            return;
        }
        LOG.info('run ' + userState.program + 'on brick' + ' signed in: ' + userState.id);
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var xmlTextConfiguration = ROBERTA_BRICK_CONFIGURATION.getXmlOfConfiguration();

        PROGRAM.runOnBrick(userState.program, userState.configuration, xmlTextProgram, xmlTextConfiguration, function(result) {
            ROBERTA_ROBOT.setState(result);
            if (result.rc == "ok") {
                MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", userState.program);
            } else {
                MSG.displayInformation(result, "", result.message, "");
            }
            refreshBlocklyProgram(result);
        });
    }
    exports.runOnBrick = runOnBrick;

    /**
     * Start the program in the simulation.
     */
    function runInSim() {
        LOG.info('run ' + userState.program + 'in simulation' + ' signed in: ' + userState.id);
        Blockly.hideChaff();
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var xmlTextConfiguration = ROBERTA_BRICK_CONFIGURATION.getXmlOfConfiguration();

        PROGRAM.runInSim(userState.program, userState.configuration, xmlTextProgram, xmlTextConfiguration, function(result) {
            if (result.rc == "ok") {
                MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", userState.program);
                blocklyWorkspace.robControls.setSimStart(false);
                refreshBlocklyProgram(result);

                if ($(".sim").hasClass('hide')) {
                    SIM.init(result.javaScriptProgram, true);
                    $(".sim").removeClass('hide');
                    $('#blocklyDiv').addClass('simActive');
                    $('#simDiv').addClass('simActive');
                    $('#simButtonsCollapse').collapse({
                        'toggle' : false
                    });
                    $('.nav > li > ul > .robotType').addClass('disabled');
                    $('#menuShowCode').parent().addClass('disabled');
                    var width;
                    var smallScreen;
                    if ($(window).width() < 768) {
                        smallScreen = true;
                        width = '52px';
                    } else {
                        smallScreen = false;
                        width = '30%';
                    }
                    $('#blocklyDiv').animate({
                        width : width
                    }, {
                        duration : 750,
                        step : function() {
                            $(window).resize();
                            Blockly.svgResize(blocklyWorkspace);
                        },
                        done : function() {
                            if (smallScreen) {
                                $('.blocklyToolboxDiv').css('display', 'none');
                            }
                            blocklyWorkspace.robControls.toogleSim();
                            Blockly.svgResize(blocklyWorkspace);
                            if (ROBERTA_TOUR.getInstance()) {
                                ROBERTA_TOUR.getInstance().trigger('SimLoaded');
                            }
                            setTimeout(function() {
                                SIM.setPause(false);
                            }, 1000);
                        }
                    });
                } else {
                    SIM.init(result.javaScriptProgram, false);
                    setTimeout(function() {
                        SIM.setPause(false);
                    }, 1000);
                }
            } else {
                MSG.displayInformation(result, "", result.message, "");
            }
            refreshBlocklyProgram(result);
        });
    }
    exports.runInSim = runInSim;

    function getBlocklyWorkspace() {
        return blocklyWorkspace;
    }

    exports.getBlocklyWorkspace = getBlocklyWorkspace;

    function updateRobControls() {
        blocklyWorkspace.updateRobControls();
        bindControl();
    }
    exports.updateRobControls = updateRobControls;

    function bindControl() {
        Blockly.bindEvent_(blocklyWorkspace.robControls.runOnBrick, 'mousedown', null, function(e) {
            LOG.info('runOnBrick from blockly button');
            runOnBrick();
            return false;
        });
        Blockly.bindEvent_(blocklyWorkspace.robControls.runInSim, 'mousedown', null, function(e) {
            LOG.info('runInSim from blockly button');
            runInSim();
            return false;
        });
        Blockly.bindEvent_(blocklyWorkspace.robControls.saveProgram, 'mousedown', null, function(e) {
            LOG.info('saveProgram from blockly button');
            saveToServer();
            return false;
        });
        Blockly.bindEvent_(blocklyWorkspace.robControls.simStop, 'mousedown', null, function(e) {
            LOG.info('simStop from blockly button');
            SIM.stopProgram();
            blocklyWorkspace.robControls.setSimStart(true);
            return false;
        });
        blocklyWorkspace.robControls.disable('saveProgram');
        blocklyWorkspace.robControls.disable('runOnBrick');
    }

    function reloadProgram() {
        blocklyWorkspace.clear();
        Blockly.hideChaff();
        Blockly.svgResize(blocklyWorkspace);
        var program = guiStateController.getProgramXML();
        var dom = Blockly.Xml.textToDom(program);
        Blockly.Xml.domToWorkspace(blocklyWorkspace, dom);
    }

    function reloadView() {
        if (isVisible()) {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            blocklyWorkspace.clear();
            dom = Blockly.Xml.textToDom(xml);
            Blockly.Xml.domToWorkspace(blocklyWorkspace, dom);
        }
        var toolbox = guiStateController.getProgramToolbox();
        blocklyWorkspace.updateToolbox(toolbox);
    }

    exports.reloadView = reloadView;

    function resetView() {
        blocklyWorkspace.device = guiStateController.getRobot();
        initProgramEnvironment();
        var toolbox = guiStateController.getProgramToolbox();
        blocklyWorkspace.updateToolbox(toolbox);
        blocklyWorkspace.device = guiStateController.getRobot();
    }
    exports.resetView = resetView;

    function isVisible() {
        return guiStateController.getView() == 'tabProgram';
    }
});
