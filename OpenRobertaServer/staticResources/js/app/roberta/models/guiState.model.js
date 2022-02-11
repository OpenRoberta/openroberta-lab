define(["require", "exports", "comm"], function (require, exports, COMM) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.init = exports.robot = exports.toolbox = exports.configuration = exports.program = exports.user = exports.gui = exports.server = void 0;
    exports.server = {};
    exports.gui = {};
    exports.user = {};
    exports.program = {};
    exports.configuration = {};
    exports.toolbox = '';
    exports.robot = {};
    /**
     * Initialize gui state object
     */
    function init() {
        var ready = new $.Deferred();
        exports.server.ping = true;
        exports.server.pingTime = 3000;
        exports.gui.view = '';
        exports.gui.prevView = '';
        exports.gui.language = '';
        exports.gui.robot = '';
        exports.gui.blocklyWorkspace = '';
        exports.gui.bricklyWorkspace = '';
        exports.gui.program = {};
        exports.gui.program.toolbox = {};
        exports.gui.program.toolbox.a = '';
        exports.gui.program.prog = {};
        exports.gui.program = {};
        exports.gui.program.toolbox = {};
        exports.gui.program.prog = {};
        exports.gui.program.download = false;
        exports.gui.configuration = {};
        exports.gui.configuration.toolbox = '';
        exports.gui.configuration.conf = '';
        exports.gui.connection = '';
        exports.gui.vendor = '';
        exports.gui.sim = false;
        exports.gui.multipleSim = false;
        exports.gui.nn = false;
        exports.gui.webotsSim = false;
        exports.gui.webotsUrl = '';
        exports.gui.fileExtension = '';
        exports.gui.connectionType = {
            TOKEN: 'token',
            AUTO: 'autoConnection',
            AGENTORTOKEN: 'arduinoAgentOrToken',
            LOCAL: 'local',
            WEBVIEW: 'webview',
            JSPLAY: 'jsPlay',
        };
        exports.gui.runEnabled = false;
        exports.user.id = -1;
        exports.user.accountName = '';
        exports.user.name = '';
        exports.user.isAccountActivated = false;
        //socket.portNames = [];
        //socket.vendorIds = [];
        exports.program.name = '';
        exports.program.saved = true;
        exports.program.shared = true;
        exports.program.timestamp = '';
        exports.program.source = '';
        exports.program.xml = '';
        exports.program.toolbox = {};
        exports.program.toolbox.level = '';
        exports.program.toolbox.xml = '';
        exports.configuration.name = '';
        exports.configuration.saved = true;
        exports.configuration.timestamp = '';
        exports.configuration.xml = '';
        exports.robot.token = '';
        exports.robot.name = '';
        exports.robot.state = '';
        exports.robot.battery = '';
        exports.robot.version = '';
        exports.robot.fWName = '';
        exports.robot.sensorValues = '';
        exports.robot.nepoExitValue = 0;
        exports.robot.time = -1;
        exports.robot.robotPort = '';
        exports.robot.socket = null;
        exports.robot.hasWlan = false;
        var getInitFromServer = function () {
            COMM.setInitToken(undefined);
            return COMM.json('/init', {
                cmd: 'init',
                screenSize: [window.screen.availWidth, window.screen.availHeight],
            }, function (result) {
                if (result.rc === 'ok') {
                    COMM.setInitToken(result.initToken);
                    $.extend(exports.server, result.server);
                    exports.server.version = result['server.version'];
                    exports.server.time = result.serverTime;
                    ready.resolve();
                }
                else {
                    console.log('ERROR: ' + result.message);
                    // MSG.displayInformation(result, "", result.message);
                }
            }, 'init data from server');
        };
        getInitFromServer();
        return ready.promise();
    }
    exports.init = init;
});
