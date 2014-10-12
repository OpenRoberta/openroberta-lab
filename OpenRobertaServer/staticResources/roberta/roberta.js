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
        LOG.info('delete ' + userState.program + ' signed in: ' + userState.id);
        COMM.json("/blocks", {
            "cmd" : "saveP",
            "name" : userState.program,
            "program" : xml_text
        }, response);
    } else {
        alert('There is no name for your program available\n\n please save one with a name or load one');
    }
}

function runOnBrick() {
    if (userState.program) {
        LOG.info('run ' + userState.program + ' signed in: ' + userState.id);
        COMM.json("/blocks", {
            "cmd" : "runP",
            "name" : userState.program,
        }, response);
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

function selectionFn() {
    $('#programNameTable .selected').removeClass('selected');
    $(this).toggleClass('selected');
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
    $('#programNameTable tbody').on('click', 'tr', selectionFn);
}

function startProgram() {
    if (userState.id) {
        // alert("No user id, you need to sign in");
        saveUPToServer();
        runOnBrick();
    } else {
        // alert("We use the user program table");
        saveToServer();
        runOnBrick();
    }
}

function checkProgram() {
    // TODO
    alert("Your program will be checked soon ;-)");
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
 * Initialize the navigation bar in the head of the page
 */
function initHeadNavigation() {

    $('#head-navigation').menu({
        position : {
            my : "left-30 top+60"
        },
        select : function(event, ui) {
            var id = ui.item.children().attr('id');
            if (id === 'head-navigation-blockly') {
                $('#tabBlockly').click();
            } else if (id === 'head-navigation-readme') {
                $('#tabReadme').click();
            }
        }
    });

    $('#head-navigation').on('click', '#submenu-program > li:not(.ui-state-disabled)', function() {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        if (this.id === 'run') {
            startProgram();
        } else if (this.id === 'check') {
            checkProgram();
        } else if (this.id === 'new') {
            initProgramEnvironment();
            $("#new-program").dialog("open");
        } else if (this.id === 'open') {
            $('#tabListing').click();
        } else if (this.id === 'save') {
            if (userState.id) {
                saveUPToServer();
            } else {
                saveToServer();
            }
        } else if (this.id === 'saveAs') {
            $("#save-program").dialog("open");
        } else if (this.id === 'attach') {
            $("#add-program").dialog("open");
        } else if (this.id === 'divide') {
        } else if (this.id === 'delete') {
            $('#tabListing').click();
        } else if (this.id === 'properties') {
        } else if (this.id === 'toolboxBeginner') {
            loadToolbox('beginner');
            $('#toolboxBeginner').addClass('ui-state-disabled');
            $('#toolboxExpert').removeClass('ui-state-disabled');
        } else if (this.id === 'toolboxExpert') {
            loadToolbox('expert');
            $('#toolboxExpert').addClass('ui-state-disabled');
            $('#toolboxBeginner').removeClass('ui-state-disabled');
        }
        return false;
    });

    $('#head-navigation').on('click', '#submenu-settings > li:not(.ui-state-disabled)', function() {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        if (this.id === 'roboter') {
            switchToBrickly();
        } else if (this.id === 'close') {
            switchToBlockly();
        }
        return false;
    });

    $('#head-navigation').on('click', '#submenu-connection > li:not(.ui-state-disabled)', function() {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        if (this.id === 'wifi') {
        } else if (this.id === 'usb') {
        } else if (this.id === 'bluetooth') {
        } else if (this.id === 'disconnect') {
        }
        return false;
    });

    $('#head-navigation').on('click', '#submenu-developertools > li:not(.ui-state-disabled)', function() {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        if (this.id === 'logging') {
            $('#tabLogging').click();
        } else if (this.id === 'simtest') {
            $('#tabSimtest').click();
        }
        return false;
    });

    $('#head-navigation').on('click', '#submenu-login > li:not(.ui-state-disabled)', function() {
        $(".ui-dialog-content").dialog("close"); // close all opened popups
        if (this.id === 'login') {
            $("#login-user").dialog("open");
        } else if (this.id === 'logout') {
        } else if (this.id === 'new') {
            $("#register-user").dialog("open");
        } else if (this.id === 'change') {
        } else if (this.id === 'delete') {
            $("#delete-user").dialog("open");
        }
        return false;
    });

    setHeadNavigationMenuState('logout');
}

function init() {

    initHeadNavigation();
    initProgramNameTable();

    $('#tabs').tabs({
        heightStyle : 'content',
        active : 0,
        beforeActivate : beforeActivateTab
    });

    // Register and signing in changes
    $('#saveUser').onWrap('click', saveUserToServer, 'save the user data');
    $('#deleteUser').onWrap('click', deleteUserOnServer, 'delete user data');
    $('#signIn').onWrap('click', signIn, 'signing in ');

    $('#addProgram').onWrap('click', function() {
        if (userState.id) {
            loadUPFromServer(true);
        } else {
            loadFromServer(true);
        }
    }, 'add the blocks');

    $('#saveProgram').onWrap('click', function() {
        if (userState.id) {
            saveUPToServer();
        } else {
            saveToServer();
        }
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
