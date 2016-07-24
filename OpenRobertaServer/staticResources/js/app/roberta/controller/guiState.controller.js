define([ 'exports', 'util', 'log', 'message', 'guiState.model', 'jquery' ], function(exports, UTIL, LOG, MSG, guiState, $) {

    /**
     * Init robot
     */
    function init(language) {

        var a = $.Deferred();
        $.when(guiState.init()).then(function() {
            if ($.cookie("OpenRoberta_" + guiState.server.version)) {
                guiState.gui.cookie = $.cookie("OpenRoberta_" + guiState.server.version);
            }

            guiState.gui.view = 'tabProgram';
            guiState.gui.prevView = 'tabProgram';
            guiState.gui.language = language;
            guiState.gui.robot = guiState.gui.cookie || guiState.server.defaultRobot;

            guiState.user.id = -1;
            guiState.user.accountName = '';
            guiState.user.name = '';

            guiState.program.name = 'NEPOprog';
            guiState.program.shared = false;
            guiState.program.toolbox.level = 'beginner';

            LOG.info('init gui state');
            a.resolve();
        });
        return a;
    }

    exports.init = init;

    function setInitialState() {
        // Robot connected?
        if (guiState.robot.time < 0) {
            $('#menuRunProg').parent().addClass('disabled');
        }
        // User logged in?
        if (isUserLoggedIn) {
            $('.logout').addClass('disabled');
        }
        // Toolbox?
        $('.level').removeClass('disabled');
        $('.level.' + guiState.program.toolbox.level).addClass('disabled');
        // View?
        if (guiState.gui.view === 'tabProgram') {
            $('#head-navigation-configuration-edit').css('display', 'none');
        } else if (guiState.gui.view === 'tabConfiguration') {
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

    function setBlocklyWorkspace(workspace) {
        guiState.blocklyWorkspace = workspace;
    }
    exports.setBlocklyWorkspace = setBlocklyWorkspace;

    function setBricklyWorkspace(workspace) {
        guiState.bricklyWorkspace = workspace;
    }
    exports.setBricklyWorkspace = setBricklyWorkspace;

    function setRobot(robot, result, opt_init) {
        guiState.gui.program = result.program;
        guiState.program.saved = true;
        guiState.configuration.saved = true;
        guiState.gui.configuration = result.configuration;
        $('.robotType').removeClass('disabled');
        $('.' + robot).addClass('disabled');
        if (!opt_init) {
            $('#head-navi-icon-robot').removeClass('typcn-open');
            $('#head-navi-icon-robot').removeClass('typcn-' + guiState.gui.robot);
            $('#head-navi-icon-robot').addClass('typcn-' + robot);
        }
        guiState.gui.robot = robot;
        setConfigurationName(getRobot().toUpperCase() + 'brick');
        setProgramName('NEPOprog');
    }

    exports.setRobot = setRobot;

    function getRobot() {
        return guiState.gui.robot;
    }
    exports.getRobot = getRobot;

    function setView(view) {
        guiState.gui.prevView = guiState.gui.view;
        guiState.gui.view = view;
        $('#head-navigation-program-edit > ul > li').removeClass('disabled');
        $('#head-navigation-configuration-edit > ul > li').removeClass('disabled');
        $('.nav > li > ul > .login, .logout').removeClass('disabled');
        if (isUserLoggedIn) {
            $('.logout').addClass('disabled');
        }

        if (view === 'tabConfiguration') {
            $('#head-navigation-program-edit').css('display', 'none');
            $('#head-navigation-configuration-edit').css('display', 'inline');
            $('#menuTabProgram').parent().removeClass('disabled');
            $('#menuTabConfiguration').parent().addClass('disabled');
        } else if (view === 'tabProgram') {
            $('#head-navigation-configuration-edit').css('display', 'none');
            $('#head-navigation-program-edit').css('display', 'inline');
            $('#menuTabConfiguration').parent().removeClass('disabled');
            $('#menuTabProgram').parent().addClass('disabled');
        } else if (view === 'tabLogList' || view === 'tabConfList') {
            $('#head-navigation-program-edit > ul > li').addClass('disabled');
            $('#head-navigation-configuration-edit > ul > li').addClass('disabled');
        }
    }

    exports.setView = setView;

    function getView() {
        return guiState.gui.view;
    }

    exports.getView = getView;

    function getPrevView() {
        return guiState.gui.prevView;
    }

    exports.getPrevView = getPrevView;

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
        return guiState.program.name;
    }
    exports.getProgramName = getProgramName;

    function setProgramName(name) {
        $('#tabProgramName').html(name);
        guiState.program.name = name;
    }
    exports.setProgramName = setProgramName;

    function getConfigurationName() {
        return guiState.configuration.name;
    }
    exports.getConfigurationName = getConfigurationName;

    function setConfigurationName(name) {
        $('#tabConfigurationName').html(name);
        guiState.program.name = name;
    }
    exports.setConfigurationName = setConfigurationName;

    function setProgramToolboxLevel(level) {
        guiState.program.toolbox.level = level;
    }
    exports.setProgramToolboxLevel = setProgramToolboxLevel;

    function getProgramToolboxLevel() {
        return guiState.program.toolbox.level;
    }
    exports.getProgramToolboxLevel = getProgramToolboxLevel;

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

    function setConfigurationXML(xml) {
        guiState.configuration.xml = xml;
    }
    exports.setConfigurationXML = setConfigurationXML;

    function getConfigurationXML() {
        return guiState.configuration.xml;
    }
    exports.getConfigurationXML = getConfigurationXML;

    function setProgramXML(xml) {
        guiState.program.xml = xml;
    }
    exports.setProgramXML = setProgramXML;

    function getProgramXML() {
        return guiState.program.xml;
    }
    exports.getProgramXML = getProgramXML;

    function getRobots() {
        return guiState.server.robots;
    }
    exports.getRobots = getRobots;

    function getProgramToolbox() {
        return guiState.gui.program.toolbox[guiState.program.toolbox.level];
    }
    exports.getProgramToolbox = getProgramToolbox;

    function getConfigurationToolbox() {
        return guiState.gui.configuration.toolbox;
    }
    exports.getConfigurationToolbox = getConfigurationToolbox;

    function getProgramProg() {
        return guiState.gui.program.prog;
    }
    exports.getProgramProg = getProgramProg;

    function getConfigurationConf() {
        return guiState.gui.configuration.conf;
    }
    exports.getConfigurationConf = getConfigurationConf;

    function noCookie() {
        return !guiState.gui.cookie;
    }
    exports.noCookie = noCookie;

    function getServerVersion() {
        return guiState.server.version;
    }
    exports.getServerVersion = getServerVersion;

    function getUserName() {
        return guiState.user.name;
    }
    exports.getUserName = getUserName;

    function getUserAccountName() {
        return guiState.user.accountName;
    }
    exports.getUserAccountName = getUserAccountName;

    function setLogin(result) {
        setState(result);
        console.log(result);
        guiState.user.accountName = result.userAccountName;
        if (result.userName === undefined || result.userName === '') {
            guiState.user.name = result.userAccountName;
        } else {
            guiState.user.name = result.userName;
        }
        guiState.user.id = result.userId;

        $('.nav > li > ul > .logout').removeClass('disabled');
        $('.nav > li > ul > .login').addClass('disabled');
        $('#head-navi-icon-user').removeClass('error');
        $('#head-navi-icon-user').addClass('ok');
        guiState.bricklyWorkspace.robControls.enable('saveProgram');
    }
    exports.setLogin = setLogin;

    function setLogout() {
        guiState.user.id = -1;
        guiState.user.accountName = '';
        guiState.user.name = '';
        setProgramName('NEPOprog');
        guiState.program.shared = false;
        $('.nav > li > ul > .logout').addClass('disabled');
        $('.nav > li > ul > .login').removeClass('disabled');
        $('#head-navi-icon-user').removeClass('ok');
        $('#head-navi-icon-user').addClass('error');
        guiState.bricklyWorkspace.robControls.disable('saveProgram');
    }
    exports.setLogout = setLogout;

//        /**
//     * Set program name
//     * 
//     * @param {name}
//     *            Name to be set
//     */
//    function setProgram(result, opt_owner) {
//        if (result) {
//            guiState.user.program = result.name;
//            guiState.user.programSaved = result.programSaved;
//            guiState.user.programShared = result.programShared;
//            guiState.user.programTimestamp = result.lastChanged;
//            var name = result.name;
//            if (opt_owner) {
//                if (guiState.user.programShared == 'WRITE') {
//                    name += ' (<span class="typcn typcn-pencil progName"></span>' + opt_owner + ')';
//                } else {
//                    name += ' (<span class="typcn typcn-eye progName"></span>' + opt_owner + ')';
//                }
//            }
//            $('#tabProgramName').html(name);
//        }
//    }
//    exports.setProgram = setProgram;
});
