import * as MSG from 'message';
import * as LOG from 'log';
import * as UTIL from 'util.roberta';
import * as GUISTATE_C from 'guiState.controller';
import * as ROBOT_C from 'robot.controller';
import * as NN_C from 'nn.controller';
import * as PROGRAM from 'program.model';
import * as USER from 'user.model';
import * as CONFIGURATION_C from 'configuration.controller';
//@ts-ignore
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'jquery-validate';
import * as ACE_EDITOR from 'aceEditor';

let $formSingleModal;

let blocklyWorkspace;
let listenToBlocklyEvents: boolean = true;
let seen: boolean = true;

let _SSID: string = '';
let _password: string = '';

export function setSSID(SSID: string): void {
    _SSID = SSID;
}
export function getSSID(): string {
    return _SSID;
}
export function setPassword(password: any): void {
    _password = password;
}
export function getPassword(): string {
    return _password;
}

/**
 * Inject Blockly with initial toolbox
 */
export function init(): void {
    initView();
    initProgramEnvironment();
    initEvents();
    initProgramForms();
}

function initView(): void {
    let toolbox = GUISTATE_C.getProgramToolbox();
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
            scaleSpeed: 1.1
        },
        variableDeclaration: true,
        robControls: true,
        theme: GUISTATE_C.getTheme()
    });
    $(window).resize();
    blocklyWorkspace.setDevice({
        group: GUISTATE_C.getRobotGroup(),
        robot: GUISTATE_C.getRobot()
    });
    GUISTATE_C.setBlocklyWorkspace(blocklyWorkspace);
    blocklyWorkspace.robControls.disable('saveProgram');
    blocklyWorkspace.robControls.refreshTooltips(GUISTATE_C.getRobotRealName());
    GUISTATE_C.checkSim();
    $('#program').find('.blocklyToolboxDiv:first').wrap('<div id=\'toolboxDiv\' style=\'position: absolute;\'></div>');
    $('#toolboxDiv').prepend(
        '<ul class="nav nav-tabs levelTabs"><li class="nav-item"><a class="nav-link typcn typcn-media-stop-outline active beginner" href="#beginner" data-bs-toggle="tab">1</a></li><li class="nav-item"><a href="#expert" class="nav-link typcn typcn-star-outline expert" data-bs-toggle="tab">2</a></li></ul>'
    );
}

function initEvents(): void {
    $('#sliderDiv').draggable({
        axis: 'x',
        cursor: 'col-resize'
    });
    $('#tabProgram').onWrap('click', function(e): boolean {
        e.preventDefault();
        if (
            GUISTATE_C.getView() === 'tabConfiguration' &&
            GUISTATE_C.isUserLoggedIn() &&
            !GUISTATE_C.isConfigurationSaved() &&
            !GUISTATE_C.isConfigurationAnonymous()
        ) {
            $('#show-message-confirm').oneWrap('shown.bs.modal', function(e): void {
                $('#confirm').off();
                $('#confirm').on('click', function(e): void {
                    e.preventDefault();
                    // TODO, check if we want to give the user the opportunity to convert the named configuration into an anonymous one
                    GUISTATE_C.setConfigurationName('');
                    // or reset to last saved version:
                    //$('#tabConfiguration').trigger('reload');
                    $('#tabProgram').tabWrapShow();
                });
                $('#confirmCancel').off();
                $('#confirmCancel').on('click', function(e): void {
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
    $('#tabProgram').onWrap('show.bs.tab', function(e): void {
        GUISTATE_C.setView('tabProgram');
    });

    $('#tabProgram').onWrap('shown.bs.tab', function(e): void {
        blocklyWorkspace.markFocused();
        blocklyWorkspace.setVisible(true);
        if (!seen) {
            // TODO may need to be removed if program tab can receive changes while in background
            reloadView();
        }
        $(window).resize();
    });
    $('#tabProgram').onWrap('hide.bs.tab', function(e): void {
        seen = false;
    });
    $('#tabProgram').onWrap('hidden.bs.tab', function(e): void {
        blocklyWorkspace.setVisible(false);
    });

    $('.expert, .beginner').onWrap('click', function(e): void {
        let target: string =
            ($(e.target).attr('href') && $(e.target).attr('href').substring(1)) ||
            ($(e.target.parentElement).attr('href') && $(e.target.parentElement).attr('href').substring(1)); // activated tab
        $('.levelTabs a[href="' + target + '"]').tabWrapShow();
        e.preventDefault();
        loadToolbox(target);
        e.stopPropagation();
        LOG.info('toolbox clicked, switched to ' + target);
    });

    bindControl();
    blocklyWorkspace.addChangeListener(function(event: Event): boolean {
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
            let block = Blockly.selected.type;
            $('#' + block).addClass('selectedHelp');
            //@ts-ignore
            $('#helpContent').scrollTo('#' + block, 1000, {
                offset: -10
            });
        }
        return false;
    });
}

/**
 * Save program to server
 */
export function saveToServer(): void {
    $('.modal').modal('hide'); // close all opened popups
    let xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
    let xmlProgramText = Blockly.Xml.domToText(xmlProgram);
    let isNamedConfig: boolean = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
    let configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
    let xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

    PROGRAM.saveProgramToServer(
        GUISTATE_C.getProgramName(),
        GUISTATE_C.getProgramOwnerName(),
        xmlProgramText,
        configName,
        xmlConfigText,
        GUISTATE_C.getProgramTimestamp(),
        function(result: any): void {
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
function saveAsProgramToServer(): void {
    $formSingleModal.validate();
    if ($formSingleModal.valid()) {
        $('.modal').modal('hide'); // close all opened popups
        //@ts-ignore
        let progName = $('#singleModalInput').val().trim();
        let xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        let xmlProgramText = Blockly.Xml.domToText(xmlProgram);
        let isNamedConfig: boolean = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        let configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        let xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
        let userAccountName = GUISTATE_C.getUserAccountName();

        LOG.info('saveAs program ' + GUISTATE_C.getProgramName());
        PROGRAM.saveAsProgramToServer(
            progName,
            userAccountName,
            xmlProgramText,
            configName,
            xmlConfigText,
            GUISTATE_C.getProgramTimestamp(),
            function(result: any): void {
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
                        let lastChanged = result.lastChanged;
                        let modalMessage =
                            Blockly.Msg.POPUP_BACKGROUND_REPLACE || 'A program with the same name already exists! <br> Would you like to replace it?';
                        $('#show-message-confirm').oneWrap('shown.bs.modal', function(e): void {
                            $('#confirm').off();
                            $('#confirm').onWrap(
                                'click',
                                function(e): void {
                                    e.preventDefault();
                                    PROGRAM.saveProgramToServer(
                                        progName,
                                        userAccountName,
                                        xmlProgramText,
                                        configName,
                                        xmlConfigText,
                                        lastChanged,
                                        function(result: any): void {
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
                                function(e) {
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
export function loadFromGallery(program: any[]): void {
    let programName = program[1];
    let user = program[3];
    let robotGroup = program[0];
    let robotType;
    if (robotGroup === GUISTATE_C.getRobotGroup()) {
        robotType = GUISTATE_C.getRobot();
    } else {
        robotType = GUISTATE_C.findRobot(robotGroup);
    }
    let owner: string = 'Gallery';
    function loadProgramFromGallery(): void {
        PROGRAM.loadProgramFromListing(programName, owner, user, function(result: any): void {
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
                $('#tabProgram').oneWrap('shown.bs.tab', function(e): void {
                    CONFIGURATION_C.reloadConf();
                    reloadProgram();
                });
                $('#tabProgram').tabWrapShow();
            }
            MSG.displayInformation(result, '', result.message);
        });
    }
    //TODO !!!!
    //@ts-ignore
    ROBOT_C.switchRobot(robotType, {}, false, loadProgramFromGallery);
}

export function initProgramForms(): void {
    $formSingleModal = $('#single-modal-form');
    $('#buttonCancelFirmwareUpdateAndRun').onWrap(
        'click',
        function(): void {
            //@ts-ignore
            start();
        },
        'cancel firmware update and run'
    );
}

export function showSaveAsModal(): void {
    $.validator.addMethod(
        'regex',
        function(value, element: HTMLElement, regexp) {
            value = value.trim();
            return value.match(regexp);
        },
        'No special Characters allowed here. Use only upper and lowercase letters (A through Z; a through z) and numbers.'
    );

    UTIL.showSingleModal(
        function(): void {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h5').text(Blockly.Msg['MENU_SAVE_AS']);
            $('#single-modal label').text(Blockly.Msg['POPUP_NAME']);
        },
        saveAsProgramToServer,
        function(): void {},
        {
            rules: {
                singleModalInput: {
                    required: true,
                    regex: /^[a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]{0,254}$/
                }
            },
            errorClass: 'form-invalid',
            errorPlacement: function(label, element): void {
                label.insertAfter(element);
            },
            messages: {
                singleModalInput: {
                    required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                    regex: Blockly.Msg['MESSAGE_INVALID_NAME']
                }
            }
        }
    );
}

export function initProgramEnvironment(): void {
    let x, y;
    if ($(window).width() < 768) {
        x = $(window).width() / 50;
        y = 25;
    } else {
        x = $(window).width() / 5;
        y = 50;
    }
    let program = GUISTATE_C.getProgramProg();
    programToBlocklyWorkspace(program);
    let blocks = blocklyWorkspace.getTopBlocks(true);
    if (blocks[0]) {
        let coord = blocks[0].getRelativeToSurfaceXY();
        blocks[0].moveBy(x - coord.x, y - coord.y);
    }
}

/**
 * New program
 */
export function newProgram(opt_further?): void {
    let further = opt_further || false;
    function loadNewProgram(): void {
        let result: any = {};
        result.rc = 'ok';
        result.name = 'NEPOprog';
        result.programShared = false;
        result.lastChanged = '';
        GUISTATE_C.setProgram(result);
        initProgramEnvironment();
        NN_C.programWasReplaced();
        LOG.info('New program loaded');
    }
    if (further || GUISTATE_C.isProgramSaved()) {
        loadNewProgram();
    } else {
        confirmLoadProgram();
    }
}

function confirmLoadProgram(): void {
    $('#show-message-confirm').oneWrap('shown.bs.modal', function(e): void {
        $('#confirm').off();
        $('#confirm').on('click', function(e): void {
            e.preventDefault();
            newProgram(true);
        });
        $('#confirmCancel').off();
        $('#confirmCancel').on('click', function(e): void {
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

export function linkProgram(): void {
    let dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
    let xml = Blockly.Xml.domToText(dom);
    //TODO this should be removed after the next release
    xml = '<export xmlns="http://de.fhg.iais.roberta.blockly"><program>' + xml + '</program><config>' + GUISTATE_C.getConfigurationXML() + '</config></export>';

    let location: URL = new URL(document.location.toString());
    let clean_uri: string = location.protocol + '//' + location.host;
    let link: string = clean_uri + '?loadSystem=';
    link += GUISTATE_C.getRobot();
    link += '&loadProgram=' + xml;
    link = encodeURI(link);
    let $temp = $('<input>');
    $('body').append($temp);
    $temp.val(link).select();
    document.execCommand('copy');
    $temp.remove();
    let displayLink: string = '</br><textarea readonly style="width:100%;" type="text">' + link + '</textarea>';
    LOG.info('ProgramLinkShare');
    MSG.displayMessage('POPUP_GET_LINK', 'POPUP', displayLink);
}

/**
 * Create a file from the blocks and download it.
 */
export function exportXml(): void {
    let dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
    let xml: string =
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
export function exportAllXml(): void {
    USER.userLoggedInCheck(function(result: any): void {
        if (result.rc === 'ok') {
            PROGRAM.exportAllProgramsXml();
        } else {
            MSG.displayMessage(result.cause, 'TOAST', 'Log in check failed for Export');
        }
    });
}

export function getBlocklyWorkspace(): any {
    return blocklyWorkspace;
}

function bindControl(): void {
    Blockly.bindEvent_(blocklyWorkspace.robControls.saveProgram, 'mousedown', null, function(e): boolean {
        LOG.info('saveProgram from blockly button');
        saveToServer();
        return false;
    });
    blocklyWorkspace.robControls.disable('saveProgram');
}

export function reloadProgram(opt_result?, opt_fromShowSource?): void {
    let program;
    if (opt_result) {
        program = opt_result.progXML;
        if (!$.isEmptyObject(opt_result.confAnnos)) {
            //@ts-ignore
            GUISTATE_C.confAnnos = opt_result.confAnnos;
            UTIL.alertTab('tabConfiguration');
        }
    } else {
        program = GUISTATE_C.getProgramXML();
    }
    programToBlocklyWorkspace(program, opt_fromShowSource);
}

export function reloadView(): void {
    if (isVisible()) {
        let dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        let xml = Blockly.Xml.domToText(dom);
        programToBlocklyWorkspace(xml);
        let toolbox = GUISTATE_C.getProgramToolbox();
        blocklyWorkspace.updateToolbox(toolbox);
        seen = true;
    } else {
        seen = false;
    }
}

export function resetView(): void {
    blocklyWorkspace.setDevice({
        group: GUISTATE_C.getRobotGroup(),
        robot: GUISTATE_C.getRobot()
    });
    initProgramEnvironment();
    let toolbox = GUISTATE_C.getProgramToolbox();
    blocklyWorkspace.updateToolbox(toolbox);
}

export function loadToolbox(level): void {
    GUISTATE_C.setProgramToolboxLevel(level);
    let xml: any = GUISTATE_C.getToolbox(level);
    if (xml) {
        blocklyWorkspace.updateToolbox(xml);
    }
    if (level === 'beginner') {
        $('.help.expert').hide();
    } else {
        $('.help.expert').show();
    }
}

export function loadExternalToolbox(toolbox: any): void {
    if (toolbox) {
        blocklyWorkspace.updateToolbox(toolbox);
    }
}

function isVisible(): boolean {
    return GUISTATE_C.getView() == 'tabProgram';
}

export function programToBlocklyWorkspace(xml, opt_fromShowSource?): void {
    if (!xml) {
        return;
    }
    listenToBlocklyEvents = false;
    blocklyWorkspace.clear();
    let dom = Blockly.Xml.textToDom(xml, blocklyWorkspace);
    Blockly.Xml.domToWorkspace(dom, blocklyWorkspace);
    blocklyWorkspace.setVersion(dom.getAttribute('xmlversion'));
    $('#infoContent').html(blocklyWorkspace.description);
    if (typeof blocklyWorkspace.description === 'string' && blocklyWorkspace.description.length) {
        $('#infoButton').addClass('notEmpty');
    } else {
        $('#infoButton').removeClass('notEmpty');
    }
    let tmpTags = blocklyWorkspace.tags;
    //@ts-ignore
    $('#infoTags').tagsinput('removeAll');
    $('.bootstrap-tagsinput input').attr('placeholder', 'Tags');
    //@ts-ignore
    $('#infoTags').tagsinput('add', tmpTags);
    let xmlConfiguration = GUISTATE_C.getConfigurationXML();
    dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
    let xmlProgram = Blockly.Xml.domToText(dom);

    let isNamedConfig: boolean = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
    let configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
    let xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
    GUISTATE_C.setProgramSaved(true);
    let language = GUISTATE_C.getLanguage();
    if ($('#codeDiv').hasClass('rightActive') && opt_fromShowSource) {
        PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, language, getSSID(), getPassword(), function(result: any): void {
            ACE_EDITOR.setViewCode(result.sourceCode);
        });
    }
    setTimeout(function(): void {
        listenToBlocklyEvents = true;
    }, 500);
}
