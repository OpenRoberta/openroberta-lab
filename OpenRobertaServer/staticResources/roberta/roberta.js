var userState = {};

/**
 * Initialize user-state-object
 */
function initUserState() {
    userState.id = -1;
    userState.name = '';
    userState.role = '';
    userState.program = 'meinProgramm';
    userState.programSaved = false;
    userState.brickSaved = false;
    userState.robot = '';
    userState.brickConnection = '';
    userState.toolbox = '';
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
        displayMessage("Du hast beim eingeben der beiden Passworte etwas falsch gemacht.");
    } else if ($role.val() == null) {
        displayMessage("Du musst angeben, ob Du Schüler oder Lehrer bist.");
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
                displayMessage("Du hast erfolgreich einen neuen Nutzer angelegt.");
                $(".ui-dialog-content").dialog("close"); // close all opened popups
            } else {
                displayMessage("Dieser Nutzer existiert bereits. Du musst einen anderen Account-Namen wählen.");
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
            displayMessage("Der Nutzer wurde gelöscht.");
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage("Der Nutzer konnte nicht gelöscht werden.");
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
            displayStatus();
            setHeadNavigationMenuState('login');
            $("#tutorials").fadeOut(700);
            $(".ui-dialog-content").dialog("close"); // close all opened popups
        } else {
            displayMessage("Du hast beim einloggen einen Fehler gemacht.");
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
            displayStatus();
            $('#programNameSave').val('');
            setHeadNavigationMenuState('logout');
        } else {
            displayMessage("Beim ausloggen ist ein Fehler passiert.");
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
        Blockly.inject(document.getElementById('blocklyDiv'), {
            path : '/blockly/',
            toolbox : toolbox.data,
            trashcan : true,
            save : true,
            check : true,
            start : true
        });
        initProgramEnvironment();
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
        displayStatus();
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
        COMM.json("/blocks", {
            "cmd" : "setToken",
            "token" : token
        }, function(response) {
            if (response.rc === "ok") {
                userState.token = token;
                $(".ui-dialog-content").dialog("close"); // close all opened popups
            } else {
                displayMessage("Das Einstellen der Roboter-Kennung hat nicht funktioniert.");
            }
        });
    } else {
        displayMessage("Du musst die Roboter-Kennung eingeben.");
    }
}

function incrCounter(e) {
    var $counter = $('#counter');
    var counter = Number($counter.text());
    $counter.text('' + (counter + 1));
}

function response(result) {
    LOG.info('result from server: ' + JSON.stringify(result));
    incrCounter();
};

/**
 * Save program to server
 */
function saveToServer() { 
    if ($('#programNameSave')) {
        var $name = $('#programNameSave');
        setProgram($name.val());
        if (userState.name) {   // Is someone logged in?
            if (!$name.val() || $name.val() === "meinProgramm") {
                $('#head-navigation #submenu-program #save').addClass('login');            
                $('#head-navigation #submenu-program #save').addClass('ui-state-disabled');
                displayMessage("Du musst einen anderen Programmnamen nehmen.");
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
        LOG.info('save ' + userState.program + ' login: ' + userState.id);
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        return COMM.json("/program", {
            "cmd" : "saveP",
            "name" : userState.program,
            "program" : xml_text
        }, response);
    } else {
        displayMessage("Du musst einen Programmnamen eingeben.");
    }
}

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
        LOG.info('show ' + userState.program + ' signed in: ' + userState.id);
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
            $("#tabs").tabs("option", "active", 0);
            $('#programNameSave').val('');
        });
    }
}

function showToolbox(result) {
    response(result);
    if (result.rc === 'ok') {
        $('#head-navigation #displayToolbox').text(userState.toolbox);
        Blockly.updateToolbox(result.data);
    }
}

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
 * @param {result} result object of server call
 */
function showPrograms(result) {
    response(result);
    if (result.rc === 'ok') {
        var $table = $('#programNameTable').dataTable();
        $table.fnClearTable();
        if (result.programNames.length > 0) {
            $table.fnAddData(result.programNames);
        }
    }
}

/**
 * Display configurations in a table
 * 
 * @param {result} result object of server call
 */
function showConfigurations(result) {
    response(result);
    if (result.rc === 'ok') {
        var $table = $('#configurationNameTable').dataTable();
        $table.fnClearTable();
        if (result.confNames.length > 0) {
            $table.fnAddData(result.confNames);
        }
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

function initProgramNameTable() {
    var columns = [ {
        "sTitle" : "Name des Programms",
        "sClass" : "programs"
    }, {
        "sTitle" : "User",
        "sClass" : "programs"
    }, {
        "sTitle" : "Blöcke",
        "sClass" : "programs"
    }, {
        "sTitle" : "Icon",
        "sClass" : "programs"
    }, {
        "sTitle" : "Created",
        "sClass" : "programs"
    }, {
        "sTitle" : "Last Update",
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
            "sLengthMenu" : 'Show <select>' + '<option value="10">10</option><option value="20">20</option><option value="25">25</option>'
                    + '<option value="30">30</option><option value="100">100</option><option value="-1">All</option>' + '</select> programs/revisions'
        },
        "fnDrawCallback" : function() {
            var counter = +$('#redrawCounter').text();
            $('#redrawCounter').text(counter + 1);
        }
    });
    $('#programNameTable tbody').onWrap('click', 'tr', selectionFn);
    $('#programNameTable tbody').onWrap('dblclick', 'tr', function(event) {
        selectionFn(event);
        if ($('#loadFromListing').css('display') === 'block') {
            $('#loadFromListing').click();
        } else if ($('#deleteFromListing').css('display') === 'block') {
            $('#deleteFromListing').click();
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
    displayMessage("Dein Programm kann zur Zeit noch nicht geprüft werden.");
}

function switchToBlockly() {
    $('#tabs').css('display','inline');
    $('#bricklyFrame').css('display','none');
}

function switchToBrickly() {
    $('#tabs').css('display','none');
    $('#bricklyFrame').css('display','inline');
}

/**
 * Display status information in the navigation bar
 */
function displayStatus() {
    $('#head-navigation #displayLogin').text(userState.name);
    $('#head-navigation #displayProgram').text(userState.program);
    $('#head-navigation #displayBrickConnection').text(userState.brickConnection);
    $('#head-navigation #displayToolbox').text(userState.toolbox);
}

/**
 * Inject Brickly with initial toolbox
 * 
 * @param {state} State to be set
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
function displayMessage(message) {
    $("#show-message > #message").text(message);
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

    // Submenu Program
    $('#head-navigation').onWrap('click', '#submenu-program > li:not(.ui-state-disabled)', function(event) {
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
            $('#loadFromListing').css('display', 'block');
            $('#deleteFromListing').css('display', 'none');
            $('#tabListing').click();
        } else if (domId === 'save') {
            saveToServer(response);
        } else if (domId === 'saveAs') {
            $("#save-program").dialog("open");
        } else if (domId === 'attach') {
            $("#add-program").dialog("open");
        } else if (domId === 'divide') {
        } else if (domId === 'delete') {
            $('#deleteFromListing').css('display', 'block');
            $('#loadFromListing').css('display', 'none');
            $('#tabListing').click();
        } else if (domId === 'properties') {
        }
        return false;
    }, 'sub menu of menu "program"');
    
    // Submenu Nepo
    $('#head-navigation').onWrap('click', '#submenu-nepo > li:not(.ui-state-disabled)', function(event) {
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
    $('#head-navigation').onWrap('click', '#submenu-configuration > li:not(.ui-state-disabled)', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        switchToBlockly();
        var domId = event.target.id;
        if (domId === 'check') {
        } else if (domId === 'standard') {
            switchToBrickly();
        } else if (domId === 'new') {
        } else if (domId === 'open') {
            $('#tabConfListing').click();
        } else if (domId === 'save') {
        } else if (domId === 'saveAs') {
        } else if (domId === 'delete') {
        } else if (domId === 'properties') {
        }
        return false;
    }, 'sub menu of menu "roboter" ("configuration")');
    
    // Submenu Connection
    $('#head-navigation').onWrap('click', '#submenu-connection > li:not(.ui-state-disabled)', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        switchToBlockly();
        var domId = event.target.id;
        if (domId === 'connect') {
            $("#set-token").dialog("open");
        }
        return false;
    }, 'sub menu of menu "connection"');

    // Submenu Developertools
    $('#head-navigation').onWrap('click', '#submenu-developertools > li:not(.ui-state-disabled)', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        switchToBlockly();
        var domId = event.target.id;
        if (domId === 'logging') {
            $('#tabLogging').click();
        } else if (domId === 'simtest') {
            $('#tabSimtest').click();
        }
        return false;
    }, 'sub menu of menu "developertools"');

    // Submenu Readme
    $('#head-navigation').onWrap('click', '#submenu-readme > li:not(.ui-state-disabled)', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        switchToBlockly();
        var domId = event.target.id;
        if (domId === 'readme') {
            $('#tabReadme').click();
        }
        return false;
    }, 'sub menu of menu "readme"');

    // Submenu Login
    $('#head-navigation').onWrap('click', '#submenu-login > li:not(.ui-state-disabled)', function(event) {
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
    $('#head-navigation').on('mouseleave', function(event) {
        $('#head-navigation').menu("collapseAll", null, false);
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

    // Close popup on submit
//    $(".jquerypopup .submit").on('click', function() {
//        $(this).closest(".jquerypopup").dialog('close');
//    });

    // define general class for Pop-Up
    $(".jquerypopup").dialog("option", "dialogClass", "jquerypopup");

    $('#saveUser').onWrap('click', saveUserToServer, 'save the user data');
    $('#deleteUser').onWrap('click', deleteUserOnServer, 'delete user data');
    $('#doLogin').onWrap('click', login, 'login ');

    $('#addProgram').onWrap('click', function() {
        if (userState.id) {
            loadUPFromServer(true);
        } else {
            loadFromServer(true);
        }
    }, 'add the blocks');

    $('#saveProgram').onWrap('click', function() {
        saveToServer();
    }, 'save program');

    $('#setToken').onWrap('click', function() {
        var $token = $('#tokenValue');
        setToken($token.val());
    }, 'set token');

    // Any submit button in a popup can be triggered by the Return-Button
    $(".jquerypopup").keydown(function(event) {
        if (event.keyCode == 13) {
            $(this).find("input.submit").click();
            event.stopPropagation();
        }
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

    $('#loadFromListing').onWrap('click', function() {
        loadFromListing();
    }, 'load blocks from program list');
    COMM.json("/blocks", {
        "cmd" : "loadT",
        "name" : "beginner"
    }, injectBlockly);

    $('#deleteFromListing').onWrap('click', function() {
        deleteFromListing();
    }, 'delete blocks from program list');
    COMM.json("/blocks", {
        "cmd" : "deleteT",
        "name" : "beginner"
    });

    $('.backToBlockly').onWrap('click', function() {
        $('#tabBlockly').click();
    });
}

/**
 * Initializations
 */
function init() {

    initUserState();
    initTabs();
    initPopups();
    initHeadNavigation();
    initProgramNameTable();   
    displayStatus();
    $('#programNameSave').val('');

    // =============================================================================

    $('#toggle').onWrap('click', LOG.toggleVisibility, 'toggle LOG visibility');
};

$(document).ready(WRAP.fn3(init, 'page init'));
