/*
-------------------------------------------------------------------------

This a typescript source file stored in openroberta-lab/TypeScriptSources 
It gets compiled to openroberta-lab/OpenRobertaServer/staticResources/js

DO NOT EDIT THIS IN openroberta-lab/OpenRobertaServer/staticResources/js !

-------------------------------------------------------------------------
*/
import * as THYMIO_M from 'thymio';
import * as GUISTATE_C from 'guiState.controller';
import * as GUISTATE from 'guiState.model';

const URL = 'ws://localhost:8597';
// provide a stop program (see https://github.com/Mobsya/vpl-web/blob/master/thymio/index.js#L197) instead of using a stop function of TDM
const STOP_PROGRAM = 'motor.left.target = 0\n' +
    'motor.right.target = 0\n' +
    'call sound.system(-1)\n' +
    'call leds.circle(32,32,32,32,32,32,32,32)\n' +
    'timer.period[0] = 100\n' +
    'onevent timer0\n' +
    'call leds.circle(0,0,0,0,0,0,0,0)\n';

export var selectedNode: THYMIO_M.Node = undefined;
var startTime: number = undefined;

export function init() {
    if (!selectedNode) {
        let client = THYMIO_M.createClient(URL);
        client.onClose = async (event) => {
            if (GUISTATE_C.getConnection() === GUISTATE_C.getConnectionTypeEnum().TDM) {
                selectedNode = undefined;
                publishDisonnected();
                setTimeout(init, 1000);
            }
        };

        client.onNodesChanged = async (nodes: THYMIO_M.Node[]) => {
            try {
                //Iterate over the nodes
                for (let node of nodes) {
                    // Select the first non busy node
                    if (!selectedNode || selectedNode.id == node.id) {
                        if (!selectedNode || (selectedNode.status != THYMIO_M.NodeStatus.ready && node.status == THYMIO_M.NodeStatus.available)) {
                            try {
                                // Lock (take ownership) of the node. We cannot mutate a node (send code to it), until we have a lock on it
                                // Once locked, a node will appear busy / unavailable to other clients until we close the connection or call `unlock` explicitely
                                // We can lock as many nodes as we want
                                await node.lock();
                                selectedNode = node;
                                publishConnected();
                            } catch (e) {
                                console.log(`Unable To Log ${node.id} (${node.name})`);
                            }
                        }
                        if (!selectedNode) {
                            continue;
                        }
                        if (selectedNode.status == THYMIO_M.NodeStatus.disconnected) {
                            selectedNode = undefined;
                            GUISTATE_C.updateMenuStatus(0);
                            init();
                        }
                    }
                }
            } catch (e) {
                console.log(e);
            }
        };
    } else {
        if (selectedNode.status == THYMIO_M.NodeStatus.ready) {
            publishConnected();
        }
    }
}

function publishConnected(): void {
    startTime = Date.now();
    GUISTATE.robot.fWName = GUISTATE_C.getRobotRealName();
    GUISTATE.robot.time = 0;
    GUISTATE.robot.battery = '-';
    GUISTATE.robot.name = selectedNode.name;
    GUISTATE.robot.state = 'wait';
    GUISTATE_C.updateMenuStatus(1);
}

function publishDisonnected(): void {
    GUISTATE.robot.fWName = '';
    GUISTATE.robot.time = -1;
    GUISTATE.robot.battery = '-';
    GUISTATE.robot.name = '';
    GUISTATE.robot.state = 'error';
    GUISTATE_C.updateMenuStatus(0);
}

export async function uploadProgram(generatedCode: string): Promise<any> {
    GUISTATE.robot.time = startTime - Date.now();
    if (selectedNode && selectedNode.status === THYMIO_M.NodeStatus.ready) {
        try {
            await selectedNode.lock();
            await selectedNode.sendAsebaProgram(generatedCode);
            await selectedNode.runProgram();
            await selectedNode.unlock();
            return 'done';
        } catch (e) {
            throw e;
        }
    } else {
        throw new Error('Exception on upload program');
    }
}

export async function stopProgram(): Promise<any> {
    return uploadProgram(STOP_PROGRAM);
}
