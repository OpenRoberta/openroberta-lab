define([ 'exports' ], function(exports) {

    /**
     * Initialize gui state object
     */
    function init() {
        exports.server = {};
        exports.server.version = '';
        exports.server.robots = [];
        exports.server.defaultRobot = [];
        exports.server.time = '';
        exports.server.doPing = true;

        // available views - program, programList, conf, confList, log
        exports.gui = {};
        exports.gui.view = '';
        exports.gui.prevView = '';
        exports.gui.language = '';
        exports.gui.robot = '';

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

        exports.conf = {};
        exports.conf.name = '';
        exports.conf.saved = true;
        exports.conf.timestamp = '';
        exports.conf.xml = '';

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

    }
    exports.init = init;
});
