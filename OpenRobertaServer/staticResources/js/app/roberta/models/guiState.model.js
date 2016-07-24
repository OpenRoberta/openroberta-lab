define([ 'exports', 'util', 'comm' ], function(exports, UTIL, COMM) {

    /**
     * Initialize gui state object
     */
    function init() {

        exports.server = {};
        exports.server.doPing = true;

        exports.gui = {};
        exports.gui.view = '';
        exports.gui.prevView = '';
        exports.gui.language = '';
        exports.gui.robot = '';
        exports.gui.blocklyWorkspace = '';
        exports.gui.bricklyWorkspace = '';

        exports.user = {};
        exports.user.id = -1;
        exports.user.accountName = '';
        exports.user.name = '';

        exports.program = {};
        exports.program.name = '';
        exports.program.saved = true;
        exports.program.shared = true;
        exports.program.timestamp = '';
        exports.program.xml = '';
        exports.program.toolbox = {};
        exports.program.toolbox.level = '';
        exports.program.toolbox.xml = '';

        exports.configuration = {};
        exports.configuration.name = '';
        exports.configuration.saved = true;
        exports.configuration.timestamp = '';
        exports.configuration.xml = '';

        exports.toolbox = '';

        exports.robot = {};
        exports.robot.token = '';
        exports.robot.name = '';
        exports.robot.state = '';
        exports.robot.battery = '';
        exports.robot.version = '';
        exports.robot.fWName = '';
        exports.robot.sensorValues = '';
        exports.robot.nepoExitValue = 0;
        exports.robot.time = -1;
        return COMM.json("/admin", {
            "cmd" : "init"
        }, function(result) {
            if (result.rc === 'ok') {
                $.extend(exports.server, result.server);
                exports.server.version = result["server.version"];
                exports.server.time = result.serverTime;
            }
        }, 'init gui state model');
    }
    exports.init = init;
});
