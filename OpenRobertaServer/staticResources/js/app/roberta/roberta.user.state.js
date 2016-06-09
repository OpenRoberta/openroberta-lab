define([ 'exports', 'util', 'rest.user' ], function(exports, UTIL, USER) {

    /**
     * Initialize user-state-object
     */
    function initUserState() {
        USER.clear(function(result) {
            UTIL.response(result);
        });

        exports.version = 'xx.xx.xx';
        exports.language = 'DE';
        exports.robot = 'ev3';
        exports.id = -1;
        exports.accountName = '';
        exports.name = '';

        exports.program = 'NEPOprog';
        exports.programSaved = false;
        exports.programShared = false;
        exports.programTimestamp = '';
        exports.programSource = '';

        exports.configuration = 'EV3basis';
        exports.configurationSaved = false;

        exports.toolbox = 'beginner';
        exports.token = '1A2B3C4D';
        exports.doPing = true;

        exports.robotName = '';
        exports.robotState = '';
        exports.robotBattery = '';
        exports.robotWait = '';
        exports.robotVersion = '';
        exports.robotFWName = ''
        exports.serverVersion = '';
        exports.sensorValues = '';

        exports.bricklyReady = false;
        exports.blocklyReady = false;
        exports.blocklyTranslated = false;
        exports.bricklyTranslated = false;

        exports.nepoExitValue = 0;
    }
    exports.initUserState = initUserState;
});
