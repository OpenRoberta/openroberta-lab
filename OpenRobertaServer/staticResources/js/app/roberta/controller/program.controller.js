define([ 'exports', 'comm', 'message', 'log', 'util', 'simulation.simulation', 'guiState.controller', 'program.model', 'prettify', 'robot.controller', 'tour.controller', 'blocks', 'jquery', 'jquery-validate', 'blocks-msg' ], function(exports, COMM, MSG, LOG, UTIL, SIM, GUISTATE_C, PROGRAM, Prettify, ROBOT_C, TOUR_C, Blockly, $) {

    var $formSingleModal;

    var blocklyWorkspace;
    var listenToBlocklyEvents = true;
    /**
     * Inject Blockly with initial toolbox
     */
    function init() {
        initView();
        initProgramEnvironment();
        initEvents();
        initProgramForms();
        LOG.info('init program view');
    }
    exports.init = init;

    function initView() {
        var toolbox = GUISTATE_C.getProgramToolbox();
        blocklyWorkspace = Blockly.inject(document.getElementById('blocklyDiv'), {
            path : '/blockly/',
            toolbox : toolbox,
            trashcan : true,
            scrollbars : true,
            media : '../blockly/media/',
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
        blocklyWorkspace.setDevice(GUISTATE_C.getRobot());
        //TODO: add the version information in the Parent POM!.
        blocklyWorkspace.setVersion('2.0');
        GUISTATE_C.setBlocklyWorkspace(blocklyWorkspace);
        blocklyWorkspace.robControls.disable('saveProgram');
        GUISTATE_C.checkSim();
    }

    function initEvents() {

        $('#tabProgram').on('show.bs.tab', function(e) {
            GUISTATE_C.setView('tabProgram');
            blocklyWorkspace.markFocused();
        });

        $('#tabProgram').onWrap('shown.bs.tab', function(e) {
            blocklyWorkspace.setVisible(true);
            reloadProgram();
        }, 'tabProgram clicked');

        $('#tabProgram').on('hide.bs.tab', function(e) {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            GUISTATE_C.setProgramXML(xml);
        });
        bindControl();
        blocklyWorkspace.addChangeListener(function(event) {
            if (listenToBlocklyEvents && event.type != Blockly.Events.UI && GUISTATE_C.isProgramSaved()) {
                GUISTATE_C.setProgramSaved(false);
            }
        });
    }

    /**
     * Save program to server
     */
    function saveToServer() {
        $('.modal').modal('hide'); // close all opened popups
        var xml = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlText = Blockly.Xml.domToText(xml);
        PROGRAM.saveProgramToServer(GUISTATE_C.getProgramName(), GUISTATE_C.getProgramShared() ? true : false, GUISTATE_C.getProgramTimestamp(), xmlText, function(result) {
            if (result.rc === 'ok') {
                GUISTATE_C.setProgramTimestamp(result.lastChanged);
                GUISTATE_C.setProgramSaved(true);
                LOG.info('save program ' + GUISTATE_C.getProgramName());
            }
            MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_PROGRAM", result.message, GUISTATE_C.getProgramName());
        });
    }
    exports.saveToServer = saveToServer;

    /**
     * Save program with new name to server
     */
    function saveAsProgramToServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            $('.modal').modal('hide'); // close all opened popups
            var xml = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlText = Blockly.Xml.domToText(xml);
            var progName = $('#singleModalInput').val().trim();
            LOG.info('saveAs program ' + GUISTATE_C.getProgramName());
            PROGRAM.saveAsProgramToServer(progName, GUISTATE_C.getProgramTimestamp(), xmlText, function(result) {
                UTIL.response(result);
                if (result.rc === 'ok') {
                    result.name = progName;
                    result.programShared = false;
                    GUISTATE_C.setProgram(result);
                    MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_PROGRAM_AS", result.message, GUISTATE_C.getProgramName());
                }
            });
        }
    }

    /**
     * Load the program that was selected in program list
     */
    function loadFromListing(program) {
        var right = 'none';
        LOG.info('loadFromList ' + program[0]);
        PROGRAM.loadProgramFromListing(program[0], program[1], function(result) {
            if (result.rc === 'ok') {
                result.programShared = false;
                var alien = program[1] === GUISTATE_C.getUserAccountName() ? null : program[1];
                if (alien) {
                    result.programShared = 'READ';
                }
                if (program[2].sharedFrom) {
                    var right = program[2].sharedFrom;
                    result.programShared = right;
                }
                result.name = program[0];
                GUISTATE_C.setProgram(result, alien);
                GUISTATE_C.setProgramXML(result.data);
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
    }
    exports.initProgramForms = initProgramForms;

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
        var x, y;
        if ($(window).width() < 768) {
            x = $(window).width() / 50;
            y = 25;
        } else {
            x = $(window).width() / 5;
            y = 50;
        }
        var program = GUISTATE_C.getProgramProg();
        programToBlocklyWorkspace(program);

        var blocks = blocklyWorkspace.getTopBlocks(true);
        if (blocks[0]) {
            var coord = blocks[0].getRelativeToSurfaceXY();
            blocks[0].moveBy(x - coord.x, y - coord.y);
        }
    }
    exports.initProgramEnvironment = initProgramEnvironment;

    /**
     * New program
     */
    function newProgram(opt_further) {
        var further = opt_further || false;
        if (further || GUISTATE_C.isProgramSaved()) {
            var result = {};
            result.rc = 'ok';
            result.name = "NEPOprog"
            result.programShared = false;
            result.lastChanged = '';
            GUISTATE_C.setProgram(result);
            initProgramEnvironment();
        } else {
            $('#confirmContinue').data('type', 'program');
            if (GUISTATE_C.isUserLoggedIn()) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
            }
        }
    }
    exports.newProgram = newProgram;

    function showProgram(result, alien) {
        if (result.rc === 'ok') {
            programToBlocklyWorkspace(result.data);
            GUISTATE_C.setProgram(result, alien);
            LOG.info('show program ' + GUISTATE_C.getProgramName());
        }
    }

    exports.showProgram = showProgram;

    // TODO is this still supported by the server?
    //    /**
    //     * Check program
    //     */
    //    function checkProgram() {
    //        LOG.info('check ' + GUISTATE_C.getProgramName());
    //        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
    //        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
    //        var xmlTextConfiguration = ROBERTA_BRICK_CONFIGURATION.getXmlOfConfiguration();
    //        MSG.displayMessage("MESSAGE_EDIT_CHECK", "TOAST", GUISTATE_C.getProgramName());
    //        PROGRAM.checkProgramCompatibility(GUISTATE_C.getProgramName(), userState.configuration, xmlTextProgram, xmlTextConfiguration, function(result) {
    //            refreshBlocklyProgram(result);
    //            MSG.displayInformation(result, "", result.message, "");
    //        });
    //    }
    //    exports.checkProgram = checkProgram;

    /**
     * Show program code
     */
    function showCode() {

        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlProgram = Blockly.Xml.domToText(dom);
        var xmlConfiguration = GUISTATE_C.getConfigurationXML();

        PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlProgram, xmlConfiguration, function(result) {
            GUISTATE_C.setState(result);
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
            // TODO change javaSource to source on server
            GUISTATE_C.setProgramSource(result.javaSource);
            //console.log(prettyPrintOne(result.javaSource, null, true));
        });
        LOG.info('show code ' + GUISTATE_C.getProgramName());

    }
    exports.showCode = showCode;

    /**
     * Open a file select dialog to load a blockly program (xml) from local
     * disk.
     */
    function importXml() {
        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xml = Blockly.Xml.domToText(dom);
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
                        // on server side we only test case insensitive block names, displaying xml can still fail:
                        try {
                            result.programSaved = false;
                            result.programShared = false;
                            result.programTimestamp = '';
                            showProgram(result);
                        } catch (e) {
                            result.data = xml;
                            result.name = GUISTATE_C.getProgramName();
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
     * Create a file from the blocks and download it.
     */
    function exportXml() {
        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xml = Blockly.Xml.domToText(dom);
        UTIL.download(GUISTATE_C.getProgramName() + ".xml", xml);
        MSG.displayMessage("MENU_MESSAGE_DOWNLOAD", "TOAST", GUISTATE_C.getProgramName());
    }
    exports.exportXml = exportXml;

    /**
     * Start the program on the brick
     */
    function runOnBrick() {
        if (!GUISTATE_C.isRobotConnected()) {
            MSG.displayMessage("POPUP_ROBOT_NOT_CONNECTED", "POPUP", "");
            return;
        } else if (GUISTATE_C.robotState === 'busy' && !GUISTATE_C.isAutoconnected()) {
            MSG.displayMessage("POPUP_ROBOT_BUSY", "POPUP", "");
            return;
            //        } else if (ROBOT_C.handleFirmwareConflict()) {
            //            $('#buttonCancelFirmwareUpdate').css('display', 'none');
            //            $('#buttonCancelFirmwareUpdateAndRun').css('display', 'inline');
            //            return;
        }
        LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick');
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var xmlTextConfiguration = GUISTATE_C.getConfigurationXML();

        if (GUISTATE_C.isAutoconnected()) {
            GUISTATE_C.setAutoConnectedBusy(true);
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
                GUISTATE_C.setState(result);
                if (result.rc == "ok") {
                    if (GUISTATE_C.isProgramToDownload()) {
                        var filename = GUISTATE_C.getProgramName() + '.hex';
                        UTIL.download(filename, result.compiledCode);
                        GUISTATE_C.setAutoConnectedBusy(false);
                    } else {
                        //create link with content
                        var programLink = "<div id='programLink' style='text-align: center;'><br><a style='font-size:36px; padding: 20px' download='" + GUISTATE_C.getProgramName() + ".hex' href='data:Application/octet-stream;content-disposition:attachment;charset=utf-8," + encodeURIComponent(result.compiledCode) + "'>" + GUISTATE_C.getProgramName() + "</a></div>";
                        $("#liA").css('background-image', 'url(\'data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" width="102" height="77" viewBox="0 0 77 102"><text fill="%23337ab7" x="3" y="34" style="font-family:Arial;font-size:20px;text-decoration:underline">' + GUISTATE_C.getProgramName() + '</text><rect x="1" y="1" stroke="%23BBBBBB" width="100" height="75" rx="2" ry="2" style="fill:none;stroke-width:3"/><g fill="%23DF01D7"><path d="M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z" transform="matrix(-2.40165 0 0 2.44495 146.371 -56.5809)" fill="%23ff0000"/></g></svg>\')');
                        $("#liA").css('background-repeat', 'no-repeat');
                        $("#liB").css('background-image', 'url(\'data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" width="102" height="77" viewBox="0 0 77 102"><rect x="0" y="0" width="102" height="77" fill="%23dddddd"/><text fill="%23333333" x="3" y="34" style="font-family:Arial;font-size:14">' + Blockly.Msg.POPUP_DOWNLOAD_SAVE_AS + '</text><rect x="1" y="1" stroke="%23BBBBBB" width="100" height="75" rx="2" ry="2" style="fill:none;stroke-width:2"/><g fill="%23DF01D7"><path d="M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z" transform="matrix(2.44495 0 0 2.44495 -47.8835 -56.5809)" fill="%239400D3"/></g></svg>\')');
                        $("#liB").css('background-repeat', 'no-repeat');
                        var usb;
                        if (GUISTATE_C.getGuiRobot() == "calliope") {
                            usb = "MINI";
                        } else {
                            usb = GUISTATE_C.getGuiRobot().toUpperCase();
                        }
                        $("#liC").css('background-image', 'url(\'data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" width="102" height="77" viewBox="0 0 77 102"><rect x="0" y="0" width="102" height="77" fill="%23dddddd"/><text fill="%23333333" x="3" y="34" style="font-family:Arial;font-size:14">' + usb + '</text><rect x="1" y="1" stroke="%23BBBBBB" width="100" height="75" rx="2" ry="2" style="fill:none;stroke-width:2"/><g fill="%23DF01D7"><path d="M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z" transform="matrix(2.44495 0 0 2.44495 -47.8835 -56.5809)" fill="%239400D3"/></g></svg>\')');
                        $("#liC").css('background-repeat', 'no-repeat');
                        $("#liD").css('background-image', 'url(\'data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" width="102" height="77" viewBox="0 0 77 102"><rect x="0" y="0" width="102" height="77" fill="%23dddddd"/><text fill="%23333333" x="3" y="40" style="font-family:Arial;font-size:20px">' + Blockly.Msg.POPUP_DOWNLOAD_SAVE + '</text><rect x="1" y="1" stroke="%23BBBBBB" width="100" height="75" rx="2" ry="2" style="fill:none;stroke-width:2"/><g fill="%23DF01D7"><path d="M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z" transform="matrix(2.44495 0 0 2.44495 -47.8835 -56.5809)" fill="%239400D3"/></g></svg>\')');
                        $("#liD").css('background-repeat', 'no-repeat');
                        var textH
                        var textC;
                        $("#save-client-compiled-program").one("shown.bs.modal", function(e) {
                            $('#downloadLink').append(programLink);
                            $('#download-instructions tr').each(function(i) {
                                $(this).delay(1000 * i).animate({
                                    opacity : 1
                                }, 1000);
                            });

                        });
                        $('#save-client-compiled-program').one('hidden.bs.modal', function(e) {
                            if ($('#label-checkbox').is(':checked')) {
                                GUISTATE_C.setProgramToDownload();
                            }
                            $('#programLink').remove();
                            $('#download-instructions tr').each(function(i) {
                                $(this).css('opacity', '0');
                            });
                            if (textC) {
                                $("#download-instructions").find("tr").eq(2).find("td").eq(1).html(textC);
                            }
                            if (textH) {
                                $("#popupDownloadHeader").text(textH);
                            }
                            GUISTATE_C.setAutoConnectedBusy(false);
                        });
                        var robotRealName;
                        var list = GUISTATE_C.getRobots();
                        
                        for (var robot in list) {
                            if (!list.hasOwnProperty(robot)) continue;
                            if (list[robot].name == GUISTATE_C.getGuiRobot()) {
                              robotRealName = list[robot].realName;
                            }
                          }
                        // fix header$(selector).attr(attribute)
                        textH = $("#popupDownloadHeader").text();
                        $("#popupDownloadHeader").text(textH.replace("$", $.trim(robotRealName)));
                        textC = $("#download-instructions").find("tr").eq(2).find("td").eq(1).html();
                        $("#download-instructions").find("tr").eq(2).find("td").eq(1).html(textC.replace("$", usb));
                        $('#save-client-compiled-program').modal('show');
                    }
                } else {
                    MSG.displayInformation(result, "", result.message, "");
                    GUISTATE_C.setAutoConnectedBusy(false);
                }
                reloadProgram(result);
            });
        } else {
            PROGRAM.runOnBrick(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
                GUISTATE_C.setState(result);
                if (result.rc == "ok") {
                    MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", GUISTATE_C.getProgramName());
                } else {
                    MSG.displayInformation(result, "", result.message, "");
                }
                reloadProgram(result);
            });
        }
    }
    exports.runOnBrick = runOnBrick;

    /**
     * Start the program in the simulation.
     */
    function runInSim() {
        LOG.info('run ' + GUISTATE_C.getProgramName() + 'in simulation');
        Blockly.hideChaff();
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var xmlTextConfiguration = GUISTATE_C.getConfigurationXML();
        GUISTATE_C.setConfigurationXML(xmlTextConfiguration);

        PROGRAM.runInSim(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
            if (result.rc == "ok") {
                MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", GUISTATE_C.getProgramName());
                blocklyWorkspace.robControls.setSimStart(false);
                reloadProgram(result);

                if ($(".sim").hasClass('hide')) {
                    SIM.init(result.javaScriptProgram, true, GUISTATE_C.getRobot());
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
                            $(window).resize();
                            blocklyWorkspace.robControls.toogleSim();
                            Blockly.svgResize(blocklyWorkspace);
                            if (TOUR_C.getInstance()) {
                                TOUR_C.getInstance().trigger('SimLoaded');
                            }
                            setTimeout(function() {
                                SIM.setPause(false);
                            }, 1000);
                        }
                    });
                } else {
                    SIM.init(result.javaScriptProgram, false, GUISTATE_C.getRobot());
                    setTimeout(function() {
                        SIM.setPause(false);
                    }, 1000);
                }
            } else {
                MSG.displayInformation(result, "", result.message, "");
            }
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
        if (!GUISTATE_C.isAutoconnected())
            blocklyWorkspace.robControls.disable('runOnBrick');
    }

    function reloadProgram(opt_result) {
        if (opt_result) {
            program = opt_result.data;
        } else {
            program = GUISTATE_C.getProgramXML();
        }
        programToBlocklyWorkspace(program);
    }

    function reloadView() {
        if (isVisible()) {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            programToBlocklyWorkspace(xml);
        }
        var toolbox = GUISTATE_C.getProgramToolbox();
        blocklyWorkspace.updateToolbox(toolbox);
    }

    exports.reloadView = reloadView;

    function resetView() {
        blocklyWorkspace.setDevice(GUISTATE_C.getRobot());
        //TODO: add the version information in the Parent POM!.
        blocklyWorkspace.setVersion('2.0');
        initProgramEnvironment();
        var toolbox = GUISTATE_C.getProgramToolbox();
        blocklyWorkspace.updateToolbox(toolbox);
    }
    exports.resetView = resetView;

    function loadToolbox(level) {
        Blockly.hideChaff();
        GUISTATE_C.setProgramToolboxLevel(level);
        var xml = GUISTATE_C.getToolbox(level);
        if (xml) {
            blocklyWorkspace.updateToolbox(xml);
        }
    }
    exports.loadToolbox = loadToolbox;

    function isVisible() {
        return GUISTATE_C.getView() == 'tabProgram';
    }

    function programToBlocklyWorkspace(xml) {
        // removing changelistener in blockly doesn't work, so no other way
        listenToBlocklyEvents = false;
        Blockly.hideChaff();
        blocklyWorkspace.clear();
        Blockly.svgResize(blocklyWorkspace);
        var dom = Blockly.Xml.textToDom(xml, blocklyWorkspace);
        Blockly.Xml.domToWorkspace(dom, blocklyWorkspace);
        setTimeout(function() {
            listenToBlocklyEvents = true;
        }, 500);
    }
});
