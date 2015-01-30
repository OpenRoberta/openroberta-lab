var bricklyActive = false;
var userState = {};
var toastMessages = [];

/**
 * Initialize user-state-object
 */
function initUserState() {
    userState.version = 'xx.xx.xx';
    userState.language = 'de';
    userState.id = -1;
    userState.accountName = '';
    userState.name = '';
    userState.program = 'meinProgramm';
    userState.configuration = 'Standardkonfiguration';
    userState.programSaved = false;
    userState.configurationSaved = false;
    userState.brickSaved = false;
    userState.toolbox = 'beginner';
    userState.token = '1A2B3C4D';
    userState.doPing = true;
    userState['robot.name'] = '';
    userState['robot.state'] = '';
    userState['robot.battery'] = '';
    userState['robot.wait'] = '';
}

/**
 * Create new user
 */
function saveUserToServer() {
    var $userAccountName = $("#accountName");
    var $userName = $('#userName');
    var $userEmail = $("#userEmail");
    var $pass1 = $('#pass1');
    var $pass2 = $('#pass2');

    if ($pass1.val() != $pass2.val()) {
        displayMessage("MESSAGE_PASSWORD_ERROR", "POPUP");
    } else {
        COMM.json("/user", {
            "cmd" : "createUser",
            "accountName" : $userAccountName.val(),
            "userName" : $userName.val(),
            "userEmail" : $userEmail.val(),
            "password" : $pass1.val(),
            "role" : 'TEACHER',
        }, function(result) {
            if (result.rc === "ok") {
                setRobotState(result);
                $(".ui-dialog-content").dialog("close"); // close all opened popups
                $('#accountNameS').val($userAccountName.val());
                $('#pass1S').val($pass1.val());
                displayMessage(result.message, "TOAST");
                login();
            } else {
                displayMessage(result.message, "POPUP");
            }
        });
    }
}

/**
 * Delete user on server
 */
function deleteUserOnServer() {
    var $pass1 = $('#pass1D');
    COMM.json("/user", {
        "cmd" : "deleteUser",
        "accountName" : userState.accountName,
        "password" : $pass1.val()
    }, function(result) {
        if (result.rc === "ok") {
            logout();
            displayMessage(result.message, "TOAST");
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage(result.message, "POPUP");
        }
    });
}

/**
 * Login user
 */
function login() {
    var $userAccountName = $("#accountNameS");
    var $pass1 = $('#pass1S');
    COMM.json("/user", {
        "cmd" : "login",
        "accountName" : $userAccountName.val(),
        "password" : $pass1.val(),
    }, function(response) {
        if (response.rc === "ok") {
            userState.accountName = response.userAccountName;
            userState.name = response.userAccountName;
            userState.id = response.userId;
            setHeadNavigationMenuState('login');
            setRobotState(response);
            displayMessage(response.message, "TOAST");
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage(response.message, "POPUP");
        }
    });
}

/**
 * Logout user
 */
function logout() {
    COMM.json("/user", {
        "cmd" : "logout"
    }, function(response) {
        if (response.rc === "ok") {
            initUserState();
            $('#programNameSave').val('');
            $('#configurationNameSave').val('');
            setHeadNavigationMenuState('logout');
            setRobotState(response);
            displayMessage(response.message, "TOAST");
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage(response.message, "POPUP");
        }
    });
}

/**
 * Update Firmware
 */
function updateFirmware() {
    COMM.json("/robot", {
        "cmd" : "updateFirmware"
    }, function(response) {
        if (response.rc === "ok") {
            setRobotState(response);
            displayMessage(response.message, "TOAST");
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage(response.message, "POPUP");
        }
    });
}

/**
 * Inject Blockly with initial toolbox
 * 
 * @param {response}
 *            toolbox
 */
function injectBlockly(toolbox) {
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
        initProgramEnvironment();
        setRobotState(toolbox);
    }
}

function initProgramEnvironment() {
    Blockly.getMainWorkspace().clear();
    // should this come from the server?
    var text = "<block_set xmlns='http: // www.w3.org/1999/xhtml'>" + "<instance x='100' y='50'>" + "<block type='robControls_start'>" + "</block>"
            + "</instance>" + "</block_set>";
    var xml = Blockly.Xml.textToDom(text);
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
        displayState();
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
        displayState();
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
    COMM.json("/blocks", {
        "cmd" : "setToken",
        "token" : resToken
    }, function(response) {
        if (response.rc === "ok") {
            userState.token = resToken;
            setRobotState(response);
            displayMessage(response.message, "TOAST");
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage(response.message, "POPUP");
        }
    });
}

/**
 * Handle result of server call
 * 
 * @param {result}
 *            Result-object from server call
 */
function response(result) {
    var str = "{";
    var comma = false;
    for (key in result) {
        if (comma) {
            str += ',';
        } else {
            comma = true;
        }
        str += '"' + key + '":';
        if (result.hasOwnProperty(key)) {
            // The output of items is limited to the first 100 characters
            if (result[key].length > 100) {
                str += '"' + JSON.stringify(result[key]).substring(1, 100) + ' ..."';
            } else {
                str += JSON.stringify(result[key]);
            }
        }
    }
    str += '}';
    LOG.info('result from server: ' + str);

    if (result.rc === 'ok') {
        displayMessage(result.message, "TOAST");
    } else {
        displayMessage(result.message, "POPUP");
    }
}

/**
 * Handle result of server call and refresh program list
 * 
 * @param {result}
 *            Result-object from server call
 */
function responseAndRefreshProgramList(result) {
    response(result);
    COMM.json("/program", {
        "cmd" : "loadPN",
    }, showPrograms);
}

/**
 * Handle result of server call and refresh configuration list
 * 
 * @param {result}
 *            Result-object from server call
 */
function responseAndRefreshConfigurationList(result) {
    response(result);
    COMM.json("/conf", {
        "cmd" : "loadCN",
    }, showConfigurations);
}

/**
 * Save program to server
 */
function saveToServer() {
    var progName = $('#programNameSave').val();
    if (progName != '') {
        setProgram(progName);
        $('#menuSaveProg').parent().removeClass('login');
        $('#menuSaveProg').parent().removeClass('disabled');
    }
    if (userState.program) {
        var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
        var xml_text = Blockly.Xml.domToText(xml);
        userState.programSaved = true;
        LOG.info('save program ' + userState.program + ' login: ' + userState.id);
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        return COMM.json("/program", {
            "cmd" : "saveP",
            "name" : userState.program,
            "program" : xml_text
        }, function(result) {
            if (result.rc === 'ok') {
                displayMessage(result.message, "TOAST");
            } else {
                displayMessage(result.message, "POPUP");
            }
        });
    } else {
        displayMessage("MESSAGE_EMPTY_NAME", "POPUP");
    }
}

/**
 * Save configuration to server
 */
function saveConfigurationToServer() {
    var confName = $('#configurationNameSave').val();
    if (confName != '') {
        setConfiguration(confName);
        $('#menuSaveConfig').parent().removeClass('login');
        $('#menuSaveConfig').parent().removeClass('disabled');
    }
    if (userState.configuration) {
        userState.configurationSaved = true;
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var xml_text = document.getElementById('bricklyFrame').contentWindow.getXmlOfConfiguration(userState.configuration);
        LOG.info('save brick configuration ' + userState.configuration);
        COMM.json("/conf", {
            "cmd" : "saveC",
            "name" : userState.configuration,
            "configuration" : xml_text
        }, function(result) {
            if (result.rc === 'ok') {
                displayMessage(result.message, "TOAST");
            } else {
                displayMessage(result.message, "POPUP");
            }
        });
    } else {
        displayMessage("MESSAGE_EMPTY_NAME", "POPUP");
    }
}

/**
 * Run program
 */
function runOnBrick() {
    LOG.info('run ' + userState.program + ' signed in: ' + userState.id);
    var xml_program = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
    var xml_text_program = Blockly.Xml.domToText(xml_program);
    var xml_text_configuration = document.getElementById('bricklyFrame').contentWindow.getXmlOfConfiguration(userState.configuration);
    return COMM.json("/program", {
        "cmd" : "runP",
        "name" : userState.program,
        "configuration" : userState.configuration,
        "programText" : xml_text_program,
        "configurationText" : xml_text_configuration
    }, response);
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
        bricklyActive = false;
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
        document.getElementById('bricklyFrame').contentWindow.showConfiguration(load, result.data);
        LOG.info('show configuration ' + userState.configuration + ' signed in: ' + userState.id);
        bricklyActive = true;
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
        COMM.json("/program", {
            "cmd" : "loadP",
            "name" : programName
        }, function(result) {
            if (result.rc === 'ok') {
                $("#tabs").tabs("option", "active", 0);
                userState.programSaved = true;
                displayMessage(result.message, "TOAST");
                showProgram(result, true, programName);
                $('#menuSaveProg').parent().removeClass('login');
                $('#menuSaveProg').parent().removeClass('disabled');
            } else {
                displayMessage(result.message, "POPUP");
            }
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
        COMM.json("/conf", {
            "cmd" : "loadC",
            "name" : configurationName
        }, function(result) {
            if (result.rc === 'ok') {
                $("#tabs").tabs("option", "active", 0);
                userState.configurationSaved = true;
                displayMessage(result.message, "TOAST");
                showConfiguration(result, true, configurationName);
                $('#menuSaveConfig').parent().removeClass('login');
                $('#menuSaveConfig').parent().removeClass('disabled');
                setRobotState(result);
            } else {
                displayMessage(result.message, "POPUP");
            }
        });
    }
}

/**
 * Delete the program that was selected in program list
 */
function deleteFromListing() {
    var $programRow = $('#programNameTable .selected');
    if ($programRow.length > 0) {
        var programName = $programRow[0].children[0].textContent;
        LOG.info('deleteFromList ' + programName + ' signed in: ' + userState.id);
        COMM.json("/program", {
            "cmd" : "deleteP",
            "name" : programName
        }, function(result) {
            if (result.rc === 'ok') {
                displayMessage(result.message, "TOAST");
                responseAndRefreshProgramList(result);
                setRobotState(result);
            } else {
                displayMessage(result.message, "POPUP");
            }
        });
    }
}

/**
 * Delete the configuration that was selected in configurations list
 */
function deleteConfigurationFromListing() {
    var $configurationRow = $('#configurationNameTable .selected');
    if ($configurationRow.length > 0) {
        var configurationName = $configurationRow[0].children[0].textContent;
        LOG.info('deleteFromConfigurationList ' + configurationName + ' signed in: ' + userState.id);
        COMM.json("/conf", {
            "cmd" : "deleteC",
            "name" : configurationName
        }, function(result) {
            if (result.rc === 'ok') {
                displayMessage(result.message, "TOAST");
                responseAndRefreshConfigurationList(result);
                setRobotState(result);
            } else {
                displayMessage(result.message, "POPUP");
            }
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
    COMM.json("/blocks", {
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

function selectionPFn(event) {
    $('#programNameTable .selected').removeClass('selected');
    $(event.target.parentNode).toggleClass('selected');
}

function selectionCFn(event) {
    $('#configurationNameTable .selected').removeClass('selected');
    $(event.target.parentNode).toggleClass('selected');
}

function beforeActivateTab(event, ui) {
    $('#tabs').tabs("refresh");
    if (ui.newPanel.selector === '#progListing') {
        COMM.json("/program", {
            "cmd" : "loadPN",
        }, showPrograms);
    } else if (ui.newPanel.selector === '#confListing') {
        COMM.json("/conf", {
            "cmd" : "loadCN",
        }, showConfigurations);
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
//        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_BLOCKS'>Blöcke</span>",
//        "sClass" : "programs"
//    }, {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>Erzeugt am</span>",
        "sClass" : "programs"
    }, {
        "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>Letzte Aktualisierung</span>",
        "sClass" : "programs"
    }, ];
    var $programs = $('#programNameTable');
    $programs
            .dataTable({
                "sDom" : '<lip>t<r>',
                "aaData" : [],
                "aoColumns" : columns,
                "aoColumnDefs" : [ { // format date fields
                    "aTargets" : [ 3, 4 ], // indexes of columns to be formatted
                    "sType" : "date",
                    "mRender" : function(data) {
                        return formatDate(data);
                    }
                } ],
                "bJQueryUI" : true,
                "sPaginationType" : "full_numbers",
                "bPaginate" : true,
                "iDisplayLength" : 20,
                "oLanguage" : {
                    "sLengthMenu" : '<span lkey="Blockly.Msg.DATATABLE_SHOW">Zeige</span> <select>'
                            + '<option value="10">10</option><option value="20">20</option><option value="25">25</option>'
                            + '<option value="30">30</option><option value="100">100</option><option value="-1">All</option>'
                            + '</select> <span lkey="Blockly.Msg.DATATABLE_PROGRAMS">Programme</span>',
                    "oPaginate" : {
                        "sFirst" : "<span lkey='Blockly.Msg.DATATABLE_FIRST'>&lt;&lt; Erste</span>",
                        "sPrevious" : "<span lkey='Blockly.Msg.DATATABLE_PREVIOUS'>&lt; Vorige</span>",
                        "sNext" : "<span lkey='Blockly.Msg.DATATABLE_NEXT'>Nächste &gt;</span>",
                        "sLast" : "<span lkey='Blockly.Msg.DATATABLE_LAST'>Letzte &gt;&gt;</span>"
                    },
                    "sEmptyTable" : "<span lkey='Blockly.Msg.DATATABLE_EMPTY_TABLE'>Die Tabelle ist leer</span>",
                    "sInfo" : "<span lkey='Blockly.Msg.DATATABLE_SHOWING'>Zeige</span> _START_ <span lkey='Blockly.Msg.DATATABLE_TO'>bis</span> _END_ <span lkey='Blockly.Msg.DATATABLE_OF'>von</span> _TOTAL_ <span lkey='Blockly.Msg.DATATABLE_ENTRIES'>Einträgen</span>",
                    "sInfoEmpty" : "&nbsp;"
                },
                "fnDrawCallback" : function() {
                }
            });
    $('#programNameTable tbody').onWrap('click', 'tr', selectionPFn);
    $('#programNameTable tbody').onWrap('dblclick', 'tr', function(event) {
        selectionPFn(event);
        if ($('#loadFromListing').css('display') === 'inline') {
            $('#loadFromListing').click();
        } else if ($('#deleteFromListing').css('display') === 'inline') {
            $('#deleteFromListing').click();
        }
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
    $configurations
            .dataTable({
                "sDom" : '<lip>t<r>',
                "aaData" : [],
                "aoColumns" : columns,
                "aoColumnDefs" : [ { // format date fields
                    "aTargets" : [ 2, 3 ], // indexes of columns to be formatted
                    "sType" : "date",
                    "mRender" : function(data) {
                        return formatDate(data);
                    }
                } ],
                "bJQueryUI" : true,
                "sPaginationType" : "full_numbers",
                "bPaginate" : true,
                "iDisplayLength" : 20,
                "oLanguage" : {
                    "sLengthMenu" : '<span lkey="Blockly.Msg.DATATABLE_SHOW">Zeige</span> <select>'
                            + '<option value="10">10</option><option value="20">20</option><option value="25">25</option>'
                            + '<option value="30">30</option><option value="100">100</option><option value="-1">All</option>'
                            + '</select> <span lkey="Blockly.Msg.DATATABLE_CONFIGURATIONS">Konfigurationen</span>',
                    "oPaginate" : {
                        "sFirst" : "<span lkey='Blockly.Msg.DATATABLE_FIRST'>&lt;&lt; Erste</span>",
                        "sPrevious" : "<span lkey='Blockly.Msg.DATATABLE_PREVIOUS'>&lt; Vorige</span>",
                        "sNext" : "<span lkey='Blockly.Msg.DATATABLE_NEXT'>Nächste &gt;</span>",
                        "sLast" : "<span lkey='Blockly.Msg.DATATABLE_LAST'>Letzte &gt;&gt;</span>"
                    },
                    "sEmptyTable" : "<span lkey='Blockly.Msg.DATATABLE_EMPTY_TABLE'>Die Tabelle ist leer</span>",
                    "sInfo" : "<span lkey='Blockly.Msg.DATATABLE_SHOWING'>Zeige</span> _START_ <span lkey='Blockly.Msg.DATATABLE_TO'>bis</span> _END_ <span lkey='Blockly.Msg.DATATABLE_OF'>von</span> _TOTAL_ <span lkey='Blockly.Msg.DATATABLE_ENTRIES'>Einträgen</span>",
                    "sInfoEmpty" : "&nbsp;"
                },
                "fnDrawCallback" : function() {
                }
            });
    $('#configurationNameTable tbody').onWrap('click', 'tr', selectionCFn);
    $('#configurationNameTable tbody').onWrap('dblclick', 'tr', function(event) {
        selectionCFn(event);
        if ($('#loadConfigurationFromListing').css('display') === 'inline') {
            $('#loadConfigurationFromListing').click();
        } else if ($('#deleteConfigurationFromListing').css('display') === 'inline') {
            $('#deleteConfigurationFromListing').click();
        }
    });
}

/**
 * Format date
 * 
 * @param {date}
 *            date from server to be formatted
 */
function formatDate(date) {
    if (date) {
        var YYYY = date.substring(0, 4);
        var MM = date.substring(5, 7);
        var DD = date.substring(8, 10);
        var hh = date.substring(11, 13);
        var mm = date.substring(14, 16);
        var ss = date.substring(17, 19);
        var str = DD + '.' + MM + '.' + YYYY + ', ' + hh + ':' + mm + ':' + ss;
        return str;
    }
    return "";
}

function switchToBlockly() {
    $('#tabs').css('display', 'inline');
    $('#bricklyFrame').css('display', 'none');
    $('#tabBlockly').click();
}

function switchToBrickly() {
    $('#tabs').css('display', 'none');
    $('#bricklyFrame').css('display', 'inline');
}

/**
 * Display status information in the navigation bar
 */
function displayState() {
    if (userState.accountName) {
        $('#iconDisplayLogin').removeClass('error');
        $('#iconDisplayLogin').addClass('ok');
    } else {
        $('#iconDisplayLogin').removeClass('ok');
        $('#iconDisplayLogin').addClass('error');
    }

    if (userState['robot.state'] != 'busy') {
       $('#iconDisplayRobotState').removeClass('ok');
       $('#iconDisplayRobotState').addClass('error');
    } else {
        $('#iconDisplayRobotState').removeClass('error');
        $('#iconDisplayRobotState').addClass('ok');
    }

    $('#tabProgramName').text(userState.program);
    $('#tabConfigurationName').text(userState.configuration);
}

function setHeadNavigationMenuState(state) {
    $('.nav-pills > li > ul > li').removeClass('disabled');
    if (state === 'login') {
        $('.nav-pills > li > ul > .login').addClass('disabled');
    } else if (state === 'logout') {
        $('.nav-pills > li > ul > .logout').addClass('disabled');
    }
}

/**
 * Escape periods and colons in given string
 * 
 * @param {str}
 *            string to be escaped
 */
function escape(str) {
    return str.replace(/(:|\.|\[|\])/g, "\\$1");
}

/**
 * Initialize the navigation bar in the head of the page
 */
function initHeadNavigation() {

    $('.navbar-fixed-top').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;

        if (domId === 'menuTabProgram') { //  Submenu 'Overview'
            $('#tabProgram').click();
        } else if (domId === 'menuTabConfiguration') { //  Submenu 'Overview'
            $('#tabConfiguration').click();
        } else if (domId === 'menuRunProg') { //  Submenu 'Program'
            runOnBrick();
        } else if (domId === 'menuCheckProg') { //  Submenu 'Program'
            displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP");
        } else if (domId === 'menuNewProg') { //  Submenu 'Program'
            setProgram("meinProgramm");
            $('#tabProgram').click();
        } else if (domId === 'menuListProg') { //  Submenu 'Program'
            deactivateHeadMenu();
            switchToBlockly();
            $('#tabListing').click();
        } else if (domId === 'menuSaveProg') { //  Submenu 'Program'
            saveToServer(response);
        } else if (domId === 'menuSaveAsProg') { //  Submenu 'Program'
            $("#save-program").dialog("open");
        } else if (domId === 'menuAttachProg') { //  Submenu 'Program'
            $("#attach-program").dialog("open");
        } else if (domId === 'menuPropertiesProg') { //  Submenu 'Program'
        } else if (domId === 'menuToolboxBeginner') {   // Submenu 'Program'
            loadToolbox('beginner');
            $('#menuToolboxBeginner').addClass('disabled');
            $('#menuToolboxExpert').removeClass('disabled');
        } else if (domId === 'menuToolboxExpert') {   // Submenu 'Program'
            loadToolbox('expert');
            $('#menuToolboxExpert').addClass('disabled');
            $('#menuToolboxBeginner').removeClass('disabled');
        } else if (domId === 'menuCheckConfig') { //  Submenu 'Configuration'
            displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP");
        } else if (domId === 'menuNewConfig') { //  Submenu 'Configuration'
            setConfiguration("Standardkonfiguration");
            switchToBrickly();
        } else if (domId === 'menuListConfig') { //  Submenu 'Configuration'
            deactivateHeadMenu();
            switchToBlockly();
            $('#tabConfigurationListing').click();
        } else if (domId === 'menuSaveConfig') { //  Submenu 'Configuration'
            saveConfigurationToServer();
        } else if (domId === 'menuSaveAsConfig') { //  Submenu 'Configuration'
            $("#save-configuration").dialog("open");
        } else if (domId === 'menuPropertiesConfig') { //  Submenu 'Configuration'
        } else if (domId === 'menuConnect') { // Submenu 'Robot'
            $("#set-token").dialog("open");
        } else if (domId === 'menuFirmware') { // Submenu 'Robot'
            updateFirmware();
        } else if (domId === 'menuRobotInfo') { // Submenu 'Robot'
            $("#robotName").text(userState['robot.name']);
            $("#robotState").text(userState['robot.state']);
            $("#robotBattery").text(userState['robot.battery']);
            $("#robotWait").text(userState['robot.wait']);
            $("#show-robot-info").dialog("open");
        } else if (domId === 'menuFirstSteps') { // Submenu 'Help'
            if (userState.language === 'De') {
                window.open("http://www.open-roberta.org/erste-schritte.html");
            } else {
                window.open("http://www.open-roberta.org/index.php?id=59&L=1");
            }
        } else if (domId === 'menuStartProgramming') { // Submenu 'Help'
            if (userState.language === 'De') {
                window.open("http://dev.open-roberta.org/willkommen.html");
            } else {
                window.open("http://dev.open-roberta.org/willkommen.html?&L=1");
            }
        } else if (domId === 'menuFaq') { // Submenu 'Help'
            if (userState.language === 'De') {
                if (userState.language === 'De') {
                    window.open("https://mp-devel.iais.fraunhofer.de/wiki/display/FAQ/FAQ+Home");
                } else {
                    window.open("https://mp-devel.iais.fraunhofer.de/wiki/display/FAQ/FAQ+Home");
                }
            } else {
                window.open("http://dev.open-roberta.org/willkommen.html?&L=1");
            }
        } else if (domId === 'menuStateInfo') { // Submenu 'Help'
            $("#loggedIn").text(userState.name);
            $("#programName").text(userState.program);
            $("#configurationName").text(userState.configuration);
            $("#toolbox").text(userState.toolbox);
            $("#show-state-info").dialog("open");
        } else if (domId === 'menuAbout') { // Submenu 'Help'
            $("#version").text(userState.version);
            $("#show-about").dialog("open");
        } else if (domId === 'menuLogging') {   // Submenu 'Help'
            switchToBlockly();
            $('#tabLogging').click();
        } else if (domId === 'menuLogin') { // Submenu 'Login'
            $("#login-user").dialog("open");
        } else if (domId === 'menuLogout') { // Submenu 'Login'
            logout();
        } else if (domId === 'menuNewUser') { // Submenu 'Login'
            $("#register-user").dialog("open");
        } else if (domId === 'menuChangeUser') { // Submenu 'Login'
            // open the same popup as in case 'new', but with fields prefilled, but REST-Call is missing
        } else if (domId === 'menuDeleteUser') { // Submenu 'Login'
            $("#delete-user").dialog("open");
        }
        return false;
    }, 'head navigation menu item clicked');

    // Close submenu on mouseleave
    $('.navbar-fixed-top').onWrap('mouseleave', function(event) {
        $('.navbar-fixed-top .dropdown').removeClass('open');
    });

    $('#logo').onWrap('click', function() {
        window.open('http://open-roberta.org');
    }, 'logo was clicked');

    $('#beta').onWrap('click', function() {
        window.open('http://open-roberta.org');
    }, 'beta logo was clicked');

    $('#tabProgram').onWrap('click', function() {
        activateHeadMenu();
        $('#tabConfiguration').removeClass('tabClicked');
        $('#tabProgram').addClass('tabClicked');
        $('#head-navigation-configuration-edit').css('display', 'none');
        $('#head-navigation-program-edit').css('display', 'inline');
        $('#menuTabProgram').parent().addClass('disabled');
        $('#menuTabConfiguration').parent().removeClass('disabled');
        switchToBlockly();
    });

    $('#tabConfiguration').onWrap('click', function() {
        activateHeadMenu();
        $('#tabProgram').removeClass('tabClicked');
        $('#tabConfiguration').addClass('tabClicked');
        $('#head-navigation-program-edit').css('display', 'none');
        $('#head-navigation-configuration-edit').css('display', 'inline');
        $('#menuTabConfiguration').parent().addClass('disabled');
        $('#menuTabProgram').parent().removeClass('disabled');
        switchToBrickly();
    });

    setHeadNavigationMenuState('logout');
}

/**
 * Initialize popups
 */
function initPopups() {
    // Standard Pop-Up settings, can be overwritten with unique HTML IDs - see below
    $(".jquerypopup").dialog({
        autoOpen : false,
        draggable : false,
        resizable : false,
        width : 400,
        show : {
            effect : 'fade',
            duration : 300
        },
        hide : {
            effect : 'fade',
            duration : 300
        }
    });

    // define general class for Pop-Up
    $(".jquerypopup").dialog("option", "dialogClass", "jquerypopup");

    $('#saveUser').onWrap('click', saveUserToServer, 'save the user data');
    $('#deleteUser').onWrap('click', deleteUserOnServer, 'delete user data');
    $('#doLogin').onWrap('click', login, 'login ');

    $('#attachProgram').onWrap('click', function() {
        // TODO
    }, 'add the blocks');

    $('#saveProgram').onWrap('click', function() {
        saveToServer();
    }, 'save program');

    $('#shareProgram').onWrap('click', function() {
        displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP");
    }, 'share program');

    $('#saveConfiguration').onWrap('click', function() {
        saveConfigurationToServer();
    }, 'save configuration');

    $('#shareConfiguration').onWrap('click', function() {
        displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP");
    }, 'share configuration');

    $('#setToken').onWrap('click', function() {
        var $token = $('#tokenValue');
        setToken($token.val());
    }, 'set token');

    $('#hideStartupMessage').onWrap('click', function() {
        $("#show-startup-message").dialog("close");
        setCookie("hideStartupMessage", true);
    }, 'hide startup-message');

    $('.cancelPopup').onWrap('click', function() {
        $('.ui-dialog-titlebar-close').click();
    });
    
    // Handle button events in popups
    $(".jquerypopup").keyup(function(event) {
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
            $(this).find("input.submit").click();                
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
        activateHeadMenu();
        loadFromListing();
    }, 'load blocks from program list');

    // load configuration
    $('#loadConfigurationFromListing').onWrap('click', function() {
        activateHeadMenu();
        loadConfigurationFromListing();
    }, 'load configuration from configuration list');

    // delete program
    $('#deleteFromListing').onWrap('click', function() {
        deleteFromListing();
    }, 'delete blocks from program list');

    // delete configuration
    $('#deleteConfigurationFromListing').onWrap('click', function() {
        deleteConfigurationFromListing();
    }, 'delete configuration from configurations list');

    // share program
    $('#shareFromListing').onWrap('click', function() {
        $("#share-program").dialog("open");
    }, 'share program');

    // share configuration
    $('#shareConfigurationFromListing').onWrap('click', function() {
        $("#share-configuration").dialog("open");
    }, 'share configuration');

    $('.backButton').onWrap('click', function() {
        activateHeadMenu();
        if (bricklyActive) {
            switchToBrickly();
        } else {
            $('#tabBlockly').click();
            switchLanguage(userState.language, true);
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
 * @param {response}
 *            response of server call
 */
function setRobotState(response) {
    if (response.version) {
        userState.version = response.version;
    }

    if (response['robot.wait'] != undefined) {
        userState['robot.wait'] = response['robot.wait'];
    } else {
        userState['robot.wait'] = undefined;
    }
    
    if (response['robot.battery'] != undefined) {
        userState['robot.battery'] = response['robot.battery'];
    } else {
        userState['robot.battery'] = '';
    }
    
    if (response['robot.name'] != undefined) {
        userState['robot.name'] = response['robot.name'];
    } else {
        userState['robot.name'] = '';
    }
    
    if (response['robot.state'] != undefined) {
        userState['robot.state'] = response['robot.state'];
    } else {
        userState['robot.state'] = '';
    }

    displayState();
}

/**
 * Translate the web page
 */
function translate(jsdata) {
    $("[lkey]").each(function(index) {
        var lkey = $(this).attr('lkey');
        var key = lkey.replace("Blockly.Msg.", "");
        var value = Blockly.Msg[key];
//        if (value == undefined) {
//            console.log('UNDEFINED    key : value = ' + key + ' : ' + value);            
//        }
        if (lkey === 'Blockly.Msg.MENU_LOG_IN') {
            $('#login-user').dialog('option', 'title', value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.POPUP_REGISTER_USER') {
            $('#register-user').dialog('option', 'title', value);
        } else if (lkey === 'Blockly.Msg.POPUP_DELETE_USER') {
            $('#delete-user').dialog('option', 'title', value);
        } else if (lkey === 'Blockly.Msg.POPUP_ATTACH_PROGRAM') {
            $('#attach-program').dialog('option', 'title', value);
        } else if (lkey === 'Blockly.Msg.POPUP_SAVE_PROGRAM') {
            $('#save-program').dialog('option', 'title', value);
        } else if (lkey === 'Blockly.Msg.POPUP_SHARE') {
            $('#share-program').dialog('option', 'title', value);
            $('#share-configuration').dialog('option', 'title', value);
        } else if (lkey === 'Blockly.Msg.POPUP_SAVE_CONFIGURATION') {
            $('#save-configuration').dialog('option', 'title', value);
        } else if (lkey === 'Blockly.Msg.POPUP_SET_TOKEN') {
            $('#set-token').dialog('option', 'title', value);
        } else if (lkey === 'Blockly.Msg.POPUP_HIDE_STARTUP_MESSAGE') {
            $('#hideStartupMessage').attr('value', value);
        } else if (lkey === 'Blockly.Msg.POPUP_ATTENTION') {
            $('#show-message').dialog('option', 'title', value);
            $('#show-startup-message').dialog('option', 'title', value);
        } else if (lkey === 'Blockly.Msg.POPUP_CANCEL') {
            $('.cancelPopup').attr('value', value);
            $('.backButton').attr('value', value);
        } else if (lkey === 'Blockly.Msg.BUTTON_LOAD') {
            $('.buttonLoad').attr('value', value);
        } else if (lkey === 'Blockly.Msg.BUTTON_DO_DELETE') {
            $('.buttonDelete').attr('value', value);
        } else if (lkey === 'Blockly.Msg.BUTTON_DO_SHARE') {
            $('.buttonShare').attr('value', value);
        } else if (lkey === 'Blockly.Msg.BUTTON_EMPTY_LIST') {
            $('#clearLog').attr('value', value);
        } else if (lkey === 'Blockly.Msg.HINT_LANGUAGE_GERMAN') {
            $('#setLangDe').prop('title', value);
        } else if (lkey === 'Blockly.Msg.HINT_LANGUAGE_ENGLISH') {
            $('#setLangEn').prop('title', value);
        } else if (lkey === 'Blockly.Msg.MENU_ROBOT_STATE_INFO') {
            $('#show-robot-info').dialog('option', 'title', value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.MENU_STATE_INFO') {
            $('#show-state-info').dialog('option', 'title', value);
            $(this).html(value);
        } else if (lkey === 'Blockly.Msg.MENU_ABOUT') {
            $('#show-about').dialog('option', 'title', value);
            $(this).html(value);
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
        var langs = [ 'De', 'En' ];
        for (i in langs) {
            $("." + langs[i] + "").css('display', 'none');
            $("#setLang" + langs[i] + "").removeClass('bigFlag');
            $("#setLang" + langs[i] + "").addClass('smallFlag');
        }
        if (langs.indexOf(langCode) < 0) {
            langCode = "De";
        }
        $("." + langCode + "").css('display', 'inline');
        $("#setLang" + langCode + "").removeClass('smallFlag');
        $("#setLang" + langCode + "").addClass('bigFlag');
        userState.language = langCode;
        var future = $.getScript('blockly/msg/js/' + langCode.toLowerCase() + '.js');
        future.then(switchLanguageInBlockly);
    }
}

/**
 * Switch blockly to another language
 */
function switchLanguageInBlockly() {
    var future = translate();
    COMM.json("/blocks", {
        "cmd" : "loadT",
        "name" : userState.toolbox
    }, injectBlockly);
}

/**
 * Initialize language switching
 */
function initializeLanguages() {
    $('#setLangDe').onWrap('click', function() {
        switchLanguage('De', false);
    }, 'switch language to "De"');

    $('#setLangEn').onWrap('click', function() {
        switchLanguage('En', false);
    }, 'switch language to "En"');
}

/**
 * Display message
 * 
 * @param {messageId}
 *            ID of message to be displayed
 * @param {output}
 *            where to display the message, "toast" or "popup"
 *         
 */
function displayMessage(messageId, output) {
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
            
        if (output === 'POPUP') {
            $('#message').attr('lkey', lkey);
            $('#message').html(value);
            $("#show-message").dialog("open");
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
    $('#toast').text(toastMessages[toastMessages.length - 1]);

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
 * Activate head menu
 */
function activateHeadMenu() {
    $('.nav > .dropdown > a').removeClass('disabled');
    $('#head-navigation > .nav-pills > li > a').removeClass('menuDisabled');
}

/**
 * Deactivate head menu
 */
function deactivateHeadMenu() {
    $('.nav > .dropdown > a').addClass('disabled');
    $('#head-navigation > .nav-pills > li > a').addClass('menuDisabled');
}

/**
 * Regularly ping the server to keep status information up-to-date
 */
function pingServer() {
    if (userState.doPing) {
        setTimeout(function() {
            COMM.json("/ping", {}, function(response) {
                setRobotState(response);
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
 * Set cookie
 * 
 * @param {key}
 *            Key of the cookie
 * @param {value}
 *            Value of the cookie
 */
function setCookie(key, value) {
    var expires = new Date();
    expires.setTime(expires.getTime() + (30 * 24 * 60 * 60 * 1000));
    document.cookie = key + '=' + value + ';expires=' + expires.toUTCString();
}

/**
 * Get cookie
 * 
 * @param {key}
 *            Key of the cookie to read
 */
function getCookie(key) {
    var keyValue = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
    return keyValue ? keyValue[2] : null;
}

/**
 * Initializations
 */
function init() {
    initLogging();
    initUserState();
    initTabs();
    initPopups();
    initHeadNavigation();
    initProgramNameTable();
    initConfigurationNameTable();
    initializeLanguages();
    switchLanguage('De', true);
    pingServer();
    $('#menuTabProgram').parent().addClass('disabled');
    $('#tabProgram').addClass('tabClicked');
    $('#head-navigation-configuration-edit').css('display', 'none');
    COMM.setErrorFn(handleServerErrors);
    if (!getCookie("hideStartupMessage")) {
        $("#show-startup-message").dialog("open");
    }
};

$(document).ready(WRAP.fn3(init, 'page init'));
