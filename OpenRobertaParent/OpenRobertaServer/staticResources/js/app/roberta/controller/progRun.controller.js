define([ 'exports', 'util', 'log', 'message', 'program.controller', 'program.model', 'socket.controller', 'guiState.controller', 'webview.controller',
        'interpreter.interpreter', 'interpreter.nativeWeDo', 'jquery' ], function(exports, UTIL, LOG, MSG, PROG_C, PROGRAM, SOCKET_C, GUISTATE_C, WEBVIEW_C, WEDO_I, WEDO_N, $) {

    var blocklyWorkspace;
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
        if (GUISTATE_C.getConnection() != 'autoConnection') {
            blocklyWorkspace.robControls.disable('runOnBrick');
        }
    }

    /**
     * Start the program on the brick
     */
    function runOnBrick() {
        if (!GUISTATE_C.isRobotConnected()) {
            MSG.displayMessage("POPUP_ROBOT_NOT_CONNECTED", "POPUP", "");
            return;
        } else if (GUISTATE_C.robotState === 'busy' && GUISTATE_C.getConnection() === 'token') {
            MSG.displayMessage("POPUP_ROBOT_BUSY", "POPUP", "");
            return;
        }
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
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
                runForAutoConnection(result);
                PROG_C.reloadProgram(result);
            });
        } else if (GUISTATE_C.getConnection() == connectionType.AGENT || GUISTATE_C.getConnection() == connectionType.AGENTORTOKEN && GUISTATE_C.getIsAgent()) {
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
                runForAgentConnection(result);
                PROG_C.reloadProgram(result);
            });
        } else if (GUISTATE_C.getConnection() == connectionType.WEBVIEW) {
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
                runForWebviewConnection(result);
                PROG_C.reloadProgram(result);
            });
        } else {
            PROGRAM.runOnBrick(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
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
            if (GUISTATE_C.isProgramToDownload() || navigator.userAgent.toLowerCase().match(/iPad|iPhone|android/i) != null) {
                // either the user doesn't want to see the modal anymore or he uses a smartphone / tablet, where you cannot choose the download folder.
                UTIL.download(filename + '.hex', result.compiledCode);
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
                fillDownloadModal(filename, result.compiledCode);

                $("#save-client-compiled-program").one("shown.bs.modal", function(e) {
                    $('#download-instructions tr').each(function(i) {
                        $(this).delay(750 * i).animate({
                            opacity : 1
                        }, 1000);
                    });
                });
                $('#save-client-compiled-program').one('hidden.bs.modal', function(e) {
                    if ($('#label-checkbox').is(':checked')) {
                        GUISTATE_C.setProgramToDownload();
                    }
                    $('#programLink').remove();
                    $('#download-instructions tr').each(function(i) {
                        $(this).css('opacity', '0');
                    });
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
    }

    function runForWebviewConnection(result) {
        if (result.rc === "ok") {
            var programSrc = result.compiledCode;
            var program = JSON.parse(programSrc);
            var ops = program.ops;
            var functionDeclaration = program.functionDeclaration;
            if (GUISTATE_C.getRobot() === "wedo") {
                GUISTATE_C.setConnectionState("busy");
                var weDoInterpreter = new WEDO_I.Interpreter();
                try{
	                weDoInterpreter.run(program, new WEDO_N.NativeWeDo(), callbackOnTermination);
	             } catch(error) {
	            	weDoInterpreter.terminate();
                    alert(error);
                }
            }
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
        }
    }

    function fillDownloadModal(fileName, content) {
        var rawSvg;
        if (!('msSaveOrOpenBlob' in navigator)) {
            $('#trA').removeClass('hidden');
        } else {
            $('#trA').addClass('hidden');
            UTIL.download(fileName + '.hex', content);
            GUISTATE_C.setConnectionState("error");
        }

        if ('Blob' in window) {
            var contentAsBlob = new Blob([ content ], {
                type : 'application/octet-stream'
            });
            if ('msSaveOrOpenBlob' in navigator) {
                navigator.msSaveOrOpenBlob(contentAsBlob, fileName + '.hex');
            } else {
                var downloadLink = document.createElement('a');
                downloadLink.download = fileName + '.hex';
                downloadLink.innerHTML = fileName;
                downloadLink.href = window.URL.createObjectURL(contentAsBlob);
            }
        } else {
            var downloadLink = document.createElement('a');
            downloadLink.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(content));
            downloadLink.setAttribute('download', fileName + '.hex');
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
        var textH = $("#popupDownloadHeader").text();
        $("#popupDownloadHeader").text(textH.replace("$", $.trim(GUISTATE_C.getRobotRealName())));
        var textC = $("#download-instructions").find("tr").eq(2).find("td").eq(1).html();
        var usb;
        if (GUISTATE_C.getRobot().indexOf("calliope") >= 0) {
            usb = "MINI";
        } else {
            usb = GUISTATE_C.getRobot().toUpperCase();
        }
        $("#download-instructions").find("tr").eq(2).find("td").eq(1).html(textC.replace("$", usb));
    }
});
