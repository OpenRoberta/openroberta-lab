define([ 'exports', 'comm', 'message', 'log', 'util', 'guiState.controller', 'guiState.model', 'program.model', 'prettify', 'robot.controller',
        'socket.controller', 'progHelp.controller', 'progRun.controller', 'progInfo.controller', 'progCode.controller', 'progSim.controller', 'blocks',
        'jquery', 'jquery-validate', 'blocks-msg' ], function(exports, COMM, MSG, LOG, UTIL, GUISTATE_C, GUISTATE, PROGRAM, Prettify, ROBOT_C, SOCKET_C,
        HELP_C, RUN_C, INFO_C, CODE_C, SIM_C, Blockly, $) {

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
        HELP_C.init(blocklyWorkspace);
        INFO_C.init(blocklyWorkspace);
        CODE_C.init(blocklyWorkspace);
        SIM_C.init(blocklyWorkspace);
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
                wheel : false,
                startScale : 1.0,
                maxScale : 4,
                minScale : .25,
                scaleSpeed : 1.1
            },
            checkInTask : [ 'start', '_def', 'event' ],
            variableDeclaration : true,
            robControls : true
        });
        blocklyWorkspace.setDevice(GUISTATE_C.getRobotGroup());
        //TODO: add the version information in the Parent POM!.
        blocklyWorkspace.setVersion('2.0');
        GUISTATE_C.setBlocklyWorkspace(blocklyWorkspace);
        blocklyWorkspace.robControls.disable('saveProgram');
        blocklyWorkspace.robControls.refreshTooltips(GUISTATE_C.getRobotRealName());
        GUISTATE_C.checkSim();
        var toolbox = $('#blocklyDiv .blocklyToolboxDiv');
        toolbox.prepend('<ul class="nav nav-tabs levelTabs"><li class="active"><a id="beginner" class="typcn typcn-media-stop-outline" href="#" data-toggle="tab"></a></li><li class=""><a id="expert" href="#" class="typcn typcn-star-outline" data-toggle="tab"></a></li></ul>');
    }

    function initEvents() {
        $('#sliderDiv').draggable({
            'axis' : 'x',
            'cursor' : 'col-resize'
        });
        $('#tabProgram').on('show.bs.tab', function(e) {
        	console.log('program show');
            GUISTATE_C.setView('tabProgram');
            blocklyWorkspace.markFocused();
        });

        $('#tabProgram').onWrap('shown.bs.tab', function(e) {
            blocklyWorkspace.setVisible(true);
            reloadProgram();
            e.stopPropagation();
        }, 'tabProgram clicked');

        $('#tabProgram').on('hide.bs.tab', function(e) {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            GUISTATE_C.setProgramXML(xml);
        });

        // work around for touch devices
        $('#beginner, #expert').on('touchend', function(e) {
            $('#' + e.target.id).trigger('click');
        });

        $('.levelTabs a').on('shown.bs.tab', function(e) {
            e.preventDefault();
            loadToolbox(e.target.id);
            e.stopPropagation();
            LOG.info('toolbox clicked, switched to ' + e.target.id);
        });

        bindControl();
        blocklyWorkspace.addChangeListener(function(event) {
            if (listenToBlocklyEvents && event.type != Blockly.Events.UI && GUISTATE_C.isProgramSaved()) {
                GUISTATE_C.setProgramSaved(false);
            }
            if (event.type === Blockly.Events.DELETE) {
                if (blocklyWorkspace.getAllBlocks().length === 0) {
                    newProgram(true);
                }
            }
            $('.selectedHelp').removeClass('selectedHelp');
            if (Blockly.selected && $('#blocklyDiv').hasClass('rightActive')) {
                var block = Blockly.selected.type;
                $('#' + block).addClass('selectedHelp');
                $('#helpContent').scrollTo('#' + block, 1000, {
                    offset : -10,
                });
            }
            return false;
        });
    }

    /**
     * Save program to server
     */
    function saveToServer() {
        $('.modal').modal('hide'); // close all opened popups
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlProgramText = Blockly.Xml.domToText(xmlProgram);
        var configName = GUISTATE_C.isConfigurationStandard() ? null : GUISTATE_C.getConfigurationName();
        var xmlConfigText = GUISTATE_C.isConfigurationSaved() ? null : GUISTATE_C.getConfigurationXML();

        PROGRAM.saveProgramToServer(GUISTATE_C.getProgramName(), xmlProgramText, configName, xmlConfigText,
        		GUISTATE_C.getProgramShared() ? true : false, GUISTATE_C.getProgramTimestamp(),
        		function(result) {
		            if (result.rc === 'ok') {
		                GUISTATE_C.setProgramTimestamp(result.lastChanged);
		                GUISTATE_C.setProgramSaved(true);
		                GUISTATE_C.setConfigurationSaved(true);
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
            var progName = $('#singleModalInput').val().trim();
            var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgramText = Blockly.Xml.domToText(xmlProgram);
            var configName = GUISTATE_C.getConfigurationName();
            var xmlConfigText = GUISTATE_C.getConfigurationXML();

            LOG.info('saveAs program ' + GUISTATE_C.getProgramName());
            PROGRAM.saveAsProgramToServer(progName, xmlProgramText, configName, xmlConfigText, GUISTATE_C.getProgramTimestamp(),
            	function(result) {
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
        PROGRAM.loadProgramFromListing(program[0], program[1], program[3], function(result) {
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
                GUISTATE_C.setProgramXML(result.programText);
                if (result.configName === null) {
                	if (result.configText === null) {
                		GUISTATE_C.setConfigurationXML(result.configText);               	
                	} else {
                		GUISTATE_C.setConfigurationXML(result.configText);               	                		
                	}
                } else {
                	GUISTATE_C.setConfigurationName(result.configName);
                	GUISTATE_C.setConfigurationXML(result.configText);	
                }
                if (!(result.configText === null)) {
                	GUISTATE_C.setConfigurationXML(result.configText);               	
                }
                $('#tabProgram').trigger('click');
            }
            MSG.displayInformation(result, "", result.message);
        });
    }
    exports.loadFromListing = loadFromListing;

    /**
     * Load the program that was selected in gallery list
     */
    function loadFromGallery(program) {
        var programName = program[1];
        var user = program[3];
        var robotGroup = program[0];
        var robotType;
        if (robotGroup === GUISTATE_C.getRobotGroup()) {
            robotType = GUISTATE_C.getRobot();
        } else {
            robotType = GUISTATE_C.findRobot(robotGroup);
        }
        var owner = 'Gallery';
        function loadProgramFromGallery() {
            PROGRAM.loadProgramFromListing(programName, owner, user, function(result) {
                if (result.rc === 'ok') {
                    result.programShared = 'READ';
                    result.name = programName;
                    GUISTATE_C.setProgram(result, owner, user);
                    GUISTATE_C.setProgramXML(result.programText);
                    $('#tabProgram').trigger('click');
                }
                MSG.displayInformation(result, "", result.message);
            });
        }
        ROBOT_C.switchRobot(robotType, null, loadProgramFromGallery);
    }
    exports.loadFromGallery = loadFromGallery;

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
        function loadNewProgram() {
            var result = {};
            result.rc = 'ok';
            result.name = "NEPOprog"
            result.programShared = false;
            result.lastChanged = '';
            GUISTATE_C.setProgram(result);
            initProgramEnvironment();
        }
        if (further || GUISTATE_C.isProgramSaved()) {
            loadNewProgram();
        } else {
            confirmLoadProgram(loadNewProgram);
        }
    }
    exports.newProgram = newProgram;

    function confirmLoadProgram(callback) {
        $('#confirmContinue').removeData();
        $('#confirmContinue').data('type', 'program');
        $('#confirmContinue').data('callback', callback);
        if (GUISTATE_C.isUserLoggedIn()) {
            MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
        } else {
            MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
        }
    }

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
            $('#codeContent').html('<pre class="prettyprint linenums">' + prettyPrintOne(result.sourceCode.escapeHTML(), null, true) + '</pre>');
            // TODO change javaSource to source on server
            GUISTATE_C.setProgramSource(result.sourceCode);
            GUISTATE_C.setProgramFileExtension(result.fileExtension);
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
        input.change(function(event) {
            var file = event.target.files[0]
            var reader = new FileReader()
            reader.readAsText(file)
            reader.onload = function(event) {
                var name = UTIL.getBasename(file.name);
                loadProgramFromXML(name, event.target.result);
            }
        })
        input.trigger('click'); // opening dialog
    }
    exports.importXml = importXml;

    function linkProgram() {
        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xml = Blockly.Xml.domToText(dom);
        var link = 'https://lab.open-roberta.org/#loadProgram';
        link += '&' + GUISTATE_C.getRobot();
        link += '&' + GUISTATE_C.getProgramName();
        link += '&' + xml;
        link = encodeURI(link);
        var $temp = $("<input>");
        $("body").append($temp);
        $temp.val(link).select();
        document.execCommand("copy");
        $temp.remove();
        var displayLink = '</br><textarea readonly style="width:100%;" type="text">' + link + '</textarea>';
        MSG.displayMessage('POPUP_GET_LINK', 'POPUP', displayLink);
    }
    exports.linkProgram = linkProgram;

    function openProgramFromXML(target) {
        var robotType = target[1];
        var programName = target[2];
        var programXml = target[3];
        ROBOT_C.switchRobot(robotType, true, function() {
            loadProgramFromXML(programName, programXml);
        });
    }
    exports.openProgramFromXML = openProgramFromXML;

    function loadProgramFromXML(name, xml) {
        PROGRAM.loadProgramFromXML(name, xml, function(result) {
            if (result.rc == "ok") {
                // save the old program that it can be restored
                var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
                var xmlOld = Blockly.Xml.domToText(dom);
                GUISTATE_C.setProgramXML(xmlOld);
                // on server side we only test case insensitive block names, displaying xml can still fail:
                try {
                    result.programSaved = false;
                    result.name = 'NEPOprog';
                    result.programShared = false;
                    result.programTimestamp = '';
                    programToBlocklyWorkspace(result.programText);
                    GUISTATE_C.setProgram(result);
                    GUISTATE_C.setProgramXML(result.programText);
                    GUISTATE_C.setConfigurationName('');
                    GUISTATE_C.setConfigurationXML(result.configText);	
                     
                    LOG.info('show program ' + GUISTATE_C.getProgramName());
                } catch (e) {
                    // restore old Program
                    reloadProgram();
                    result.rc = "error";
                    MSG.displayInformation(result, "", Blockly.Msg.ORA_PROGRAM_IMPORT_ERROR, result.name);
                }
            } else {
                MSG.displayInformation(result, "", result.message, "");
            }
        });
    }

    /**
     * Create a file from the blocks and download it.
     */
    function exportXml() {
        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xml = '<export xmlns="http://de.fhg.iais.roberta.blockly"><program>' + Blockly.Xml.domToText(dom) + '</program><config>' + GUISTATE_C.getConfigurationXML() + '</config></export>';
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
        } else if (GUISTATE_C.robotState === 'busy' && GUISTATE_C.getConnection() === 'token') {
            MSG.displayMessage("POPUP_ROBOT_BUSY", "POPUP", "");
            return;
        }
        LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick');
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var xmlTextConfiguration = GUISTATE_C.getConfigurationXML();
        var connectionType = GUISTATE_C.getConnectionTypeEnum();
        switch (GUISTATE_C.getConnection()) {
        case connectionType.TOKEN:
            PROGRAM.runOnBrick(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
                RUN_C.runForToken(result);
            });
            break;
        case connectionType.AUTO:
            GUISTATE_C.setAutoConnectedBusy(true);
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
                RUN_C.runForAutoConnection(result);
            });
            break;
        case connectionType.AGENT:
            $('#menuRunProg').parent().addClass('disabled');
            GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
                RUN_C.runForAgentConnection(result);
            });
            break;
        case connectionType.AGENTORTOKEN:
            if (GUISTATE_C.getIsAgent() == true) {
                PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
                    RUN_C.runForAgentConnection(result);
                });
            } else {
                PROGRAM.runOnBrick(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
                    RUN_C.runForToken(result);
                });
            }
            break;
        default:
            break;
        }
    }
    exports.runOnBrick = runOnBrick;

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
        Blockly.bindEvent_(blocklyWorkspace.robControls.saveProgram, 'mousedown', null, function(e) {
            LOG.info('saveProgram from blockly button');
            saveToServer();
            return false;
        });
        blocklyWorkspace.robControls.disable('saveProgram');
        if (GUISTATE_C.getConnection() == 'token') {
            blocklyWorkspace.robControls.disable('runOnBrick');
        }
    }

    function reloadProgram(opt_result) {
        if (opt_result) {
            program = opt_result.data;
        } else {
            program = GUISTATE_C.getProgramXML();
        }
        programToBlocklyWorkspace(program);
    }
    exports.reloadProgram = reloadProgram;

    function reloadView() {
        if (isVisible()) {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            programToBlocklyWorkspace(xml);
            INFO_C.init(blocklyWorkspace);
        }
        var toolbox = GUISTATE_C.getProgramToolbox();
        blocklyWorkspace.updateToolbox(toolbox);
    }

    exports.reloadView = reloadView;

    function resetView() {
        blocklyWorkspace.setDevice(GUISTATE_C.getRobotGroup());
        //TODO: add the version information in the Parent POM!.
        blocklyWorkspace.setVersion('2.0');
        initProgramEnvironment();
        var toolbox = GUISTATE_C.getProgramToolbox();
        blocklyWorkspace.updateToolbox(toolbox);
        HELP_C.init(blocklyWorkspace);
        INFO_C.init(blocklyWorkspace);
    }
    exports.resetView = resetView;

    function loadToolbox(level) {
        Blockly.hideChaff();
        GUISTATE_C.setProgramToolboxLevel(level);
        var xml = GUISTATE_C.getToolbox(level);
        if (xml) {
            blocklyWorkspace.updateToolbox(xml);
        }
        if (toolbox === 'beginner') {
            $('.help.expert').hide();
        } else {
            $('.help.expert').show();
        }
    }
    exports.loadToolbox = loadToolbox;

    function isVisible() {
        return GUISTATE_C.getView() == 'tabProgram';
    }

    function programToBlocklyWorkspace(xml) {
        listenToBlocklyEvents = false;
        Blockly.hideChaff();
        blocklyWorkspace.clear();
        var dom = Blockly.Xml.textToDom(xml, blocklyWorkspace);
        Blockly.Xml.domToWorkspace(dom, blocklyWorkspace);
        // update right panel if it is already open
        if ($('.fromRight').hasClass('rightActive')) {
            $('#infoContent').html(blocklyWorkspace.description);
            var tmpTags = blocklyWorkspace.tags;
            $('#infoTags').tagsinput('removeAll');
            $('#infoTags').tagsinput('add', tmpTags);
            var xmlConfiguration = GUISTATE_C.getConfigurationXML();
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgram = Blockly.Xml.domToText(dom);
            var xmlConfiguration = GUISTATE_C.getConfigurationXML();
            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlProgram, xmlConfiguration, function(result) {
                $('#codeContent').html('<pre class="prettyprint linenums">' + prettyPrintOne(result.sourceCode.escapeHTML(), null, true) + '</pre>');
            });
        }
        setTimeout(function() {
            listenToBlocklyEvents = true;
        }, 500);
    }
});
