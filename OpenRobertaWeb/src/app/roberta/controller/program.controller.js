import * as MSG from 'message';
import * as LOG from 'log';
import * as UTIL from 'util';
import * as GUISTATE_C from 'guiState.controller';
import * as ROBOT_C from 'robot.controller';
import * as NN_C from 'nn.controller';
import * as PROGRAM from 'program.model';
import * as USER from 'user.model';
import * as CONFIGURATION_C from 'configuration.controller';
import * as PROGCODE_C from 'progCode.controller';
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'jquery-validate';

var $formSingleModal;

var blocklyWorkspace;
var listenToBlocklyEvents = true;
var seen = true;

export const SSID = '';
export const password = '';

/**
 * Inject Blockly with initial toolbox
 */
function init() {
    initView();
    initProgramEnvironment();
    initEvents();
    initProgramForms();
}

function initView() {
    var toolbox = GUISTATE_C.getProgramToolbox();
    blocklyWorkspace = Blockly.inject(document.getElementById('blocklyDiv'), {
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
            minScale: 0.25,
            scaleSpeed: 1.1,
        },
        checkInTask: ['start', '_def', 'event'],
        variableDeclaration: true,
        robControls: true,
        theme: GUISTATE_C.getTheme(),
    });
    $(window).resize();
    blocklyWorkspace.setDevice({
        group: GUISTATE_C.getRobotGroup(),
        robot: GUISTATE_C.getRobot(),
    });
    GUISTATE_C.setBlocklyWorkspace(blocklyWorkspace);
    blocklyWorkspace.robControls.disable('saveProgram');
    blocklyWorkspace.robControls.refreshTooltips(GUISTATE_C.getRobotRealName());
    GUISTATE_C.checkSim();
    var toolbox = $('#blockly .blocklyToolboxDiv');
    toolbox.prepend(
        '<ul class="nav nav-tabs levelTabs"><li class="active"><a class="typcn typcn-media-stop-outline" href="#beginner" data-toggle="tab">1</a></li><li class=""><a href="#expert" class="typcn typcn-star-outline" data-toggle="tab">2</a></li></ul>'
    );
}

function initEvents() {
    $('#sliderDiv').draggable({
        axis: 'x',
        cursor: 'col-resize',
    });
    $('#tabProgram').onWrap('click', function (e) {
        e.preventDefault();
        if (
            GUISTATE_C.getView() === 'tabConfiguration' &&
            GUISTATE_C.isUserLoggedIn() &&
            !GUISTATE_C.isConfigurationSaved() &&
            !GUISTATE_C.isConfigurationAnonymous()
        ) {
            $('#show-message-confirm').oneWrap('shown.bs.modal', function (e) {
                $('#confirm').off();
                $('#confirm').on('click', function (e) {
                    e.preventDefault();
                    // TODO, check if we want to give the user the opportunity to convert the named configuration into an anonymous one
                    GUISTATE_C.setConfigurationName('');
                    // or reset to last saved version:
                    //$('#tabConfiguration').trigger('reload');
                    $('#tabProgram').tabWrapShow();
                });
                $('#confirmCancel').off();
                $('#confirmCancel').on('click', function (e) {
                    e.preventDefault();
                    $('.modal').modal('hide');
                });
            });
            MSG.displayMessage('POPUP_CONFIGURATION_UNSAVED', 'POPUP', '', true);
            return false;
        } else {
            $('#tabProgram').tabWrapShow();
        }
    });
    $('#tabProgram').onWrap('show.bs.tab', function (e) {
        GUISTATE_C.setView('tabProgram');
    });

    $('#tabProgram').onWrap('shown.bs.tab', function (e) {
        blocklyWorkspace.markFocused();
        blocklyWorkspace.setVisible(true);
        if (!seen) {
            // TODO may need to be removed if program tab can receive changes while in background
            reloadView();
        }
        $(window).resize();
    });
    $('#tabProgram').onWrap('hide.bs.tab', function (e) {
        seen = false;
    });
    $('#tabProgram').onWrap('hidden.bs.tab', function (e) {
        blocklyWorkspace.setVisible(false);
    });

    // work around for touch devices
    $('.levelTabs').on('touchend', function (e) {
        var target = $(e.target).attr('href');
        $('.levelTabs a[href="' + target + '"]').tabWrapShow();
    });

    $('.levelTabs a[data-toggle="tab"]').onWrap('shown.bs.tab', function (e) {
        var target = $(e.target).attr('href').substring(1); // activated tab
        e.preventDefault();
        loadToolbox(target);
        e.stopPropagation();
        LOG.info('toolbox clicked, switched to ' + target);
    });

    bindControl();
    blocklyWorkspace.addChangeListener(function (event) {
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
                offset: -10,
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

    PROGRAM.saveProgramToServer(
        GUISTATE_C.getProgramName(),
        GUISTATE_C.getProgramOwnerName(),
        xmlProgramText,
        configName,
        xmlConfigText,
        GUISTATE_C.getProgramTimestamp(),
        function (result) {
            if (result.rc === 'ok') {
                GUISTATE_C.setProgramTimestamp(result.lastChanged);
                GUISTATE_C.setProgramSaved(true);
                GUISTATE_C.setConfigurationSaved(true);
                LOG.info('save program ' + GUISTATE_C.getProgramName());
            }
            MSG.displayInformation(result, 'MESSAGE_EDIT_SAVE_PROGRAM', result.message, GUISTATE_C.getProgramName());
        }
    );
}

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
        PROGRAM.saveAsProgramToServer(
            progName,
            userAccountName,
            xmlProgramText,
            configName,
            xmlConfigText,
            GUISTATE_C.getProgramTimestamp(),
            function (result) {
                if (result.rc === 'ok') {
                    LOG.info('saved program ' + GUISTATE_C.getProgramName() + ' as ' + progName);
                    result.name = progName;
                    result.programShared = false;
                    GUISTATE_C.setProgram(result, userAccountName, userAccountName);
                    MSG.displayInformation(result, 'MESSAGE_EDIT_SAVE_PROGRAM_AS', result.message, GUISTATE_C.getProgramName());
                } else {
                    if (result.cause === 'ORA_PROGRAM_SAVE_AS_ERROR_PROGRAM_EXISTS') {
                        //show replace option
                        //get last changed of program to overwrite
                        var lastChanged = result.lastChanged;
                        var modalMessage =
                            Blockly.Msg.POPUP_BACKGROUND_REPLACE || 'A program with the same name already exists! <br> Would you like to replace it?';
                        $('#show-message-confirm').oneWrap('shown.bs.modal', function (e) {
                            $('#confirm').off();
                            $('#confirm').onWrap(
                                'click',
                                function (e) {
                                    e.preventDefault();
                                    PROGRAM.saveProgramToServer(
                                        progName,
                                        userAccountName,
                                        xmlProgramText,
                                        configName,
                                        xmlConfigText,
                                        lastChanged,
                                        function (result) {
                                            if (result.rc === 'ok') {
                                                LOG.info('saved program ' + GUISTATE_C.getProgramName() + ' as ' + progName + ' and overwrote old content');
                                                result.name = progName;
                                                GUISTATE_C.setProgram(result, userAccountName, userAccountName);
                                                MSG.displayInformation(result, 'MESSAGE_EDIT_SAVE_PROGRAM_AS', result.message, GUISTATE_C.getProgramName());
                                            } else {
                                                LOG.info('failed to overwrite ' + progName);
                                                MSG.displayMessage(result.message, 'POPUP', '');
                                            }
                                        }
                                    );
                                },
                                'confirm modal'
                            );
                            $('#confirmCancel').off();
                            $('#confirmCancel').onWrap(
                                'click',
                                function (e) {
                                    e.preventDefault();
                                    $('.modal').modal('hide');
                                },
                                'cancel modal'
                            );
                        });
                        MSG.displayPopupMessage('ORA_PROGRAM_SAVE_AS_ERROR_PROGRAM_EXISTS', modalMessage, Blockly.Msg.POPUP_REPLACE, Blockly.Msg.POPUP_CANCEL);
                    }
                }
            }
        );
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
        PROGRAM.loadProgramFromListing(programName, owner, user, function (result) {
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
                $('#tabProgram').oneWrap('shown.bs.tab', function (e) {
                    CONFIGURATION_C.reloadConf();
                    reloadProgram();
                });
                $('#tabProgram').clickWrap();
            }
            MSG.displayInformation(result, '', result.message);
        });
    }
    ROBOT_C.switchRobot(robotType, null, loadProgramFromGallery);
}

function initProgramForms() {
    $formSingleModal = $('#single-modal-form');
    $('#buttonCancelFirmwareUpdateAndRun').onWrap(
        'click',
        function () {
            start();
        },
        'cancel firmware update and run'
    );
}

function showSaveAsModal() {
    $.validator.addMethod(
        'regex',
        function (value, element, regexp) {
            value = value.trim();
            return value.match(regexp);
        },
        'No special Characters allowed here. Use only upper and lowercase letters (A through Z; a through z) and numbers.'
    );

    UTIL.showSingleModal(
        function () {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg['MENU_SAVE_AS']);
            $('#single-modal label').text(Blockly.Msg['POPUP_NAME']);
        },
        saveAsProgramToServer,
        function () {},
        {
            rules: {
                singleModalInput: {
                    required: true,
                    regex: /^[a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]{0,254}$/,
                },
            },
            errorClass: 'form-invalid',
            errorPlacement: function (label, element) {
                label.insertAfter(element);
            },
            messages: {
                singleModalInput: {
                    required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                    regex: Blockly.Msg['MESSAGE_INVALID_NAME'],
                },
            },
        }
    );
}

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

/**
 * New program
 */
function newProgram(opt_further) {
    var further = opt_further || false;
    function loadNewProgram() {
        var result = {};
        result.rc = 'ok';
        result.name = 'NEPOprog';
        result.programShared = false;
        result.lastChanged = '';
        GUISTATE_C.setProgram(result);
        initProgramEnvironment();
        NN_C.programWasReplaced();
        LOG.info('ProgramNew');
    }
    if (further || GUISTATE_C.isProgramSaved()) {
        loadNewProgram();
    } else {
        confirmLoadProgram();
    }
}

function confirmLoadProgram() {
    $('#show-message-confirm').oneWrap('shown.bs.modal', function (e) {
        $('#confirm').off();
        $('#confirm').on('click', function (e) {
            e.preventDefault();
            newProgram(true);
        });
        $('#confirmCancel').off();
        $('#confirmCancel').on('click', function (e) {
            e.preventDefault();
            $('.modal').modal('hide');
        });
    });
    if (GUISTATE_C.isUserLoggedIn()) {
        MSG.displayMessage('POPUP_BEFOREUNLOAD_LOGGEDIN', 'POPUP', '', true);
    } else {
        MSG.displayMessage('POPUP_BEFOREUNLOAD', 'POPUP', '', true);
    }
}

function linkProgram() {
    var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
    var xml = Blockly.Xml.domToText(dom);
    //TODO this should be removed after the next release
    xml = '<export xmlns="http://de.fhg.iais.roberta.blockly"><program>' + xml + '</program><config>' + GUISTATE_C.getConfigurationXML() + '</config></export>';
    var link = 'https://lab.open-roberta.org/#loadProgram';
    link += '&&' + GUISTATE_C.getRobot();
    link += '&&' + GUISTATE_C.getProgramName();
    link += '&&' + xml;
    link = encodeURI(link);
    var $temp = $('<input>');
    $('body').append($temp);
    $temp.val(link).select();
    document.execCommand('copy');
    $temp.remove();
    var displayLink = '</br><textarea readonly style="width:100%;" type="text">' + link + '</textarea>';
    LOG.info('ProgramLinkShare');
    MSG.displayMessage('POPUP_GET_LINK', 'POPUP', displayLink);
}

/**
 * Create a file from the blocks and download it.
 */
function exportXml() {
    var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
    var xml =
        '<export xmlns="http://de.fhg.iais.roberta.blockly"><program>' +
        Blockly.Xml.domToText(dom) +
        '</program><config>' +
        GUISTATE_C.getConfigurationXML() +
        '</config></export>';
    LOG.info('ProgramExport');
    UTIL.download(GUISTATE_C.getProgramName() + '.xml', xml);
    MSG.displayMessage('MENU_MESSAGE_DOWNLOAD', 'TOAST', GUISTATE_C.getProgramName());
}

/**
 * Download all programs by the current User
 */
function exportAllXml() {
    USER.userLoggedInCheck(function (result) {
        if (result.rc === 'ok') {
            PROGRAM.exportAllProgramsXml();
        } else {
            MSG.displayMessage(result.cause, 'TOAST', 'Log in check failed for Export');
        }
    });
}

function getBlocklyWorkspace() {
    return blocklyWorkspace;
}

function bindControl() {
    Blockly.bindEvent_(blocklyWorkspace.robControls.saveProgram, 'mousedown', null, function (e) {
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

function resetView() {
    blocklyWorkspace.setDevice({
        group: GUISTATE_C.getRobotGroup(),
        robot: GUISTATE_C.getRobot(),
    });
    initProgramEnvironment();
    var toolbox = GUISTATE_C.getProgramToolbox();
    blocklyWorkspace.updateToolbox(toolbox);
}

function loadToolbox(level) {
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

function loadExternalToolbox(toolbox) {
    if (toolbox) {
        blocklyWorkspace.updateToolbox(toolbox);
    }
}

function isVisible() {
    return GUISTATE_C.getView() == 'tabProgram';
}

function programToBlocklyWorkspace(xml, opt_fromShowSource) {
    if (!xml) {
        return;
    }
    listenToBlocklyEvents = false;
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
    if ($('#codeDiv').hasClass('rightActive') && opt_fromShowSource) {
        PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, language, SSID, password, function (result) {
            PROGCODE_C.setCode(result.sourceCode);
        });
    }
    setTimeout(function () {
        listenToBlocklyEvents = true;
    }, 500);
}
export {
    init,
    saveToServer,
    loadFromGallery,
    initProgramForms,
    showSaveAsModal,
    initProgramEnvironment,
    newProgram,
    linkProgram,
    exportXml,
    exportAllXml,
    getBlocklyWorkspace,
    reloadProgram,
    reloadView,
    resetView,
    loadToolbox,
    loadExternalToolbox,
    programToBlocklyWorkspace,
};
