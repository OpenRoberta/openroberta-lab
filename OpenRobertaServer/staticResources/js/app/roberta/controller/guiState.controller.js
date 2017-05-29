define([ 'exports', 'util', 'log', 'message', 'guiState.model', 'socket.controller', 'jquery' ], function(exports, UTIL, LOG, MSG, GUISTATE, SOCKET_C, $) {

    /**
     * Init robot
     */
    function init(language) {

        var ready = $.Deferred();
        $.when(GUISTATE.init()).then(function() {
            if ($.cookie("OpenRoberta_" + GUISTATE.server.version)) {
                GUISTATE.gui.cookie = $.cookie("OpenRoberta_" + GUISTATE.server.version);
            }

            GUISTATE.gui.view = 'tabProgram';
            GUISTATE.gui.prevView = 'tabProgram';
            GUISTATE.gui.language = language;

            GUISTATE.gui.robot = GUISTATE.gui.cookie || GUISTATE.server.defaultRobot;

            GUISTATE.user.id = -1;
            GUISTATE.user.accountName = '';
            GUISTATE.user.name = '';

            GUISTATE.robot.name = '';
            GUISTATE.robot.robotPort = '';
            GUISTATE.gui.isAgent = false;

            //GUISTATE.socket.portNames = [];
            //GUISTATE.socket.vendorIds = [];

            GUISTATE.program.name = 'NEPOprog';
            GUISTATE.program.shared = false;
            GUISTATE.program.toolbox.level = 'beginner';

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
        } else if (GUISTATE.gui.view === 'tabConfiguration') {
            $('#head-navigation-program-edit').css('display', 'none');
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
        if (GUISTATE.program.shared == 'READ') {
            return false;
        }
        return true;
    }
    exports.isProgramWritable = isProgramWritable;

    function isConfigurationStandard() {
        return GUISTATE.configuration.name == getRobot().toUpperCase() + 'basis';
    }
    exports.isConfigurationStandard = isConfigurationStandard;

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

        switch (getConnection()) {
        case 'arduinoAgentOrToken':
        case 'token':
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
        case 'autoConnection':
            break;
        case 'arduinoAgent':
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

    function getBricklyWorkspace(workspace) {
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
        GUISTATE.gui.configurationUsed = result.configurationUsed;
        $('#blocklyDiv, #bricklyDiv').css('background', 'url(../../../../css/img/' + robotGroup + 'Background.jpg) repeat');
        $('#blocklyDiv, #bricklyDiv').css('background-size', '100%');
        $('#blocklyDiv, #bricklyDiv').css('background-position', 'initial');
        
        if (!isConfigurationUsed()) {
            $('#bricklyDiv').css('background', 'url(../../../../css/img/' + robotGroup + 'BackgroundConf.svg) no-repeat');
            $('#bricklyDiv').css('background-position', 'center');
            $('#bricklyDiv').css('background-size', '75% auto');
        }
        
        $('.robotType').removeClass('disabled');
        $('.' + robot).addClass('disabled');
        $('#head-navi-icon-robot').removeClass('typcn-open');
        $('#head-navi-icon-robot').removeClass('typcn-' + GUISTATE.gui.robotGroup);
        $('#head-navi-icon-robot').addClass('typcn-' + robotGroup);
        
        if (!opt_init) {
            setProgramSaved(true);
            setConfigurationSaved(true);
            checkSim();
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
        
        switch (getConnection()) {
        case 'token':
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            if (GUISTATE.gui.blocklyWorkspace) {
                GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
            }
            $('#menuRunProg').parent().addClass('disabled');
            $('#menuConnect').parent().removeClass('disabled');

            break;
        case 'autoConnection':
            console.log('autoConnection');
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').addClass('wait');
            if (GUISTATE.gui.blocklyWorkspace) {
                GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
            }
            $('#menuRunProg').parent().removeClass('disabled');
            $('#menuConnect').parent().addClass('disabled');
            break;
        case 'arduinoAgentOrToken':
        	 SOCKET_C.init();
             if (GUISTATE.isAgent == true){
                 SOCKET_C.updateMenuStatus();
                 console.log('arduino based bobot was selected');
             }
             else{
                 $('#menuConnect').parent().removeClass('disabled');
             }
             break;
        case 'arduinoAgent':
            SOCKET_C.init();
            if (GUISTATE.isAgent == true){
                SOCKET_C.updateMenuStatus();
                console.log('arduino based bobot was selected');
            }
            break;
        default:
            console.log('unknown connection');
            break;
        }
        
        GUISTATE.gui.robot = robot;
        GUISTATE.gui.robotGroup = robotGroup;

        var value = Blockly.Msg.MENU_START_BRICK;
        if (value.indexOf("$") >= 0) {
            value = value.replace("$", getRobotRealName());
        }
        $('#menuRunProg').text(value);
        if (GUISTATE.gui.blocklyWorkspace) {
            GUISTATE.gui.blocklyWorkspace.robControls.refreshTooltips(getRobotRealName());
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

    function setAutoConnectedBusy(busy) {
        if (busy) {
            GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
            $('#menuRunProg').parent().addClass('disabled');
        } else {
            GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
            $('#menuRunProg').parent().removeClass('disabled');
        }
    }
    exports.setAutoConnectedBusy = setAutoConnectedBusy;

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

    function isRobotConnected() {
        return GUISTATE.robot.time > 0 || GUISTATE.gui.connection;
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
        GUISTATE.gui.prevView = GUISTATE.gui.view;
        GUISTATE.gui.view = view;
        $('#head-navigation-program-edit > ul > li').removeClass('disabled');
        $('#head-navigation-configuration-edit > ul > li').removeClass('disabled');
        $('.nav > li > ul').removeClass('disabled');
        $('.level.' + GUISTATE.program.toolbox.level).addClass('disabled');
        if (isUserLoggedIn()) {
            $('.nav > li > ul > .logout').addClass('disabled');
            if (isProgramSaved()) {
                $('#menuSaveProg').parent().addClass('disabled');
            }
            if (isConfigurationSaved()) {
                $('#menuSaveConfig').parent().addClass('disabled');
            }
        } else {
            $('.nav > li > ul > .login').addClass('disabled');
        }
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
        } else if (view === 'tabProgList') {
            $('#head-navigation-program-edit > ul > li').addClass('disabled');
            $('#head-navigation-configuration-edit > ul > li').addClass('disabled');
        } else if (view === 'tabConfList') {
            $('#head-navigation-program-edit > ul > li').addClass('disabled');
            $('#head-navigation-configuration-edit > ul > li').addClass('disabled');
        } else if (view === 'tabLogList') {
            $('#head-navigation-program-edit > ul > li').addClass('disabled');
            $('#head-navigation-configuration-edit > ul > li').addClass('disabled');
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
        } else {
            $('.DE').css('display', 'none');
            $('.EN').css('display', 'inline');
        }
        GUISTATE.gui.language = language;
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
                $('#menuSaveProg').parent().addClass('disabled');
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
            if (isUserLoggedIn() && !isConfigurationStandard()) {
                $('#menuSaveConfig').parent().removeClass('disabled');
                getBricklyWorkspace().robControls.enable('saveProgram');
            } else {
                $('#menuSaveConfig').parent().addClass('disabled');
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
        GUISTATE.program.fileExtension = fileExtension;
    }
    exports.setProgramFileExtension = setProgramFileExtension;

    function getProgramFileExtension() {
        return GUISTATE.program.fileExtension;
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

    /*
     * function getSocketPorts() { return GUISTATE.socket.portNames; }
     * exports.getSocketPorts = getSocketPorts;
     * 
     * function setSocketPorts(ports) { GUISTATE.socket.portNames = ports; }
     * exports.setSocketPorts = setSocketPorts;
     * 
     * function getSocketVendorIds() { return GUISTATE.socket.vendorIds; }
     * exports.getSocketVendorIds = getSocketVendorIds;
     * 
     * function setSocketVendorIds(vendorIds) { GUISTATE.socket.vendorIds =
     * vendorIds; } exports.setSocketVendorIds = setSocketVendorIds;
     */

    function getConfigurationName() {
        return GUISTATE.configuration.name;
    }
    exports.getConfigurationName = getConfigurationName;

    function setConfigurationName(name) {
        $('#tabConfigurationName').html(name);
        GUISTATE.configuration.name = name;
    }
    exports.setConfigurationName = setConfigurationName;

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

    function getGuiRobot() {
        return GUISTATE.gui.robot;
    }
    exports.getGuiRobot = getGuiRobot;

    function setGuiRobot(robot) {
        GUISTATE.gui.robot = robot;
    }
    exports.setGuiRobot = getGuiRobot;

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

    function getServerVersion() {
        return GUISTATE.server.version;
    }
    exports.getServerVersion = getServerVersion;

    function getUserName() {
        return GUISTATE.user.name;
    }
    exports.getUserName = getUserName;

    function getUserAccountName() {
        return GUISTATE.user.accountName;
    }
    exports.getUserAccountName = getUserAccountName;

    function setLogin(result) {
        setState(result);
        GUISTATE.user.accountName = result.userAccountName;
        if (result.userName === undefined || result.userName === '') {
            GUISTATE.user.name = result.userAccountName;
        } else {
            GUISTATE.user.name = result.userName;
        }
        GUISTATE.user.id = result.userId;

        $('.nav > li > ul > .login, .logout').removeClass('disabled');
        $('.nav > li > ul > .logout').addClass('disabled');
        $('#head-navi-icon-user').removeClass('error');
        $('#head-navi-icon-user').addClass('ok');
        $('#menuSaveProg').parent().addClass('disabled');
        $('#menuSaveConfig').parent().addClass('disabled');
        setProgramSaved(true);
        setConfigurationSaved(true);

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
//        setProgramSaved(true);
//        setConfigurationSaved(true);
        if (GUISTATE.gui.view == 'tabProgList') {
            $('#tabProgram').trigger('click');
        } else if (GUISTATE.gui.view == 'tabConfList') {
            $('#tabConfiguration').trigger('click');
        }
    }
    exports.setLogout = setLogout;

    function setProgram(result, opt_owner) {
        if (result) {
            GUISTATE.program.name = result.name;
            GUISTATE.program.shared = result.programShared;
            GUISTATE.program.timestamp = result.lastChanged;
            setProgramSaved(true);
            var name = result.name;
            if (opt_owner) {
                if (GUISTATE.program.shared == 'WRITE') {
                    name += ' (<span class="typcn typcn-pencil progName"></span>' + opt_owner + ')';
                } else {
                    name += ' (<span class="typcn typcn-eye progName"></span>' + opt_owner + ')';
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
            GUISTATE.configuration.name = result.name;
            GUISTATE.configuration.timestamp = result.lastChanged;
            setConfigurationSaved(true);
            $('#tabConfigurationName').html(result.name);
        }
    }
    exports.setConfiguration = setConfiguration;

    function checkSim() {
        if (GUISTATE.gui.sim == true) {
            $('#menuRunSim').parent().removeClass('disabled');
            $('#progSim').show();
        } else {
            $('#menuRunSim').parent().addClass('disabled');
            $('#progSim').hide();
        }
    }
    exports.checkSim = checkSim;

    function getConnection() {
        return GUISTATE.gui.connection;
    }
    exports.getConnection = getConnection;

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
});
