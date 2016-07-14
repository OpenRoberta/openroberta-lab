define([ 'exports', 'util', 'log', 'message', 'guiState.model', 'jquery' ], function(exports, UTIL, LOG, MSG, guiState, $) {

    /**
     * Init robot
     */
    function init(result, language) {

        guiState.init();

        if (result.rc === 'ok') { // TODO evaluate the result
            guiState.server.version = result.serverVersion;
            guiState.server.robots = result.serverRobots;
            guiState.server.defaultRobot = result.serverDefaultRobot;
            guiState.server.time = 'CEST';
            guiState.server.doPing = true;

            guiState.gui.view = 'tabProgram';
            guiState.gui.prevView = 'tabProgram';
            guiState.gui.language = language;
            guiState.gui.robot = guiState.server.defaultRobot;

            guiState.user.id = -1;
            guiState.user.accountName = '';
            guiState.user.name = '';

            guiState.program.name = 'NEPOprog';
            guiState.program.shared = false;
            guiState.program.toolbox.level = 'beginner';

            guiState.conf.name = guiState.server.defaultRobot.toUpperCase() + 'basis';
        }
        LOG.info('init gui state');
    }

    exports.init = init;

    function setInitialState() {
        // Robot connected?
        if (guiState.robot.time < 0) {
            $('#menuRunProg').parent().addClass('disabled');
        }
        // User logged in?
        if (guiState.user.id === -1) {
            $('.logout').addClass('disabled');
        }
        // Toolbox?
        if (guiState.toolbox === 'beginner') {
            $('#menuToolboxBeginner').parent().addClass('disabled');
        } else {
            $('#menuToolboxExpert').parent().addClass('disabled');
        }
        // View?
        if (guiState.gui.view === 'program') {
            $('#head-navigation-configuration-edit').css('display', 'none');
        } else if (guiState.gui.view === 'conf') {
            $('#head-navigation-program-edit').css('display', 'none');
        }
        // Robot?
        $('#menu-' + guiState.gui.robot).parent().addClass('disabled');
    }

    exports.setInitialState = setInitialState;

    /**
     * Set gui state
     * 
     * @param {result}
     *            result of server call
     */
    function setState(result) {
        if (result['server.version']) {
            guiState.server.version = result['server.version'];
        }
        if (result['robot.version']) {
            guiState.robot.version = result['robot.version'];
        }
        if (result['robot.firmwareName'] != undefined) {
            guiState.robot.fWName = result['robot.firmwareName'];
        } else {
            guiState.robot.fWName = '';
        }
        if (result['robot.wait'] != undefined) {
            guiState.robot.time = result['robot.wait'];
        } else {
            guiState.robot.time = '';
        }
        if (result['robot.battery'] != undefined) {
            guiState.robot.battery = result['robot.battery'];
        } else {
            guiState.robot.battery = '';
        }
        if (result['robot.name'] != undefined) {
            guiState.robot.name = result['robot.name'];
        } else {
            guiState.robot.name = '';
        }
        if (result['robot.state'] != undefined) {
            guiState.robot.state = result['robot.state'];
        } else {
            guiState.robot.state = '';
        }
        if (result['robot.sensorvalues'] != undefined) {
            guiState.robot.sensorValues = result['robot.sensorvalues'];
        } else {
            guiState.robot.sensorValues = '';
        }
        if (result['robot.nepoexitvalue'] != undefined) {
            //TODO: For different robots we have different error messages
            if (result['robot.nepoexitvalue'] !== guiState.robot.nepoExitValue) {
                guiState.nepoExitValue = result['robot.nepoexitvalue'];
                if (guiState.nepoExitValue !== 143 && guiState.robot.nepoExitValue !== 0) {
                    MSG.displayMessage('POPUP_PROGRAM_TERMINATED_UNEXPECTED', 'POPUP', '')
                }
            }
        }
        if (guiState.user.accountName) {
            $('#iconDisplayLogin').removeClass('error');
            $('#iconDisplayLogin').addClass('ok');
        } else {
            $('#iconDisplayLogin').removeClass('ok');
            $('#iconDisplayLogin').addClass('error');
        }
        if (guiState.robot.state === 'wait') {
            $('#iconDisplayRobotState').removeClass('error');
            $('#iconDisplayRobotState').removeClass('busy');
            $('#iconDisplayRobotState').addClass('wait');
            //ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.enable('runOnBrick');
            $('#menuRunProg').parent().removeClass('disabled');

        } else if (guiState.robot.state === 'busy') {
            $('#iconDisplayRobotState').removeClass('wait');
            $('#iconDisplayRobotState').removeClass('error');
            $('#iconDisplayRobotState').addClass('busy');
            //ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.disable('runOnBrick');
            $('#menuRunProg').parent().addClass('disabled');

        } else {
            $('#iconDisplayRobotState').removeClass('busy');
            $('#iconDisplayRobotState').removeClass('wait');
            $('#iconDisplayRobotState').addClass('error');
            //ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.disable('runOnBrick');
            $('#menuRunProg').parent().addClass('disabled');
        }

    }
    exports.setState = setState;

    function setRobot(robot) {

        $('.robotType').removeClass('disabled');
        $('.' + robot).addClass('disabled');
        $('#head-navi-icon-robot').removeClass('typcn-open');
        $('#head-navi-icon-robot').removeClass('typcn-' + guiState.gui.robot);
        $('#head-navi-icon-robot').addClass('typcn-' + robot);
        guiState.gui.robot = robot;
        guiState.program.saved = true;
        guiState.conf.saved = true;
    }

    exports.setRobot = setRobot;

    function getRobot() {
        return guiState.gui.robot;
    }
    exports.getRobot = getRobot;

    function setView(view) {
        guiState.gui.prevView = guiState.gui.view;
        guiState.gui.view = view;
        if (view === 'tabConfiguration') {
            $('#head-navigation-program-edit').css('display', 'none');
            $('#head-navigation-configuration-edit').css('display', 'inline');
            $('#menuTabProgram').parent().removeClass('disabled');
            $('#menuTabConfiguration').parent().addClass('disabled');
        }
    }

    exports.setView = setView;

    function getPrevView() {

        return guiState.gui.prevView;
    }

    exports.getPrevView = getPrevView;

    function setUser(result) {
        setState(result);
        guiState.user.accountName = result.userAccountName;
        if (result.userName === undefined || result.userName === '') {
            guiState.user.name = result.userAccountName;
        } else {
            guiState.user.name = result.userName;
        }
        guiState.user.id = result.userId;

        $('.nav > li > ul > .logout').removeClass('disabled');
//        if (state === 'login') {
//            $('.nav > li > ul > .login').addClass('disabled');
//        } else if (state === 'logout') {
//            $('.nav > li > ul > .logout').addClass('disabled');
//        }
    }
    exports.setUser = setUser;

    function setLanguage(language) {
        $('#language li a[lang=' + language + ']').parent().addClass('disabled');
        $('#language li a[lang=' + guiState.gui.language + ']').parent().removeClass('disabled');
        if (language === 'de') {
            $('.EN').css('display', 'none');
            $('.DE').css('display', 'inline');
        } else {
            $('.DE').css('display', 'none');
            $('.EN').css('display', 'inline');
        }
        guiState.gui.language = language;
    }
    exports.setLanguage = setLanguage;

    function getLanguage() {
        return guiState.gui.language;
    }
    exports.getLanguage = getLanguage;

    function isProgramSaved() {
        return guiState.program.saved;
    }
    exports.isProgramSaved = isProgramSaved;

    function setProgramSaved(save) {
        return guiState.program.saved = save;
    }
    exports.setProgramSaved = setProgramSaved;

    function isConfSaved() {
        return guiState.conf.saved;
    }
    exports.isConfSaved = isConfSaved;

    function setConfSaved(save) {
        return guiState.conf.saved = save;
    }
    exports.setConfSaved = setConfSaved;

    function getProgramShared() {
        return guiState.program.shared;
    }
    exports.getProgramShared = getProgramShared;

    function isUserLoggedIn() {
        return guiState.user.id >= 0;
    }
    exports.isUserLoggedIn = isUserLoggedIn;

    function getProgramName() {
        return exports.program.name;
    }
    exports.getProgramName = getProgramName;

    function setProgramToolboxLevel(level) {
        guiState.program.toolbox.level = level;
    }
    exports.setProgramToolboxLevel = setProgramToolboxLevel;

    function getProgramToolboxLevel() {
        return guiState.program.toolbox.level;
    }
    exports.getProgramToolboxLevel = getProgramToolboxLevel;

    function setProgramToolbox(xml) {
        guiState.program.toolbox = xml;
    }
    exports.setProgramToolbox = setProgramToolbox;

    function getProgramToolbox() {
        return guiState.program.toolbox;
    }
    exports.getProgramToolbox = getProgramToolbox;

    function setConfToolbox(xml) {
        guiState.conf.toolbox = xml;
    }
    exports.setConfToolbox = setConfToolbox;

    function getConfToolbox() {
        return guiState.conf.toolbox;
    }
    exports.getConfToolbox = getConfToolbox;

    function getGuiRobot() {
        return guiState.gui.robot;
    }
    exports.getGuiRobot = getGuiRobot;

    function setGuiRobot(robot) {
        guiState.gui.robot = robot;
    }
    exports.setGuiRobot = getGuiRobot;

    function setConfXML(xml) {
        guiState.conf.xml = xml;
    }
    exports.setConfXML = setConfXML;
    
    function getConfXML() {
        return guiState.conf.xml;
    }
    exports.getConfXML = getConfXML;
});
