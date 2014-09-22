var token = '1A2B3C4D';

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
    var text = "<block_set xmlns='http: // www.w3.org/1999/xhtml'>"
        + "<instance x='25' y='50'>" + "<block type='robControls_start'>"
        + "</block>" + "</instance>" + "</block_set>";
    var xml = Blockly.Xml.textToDom(text);
    Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
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

function saveToServer() {
  var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
  var xml_text = Blockly.Xml.domToText(xml);
  var $name = $('#programName');
  COMM.json("/blocks", {
    "cmd" : "saveP",
    "name" : $name.val(),
    "program" : xml_text
  }, response);
}

function runOnBrick() {
  var $name = $('#programName');
  COMM.json("/blocks", {
    "cmd" : "runP",
    "name" : $name.val()
  }, response);
}

function showProgram(result, load) {
  response(result);
  if (result.rc === 'ok') {
    var xml = Blockly.Xml.textToDom(result.data);
    if (load) {
      Blockly.mainWorkspace.clear();
    }
    Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xml);
  }
};

function loadFromServer(load) {
  var $name = $('#programName');
  COMM.json("/blocks", {
    "cmd" : "loadP",
    "name" : $name.val()
  }, function(result) {
    showProgram(result, load);
  });
}

function deleteOnServer() {
  var $name = $('#programName');
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
  COMM.json("/blocks", {
    "cmd" : "loadP",
    "name" : programName
  }, function(result) {
    $("#tabs").tabs("option", "active", 0);
    $('#name').val(programName);
    showProgram(result, true);
  });
}

var toolboxDone = false;

function showToolbox(result) {
  response(result);
  if (result.rc === 'ok') {
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
  $programs
      .dataTable({
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
          "sLengthMenu" : 'Show <select>'
              + '<option value="10">10</option><option value="20">20</option><option value="25">25</option>'
              + '<option value="30">30</option><option value="100">100</option><option value="-1">All</option>'
              + '</select> programs/revisions'
        },
        "fnDrawCallback" : function() {
          var counter = +$('#redrawCounter').text();
          $('#redrawCounter').text(counter + 1);
        }
      });
  $('#programNameTable tbody').on('click', 'tr', selectionFn);
}

function startProgram() {
  if (userId == "none") {
    // alert("No user id, you need to sign in");
    saveToServer();
    runOnBrick();
  } else {
    // alert("We use the user program table");
    // saveUPToServer();
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
  $('#loadFromListing').onWrap('click', myLoadFromListing,
      'load blocks from program list');

  // #brickConfiguration is provisional, should be integrated in the menue
  $('#brickConfiguration').onWrap('click', function() {
    switchToBrickly();
  }, 'show brick configuration');

  $('#load').onWrap('click', function() {
    if (userId == "none") {
      alert("No user id, using the general table");
      loadFromServer(true);
    } else {
      alert("We use the user account");
      loadUPFromServer(true);
    }

  }, 'load the blocks');

  $('#add').onWrap('click', function() {

    if (userId == "none") {
      alert("No user id, you need to sign in");
      loadFromServer(false);
    } else {
      alert("We use the user program table");
      loadUPFromServer(false);
    }

  }, 'add the blocks');

  $('#del').onWrap('click', function() {
    if (userId == "none") {
      alert("No user id, you need to sign in");
      deleteOnServer();
    } else {
      alert("We use the user program table");
      deleteUPOnServer();
    }
  }, 'add the blocks');

  $('#run').onWrap('click', function() {

    if (userId == "none") {
      alert("No user id, you need to sign in");
      saveToServer();
      runOnBrick();
    } else {
      alert("We use the user program table");
      // saveUPToServer();
      runOnBrick();
    }

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
  /*
   * COMM.json("/blocks", { "cmd" : "loadT", "name" : "brickEV3" },
   * injectBlrickly);
   */

};

$(document).ready(WRAP.fn3(init, 'page init'));