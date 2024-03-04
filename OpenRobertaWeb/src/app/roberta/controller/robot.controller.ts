import * as UTIL from 'util.roberta';
import * as LOG from 'log';
import * as MSG from 'message';
import * as GUISTATE_C from 'guiState.controller';
import * as ROBOT from 'robot.model';
import * as PROGRAM_C from 'program.controller';
import * as CONFIGURATION_C from 'configuration.controller';
import * as WEBVIEW_C from 'webview.controller';
import * as CODEEDITOR_C from 'sourceCodeEditor.controller';
import * as PROGCODE_C from 'progCode.controller';
import * as $ from 'jquery';
// @ts-ignore
import * as Blockly from 'blockly';
import 'jquery-validate';
import { ConnectionInterface } from 'connection.interface';
import * as CONNECTION_C from 'connection.controller';

let $formSingleModal;
let robotPort;
let connectionInstance: ConnectionInterface;

/**
 * Initialize robot
 */
function init(robot) {
    let ready = $.Deferred();
    $.when(
        ROBOT.setRobot(robot, function (result) {
            if (result.rc == 'ok') {
                GUISTATE_C.setRobot(robot, result, true);
            }
        })
    ).then(function () {
        initRobotForms();
        ready.resolve();
    });
    return ready.promise();
}

/**
 * Set token
 *
 * @param token Token value to be set
 */
function setToken(token: string) {
    $formSingleModal.validate();
    if ($formSingleModal.valid()) {
        ROBOT.setToken(token, function (result) {
            if (result.rc === 'ok') {
                GUISTATE_C.setRobotToken(token);
                GUISTATE_C.setState(result);
                // @ts-ignore
                MSG.displayInformation(result, 'MESSAGE_ROBOT_CONNECTED', result.message, GUISTATE_C.getRobotName());
                handleFirmwareConflict(result['robot.update'], result['robot.serverVersion']);
            } else {
                if (result.message === 'ORA_TOKEN_SET_ERROR_WRONG_ROBOTTYPE') {
                    $('.modal').modal('hide');
                }
            }
            UTIL.response(result);
        });
    }
}

function setPort(port) {
    robotPort = port;
    $('#single-modal-list').modal('hide');
    GUISTATE_C.setRobotPort(port);
}

function getPort() {
    return robotPort;
}

function initRobotForms() {
    $('#iconDisplayRobotState').onWrap(
        'click',
        function () {
            connectionInstance.showRobotInfo();
        },
        'display robot state'
    );

    $('#wlan-form').removeData('validator');
    $.validator.addMethod(
        'wlanRegex',
        function (value, element) {
            return this.optional(element) || /^[a-zA-Z0-9$ *\(\)\{\}\[\]><~`\'\\\/|=+!?.,%#+&^@_\-äöüÄÖÜß]+$/gi.test(value);
        },
        'This field contains nonvalid symbols.'
    );
    $('#wlan-form').validate({
        rules: {
            wlanSsid: {
                required: true,
                wlanRegex: true,
            },
            wlanPassword: {
                required: true,
                wlanRegex: true,
            },
        },
        errorClass: 'form-invalid',
        errorPlacement: function (label, element) {
            label.insertBefore(element.parent());
        },
        messages: {
            wlanSsid: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                wlanRegex: Blockly.Msg['VALIDATION_CONTAINS_SPECIAL_CHARACTERS'],
            },
            wlanPassword: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                wlanRegex: Blockly.Msg['VALIDATION_CONTAINS_SPECIAL_CHARACTERS'],
            },
        },
    });

    $('#setWlanCredentials').onWrap(
        'click',
        function (e) {
            e.preventDefault();
            $('#wlan-form').validate();
            if ($('#wlan-form').valid()) {
                PROGRAM_C.setSSID((<HTMLInputElement>document.getElementById('wlanSsid')).value);
                PROGRAM_C.setPassword((<HTMLInputElement>document.getElementById('wlanPassword')).value);
                $('#menu-wlan').modal('hide');
            }
        },
        'wlan form submitted'
    );

    $('#doUpdateFirmware').onWrap(
        'click',
        function () {
            $('#set-token').modal('hide');
            $('#confirmUpdateFirmware').modal('hide');
            updateFirmware();
        },
        'update firmware of robot'
    );

    $formSingleModal = $('#single-modal-form');

    $('#connectionsTable').bootstrapTable({
        formatNoMatches: function () {
            return '<div class="lds-ellipsis"></div>';
        },
        columns: [
            {
                // TODO: translations
                title: 'Name',
                field: 'name',
            },
            {
                visible: false,
                field: 'id',
            },
        ],
    });
    $('#connectionsTable').onWrap(
        'click-row.bs.table',
        function (e, row) {
            WEBVIEW_C.jsToAppInterface({
                target: GUISTATE_C.getRobot(),
                type: 'connect',
                robot: row.id,
            });
        },
        'connect to robot'
    );
    $('#show-available-connections').on('hidden.bs.modal', function (e) {
        WEBVIEW_C.jsToAppInterface({
            target: GUISTATE_C.getRobot(),
            type: 'stopScan',
        });
    });

    $('#show-available-connections').onWrap(
        'add',
        function (event, data) {
            $('#connectionsTable').bootstrapTable('insertRow', {
                index: 999,
                row: {
                    name: data.brickname,
                    id: data.brickid,
                },
            });
        },
        'insert robot connections'
    );

    $('#show-available-connections').onWrap(
        'connect',
        function (event, data) {
            let result = {};
            result['robot.name'] = data.brickname;
            result['robot.state'] = 'wait';
            GUISTATE_C.setState(result);
            $('#show-available-connections').modal('hide');
        },
        'connect to a robot'
    );
}

function showSetTokenModal() {
    UTIL.showSingleModal(
        function () {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h5').text(Blockly.Msg['MENU_CONNECT']);
            $('#single-modal label').text(Blockly.Msg['POPUP_VALUE']);
            $('#singleModalInput').addClass('capitalLetters');
            $('#single-modal a[href]').text(Blockly.Msg['POPUP_STARTUP_HELP']);
            $('#single-modal a[href]').attr('href', 'http://wiki.open-roberta.org');
        },
        function () {
            // @ts-ignore
            setToken($('#singleModalInput').val().toUpperCase());
        },
        function () {
            $('#singleModalInput').removeClass('capitalLetters');
        },
        {
            rules: {
                singleModalInput: {
                    required: true,
                    minlength: 8,
                    maxlength: 8,
                },
            },
            errorClass: 'form-invalid',
            errorPlacement: function (label, element) {
                label.insertAfter(element);
            },
            messages: {
                singleModalInput: {
                    required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                    minlength: Blockly.Msg['VALIDATION_TOKEN_LENGTH'],
                    maxlength: Blockly.Msg['VALIDATION_TOKEN_LENGTH'],
                },
            },
        }
    );
}

function showScanModal() {
    if ($('#show-available-connections').is(':visible')) {
        return;
    }
    $('#connectionsTable').bootstrapTable('removeAll');
    WEBVIEW_C.jsToAppInterface({
        target: GUISTATE_C.getRobot(),
        type: 'startScan',
    });
    $('#show-available-connections').modal('show');
}

function showListModal() {
    UTIL.showSingleListModal(
        function () {
            $('#single-modal-list h3').text(Blockly.Msg['MENU_CONNECT']);
            $('#single-modal-list label').text(Blockly.Msg['POPUP_VALUE']);
            $('#single-modal-list a[href]').text(Blockly.Msg['POPUP_STARTUP_HELP']);
            $('#single-modal-list a[href]').attr('href', 'http://wiki.open-roberta.org');
        },
        function () {
            // @ts-ignore
            setPort(document.getElementById('singleModalListInput').value);
        },
        function () {}
    );
}

/**
 * Handle firmware conflict between server and robot
 */
function handleFirmwareConflict(updateInfo, robotServerVersion: string) {
    if (updateInfo < 0) {
        LOG.info(
            "The firmware version '" +
                robotServerVersion +
                "' on the server is newer than the firmware version '" +
                GUISTATE_C.getRobotVersion() +
                "' on the robot"
        );
        $('#confirmUpdateFirmware').modal('show');
        return true;
    } else if (updateInfo > 0) {
        LOG.info(
            "The firmware version '" +
                robotServerVersion +
                "' on the server is older than the firmware version '" +
                GUISTATE_C.getRobotVersion() +
                "' on the robot"
        );
        // @ts-ignore
        MSG.displayMessage('MESSAGE_FIRMWARE_ERROR', 'POPUP', '');
        return true;
    }
    return false;
}

/**
 * Update robot firmware
 */
function updateFirmware() {
    ROBOT.updateFirmware(function (result) {
        GUISTATE_C.setState(result);
        if (result.rc === 'ok') {
            // @ts-ignore
            MSG.displayMessage('MESSAGE_RESTART_ROBOT', 'POPUP', '');
        } else {
            // @ts-ignore
            MSG.displayInformation(result, '', result.message, GUISTATE_C.getRobotFWName());
        }
    });
}

/**
 * Switch robot
 */
function switchRobot(robot: string, opt_continue: boolean, opt_callback) {
    // @ts-ignore
    PROGRAM_C.SSID = null;
    // @ts-ignore
    PROGRAM_C.password = null;
    // @ts-ignore
    document.getElementById('wlanSsid').value = '';
    // @ts-ignore
    document.getElementById('wlanPassword').value = '';

    let further;
    // no need to ask for saving programs if you switch the robot in between a group
    if (typeof opt_continue === 'undefined' && GUISTATE_C.findGroup(robot) == GUISTATE_C.getRobotGroup()) {
        further = true;
    } else {
        further = opt_continue || false;
        Blockly.clipboardXml_ = null;
    }
    if (further || (GUISTATE_C.isProgramSaved() && GUISTATE_C.isConfigurationSaved())) {
        if (robot === GUISTATE_C.getRobot()) {
            typeof opt_callback === 'function' && opt_callback();
            return;
        }
        ROBOT.setRobot(robot, function (result) {
            if (result.rc === 'ok') {
                if ($('.rightMenuButton.rightActive').length > 0) {
                    $('.rightMenuButton.rightActive').clickWrap();
                }
                if (GUISTATE_C.findGroup(robot) != GUISTATE_C.getRobotGroup()) {
                    GUISTATE_C.setRobot(robot, result);
                    CONFIGURATION_C.resetView();
                    PROGRAM_C.resetView();
                } else {
                    GUISTATE_C.setRobot(robot, result);
                }

                CONNECTION_C.switchConnection(robot);

                CONFIGURATION_C.changeRobotSvg();
                if (GUISTATE_C.getView() == 'tabConfList') {
                    $('#confList>.bootstrap-table').find('button[name="refresh"]').clickWrap();
                }
                if (GUISTATE_C.getView() == 'tabProgList') {
                    $('#progList>.bootstrap-table').find('button[name="refresh"]').clickWrap();
                }
                PROGCODE_C.setCodeLanguage(GUISTATE_C.getSourceCodeFileExtension());
                CODEEDITOR_C.setCodeLanguage(GUISTATE_C.getSourceCodeFileExtension());
                CODEEDITOR_C.resetScroll();
                //TODO inform app if one is there
                //                    WEBVIEW_C.jsToAppInterface({
                //                        'target' : 'wedo',
                //                        'op' : {'type''disconnect'
                //                    });
                typeof opt_callback === 'function'
                    ? opt_callback()
                    : // @ts-ignore
                      MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getRobotRealName());
                let deprecatedData = GUISTATE_C.getRobotDeprecatedData(robot);
                if (deprecatedData !== undefined) {
                    $('#show-message>.modal-dialog').removeClass('modal-sm');
                    $('#show-message').on('hidden.bs.modal', function () {
                        $('#show-message>.modal-dialog').addClass('modal-sm');
                    });
                    MSG.displayPopupMessage(GUISTATE_C.getLanguage(), deprecatedData, 'OK', false);
                }
            } else {
                // @ts-ignore
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getRobotRealName());
            }
        });
    } else {
        $('#show-message-confirm').oneWrap('shown.bs.modal', function (e) {
            $('#confirm').off();
            $('#confirm').onWrap(
                'click',
                function (e) {
                    e.preventDefault();
                    switchRobot(robot, true, opt_callback);
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
        if (GUISTATE_C.isUserLoggedIn()) {
            // @ts-ignore
            MSG.displayMessage('POPUP_BEFOREUNLOAD_LOGGEDIN', 'POPUP', '', true);
        } else {
            // @ts-ignore
            MSG.displayMessage('POPUP_BEFOREUNLOAD', 'POPUP', '', true);
        }
    }
}
export { init, setPort, getPort, showSetTokenModal, showScanModal, showListModal, handleFirmwareConflict, updateFirmware, switchRobot };
