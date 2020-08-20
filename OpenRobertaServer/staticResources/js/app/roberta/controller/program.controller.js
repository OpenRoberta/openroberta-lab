define([ 'exports', 'comm', 'message', 'log', 'util', 'guiState.controller', 'robot.controller', 'program.model', 'configuration.controller', 'progCode.controller', 'blockly', 'jquery',
        'jquery-validate' ], function(exports, COMM, MSG, LOG, UTIL, GUISTATE_C, ROBOT_C, PROGRAM, CONFIGURATION_C, PROGCODE_C, Blockly, $) {

    var $formSingleModal;

    var blocklyWorkspace;
    var listenToBlocklyEvents = true;
    var seen = true;
    /**
     * Inject Blockly with initial toolbox
     */
    function init() {
        initView();
        initProgramEnvironment();
        initEvents();
        initProgramForms();
        exports.SSID = '';
        exports.password = '';
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
            robControls : true,
            theme : GUISTATE_C.getTheme()
        });
        $(window).resize();
        blocklyWorkspace.setDevice({
            group : GUISTATE_C.getRobotGroup(),
            robot : GUISTATE_C.getRobot()
        });
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
        });

        $('#tabProgram').on('shown.bs.tab', function(e) {
            blocklyWorkspace.markFocused();
            blocklyWorkspace.setVisible(true);
            if (!seen) { // TODO may need to be removed if program tab can recieve changes while in background
                reloadView();
            }
            $(window).resize();
        });
        $('#tabProgram').on('hide.bs.tab', function(e) {
            Blockly.hideChaff();
        });
        $('#tabProgram').on('hidden.bs.tab', function(e) {
            blocklyWorkspace.setVisible(false);
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

        PROGRAM.saveProgramToServer(GUISTATE_C.getProgramName(), GUISTATE_C.getProgramOwnerName(), xmlProgramText, configName, xmlConfigText, GUISTATE_C.getProgramTimestamp(), function(
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
            var userAccountName = GUISTATE_C.getUserAccountName();

            LOG.info('saveAs program ' + GUISTATE_C.getProgramName());
            PROGRAM.saveAsProgramToServer(progName, userAccountName, xmlProgramText, configName, xmlConfigText, GUISTATE_C.getProgramTimestamp(), function(result) {
                UTIL.response(result);
                if (result.rc === 'ok') {
                    result.name = progName;
                    result.programShared = false;
                    GUISTATE_C.setProgram(result, userAccountName, userAccountName);
                    MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_PROGRAM_AS", result.message, GUISTATE_C.getProgramName());
                }
            });
        }
    }

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
                    GUISTATE_C.setProgramXML(result.progXML);
//                    GUISTATE_C.setConfigurationName('');
//                    GUISTATE_C.setConfigurationXML(result.confXML);
                    if (result.configName === undefined) {
                        if (result.confXML === undefined) {
                            GUISTATE_C.setConfigurationNameDefault();
                            GUISTATE_C.setConfigurationXML(GUISTATE_C.getConfigurationConf());
                        } else {
                            GUISTATE_C.setConfigurationName('');
                            GUISTATE_C.setConfigurationXML(result.confXML);
                        }
                    } else {
                        GUISTATE_C.setConfigurationName(result.configName);
                        GUISTATE_C.setConfigurationXML(result.confXML);
                    }
                    $('#tabProgram').one('shown.bs.tab', function(e) {
                        CONFIGURATION_C.reloadConf();
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
                    regex : /^[a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]{0,254}$/
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
            LOG.info('ProgramNew');
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
        LOG.info('ProgramLinkShare');
        MSG.displayMessage('POPUP_GET_LINK', 'POPUP', displayLink);
    }
    exports.linkProgram = linkProgram;

    /**
     * Create a file from the blocks and download it.
     */
    function exportXml() {
        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xml = '<export xmlns="http://de.fhg.iais.roberta.blockly"><program>' + Blockly.Xml.domToText(dom) + '</program><config>'
                + GUISTATE_C.getConfigurationXML() + '</config></export>';
        LOG.info('ProgramExport');
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

    function reloadProgram(opt_result, opt_fromShowSource) {
        var program;
        if (opt_result) {
            program = opt_result.progXML;
            if (!$.isEmptyObject(opt_result.confAnnos)) {
                GUISTATE_C.confAnnos = opt_result.confAnnos;
                UTIL.alertTab('tabConfiguration');
            }
        } else {
            program = GUISTATE_C.getProgramXML();
        }
        programToBlocklyWorkspace(program, opt_fromShowSource);
    }
    exports.reloadProgram = reloadProgram;

    function reloadView() {
        if (isVisible()) {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            programToBlocklyWorkspace(xml);
            var toolbox = GUISTATE_C.getProgramToolbox();
            blocklyWorkspace.updateToolbox(toolbox);
            seen = true;
        } else {
            seen = false;
        }
    }

    exports.reloadView = reloadView;

    function resetView() {
        blocklyWorkspace.setDevice({
            group : GUISTATE_C.getRobotGroup(),
            robot : GUISTATE_C.getRobot()
        });
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

    function programToBlocklyWorkspace(xml, opt_fromShowSource) {
        if (!xml) {
            return;
        }
        listenToBlocklyEvents = false;
        Blockly.hideChaff();
        blocklyWorkspace.clear();
        var dom = Blockly.Xml.textToDom(xml, blocklyWorkspace);
        Blockly.Xml.domToWorkspace(dom, blocklyWorkspace);
        blocklyWorkspace.setVersion(dom.getAttribute('xmlversion'));
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
        if ($('#codeDiv').hasClass('rightActive') && !opt_fromShowSource) {
            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, language, function(result) {
                PROGCODE_C.setCode(result.sourceCode);
            });
        }
        setTimeout(function() {
            listenToBlocklyEvents = true;
        }, 500);
    }
    exports.programToBlocklyWorkspace = programToBlocklyWorkspace;
});
