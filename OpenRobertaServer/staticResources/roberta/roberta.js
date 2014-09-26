var userState = {
    id : false,
    name : null,
    program : null,
    programSaved : true,
    brickSaved : true,
    toolbox : 'beginner',
    token : '1A2B3C4D'
};

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
        // should this come from the server?
        var text = "<block_set xmlns='http: // www.w3.org/1999/xhtml'>" + "<instance x='25' y='50'>" + "<block type='robControls_start'>" + "</block>"
                + "</instance>" + "</block_set>";
        var xml = Blockly.Xml.textToDom(text);
        Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
    }
}

function setProgram(name) {
    if (name) {
        userState.program = name;
        $("#setProgram").text(name);
    } else {
        $("#setProgram").text('Programm')
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
    if ($('#programName')) {
        var $name = $('#programName');
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
    var $name = $('#programName');
    COMM.json("/blocks", {
        "cmd" : "loadP",
        "name" : $name.val()
    }, function(result) {
        showProgram(result, load, $name.val());
    });
    LOG.info('load ' + $name.val() + ' signed in: ' + userState.id);
}

function deleteOnServer() {
    var $name = $('#programName');
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
    var programName = $programRow[0].textContent;
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

var toolboxDone = false;

function showToolbox(result) {
    response(result);
    if (result.rc === 'ok') {
        userState.toolbox = result.name;
        Blockly.updateToolbox(result.data);
    }
}

function loadToolbox(toolbox) {
    COMM.json("/blocks", {
        "cmd" : "loadT",
        "name" : toolbox
    }, showToolbox);
}

function showPrograms(result) {
    response(result);
    if (result.rc === 'ok') {
        var programNames = result.programNames;
        var namesArrayArray = [];
        for (var i = 0; i < programNames.length; i++) {
            namesArrayArray.push([ programNames[i] ]);
        }
        var $table = $('#programNameTable').dataTable();
        $table.fnClearTable();
        $table.fnAddData(namesArrayArray);
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
        "sName" : "program name",
        "sClass" : "programs"
    } ];
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

function init() {
    $('#head-navigation').menu({
        position: { my: "left-30 top+60" },
        select : function(event, ui) {
            var id = ui.item.children().attr('id');
            if (id === 'head-navigation-blockly') {
                $('#tabBlockly').click();
            } else if (id === 'head-navigation-readme') {
                $('#tabReadme').click();
            }
        }
    });

    $("#head-navigation #submenu-program > li").click(function() {
        var id = this.id;
        if (id === 'start') {
            $("#program").dialog("open");
        } else if (id === 'check') {
            $("#program").dialog("open");
        } else if (id === 'new') {
            $("#program").dialog("open");
        } else if (id === 'open') {
            $('#tabListing').click();
        } else if (id === 'save') {
            $("#program").dialog("open");
        } else if (id === 'saveAs') {
            $("#program").dialog("open");
        } else if (id === 'attach') {
        } else if (id === 'divide') {
        } else if (id === 'properties') {
        }
    });

    $("#head-navigation #submenu-settings > li").click(function() {
        var id = this.id;
        if (id === 'roboter') {
            switchToBrickly();
        } else if (id === 'toolbox') {
        } else if (id === 'close') {
        }
    });

    $("#head-navigation #submenu-connection > li").click(function() {
        var id = this.id;
        if (id === 'wifi') {
        } else if (id === 'usb') {
        } else if (id === 'bluetooth') {
        } else if (id === 'disconnect') {
        }
    });

    $("#head-navigation #submenu-developertools > li").click(function() {
        var id = this.id;
        if (id === 'logging') {
            $('#tabLogging').click();
        } else if (id === 'simtest') {
            $('#tabSimtest').click();
        }
    });

    $("#head-navigation #submenu-login > li").click(function() {
        var id = this.id;
        if (id === 'login') {
        } else if (id === 'logout') {
        } else if (id === 'new') {
        } else if (id === 'change') {
        }
    });

    $('#tabs').tabs({
        heightStyle : 'content',
        active : 0,
        beforeActivate : beforeActivateTab
    });
    initProgramNameTable();

    // Register and signing in changes
    $('#saveUser').onWrap('click', saveUserToServer, 'save the user data');
    $('#deleteUser').onWrap('click', deleteUserOnServer, 'delete user data');
    $('#signIn').onWrap('click', signIn, 'signing in ');
    // $('#loadFromListing').onWrap('click', myLoadFromListing,
    // 'load blocks from program list');

    $('#load').onWrap('click', function() {
        if (userState.id) {
            loadUPFromServer(true);
        } else {
            loadFromServer(true);
        }
    }, 'load the blocks');

    $('#add').onWrap('click', function() {
        if (userState.id) {
            loadUPFromServer(true);
        } else {
            loadFromServer(true);
        }
    }, 'add the blocks');
    $('#save').onWrap('click', function() {
        if (userState.id) {
            saveUPToServer();
        } else {
            saveToServer();
        }
    }, 'save program');
    $('#del').onWrap('click', function() {
        if (userState.id) {
            deleteUPOnServer();
        } else {
            deleteOnServer();
        }
    }, 'del program');

    $('#run').onWrap('click', function() {
        startProgram();
    }, 'save+run the program');
    // =============================================================================

    $('#toggle').onWrap('click', LOG.toggleVisibility, 'toggle LOG visibility');
    $('#loadFromListing').onWrap('click', function() {
        loadFromListing();
    }, 'load blocks from program list');

    $('#toolbox1').onWrap('click', function() {
        loadToolbox('beginner');
    }, 'load toolbox 1');
    $('#toolbox2').onWrap('click', function() {
        loadToolbox('expert');
    }, 'load toolbox 2');
    COMM.json("/blocks", {
        "cmd" : "loadT",
        "name" : "beginner"
    }, injectBlockly);
};

$(document).ready(WRAP.fn3(init, 'page init'));
