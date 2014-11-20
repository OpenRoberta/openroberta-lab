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
        displayMessage("message1");
    } else if ($role.val() == null) {
        displayMessage("message2");
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
                displayMessage("message3");
                $(".ui-dialog-content").dialog("close"); // close all opened popups
            } else {
                displayMessage("message4");
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
            displayMessage("message5");
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage("message6");
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
            displayMessage("message7");
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
            initProgramEnvironment();
            setRobotState(response);
        } else {
            displayMessage("message8");
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
        displayMessage("message10");
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
};

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
};

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
                displayMessage("message11");
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
        displayMessage("message12");
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
                displayMessage("message11");
                return;
            }
            $('#head-navigation #submenu-configuration #save').removeClass('login');
            $('#head-navigation #submenu-configuration #save').removeClass('ui-state-disabled');
        }
    }
    if (userState.configuration) {
        var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
        var xml_text = Blockly.Xml.domToText(xml);
        userState.configurationSaved = true;
        LOG.info('save configuration ' + userState.configuration + ' login: ' + userState.id);
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        return COMM.json("/conf", {
            "cmd" : "saveC",
            "name" : userState.configuration,
            "configuration" : xml_text
        }, responseAndRefreshList);
    } else {
        displayMessage("message12");
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
        "sTitle" : "Name des Programms",
        "sClass" : "programs"
    }, {
        "sTitle" : "Erzeugt von",
        "sClass" : "programs"
    }, {
        "sTitle" : "Blöcke",
        "sClass" : "programs"
    }, {
        "sTitle" : "Icon",
        "sClass" : "programs"
    }, {
        "sTitle" : "Erzeugt am",
        "sClass" : "programs"
    }, {
        "sTitle" : "Letzte Aktualisierung",
        "sClass" : "programs"
    }, ];
    var $programs = $('#programNameTable');
    $programs.dataTable({
        "sDom" : '<lip>t<r>',
        "aaData" : [],
        "aoColumns" : columns,
        "oLanguage" : {
            "sSearch" : "Search all columns:"
        },
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bPaginate" : true,
        "iDisplayLength" : 20,
        "oLanguage" : {
            "sLengthMenu" : 'Zeige <select>' + '<option value="10">10</option><option value="20">20</option><option value="25">25</option>'
                    + '<option value="30">30</option><option value="100">100</option><option value="-1">All</option>' + '</select> Programme/Revisionen'
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
        "sTitle" : "Name der Konfiguration",
        "sClass" : "configurations"
    }, {
        "sTitle" : "Erzeugt von",
        "sClass" : "configurations"
    }, {
        "sTitle" : "Icon",
        "sClass" : "configurations"
    }, {
        "sTitle" : "Erzeugt am",
        "sClass" : "configurations"
    }, {
        "sTitle" : "Letzte Aktualisierung",
        "sClass" : "configurations"
    }, ];
    var $configurations = $('#configurationNameTable');
    $configurations.dataTable({
        "sDom" : '<lip>t<r>',
        "aaData" : [],
        "aoColumns" : columns,
        "oLanguage" : {
            "sSearch" : "Search all columns:"
        },
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bPaginate" : true,
        "iDisplayLength" : 20,
        "oLanguage" : {
            "sLengthMenu" : 'Zeige <select>' + '<option value="10">10</option><option value="20">20</option><option value="25">25</option>'
                    + '<option value="30">30</option><option value="100">100</option><option value="-1">All</option>' + '</select> Konfigurationen'
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
    displayMessage("message13");
}

/**
 * Check configuration
 */
function checkConfiguration() {
    // TODO
    displayMessage("message14");
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

/**
 * Inject Brickly with initial toolbox
 * 
 * @param {state}
 *            State to be set
 */
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
        switchToBlockly();
        var domId = event.target.id;
        if (domId === 'run') {
            startProgram();
        } else if (domId === 'check') {
            checkProgram();
        } else if (domId === 'new') {
            initProgramEnvironment();
            setProgram("meinProgramm");
        } else if (domId === 'open') {
            $('#loadFromListing').css('display', 'inline');
            $('#deleteFromListing').css('display', 'none');
            $('#tabListing').click();
        } else if (domId === 'save') {
            saveToServer(response);
        } else if (domId === 'saveAs') {
            $("#save-program").dialog("open");
        } else if (domId === 'attach') {
            $("#attach-program").dialog("open");
        } else if (domId === 'divide') {
        } else if (domId === 'delete') {
            $('#deleteFromListing').css('display', 'inline');
            $('#loadFromListing').css('display', 'none');
            $('#tabListing').click();
        } else if (domId === 'properties') {
        }
        return false;
    }, 'sub menu of menu "program"');

    // Submenu Nepo
    $('#head-navigation').onWrap('click', '#submenu-nepo > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        switchToBlockly();
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
        switchToBlockly();
        var domId = event.target.id;
        if (domId === 'check') {
            checkConfiguration();
        } else if (domId === 'standard') {
            switchToBrickly();
        } else if (domId === 'new') {
            setConfiguration("meineKonfiguration");
        } else if (domId === 'open') {
            $('#loadConfigurationFromListing').css('display', 'inline');
            $('#deleteConfigurationFromListing').css('display', 'none');
            $('#tabConfigurationListing').click();
        } else if (domId === 'save') {
            saveConfigurationToServer(response);
        } else if (domId === 'saveAs') {
            $("#save-configuration").dialog("open");
        } else if (domId === 'delete') {
            $('#deleteConfigurationFromListing').css('display', 'inline');
            $('#loadConfigurationFromListing').css('display', 'none');
            $('#tabConfigurationListing').click();
        } else if (domId === 'properties') {
        }
        return false;
    }, 'sub menu of menu "roboter" ("configuration")');

    // Submenu Connection
    $('#head-navigation').onWrap('click', '#submenu-connection > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        switchToBlockly();
        var domId = event.target.id;
        if (domId === 'connect') {
            $("#set-token").dialog("open");
        }
        return false;
    }, 'sub menu of menu "connection"');

    // Submenu Developertools
    $('#head-navigation').onWrap('click', '#submenu-developertools > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        switchToBlockly();
        var domId = event.target.id;
        if (domId === 'logging') {
            $('#tabLogging').click();
        } else if (domId === 'simulator') {
            $('#tabSimulator').click();
        }
        return false;
    }, 'sub menu of menu "developertools"');

    // Submenu Help
    $('#head-navigation').onWrap('click', '#submenu-help > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        switchToBlockly();
        var domId = event.target.id;
        if (domId === 'help') {
            window.open("http://www.open-roberta.org/erste-schritte.html");
        }
        return false;
    }, 'sub menu of menu "help"');

    // Submenu Login
    $('#head-navigation').onWrap('click', '#submenu-login > li:not(.ui-state-disabled) > span', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        switchToBlockly();
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

    $('.backToBlockly').onWrap('click', function() {
        $('#tabBlockly').click();
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
        if (key  === 'login') {
            $('#login-user').dialog('option', 'title', value);
        } else if (key  === 'doLogin') {
            $('#login-user #doLogin').attr('value', value);
        } else if (key  === 'registerUser') {
            $('#register-user').dialog('option', 'title', value);
            $('#register-user #saveUser').attr('value', value);
        } else if (key  === 'deleteUser') {
            $('#delete-user').dialog('option', 'title', value);
            $('#delete-user #deleteUser').attr('value', value);
        } else if (key  === 'attachProgram') {
            $('#attach-program').dialog('option', 'title', value);
            $('#attach-program #attachProgram').attr('value', value);
        } else if (key  === 'saveProgram') {
            $('#save-program').dialog('option', 'title', value);
            $('#save-program #saveProgram').attr('value', value);
        } else if (key  === 'saveConfiguration') {
            $('#save-configuration').dialog('option', 'title', value);
            $('#save-configuration #saveConfiguration').attr('value', value);
        } else if (key  === 'setToken') {
            $('#set-token').dialog('option', 'title', value);
            $('#set-token #saveConfiguration').attr('value', value);
        } else if (key  === 'attention') {
            $('#show-message').dialog('option', 'title', value);
        } else if (key  === 'back') {
            $('.backToBlockly').attr('value', value);
        } else if (key  === 'load') {
            $('.buttonLoad').attr('value', value);
        } else if (key  === 'doDelete') {
            $('.buttonDelete').attr('value', value);
        } else if (key  === 'emptyList') {
            $('#clearLog').attr('value', value);
        }
        //console.log('key/value = ' + key + '/' + value);
        $(this).html(value);
    });
};

/**
 * Switch to another language
 * 
 * @param {langCode}
 *            Code of language to switch to
 */
function switchLanguage(langCode) {
    var langs = ['De', 'En'];
    for (i in langs) {
        $("." + langs[i] + "").css('display','none');
    }
    if (langs.indexOf(langCode) < 0) {
        langCode = "De";
    }
    $("." + langCode + "").css('display','inline');
    userState.language = langCode;
    $.getJSON('css/lang/' + langCode.toLowerCase() + '.json', translate);
    $.getScript('blockly/msg/js/' + langCode.toLowerCase() + '.js');
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
    displayState();
};

$(document).ready(WRAP.fn3(init, 'page init'));
