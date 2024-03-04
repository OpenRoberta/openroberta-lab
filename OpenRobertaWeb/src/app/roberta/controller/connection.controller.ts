import { ConnectionInterface } from 'connection.interface';
import * as GUISTATE_C from 'guiState.controller';

let connectionInstance: ConnectionInterface;
let robot = {
    socket: null,
    isAgent: false,
};

export async function initConnection(robotName: string) {
    assertBlocklyWorkspaceLoaded();
    await setConnection(robotName);
}

export async function switchConnection(robotName: string) {
    getConnectionInstance().terminate();
    await setConnection(robotName);
}

export async function setConnection(robotName: string) {
    setConnectionInstance(await resolveClass(robotName));

    assertClassResolved();
    getConnectionInstance().init();
}

function assertBlocklyWorkspaceLoaded() {
    if (GUISTATE_C.getBlocklyWorkspace() == null || GUISTATE_C.getBlocklyWorkspace() == undefined) {
        throw new Error('Blockly Workspace has not yet been loaded');
    }
}
function assertClassResolved() {
    if (getConnectionInstance() == null || getConnectionInstance() == undefined) {
        throw new Error('could not resolve connection module/class');
    }
}

/**
 * resolves module and loads contents for use
 * @param robotName
 */
async function resolveClass(robotName: string): Promise<ConnectionInterface> {
    let className = capitalizeFirstLetter(robotName) + 'Connection';
    let module = await import('connections');
    return new module[className]();
}

function capitalizeFirstLetter(string: string): string {
    return string[0].toUpperCase() + string.slice(1);
}

export function getConnectionInstance(): ConnectionInterface {
    return connectionInstance;
}

export function setConnectionInstance(connection: ConnectionInterface) {
    connectionInstance = connection;
}

export function setSocket(socket) {
    robot.socket = socket;
}

export function getSocket() {
    return robot.socket;
}

export function getIsAgent() {
    return robot.isAgent;
}

export function setIsAgent(isAgent: boolean) {
    robot.isAgent = isAgent;
}
