import * as LOG from 'log';
import * as GUISTATE_C from 'guiState.controller';
import * as $ from 'jquery';
// @ts-ignore
import * as Blockly from 'blockly';
import * as CONNECTION_C from 'connection.controller';
import * as GUISTATE from 'guiState.model';

let blocklyWorkspace;

function init(workspace) {
    blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
    initEvents();
}

function initEvents() {
    Blockly.bindEvent_(blocklyWorkspace.robControls.runOnBrick, 'mousedown', null, function (e) {
        if ($('#runOnBrick').hasClass('disabled')) {
            let notificationElement = $('#releaseInfo');
            let notificationElementTitle = notificationElement.children('#releaseInfoTitle');
            let notificationElementDescription = notificationElement.children('#releaseInfoContent');
            notificationElementDescription.html(Blockly.Msg.POPUP_RUN_NOTIFICATION);
            notificationElementTitle.html(Blockly.Msg.POPUP_ATTENTION);
            let a = notificationElement.on('notificationFadeInComplete', function () {
                clearTimeout(a.data('hideInteval'));
                let id = setTimeout(function () {
                    notificationElement.fadeOut(500);
                }, 10000);
                a.data('hideInteval', id);
            });
            notificationElement.fadeIn(500, function () {
                $(this).trigger('notificationFadeInComplete');
            });

            return false;
        }
        LOG.info('runOnBrick from blockly button');
        runOnBrick();
        return false;
    });
    Blockly.bindEvent_(blocklyWorkspace.robControls.stopBrick, 'mousedown', null, function (e) {
        LOG.info('stopBrick from blockly button');
        stopProgram();
        return false;
    });
    Blockly.bindEvent_(blocklyWorkspace.robControls.stopProgram, 'mousedown', null, function (e) {
        LOG.info('stopProgram from blockly button');
        stopProgram();
        return false;
    });
}

/**
 * Start the program on brick from the source code editor
 */
function runNative(sourceCode) {
    let ping = GUISTATE_C.doPing();
    GUISTATE_C.setConnectionState('busy');
    GUISTATE_C.setPing(false);
    LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick from source code editor');
    CONNECTION_C.getConnectionInstance().runNative(sourceCode);
    GUISTATE_C.setPing(ping);
}

/**
 * Start the program on the brick
 */
function runOnBrick(opt_program?) {
    let ping = GUISTATE.server.ping;
    GUISTATE_C.setConnectionState('busy');
    GUISTATE_C.setPing(false);
    LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick');
    let xmlProgram;
    let xmlTextProgram;
    if (opt_program) {
        xmlTextProgram = opt_program;
    } else {
        xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
    }
    let isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
    let configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
    let xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

    CONNECTION_C.getConnectionInstance().runOnBrick(configName, xmlTextProgram, xmlConfigText);
    GUISTATE_C.setPing(ping);
}

async function stopProgram() {
    CONNECTION_C.getConnectionInstance().stopProgram();
}

export { init, runNative, runOnBrick };
