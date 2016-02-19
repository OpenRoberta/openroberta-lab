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
        exports.configuration = 'EV3basis';
        exports.programSaved = false;
        exports.configurationSaved = false;
        exports.programModified = false;
        exports.programShared = false;
        exports.programTimestamp = '';
        exports.configurationModified = false;
        exports.toolbox = 'beginner';
        exports.token = '1A2B3C4D';
        exports.doPing = true;
        exports.robotName = '';
        exports.robotState = '';
        exports.robotBattery = '';
        exports.robotWait = '';
        exports.sensorValues = '';
        exports.robotVersion = '';
        exports.serverVersion = '';
        exports.programBlocks = null;
        exports.programBlocksSaved = null;
        exports.bricklyReady = false;
        exports.blocklyReady = false;
        exports.blocklyTranslated = false;
        exports.bricklyTranslated = false;
    }

    exports.initUserState = initUserState;
});
