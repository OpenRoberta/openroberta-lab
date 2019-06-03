define([ 'exports', 'util', 'log', 'message', 'program.controller', 'program.model', 'socket.controller', 'guiState.controller', 'webview.controller',
        'interpreter.interpreter', 'interpreter.nativeWeDo', 'jquery' ], function(exports, UTIL, LOG, MSG, PROG_C, PROGRAM, SOCKET_C, GUISTATE_C, WEBVIEW_C,
        WEDO_I, WEDO_N, $) {

    var blocklyWorkspace;
    var interpreter;
    /**
     * 
     */
    function init(workspace) {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        //initView();
        initEvents();
    }
    exports.init = init;

    function initEvents() {
        Blockly.bindEvent_(blocklyWorkspace.robControls.runOnBrick, 'mousedown', null, function(e) {
            LOG.info('runOnBrick from blockly button');
            runOnBrick();
            return false;
        });
        Blockly.bindEvent_(blocklyWorkspace.robControls.stopBrick, 'mousedown', null, function(e) {
            LOG.info('stopBrick from blockly button');
            stopBrick();
            return false;
        });
        if (GUISTATE_C.getConnection() != 'autoConnection') {
            blocklyWorkspace.robControls.disable('runOnBrick');
        }
    }

    /**
     * Start the program on the brick
     */
    function runOnBrick() {
        GUISTATE_C.setConnectionState("busy");
        LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick');
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);

        var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

        var language = GUISTATE_C.getLanguage();

        var connectionType = GUISTATE_C.getConnectionTypeEnum();
        if (GUISTATE_C.getConnection() == connectionType.AUTO || GUISTATE_C.getConnection() == connectionType.LOCAL) {
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function(result) {
                runForAutoConnection(result);
                PROG_C.reloadProgram(result);
            });
        } else if (GUISTATE_C.getConnection() == connectionType.AGENT || GUISTATE_C.getConnection() == connectionType.AGENTORTOKEN && GUISTATE_C.getIsAgent()) {
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function(result) {
                runForAgentConnection(result);
                PROG_C.reloadProgram(result);
            });
        } else if (GUISTATE_C.getConnection() == connectionType.WEBVIEW) {
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function(result) {
                runForWebviewConnection(result);
                PROG_C.reloadProgram(result);
            });
        } else {
            PROGRAM.runOnBrick(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function(result) {
                runForToken(result);
                PROG_C.reloadProgram(result);
            });
        }
    }
    exports.runOnBrick = runOnBrick;

    function runForAutoConnection(result) {
        GUISTATE_C.setState(result);
        if (result.rc == "ok") {
            var filename = GUISTATE_C.getProgramName();
            if (GUISTATE_C.getRobot() === 'sensebox') {
                filename += '.bin';
                result.compiledCode = UTIL.base64decode(result.compiledCode);
            } else if (GUISTATE_C.getRobot() === 'ev3c4ev3') {
                filename += '.uf2';
                result.compiledCode = UTIL.base64decode(result.compiledCode);
                // TODO: Update the popup message to tell the user to start the program (selecting it in the EV3 menu)
            } else {
                filename += '.hex';
            }
            if (GUISTATE_C.isProgramToDownload() || navigator.userAgent.toLowerCase().match(/iPad|iPhone|android/i) != null) {
                // either the user doesn't want to see the modal anymore or he uses a smartphone / tablet, where you cannot choose the download folder.
                UTIL.download(filename, result.compiledCode);
                setTimeout(function() {
                    GUISTATE_C.setConnectionState("wait");
                }, 5000);
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            } else if (GUISTATE_C.getConnection() == GUISTATE_C.getConnectionTypeEnum().LOCAL) {
                setTimeout(function() {
                    GUISTATE_C.setConnectionState("wait");
                }, 5000);
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            } else {
                createDownloadLink(filename, result.compiledCode);
                var textH = $("#popupDownloadHeader").text();
                $("#popupDownloadHeader").text(textH.replace("$", $.trim(GUISTATE_C.getRobotRealName())));
                for (var i = 1; Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]; i++) {
                    var step = $('<li class="typcn typcn-roberta">');
                    var a = Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i + '_' + GUISTATE_C.getRobotGroup().toUpperCase()] || Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]
                            || 'POPUP_DOWNLOAD_STEP_' + i;
                    step.html('<span class="download-message">' + a + '</span>');
                    step.css('opacity', '0');
                    $('#download-instructions').append(step);
                }
                var substituteName = GUISTATE_C.getRobotGroup().toUpperCase();
                $("#download-instructions li").each(function(index) {
                    if (GUISTATE_C.getRobotGroup() === "calliope") {
                        substituteName = "MINI";
                    }
                    $(this).html($(this).html().replace("$", substituteName));
                })
                $("#save-client-compiled-program").one("shown.bs.modal", function(e) {
                    $('#download-instructions li').each(function(index) {
                        $(this).delay(750 * index).animate({
                            opacity : 1
                        }, 1000);
                    });
                });
                $('#save-client-compiled-program').one('hidden.bs.modal', function(e) {
                    var textH = $("#popupDownloadHeader").text();
                    $("#popupDownloadHeader").text(textH.replace($.trim(GUISTATE_C.getRobotRealName()), "$"));
                    if ($('#label-checkbox').is(':checked')) {
                        GUISTATE_C.setProgramToDownload();
                    }
                    $('#programLink').remove();
                    $('#download-instructions').empty();
                    GUISTATE_C.setConnectionState("wait");
                    MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                });
                $('#save-client-compiled-program').modal('show');
            }
        } else {
            GUISTATE_C.setConnectionState("wait");
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        }
    }

    function runForAgentConnection(result) {
        $('#menuRunProg').parent().addClass('disabled');
        $('#head-navi-icon-robot').addClass('busy');
        GUISTATE_C.setState(result);
        if (result.rc == "ok") {
            SOCKET_C.uploadProgram(result.compiledCode, GUISTATE_C.getRobotPort());
            setTimeout(function() {
                GUISTATE_C.setConnectionState("error");
            }, 5000);
        } else {
            GUISTATE_C.setConnectionState("error");
        }
        MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
    }

    function runForToken(result) {
        GUISTATE_C.setState(result);
        MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        if (result.rc == "ok") {
            if (Blockly.Msg['MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase()]) {
                MSG.displayMessage('MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase(), 'TOAST');
            }
        } else {
            GUISTATE_C.setConnectionState("error");
        }
    }

    function callbackOnTermination() {
        GUISTATE_C.setConnectionState("wait");
        blocklyWorkspace.robControls.switchToStart();
    }

    function stopBrick() {
        if (interpreter !== null) {
            interpreter.terminate();
        }
    }

    function runForWebviewConnection(result) {
        if (result.rc === "ok") {
            var programSrc = result.compiledCode;
            var program = JSON.parse(programSrc);
            var ops = program.ops;
            var functionDeclaration = program.functionDeclaration;
            switch (GUISTATE_C.getRobot()) {
            case "wedo":
                interpreter = new WEDO_I.Interpreter();
                if (interpreter !== null) {
                    GUISTATE_C.setConnectionState("busy");
                    blocklyWorkspace.robControls.switchToStop();
                    try {
                        interpreter.run(program, new WEDO_N.NativeWeDo(), callbackOnTermination);
                    } catch (error) {
                        interpreter.terminate();
                        interpreter = null;
                        alert(error);
                    }
                }
                break;
            default:
                // TODO
            }
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
        }
    }

    function createDownloadLink(fileName, content) {
        var rawSvg;
        if (!('msSaveOrOpenBlob' in navigator)) {
            $('#trA').removeClass('hidden');
        } else {
            $('#trA').addClass('hidden');
            UTIL.download(fileName, content);
            GUISTATE_C.setConnectionState("error");
        }

        if ('Blob' in window) {
            var contentAsBlob = new Blob([ content ], {
                type : 'application/octet-stream'
            });
            if ('msSaveOrOpenBlob' in navigator) {
                navigator.msSaveOrOpenBlob(contentAsBlob, fileName);
            } else {
                var downloadLink = document.createElement('a');
                downloadLink.download = fileName;
                downloadLink.innerHTML = fileName;
                downloadLink.href = window.URL.createObjectURL(contentAsBlob);
            }
        } else {
            var downloadLink = document.createElement('a');
            downloadLink.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(content));
            downloadLink.setAttribute('download', fileName);
            downloadLink.style.display = 'none';
        }

        //create link with content
        if (downloadLink && !('msSaveOrOpenBlob' in navigator)) {
            var programLinkDiv = document.createElement('div');
            programLinkDiv.setAttribute('id', 'programLink');
            var linebreak = document.createElement('br');
            programLinkDiv.setAttribute('style', 'text-align: center;');
            programLinkDiv.appendChild(linebreak);
            programLinkDiv.appendChild(downloadLink);
            downloadLink.setAttribute('style', 'font-size:36px');
            $('#downloadLink').append(programLinkDiv);
        }
    }
});
