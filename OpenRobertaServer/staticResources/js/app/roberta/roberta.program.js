define([ 'exports', 'comm', 'message', 'log', 'util', 'simulation.simulation', 'roberta.user-state', 'rest.program', 'roberta.user', 'roberta.robot',
        'roberta.brick-configuration', 'roberta.navigation', 'blocks', 'prettify', 'jquery', 'jquery-validate', 'blocks-msg' ], function(exports, COMM, MSG,
        LOG, UTIL, SIM, userState, PROGRAM, ROBERTA_USER, ROBERTA_ROBOT, ROBERTA_BRICK_CONFIGURATION, ROBERTA_NAVIGATION, Blockly, Prettify, $) {

    var $formSingleModal;

    var blocklyWorkspace;

    /**
     * Save program to server
     */
    function saveToServer() {
        if (userState.program) {
            var xml = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlText = Blockly.Xml.domToText(xml);
            userState.programSaved = true;
            LOG.info('save program ' + userState.program + ' login: ' + userState.id);
            $('.modal').modal('hide'); // close all opened popups
            PROGRAM.saveProgramToServer(userState.program, userState.programShared, userState.programTimestamp, xmlText, function(result) {
                if (result.rc === 'ok') {
                    userState.programModified = false;
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
                    $('#menuSaveProg').parent().removeClass('disabled');
                    blocklyWorkspace.robControls.enable('saveProgram');
                    userState.programSaved = true;
                    userState.programModified = false;
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
     * Display programs in a table
     * 
     * @param {result}
     *            result object of server call
     */
    function showPrograms(result) {
        UTIL.response(result);
        if (result.rc === 'ok') {
            var $table = $('#programNameTable').dataTable();
            $table.fnClearTable();
            if (result.programNames.length > 0) {
                $table.fnAddData(result.programNames);
            }
            ROBERTA_ROBOT.setState(result);
        }
    }

    exports.showPrograms = showPrograms;

    /**
     * Load the program that was selected in program list
     */
    function loadFromListing() {
        var $programRow = $('#programNameTable .selected');
        if ($programRow.length > 0) {
            var programName = $programRow[0].children[0].textContent;
            var owner = $programRow[0].children[1].textContent;
            var right = $programRow[0].children[2].textContent;
            userState.programTimestamp = $programRow[0].children[5].textContent;
            LOG.info('loadFromList ' + programName + ' signed in: ' + userState.id);
            PROGRAM.loadProgramFromListing(programName, owner, function(result) {
                if (result.rc === 'ok') {
                    userState.programShared = false;
                    $('#menuSaveProg').parent().removeClass('disabled');
                    blocklyWorkspace.robControls.enable("saveProgram");
                    if (right === Blockly.Msg.POPUP_SHARE_READ) {
                        $('#menuSaveProg').parent().addClass('disabled');
                        blocklyWorkspace.robControls.disable('saveProgram');
                    } else if (right === Blockly.Msg.POPUP_SHARE_WRITE) {
                        userState.programShared = true;
                    }
                    $("#tabs").tabs("option", "active", 0);
                    userState.programSaved = true;
                    var alien = owner === userState.accountName ? null : owner;
                    showProgram(result, true, programName, alien);
                    //$('#menuSaveProg').parent().removeClass('login');
                }
                MSG.displayInformation(result, "", result.message);
            });
        }
    }

    /**
     * Delete the programs that were selected in program list
     */
    function deleteFromListing() {
        var $programRow = $('#programNameTable .selected');
        var progs = [];
        for (var i = 0; i < $programRow.length; i++) {
            progs.push({
                name : $programRow[i].children[0].textContent,
                owner : $programRow[i].children[1].textContent
            });
        }
        for (var i = 0; i < progs.length; i++) {
            var prog = progs[i];
            LOG.info('deleteFromList ' + prog.name + ' signed in: ' + userState.id);
            if (prog.owner === userState.accountName) {
                PROGRAM.deleteProgramFromListing(prog.name, function(result, progName) {
                    UTIL.response(result);
                    if (result.rc === 'ok') {
                        MSG.displayInformation(result, "MESSAGE_PROGRAM_DELETED", result.message, progName);
                        PROGRAM.refreshList(showPrograms);
                    }
                });
            } else {
                PROGRAM.deleteShare(prog.name, prog.owner, function(result, progName) {
                    UTIL.response(result);
                    if (result.rc === 'ok') {
                        MSG.displayInformation(result, "MESSAGE_PROGRAM_DELETED", result.message, progName);
                        PROGRAM.refreshList(showPrograms);
                    }
                });
            }
        }
        $('.modal').modal('hide');
    }

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
        $('#doDeleteProgram').onWrap('click', function() {
            deleteFromListing();
            //  $('.modal').modal('hide'); // close all opened popups
        }, 'delete program');

        // Refresh list of programs
        $('#refreshListing').onWrap('click', function() {
            PROGRAM.refreshList(showPrograms);
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
                    required : jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"]),
                    regex : jQuery.validator.format(Blockly.Msg["MESSAGE_INVALID_NAME"])
                }
            }
        });
    }
    exports.showSaveAsModal = showSaveAsModal;

    function initProgramEnvironment(opt_programBlocks) {
        blocklyWorkspace.clear();
        var x, y;
        if ($(window).width() < 768) {
            x = 25;
            y = 25;
        } else {
            x = 370;
            y = 50;
        }
        var text = "<block_set xmlns='http: // www.w3.org/1999/xhtml'>" + "<instance x='" + x + "' y='" + y + "'>" + "<block type='robControls_start'>"
                + "</block>" + "</instance>" + "</block_set>";
        var program = opt_programBlocks || text;
        var xml = Blockly.Xml.textToDom(program);
        Blockly.Xml.domToWorkspace(blocklyWorkspace, xml);
        userState.blocklyReady = true;
        Blockly.svgResize(blocklyWorkspace);
    }
    exports.initProgramEnvironment = initProgramEnvironment;

    /**
     * New program
     */
    function newProgram() {
        if (userState.programModified) {
            if (userState.id === -1) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "");
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "");
            }
            userState.programModified = false;
            return false;
        } else {
            ROBERTA_USER.setProgram("NEPOprog");
            userState.programShared = false;
            userState.programTimestamp = '';
            initProgramEnvironment();
            $('#tabProgram').click();
            $('#menuSaveProg').parent().addClass('disabled');
            blocklyWorkspace.robControls.disable('saveProgram');
            return true;
        }
    }
    exports.newProgram = newProgram;

    function showProgram(result, load, name, opt_owner) {
        UTIL.response(result);
        if (result.rc === 'ok') {
            ROBERTA_USER.setProgram(name, opt_owner);
            var xml = Blockly.Xml.textToDom(result.data);
            if (load) {
                blocklyWorkspace.clear();
            }
            Blockly.Xml.domToWorkspace(blocklyWorkspace, xml);
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
        if (userState.robot === 'ev3') {
            LOG.info('show code ' + userState.program + ' signed in: ' + userState.id);
            var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
            var xmlTextConfiguration = ROBERTA_BRICK_CONFIGURATION.getXmlOfConfiguration();

            PROGRAM.showSourceProgram(userState.program, userState.configuration, xmlTextProgram, xmlTextConfiguration, function(result) {
                ROBERTA_ROBOT.setState(result);
                $('#blocklyDiv').addClass('codeActive');
                $('#blocklyDiv').parent().bind('transitionend', function() {
                    Blockly.svgResize(blocklyWorkspace);
                });
                $('#codeDiv').addClass('codeActive');
                $('.nav > li > ul > .robotType').addClass('disabled');
                $('#head-navigation-program-edit').addClass('disabled');
                $('#head-navigation-program-edit>ul').addClass('hidden');
                UTIL.cacheBlocks(blocklyWorkspace);
                COMM.json("/toolbox", {
                    "cmd" : "loadT",
                    "name" : userState.toolbox,
                    "owner" : " "
                }, function(result) {
                    injectBlockly(result, userState.programBlocks, true);
                });
                $(".code").removeClass('hide');
                $('#codeDiv').html('<pre class="prettyprint linenums">' + prettyPrintOne(result.javaSource, null, true) + '</pre>');
            });
        }
    }
    exports.showCode = showCode;

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
            //PROGRAM.showSourceProgram(userState.program, userState.configuration, xmlTextProgram, xmlTextConfiguration, function(result) {
            // console.log(result.javaSource);
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
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var xmlTextConfiguration = ROBERTA_BRICK_CONFIGURATION.getXmlOfConfiguration();

        PROGRAM.runInSim(userState.program, userState.configuration, xmlTextProgram, xmlTextConfiguration, function(result) {
            SIM.init(result.javaScriptProgram);
            $('#blocklyDiv').addClass('simActive');
            $('#blocklyDiv').parent().bind('transitionend', function() {
                $(window).trigger('resize');
                Blockly.svgResize(blocklyWorkspace);
                setTimeout(function() {
                    SIM.setPause(false);
                }, 1000);
            });
            $('#simDiv').addClass('simActive');
            $('#simButtonsCollapse').collapse({
                'toggle' : false
            });
            $('.nav > li > ul > .robotType').addClass('disabled');
            $('#head-navigation-program-edit').addClass('disabled');
            $('#head-navigation-program-edit>ul').addClass('hidden');
            UTIL.cacheBlocks(blocklyWorkspace);
            refreshBlocklyProgram(result);
            $(".sim").removeClass('hide');
        });
    }
    exports.runInSim = runInSim;

    /**
     * Inject Blockly with initial toolbox
     */
    function injectBlockly(toolbox, opt_programBlocks, opt_readOnly) {
        UTIL.response(toolbox);
        var readOnly = opt_readOnly | false;
        if (toolbox.rc === 'ok') {
            if ($(window).width() < 768 && readOnly) {
                blocklyWorkspace.clear();
            } else {
                $('#blocklyDiv').html('');
                blocklyWorkspace = Blockly.inject(document.getElementById('blocklyDiv'), {
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
                    checkInTask : [ 'start', '_def', 'event' ],
                    variableDeclaration : true,
                    robControls : true
                });
                blocklyWorkspace.addChangeListener(function(event) {
                    console.log("changed");
                });
                Blockly.bindEvent_(blocklyWorkspace.robControls.runOnBrick, 'mousedown', null, function(e) {
                    LOG.info('runOnBrick from blockly button');
                    runOnBrick();
                });
                Blockly.bindEvent_(blocklyWorkspace.robControls.runInSim, 'mousedown', null, function(e) {
                    LOG.info('runInSim from blockly button');
                    runInSim();
                });
                Blockly.bindEvent_(blocklyWorkspace.robControls.saveProgram, 'mousedown', null, function(e) {
                    LOG.info('saveProgram from blockly button');
                    saveToServer();
                });
                blocklyWorkspace.robControls.disable('saveProgram');
            }
            initProgramEnvironment(opt_programBlocks);
            ROBERTA_ROBOT.setState(toolbox);
        }
    }
    exports.injectBlockly = injectBlockly;

    function getBlocklyWorkspace() {
        return blocklyWorkspace;
    }

    exports.getBlocklyWorkspace = getBlocklyWorkspace;
});
