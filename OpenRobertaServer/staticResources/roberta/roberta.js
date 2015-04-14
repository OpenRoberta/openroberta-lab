var bricklyActive = false;
var userState = {};
var toastMessages = [];

/**
 * Initialize user-state-object
 */
function initUserState() {
    userState.version = 'xx.xx.xx';
    userState.language = 'DE';
    userState.id = -1;
    userState.accountName = '';
    userState.name = '';
    userState.program = 'NEPOprog';
    userState.configuration = 'EV3basis';
    userState.programSaved = false;
    userState.configurationSaved = false;
    userState.programModified = false;
    userState.configurationModified = false;
    userState.toolbox = 'beginner';
    userState.token = '1A2B3C4D';
    userState.doPing = true;
    userState.robotName = '';
    userState.robotState = '';
    userState.robotBattery = '';
    userState.robotWait = '';
    userState.robotFirmware = '';
}

/**
 * Login user
 */
function login() {
    USER.login($("#accountNameS").val(), $('#pass1S').val(), function(result) {
        if (result.rc === "ok") {
            userState.accountName = result.userAccountName;
            if (result.userName === undefined || result.userName === '') {
                userState.name = result.userAccountName;
            } else {
                userState.name = result.userName;
            }
            userState.id = result.userId;
            setHeadNavigationMenuState('login');
            setRobotState(result);
        }
        displayInformation(result, "MESSAGE_USER_LOGIN", result.message, userState.name);
    });
}

/**
 * Logout user
 */
function logout() {
    USER.logout(function(result) {
        if (result.rc === "ok") {
            initUserState();
            $('#programNameSave').val('');
            $('#configurationNameSave').val('');
            setHeadNavigationMenuState('logout');
            Blockly.getMainWorkspace().saveButton.disable();
            setRobotState(result);
        }
        displayInformation(result, "MESSAGE_USER_LOGOUT", result.message);
    });
}

/**
 * Create new user
 */
function saveUserToServer() {
    if ($('#pass1').val() != $('#pass2').val()) {
        displayMessage("MESSAGE_PASSWORD_ERROR", "POPUP", "");
    } else {
        USER.saveUserToServer($("#accountName").val(), $('#userName').val(), $("#userEmail").val(), $('#pass1').val(), function(result) {
            if (result.rc === "ok") {
                setRobotState(result);
                $('#accountNameS').val($("#accountName").val());
                $('#pass1S').val($('#pass1').val());
                login();
            }
            displayInformation(result, "", result.message);
        });
    }
}

/**
 * Delete user on server
 */
function deleteUserOnServer() {
    USER.deleteUserOnServer(userState.accountName, $('#pass1D').val(), function(result) {
        if (result.rc === "ok") {
            logout();
        }
        displayInformation(result, "MESSAGE_USER_DELETED", result.message, userState.name);
    });
}

/**
 * Update Firmware
 */
function updateFirmware() {
    ROBOT.updateFirmware(function(result) {
        if (result.rc === "ok") {
            setRobotState(result);
        }
        displayInformation(result, "MESSAGE_ROBOT_CONNECTED", result.message, userState.robotFirmware);
    });
}

/**
 * Show user info
 */
function showUserInfo() {
    if (userState.id != -1) {
        $("#popup_username").text(Blockly.Msg["POPUP_USERNAME"] + ": ");
        $("#loggedIn").text(userState.name);
    } else {
        $("#popup_username").text(Blockly.Msg["POPUP_USERNAME_LOGOFF"]);
    }
    $("#programName").text(userState.program);
    $("#configurationName").text(userState.configuration);
    if (userState.toolbox === 'beginner') {
        $("#toolbox").text(Blockly.Msg["MENU_BEGINNER"]);
    } else {
        $("#toolbox").text(Blockly.Msg["MENU_EXPERT"]);
    }
    $("#show-state-info").modal("show");
}

/**
 * Show robot info
 */
function showRobotInfo() {
    if (userState.robotName) {
        $("#robotName").text(userState.robotName);
        if (userState.robotState === "wait" ) {
            $("#robotStateWait").css('display','inline');
            $("#robotStateDisconnected").css('display','none');
            $("#robotStateBusy").css('display','none');            
        } else if (userState.robotState === "busy" ) {
            $("#robotStateWait").css('display','none');
            $("#robotStateDisconnected").css('display','none');
            $("#robotStateBusy").css('display','inline');            
        } else {
            $("#robotStateWait").css('display','none');
            $("#robotStateDisconnected").css('display','inline');
            $("#robotStateBusy").css('display','none');            
        }
        if (userState.language == 'EN') {
            $("#robotBattery").text(userState.robotBattery + ' V');
        } else {
            $("#robotBattery").text(userState.robotBattery.toString().replace(".", ",") + ' V');
        }
        var robotWait = parseInt(userState.robotWait);
        if (robotWait < 1000) {
            $("#robotWait").text(robotWait + ' ms');
        } else {
            $("#robotWait").text(Math.round(robotWait / 1000) + ' s');
        }
        $("#show-robot-info").modal("show");
    } else {
        displayMessage("ORA_ROBOT_NOT_CONNECTED", "POPUP", "");
    }
}

/**
 * Inject Blockly with initial toolbox
 */
function injectBlockly(toolbox, opt_programBlocks) {
    response(toolbox);
    if (toolbox.rc === 'ok') {
        $('#blocklyDiv').html('');
        Blockly.inject(document.getElementById('blocklyDiv'), {
            path : '/blockly/',
            toolbox : toolbox.data,
            trashcan : true,
            save : true,
            //check : true,
            start : true
        });
        initProgramEnvironment(opt_programBlocks);
        setRobotState(toolbox);
    }
}

function initProgramEnvironment(opt_programBlocks) {
    Blockly.getMainWorkspace().clear();
    // should this come from the server?
    var text = "<block_set xmlns='http: // www.w3.org/1999/xhtml'>" + "<instance x='100' y='50'>" + "<block type='robControls_start'>" + "</block>"
            + "</instance>" + "</block_set>";
    var program = opt_programBlocks || text;
    var xml = Blockly.Xml.textToDom(program);
    Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
}

/**
 * Set program name
 * 
 * @param {name}
 *            Name to be set
 */
function setProgram(name) {
    if (name) {
        userState.program = name;
        $('#tabProgramName').text(name);
    }
}

/**
 * Set configuration name
 * 
 * @param {name}
 *            Name to be set
 */
function setConfiguration(name) {
    if (name) {
        userState.configuration = name;
        $('#tabConfigurationName').text(name);
    }
}

/**
 * Set token
 * 
 * @param {token}
 *            Token value to be set
 */
function setToken(token) {
    var resToken = token.toUpperCase();
    ROBOT.setToken(resToken, function(result) {
        if (result.rc === "ok") {
            userState.token = resToken;
            setRobotState(result);
        }
        displayInformation(result, "MESSAGE_ROBOT_CONNECTED", result.message, userState.robotName);
    });
}

/**
 * Handle result of server call
 * 
 * @param {result}
 *            Result-object from server call
 */
function response(result) {
    LOG.info('result from server: ' + UTIL.formatResultLog(result));
    if (result.rc != 'ok') {
        displayMessage(result.message, "POPUP", "");
    }
}

/**
 * Save program with new name to server
 */
function saveAsProgramToServer() {
    var progName = $('#programNameSave').val().trim();
    if (!progName.match(/^[a-zA-Z][a-zA-Z0-9]*$/)) {
        displayMessage("MESSAGE_INVALID_NAME", "POPUP", "");
        return;
    }
    setProgram(progName);
    $('#menuSaveProg').parent().removeClass('login');
    $('#menuSaveProg').parent().removeClass('disabled');
    Blockly.getMainWorkspace().saveButton.enable();
    var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
    var xmlText = Blockly.Xml.domToText(xml);
    userState.programSaved = true;
    LOG.info('saveAs program ' + userState.program + ' login: ' + userState.id);
    $('.modal').modal('hide'); // close all opened popups
    PROGRAM.saveAsProgramToServer( userState.program, xmlText, function(result) {
        if (result.rc === 'ok') {
            userState.programModified = false;
        }
        displayInformation(result, "MESSAGE_EDIT_SAVE_PROGRAM_AS", result.message, userState.program);
    });
}

/**
 * Save program to server
 */
function saveToServer() {
    if (userState.program) {
        var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
        var xmlText = Blockly.Xml.domToText(xml);
        userState.programSaved = true;
        LOG.info('save program ' + userState.program + ' login: ' + userState.id);
        $('.modal').modal('hide'); // close all opened popups
        PROGRAM.saveProgramToServer( userState.program, xmlText, function(result) {
            if (result.rc === 'ok') {
                userState.programModified = false;
            }
            displayInformation(result, "MESSAGE_EDIT_SAVE_PROGRAM", result.message, userState.program);
        });
    }
}

/**
 * Save configuration with new name to server
 */
function saveAsConfigurationToServer() {
    var confName = $('#configurationNameSave').val();
    if (!confName.match(/^[a-zA-Z][a-zA-Z0-9]*$/)) {
        displayMessage("MESSAGE_INVALID_NAME", "POPUP", "");
        return;
    }
    setConfiguration(confName);
    $('#menuSaveConfig').parent().removeClass('login');
    $('#menuSaveConfig').parent().removeClass('disabled');
    document.getElementById('bricklyFrame').contentWindow.Blockly.getMainWorkspace().saveButton.enable();
    userState.configurationSaved = true;
    $('.modal').modal('hide'); // close all opened popups
    var xmlText = document.getElementById('bricklyFrame').contentWindow.getXmlOfConfiguration(userState.configuration);
    LOG.info('save brick configuration ' + userState.configuration);
    CONFIGURATION.saveAsConfigurationToServer( userState.configuration, xmlText, function(result) {
        if (result.rc === 'ok') {
            userState.configurationModified = false;
        }
        displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION_AS", result.message, userState.configuration);
    });
}

/**
 * Save configuration to server
 */
function saveConfigurationToServer() {
    if (userState.configuration) {
        userState.configurationSaved = true;
        $('.modal').modal('hide'); // close all opened popups
        var xmlText = document.getElementById('bricklyFrame').contentWindow.getXmlOfConfiguration(userState.configuration);
        LOG.info('save brick configuration ' + userState.configuration);
        CONFIGURATION.saveConfigurationToServer( userState.configuration, xmlText, function(result) {
            if (result.rc === 'ok') {
                userState.configurationModified = false;
            }
            displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION", result.message, userState.configuration);
        });
    }
}

/**
 * Run program
 */
function runOnBrick() {
    if (userState.robotState === '' || userState.robotState === 'disconnected') {
        displayMessage("POPUP_ROBOT_NOT_CONNECTED", "POPUP", "");
    } else if (userState.robotState === 'busy') {
        displayMessage("POPUP_ROBOT_BUSY", "POPUP", "");
    } else {
        LOG.info('run ' + userState.program + ' signed in: ' + userState.id);
        var xmlProgram = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var xmlTextConfiguration = document.getElementById('bricklyFrame').contentWindow.getXmlOfConfiguration(userState.configuration);
        displayMessage("MESSAGE_EDIT_START", "TOAST", userState.program);
        PROGRAM.runOnBrick( userState.program, userState.configuration, xmlTextProgram, xmlTextConfiguration, response);
    }
}

function showProgram(result, load, name) {
    response(result);
    if (result.rc === 'ok') {
        setProgram(name);
        var xml = Blockly.Xml.textToDom(result.data);
        if (load) {
            Blockly.mainWorkspace.clear();
        }
        Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
        LOG.info('show program ' + userState.program + ' signed in: ' + userState.id);
    }
};

/**
 * Show configuration in brickly iframe
 * 
 * @param {result}
 *            Result-object of server call
 * @param {load}
 *            load configuration into workspace
 * @param {name}
 *            name of configuration
 */
function showConfiguration(result, load, name) {
    response(result);
    if (result.rc === 'ok') {
        switchToBrickly();
        setConfiguration(name);
        document.getElementById('bricklyFrame').contentWindow.showConfiguration(result.data, load);
        LOG.info('show configuration ' + userState.configuration + ' signed in: ' + userState.id);
    }
};

/**
 * Load the program that was selected in program list
 */
function loadFromListing() {
    var $programRow = $('#programNameTable .selected');
    if ($programRow.length > 0) {
        var programName = $programRow[0].children[0].textContent;
        LOG.info('loadFromList ' + programName + ' signed in: ' + userState.id);
        PROGRAM.loadProgramFromListing( programName, function(result) {
            if (result.rc === 'ok') {
                $("#tabs").tabs("option", "active", 0);
                userState.programSaved = true;
                showProgram(result, true, programName);
                $('#menuSaveProg').parent().removeClass('login');
                $('#menuSaveProg').parent().removeClass('disabled');
                Blockly.getMainWorkspace().saveButton.enable();
            }
            displayInformation(result, "", result.message);
        });
    }
}

/**
 * Load the configuration that was selected in configurations list
 */
function loadConfigurationFromListing() {
    var $configurationRow = $('#configurationNameTable .selected');
    if ($configurationRow.length > 0) {
        var configurationName = $configurationRow[0].children[0].textContent;
        LOG.info('loadFromConfigurationList ' + configurationName + ' signed in: ' + userState.id);
        CONFIGURATION.loadConfigurationFromListing( configurationName, function(result) {
            if (result.rc === 'ok') {
                $("#tabs").tabs("option", "active", 0);
                userState.configurationSaved = true;
                showConfiguration(result, true, configurationName);
                $('#menuSaveConfig').parent().removeClass('login');
                $('#menuSaveConfig').parent().removeClass('disabled');
                setRobotState(result);
            }
            displayInformation(result, "", result.message);
        });
    }
}

/**
 * Delete the programs that were selected in program list
 */
function deleteFromListing() {
    var $programRow = $('#programNameTable .selected');
    for (var i=0; i < $programRow.length; i++) {
        var programName = $programRow[i].children[0].textContent;
        LOG.info('deleteFromList ' + programName + ' signed in: ' + userState.id);
        PROGRAM.deleteProgramFromListing( programName, function(result) {
            if (result.rc === 'ok') {
                response(result);
                PROGRAM.refreshList(showPrograms);
                setRobotState(result);
            }
            displayInformation(result, "MESSAGE_PROGRAM_DELETED", result.message, programName);
        });
    }
}

/**
 * Delete the configurations that waere selected in configurations list
 */
function deleteConfigurationFromListing() {
    var $configurationRow = $('#configurationNameTable .selected');
    for (var i=0; i < $configurationRow.length; i++) {
        var configurationName = $configurationRow[i].children[0].textContent;
        LOG.info('deleteFromConfigurationList ' + configurationName + ' signed in: ' + userState.id);
        CONFIGURATION.deleteConfigurationFromListing( configurationName, function(result) {
            if (result.rc === 'ok') {
                response(result);
                CONFIGURATION.refreshList(showConfigurations);
                setRobotState(result);
            }
            displayInformation(result, "MESSAGE_CONFIGURATION_DELETED", result.message, configurationName);
        });
    }
}

/**
 * Show toolbox
 * 
 * @param {result}
 *            result of server call
 */
function showToolbox(result) {
    response(result);
    if (result.rc === 'ok') {
        Blockly.updateToolbox(result.data);
        setRobotState(result);
    }
}

/**
 * Load toolbox from server
 * 
 * @param {toolbox}
 *            toolbox to be loaded
 */
function loadToolbox(toolbox) {
    userState.toolbox = toolbox;
    COMM.json("/admin", {
        "cmd" : "loadT",
        "name" : toolbox
    }, showToolbox);
}

/**
 * Display programs in a table
 * 
 * @param {result}
 *            result object of server call
 */
function showPrograms(result) {
    response(result);
    if (result.rc === 'ok') {
        var $table = $('#programNameTable').dataTable();
        $table.fnClearTable();
        if (result.programNames.length > 0) {
            $table.fnAddData(result.programNames);
        }
        setRobotState(result);
    }
}

/**
 * Display configurations in a table
 * 
 * @param {result}
 *            result object of server call
 */
function showConfigurations(result) {
    response(result);
    if (result.rc === 'ok') {
        var $table = $('#configurationNameTable').dataTable();
        $table.fnClearTable();
        if (result.configurationNames.length > 0) {
            $table.fnAddData(result.configurationNames);
        }
        setRobotState(result);
    }
}

/**
 * Select row in programs-/configurations-datatable
 */
var start;
function selectionFn(event) {
    $(event.target.parentNode).toggleClass('selected');
    var rowIndex = event.currentTarget.rowIndex;

    if (!start) {
        start = rowIndex;
    }

    // Shift-Click ?
    if(event.shiftKey) {
        var end = rowIndex;
        $('#programNameTable tbody tr').removeClass("selected");
        for (i = Math.min(start, end); i <= Math.max(start, end); i++) {
            if (! $(event.target.parentNode.parentNode.childNodes).eq(i).hasClass("selected")) {
                $(event.target.parentNode.parentNode.childNodes).eq(i).addClass("selected");
            }
        }
         
        // Clear browser text selection mask
        if (window.getSelection) {
            if (window.getSelection().empty) {  // Chrome
                window.getSelection().empty();
            } else if (window.getSelection().removeAllRanges) {  // Firefox
                window.getSelection().removeAllRanges();
            }
        } else if (document.selection) {  // IE?
            document.selection.empty();
        }
    }
    
    start = rowIndex;
}

function beforeActivateTab(event, ui) {
    $('#tabs').tabs("refresh");
    if (ui.newPanel.selector === '#progListing') {
        PROGRAM.refreshList(showPrograms);
    } else if (ui.newPanel.selector === '#confListing') {
        CONFIGURATION.refreshList(showConfigurations);
    }
}

/**
 * Initialize table of programs
 */
function initProgramNameTable() {

    var columns = [ {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>Name des Programms</span>",
        "sClass" : "programs"
    }, {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>Erzeugt von</span>",
        "sClass" : "programs"
    }, {
//        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_SHARED_WITH'>Geteilt mit</span>",
//        "sClass" : "programs"
//    }, {
//        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_SHARED_FOR'>Geteilt für</span>",
//        "sClass" : "programs"
//    }, {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>Erzeugt am</span>",
        "sClass" : "programs"
    }, {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>Letzte Aktualisierung</span>",
        "sClass" : "programs"
    }, ];
    var $programs = $('#programNameTable');

    var oTable = $programs.dataTable({
        "sDom" : '<lip>t<r>',
        "aaData" : [],
        "aoColumns" : columns,
        "aoColumnDefs" : [ { // format date fields
            "aTargets" : [ 2, 3 ], // indexes of columns to be formatted
            "sType" : "date-de",
            "mRender" : function(data) {
                return UTIL.formatDate(data);
            }
        } ],
        "bJQueryUI" : true,
        "oLanguage" : {
            "sEmptyTable" : "<span lkey='Blockly.Msg.DATATABLE_EMPTY_TABLE'>Die Tabelle ist leer</span>"
        },
        "fnDrawCallback" : function() {
        },
        "scrollY" : UTIL.calcDataTableHeight(),
        "scrollCollapse" : true,
        "paging" : false,
        "bInfo" : false
    });

    $(window).resize(function() {
        var oSettings = oTable.fnSettings();
        oSettings.oScroll.sY = UTIL.calcDataTableHeight();
        oTable.fnDraw(false); // redraw the table
    });

    $('#programNameTable tbody').onWrap('click', 'tr', selectionFn);
    $('#programNameTable tbody').onWrap('dblclick', 'tr', function(event) {
        selectionFn(event);
        $('#loadFromListing').click();
    });
}

/**
 * Initialize configurations table
 */
function initConfigurationNameTable() {
    var columns = [ {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CONFIGURATION_NAME'>Name der Konfiguration</span>",
        "sClass" : "configurations"
    }, {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>Erzeugt von</span>",
        "sClass" : "configurations"
    }, {
//        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_SHARED_WITH'>Geteilt mit</span>",
//        "sClass" : "programs"
//    }, {
//        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_SHARED_FOR'>Geteilt für</span>",
//        "sClass" : "programs"
//    }, {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>Erzeugt am</span>",
        "sClass" : "configurations"
    }, {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>Letzte Aktualisierung</span>",
        "sClass" : "configurations"
    }, ];
    var $configurations = $('#configurationNameTable');

    var oTable = $configurations.dataTable({
        "sDom" : '<lip>t<r>',
        "aaData" : [],
        "aoColumns" : columns,
        "aoColumnDefs" : [ { // format date fields
            "aTargets" : [ 2, 3 ], // indexes of columns to be formatted
            "sType" : "date-de",
            "mRender" : function(data) {
                return UTIL.formatDate(data);
            }
        } ],
        "bJQueryUI" : true,
        "oLanguage" : {
            "sEmptyTable" : "<span lkey='Blockly.Msg.DATATABLE_EMPTY_TABLE'>Die Tabelle ist leer</span>"
        },
        "fnDrawCallback" : function() {
        },
        "scrollY" : UTIL.calcDataTableHeight(),
        "scrollCollapse" : true,
        "paging" : false,
        "bInfo" : false
    });

    $(window).resize(function() {
        var oSettings = oTable.fnSettings();
        oSettings.oScroll.sY = UTIL.calcDataTableHeight();
        oTable.fnDraw(false); // redraw the table
    });

    $('#configurationNameTable tbody').onWrap('click', 'tr', selectionFn);
    $('#configurationNameTable tbody').onWrap('dblclick', 'tr', function(event) {
        selectionFn(event);
        $('#loadConfigurationFromListing').click();
    });
}

/**
 * Switch to Blockly tab
 */
function switchToBlockly() {
    $('#tabs').css('display', 'inline');
    $('#bricklyFrame').css('display', 'none');
    $('#tabBlockly').click();
    // do this twice :-( to make sure all metrics are calculated correctly.
    Blockly.getMainWorkspace().render();
    Blockly.getMainWorkspace().render();
    bricklyActive = false;
}

/**
 * Switch to Brickly tab
 */
function switchToBrickly() {
    $('#tabs').css('display', 'none');
    $('#bricklyFrame').css('display', 'inline');
    $('#tabBrickly').click();
    bricklyActive = true;
}

/**
 * Switch to Simulation tab
 */
function switchToSimulation() {
    $('#tabs').css('display', 'inline');
    $('#bricklyFrame').css('display', 'none');
    $('#tabSim').click();
    bricklyActive = false;
}

function setHeadNavigationMenuState(state) {
    $('.nav > li > ul > li').removeClass('disabled');
    if (state === 'login') {
        $('.nav > li > ul > .login').addClass('disabled');
    } else if (state === 'logout') {
        $('.nav > li > ul > .logout').addClass('disabled');
    }
}

/**
 * Initialize the navigation bar in the head of the page
 */
function initHeadNavigation() {
    $('.navbar-fixed-top').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
        $('.modal').modal('hide'); // close all opened popups
        var domId = event.target.id;
        if (domId === 'menuRunProg') { //  Submenu 'Program'
            runOnBrick();
        } else if (domId === 'menuCheckProg') { //  Submenu 'Program'
            displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP", "");
        } else if (domId === 'menuNewProg') { //  Submenu 'Program'
            setProgram("NEPOprog");
            initProgramEnvironment();
            $('#menuSaveProg').parent().addClass('disabled');
            Blockly.getMainWorkspace().saveButton.disable();
            $('#tabProgram').click();
        } else if (domId === 'menuListProg') { //  Submenu 'Program'
            deactivateProgConfigMenu();
            $('#tabListing').click();
        } else if (domId === 'menuSaveProg') { //  Submenu 'Program'
            saveToServer();
        } else if (domId === 'menuSaveAsProg') { //  Submenu 'Program'
            $("#save-program").modal('show');
        } else if (domId === 'menuPropertiesProg') { //  Submenu 'Program'
        } else if (domId === 'menuToolboxBeginner') { // Submenu 'Program'
            loadToolbox('beginner');
            $('#menuToolboxBeginner').parent().addClass('disabled');
            $('#menuToolboxExpert').parent().removeClass('disabled');
        } else if (domId === 'menuToolboxExpert') { // Submenu 'Program'
            loadToolbox('expert');
            $('#menuToolboxExpert').parent().addClass('disabled');
            $('#menuToolboxBeginner').parent().removeClass('disabled');
        } else if (domId === 'menuCheckConfig') { //  Submenu 'Configuration'
            displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP", "");
        } else if (domId === 'menuNewConfig') { //  Submenu 'Configuration'
            setConfiguration("EV3basis");
            document.getElementById('bricklyFrame').contentWindow.initConfigurationEnvironment();
            $('#menuSaveConfig').parent().addClass('disabled');
            document.getElementById('bricklyFrame').contentWindow.Blockly.getMainWorkspace().saveButton.disable();
        } else if (domId === 'menuListConfig') { //  Submenu 'Configuration'
            deactivateProgConfigMenu();
            $('#tabs').css('display', 'inline');
            $('#bricklyFrame').css('display', 'none');
            $('#tabConfigurationListing').click();
        } else if (domId === 'menuSaveConfig') { //  Submenu 'Configuration'
            saveConfigurationToServer();
        } else if (domId === 'menuSaveAsConfig') { //  Submenu 'Configuration'
            $("#save-configuration").modal("show");
        } else if (domId === 'menuPropertiesConfig') { //  Submenu 'Configuration'
        } else if (domId === 'menuConnect') { // Submenu 'Robot'
            $("#set-token").modal("show");
        } else if (domId === 'menuFirmware') { // Submenu 'Robot'
            updateFirmware();
        } else if (domId === 'menuRobotInfo') { // Submenu 'Robot'
            showRobotInfo();
        } else if (domId === 'menuGeneral') { // Submenu 'Help'
            window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/BIAM");
        } else if (domId === 'menuEV3conf') { // Submenu 'Help'
            window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/BIAM");
        } else if (domId === 'menuProgramming') { // Submenu 'Help'
            window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/CwA-/");
        } else if (domId === 'menuFaq') { // Submenu 'Help'
            window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/BoAd");
        } else if (domId === 'menuShowAgain') { // Submenu 'Help'
            $("#show-startup-message").modal("show");
        } else if (domId === 'menuStateInfo') { // Submenu 'Help'
            showUserInfo();
        } else if (domId === 'menuAbout') { // Submenu 'Help'
            $("#version").text(userState.version);
            $("#show-about").modal("show");
        } else if (domId === 'menuLogging') { // Submenu 'Help'
            deactivateProgConfigMenu();
            $('#tabs').css('display', 'inline');
            $('#bricklyFrame').css('display', 'none');
            $('#tabLogging').click();
        } else if (domId === 'menuLogin') { // Submenu 'Login'
            $("#login-user").modal('show');
        } else if (domId === 'menuLogout') { // Submenu 'Login'
            logout();
        } else if (domId === 'menuNewUser') { // Submenu 'Login'
            $("#register-user").modal('show');
        } else if (domId === 'menuChangeUser') { // Submenu 'Login'
            // open the same popup as in case 'new', but with fields prefilled, REST-Call is missing
        } else if (domId === 'menuDeleteUser') { // Submenu 'Login'
            $("#delete-user").modal('show');
        }
        return false;
    }, 'head navigation menu item clicked');

    $('.navbar-fixed-top .navbar-nav').onWrap('click', 'li:not(.disabled) a', function(event) {
        var domId = event.target.id;
        if (domId === 'menuTabProgram') {
            if ($('#tabSimulation').hasClass('tabClicked')) {
                $('.scroller-left').click();
            }
            $('.scroller-left').click();
            $('#tabProgram').click();
        } else if (domId === 'menuTabConfiguration') {
            if ($('#tabProgram').hasClass('tabClicked')) {
                $('.scroller-right').click();
            } else if ($('#tabConfiguration').hasClass('tabClicked')) {
                $('.scroller-right').click();
            }
            $('#tabConfiguration').click();
        } else if (domId === 'menuTabSimulation') {
            if ($('#tabProgram').hasClass('tabClicked')) {
                $('.scroller-right').click();
            }
            $('.scroller-right').click();
            $('#tabSimulation').click();
        }
        return false;
    });

    // Close submenu on mouseleave
    $('.navbar-fixed-top').onWrap('mouseleave', function(event) {
        $('.navbar-fixed-top .dropdown').removeClass('open');
    });

    $('#imgLogo, #imgBeta').onWrap('click', function() {
        window.open('http://open-roberta.org');
    }, 'logo was clicked');

    $('#beta').onWrap('click', function() {
        window.open('http://open-roberta.org');
    }, 'beta logo was clicked');

    $('#iconDisplayLogin').onWrap('click', function() {
        showUserInfo();
    }, 'icon user click');

    $('#iconDisplayRobotState').onWrap('click', function() {
        showRobotInfo();
    }, 'icon robot click');

    $('#tabProgram').onWrap('click', function() {
        activateProgConfigMenu();
        $('#tabProgram').addClass('tabClicked');
        $('#tabConfiguration').removeClass('tabClicked');
        $('#tabSimulation').removeClass('tabClicked');
        $('#head-navigation-program-edit').css('display', 'inline');
        $('#head-navigation-configuration-edit').css('display', 'none');
        $('#menuTabProgram').parent().addClass('disabled');
        $('#menuTabConfiguration').parent().removeClass('disabled');
        $('#menuTabSimulation').parent().removeClass('disabled');
        switchToBlockly();
    });

    $('#tabConfiguration').onWrap('click', function() {
        activateProgConfigMenu();
        $('#tabProgram').removeClass('tabClicked');
        $('#tabConfiguration').addClass('tabClicked');
        $('#tabSimulation').removeClass('tabClicked');
        $('#head-navigation-program-edit').css('display', 'none');
        $('#head-navigation-configuration-edit').css('display', 'inline');
        $('#menuTabProgram').parent().removeClass('disabled');
        $('#menuTabConfiguration').parent().addClass('disabled');
        $('#menuTabSimulation').parent().removeClass('disabled');
        switchToBrickly();
    });

    $('#tabSimulation').onWrap('click', function() {
        $('#tabProgram').removeClass('tabClicked');
        $('#tabConfiguration').removeClass('tabClicked');
        $('#tabSimulation').addClass('tabClicked');
        $('#head-navigation-program-edit').css('display', 'none');
        $('#head-navigation-configuration-edit').css('display', 'none');
        $('#menuTabProgram').parent().removeClass('disabled');
        $('#menuTabConfiguration').parent().removeClass('disabled');
        $('#menuTabSimulation').parent().addClass('disabled');
        switchToSimulation();
    });

    $('.scroller-right').onWrap('click', function() {
        if ($('#tabProgram').hasClass('tabClicked')) {
            $('.scroller-left').removeClass('hidden-xs');
            $('.scroller-right').removeClass('hidden-xs');
            $('#tabProgram').addClass('hidden-xs');
            $('#tabSimulation').addClass('hidden-xs');
            $('#tabConfiguration').removeClass('hidden-xs');
            $('#tabConfiguration').click();
        } else if ($('#tabConfiguration').hasClass('tabClicked')) {
            $('.scroller-left').removeClass('hidden-xs');
            $('.scroller-right').addClass('hidden-xs');
            $('#tabProgram').addClass('hidden-xs');
            $('#tabConfiguration').addClass('hidden-xs');
            $('#tabSimulation').removeClass('hidden-xs');
            $('#tabSimulation').click();
        }
    });

    $('.scroller-left').onWrap('click', function() {
        if ($('#tabConfiguration').hasClass('tabClicked')) {
            $('.scroller-right').removeClass('hidden-xs');
            $('.scroller-left').addClass('hidden-xs');
            $('#tabConfiguration').addClass('hidden-xs');
            $('#tabSimulation').addClass('hidden-xs');
            $('#tabProgram').removeClass('hidden-xs');
            $('#tabProgram').click();
        } else if ($('#tabSimulation').hasClass('tabClicked')) {
            $('.scroller-right').removeClass('hidden-xs');
            $('.scroller-left').removeClass('hidden-xs');
            $('#tabProgram').addClass('hidden-xs');
            $('#tabSimulation').addClass('hidden-xs');
            $('#tabConfiguration').removeClass('hidden-xs');
            $('#tabConfiguration').click();
        }
    });

    setHeadNavigationMenuState('logout');
}

/**
 * Initialize popups
 */
function initPopups() {
    $('#saveUser').onWrap('click', saveUserToServer);
    $('#deleteUser').onWrap('click', deleteUserOnServer);
    $('#doLogin').onWrap('click', login);
    $('#saveProgram').onWrap('click', saveAsProgramToServer);
    $('#saveConfiguration').onWrap('click', saveAsConfigurationToServer);

    $('#shareProgram').onWrap('click', function() {
        displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP", "");
    }, 'share program');

    $('#shareConfiguration').onWrap('click', function() {
        displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP", "");
    }, 'share configuration');

    $('#setToken').onWrap('click', function() {
        setToken($('#tokenValue').val());
    }, 'set token');

    $('#hideStartupMessage').onWrap('click', function() {
        $("#show-startup-message").modal("hide");
        UTIL.setCookie("hideStartupMessage", true);
    }, 'hide startup-message');

    $('.cancelPopup').onWrap('click', function() {
        $('.ui-dialog-titlebar-close').click();
    });
    
    $('#about-join').onWrap('click', function() {
        $('#show-about').modal('hide');
    });
    
    $('#login-user').onWrap('hidden.bs.modal', function () {
        $('#login-user input').val('');
    });
    
    $('#delete-user').onWrap('hidden.bs.modal', function () {
        $('#delete-user input').val('');
    });
    
    $('#register-user').onWrap('hidden.bs.modal', function () {
        $('#register-user input').val('');
        $('#login-user input').val('');
    });

    // Handle key events in popups
    $(".modal").keyup(function(event) {
        // fix for not working backspace button in password fields
        if (event.keyCode === $.ui.keyCode.BACKSPACE) {
            if (event.target.type === "password" && $("#" + event.target.id).val().length > 0) {
                var length = $("#" + event.target.id).val().length - 1;
                var res = $("#" + event.target.id).val().substring(0, length);
                $("#" + event.target.id).val(res);
            }
        }
        if (event.keyCode == 27) { // escape
            window.close();
        }
        if (event.keyCode == 13) { // enter
            $(this).find(".modal-footer button:first").click();
        }
        event.stopPropagation();
    });
}

/**
 * Initialize tabs
 */
function initTabs() {
    $('#tabs').tabs({
        heightStyle : 'content',
        active : 0,
        beforeActivate : beforeActivateTab
    });

    // load program
    $('#loadFromListing').onWrap('click', function() {
        activateProgConfigMenu();
        loadFromListing();
    }, 'load blocks from program list');

    // load configuration
    $('#loadConfigurationFromListing').onWrap('click', function() {
        activateProgConfigMenu();
        loadConfigurationFromListing();
    }, 'load configuration from configuration list');

    // confirm program deletion
    $('#deleteFromListing').onWrap('click', function() {
        if ($('#programNameTable .selected').length > 0) {
            $("#confirmDeleteProgram").modal("show");
        }
    }, 'Ask for confirmation to delete a program');

    // delete program
    $('#doDeleteProgram').onWrap('click', function() {
        deleteFromListing();
        $('.modal').modal('hide'); // close all opened popups
    }, 'delete program');

    // confirm configuration deletion
    $('#deleteConfigurationFromListing').onWrap('click', function() {
        if ($('#configurationNameTable .selected').length > 0) {
            $("#confirmDeleteConfiguration").modal("show");
        }
    }, 'Ask for confirmation to delete a configuration');

    // delete configuration
    $('#doDeleteConfiguration').onWrap('click', function() {
        deleteConfigurationFromListing();
        $('.modal').modal('hide'); // close all opened popups
    }, 'delete configuration from configurations list');

    // share program
    $('#shareFromListing').onWrap('click', function() {
        $("#share-program").modal("show");
    }, 'share program');

    // share configuration
    $('#shareConfigurationFromListing').onWrap('click', function() {
        $("#share-configuration").modal("show");
    }, 'share configuration');

    $('#backConfiguration').onWrap('click', function() {
        activateProgConfigMenu();
        switchToBrickly();
    });
    $('#backProgram').onWrap('click', function() {
        activateProgConfigMenu();
        switchToBlockly();
    });
    $('#backLogging').onWrap('click', function() {
        activateProgConfigMenu();
        if (bricklyActive) {
            switchToBrickly();
        } else {
            switchToBlockly();
        }
    });
}

/**
 * Initialize logging
 */
function initLogging() {
    $('#clearLog').onWrap('click', function() {
        $('#log li').remove();
    }, 'clear LOG list');
}

/**
 * Set robot state
 * 
 * @param {result}
 *            result of server call
 */
function setRobotState(result) {
    if (result.version) {
        userState.version = result.version;
    }

    if (result['robot.wait'] != undefined) {
        userState.robotWait = result['robot.wait'];
    } else {
        userState.robotWait = '';
    }

    if (userState.robotState === 'wait') {
        $('#iconDisplayRobotState').removeClass('error');
        $('#iconDisplayRobotState').removeClass('busy');
        $('#iconDisplayRobotState').removeClass('wait');
        $('#iconDisplayRobotState').addClass('ok');
    } else if (userState.robotState === 'busy') {
        $('#iconDisplayRobotState').removeClass('ok');
        $('#iconDisplayRobotState').removeClass('wait');
        $('#iconDisplayRobotState').removeClass('error');
        $('#iconDisplayRobotState').addClass('busy');
    } else {
        $('#iconDisplayRobotState').removeClass('busy');
        $('#iconDisplayRobotState').removeClass('wait');
        $('#iconDisplayRobotState').removeClass('ok');
        $('#iconDisplayRobotState').addClass('error');
    }

    if (result['robot.battery'] != undefined) {
        userState.robotBattery = result['robot.battery'];
    } else {
        userState.robotBattery = '';
    }

    if (result['robot.name'] != undefined) {
        userState.robotName = result['robot.name'];
    } else {
        userState.robotName = '';
    }

    if (result['robot.state'] != undefined) {
        userState.robotState = result['robot.state'];
    } else {
        userState.robotState = '';
    }

    if (userState.accountName) {
        $('#iconDisplayLogin').removeClass('error');
        $('#iconDisplayLogin').addClass('ok');
    } else {
        $('#iconDisplayLogin').removeClass('ok');
        $('#iconDisplayLogin').addClass('error');
    }
}

/**
 * Translate the web page
 */
function translate(jsdata) {
    $("[lkey]").each(function(index) {
        var lkey = $(this).attr('lkey');
        var key = lkey.replace("Blockly.Msg.", "");
        var value = Blockly.Msg[key];
        if (value == undefined) {
            console.log('UNDEFINED    key : value = ' + key + ' : ' + value);
        }
        if (lkey === 'Blockly.Msg.MENU_LOG_IN') {
            $('#login-user h3').text(value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.MENU_NEW') {
            $('#register-user h3').text(value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.MENU_DELETE_USER') {
            $('#delete-user h3').text(value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.MENU_SAVE_AS') {
            $('#save-program h3').text(value);
            $('#save-configuration h3').text(value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.BUTTON_DO_SHARE') {
            $('#share-program h3').text(value);
            $('#share-configuration h3').text(value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.MENU_CONNECT') {
            $('#set-token h3').text(value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.POPUP_HIDE_STARTUP_MESSAGE') {
            $('#hideStartupMessage').text(value);
        } else if (lkey === 'Blockly.Msg.POPUP_TEXT_STARTUP_MESSAGE') {
            $('#popupTextStartupMessage').html(value);
        } else if (lkey === 'Blockly.Msg.POPUP_ATTENTION') {
            $('#show-message h3').text(value);
            $('#show-startup-message h3').text(value);
        } else if (lkey === 'Blockly.Msg.POPUP_CANCEL') {
            $('.cancelPopup').attr('value', value);
            $('.backButton').attr('value', value);
        } else if (lkey === 'Blockly.Msg.POPUP_ABOUT_JOIN') {
            $('#about-join').html(value);
        } else if (lkey === 'Blockly.Msg.BUTTON_LOAD') {
            $('.buttonLoad').attr('value', value);
        } else if (lkey === 'Blockly.Msg.BUTTON_DO_DELETE') {
            $('.buttonDelete').attr('value', value);
        } else if (lkey === 'Blockly.Msg.BUTTON_DO_SHARE') {
            $('.buttonShare').attr('value', value);
        } else if (lkey === 'Blockly.Msg.BUTTON_EMPTY_LIST') {
            $('#clearLog').attr('value', value);
        } else if (lkey === 'Blockly.Msg.MENU_ROBOT_STATE_INFO') {
            $('#show-robot-info h3').text(value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.MENU_STATE_INFO') {
            $('#show-state-info h3').text(value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.MENU_ABOUT') {
            $('#show-about h3').text(value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.MENU_TITLE_EDIT') {
            $('#head-navi-tooltip-program').attr('data-original-title', value).tooltip('fixTitle');
            $('#head-navi-tooltip-configuration').attr('data-original-title', value).tooltip('fixTitle');
        } else if (lkey === 'Blockly.Msg.MENU_TITLE_ROBOT') {
            $('#head-navi-tooltip-robot').attr('data-original-title', value).tooltip('fixTitle');
        } else if (lkey === 'Blockly.Msg.MENU_TITLE_HELP') {
            $('#head-navi-tooltip-help').attr('data-original-title', value).tooltip('fixTitle');
        } else if (lkey === 'Blockly.Msg.MENU_TITLE_USER') {
            $('#head-navi-tooltip-user').attr('data-original-title', value).tooltip('fixTitle');
        } else if (lkey === 'Blockly.Msg.MENU_TITLE_USER_STATE') {
            $('#iconDisplayLogin').attr('data-original-title', value).tooltip('fixTitle');
        } else if (lkey === 'Blockly.Msg.MENU_TITLE_ROBOT_STATE') {
            $('#iconDisplayRobotState').attr('data-original-title', value).tooltip('fixTitle');
        } else {
            $(this).html(value);
        }
    });
};

/**
 * Switch to another language
 * 
 * @param {langCode}
 *            Code of language to switch to
 * @param {forceSwitch}
 *            force the language setting
 */
function switchLanguage(langCode, forceSwitch) {
    if (forceSwitch || userState.language != langCode) {
        var langs = [ 'DE', 'EN' ];
        if (langs.indexOf(langCode) < 0) {
            langCode = "DE";
        }

        for (i = 0; i < langs.length; i++) {
            $('.' + langs[i] + '').css('display', 'none');
        }
        $('.' + langCode + '').css('display', 'inline');

        userState.language = langCode;
        var url = 'blockly/msg/js/' + langCode.toLowerCase() + '.js';
        var future = $.getScript(url);
        future.then(switchLanguageInBlockly);
        if (Blockly.mainWorkspace !== null) {
            document.getElementById('bricklyFrame').contentWindow.switchLanguage(url);
        }
    }
}

/**
 * Switch blockly to another language
 */
function switchLanguageInBlockly(url) {
    var future = translate();
    var programBlocks = null;
    if (Blockly.mainWorkspace !== null) {
        var xmlProgram = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
        programBlocks = Blockly.Xml.domToText(xmlProgram);
    }
    // translate programming tab
    COMM.json("/admin", {
        "cmd" : "loadT",
        "name" : userState.toolbox
    }, function(result) {
        injectBlockly(result, programBlocks);
    });
}

/**
 * Initialize language switching
 */
function initializeLanguages() {
    if (navigator.language.indexOf("en") > -1) {
        switchLanguage('EN', true);
        $('#chosenLanguage').text('EN');
    } else {
        switchLanguage('DE', true)
        $('#chosenLanguage').text('DE');
    }

    $('#language').on('click', '.dropdown-menu li a', function() {
        var chosenLanguage = $(this).text();
        $('#chosenLanguage').text(chosenLanguage);
        switchLanguage(chosenLanguage, false);
    });
}

/**
 * Display information
 * 
 * @param {result}
 *            Response of a REST-call.
 * @param {successMessage}
 *            Toast-message to be displayed if REST-call was ok.
 * @param {result}
 *            Popup-message to be displayed if REST-call failed.
 * @param {messageParam}
 *            Parameter to be used in the message text.
 */
function displayInformation(result, successMessage, errorMessage, messageParam) {
    if (result.rc === "ok") {
        $('.modal').modal('hide'); // close all opened popups
        displayMessage(successMessage, "TOAST", messageParam);
    } else {
        displayMessage(errorMessage, "POPUP", messageParam);
    }
}

/**
 * Display message
 * 
 * @param {messageId}
 *            ID of message to be displayed
 * @param {output}
 *            where to display the message, "TOAST" or "POPUP"
 * @param {replaceWith}
 *            Text to replace an optional '$' in the message-text
 * 
 */
function displayMessage(messageId, output, replaceWith) {
    if (messageId != undefined) {
        if (messageId.indexOf(".") >= 0 || messageId.toUpperCase() != messageId) {
            // Invalid Message-Key 
            LOG.info('Invalid message-key received: ' + messageId);
        }

        var lkey = 'Blockly.Msg.' + messageId;
        var value = Blockly.Msg[messageId];
        if (value === undefined || value === '') {
            value = messageId;
        }
        if (value.indexOf("$") >= 0) {
            value = value.replace("$", replaceWith);
        }

        if (output === 'POPUP') {
            $('#message').attr('lkey', lkey);
            $('#message').html(value);
            $("#show-message").modal("show");
        } else if (output === 'TOAST') {
            toastMessages.unshift(value);
            if (toastMessages.length === 1) {
                displayToastMessages();
            }
        }
    }
}

/**
 * Display toast messages
 */
function displayToastMessages() {
    $('#toastText').text(toastMessages[toastMessages.length - 1]);
    $('#toastContainer').delay(100).fadeIn("slow", function() {
        $(this).delay(1000).fadeOut("slow", function() {
            toastMessages.pop();
            if (toastMessages.length > 0) {
                displayToastMessages();
            }
        });
    });
}

/**
 * Activate program and config menu when in frames that hides blockly/brickly.
 */
function activateProgConfigMenu() {
    $('#head-navigation-program-edit > ul > li').removeClass('disabled');
    $('#head-navigation-configuration-edit > ul > li').removeClass('disabled');
    setHeadNavigationMenuState(userState.id === -1 ? 'logout' : 'login');
    if (userState.programSaved) {
        $('#menuSaveProg').parent().removeClass('login');
        $('#menuSaveProg').parent().removeClass('disabled');
        Blockly.getMainWorkspace().saveButton.enable();
    }
    if (userState.configurationSaved) {
        $('#menuSaveConfig').parent().removeClass('login');
        $('#menuSaveConfig').parent().removeClass('disabled');
        document.getElementById('bricklyFrame').contentWindow.Blockly.getMainWorkspace().saveButton.enable();
    }
}
/**
 * Deactivate program and config menu.
 */
function deactivateProgConfigMenu() {
    $('#head-navigation-program-edit > ul > li').addClass('disabled');
    $('#head-navigation-configuration-edit > ul > li').addClass('disabled');
}

/**
 * Regularly ping the server to keep status information up-to-date
 */
function pingServer() {
    if (userState.doPing) {
        setTimeout(function() {
            COMM.ping(function(result) {
                setRobotState(result);
                pingServer();
            });
        }, 5000);
    }
}

/**
 * Handle server errors
 */
function handleServerErrors() {
    userState.doPing = false;
}

/**
 * Set modification state.
 * 
 * @param {Boolean}
 *            modified or not.
 */
function setWorkspaceModified(modified) {
    userState.programModified = modified;
}

/**
 * Initializations
 */
function init() {
    initLogging();
    initUserState();
    $('#tabProgramName').text(userState.program);
    $('#tabConfigurationName').text(userState.configuration);
    initTabs();
    initPopups();
    initHeadNavigation();
    UTIL.initDataTables();
    initProgramNameTable();
    initConfigurationNameTable();
    initializeLanguages();
    pingServer();
    $('#menuTabProgram').parent().addClass('disabled');
    $('#tabProgram').addClass('tabClicked');
    $('#head-navigation-configuration-edit').css('display', 'none');
    COMM.setErrorFn(handleServerErrors);
    if (!UTIL.getCookie("hideStartupMessage")) {
        $("#show-startup-message").modal("show");
    }

    // Workaround to set the focus on input fields with attribute 'autofocus'
    $('.modal').on('shown.bs.modal', function() {
        $(this).find('[autofocus]').focus();
    });

    $(window).on('beforeunload', function(e) {
        if (userState.programModified === true || userState.configurationModified === true) {
            if (userState.id === -1) {
// Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD']);
                return Blockly.Msg.POPUP_BEFOREUNLOAD;
            } else {
// Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD_LOGGEDIN']);
                return Blockly.Msg.POPUP_BEFOREUNLOAD_LOGGEDIN;
            }
        }
    });
    
    $('[rel="tooltip"]').tooltip({
        placement : "right"
    });
    
    UTIL.resizeTabBar(); // for small devices only
};

$(window).on('resize', UTIL.resizeTabBar); // for small devices only

$(document).ready(WRAP.fn3(init, 'page init'));
