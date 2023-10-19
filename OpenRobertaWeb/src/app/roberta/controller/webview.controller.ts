import * as GUISTATE_C from 'guiState.controller';
import * as INTERPRETER from 'interpreter.interpreter';
import * as WEDO_B from 'interpreter.robotWeDoBehaviour';
import * as LOG from 'log';
// @ts-ignore
import * as Blockly from 'blockly';
import * as $ from 'jquery';

let ready: JQuery.Deferred<string, string, string>;
let aLanguage: string;
let webViewType: string;
let interpreter: INTERPRETER.Interpreter;
let theRobotBehaviour: WEDO_B.RobotWeDoBehaviour;

/**
 * Init webviewbotWeDoBehaviour;

 */
export function init(language: string): JQuery.Promise<string, string, string> {
    aLanguage = language;
    ready = $.Deferred();
    let a: { target: string; type: string } = { target: 'internal', type: 'identify' };
    if (tryAndroid(a)) {
        webViewType = 'Android';
    } else if (tryIOS(a)) {
        webViewType = 'IOS';
    } else {
        // Obviously not in an Open Roberta webview
        ready.resolve(language);
    }
    return ready.promise();
}

export function appToJsInterface(jsonData: string): void {
    try {
        let data: any = JSON.parse(jsonData); //Todo: exchange any
        if (!data.target || !data.type) {
            throw 'invalid arguments';
        }
        if (data.target == 'internal') {
            if (data.type == 'identify') {
                ready.resolve(aLanguage, data.name);
            } else {
                throw 'invalid arguments';
            }
        } else if (data.target === GUISTATE_C.getRobot()) {
            if (data.type == 'scan' && data.state == 'appeared') {
                $('#show-available-connections').trigger('add', data);
            } else if (data.type == 'scan' && data.state == 'error') {
                $('#show-available-connections').modal('hide');
            } else if (data.type == 'scan' && data.state == 'disappeared') {
                console.log(data);
            } else if (data.type == 'connect' && data.state == 'connected') {
                $('#show-available-connections').trigger('connect', data);
                theRobotBehaviour.update(data);
                GUISTATE_C.setConnectionState('wait');
                //@ts-ignore
                let bricklyWorkspace: any = GUISTATE_C.getBricklyWorkspace();
                let blocks: any[] = bricklyWorkspace.getAllBlocks();
                for (let i: number = 0; i < blocks.length; i++) {
                    if (blocks[i].type === 'robBrick_WeDo-Brick') {
                        let field: Blockly.field = blocks[i].getField('VAR');
                        field.setValue(data.brickname.replace(/\s/g, ''));
                        blocks[i].render();
                        let dom: HTMLElement = Blockly.Xml.workspaceToDom(bricklyWorkspace);
                        let xml: XMLDocument = Blockly.Xml.domToText(dom);
                        GUISTATE_C.setConfigurationXML(xml);
                        break;
                    }
                }
            } else if (data.type === 'connect' && data.state === 'disconnected') {
                theRobotBehaviour.update(data);
                if (interpreter != undefined) {
                    interpreter.terminate();
                }
                //@ts-ignore
                let bricklyWorkspace: any = GUISTATE_C.getBricklyWorkspace();
                let blocks: any[] = bricklyWorkspace.getAllBlocks();
                for (let i: number = 0; i < blocks.length; i++) {
                    if (blocks[i].type === 'robBrick_WeDo-Brick') {
                        let field: Blockly.field = blocks[i].getField('VAR');
                        field.setValue(Blockly.Msg.ROBOT_DEFAULT_NAME_WEDO || Blockly.Msg.ROBOT_DEFAULT_NAME || 'Brick1');
                        blocks[i].render();
                        let dom: HTMLElement = Blockly.Xml.workspaceToDom(bricklyWorkspace);
                        let xml: XMLDocument = Blockly.Xml.domToText(dom);
                        GUISTATE_C.setConfigurationXML(xml);
                        break;
                    }
                }
                GUISTATE_C.setConnectionState('error');
            } else {
                theRobotBehaviour.update(data);
            }
        } else {
            throw 'invalid arguments';
        }
    } catch (error) {
        LOG.error('appToJsInterface >' + error + ' caused by: ' + jsonData);
    }
}

function callbackOnTermination(): void {
    GUISTATE_C.setConnectionState('wait');
    GUISTATE_C.getBlocklyWorkspace().robControls.switchToStart();
}

export function getInterpreter(program: any): INTERPRETER.Interpreter {
    //Todo: exchange any
    interpreter = new INTERPRETER.Interpreter(program, theRobotBehaviour, callbackOnTermination, [], null, null);
    return interpreter;
}

export function isRobotConnected(): boolean {
    return theRobotBehaviour && theRobotBehaviour.getConnectedBricks().length > 0;
}

export function setRobotBehaviour(): void {
    switch (GUISTATE_C.getRobot()) {
        case 'wedo':
            theRobotBehaviour = new WEDO_B.RobotWeDoBehaviour(jsToAppInterface, jsToDisplay);
        // TODO: introduce here new robots and behaviours and add them to the dependencies on top of the file
        default:
            LOG.error('Webview: no robot behaviour for ' + GUISTATE_C.getRobot() + ' available!');
    }
}

export function jsToAppInterface(jsonData: any): void {
    try {
        if (webViewType === 'Android') {
            // @ts-ignore
            OpenRoberta.jsToAppInterface(JSON.stringify(jsonData));
        } else if (webViewType === 'IOS') {
            // @ts-ignore
            window.webkit.messageHandlers.OpenRoberta.postMessage(JSON.stringify(jsonData));
        } else {
            throw 'invalid webview type';
        }
    } catch (error) {
        LOG.error('jsToAppInterface >' + error + ' caused by: ' + jsonData);
    }
}

function tryAndroid(data: any): boolean {
    try {
        // @ts-ignore
        OpenRoberta.jsToAppInterface(JSON.stringify(data));
        return true;
    } catch (error) {
        return false;
    }
}

function tryIOS(data: any): boolean {
    try {
        // @ts-ignore
        window.webkit.messageHandlers.OpenRoberta.postMessage(JSON.stringify(data));
        return true;
    } catch (error) {
        return false;
    }
}

export function jsToDisplay(action: any): void {
    if (action.show !== undefined) {
        $('#showDisplayText').append('<div>' + action.show + '</div>');
        if (!$('#showDisplayText').is(':visible')) {
            $('#showDisplay').oneWrap('hidden.bs.modal', function(): void {
                $('#showDisplayText').empty();
            });
            $('#showDisplay').modal('show');
        }
        $('#showDisplayText').scrollTop($('#showDisplayText').prop('scrollHeight'));
    } else if (action.clear !== undefined) {
        $('#showDisplayText').empty();
    }
}
