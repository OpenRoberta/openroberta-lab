import * as LOG from 'log';
import * as UTIL from 'util.roberta';
import * as MSG from 'message';
import * as GUISTATE_C from 'guiState.controller';
// @ts-ignore
import * as Blockly from 'blockly';
import * as CONFIGURATION from 'configuration.model';
import * as CV from 'confVisualization';
import * as $ from 'jquery';
import 'jquery-validate';
import { BaseResponse, ConfResponse } from '../ts/restEntities';

let $formSingleModal: JQuery<HTMLElement>;

let bricklyWorkspace: any;
let confVis: any;
let listenToBricklyEvents: boolean = true;
let seen: boolean = false;

export function init(): void {
    initView();
    initEvents();
    initConfigurationForms();
    initConfigurationEnvironment();
}

/**
 * Inject Brickly with initial toolbox
 *
 *            toolbox
 */
function initView(): void {
    let toolbox: string = GUISTATE_C.getConfigurationToolbox();
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
            minScale: 0.25,
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
    bricklyWorkspace.robControls.runOnBrick.setAttribute('style', 'display : none');
    GUISTATE_C.setBricklyWorkspace(bricklyWorkspace);
    bricklyWorkspace.robControls.disable('saveProgram');
}

function initEvents(): void {
    $('#tabConfiguration').onWrap('show.bs.tab', function(): void {
        GUISTATE_C.setView('tabConfiguration');
    });

    $('#tabConfiguration').onWrap(
        'shown.bs.tab',
        function(): void {
            bricklyWorkspace.markFocused();
            if (GUISTATE_C.isConfigurationUsed()) {
                bricklyWorkspace.setVisible(true);
            } else {
                bricklyWorkspace.setVisible(false);
            }
            $(window).resize();
            UTIL.clearAnnotations(bricklyWorkspace);

            // @ts-ignore
            if (GUISTATE_C.confAnnos) {
                // @ts-ignore
                UTIL.annotateBlocks(bricklyWorkspace, GUISTATE_C.confAnnos);
                // @ts-ignore
                delete GUISTATE_C.confAnnos;
            }
            confVis && confVis.refresh();
        },
        'tabConfiguration clicked'
    );

    $('#tabConfiguration').onWrap('hidden.bs.tab', function(): void {
        let dom: HTMLElement = confVis ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
        let xml: XMLDocument = Blockly.Xml.domToText(dom);
        GUISTATE_C.setConfigurationXML(xml);
        bricklyWorkspace.setVisible(false);
    });

    Blockly.bindEvent_(bricklyWorkspace.robControls.saveProgram, 'mousedown', null, function(): void {
        LOG.info('saveConfiguration from brickly button');
        saveToServer();
    });

    bricklyWorkspace.addChangeListener(function(event: Event): void {
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

export function initConfigurationForms(): void {
    $formSingleModal = $('#single-modal-form');
}

/**
 * Save configuration to server
 */
export function saveToServer(): void {
    $('.modal').modal('hide'); // close all opened popups
    if (GUISTATE_C.isConfigurationStandard() || GUISTATE_C.isConfigurationAnonymous()) {
        LOG.error('saveToServer may only be called with an explicit config name');
        return;
    }
    let dom: HTMLElement = confVis ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
    let xmlText: string = Blockly.Xml.domToText(dom);
    CONFIGURATION.saveConfigurationToServer(GUISTATE_C.getConfigurationName(), xmlText, function(result: BaseResponse): void {
        if (result.rc === 'ok') {
            GUISTATE_C.setConfigurationSaved(true);
            LOG.info('save brick configuration ' + GUISTATE_C.getConfigurationName());
        }
        MSG.displayInformation(result, 'MESSAGE_EDIT_SAVE_CONFIGURATION', result.message, GUISTATE_C.getConfigurationName());
    });
}

/**
 * Save configuration with new name to server
 */
export function saveAsToServer(): void {
    $formSingleModal.validate();
    if ($formSingleModal.valid()) {
        $('.modal').modal('hide'); // close all opened popups
        let confName: string = $('#singleModalInput').val().toString().trim();
        if (GUISTATE_C.getConfigurationStandardName() === confName) {
            LOG.error('saveAsToServer may NOT use the config standard name');
            return;
        }
        let dom: HTMLElement = confVis ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
        let xmlText: string = Blockly.Xml.domToText(dom);
        CONFIGURATION.saveAsConfigurationToServer(confName, xmlText, function(result: any): void {
            if (result.rc === 'ok') {
                result.name = confName;
                GUISTATE_C.setConfiguration(result);
                GUISTATE_C.setProgramSaved(false);
                LOG.info('save brick configuration ' + GUISTATE_C.getConfigurationName());
                MSG.displayInformation(result, 'MESSAGE_EDIT_SAVE_CONFIGURATION_AS', result.message, GUISTATE_C.getConfigurationName());
            } else if (result.cause == 'ORA_CONFIGURATION_SAVE_AS_ERROR_CONFIGURATION_EXISTS') {
                //Replace popup window
                let modalMessage: string =
                    Blockly.Msg.POPUP_BACKGROUND_REPLACE_CONFIGURATION ||
                    'A configuration with the same name already exists! <br> Would you like to replace it?';
                $('#show-message-confirm').onWrap('shown.bs.modal', function(): void {
                    $('#confirm').off();
                    $('#confirm').onWrap(
                        'click',
                        function(e: Event): void {
                            e.preventDefault();
                            CONFIGURATION.saveConfigurationToServer(confName, xmlText, function(result: any): void {
                                if (result.rc == 'ok') {
                                    result.name = confName;
                                    GUISTATE_C.setConfiguration(result);
                                    GUISTATE_C.setProgramSaved(false);
                                    LOG.info('saved configuration' + GUISTATE_C.getConfigurationName() + ' as' + confName + ' and overwrote old content');
                                    MSG.displayInformation(
                                        result,
                                        'MESSAGE_EDIT_SAVE_CONFIGURATION_AS',
                                        result.message,
                                        GUISTATE_C.getConfigurationName(),
                                        null
                                    );
                                } else {
                                    LOG.info('failed to overwrite ' + confName);
                                    MSG.displayMessage(result.message, 'POPUP', '');
                                }
                            });
                        },
                        'confirm modal'
                    );
                    $('#confirmCancel').off();
                    $('#confirmCancel').onWrap(
                        'click',
                        function(e: Event) {
                            e.preventDefault();
                            $('.modal').modal('hide');
                        },
                        'cancel modal'
                    );
                });
                MSG.displayPopupMessage(
                    'ORA_CONFIGURATION_SAVE_AS_ERROR_CONFIGURATION_EXISTS',
                    modalMessage,
                    Blockly.Msg.POPUP_REPLACE,
                    Blockly.Msg.POPUP_CANCEL
                );
            }
        });
    }
}

/**
 * Load the configuration that was selected in configurations list
 */
export function loadFromListing(conf: any[]): void {
    LOG.info('loadFromList ' + conf[0]);
    CONFIGURATION.loadConfigurationFromListing(conf[0], conf[1], function(result: any): void {
        if (result.rc === 'ok') {
            result.name = conf[0];
            $('#tabConfiguration').oneWrap('shown.bs.tab', function(): void {
                showConfiguration(result);
            });
            // @ts-ignore
            $('#tabConfiguration').tabWrapShow();
        }
        MSG.displayInformation(result, '', result.message, '');
    });
}

export function initConfigurationEnvironment(): void {
    let conf: string = GUISTATE_C.getConfigurationConf();
    configurationToBricklyWorkspace(conf);
    if (isVisible()) {
        let x: number, y: number;
        if ($(window).width() < 768) {
            x = $(window).width() / 50;
            y = 25;
        } else {
            x = $(window).width() / 5;
            y = 50;
        }
        let blocks = bricklyWorkspace.getTopBlocks(true);
        for (let i: number = 0; i < blocks.length; i++) {
            let coord = Blockly.getSvgXY_(blocks[i].svgGroup_, bricklyWorkspace);
            let coordBlock: Blockly.Coordinates = blocks[i].getRelativeToSurfaceXY();
            blocks[i].moveBy(coordBlock.x - coord.x + x, coordBlock.y - coord.y + y);
        }
        seen = true;
    } else {
        seen = false;
        bricklyWorkspace.setVisible(false);
    }
    let dom: HTMLElement = confVis ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
    let xml: XMLDocument = Blockly.Xml.domToText(dom);
    GUISTATE_C.setConfigurationXML(xml);
}

export function showSaveAsModal(): void {
    let regexString: RegExp = new RegExp('^(?!\\b' + GUISTATE_C.getConfigurationStandardName() + '\\b)([a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]*)$');
    $.validator.addMethod(
        'regex',
        function(value: any, _element: HTMLElement, regexp: RegExp) {
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
        saveAsToServer,
        function(): void {},
        {
            rules: {
                singleModalInput: {
                    required: true,
                    regex: regexString
                }
            },
            errorClass: 'form-invalid',
            errorPlacement: function(label: JQuery<HTMLElement>, element: JQuery<HTMLElement>): void {
                label.insertAfter(element);
            },
            messages: {
                singleModalInput: {
                    required: jQuery.validator.format(Blockly.Msg['VALIDATION_FIELD_REQUIRED']),
                    regex: jQuery.validator.format(Blockly.Msg['MESSAGE_INVALID_CONF_NAME'])
                }
            }
        }
    );
}

/**
 * New configuration
 */
export function newConfiguration(opt_further?: boolean): void {
    let further: boolean = opt_further || false;
    if (further || GUISTATE_C.isConfigurationSaved()) {
        let result: any = {
            name: GUISTATE_C.getRobotGroup().toUpperCase() + 'basis',
            lastChanged: '',
            rc: null,
            message: null,
            cause: null,
            confXML: null,
            parameters: null,
            cmd: null,
            initToken: null
        };
        GUISTATE_C.setConfiguration(result);
        initConfigurationEnvironment();
    } else {
        $('#show-message-confirm').oneWrap('shown.bs.modal', function(): void {
            $('#confirm').off();
            $('#confirm').onWrap('click', function(e: Event): void {
                e.preventDefault();
                newConfiguration(true);
            });
            $('#confirmCancel').off();
            $('#confirmCancel').onWrap('click', function(e: Event): void {
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
}

/**
 * Show configuration
 *
 *            load configuration
 *            data of server call
 * @param result
 */
export function showConfiguration(result: ConfResponse): void {
    if (result.rc == 'ok') {
        configurationToBricklyWorkspace(result.confXML);
        GUISTATE_C.setConfiguration(result);
        LOG.info('show configuration ' + GUISTATE_C.getConfigurationName());
    }
}

export function getBricklyWorkspace(): any {
    return bricklyWorkspace;
}

export function reloadConf(opt_result?: ConfResponse): void {
    let conf: string;
    if (opt_result) {
        conf = opt_result.confXML;
    } else {
        conf = GUISTATE_C.getConfigurationXML();
    }
    if (!seen) {
        configurationToBricklyWorkspace(conf);
        let x: number, y: number;
        if ($(window).width() < 768) {
            x = $(window).width() / 50;
            y = 25;
        } else {
            x = $(window).width() / 5;
            y = 50;
        }
        let blocks = bricklyWorkspace.getTopBlocks(true);
        for (let i: number = 0; i < blocks.length; i++) {
            let coord = Blockly.getSvgXY_(blocks[i].svgGroup_, bricklyWorkspace);
            let coordBlock = blocks[i].getRelativeToSurfaceXY();
            blocks[i].moveBy(coordBlock.x - coord.x + x, coordBlock.y - coord.y + y);
        }
    } else {
        configurationToBricklyWorkspace(conf);
    }
}

export function reloadView(): void {
    let dom: HTMLElement = confVis ? confVis.getXml() : Blockly.Xml.workspaceToDom(bricklyWorkspace);
    let xml: string = Blockly.Xml.domToText(dom);
    configurationToBricklyWorkspace(xml);
    let toolbox = GUISTATE_C.getConfigurationToolbox();
    bricklyWorkspace.updateToolbox(toolbox);
}

export function changeRobotSvg(): void {
    if (CV.CircuitVisualization.isRobotVisualized(GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getRobot(), null)) {
        bricklyWorkspace.setDevice({
            group: GUISTATE_C.getRobotGroup(),
            robot: GUISTATE_C.getRobot()
        });
        confVis.resetRobot();
    }
}

export function resetView(): void {
    bricklyWorkspace.setDevice({
        group: GUISTATE_C.getRobotGroup(),
        robot: GUISTATE_C.getRobot()
    });
    initConfigurationEnvironment();
    let toolbox: string = GUISTATE_C.getConfigurationToolbox();
    bricklyWorkspace.updateToolbox(toolbox);
}

function isVisible(): boolean {
    return GUISTATE_C.getView() == 'tabConfiguration';
}

function resetConfVisIfAvailable(): void {
    if (confVis) {
        confVis.dispose();
        confVis = null;
    }
}

export function configurationToBricklyWorkspace(xml: string): void {
    // removing changelistener in blockly doesn't work, so no other way
    listenToBricklyEvents = false;
    bricklyWorkspace.clear();
    Blockly.svgResize(bricklyWorkspace);
    let dom: HTMLElement = Blockly.Xml.textToDom(xml, bricklyWorkspace);
    resetConfVisIfAvailable();
    if (CV.CircuitVisualization.isRobotVisualized(GUISTATE_C.getRobotGroup(), GUISTATE_C.getRobot())) {
        confVis = CV.CircuitVisualization.domToWorkspace(dom, bricklyWorkspace);
    } else {
        Blockly.Xml.domToWorkspace(dom, bricklyWorkspace);
    }
    bricklyWorkspace.setVersion(dom.getAttribute('xmlversion'));
    let name: string;
    let configName: string = GUISTATE_C.getConfigurationName() == undefined ? '' : GUISTATE_C.getConfigurationName();
    if (xml == GUISTATE_C.getConfigurationConf()) {
        name = GUISTATE_C.getRobotGroup().toUpperCase() + 'basis';
    } else {
        name = configName;
    }
    GUISTATE_C.setConfigurationName(name);
    GUISTATE_C.setConfigurationSaved(true);
    $('#tabConfigurationName').html(name);
    setTimeout(function(): void {
        listenToBricklyEvents = true;
    }, 500);
    seen = isVisible();
    if (GUISTATE_C.isConfigurationUsed()) {
        bricklyWorkspace.setVisible(true);
    } else {
        bricklyWorkspace.setVisible(false);
    }
}
