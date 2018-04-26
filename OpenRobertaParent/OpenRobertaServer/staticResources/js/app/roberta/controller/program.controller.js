define([ 'exports', 'comm', 'message', 'log', 'util', 'guiState.controller', 'program.model', 'robot.controller', 'prettify', 'blocks', 'jquery',
        'jquery-validate', 'blocks-msg' ], function(exports, COMM, MSG, LOG, UTIL, GUISTATE_C, PROGRAM, ROBOT_C, Prettify, Blockly, $) {

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
        $(window).resize();
        blocklyWorkspace.setDevice(GUISTATE_C.getRobotGroup());
        //TODO: add the version information in the Parent POM!.
        blocklyWorkspace.setVersion('2.0');
        GUISTATE_C.setBlocklyWorkspace(blocklyWorkspace);
        blocklyWorkspace.robControls.disable('saveProgram');
        blocklyWorkspace.robControls.refreshTooltips(GUISTATE_C.getRobotRealName());
        GUISTATE_C.checkSim();
        var toolbox = $('#blockly .blocklyToolboxDiv');
        toolbox.prepend('<ul class="nav nav-tabs levelTabs"><li class="active"><a class="typcn typcn-media-stop-outline" href="#beginner" data-toggle="tab">1</a></li><li class=""><a href="#expert" class="typcn typcn-star-outline" data-toggle="tab">2</a></li></ul>');
    }

    function initEvents() {
        $('#sliderDiv').draggable({
            'axis' : 'x',
            'cursor' : 'col-resize'
        });
        $('#tabProgram').on('click', function(e) {
            e.preventDefault();
            if (GUISTATE_C.getView() === 'tabConfiguration' && GUISTATE_C.isUserLoggedIn() && !GUISTATE_C.isConfigurationSaved()
                    && !GUISTATE_C.isConfigurationAnonymous()) {
                $('#show-message-confirm').one('shown.bs.modal', function(e) {
                    $('#confirm').off();
                    $('#confirm').on('click', function(e) {
                        e.preventDefault();
                        // TODO, check if we want to give the user the opportunity to convert the named configuration into an anonymous one
                        GUISTATE_C.setConfigurationName('');
                        // or reset to last saved version:
                        //$('#tabConfiguration').trigger('reload');
                        $('#tabProgram').tab('show');
                    });
                    $('#confirmCancel').off();
                    $('#confirmCancel').on('click', function(e) {
                        e.preventDefault();
                        $('.modal').modal('hide');
                    });
                });
                MSG.displayMessage("POPUP_CONFIGURATION_UNSAVED", "POPUP", "", true);
                return false;
            } else {
                $('#tabProgram').tab('show');
            }
        });
        $('#tabProgram').on('show.bs.tab', function(e) {
            GUISTATE_C.setView('tabProgram');
            blocklyWorkspace.markFocused();
        });

        $('#tabProgram').on('shown.bs.tab', function(e) {
            $(window).resize();
        });

        // work around for touch devices
        $('.levelTabs').on('touchend', function(e) {
            var target = $(e.target).attr("href");
            $('.levelTabs a[href="' + target + '"]').tab('show');
        });

        $('.levelTabs a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
            var target = $(e.target).attr("href").substring(1); // activated tab
            e.preventDefault();
            loadToolbox(target);
            e.stopPropagation();
            LOG.info('toolbox clicked, switched to ' + target);
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
            if (Blockly.selected && $('#blockly').hasClass('rightActive')) {
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
        var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

        PROGRAM.saveProgramToServer(GUISTATE_C.getProgramName(), xmlProgramText, configName, xmlConfigText, GUISTATE_C.getProgramShared() ? true : false, GUISTATE_C.getProgramTimestamp(), function(
                result) {
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
            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

            LOG.info('saveAs program ' + GUISTATE_C.getProgramName());
            PROGRAM.saveAsProgramToServer(progName, xmlProgramText, configName, xmlConfigText, GUISTATE_C.getProgramTimestamp(), function(result) {
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

                if (result.configName === undefined) {
                    if (result.configText === undefined) {
                        GUISTATE_C.setConfigurationNameDefault();
                        GUISTATE_C.setConfigurationXML(GUISTATE_C.getConfigurationConf());
                    } else {
                        GUISTATE_C.setConfigurationName('');
                        GUISTATE_C.setConfigurationXML(result.configText);
                    }
                } else {
                    GUISTATE_C.setConfigurationName(result.configName);
                    GUISTATE_C.setConfigurationXML(result.configText);
                }
                $('#tabProgram').one('shown.bs.tab', function(e) {
                    reloadProgram();
                });
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
//                    GUISTATE_C.setConfigurationName('');
//                    GUISTATE_C.setConfigurationXML(result.configText);
                    if (result.configName === undefined) {
                        if (result.configText === undefined) {
                            GUISTATE_C.setConfigurationNameDefault();
                            GUISTATE_C.setConfigurationXML(GUISTATE_C.getConfigurationConf());
                        } else {
                            GUISTATE_C.setConfigurationName('');
                            GUISTATE_C.setConfigurationXML(result.configText);
                        }
                    } else {
                        GUISTATE_C.setConfigurationName(result.configName);
                        GUISTATE_C.setConfigurationXML(result.configText);
                    }
                    $('#tabProgram').one('shown.bs.tab', function(e) {
                        reloadProgram();
                    });
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
            confirmLoadProgram();
        }
    }
    exports.newProgram = newProgram;

    function confirmLoadProgram() {
        $('#show-message-confirm').one('shown.bs.modal', function(e) {
            $('#confirm').off();
            $('#confirm').on('click', function(e) {
                e.preventDefault();
                newProgram(true);
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

    /**
     * two Experimental functions: Open a file select dialog to load source code
     * from local disk and send it to the cross compiler
     */
    function importSourceCodeToCompile() {
        var input = $(document.createElement('input'));
        input.attr("type", "file");
        input.change(function(event) {
            var file = event.target.files[0]
            var reader = new FileReader()
            reader.readAsText(file)
            reader.onload = function(event) {
                // TODO move this to the run controller once it is clear what should happen
                var name = UTIL.getBasename(file.name);
                PROGRAM.compileN(name, event.target.result, GUISTATE_C.getLanguage(), function(result) {
                    alert(result.rc);
                });
            }
        })
        input.trigger('click'); // opening dialog
    }
    exports.importSourceCodeToCompile = importSourceCodeToCompile;

    /**
     * two Experimental functions: Open a file select dialog to load source code
     * from local disk and send it to the cross compiler
     */
    function importNepoCodeToCompile() {
        var input = $(document.createElement('input'));
        input.attr("type", "file");
        input.change(function(event) {
            var file = event.target.files[0]
            var reader = new FileReader()
            reader.readAsText(file)
            reader.onload = function(event) {
                // TODO move this to the run controller once it is clear what should happen
                var name = UTIL.getBasename(file.name);
                PROGRAM.compileP(name, event.target.result, GUISTATE_C.getLanguage(), function(result) {
                    alert(result.rc);
                });
            }
        })
        input.trigger('click'); // opening dialog
    }
    exports.importNepoCodeToCompile = importNepoCodeToCompile;

    function linkProgram() {
        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xml = Blockly.Xml.domToText(dom);
        //TODO this should be removed after the next release
        xml = '<export xmlns="http://de.fhg.iais.roberta.blockly"><program>' + xml + '</program><config>' + GUISTATE_C.getConfigurationXML()
                + '</config></export>';
        var link = 'https://lab.open-roberta.org/#loadProgram';
        link += '&&' + GUISTATE_C.getRobot();
        link += '&&' + GUISTATE_C.getProgramName();
        link += '&&' + xml;
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
        if (xml.search("<export") === -1) {
            xml = '<export xmlns="http://de.fhg.iais.roberta.blockly"><program>' + xml + '</program><config>' + GUISTATE_C.getConfigurationXML()
                    + '</config></export>';
        }
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
        var xml = '<export xmlns="http://de.fhg.iais.roberta.blockly"><program>' + Blockly.Xml.domToText(dom) + '</program><config>'
                + GUISTATE_C.getConfigurationXML() + '</config></export>';
        UTIL.download(GUISTATE_C.getProgramName() + ".xml", xml);
        MSG.displayMessage("MENU_MESSAGE_DOWNLOAD", "TOAST", GUISTATE_C.getProgramName());
    }
    exports.exportXml = exportXml;

    function getBlocklyWorkspace() {
        return blocklyWorkspace;
    }

    exports.getBlocklyWorkspace = getBlocklyWorkspace;

    function bindControl() {
        Blockly.bindEvent_(blocklyWorkspace.robControls.saveProgram, 'mousedown', null, function(e) {
            LOG.info('saveProgram from blockly button');
            saveToServer();
            return false;
        });
        blocklyWorkspace.robControls.disable('saveProgram');
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
    }
    exports.resetView = resetView;

    function loadToolbox(level) {
        Blockly.hideChaff();
        GUISTATE_C.setProgramToolboxLevel(level);
        var xml = GUISTATE_C.getToolbox(level);
        if (xml) {
            blocklyWorkspace.updateToolbox(xml);
        }
        if (level === 'beginner') {
            $('.help.expert').hide();
        } else {
            $('.help.expert').show();
        }
    }
    exports.loadToolbox = loadToolbox;

    function loadExternalToolbox(toolbox) {
        Blockly.hideChaff();
        if (toolbox) {
            blocklyWorkspace.updateToolbox(toolbox);
        }
    }
    exports.loadExternalToolbox = loadExternalToolbox;

    function isVisible() {
        return GUISTATE_C.getView() == 'tabProgram';
    }

    function programToBlocklyWorkspace(xml) {
        listenToBlocklyEvents = false;
        Blockly.hideChaff();
        blocklyWorkspace.clear();
        var dom = Blockly.Xml.textToDom(xml, blocklyWorkspace);
        Blockly.Xml.domToWorkspace(dom, blocklyWorkspace);
        $('#infoContent').html(blocklyWorkspace.description);
        if (typeof blocklyWorkspace.description === 'string' && blocklyWorkspace.description.length) {
            $('#infoButton').addClass('notEmpty');
        } else {
            $('#infoButton').removeClass('notEmpty');
        }
        var tmpTags = blocklyWorkspace.tags;
        $('#infoTags').tagsinput('removeAll');
        $('.bootstrap-tagsinput input').attr('placeholder', 'Tags');
        $('#infoTags').tagsinput('add', tmpTags);
        var xmlConfiguration = GUISTATE_C.getConfigurationXML();
        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlProgram = Blockly.Xml.domToText(dom);

        var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

        var language = GUISTATE_C.getLanguage();
        if ($('#codeDiv').hasClass('rightActive')) {
            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, language, function(result) {
                $('#codeContent').html('<pre class="prettyprint linenums">' + prettyPrintOne(result.sourceCode.escapeHTML(), null, true) + '</pre>');
            });
        }
        setTimeout(function() {
            listenToBlocklyEvents = true;
        }, 500);
    }
});
