import * as COMM from 'comm';

export const server: any = {};
export const gui: any = {};
export const user: any = {};
export const program: any = {};
export const configuration: any = {};
export const toolbox = '';
export const robot: any = {};

/**
 * Initialize gui state object
 */
export function init() {
    let ready: JQuery.Deferred<any, any, any> = $.Deferred();
    //TODO changed default ping behaviour because we dont need to ping on init
    server.ping = true;
    server.pingTime = 3000;
    server.robotsByName = {};

    gui.view = '';
    gui.prevView = '';
    gui.language = '';
    gui.robot = '';
    gui.blocklyWorkspace = '';
    gui.bricklyWorkspace = '';
    gui.program = {};
    gui.program.toolbox = {};
    gui.program.toolbox.a = '';
    gui.program.prog = {};
    gui.program = {};
    gui.program.toolbox = {};
    gui.program.prog = {};
    gui.program.download = false;
    gui.configuration = {};
    gui.configuration.toolbox = '';
    gui.configuration.conf = '';
    gui.vendor = '';
    gui.sim = false;
    gui.multipleSim = false;
    gui.markerSim = false;
    gui.pluginSim = '';
    gui.nn = false;
    gui.nnActivations = {};
    gui.webotsSim = false;
    gui.webotsUrl = '';
    gui.fileExtension = '';
    gui.runEnabled = false;

    user.id = -1;
    user.accountName = '';
    user.name = '';
    user.isAccountActivated = false;

    program.name = '';
    program.saved = true;
    program.shared = true;
    program.timestamp = '';
    program.source = '';
    program.xml = '';
    program.toolbox = {};
    program.toolbox.level = '';
    program.toolbox.xml = '';

    configuration.name = '';
    configuration.saved = true;
    configuration.timestamp = '';
    configuration.xml = '';

    robot.token = '';
    robot.name = '';
    robot.state = '';
    robot.battery = '';
    robot.version = '';
    robot.fWName = '';
    robot.sensorValues = '';
    robot.nepoExitValue = 0;
    robot.time = -1;
    robot.robotPort = '';
    robot.url = '';

    let getInitFromServer = function() {
        COMM.setInitToken(undefined);
        return COMM.json(
            '/init',
            {
                cmd: 'init',
                screenSize: [window.screen.availWidth, window.screen.availHeight]
            },
            function(result): void {
                if (result.rc === 'ok') {
                    COMM.setInitToken(result.initToken);
                    $.extend(server, result.server);
                    for (let key in server.robots) {
                        server.robotsByName[server.robots[key].name] = server.robots[key];
                    }
                    server.version = result['server.version'];
                    server.time = result.serverTime;
                    ready.resolve();
                } else {
                    console.log('ERROR: ' + result.message);
                    // MSG.displayInformation(result, "", result.message);
                }
            },
            'init data from server'
        );
    };
    getInitFromServer();

    return ready.promise();
}
