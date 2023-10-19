import * as UTIL from 'util.roberta';
import * as MSG from 'message';
import * as GUISTATE from 'guiState.model';
import * as HELP_C from 'progHelp.controller';
import * as LEGAL_C from 'legal.controller';
import * as WEBVIEW_C from 'webview.controller';
import * as CV from 'confVisualization';
import * as $ from 'jquery';
//@ts-ignore
import * as Blockly from 'blockly';
import * as NOTIFICATION_C from 'notification.controller';
import * as CONNECTION_C from 'connection.controller';
import * as PROGRAM_C from 'program.controller';
import * as CONFIGURATION_C from 'configuration.controller';
import * as USER_C from 'user.controller';
import * as NN_C from 'nn.controller';
import { switchLanguage as PROGLIST_C_switchLanguage } from 'progList.controller';
import { switchLanguage as CONFLIST_C_switchLanguage } from 'confList.controller';
import { switchLanguage as GALLERYLIST_C_switchLanguage } from 'galleryList.controller';
import { switchLanguage as TUTORIALLIST_C_switchLanguage } from 'tutorialList.controller';
import { switchLanguage as LOGLIST_C_switchLanguage } from 'logList.controller';
import { switchLanguage as PROGINFO_C_switchLanguage } from 'progInfo.controller';
import * as ACE_EDITOR from 'aceEditor';
import { SetRobotResponse } from '../ts/restEntities';
import { RobotBase } from 'robot.base';

export let LONG: number = 300000; // Ping time 5min
export let SHORT: number = 3000; // Ping time 3sec

/**
 * Init robot
 */
export function init(language: string, opt_data: boolean) {
    let ready: JQuery.Deferred<any, any, any> = $.Deferred();
    $.when(GUISTATE.init()).then(function(): void {
        GUISTATE.gui.webview = opt_data || false;
        if (GUISTATE.gui.webview) {
            $('.logo').css({
                right: '32px'
            });
        }

        GUISTATE.gui.view = 'tabProgram';
        GUISTATE.gui.prevView = 'tabProgram';
        GUISTATE.gui.language = language;
        GUISTATE.gui.startWithoutPopup = false;

        GUISTATE.user.id = -1;
        GUISTATE.user.accountName = '';
        GUISTATE.user.name = '';

        GUISTATE.robot.name = '';
        GUISTATE.robot.robotPort = '';

        GUISTATE.program.toolbox.level = 'beginner';
        setProgramOwnerName(null);
        setProgramAuthorName(null);
        setProgramShareRelation(null);
        setProgramName('NEPOprog');

        if (GUISTATE.server.theme !== 'default') {
            let themePath: string = '../theme/' + GUISTATE.server.theme + '.json';
            $.getJSON(themePath)
                .done(function(data): void {
                    // store new theme properties (only colors so far)
                    GUISTATE.server.theme = data;
                })
                .fail(function(e, r): void {
                    // this should not happen
                    console.error('"' + themePath + '" is not a valid json file! The reason is probably a', r);
                    GUISTATE.server.theme = 'default';
                });
        }
        ready.resolve();
    });
    return ready.promise();
}

export function setInitialState(): void {
    // Toolbox?
    $('.level').removeClass('disabled');
    $('.level.' + GUISTATE.program.toolbox.level).addClass('disabled');
    // View?
    if (GUISTATE.gui.view === 'tabProgram' || GUISTATE.gui.view === 'start') {
        $('#head-navigation-configuration-edit').css('display', 'none');
        GUISTATE.gui.blocklyWorkspace.markFocused();
    } else if (GUISTATE.gui.view === 'tabConfiguration') {
        $('#head-navigation-program-edit').css('display', 'none');
        GUISTATE.gui.bricklyWorkspace.markFocused();
    }

    // Robot?
    $('#menu-' + GUISTATE.gui.robot)
        .parent()
        .addClass('disabled');
    // Tutorials?
    updateTutorialMenu();
}

export function setExtensions(extensions): void {
    GUISTATE.robot.extentions = extensions;
}

export function getExtensions() {
    return GUISTATE.robot.extentions;
}

export function hasExtension(value): boolean {
    let hasExtension: boolean = false;
    for (const extension in GUISTATE.robot.extentions) {
        if (extension === value) {
            hasExtension = true;
        }
    }
    return hasExtension;
}

/**
 * Check if a program is a standard program or not
 *
 */
export function isProgramStandard(): boolean {
    return GUISTATE.program.name == 'NEPOprog';
}

export function isProgramWritable(): boolean {
    if (GUISTATE.program.shared == 'WRITE') {
        return true;
    } else if (GUISTATE.program.shared == 'READ') {
        return false;
    }
    return true;
}

export function isConfigurationStandard(): boolean {
    return GUISTATE.configuration.name == getRobotGroup().toUpperCase() + 'basis';
}

export function getConfigurationStandardName(): string {
    return getRobotGroup().toUpperCase() + 'basis';
}

export function isConfigurationAnonymous(): boolean {
    return GUISTATE.configuration.name == '';
}

export function isKioskMode(): boolean {
    //@ts-ignore
    return GUISTATE.kiosk && GUISTATE.kiosk === true;
}

export function setState(result: any): void {
    if (result['serverVersion']) {
        GUISTATE.server.version = result['serverVersion'];
        $('.labReleaseVersion').text(GUISTATE.server.version);
    }
    if (result['robotVersion']) {
        GUISTATE.robot.version = result['robotVersion'];
    }
    //if (getConnection() !== getConnectionTypeEnum().TDM) {
    if (result['robotFirmwareName'] != undefined) {
        GUISTATE.robot.fWName = result['robotFirmwareName'];
    } else {
        GUISTATE.robot.fWName = '';
    }
    if (result['robotWait'] != undefined) {
        GUISTATE.robot.time = result['robotWait'];
    } else {
        GUISTATE.robot.time = -1;
    }
    if (result['robotBattery'] != undefined) {
        GUISTATE.robot.battery = result['robotBattery'];
    } else {
        GUISTATE.robot.battery = '';
    }
    if (result['robotName'] != undefined) {
        GUISTATE.robot.name = result['robotName'];
    } else {
        GUISTATE.robot.name = '';
    }
    if (result['robotState'] != undefined) {
        GUISTATE.robot.state = result['robotState'];
    } else {
        GUISTATE.robot.state = '';
    }
    //}
    if (result['robotSensorvalues'] != undefined) {
        GUISTATE.robot.sensorValues = result['robotSensorvalues'];
    } else {
        GUISTATE.robot.sensorValues = '';
    }
    if (result['robotNepoexitvalue'] != undefined) {
        //TODO: For different robots we have different error messages
        if (result['robotNepoexitvalue'] !== GUISTATE.robot.nepoExitValue) {
            GUISTATE.robot.nepoExitValue = result['robotNepoexitvalue'];
            if (GUISTATE.robot.nepoExitValue !== 143 && GUISTATE.robot.nepoExitValue !== 0) {
                MSG.displayMessage('POPUP_PROGRAM_TERMINATED_UNEXPECTED', 'POPUP', '');
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
    if (CONNECTION_C.getConnectionInstance() != null && CONNECTION_C.getConnectionInstance() !== undefined) {
        CONNECTION_C.getConnectionInstance().setState();
    }
}

export function getBlocklyWorkspace() {
    return GUISTATE.gui.blocklyWorkspace;
}

export function setBlocklyWorkspace(workspace: any): void {
    GUISTATE.gui.blocklyWorkspace = workspace;
}

function getBricklyWorkspace(): any {
    return GUISTATE.gui.bricklyWorkspace;
}

export function setBricklyWorkspace(workspace: any): void {
    GUISTATE.gui.bricklyWorkspace = workspace;
}

export function setRobot(robot: string, result: SetRobotResponse, opt_init?: boolean): void {
    // make sure we use the group instead of the specific robottype if the robot belongs to a group
    let robotGroup: string = findGroup(robot);
    GUISTATE.gui.program = result.program;
    GUISTATE.gui.configuration = result.configuration;
    GUISTATE.gui.sim = result.sim;
    GUISTATE.gui.multipleSim = result.multipleSim;
    GUISTATE.gui.markerSim = result.markerSim;
    //@ts-ignore PluginSim is not a property of SetRobotResponse
    GUISTATE.gui.pluginSim = result.pluginSim;
    GUISTATE.gui.nnActivations = result.nnActivations;
    GUISTATE.gui.webotsSim = result.webotsSim;
    GUISTATE.gui.webotsUrl = result.webotsUrl;
    GUISTATE.gui.vendor = result.vendor;
    GUISTATE.gui.signature = result.signature;
    GUISTATE.gui.commandLine = result.commandLine;
    GUISTATE.gui.configurationUsed = result.configurationUsed;
    GUISTATE.gui.sourceCodeFileExtension = result.sourceCodeFileExtension;
    GUISTATE.gui.binaryFileExtension = result.binaryFileExtension;
    GUISTATE.gui.firmwareDefault = result.firmwareDefault;

    $('#blocklyDiv, #bricklyDiv').css('background', 'url(../../../../css/img/' + robotGroup + 'Background.jpg) repeat');
    $('#blocklyDiv, #bricklyDiv').css('background-size', '100%');
    $('#blocklyDiv, #bricklyDiv').css('background-position', 'initial');

    if (!isConfigurationUsed()) {
        $('#bricklyDiv').css('background', 'url(../../../../css/img/' + robotGroup + 'BackgroundConf.svg) no-repeat');
        $('#bricklyDiv').css('background-position', 'center');
        $('#bricklyDiv').css('background-size', '75% auto');
    } else if (CV.CircuitVisualization.isRobotVisualized(robotGroup, robot)) {
        $('#bricklyDiv').css('background', '');
        $('#bricklyDiv').css('background-position', '');
        $('#bricklyDiv').css('background-size', '');
    }

    $('.robotType').removeClass('disabled');
    $('.robotType.' + robot).addClass('disabled');
    $('#head-navi-icon-robot').removeClass('typcn-open');
    $('#head-navi-icon-robot').removeClass('typcn-' + GUISTATE.gui.robotGroup);
    $('#head-navi-icon-robot').addClass('typcn-' + robotGroup);
    $('.simWindow').removeClass('simWindow-openedButHidden');

    checkSim();
    setProgramOwnerName(null);
    setProgramAuthorName(null);
    setProgramShareRelation(null);
    if (!opt_init) {
        setProgramSaved(true);
        setConfigurationSaved(true);
        if (findGroup(robot) != getRobotGroup()) {
            //@ts-ignore
            setConfigurationName(robotGroup.toUpperCase() + 'basis');
            setProgramName('NEPOprog');
        }
    } else {
        //@ts-ignore
        setConfigurationName(robotGroup.toUpperCase() + 'basis');
        setProgramName('NEPOprog');
    }

    $('#simRobot').removeClass('typcn-' + GUISTATE.gui.robotGroup);
    $('#simRobot').addClass('typcn-' + robotGroup);

    let groupSwitched: boolean = false;
    if (findGroup(robot) != getRobotGroup()) {
        groupSwitched = true;
    }

    if (GUISTATE.gui.firmwareDefault === undefined) {
        $('#robotDefaultFirmware').addClass('hidden');
    } else {
        $('#robotDefaultFirmware').removeClass('hidden');
    }

    GUISTATE.gui.robot = robot;
    GUISTATE.gui.robotGroup = robotGroup;

    let value = Blockly.Msg.MENU_START_BRICK;
    if (value.indexOf('$') >= 0) {
        value = value.replace('$', getRobotRealName());
    }
    $('#menuRunProg').html(value);
    if (GUISTATE.gui.blocklyWorkspace) {
        GUISTATE.gui.blocklyWorkspace.robControls.refreshTooltips(getRobotRealName());
    }
    if (groupSwitched) {
        HELP_C.initView();
        if (inWebview()) {
            WEBVIEW_C.setRobotBehaviour();
            WEBVIEW_C.jsToAppInterface({
                target: 'internal',
                type: 'setRobot',
                robot: robotGroup
            });
        }
    }

    if (hasExtension('nn')) {
        $('#nn-activations').empty();
        $('.tabLinkNN').show();
        $.each(GUISTATE.gui.nnActivations, function(_, item): void {
            $('#nn-activations').append(
                $('<option>', {
                    value: item,
                    text: UTIL.activationDisplayName[item]
                })
            );
        });
    } else {
        $('.tabLinkNN').hide();
    }

    UTIL.clearTabAlert('tabConfiguration'); // also clear tab alert when switching robots

    ACE_EDITOR.setCodeLanguage(getSourceCodeFileExtension());
}

export function resetRobot() {
    GUISTATE.gui.robot = undefined;
}

export function setKioskMode(kiosk: boolean): void {
    //@ts-ignore
    GUISTATE.kiosk = kiosk;
}

export function findGroup(robot: string): string {
    let robots: RobotBase[] = getRobots();
    for (let propt in robots) {
        //@ts-ignore
        if (robots[propt].name == robot && robots[propt].group !== '') {
            //@ts-ignore
            robot = robots[propt].group;
            return robot;
        }
    }
    return robot;
}

export function findRobot(group) {
    let robots: any[] = getRobots();
    let robot: any;
    for (robot in robots) {
        if (robots[robot].group === group) {
            return robots[robot].name;
            break;
        }
    }
    return null;
}

export function setConnectionState(state: string): void {
    switch (state) {
        case 'busy':
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').addClass('busy');
            setRunEnabled(false);
            break;
        case 'error':
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').addClass('error');
            setRunEnabled(false);
            break;
        case 'wait':
            if (isRobotConnected()) {
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').addClass('wait');
                setRunEnabled(true);
            } else {
                setConnectionState('error');
            }
            break;
        default:
            break;
    }
}

export function isRunEnabled() {
    return GUISTATE.gui.runEnabled;
}

export function setRunEnabled(running: boolean): void {
    running ? true : false;
    GUISTATE.gui.runEnabled = running;
    if (running) {
        GUISTATE.gui.blocklyWorkspace && GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
        GUISTATE.gui.blocklyWorkspace && GUISTATE.gui.blocklyWorkspace.robControls.enable('stopProgram');
        $('.menuRunProg, #runSourceCodeEditor').removeClass('disabled');
    } else {
        GUISTATE.gui.blocklyWorkspace && GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
        GUISTATE.gui.blocklyWorkspace && GUISTATE.gui.blocklyWorkspace.robControls.disable('stopProgram');
        $('.menuRunProg, #runSourceCodeEditor').addClass('disabled');
    }
}

export function getRobot() {
    return GUISTATE.gui.robot;
}

export function getRobotGroup() {
    return GUISTATE.gui.robotGroup;
}

export function setRobotPort(port: string): void {
    GUISTATE.robot.robotPort = port;
}

export function getRobotPort(): string {
    return GUISTATE.robot.robotPort;
}

export function getRobotRealName() {
    for (let robot in getRobots()) {
        if (!getRobots().hasOwnProperty(robot)) {
            continue;
        }
        if (getRobots()[robot].name == getRobot()) {
            return getRobots()[robot].realName;
        }
    }
    return getRobot();
}

export function getMenuRobotRealName(robotName: string): string {
    for (let robot in getRobots()) {
        if (!getRobots().hasOwnProperty(robot)) {
            continue;
        }
        if (getRobots()[robot].name == robotName) {
            return getRobots()[robot].realName;
        }
    }
    return 'Robot not found';
}

export function isRobotBeta(robotName: string): boolean {
    return getRobotsByName()[robotName].announcement == 'beta';
}

export function isRobotDeprecated(robot): boolean {
    return getRobotsByName()[robot].announcement == 'deprecated';
}

export function getRobotDeprecatedData(robot) {
    if (isRobotDeprecated(robot)) {
        return NOTIFICATION_C.getDeprecatedNotifications(robot, getLanguage());
    }
}

export function getRobotInfoDE(robotName: string) {
    for (let robot in getRobots()) {
        if (!getRobots().hasOwnProperty(robot)) {
            continue;
        }
        if (getRobots()[robot].name == robotName) {
            return getRobots()[robot].infoDE;
        }
    }
    return '#';
}

export function getRobotInfoEN(robotName: string) {
    for (let robot in getRobots()) {
        if (!getRobots().hasOwnProperty(robot)) {
            continue;
        }
        if (getRobots()[robot].name == robotName) {
            return getRobots()[robot].infoEN;
        }
    }
    return '#';
}

export function isRobotConnected(): boolean {
    if (!CONNECTION_C.getConnectionInstance()) return false;
    return CONNECTION_C.getConnectionInstance().isRobotConnected();
}

export function isConfigurationUsed(): boolean {
    return GUISTATE.gui.configurationUsed;
}

export function isRobotDisconnected(): number {
    return (GUISTATE.robot.time = -1);
}

export function getRobotTime(): string {
    return GUISTATE.robot.time;
}

export function getRobotName(): string {
    return GUISTATE.robot.name;
}

export function getRobotBattery(): string {
    return GUISTATE.robot.battery;
}

export function getRobotState(): string {
    return GUISTATE.robot.state;
}

export function getRobotVersion(): string {
    return GUISTATE.robot.version;
}

export function hasRobotDefaultFirmware(): string {
    return GUISTATE.gui.firmwareDefault;
}

export function setView(view: string): void {
    if (GUISTATE.gui.view === view) {
        return;
    }
    $('#head-navi-tooltip-program').attr('data-bs-toggle', 'dropdown');
    $('#head-navi-tooltip-configuration').attr('data-bs-toggle', 'dropdown');
    $('#head-navi-tooltip-robot').attr('data-bs-toggle', 'dropdown');
    $('#head-navigation-program-edit').removeClass('disabled');
    $('.robotType').removeClass('disabled');
    $('#head-navigation-configuration-edit').removeClass('disabled');
    GUISTATE.gui.prevView = GUISTATE.gui.view;
    GUISTATE.gui.view = view;
    if (!isRobotConnected()) {
        setRunEnabled(false);
        $('#runSourceCodeEditor').addClass('disabled');
    }
    if ($('.rightMenuButton.rightActive').length > 0) {
        $('.rightMenuButton.rightActive').clickWrap();
    }
    if (view === 'tabConfiguration') {
        $('#head-navigation-program-edit').css('display', 'none');
        $('#head-navigation-configuration-edit').css('display', 'inline');
        UTIL.clearTabAlert(view);
    } else if (view === 'tabProgram') {
        $('#head-navigation-configuration-edit').css('display', 'none');
        $('#head-navigation-program-edit').css('display', 'inline');
    } else if (view === 'tabNN') {
        $('#head-navigation-configuration-edit').css('display', 'none');
        $('#head-navigation-program-edit').css('display', 'inline');
    } else if (view === 'tabNNlearn') {
        $('#head-navigation-configuration-edit').css('display', 'none');
        $('#head-navigation-program-edit').css('display', 'inline');
    } else if (view === 'tabSourceCodeEditor') {
        $('#head-navigation-configuration-edit').css('display', 'none');
        $('#head-navigation-program-edit').css('display', 'inline');
        $('#head-navigation-program-edit').addClass('disabled');
        $('.robotType').addClass('disabled');
        $('#head-navi-tooltip-program').attr('data-bs-toggle', '');
        $('#head-navi-tooltip-configuration').attr('data-bs-toggle', '');
    } else {
        $('#head-navi-tooltip-program').attr('data-bs-toggle', '');
        $('#head-navi-tooltip-configuration').attr('data-bs-toggle', '');
        $('#head-navigation-program-edit').addClass('disabled');
        $('#head-navigation-configuration-edit').addClass('disabled');
    }
}

export function getView(): string {
    return GUISTATE.gui.view;
}

export function getPrevView() {
    return GUISTATE.gui.prevView;
}

export function setLanguage(language: string): void {
    $('#language li a[lang=' + language + ']')
        .parent()
        .addClass('disabled');
    $('#language li a[lang=' + GUISTATE.gui.language + ']')
        .parent()
        .removeClass('disabled');
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
    LEGAL_C.loadLegalTexts();
    USER_C.initValidationMessages();
    NOTIFICATION_C.reloadNotifications();
    if (getView() !== 'tabStart') {
        PROGRAM_C.reloadView();
        CONFIGURATION_C.reloadView();
        PROGLIST_C_switchLanguage();
        CONFLIST_C_switchLanguage();
        GALLERYLIST_C_switchLanguage();
        TUTORIALLIST_C_switchLanguage();
        LOGLIST_C_switchLanguage();
        PROGINFO_C_switchLanguage();
        NN_C.reloadViews();
        let value = Blockly.Msg.MENU_START_BRICK;
        if (value.indexOf('$') >= 0) {
            value = value.replace('$', getRobotRealName());
        }
        $('#menuRunProg').text(value);
        if (getBlocklyWorkspace()) {
            getBlocklyWorkspace().robControls.refreshTooltips(getRobotRealName());
        }
        $('#infoContent').attr('data-placeholder', Blockly.Msg.INFO_DOCUMENTATION_HINT || 'Document your program here ...');
        $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
        updateTutorialMenu();
    }
}

export function getLanguage() {
    return GUISTATE.gui.language;
}

export function isProgramSaved() {
    return GUISTATE.program.saved;
}

export function setProgramSaved(save) {
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

export function isConfigurationSaved() {
    return GUISTATE.configuration.saved;
}

export function setConfigurationSaved(save) {
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

export function getProgramShared() {
    return GUISTATE.program.shared;
}

export function setProgramSource(source) {
    GUISTATE.program.source = source;
}

export function getProgramSource() {
    return GUISTATE.program.source;
}

export function getSourceCodeFileExtension() {
    return GUISTATE.gui.sourceCodeFileExtension;
}

export function getBinaryFileExtension() {
    return GUISTATE.gui.binaryFileExtension;
}

export function isUserLoggedIn() {
    return GUISTATE.user.id >= 0;
}

export function getProgramTimestamp() {
    return GUISTATE.program.timestamp;
}

export function setProgramTimestamp(timestamp) {
    GUISTATE.program.timestamp = timestamp;
}

export function getProgramName() {
    return GUISTATE.program.name;
}

export function setProgramName(name): void {
    let displayName = name;
    if (getProgramShareRelation() && getProgramShareRelation() !== 'NONE' && getProgramOwnerName() !== getUserAccountName()) {
        let owner = getProgramOwnerName(),
            author = getProgramAuthorName(),
            relation = getProgramShareRelation(),
            icon: string = '',
            content: string = '',
            suffix: string = '';

        if (owner === 'Gallery') {
            // user has uploaded this program to the gallery
            icon = 'th-large-outline';
            if (relation === 'READ') {
                content = author;
            }
        } else if (owner === 'Roberta') {
            // user loads a program from the example program list
            icon = 'roberta';
        } else if (relation == 'WRITE') {
            // user loads a program, owned by another user, but with WRITE rights
            icon = 'pencil';
            suffix = '<span style="color:#33B8CA;">' + owner + '</span>';
        } else if (relation == 'READ') {
            // user loads a program, owned by another user, but with READ rights
            icon = 'eye';
            suffix = '<span style="color:#33B8CA;">' + owner + '</span>';
        }

        displayName += ' <b><span style="color:#33B8CA;" class="typcn typcn-' + icon + ' progName">' + content + '</span></b>' + suffix;
    }
    $('#tabProgramName').html(displayName);
    GUISTATE.program.name = name;
}

export function getProgramOwnerName() {
    return GUISTATE.program.owner || getUserAccountName();
}

export function setProgramOwnerName(name): void {
    GUISTATE.program.owner = name;
}

export function getProgramAuthorName() {
    return GUISTATE.program.author || getUserAccountName();
}

export function setProgramAuthorName(name): void {
    GUISTATE.program.author = name;
}

export function getProgramShareRelation() {
    return GUISTATE.program.shared;
}

export function setProgramShareRelation(relation): void {
    GUISTATE.program.shared = relation;
}

export function getConfigurationName() {
    return GUISTATE.configuration.name;
}

export function setConfigurationName(name): void {
    $('#tabConfigurationName').html(name);
    GUISTATE.configuration.name = name;
}

export function setConfigurationNameDefault(): void {
    setConfigurationName(getConfigurationStandardName());
}

export function setProgramToolboxLevel(level): void {
    $('.level').removeClass('disabled');
    $('.level.' + level).addClass('disabled');
    GUISTATE.program.toolbox.level = level;
}

export function getProgramToolboxLevel() {
    return GUISTATE.program.toolbox.level;
}

export function getToolbox(level) {
    return GUISTATE.gui.program.toolbox[level];
}

export function getConfToolbox() {
    //@ts-ignore
    return GUISTATE.conf.toolbox;
}

export function getRobotFWName() {
    return GUISTATE.robot.fWName;
}

export function setRobotToken(token): void {
    GUISTATE.robot.token = token;
}

export function setRobotUrl(url): void {
    GUISTATE.robot.url = url;
}

export function setConfigurationXML(xml): void {
    GUISTATE.configuration.xml = xml;
}

export function getConfigurationXML() {
    return GUISTATE.configuration.xml;
}

export function setProgramXML(xml): void {
    GUISTATE.program.xml = xml;
}

export function getProgramXML() {
    return GUISTATE.program.xml;
}

export function getRobots() {
    return GUISTATE.server.robots;
}

function getRobotsByName() {
    return GUISTATE.server.robotsByName;
}

export function getProgramToolbox() {
    if (GUISTATE.gui.program.dynamicToolbox) {
        return GUISTATE.gui.program.dynamicToolbox;
    } else {
        return GUISTATE.gui.program.toolbox[GUISTATE.program.toolbox.level];
    }
}

export function setDynamicProgramToolbox(toolbox): void {
    GUISTATE.gui.program['dynamicToolbox'] = toolbox;
}

export function resetDynamicProgramToolbox(): void {
    delete GUISTATE.gui.program['dynamicToolbox'];
}

export function getConfigurationToolbox() {
    return GUISTATE.gui.configuration.toolbox;
}

export function getProgramProg() {
    return GUISTATE.gui.program.prog;
}

export function getConfigurationConf() {
    return GUISTATE.gui.configuration.conf;
}

export function getStartWithoutPopup() {
    return GUISTATE.gui.startWithoutPopup;
}

export function setStartWithoutPopup(): boolean {
    return (GUISTATE.gui.startWithoutPopup = true);
}

export function getServerVersion() {
    return GUISTATE.server.version;
}

export function isPublicServerVersion() {
    return GUISTATE.server.isPublic;
}

export function getUserName() {
    return GUISTATE.user.name;
}

export function getUserAccountName() {
    return GUISTATE.user.accountName;
}

export function isUserAccountActivated() {
    return GUISTATE.user.isAccountActivated;
}

export function isUserMemberOfUserGroup(): boolean {
    return GUISTATE.user.userGroup != '';
}

export function getUserUserGroup() {
    return GUISTATE.user.userGroup;
}

export function getUserUserGroupOwner() {
    return GUISTATE.user.userGroupOwner;
}

export function setLogin(result: any): void {
    setState(result);
    GUISTATE.user.accountName = result.userAccountName;
    if (result.userName === undefined || result.userName === '') {
        GUISTATE.user.name = result.userAccountName;
    } else {
        GUISTATE.user.name = result.userName;
    }
    GUISTATE.user.id = result.userId;
    GUISTATE.user.isAccountActivated = result.isAccountActivated;
    GUISTATE.user.userGroup = result.userGroupName;
    GUISTATE.user.userGroupOwner = result.userGroupOwner;

    $('.navbar-nav > li > ul > .login, .logout').removeClass('disabled');
    $('.navbar-nav > li > ul > .login.unavailable').addClass('disabled');
    $('.navbar-nav > li > ul > .logout').addClass('disabled');
    $('#head-navi-icon-user').removeClass('error');
    $('#head-navi-icon-user').addClass('ok');
    $('.menuLogin').addClass('pe-none');
    $('#menuSaveProg').parent().addClass('disabled');
    $('#menuSaveConfig').parent().addClass('disabled');

    if (getRobot()) {
        setProgramSaved(true);
        setConfigurationSaved(true);
        if (GUISTATE.gui.view == 'tabGalleryList') {
            $('#galleryList').find('button[name="refresh"]').clickWrap();
        }
    }
    if (isUserMemberOfUserGroup()) {
        $('#registerUserName, #registerUserEmail').prop('disabled', true);
        $('#userGroupMemberDefaultPasswordHint').removeClass('hidden');
    }
}

export function setLogout(): void {
    if (isUserMemberOfUserGroup()) {
        $('#registerUserName, #registerUserEmail').prop('disabled', false);
        $('#userGroupMemberDefaultPasswordHint').addClass('hidden');
    }

    GUISTATE.user.id = -1;
    GUISTATE.user.accountName = '';
    GUISTATE.user.name = '';
    GUISTATE.user.userGroup = '';
    GUISTATE.user.userGroupOwner = '';
    if (getView() === 'tabUserGroupList') {
        $('#' + getPrevView()).tabWrapShow();
    }
    setProgramName('NEPOprog');
    setProgramOwnerName(null);
    setProgramAuthorName(null);
    setProgramShareRelation(null);
    GUISTATE.program.shared = false;
    $('.navbar-nav > li > ul > .logout, .login').removeClass('disabled');
    $('.navbar-nav > li > ul > .login').addClass('disabled');
    $('#head-navi-icon-user').removeClass('ok');
    $('#head-navi-icon-user').addClass('error');
    if (GUISTATE.gui.view == 'tabProgList') {
        //@ts-ignore
        $('#tabProgram').tabWrapShow();
    } else if (GUISTATE.gui.view == 'tabConfList') {
        $('#tabConfiguration').clickWrap();
    } else if (GUISTATE.gui.view == 'tabGalleryList') {
        $('#galleryList').find('button[name="refresh"]').clickWrap();
    }
    $('.menuLogin').removeClass('pe-none');
}

export function setProgram(result: any, opt_owner?, opt_author?): void {
    if (result) {
        GUISTATE.program.shared = result.programShared;
        GUISTATE.program.timestamp = result.lastChanged;
        setProgramSaved(true);
        setConfigurationSaved(true);
        let name = result.name;

        setProgramShareRelation(result.programShared);
        if (opt_owner) {
            setProgramOwnerName(opt_owner);
        } else if (result.parameters && result.parameters.OWNER_NAME) {
            setProgramOwnerName(result.parameters.OWNER_NAME);
        } else {
            setProgramOwnerName(null);
        }

        if (opt_author) {
            setProgramAuthorName(opt_author);
        } else if (result.parameters && result.parameters.AUTHOR_NAME) {
            setProgramAuthorName(result.parameters.AUTHOR_NAME);
        } else {
            setProgramOwnerName(null);
        }
        setProgramName(result.name);
    }
}

/**
 * Set configuration
 * @param {*} result
 */
export function setConfiguration(result: any): void {
    if (result) {
        setConfigurationName(result.name);
        GUISTATE.configuration.timestamp = result.lastChanged;
        setConfigurationSaved(true);
        setProgramSaved(false);
        $('#tabConfigurationName').html(result.name);
    }
}

export function checkSim(): void {
    if (hasSim()) {
        if (hasMarkerSim()) {
            $('#simMarkerObject').parent().css('display', 'inline-block');
        } else {
            $('#simMarkerObject').parent().css('display', 'none');
        }
        $('#menuRunSim').parent().removeClass('disabled');
        $('#simButton, #simDebugButton').show();
    } else {
        $('#menuRunSim').parent().addClass('disabled');
        $('#simButton, #simDebugButton').hide();
    }
    if (hasWebotsSim()) {
        $('#simDebugButton').hide();
    }
    if (hasMultiSim()) {
        $('#menuRunMulipleSim').parent().removeClass('unavailable');
        $('#menuRunMulipleSim').parent().addClass('available');
        $('#menuRunMulipleSim').parent().removeClass('disabled');
    } else {
        $('#menuRunMulipleSim').parent().addClass('unavailable');
        $('#menuRunMulipleSim').parent().removeClass('available');
        $('#menuRunMulipleSim').parent().addClass('disabled');
    }
}

export function hasSim(): boolean {
    return GUISTATE.gui.sim == true;
}

export function hasMultiSim(): boolean {
    return GUISTATE.gui.multipleSim == true;
}

export function hasMarkerSim(): boolean {
    return GUISTATE.gui.markerSim == true;
}

export function getPluginSim() {
    return GUISTATE.gui.pluginSim || null;
}

export function hasWebotsSim(): boolean {
    return GUISTATE.gui.webotsSim == true;
}

export function getWebotsUrl() {
    return GUISTATE.gui.webotsUrl;
}

export function getListOfTutorials() {
    return GUISTATE.server.tutorial;
}

export function getVendor() {
    return GUISTATE.gui.vendor;
}

export function getSignature() {
    return GUISTATE.gui.signature;
}

export function getCommandLine() {
    return GUISTATE.gui.commandLine;
}

export function setPing(ping): void {
    GUISTATE.server.ping = ping;
}

export function doPing() {
    return GUISTATE.server.ping;
}

export function setPingTime(time): void {
    GUISTATE.server.pingTime = time;
}

export function getPingTime() {
    return GUISTATE.server.pingTime;
}

export function getAvailableHelp() {
    return GUISTATE.server.help;
}

export function getTheme() {
    return GUISTATE.server.theme;
}

export function inWebview() {
    return GUISTATE.gui.webview || false;
}

export function setWebview(webview): void {
    GUISTATE.gui.webview = webview;
}

export function updateMenuStatus(numOfConnections: any): void {
    CONNECTION_C.getConnectionInstance().updateMenuStatus(numOfConnections);
}

export function updateTutorialMenu(): void {
    $('#head-navigation-tutorial').hide();
    let tutorialList = getListOfTutorials();
    for (let tutorial in tutorialList) {
        if (tutorialList.hasOwnProperty(tutorial) && tutorialList[tutorial].language === getLanguage().toUpperCase()) {
            $('#head-navigation-tutorial').show();
            break;
        }
    }
}

export function getLegalTextsMap() {
    return GUISTATE.server.legalTexts;
}
