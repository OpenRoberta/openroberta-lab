function myLoadFromListing() {
    if (userId == "none") {

    } else {

        COMM.json("/blocks", {
            "cmd" : "obtainUserPrograms",
            "accountName" : userAccountName,
            "userId" : userId,
        }, showPrograms);
    }

}

function saveUPToServer() {
    if ($('#programName')) {
        var $name = $('#programName');
        setProgram($name.val());
    }
    if (userState.program) {
        var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
        var xml_text = Blockly.Xml.domToText(xml);
        userState.programSaved = true;
        LOG.info('save ' + userState.program + ' signed in: ' + userState.id);
        COMM.json("/blocks", {
            "cmd" : "saveP",
            "name" : userState.program,
            "program" : xml_text
        }, response);
    } else {
        alert('There is no name for your program available\n\n please save one with a name or load one');
    }
}

function loadUPFromServer(load) {
    var $name = $('#programName');
    COMM.json("/blocks", {
        "cmd" : "loadP",
        "name" : $name.val()
    }, function(result) {
        showProgram(result, load, $name.val());
    });
}

function deleteUPOnServer() {
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
