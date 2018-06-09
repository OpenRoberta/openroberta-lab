define([ 'exports', 'util', 'log', 'message', 'guiState.model', 'progHelp.controller', 'webview.controller', 'socket.controller', 'wedo.model', 'jquery' ], function(
        exports, UTIL, LOG, MSG, GUISTATE, HELP_C, WEBVIEW_C, SOCKET_C, WEDO, $) {

    /**
     * Init robot
     */
    function init(language, opt_data) {
        var ready = $.Deferred();
        $.when(GUISTATE.init()).then(function() {
            GUISTATE.gui.webview = opt_data || false;
            if (GUISTATE.gui.webview) {
                $('.logo').css({
                    'right' : '32px',
                });

            }
            var cookieName = "OpenRoberta_" + GUISTATE.server.version;

            if ($.cookie(cookieName)) {
                GUISTATE.gui.cookie = $.cookie(cookieName);
            }

            GUISTATE.gui.view = 'tabProgram';
            GUISTATE.gui.prevView = 'tabProgram';
            GUISTATE.gui.language = language;
            GUISTATE.gui.startWithoutPopup = false;

            GUISTATE.gui.robot = GUISTATE.gui.cookie || GUISTATE.server.defaultRobot;
            GUISTATE.gui.defaultRobot = GUISTATE.server.defaultRobot;

            GUISTATE.user.id = -1;
            GUISTATE.user.accountName = '';
            GUISTATE.user.name = '';

            GUISTATE.robot.name = '';
            GUISTATE.robot.robotPort = '';
            GUISTATE.robot.socket = null;
            GUISTATE.gui.isAgent = true;

            //GUISTATE.socket.portNames = [];
            //GUISTATE.socket.vendorIds = [];

            GUISTATE.program.name = 'NEPOprog';
            GUISTATE.program.shared = false;
            GUISTATE.program.toolbox.level = 'beginner';
            
            for (var i = 0; i < GUISTATE.server.tutorial.length; i++) {
                var tutorialPath = '../tutorial/' + GUISTATE.server.tutorial[i];
                $.getJSON(tutorialPath).done(function(data) {
                    // store the available tutorial objects
                    if (data.name) {
                        var tutorialId = data.name.toLowerCase().replace(/ /g, "");
                        GUISTATE.tutorials[tutorialId] = data;
                    } else {
                        console.error('"' + tutorialPath + '" is not a valid tutorial file! No name could be found.');
                    }
                }).fail(function(e, r) {
                    // this should not happen
                    console.error('"' + tutorialPath + '" is not a valid json file! The reason is probably a', r);
                });
            }

            if (GUISTATE.server.theme !== 'default') {
                var themePath = '../theme/' + GUISTATE.server.theme + '.json';
                $.getJSON(themePath).done(function(data) {
                    // store new theme properties (only colors so far)
                    GUISTATE.server.theme = data;
                }).fail(function(e, r) {
                    // this should not happen
                    console.error('"' + themePath + '" is not a valid json file! The reason is probably a', r);
                    GUISTATE.server.theme = 'default';
                });
            }
            LOG.info('init gui state');
            ready.resolve();
        });
        return ready.promise();
    }

    exports.init = init;

    function setInitialState() {
        // Robot connected?
        if (GUISTATE.robot.time < 0) {
            $('#menuRunProg').parent().addClass('disabled');
        }
        // User not logged in?
        $('.nav > li > ul > .login').addClass('disabled');
        $('#head-navi-icon-user').addClass('error');
        // Toolbox?
        $('.level').removeClass('disabled');
        $('.level.' + GUISTATE.program.toolbox.level).addClass('disabled');
        // View?
        if (GUISTATE.gui.view === 'tabProgram') {
            $('#head-navigation-configuration-edit').css('display', 'none');
            GUISTATE.gui.blocklyWorkspace.markFocused();
        } else if (GUISTATE.gui.view === 'tabConfiguration') {
            $('#head-navigation-program-edit').css('display', 'none');
            GUISTATE.gui.bricklyWorkspace.markFocused();
        }
        // Robot?
        $('#menu-' + GUISTATE.gui.robot).parent().addClass('disabled');
    }

    exports.setInitialState = setInitialState;

    /**
     * Set gui state
     * 
     * @param {result}
     *            result of server call
     */
    function isProgramStandard() {
        return GUISTATE.program.name == 'NEPOprog';
    }
    exports.isProgramStandard = isProgramStandard;

    function isProgramWritable() {
        if (GUISTATE.program.shared == 'WRITE') {
            return true;
        } else if (GUISTATE.program.shared == 'READ') {
            return false;
        }
        return true;
    }
    exports.isProgramWritable = isProgramWritable;
    
    function isConfigurationStandard() {
        return GUISTATE.configuration.name == getRobotGroup().toUpperCase() + 'basis';
    }
    exports.isConfigurationStandard = isConfigurationStandard;

    function getConfigurationStandardName() {
        return getRobotGroup().toUpperCase() + 'basis';
    }
    exports.getConfigurationStandardName = getConfigurationStandardName;

    function isConfigurationAnonymous() {
        return GUISTATE.configuration.name == '';
    }
    exports.isConfigurationAnonymous = isConfigurationAnonymous;

    function setState(result) {
        if (result['server.version']) {
            GUISTATE.server.version = result['server.version'];
        }
        if (result['robot.version']) {
            GUISTATE.robot.version = result['robot.version'];
        }
        if (result['robot.firmwareName'] != undefined) {
            GUISTATE.robot.fWName = result['robot.firmwareName'];
        } else {
            GUISTATE.robot.fWName = '';
        }
        if (result['robot.wait'] != undefined) {
            GUISTATE.robot.time = result['robot.wait'];
        } else {
            GUISTATE.robot.time = -1;
        }
        if (result['robot.battery'] != undefined) {
            GUISTATE.robot.battery = result['robot.battery'];
        } else {
            GUISTATE.robot.battery = '';
        }
        if (result['robot.name'] != undefined) {
            GUISTATE.robot.name = result['robot.name'];
        } else {
            GUISTATE.robot.name = '';
        }
        if (result['robot.state'] != undefined) {
            GUISTATE.robot.state = result['robot.state'];
        } else {
            GUISTATE.robot.state = '';
        }
        if (result['robot.sensorvalues'] != undefined) {
            GUISTATE.robot.sensorValues = result['robot.sensorvalues'];
        } else {
            GUISTATE.robot.sensorValues = '';
        }
        if (result['robot.nepoexitvalue'] != undefined) {
            //TODO: For different robots we have different error messages
            if (result['robot.nepoexitvalue'] !== GUISTATE.robot.nepoExitValue) {
                GUISTATE.nepoExitValue = result['robot.nepoexitvalue'];
                if (GUISTATE.nepoExitValue !== 143 && GUISTATE.robot.nepoExitValue !== 0) {
                    MSG.displayMessage('POPUP_PROGRAM_TERMINATED_UNEXPECTED', 'POPUP', '')
                }
            }
        }
        if (GUISTATE.user.accountName) {
            $('#iconDisplayLogin').removeClass('error');
            $('#iconDisplayLogin').addClass('ok');
        } else {
            $('#iconDisplayLogin').removeClass('ok');
            $('#iconDisplayLogin').addClass('error');
        }

        connectionType = getConnection();
        switch (getConnection()) {
        case GUISTATE.gui.connectionType.AGENTORTOKEN:
            if (GUISTATE.gui.isAgent === true) {
                break;
            }
        case GUISTATE.gui.connectionType.TOKEN:
            $('#menuConnect').parent().removeClass('disabled');
            if (GUISTATE.robot.state === 'wait') {
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').addClass('wait');
                GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
                $('#menuRunProg').parent().removeClass('disabled');
            } else if (GUISTATE.robot.state === 'busy') {
                $('#head-navi-icon-robot').removeClass('wait');
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').addClass('busy');
                GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
                $('#menuRunProg').parent().addClass('disabled');
            } else {
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').removeClass('wait');
                $('#head-navi-icon-robot').addClass('error');
                GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
                $('#menuRunProg').parent().addClass('disabled');
            }
            break;
        case GUISTATE.gui.connectionType.AUTO:
            break;
        case GUISTATE.gui.connectionType.LOCAL:
            break;
        case GUISTATE.gui.connectionType.AGENT:
            break;
        case GUISTATE.gui.connectionType.WEBVIEW:
            break;
        default:
            break;
        }
    }
    exports.setState = setState;

    function getIsAgent() {
        return GUISTATE.gui.isAgent;
    }
    exports.getIsAgent = getIsAgent;

    function setIsAgent(isAgent) {
        GUISTATE.gui.isAgent = isAgent;
    }
    exports.setIsAgent = setIsAgent;

    function getBlocklyWorkspace() {
        return GUISTATE.gui.blocklyWorkspace;
    }
    exports.getBlocklyWorkspace = getBlocklyWorkspace;

    function setBlocklyWorkspace(workspace) {
        GUISTATE.gui.blocklyWorkspace = workspace;
    }
    exports.setBlocklyWorkspace = setBlocklyWorkspace;

    function getBricklyWorkspace() {
        return GUISTATE.gui.bricklyWorkspace;
    }
    exports.getBricklyWorkspace = getBricklyWorkspace;

    function setBricklyWorkspace(workspace) {
        GUISTATE.gui.bricklyWorkspace = workspace;
    }
    exports.setBricklyWorkspace = setBricklyWorkspace;

    function setRobot(robot, result, opt_init) {
        // make sure we use the group instead of the specific robottype if the robot belongs to a group
        var robotGroup = findGroup(robot);
        GUISTATE.gui.program = result.program;
        GUISTATE.gui.configuration = result.configuration;
        GUISTATE.gui.sim = result.sim;
        GUISTATE.gui.connection = result.connection;
        GUISTATE.gui.vendor = result.vendor;
        GUISTATE.gui.signature = result.signature;
        GUISTATE.gui.commandLine = result.commandLine;
        GUISTATE.gui.configurationUsed = result.configurationUsed;
        GUISTATE.gui.fileExtension = result.fileExtension;
        $('#blocklyDiv, #bricklyDiv').css('background', 'url(../../../../css/img/' + robotGroup + 'Background.jpg) repeat');
        $('#blocklyDiv, #bricklyDiv').css('background-size', '100%');
        $('#blocklyDiv, #bricklyDiv').css('background-position', 'initial');

        if (!isConfigurationUsed()) {
            $('#bricklyDiv').css('background', 'url(../../../../css/img/' + robotGroup + 'BackgroundConf.svg) no-repeat');
            $('#bricklyDiv').css('background-position', 'center');
            $('#bricklyDiv').css('background-size', '75% auto');
        }

        $('.robotType').removeClass('disabled');
        $('.robotType.' + robot).addClass('disabled');
        $('#head-navi-icon-robot').removeClass('typcn-open');
        $('#head-navi-icon-robot').removeClass('typcn-' + GUISTATE.gui.robotGroup);
        $('#head-navi-icon-robot').addClass('typcn-' + robotGroup);

        checkSim();
        if (!opt_init) {
            setProgramSaved(true);
            setConfigurationSaved(true);
            if (findGroup(robot) != getRobotGroup()) {
                setConfigurationName(robotGroup.toUpperCase() + 'basis');
                setProgramName('NEPOprog');
            }
        } else {
            setConfigurationName(robotGroup.toUpperCase() + 'basis');
            setProgramName('NEPOprog');
        }

        $('#simRobot').removeClass('typcn-' + GUISTATE.gui.robotGroup);
        $('#simRobot').addClass('typcn-' + robotGroup);

        var connectionType = getConnection();
        $('#robotConnect').removeClass('disabled');
        switch (getConnection()) {
        case GUISTATE.gui.connectionType.TOKEN:
            SOCKET_C.listRobotStop();
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            if (GUISTATE.gui.blocklyWorkspace) {
                GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
            }
            $('#menuRunProg').parent().addClass('disabled');
            $('#menuConnect').parent().removeClass('disabled');
            break;
        case GUISTATE.gui.connectionType.LOCAL:
        case GUISTATE.gui.connectionType.AUTO:
            SOCKET_C.listRobotStop();
            //console.log('autoConnection');
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').addClass('wait');
            if (GUISTATE.gui.blocklyWorkspace) {
                GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
            }
            $('#menuRunProg').parent().removeClass('disabled');
            $('#menuConnect').parent().addClass('disabled');
            break;
        case GUISTATE.gui.connectionType.AGENTORTOKEN:
            SOCKET_C.listRobotStart();
            if (GUISTATE.gui.isAgent == true) {
                updateMenuStatus();
                //console.log('arduino based bobot was selected');
            } else {
                $('#menuConnect').parent().removeClass('disabled');
            }
            break;
        case GUISTATE.gui.connectionType.AGENT:
            //console.log("agent selected");
            SOCKET_C.listRobotStart();
            if (GUISTATE.gui.isAgent == true) {
                updateMenuStatus();
                //console.log('arduino based bobot was selected');
            } else {
                $('#menuConnect').parent().addClass('disabled');
            }
            break;
        case GUISTATE.gui.connectionType.WEBVIEW:
            SOCKET_C.listRobotStop();
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            if (GUISTATE.gui.blocklyWorkspace) {
                GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
            }
            $('#menuRunProg').parent().addClass('disabled');
            $('#menuConnect').parent().removeClass('disabled');
            // are we in an Open Roberta Webview
            if (inWebview()) {
                $('#robotConnect').removeClass('disabled');
            } else {
                $('#robotConnect').addClass('disabled');
            }
            break;
        default:
            //console.log('unknown connection');
            break;
        }

        var groupSwitched = false;
        if (findGroup(robot) != getRobotGroup()) {
            groupSwitched = true;
        }

        GUISTATE.gui.robot = robot;
        GUISTATE.gui.robotGroup = robotGroup;

        var value = Blockly.Msg.MENU_START_BRICK;
        if (value.indexOf("$") >= 0) {
            value = value.replace("$", getRobotRealName());
        }
        $('#menuRunProg').html(value);
        if (GUISTATE.gui.blocklyWorkspace) {
            GUISTATE.gui.blocklyWorkspace.robControls.refreshTooltips(getRobotRealName());
        }
        if (groupSwitched) {
            HELP_C.initView();
            updateTutorialMenu();
            WEBVIEW_C.jsToAppInterface({
                'target' : 'internal',
                'type' : 'setRobot',
                'robot' : robotGroup
            });
        }
    }

    exports.setRobot = setRobot;

    function findGroup(robot) {
        var robots = getRobots();
        for ( var propt in robots) {
            if (robots[propt].name == robot && robots[propt].group !== '') {
                robot = robots[propt].group;
                return robot;
            }
        }
        return robot;
    }
    exports.findGroup = findGroup;

    function findRobot(group) {
        var robots = getRobots();
        for (robot in robots) {
            if (robots[robot].group === group) {
                return robots[robot].name;
                break;
            }
        }
        return null;
    }
    exports.findRobot = findRobot;

    function setConnectionState(state) {
        switch (state) {
        case "busy":
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').addClass('busy');
            GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
            $('#menuRunProg').parent().addClass('disabled');
            break;
        case "error":
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').addClass('error');
            GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
            $('#menuRunProg').parent().addClass('disabled');
            break;
        case "wait":
            if (isRobotConnected()) {
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').addClass('wait');
                GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
                $('#menuRunProg').parent().removeClass('disabled');
            } else {
                setConnectionState('error');
            }
            break;
        default:
            break;
        }
    }
    exports.setConnectionState = setConnectionState;

    function getRobot() {
        return GUISTATE.gui.robot;
    }
    exports.getRobot = getRobot;

    function getRobotGroup() {
        return GUISTATE.gui.robotGroup;
    }
    exports.getRobotGroup = getRobotGroup;

    function setRobotPort(port) {
        GUISTATE.robot.robotPort = port;
    }
    exports.setRobotPort = setRobotPort;

    function getRobotPort() {
        return GUISTATE.robot.robotPort;
    }
    exports.getRobotPort = getRobotPort;

    function getRobotRealName() {
        for ( var robot in getRobots()) {
            if (!getRobots().hasOwnProperty(robot)) {
                continue;
            }
            if (getRobots()[robot].name == getRobot()) {
                return getRobots()[robot].realName;
            }
        }
        return getRobot();
    }
    exports.getRobotRealName = getRobotRealName;

    function getMenuRobotRealName(robotName) {
        for ( var robot in getRobots()) {
            if (!getRobots().hasOwnProperty(robot)) {
                continue;
            }
            if (getRobots()[robot].name == robotName) {
                return getRobots()[robot].realName;
            }
        }
        return "Robot not found";
    }
    exports.getMenuRobotRealName = getMenuRobotRealName;

    function getIsRobotBeta(robotName) {
        for ( var robot in getRobots()) {
            if (!getRobots().hasOwnProperty(robot)) {
                continue;
            }
            if (getRobots()[robot].name == robotName && getRobots()[robot].beta == true) {
                return true;
            }
        }
        return false;
    }
    exports.getIsRobotBeta = getIsRobotBeta;

    function getRobotInfo(robotName) {
        for ( var robot in getRobots()) {
            if (!getRobots().hasOwnProperty(robot)) {
                continue;
            }
            if (getRobots()[robot].name == robotName) {
                return getRobots()[robot].info;
            }
        }
        return "Info not found";
    }
    exports.getRobotInfo = getRobotInfo;

    function isRobotConnected() {
        if (GUISTATE.robot.time > 0) {
            return true;
        }
        if (GUISTATE.gui.connection === GUISTATE.gui.connectionType.AUTO) {
            return true;
        }
        if (GUISTATE.gui.connection === GUISTATE.gui.connectionType.AGENTORTOKEN) {
            return true;
        }
        if (GUISTATE.gui.connection === GUISTATE.gui.connectionType.WEBVIEW && WEDO.getConnectedBricks().length > 0) {
            return true;
        }
        return false;
    }
    exports.isRobotConnected = isRobotConnected;

    function isConfigurationUsed() {
        return GUISTATE.gui.configurationUsed;
    }
    exports.isConfigurationUsed = isConfigurationUsed;

    function isRobotDisconnected() {
        return GUISTATE.robot.time = -1;
    }
    exports.isRobotDisconnected = isRobotDisconnected;

    function getRobotTime() {
        return GUISTATE.robot.time;
    }
    exports.getRobotTime = getRobotTime;

    function getRobotName() {
        return GUISTATE.robot.name;
    }
    exports.getRobotName = getRobotName;

    function getRobotBattery() {
        return GUISTATE.robot.battery;
    }
    exports.getRobotBattery = getRobotBattery;

    function getRobotState() {
        return GUISTATE.robot.state;
    }
    exports.getRobotState = getRobotState;

    function getRobotVersion() {
        return GUISTATE.robot.version;
    }
    exports.getRobotVersion = getRobotVersion;

    function setView(view) {
        $('#head-navi-tooltip-program').attr('data-toggle', 'dropdown');
        $('#head-navi-tooltip-configuration').attr('data-toggle', 'dropdown');
        $('#head-navigation-program-edit').removeClass('disabled');
        $('#head-navigation-configuration-edit').removeClass('disabled');
        $(".modal").modal("hide");
        GUISTATE.gui.prevView = GUISTATE.gui.view;
        GUISTATE.gui.view = view;
        if (!isRobotConnected()) {
            $('#menuRunProg').parent().addClass('disabled');
            getBlocklyWorkspace().robControls.disable('runOnBrick');
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
        } else {
            $('#head-navi-tooltip-program').attr('data-toggle', '');
            $('#head-navi-tooltip-configuration').attr('data-toggle', '');
            $('#head-navigation-program-edit').addClass('disabled');
            $('#head-navigation-configuration-edit').addClass('disabled');
        }
    }

    exports.setView = setView;

    function getView() {
        return GUISTATE.gui.view;
    }

    exports.getView = getView;

    function getPrevView() {
        return GUISTATE.gui.prevView;
    }

    exports.getPrevView = getPrevView;

    function setLanguage(language) {
        $('#language li a[lang=' + language + ']').parent().addClass('disabled');
        $('#language li a[lang=' + GUISTATE.gui.language + ']').parent().removeClass('disabled');
        if (language === 'de') {
            $('.EN').css('display', 'none');
            $('.DE').css('display', 'inline');
            $('li>a.DE').css('display', 'block');
        } else {
            $('.DE').css('display', 'none');
            $('.EN').css('display', 'inline');
            $('li>a.EN').css('display', 'block');
        }
        GUISTATE.gui.language = language;
        HELP_C.initView();
        $('#infoContent').attr('data-placeholder', Blockly.Msg.INFO_DOCUMENTATION_HINT || 'Document your program here ...');
        $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
        updateTutorialMenu();
    }
    exports.setLanguage = setLanguage;

    function getLanguage() {
        return GUISTATE.gui.language;
    }
    exports.getLanguage = getLanguage;

    function isProgramSaved() {
        return GUISTATE.program.saved;
    }
    exports.isProgramSaved = isProgramSaved;

    function setProgramSaved(save) {
        if (save) {
            $('#menuSaveProg').parent().removeClass('disabled');
            $('#menuSaveProg').parent().addClass('disabled');
            getBlocklyWorkspace().robControls.disable('saveProgram');
        } else {
            if (isUserLoggedIn() && !isProgramStandard() && isProgramWritable()) {
                $('#menuSaveProg').parent().removeClass('disabled');
                getBlocklyWorkspace().robControls.enable('saveProgram');
            } else {
                $('#menuSaveProg').parent().removeClass('disabled');
                $('#menuSaveProg').parent().addClass('disabled');
                getBlocklyWorkspace().robControls.disable('saveProgram');
            }
        }
        GUISTATE.program.saved = save;
    }
    exports.setProgramSaved = setProgramSaved;

    function isConfigurationSaved() {
        return GUISTATE.configuration.saved;
    }
    exports.isConfigurationSaved = isConfigurationSaved;

    function setConfigurationSaved(save) {
        if (save) {
            $('#menuSaveConfig').parent().removeClass('disabled');
            $('#menuSaveConfig').parent().addClass('disabled');
            getBricklyWorkspace().robControls.disable('saveProgram');

        } else {
            if (isUserLoggedIn() && !isConfigurationStandard() && !isConfigurationAnonymous()) {
                $('#menuSaveConfig').parent().removeClass('disabled');
                getBricklyWorkspace().robControls.enable('saveProgram');
            } else {
                $('#menuSaveConfig').parent().removeClass('disabled');
                $('#menuSaveConfig').parent().addClass('disabled');
                getBricklyWorkspace().robControls.disable('saveProgram');
            }
        }
        GUISTATE.configuration.saved = save;
    }
    exports.setConfigurationSaved = setConfigurationSaved;

    function getProgramShared() {
        return GUISTATE.program.shared;
    }
    exports.getProgramShared = getProgramShared;

    function setProgramSource(source) {
        GUISTATE.program.source = source;
    }
    exports.setProgramSource = setProgramSource;

    function getProgramSource() {
        return GUISTATE.program.source;
    }
    exports.getProgramSource = getProgramSource;

    function setProgramFileExtension(fileExtension) {
        GUISTATE.gui.fileExtension = fileExtension;
    }
    exports.setProgramFileExtension = setProgramFileExtension;

    function getProgramFileExtension() {
        return GUISTATE.gui.fileExtension;
    }
    exports.getProgramFileExtension = getProgramFileExtension;

    function isUserLoggedIn() {
        return GUISTATE.user.id >= 0;
    }
    exports.isUserLoggedIn = isUserLoggedIn;

    function getProgramTimestamp() {
        return GUISTATE.program.timestamp;
    }
    exports.getProgramTimestamp = getProgramTimestamp;

    function setProgramTimestamp(timestamp) {
        GUISTATE.program.timestamp = timestamp
    }
    exports.setProgramTimestamp = setProgramTimestamp;

    function getProgramName() {
        return GUISTATE.program.name;
    }
    exports.getProgramName = getProgramName;

    function setProgramName(name) {
        $('#tabProgramName').html(name);
        GUISTATE.program.name = name;
    }
    exports.setProgramName = setProgramName;

    function getConfigurationName() {
        return GUISTATE.configuration.name;
    }
    exports.getConfigurationName = getConfigurationName;

    function setConfigurationName(name) {
        $('#tabConfigurationName').html(name);
        GUISTATE.configuration.name = name;
    }
    exports.setConfigurationName = setConfigurationName;

    function setConfigurationNameDefault() {
        setConfigurationName(getConfigurationStandardName());
    }
    exports.setConfigurationNameDefault = setConfigurationNameDefault;

    function setProgramToolboxLevel(level) {
        $('.level').removeClass('disabled');
        $('.level.' + level).addClass('disabled');
        GUISTATE.program.toolbox.level = level;
    }
    exports.setProgramToolboxLevel = setProgramToolboxLevel;

    function getProgramToolboxLevel() {
        return GUISTATE.program.toolbox.level;
    }
    exports.getProgramToolboxLevel = getProgramToolboxLevel;

    function getToolbox(level) {
        return GUISTATE.gui.program.toolbox[level];
    }
    exports.getToolbox = getToolbox;

    function getConfToolbox() {
        return GUISTATE.conf.toolbox;
    }
    exports.getConfToolbox = getConfToolbox;

    function getDefaultRobot() {
        return GUISTATE.gui.defaultRobot;
    }
    exports.getDefaultRobot = getDefaultRobot;

    function setDefaultRobot(robot) {
        GUISTATE.gui.defaultRobot = robot;
    }
    exports.setDefaultRobot = setDefaultRobot;

    function getRobotFWName() {
        return GUISTATE.robot.fWName;
    }
    exports.getRobotFWName = getRobotFWName;

    function setRobotToken(token) {
        GUISTATE.robot.token = token;
    }
    exports.setRobotToken = setRobotToken;

    function setConfigurationXML(xml) {
        GUISTATE.configuration.xml = xml;
    }
    exports.setConfigurationXML = setConfigurationXML;

    function getConfigurationXML() {
        return GUISTATE.configuration.xml;
    }
    exports.getConfigurationXML = getConfigurationXML;

    function setProgramXML(xml) {
        GUISTATE.program.xml = xml;
    }
    exports.setProgramXML = setProgramXML;

    function getProgramXML() {
        return GUISTATE.program.xml;
    }
    exports.getProgramXML = getProgramXML;

    function getRobots() {
        return GUISTATE.server.robots;
    }
    exports.getRobots = getRobots;

    function getProgramToolbox() {
        return GUISTATE.gui.program.toolbox[GUISTATE.program.toolbox.level];
    }
    exports.getProgramToolbox = getProgramToolbox;

    function getConfigurationToolbox() {
        return GUISTATE.gui.configuration.toolbox;
    }
    exports.getConfigurationToolbox = getConfigurationToolbox;

    function getProgramProg() {
        return GUISTATE.gui.program.prog;
    }
    exports.getProgramProg = getProgramProg;

    function getConfigurationConf() {
        return GUISTATE.gui.configuration.conf;
    }
    exports.getConfigurationConf = getConfigurationConf;

    function noCookie() {
        return !GUISTATE.gui.cookie;
    }
    exports.noCookie = noCookie;

    function getStartWithoutPopup() {
        return GUISTATE.gui.startWithoutPopup;
    }
    exports.getStartWithoutPopup = getStartWithoutPopup;

    function setStartWithoutPopup() {
        return GUISTATE.gui.startWithoutPopup = true;
    }
    exports.setStartWithoutPopup = setStartWithoutPopup;

    function getServerVersion() {
        return GUISTATE.server.version;
    }
    exports.getServerVersion = getServerVersion;

    function isPublicServerVersion() {
        return GUISTATE.server.isPublic;
    }
    exports.isPublicServerVersion = isPublicServerVersion;

    function getUserName() {
        return GUISTATE.user.name;
    }
    exports.getUserName = getUserName;

    function getUserAccountName() {
        return GUISTATE.user.accountName;
    }
    exports.getUserAccountName = getUserAccountName;

    function isUserAccountActivated() {
        return GUISTATE.user.isAccountActivated;
    }
    exports.isUserAccountActivated = isUserAccountActivated;

    function setLogin(result) {
        setState(result);
        GUISTATE.user.accountName = result.userAccountName;
        if (result.userName === undefined || result.userName === '') {
            GUISTATE.user.name = result.userAccountName;
        } else {
            GUISTATE.user.name = result.userName;
        }
        GUISTATE.user.id = result.userId;
        GUISTATE.user.isAccountActivated = result.isAccountActivated;

        $('.nav > li > ul > .login, .logout').removeClass('disabled');
        $('.nav > li > ul > .logout').addClass('disabled');
        $('#head-navi-icon-user').removeClass('error');
        $('#head-navi-icon-user').addClass('ok');
        $('#menuSaveProg').parent().addClass('disabled');
        $('#menuSaveConfig').parent().addClass('disabled');
        setProgramSaved(true);
        setConfigurationSaved(true);
        if (GUISTATE.gui.view == 'tabGalleryList') {
            $('#galleryList').find('button[name="refresh"]').trigger('click');
        }
    }
    exports.setLogin = setLogin;

    function setLogout() {
        GUISTATE.user.id = -1;
        GUISTATE.user.accountName = '';
        GUISTATE.user.name = '';
        setProgramName('NEPOprog');
        GUISTATE.program.shared = false;
        $('.nav > li > ul > .logout, .login').removeClass('disabled');
        $('.nav > li > ul > .login').addClass('disabled');
        $('#head-navi-icon-user').removeClass('ok');
        $('#head-navi-icon-user').addClass('error');
        if (GUISTATE.gui.view == 'tabProgList') {
            $('#tabProgram').trigger('click');
        } else if (GUISTATE.gui.view == 'tabConfList') {
            $('#tabConfiguration').trigger('click');
        } else if (GUISTATE.gui.view == 'tabGalleryList') {
            $('#galleryList').find('button[name="refresh"]').trigger('click');
        }
    }

    exports.setLogout = setLogout;

    function setProgram(result, opt_owner, opt_author) {
        if (result) {
            GUISTATE.program.name = result.name;
            GUISTATE.program.shared = result.programShared;
            GUISTATE.program.timestamp = result.lastChanged;
            setProgramSaved(true);
            setConfigurationSaved(true);
            var name = result.name;
            if (opt_owner) {
                if (opt_owner === 'Gallery' && GUISTATE.program.shared == 'X_WRITE') { // user has uploaded this program to the gallery
                    name += ' <b><span style="color:#33B8CA;" class="typcn typcn-th-large-outline progName"></span></b>';
                } else if (opt_owner === 'Gallery' && GUISTATE.program.shared == 'READ') { // user loads a program from the gallery
                    name += ' <b><span style="color:#33B8CA;" class="typcn typcn-th-large-outline progName">' + opt_author + '</span></b>';
                } else if (opt_owner === 'Roberta') { // user loads a program from the example program list
                    name += ' <b><span style="color:#33B8CA;" class="typcn typcn-roberta progName"></span></b>';
                } else if (GUISTATE.program.shared == 'WRITE') { // user loads a program, owned by another user, but with WRITE rights
                    name += ' <b><span style="color:#33B8CA;" class="typcn typcn-pencil progName"></span></b><span style="color:#33B8CA;">' + opt_owner
                            + '</span>';
                } else if (GUISTATE.program.shared == 'READ') { // user loads a program, owned by another user, but with READ rights
                    name += ' <b><span style="color:#33B8CA;" class="typcn typcn-eye progName"></span></b><span style="color:#33B8CA;">' + opt_owner
                            + '</span>';
                } else {
                    console.log("Program with undefined rights from " + opt_owner + " loaded.");
                }
            }
            $('#tabProgramName').html(name);
        }
    }
    exports.setProgram = setProgram;

    /**
     * Set program name
     * 
     * @param {name}
     *            Name to be set
     */
    function setConfiguration(result) {
        if (result) {
            setConfigurationName(result.name);
            GUISTATE.configuration.timestamp = result.lastChanged;
            setConfigurationSaved(true);
            setProgramSaved(false);
            $('#tabConfigurationName').html(result.name);
        }
    }
    exports.setConfiguration = setConfiguration;

    function checkSim() {
        if (GUISTATE.gui.sim == true) {
            $('#menuRunSim').parent().removeClass('disabled');
            $('#simButton').show();
        } else {
            $('#menuRunSim').parent().addClass('disabled');
            $('#simButton').hide();
        }
    }
    exports.checkSim = checkSim;

    function getListOfTutorials() {
        return GUISTATE.tutorials;
    }
    exports.getListOfTutorials = getListOfTutorials;

    function getConnectionTypeEnum() {
        return GUISTATE.gui.connectionType;
    }
    exports.getConnectionTypeEnum = getConnectionTypeEnum;

    function getConnection() {
        return GUISTATE.gui.connection;
    }
    exports.getConnection = getConnection;

    function getVendor() {
        return GUISTATE.gui.vendor;
    }
    exports.getVendor = getVendor;

    function getSignature() {
        return GUISTATE.gui.signature;
    }
    exports.getSignature = getSignature;

    function getCommandLine() {
        return GUISTATE.gui.commandLine;
    }
    exports.getCommandLine = getCommandLine;

    function setProgramToDownload() {
        return GUISTATE.gui.program.download = true;
    }
    exports.setProgramToDownload = setProgramToDownload;

    function isProgramToDownload() {
        return GUISTATE.gui.program.download;
    }
    exports.isProgramToDownload = isProgramToDownload;

    function setPing(ping) {
        GUISTATE.server.ping = ping;
    }
    exports.setPing = setPing;

    function doPing() {
        return GUISTATE.server.ping;
    }
    exports.doPing = doPing;

    function setSocket(socket) {
        GUISTATE.robot.socket = socket;
    }
    exports.setSocket = setSocket;

    function getSocket() {
        return GUISTATE.robot.socket;
    }
    exports.getSocket = getSocket;

    function getAvailableHelp() {
        return GUISTATE.server.help;
    }
    exports.getAvailableHelp = getAvailableHelp;

    function getTheme() {
        return GUISTATE.server.theme;
    }
    exports.getTheme = getTheme;

    function inWebview() {
        return GUISTATE.gui.webview || false;
    }
    exports.inWebview = inWebview;

    function setWebview(webview) {
        GUISTATE.gui.webview = webview;
    }
    exports.setWebview = setWebview;

    function updateMenuStatus() {
        // TODO revice this function, because isAgent is the exception
        switch (SOCKET_C.getPortList().length) {
        case 0:
            if (getConnection() !== GUISTATE.gui.connectionType.AGENTORTOKEN) {
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').removeClass('wait');
                if (GUISTATE.gui.blocklyWorkspace) {
                    GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
                }
                $('#menuRunProg').parent().addClass('disabled');
                $('#menuConnect').parent().addClass('disabled');
                setIsAgent(true);
            } else {
                setIsAgent(false);
            }
            break;
        case 1:
            setIsAgent(true);
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').addClass('wait');
            if (GUISTATE.gui.blocklyWorkspace) {
                GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
            }
            $('#menuRunProg').parent().removeClass('disabled');
            $('#menuConnect').parent().addClass('disabled');
            break;
        default:
            setIsAgent(true);
            // Always:
            $('#menuConnect').parent().removeClass('disabled');
            // If the port is not chosen:
            if (getRobotPort() == "") {
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').removeClass('wait');
                if (GUISTATE.gui.blocklyWorkspace) {
                    GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
                }
                $('#menuRunProg').parent().addClass('disabled');
                //$('#menuConnect').parent().addClass('disabled');
            } else {
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').addClass('wait');
                if (GUISTATE.gui.blocklyWorkspace) {
                    GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
                }
                $('#menuRunProg').parent().removeClass('disabled')
            }
            break;
        }
    }
    exports.updateMenuStatus = updateMenuStatus;

    function updateTutorialMenu() {
        $('#head-navigation-tutorial-dropdown li').hide();
        var length = 0;
        if (getLanguage() === 'de') {
            $('#head-navigation-tutorial-dropdown li.DE.' + getRobotGroup()).show();
            length = $('#head-navigation-tutorial-dropdown li.DE.' + getRobotGroup()).length;
        } else {
            $('#head-navigation-tutorial-dropdown li.EN.' + getRobotGroup()).show();
            length = $('#head-navigation-tutorial-dropdown li.EN.' + getRobotGroup()).length;
        }
        if (length > 0) {
            $('#head-navigation-tutorial').show();
        } else {
            $('#head-navigation-tutorial').hide();
        }
    }
    exports.updateTutorialMenu = updateTutorialMenu;
});
