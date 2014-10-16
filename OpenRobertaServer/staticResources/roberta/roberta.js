var userState = {};
initializeUserState();

/**
 * Initialize user-state-object
 */
function initializeUserState() {
    userState.id = -1;
    userState.name = 'none';
    userState.role = 'none';
    userState.program = 'none';
    userState.programSaved = false;
    userState.brickSaved = false;
    userState.robot = 'none';
    userState.brickConnection = 'none';
    userState.toolbox = 'none';
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
        displayMessage("Wrong password");
    } else if ($role.val() == null) {
        displayMessage("Select your role");
    } else {
        var roleGerman = $role.val();
        var role = "STUDENT";
        if (roleGerman === "Lehrer") {
            role = "TEACHER";
        }
        COMM.json("/blocks", {
            "cmd" : "createUser",
            "accountName" : $userAccountName.val(),
            "userName" : $userName.val(),
            "userEmail" : $userEmail.val(),
            "password" : $pass1.val(),
            "role" : role
        }, function(result) {
            if (result.rc === "ok")
                displayMessage("User created");
            else
                displayMessage("User already exists, choose a different account name, cause: " + response.cause);
        });
    }
}

/**
 * Delete user
 */
function deleteUserOnServer() {
    var $userAccountName = $("#accountNameD");
    userState.id = null;
    COMM.json("/blocks", {
        "cmd" : "deleteUser",
        "accountName" : $userAccountName.val()
    }, function(result) {
        displayMessage("server return: " + result.rc);
    });
}

/**
 * Login user
 */
function login() {
    var $userAccountName = $("#accountNameS");
    var $pass1 = $('#pass1S');

    COMM.json("/blocks", {
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
        } else {
            displayMessage("Login failed, cause: " + response.cause);
        }
    });
}

/**
 * Logout user
 */
function logout() {
    COMM.json("/blocks", {
        "cmd" : "logout"
    }, function(response) {
        if (response.rc === "ok") {
            initializeUserState();
            displayStatus();
            setHeadNavigationMenuState('logout');
        } else {
            displayMessage("Logout failed, cause: " + response.cause);
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

function setProgram(name) {
    if (name) {
        userState.program = name;
        $("#setProgram").text(name);
    } else {
        $("#setProgram").text('Programm');
    } // bad because of language dependent
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

function saveToServer() {
    if ($('#programNameSave')) {
        var $name = $('#programNameSave');
        setProgram($name.val());
    }
    if (userState.program) {
        var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
        var xml_text = Blockly.Xml.domToText(xml);
        userState.programSaved = true;
        LOG.info('save ' + userState.program + ' login: ' + userState.id);
        return COMM.json("/blocks", {
            "cmd" : "saveP",
            "name" : userState.program,
            "program" : xml_text
        }, response);
    } else {
        displayMessage("There is no name for your program available\n\n please save one with a name or load one, cause: " + response.cause);
    }
}

function runOnBrick() {
    LOG.info('run ' + userState.program + ' signed in: ' + userState.id);
    return COMM.json("/blocks", {
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

function loadFromServer(load) {
    var $name = $('#programNameLoad');
    COMM.json("/blocks", {
        "cmd" : "loadP",
        "name" : $name.val()
    }, function(result) {
        showProgram(result, load, $name.val());
    });
    LOG.info('load ' + $name.val() + ' signed in: ' + userState.id);
}

function deleteOnServer() {
    var $name = $('#programNameDelete');
    userState.programSaved = false;
    if (usertState.program === $name.val) {
        setProgram();
    }
    LOG.info('del ' + $name.val() + ' signed in: ' + userState.id);
    COMM.json("/blocks", {
        "cmd" : "deletePN",
        "name" : $name.val()
    }, function(result) {
    });
}

function loadFromListing() {
    var $programRow = $('#programNameTable .selected');
    if ($programRow.length <= 0) {
        return;
    }
    var programName = $programRow[0].children[0].textContent;
    LOG.info('loadFromList ' + programName + ' signed in: ' + userState.id);
    COMM.json("/blocks", {
        "cmd" : "loadP",
        "name" : programName
    }, function(result) {
        $("#tabs").tabs("option", "active", 0);
        $('#name').val(programName);
        userState.programSaved = true;
        showProgram(result, true, programName);
    });
}

function showToolbox(result) {
    response(result);
    if (result.rc === 'ok') {
        $('#head-navigation #toolbox').text(userState.toolbox);
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

function selectionFn(event) {
    $('#programNameTable .selected').removeClass('selected');
    $(event.target.parentNode).toggleClass('selected');
}

function beforeActivateTab(event, ui) {
    $('#tabs').tabs("refresh");
    if (ui.newPanel.selector !== '#listing') {
        return;
    }
    COMM.json("/blocks", {
        "cmd" : "loadPN",
    }, showPrograms);
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
        $('#loadFromListing').click();
    });

}

function startProgram() {
    var saveFuture = saveToServer();
    saveFuture.then(runOnBrick);
}

function checkProgram() {
    // TODO
    displayMessage("Your program will be checked soon ;-)");
}

function switchToBlockly() {
    document.getElementById('tabs').style.display = 'inline';
    document.getElementById('bricklyFrame').style.height = '0';
    document.getElementById('bricklyFrame').style.width = '0';
}

function switchToBrickly() {
    document.getElementById('tabs').style.display = 'none';
    document.getElementById('bricklyFrame').style.height = '100%';
    document.getElementById('bricklyFrame').style.width = '100%';
}

/**
 * Initialize the navigation bar in the head of the page
 */
function displayStatus() {
    $('#setName').text(userState.name);
    $('#head-navigation #role').text(userState.role);
    $('#head-navigation #robot').text(userState.robot);
    $('#head-navigation #brickConnection').text(userState.brickConnection);
}

/**
 * Inject Brickly with initial toolbox
 * 
 * @param {state}
 *            state to be set
 * 
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
 * @param {message} Messabe to be displayed
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
            my : "left-30 top+60"
        }
    });

    $('#head-navigation').onWrap('click', '#head-navigation-readme:not(.ui-state-disabled)', function() {
        $('#tabReadme').click();
    });

    $('#head-navigation').onWrap('click', '#head-navigation-blockly:not(.ui-state-disabled)', function() {
        $('#tabBlockly').click();
    });

    function submenuProgram(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'run') {
            startProgram();
        } else if (domId === 'check') {
            checkProgram();
        } else if (domId === 'new') {
            initProgramEnvironment();
            $("#new-program").dialog("open");
        } else if (domId === 'open') {
            $('#tabListing').click();
        } else if (domId === 'save') {
            saveToServer(response);
        } else if (domId === 'saveAs') {
            $("#save-program").dialog("open");
        } else if (domId === 'attach') {
            $("#add-program").dialog("open");
        } else if (domId === 'divide') {
        } else if (domId === 'delete') {
            $('#tabListing').click();
        } else if (domId === 'properties') {
        } else if (domId === 'toolboxBeginner') {
            loadToolbox('beginner');
            $('#toolboxBeginner').addClass('ui-state-disabled');
            $('#toolboxExpert').removeClass('ui-state-disabled');
        } else if (domId === 'toolboxExpert') {
            loadToolbox('expert');
            $('#toolboxExpert').addClass('ui-state-disabled');
            $('#toolboxBeginner').removeClass('ui-state-disabled');
        }
        return false;
    }

    $('#head-navigation').onWrap('click', '#submenu-program > li:not(.ui-state-disabled)', submenuProgram, 'sub menu of menu "program"');

    $('#head-navigation').onWrap('click', '#submenu-settings > li:not(.ui-state-disabled)', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'roboter') {
            switchToBrickly();
        } else if (domId === 'close') {
            switchToBlockly();
        }
        return false;
    });

    $('#head-navigation').onWrap('click', '#submenu-connection > li:not(.ui-state-disabled)', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'wifi') {
        } else if (domId === 'usb') {
        } else if (domId === 'bluetooth') {
        } else if (domId === 'disconnect') {
        }
        return false;
    });

    $('#head-navigation').onWrap('click', '#submenu-developertools > li:not(.ui-state-disabled)', function(event) {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        var domId = event.target.id;
        if (domId === 'logging') {
            $('#tabLogging').click();
        } else if (domId === 'simtest') {
            $('#tabSimtest').click();
        }
        return false;
    });

    $('#head-navigation').onWrap('click', '#submenu-login > li:not(.ui-state-disabled)', function(event) {
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
    });

    setHeadNavigationMenuState('logout');
}

/**
 * Initialize the popups
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

    $(".jquerypopup .submit").on('click', function() {
        $(this).closest(".jquerypopup").dialog('close');
    });

    // overwritten Styles for unique Pop-Up
    $("#delete-user").dialog({
        width : 300
    });

    // define general class for Pop-Up
    $(".jquerypopup").dialog("option", "dialogClass", "jquerypopup");

    // Close all previous Pop-Ups.
    $(".popup-opener").click(function() {
        $(".ui-dialog-content").dialog("close");
    });

    // Open Pop-Up "Login User"
    $("#open-login-user").click(function() {
        $("#login-user").dialog("open");
    });

    // Open Pop-Up "Register User"
    $("#open-register-user").click(function() {
        $("#register-user").dialog("open");
    });

    // Open Pop-Up "Delete User"
    $("#open-delete-user").click(function() {
        $("#delete-user").dialog("open");
    });

    // Open Pop-Up "Programm"
    $("#open-program").click(function() {
        $("#program").dialog("open");
    });
}

/**
 * Initializations
 */
function init() {

    initPopups();
    initHeadNavigation();
    initProgramNameTable();

    $('#tabs').tabs({
        heightStyle : 'content',
        active : 0,
        beforeActivate : beforeActivateTab
    });

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

    $('#newProgram').onWrap('click', function() {
        var $name = $('#programNameNew');
        setProgram($name.val());
    }, 'new program');
    // =============================================================================

    $('#toggle').onWrap('click', LOG.toggleVisibility, 'toggle LOG visibility');
    $('#loadFromListing').onWrap('click', function() {
        loadFromListing();
    }, 'load blocks from program list');
    COMM.json("/blocks", {
        "cmd" : "loadT",
        "name" : "beginner"
    }, injectBlockly);
};

$(document).ready(WRAP.fn3(init, 'page init'));
