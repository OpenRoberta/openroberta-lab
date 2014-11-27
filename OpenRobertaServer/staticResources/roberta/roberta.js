var bricklyActive = false;
var userState = {};

/**
 * Initialize user-state-object
 */
function initUserState() {
    userState.language = 'de';
    userState.id = -1;
    userState.name = '';
    userState.role = '';
    userState.program = 'meinProgramm';
    userState.configuration = 'meineKonfiguration';
    userState.programSaved = false;
    userState.configurationSaved = false;
    userState.brickSaved = false;
    userState.robot = '';
    userState.robotState = 'robot.dontKnow';
    userState.toolbox = 'beginner';
    userState.token = '1A2B3C4D';
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
    var $role = $("input[name=role]:checked");

    if ($pass1.val() != $pass2.val()) {
        displayMessage("MESSAGE.PASSWORD_ERROR");
    } else if ($role.val() == null) {
        displayMessage("MESSAGE.TEACHER_OR_STUDENT");
    } else {
        var roleGerman = $role.val();
        var role = "STUDENT";
        if (roleGerman === "Lehrer") {
            role = "TEACHER";
        }
        COMM.json("/user", {
            "cmd" : "createUser",
            "accountName" : $userAccountName.val(),
            "userName" : $userName.val(),
            "userEmail" : $userEmail.val(),
            "password" : $pass1.val(),
            "role" : role
        }, function(result) {
            if (result.rc === "ok") {
                setRobotState(result);
                displayMessage("MESSAGE.USER_CREATED");
                $(".ui-dialog-content").dialog("close"); // close all opened popups
            } else {
                displayMessage("MESSAGE.USER_EXISTS");
            }
        });
    }
}

/**
 * Delete user on server
 */
function deleteUserOnServer() {
    var $userAccountName = $("#accountNameD");
    userState.id = null;
    COMM.json("/user", {
        "cmd" : "deleteUser",
        "accountName" : $userAccountName.val()
    }, function(result) {
        if (result.rc === "ok") {
            setRobotState(result);
            displayMessage("MESSAGE.USER_DELETED");
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage("MESSAGE.USER_DELETE_ERROR");
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
            userState.name = response.userAccountName;
            userState.id = response.userId;
            userState.role = response.userRole;
            setHeadNavigationMenuState('login');
            setRobotState(response);
            $("#tutorials").fadeOut(700);
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage("MESSAGE.LOGIN_ERROR");
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
            setHeadNavigationMenuState('logout');
            setRobotState(response);
        } else {
            displayMessage("MESSAGE.LOGOUT_ERROR");
        }
        $(".ui-dialog-content").dialog("close"); // close all opened popups
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
            check : true,
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
    if (token) {
        var resToken = token.toUpperCase(); 
        COMM.json("/blocks", {
            "cmd" : "setToken",
            "token" : resToken
        }, function(response) {
            if (response.rc === "ok") {
                userState.token = resToken;
                setRobotState(response);
                $(".ui-dialog-content").dialog("close"); // close all opened popups
            } else {
                displayMessage("message9");
            }
        });
    } else {
        displayMessage("MESSAGE.SET_TOKEN");
    }
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
        if(comma) {
            str += ',';
        } else {
            comma = true;
        }
        str += '"' + key + '":';
        if (result.hasOwnProperty(key)) {
            // The output of items is limited to the first 100 characters
            if (result[key].length > 100) {
                str += '"' + JSON.stringify(result[key]).substring(1,100) + ' ..."';
            } else {
                str += JSON.stringify(result[key]);
            }
        }
    }
    str += '}';    
    LOG.info('result from server: ' + str);
}

/**
 * Handle result of server call and refresh list if necessary
 * 
 * @param {result}
 *            Result-object from server call
 */
function responseAndRefreshList(result) {
    response(result);
    var activeTab = $("#tabs div[aria-expanded='true']" ).attr('id');
    if (activeTab === 'listing') {
        COMM.json("/program", {
            "cmd" : "loadPN",
        }, showPrograms);
    } else if (activeTab === 'confListing') {
        COMM.json("/conf", {
            "cmd" : "loadCN",
        }, showConfigurations);
    }
}

/**
 * Save program to server
 */
function saveToServer() {
    if ($('#programNameSave')) {
        var $name = $('#programNameSave');
        setProgram($name.val());
        if (userState.name) { // Is someone logged in?
            if (!$name.val() || $name.val() === "meinProgramm") {
                $('#head-navigation #submenu-program #save').addClass('login');
                $('#head-navigation #submenu-program #save').addClass('ui-state-disabled');
                displayMessage("MESSAGE.NAME_ERROR");
                return;
            }
            $('#head-navigation #submenu-program #save').removeClass('login');
            $('#head-navigation #submenu-program #save').removeClass('ui-state-disabled');
        }
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
        },  responseAndRefreshList);
    } else {
        displayMessage("MESSAGE.EMPTY_NAME");
    }
}

/**
 * Save configuration to server
 */
function saveConfigurationToServer() {
    if ($('#configurationNameSave')) {
        var $name = $('#configurationNameSave');
        setConfiguration($name.val());
        if (userState.name) { // Is someone logged in?
            if (!$name.val() || $name.val() === "meineKonfiguration") {
                $('#head-navigation #submenu-configuration #save').addClass('login');
                $('#head-navigation #submenu-configuration #save').addClass('ui-state-disabled');
                displayMessage("MESSAGE.NAME_ERROR");
                return;
            }
            $('#head-navigation #submenu-configuration #save').removeClass('login');
            $('#head-navigation #submenu-configuration #save').removeClass('ui-state-disabled');
        }
    }
    if (userState.configuration) {
        userState.configurationSaved = true;
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        document.getElementById('bricklyFrame').contentWindow.saveToServer(userState.configuration);
        responseAndRefreshList();
    } else {
        displayMessage("MESSAGE.EMPTY_NAME");
    }
}

/**
 * Run program
 */
function runOnBrick() {
    LOG.info('run ' + userState.program + ' signed in: ' + userState.id);
    return COMM.json("/program", {
        "cmd" : "runP",
        "name" : userState.program,
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

function showConfiguration(result, load, name) {
    response(result);
    if (result.rc === 'ok') {
        setConfiguration(name);
        var xml = Blockly.Xml.textToDom(result.data);
        if (load) {
            Blockly.mainWorkspace.clear();
        }
        Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
        LOG.info('show configuration ' + userState.configuration + ' signed in: ' + userState.id);
        switchToBrickly();
        bricklyActive = true;
    }
};

/**
 * Load program from server
 */
function loadFromServer(load) {
    var $name = $('#programNameLoad');
    COMM.json("/program", {
        "cmd" : "loadP",
        "name" : $name.val()
    }, function(result) {
        showProgram(result, load, $name.val());
    });
    LOG.info('load ' + $name.val() + ' signed in: ' + userState.id);
}

/**
 * Delete program on server
 */
function deleteOnServer() {
    var $name = $('#programNameDelete');
    userState.programSaved = false;
    if (usertState.program === $name.val) {
        setProgram();
    }
    LOG.info('del ' + $name.val() + ' signed in: ' + userState.id);
    COMM.json("/program", {
        "cmd" : "deleteP",
        "name" : $name.val()
    }, function(result) {
    });
}

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
            $("#tabs").tabs("option", "active", 0);
            $('#programNameSave').val(programName);
            userState.programSaved = true;
            showProgram(result, true, programName);
            $('#head-navigation #submenu-program #save').removeClass('login');
            $('#head-navigation #submenu-program #save').removeClass('ui-state-disabled');
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
            $("#tabs").tabs("option", "active", 0);
            $('#configurationNameSave').val(configurationName);
            userState.configurationSaved = true;
            showConfiguration(result, true, configurationName);
            $('#head-navigation #submenu-configuration #save').removeClass('login');
            $('#head-navigation #submenu-configuration #save').removeClass('ui-state-disabled');
            setRobotState(result);
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
            $('#programNameSave').val('');
            responseAndRefreshList(result);
            setRobotState(result);
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
            $('#configurationNameSave').val('');
            responseAndRefreshList(result);
            setRobotState(result);
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
        $('#head-navigation #displayToolbox').text(userState.toolbox);
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

function selectionFn(event) {
    $('#programNameTable .selected').removeClass('selected');
    $(event.target.parentNode).toggleClass('selected');
}

function beforeActivateTab(event, ui) {
    $('#tabs').tabs("refresh");
    if (ui.newPanel.selector === '#listing') {
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
        "sTitle" : "<span lkey='DATATABLE.PROGRAM_NAME'>Name des Programms</span>",
        "sClass" : "programs"
    }, {
        "sTitle" : "<span lkey='DATATABLE.CREATED_BY'>Erzeugt von</span>",
        "sClass" : "programs"
    }, {
        "sTitle" : "<span lkey='DATATABLE.BLOCKS'>Blöcke</span>",
        "sClass" : "programs"
    }, {
        "sTitle" : "<span lkey='DATATABLE.ICON'>Icon</span>",
        "sClass" : "programs"
    }, {
        "sTitle" : "<span lkey='DATATABLE.CREATED_ON'>Erzeugt am</span>",
        "sClass" : "programs"
    }, {
        "sTitle" : "<span lkey='DATATABLE.ACTUALIZATION'>Letzte Aktualisierung</span>",
        "sClass" : "programs"
    }, ];
    var $programs = $('#programNameTable');
    $programs.dataTable({
        "sDom" : '<lip>t<r>',
        "aaData" : [],
        "aoColumns" : columns,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bPaginate" : true,
        "iDisplayLength" : 20,
        "oLanguage" : {
            "sLengthMenu" : '<span lkey="DATATABLE.SHOW">Zeige</span> <select>' + '<option value="10">10</option><option value="20">20</option><option value="25">25</option>'
                    + '<option value="30">30</option><option value="100">100</option><option value="-1">All</option>' + '</select> <span lkey="DATATABLE.PROGRAMS">Programme</span>',
            "oPaginate": {
                "sFirst": "<span lkey='DATATABLE.FIRST'>Erste</span>",
                "sPrevious": "<span lkey='DATATABLE.PREVIOUS'>Vorige</span>",
                "sNext": "<span lkey='DATATABLE.NEXT'>Nächste</span>",
                "sLast": "<span lkey='DATATABLE.LAST'>Letzte</span>"
            },
            "sEmptyTable": "<span lkey='DATATABLE.EMPTY_TABLE'>Die Tabelle ist leer</span>",
            "sInfo": "<span lkey='DATATABLE.SHOWING'>Zeige</span> _START_ <span lkey='DATATABLE.TO'>bis</span> _END_ <span lkey='DATATABLE.OF'>von</span> _TOTAL_ <span lkey='DATATABLE.ENTRIES'>Einträgen</span>",
            "sInfoEmpty": "&nbsp;"
        },
        "fnDrawCallback" : function() {
        }
    });
    $('#programNameTable tbody').onWrap('click', 'tr', selectionFn);
    $('#programNameTable tbody').onWrap('dblclick', 'tr', function(event) {
        selectionFn(event);
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
        "sTitle" : "<span lkey='DATATABLE.CONFIGURATION_NAME'>Name der Konfiguration</span>",
        "sClass" : "configurations"
    }, {
        "sTitle" : "<span lkey='DATATABLE.CREATED_BY'>Erzeugt von</span>",
        "sClass" : "configurations"
    }, {
        "sTitle" : "<span lkey='DATATABLE.ICON'>Icon</span>",
        "sClass" : "configurations"
    }, {
        "sTitle" : "<span lkey='DATATABLE.CREATED_ON'>Erzeugt am</span>",
        "sClass" : "configurations"
    }, {
        "sTitle" : "<span lkey='DATATABLE.ACTUALIZATION'>Letzte Aktualisierung</span>",
        "sClass" : "configurations"
    }, ];
    var $configurations = $('#configurationNameTable');
    $configurations.dataTable({
        "sDom" : '<lip>t<r>',
        "aaData" : [],
        "aoColumns" : columns,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bPaginate" : true,
        "iDisplayLength" : 20,
        "oLanguage" : {
            "sLengthMenu" : '<span lkey="DATATABLE.SHOW">Zeige</span> <select>' + '<option value="10">10</option><option value="20">20</option><option value="25">25</option>'
                    + '<option value="30">30</option><option value="100">100</option><option value="-1">All</option>' + '</select> <span lkey="DATATABLE.CONFIGURATIONS">Konfigurationen</span>',
            "oPaginate": {
                "sFirst": "<span lkey='DATATABLE.FIRST'>Erste</span>",
                "sPrevious": "<span lkey='DATATABLE.PREVIOUS'>Vorige</span>",
                "sNext": "<span lkey='DATATABLE.NEXT'>Nächste</span>",
                "sLast": "<span lkey='DATATABLE.LAST'>Letzte</span>"
            },
            "sEmptyTable": "<span lkey='DATATABLE.EMPTY_TABLE'>Die Tabelle ist leer</span>",
            "sInfo": "<span lkey='DATATABLE.SHOWING'>Zeige</span> _START_ <span lkey='DATATABLE.TO'>bis</span> _END_ <span lkey='DATATABLE.OF'>von</span> _TOTAL_ <span lkey='DATATABLE.ENTRIES'>Einträgen</span>",
            "sInfoEmpty": "&nbsp;"
        },
        "fnDrawCallback" : function() {
        }
    });
    $('#configurationNameTable tbody').onWrap('click', 'tr', selectionFn);
    $('#configurationNameTable tbody').onWrap('dblclick', 'tr', function(event) {
        selectionFn(event);
        if ($('#loadConfigurationFromListing').css('display') === 'inline') {
            $('#loadConfigurationFromListing').click();
        } else if ($('#deleteConfigurationFromListing').css('display') === 'inline') {
            $('#deleteConfigurationFromListing').click();
        }
    });
}

/**
 * Run program
 */
function startProgram() {
    var saveFuture = saveToServer();
    saveFuture.then(runOnBrick);
}

/**
 * Check program
 */
function checkProgram() {
    // TODO
    displayMessage("MESSAGE.PROGRAM_NOT_CHECKABLE");
}

/**
 * Check configuration
 */
function checkConfiguration() {
    // TODO
    displayMessage("MESSAGE.CONFIGURATION_NOT_CHECKABLE");
}

function switchToBlockly() {
    $('#tabs').css('display', 'inline');
    $('#bricklyFrame').css('display', 'none');
}

function switchToBrickly() {
    $('#tabs').css('display', 'none');
    $('#bricklyFrame').css('display', 'inline');
}

/**
 * Display status information in the navigation bar
 */
function displayState() {
    if (userState.name) {
        $('#head-navigation #displayLogin').text(userState.name);
        $('#head-navigation #iconDisplayLogin').css('display', 'inline');
    } else {
        $('#head-navigation #displayLogin').text('');
        $('#head-navigation #iconDisplayLogin').css('display', 'none');
    }

    if (userState.program) {
        $('#head-navigation #displayProgram').text(userState.program);
        $('#head-navigation #iconDisplayProgram').css('display', 'inline');
    } else {
        $('#head-navigation #displayProgram').text('');
        $('#head-navigation #iconDisplayProgram').css('display', 'none');
    }

    if (userState.configuration) {
        $('#head-navigation #displayConfiguration').text(userState.configuration);
        $('#head-navigation #iconDisplayConfiguration').css('display', 'inline');
    } else {
        $('#head-navigation #displayConfiguration').text('');
        $('#head-navigation #iconDisplayConfiguration').css('display', 'none');
    }

    if (userState.toolbox) {
        $('#head-navigation #displayToolbox').text(userState.toolbox);
        $('#head-navigation #iconDisplayToolbox').css('display', 'inline');
    } else {
        $('#head-navigation #displayToolbox').text('');
        $('#head-navigation #iconDisplayToolbox').css('display', 'none');
    }

    if (userState.robotState) {
        $('#head-navigation #displayRobotState').text(userState.robotState);
        $('#head-navigation #iconDisplayRobotState').css('display', 'inline');
    } else {
        $('#head-navigation #displayRobotState').text('');
        $('#head-navigation #iconDisplayRobotState').css('display', 'none');
    }
}

function setHeadNavigationMenuState(state) {
    $('#head-navigation > li > ul > li').removeClass('ui-state-disabled');
    if (state === 'login') {
        $('#head-navigation > li > ul > .login').addClass('ui-state-disabled');
    } else if (state === 'logout') {
        $('#head-navigation > li > ul > .logout').addClass('ui-state-disabled');
    }
}

/**
 * Display message
 * 
 * @param {message}
 *            Messabe to be displayed
 */
function displayMessage(messageId) {
    $('.message').css('display', 'none');
    messageId = messageId.replace( /(:|\.|\[|\])/g, "\\$1" );   // Escape periods and colons
    $('#' + messageId + '').css('display', 'inline');
    $("#show-message").dialog("open");
}

/**
 * Initialize the navigation bar in the head of the page
 */
function initHeadNavigation() {

    $('#head-navigation').menu({
        position : {
            at : "left top+57",
            collision : "fit"
        }
    });

    // Open / close menu on click (for tablets)
    $('#head-navigation > li > a').onWrap('click', function(event) {
        var domId = event.target.id;
        if (domId === 'head-navigation-program') {
            if ($('#head-navigation #submenu-program').css('display') === 'none' ) {
                $('#head-navigation #submenu-program').css('display','block'); 
            } else {
                $('#head-navigation #submenu-program').css('display','none');               
            }
        } else if (domId === 'head-navigation-nepo') {
            if ($('#head-navigation #submenu-nepo').css('display') === 'none' ) {
                $('#head-navigation #submenu-nepo').css('display','block'); 
            } else {
                $('#head-navigation #submenu-nepo').css('display','none');               
            }
        } else if (domId === 'head-navigation-configuration') {
            if ($('#head-navigation #submenu-configuration').css('display') === 'none' ) {
                $('#head-navigation #submenu-configuration').css('display','block'); 
            } else {
                $('#head-navigation #submenu-configuration').css('display','none');               
            }
        } else if (domId === 'head-navigation-connection') {
            if ($('#head-navigation #submenu-connection').css('display') === 'none' ) {
                $('#head-navigation #submenu-connection').css('display','block'); 
            } else {
                $('#head-navigation #submenu-connection').css('display','none');               
            }
        } else if (domId === 'head-navigation-developertools') {
            if ($('#head-navigation #submenu-developertools').css('display') === 'none' ) {
                $('#head-navigation #submenu-developertools').css('display','block'); 
            } else {
                $('#head-navigation #submenu-developertools').css('display','none');               
            }
        } else if (domId === 'head-navigation-help') {
            if ($('#head-navigation #submenu-help').css('display') === 'none' ) {
                $('#head-navigation #submenu-help').css('display','block'); 
            } else {
                $('#head-navigation #submenu-help').css('display','none');               
            }
        } else if (domId === 'head-navigation-login') {
            if ($('#head-navigation #submenu-login').css('display') === 'none' ) {
                $('#head-navigation #submenu-login').css('display','block'); 
            } else {
                $('#head-navigation #submenu-login').css('display','none');               
            }
        }
    });

    // Submenu Program
    $('#head-navigation').onWrap('click', '#submenu-program > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'runProg') {
            startProgram();
        } else if (domId === 'checkProg') {
            checkProgram();
        } else if (domId === 'newProg') {
            initProgramEnvironment();
            setProgram("meinProgramm");
        } else if (domId === 'openProg') {
            switchToBlockly();
            $('#loadFromListing').css('display', 'inline');
            $('#deleteFromListing').css('display', 'none');
            $('#tabListing').click();
        } else if (domId === 'saveProg') {
            saveToServer(response);
        } else if (domId === 'saveAsProg') {
            $("#save-program").dialog("open");
        } else if (domId === 'attachProg') {
            $("#attach-program").dialog("open");
        } else if (domId === 'divideProg') {
        } else if (domId === 'deleteProg') {
            switchToBlockly();
            $('#deleteFromListing').css('display', 'inline');
            $('#loadFromListing').css('display', 'none');
            $('#tabListing').click();
        } else if (domId === 'propertiesProg') {
        }
        return false;
    }, 'sub menu of menu "program"');

    // Submenu Nepo
    $('#head-navigation').onWrap('click', '#submenu-nepo > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'toolboxBeginner') {
            loadToolbox('beginner');
            $('#toolboxBeginner').addClass('ui-state-disabled');
            $('#toolboxExpert').removeClass('ui-state-disabled');
        } else if (domId === 'toolboxExpert') {
            loadToolbox('expert');
            $('#toolboxExpert').addClass('ui-state-disabled');
            $('#toolboxBeginner').removeClass('ui-state-disabled');
        }
        return false;
    }, 'sub menu of menu "nepo"');

    // Submenu Roboter (Configuration)
    $('#head-navigation').onWrap('click', '#submenu-configuration > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'checkConfig') {
            checkConfiguration();
        } else if (domId === 'standardConfig') {
            switchToBrickly();
            bricklyActive = true;
        } else if (domId === 'newConfig') {
            setConfiguration("meineKonfiguration");
        } else if (domId === 'openConfig') {
            switchToBlockly();
            $('#loadConfigurationFromListing').css('display', 'inline');
            $('#deleteConfigurationFromListing').css('display', 'none');
            $('#tabConfigurationListing').click();
        } else if (domId === 'saveConfig') {
            saveConfigurationToServer();
        } else if (domId === 'saveAsConfig') {
            $("#save-configuration").dialog("open");
        } else if (domId === 'deleteConfig') {
            switchToBlockly();
            $('#deleteConfigurationFromListing').css('display', 'inline');
            $('#loadConfigurationFromListing').css('display', 'none');
            $('#tabConfigurationListing').click();
        } else if (domId === 'propertiesConfig') {
        }
        return false;
    }, 'sub menu of menu "roboter" ("configuration")');

    // Submenu Connection
    $('#head-navigation').onWrap('click', '#submenu-connection > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'connect') {
            $("#set-token").dialog("open");
        }
        return false;
    }, 'sub menu of menu "connection"');

    // Submenu Developertools
    $('#head-navigation').onWrap('click', '#submenu-developertools > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'logging') {
            switchToBlockly();
            $('#tabLogging').click();
        } else if (domId === 'simulator') {
            switchToBlockly();
            $('#tabSimulator').click();
        }
        return false;
    }, 'sub menu of menu "developertools"');

    // Submenu Help
    $('#head-navigation').onWrap('click', '#submenu-help > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'help') {
            window.open("http://www.open-roberta.org/erste-schritte.html");
        }
        return false;
    }, 'sub menu of menu "help"');

    // Submenu Login
    $('#head-navigation').onWrap('click', '#submenu-login > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'login') {
            $("#login-user").dialog("open");
        } else if (domId === 'logout') {
            logout();
        } else if (domId === 'new') {
            $("#register-user").dialog("open");
        } else if (domId === 'change') {
        } else if (domId === 'delete') {
            $("#delete-user").dialog("open");
        }
        return false;
    }, 'sub menu of menu "login"');

    // Close submenu on mouseleave
    $('#head-navigation').onWrap('mouseleave', function(event) {
        $('#head-navigation').menu("collapseAll", null, false);
    });

    $('#head-navigation #logo').onWrap('click', function() {
        window.open('http://open-roberta.org');
    }, 'logo was clicked');

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
        if (userState.id) {
            loadUPFromServer(true);
        } else {
            loadFromServer(true);
        }
    }, 'add the blocks');

    $('#saveProgram').onWrap('click', function() {
        saveToServer();
    }, 'save program');

    $('#saveConfiguration').onWrap('click', function() {
        saveConfigurationToServer();
    }, 'save configuration');

    $('#setToken').onWrap('click', function() {
        var $token = $('#tokenValue');
        setToken($token.val());
    }, 'set token');

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
        loadFromListing();
    }, 'load blocks from program list');

    // load configuration
    $('#loadConfigurationFromListing').onWrap('click', function() {
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

    $('.backButton').onWrap('click', function() {
        console.log(bricklyActive);
        if (bricklyActive) {
            switchToBrickly();
        } else {
            $('#tabBlockly').click();
        }
    });
}

/**
 * Initialize logging
 */
function initLogging() {
    $('#clearLog').onWrap('click', function() {$('#log li').remove();}, 'clear LOG list');
}

/**
 * Set robot state
 * 
 * @param {response}
 *            response of server call
 */
function setRobotState(response) {
    var robotState = response.robot_state;
    if (robotState === 'robot.waiting') {
        robotState += ' for ' + response.robot_waiting;        
    }
    userState.robotState = robotState;
    displayState();
}

/**
 * Translate the web page
 */
function translate(jsdata) {   
    $("[lkey]").each (function (index)
    {
        var key = $(this).attr('lkey');
        var value = jsdata[key];
        //console.log('key/value = ' + key + '/' + value);
        if (key === 'MENU.LOGIN') {
            $('#login-user').dialog('option', 'title', value);
            $(this).html(value);            
        } else if (key  === 'POPUP.DO_LOGIN') {
            $('#login-user #doLogin').attr('value', value);
        } else if (key  === 'POPUP.REGISTER_USER') {
            $('#register-user').dialog('option', 'title', value);
            $('#register-user #saveUser').attr('value', value);
        } else if (key  === 'POPUP.DELETE_USER') {
            $('#delete-user').dialog('option', 'title', value);
            $('#delete-user #deleteUser').attr('value', value);
        } else if (key  === 'POPUP.ATTACH_PROGRAM') {
            $('#attach-program').dialog('option', 'title', value);
            $('#attach-program #attachProgram').attr('value', value);
        } else if (key  === 'POPUP.SAVE_PROGRAM') {
            $('#save-program').dialog('option', 'title', value);
            $('#save-program #saveProgram').attr('value', value);
        } else if (key  === 'POPUP.SAVE_CONFIGURATION') {
            $('#save-configuration').dialog('option', 'title', value);
            $('#save-configuration #saveConfiguration').attr('value', value);
        } else if (key  === 'POPUP.SET_TOKEN') {
            $('#set-token').dialog('option', 'title', value);
            $('#set-token #setToken').attr('value', value);
        } else if (key  === 'POPUP.ATTENTION') {
            $('#show-message').dialog('option', 'title', value);
        } else if (key  === 'BUTTON.BACK') {
            $('.backButton').attr('value', value);
        } else if (key  === 'BUTTON.LOAD') {
            $('.buttonLoad').attr('value', value);
        } else if (key  === 'BUTTON.DO_DELETE') {
            $('.buttonDelete').attr('value', value);
        } else if (key  === 'BUTTON.EMPTY_LIST') {
            $('#clearLog').attr('value', value);
        } else if (key  === 'HINT.LANGUAGE_GERMAN') {
            $('#setLangDe').prop('title', value);
        } else if (key  === 'HINT.LANGUAGE_ENGLISH') {
            $('#setLangEn').prop('title', value);
        } else if (key  === 'HINT.ROBOT_STATE') {
            $('#displayRobotState').prop('title', value);
        } else if (key  === 'HINT.TOOLBOX') {
            $('#displayToolbox').prop('title', value);
        } else if (key  === 'HINT.USER') {
            $('#displayLogin').prop('title', value);
        } else if (key  === 'HINT.PROGRAM') {
            $('#displayProgram').prop('title', value);
        } else if (key  === 'HINT.CONFIGURATION') {
            $('#displayConfiguration').prop('title', value);
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
 */
function switchLanguage(langCode) {
    if (userState.language != langCode) {
        var langs = ['De', 'En'];
        for (i in langs) {
            $("." + langs[i] + "").css('display','none');
            $("#setLang" + langs[i] + "").removeClass('bigFlag');
            $("#setLang" + langs[i] + "").addClass('smallFlag');
        }
        if (langs.indexOf(langCode) < 0) {
            langCode = "De";
        }
        $("." + langCode + "").css('display','inline');
        $("#setLang" + langCode + "").removeClass('smallFlag');
        $("#setLang" + langCode + "").addClass('bigFlag');
        userState.language = langCode;
        $.getJSON('blockly/msg/json/' + langCode.toLowerCase() + '.json', translate);
        $.getScript('blockly/msg/js/' + langCode.toLowerCase() + '.js');
        COMM.json("/blocks", {
            "cmd" : "loadT",
            "name" : userState.toolbox
        }, injectBlockly);
    }
}

/**
 * Initialize language switching
 */
function initializeLanguages() {
    $('#setLangDe').onWrap('click', function() {
        switchLanguage('De');
    }, 'switch language to "De"');
    
    $('#setLangEn').onWrap('click', function() {
        switchLanguage('En');
    }, 'switch language to "En"');
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
    $('#programNameSave').val('');
    $('#configurationNameSave').val('');
    initializeLanguages();
    switchLanguage('De');
};

$(document).ready(WRAP.fn3(init, 'page init'));
