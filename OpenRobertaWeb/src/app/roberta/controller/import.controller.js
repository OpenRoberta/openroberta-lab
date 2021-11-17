import * as COMM from 'comm';
import * as MSG from 'message';
import * as LOG from 'log';
import * as UTIL from 'util';
import * as GUISTATE_C from 'guiState.controller';
import * as PROGRAM_C from 'program.controller';
import * as CONFIGURATION_C from 'configuration.controller';
import * as PROGRAM from 'program.model';
import * as ROBOT_C from 'robot.controller';
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'jquery-validate';

function init(callback) {
    $('#fileSelector').val(null);
    $('#fileSelector').off();
    $('#fileSelector').onWrap(
        'change',
        function (event) {
            var file = event.target.files[0];
            var reader = new FileReader();
            reader.onload = function (event) {
                var name = UTIL.getBasename(file.name);
                if ($.isFunction(callback)) {
                    callback(name, event.target.result);
                }
            };
            reader.readAsText(file);
            return false;
        },
        'import clicked'
    );
}

function importXml() {
    init(loadProgramFromXML);
    $('#fileSelector').attr('accept', '.xml');
    $('#fileSelector').clickWrap(); // opening dialog
}

function importSourceCode(callback) {
    init(callback);
    $('#fileSelector').attr('accept', '.' + GUISTATE_C.getSourceCodeFileExtension());
    $('#fileSelector').clickWrap(); // opening dialog
}

function openProgramFromXML(target) {
    var robotType = target[1];
    var programName = target[2];
    var programXml = target[3];
    ROBOT_C.switchRobot(robotType, true, function () {
        loadProgramFromXML(programName, programXml);
    });
}

function loadProgramFromXML(name, xml) {
    if (xml.search('<export') === -1) {
        xml =
            '<export xmlns="http://de.fhg.iais.roberta.blockly"><program>' +
            xml +
            '</program><config>' +
            GUISTATE_C.getConfigurationXML() +
            '</config></export>';
    }
    PROGRAM.loadProgramFromXML(name, xml, function (result) {
        if (result.rc == 'ok') {
            // save the old program and configuration that it can be restored
            var dom = Blockly.Xml.workspaceToDom(GUISTATE_C.getBlocklyWorkspace());
            var xmlProgOld = Blockly.Xml.domToText(dom);
            GUISTATE_C.setProgramXML(xmlProgOld);
            dom = Blockly.Xml.workspaceToDom(GUISTATE_C.getBricklyWorkspace());
            var xmlConfOld = Blockly.Xml.domToText(dom);
            GUISTATE_C.setConfigurationXML(xmlConfOld);

            // on server side we only test case insensitive block names, displaying xml can still fail:
            result.programSaved = false;
            result.name = 'NEPOprog';
            result.programShared = false;
            result.programTimestamp = '';
            var nameConfOld = GUISTATE_C.getConfigurationName();
            try {
                CONFIGURATION_C.configurationToBricklyWorkspace(result.confXML);
                GUISTATE_C.setConfigurationXML(result.confXML);
                PROGRAM_C.programToBlocklyWorkspace(result.progXML);
                GUISTATE_C.setProgram(result);
                GUISTATE_C.setProgramXML(result.progXML);
                GUISTATE_C.setConfigurationName('');
                LOG.info('show program ' + GUISTATE_C.getProgramName());
            } catch (e) {
                // restore old Program
                LOG.error(e.message);
                GUISTATE_C.setProgramXML(xmlProgOld);
                GUISTATE_C.setConfigurationXML(xmlConfOld);
                GUISTATE_C.setConfigurationName(nameConfOld);
                CONFIGURATION_C.reloadConf();
                PROGRAM_C.reloadProgram();
                result.rc = 'error';
                MSG.displayInformation(result, '', Blockly.Msg.ORA_PROGRAM_IMPORT_ERROR, name);
            }
        } else {
            if (result.message === 'ORA_PROGRAM_IMPORT_ERROR_WRONG_ROBOT_TYPE') {
                MSG.displayInformation(result, '', result.message, result.robotTypes);
            } else {
                MSG.displayInformation(result, '', result.message, name);
            }
        }
    });
}

/**
 * Open a file select dialog to load source code from local disk and send it
 * to the cross compiler
 */
function importSourceCodeToCompile() {
    init(compileFromSource);
    $('#fileSelector').attr('accept', '.' + GUISTATE_C.getSourceCodeFileExtension());
    $('#fileSelector').clickWrap(); // opening dialog
}

function compileFromSource(name, source) {
    PROGRAM.compileN(name, source, GUISTATE_C.getLanguage(), function (result) {
        var alertMsg = result.rc;
        if (result.parameters !== undefined) {
            alertMsg += '\nMessage is:\n' + result.parameters.MESSAGE;
        }
        alert(alertMsg);
    });
}

/**
 * Open a file select dialog to load source code from local disk and send it
 * to the cross compiler
 */

function importNepoCodeToCompile() {
    init(compileFromNepoCode);
    $('#fileSelector').attr('accept', '.xml');
    $('#fileSelector').clickWrap(); // opening dialog
}
export { init, importXml, importSourceCode, openProgramFromXML, loadProgramFromXML, importSourceCodeToCompile, importNepoCodeToCompile };

function compileFromNepoCode(name, source) {
    PROGRAM.compileP(name, source, GUISTATE_C.getLanguage(), function (result) {
        alert(result.rc);
    });
}
