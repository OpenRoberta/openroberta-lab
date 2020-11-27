define(['exports', 'util', 'log', 'message', 'program.controller', 'program.model', 'socket.controller', 'guiState.controller', 'webview.controller', 'jquery'], function(
    exports, UTIL, LOG, MSG, PROG_C, PROGRAM, SOCKET_C, GUISTATE_C, WEBVIEW_C, $) {

    var blocklyWorkspace;
    var interpreter;
    var reset;

    function init(workspace) {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        reset = false;
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
        if (GUISTATE_C.getConnection() !== 'autoConnection' && GUISTATE_C.getConnection() !== 'jsPlay') {
            blocklyWorkspace.robControls.disable('runOnBrick');
        }
    }

    /**
     * Start the program on brick from the source code editor
     */

    function runNative(sourceCode) {
        GUISTATE_C.setPing(false);
        GUISTATE_C.setConnectionState("busy");
        LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick from source code editor');
        var callback = getConnectionTypeCallbackForEditor();
        PROGRAM.runNative(GUISTATE_C.getProgramName(), sourceCode, GUISTATE_C.getLanguage(), function(result) {
            callback(result);
            GUISTATE_C.setPing(true);
        });
    }
    exports.runNative = runNative;

    /**
     * Start the program on the brick
     */
    function runOnBrick() {
        GUISTATE_C.setPing(false);
        GUISTATE_C.setConnectionState("busy");
        LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick');

        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
        var callback = getConnectionTypeCallback();
        PROGRAM.runOnBrick(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, GUISTATE_C.getLanguage(), callback);
    }
    exports.runOnBrick = runOnBrick;

    function getConnectionTypeCallbackForEditor() {
        var connectionType = GUISTATE_C.getConnectionTypeEnum();
        if (GUISTATE_C.getConnection() === connectionType.AUTO || GUISTATE_C.getConnection() === connectionType.LOCAL) {
            return function(result) {
                runForAutoConnection(result);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.AGENT || GUISTATE_C.getConnection() === connectionType.AGENTORTOKEN && GUISTATE_C.getIsAgent()) {
            return function(result) {
                runForAgentConnection(result);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.WEBVIEW) {
            return function(result) {
                runForWebviewConnection(result);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.JSPLAY) {
            return function(result) {
                runForJSPlayConnection(result);
            };
        }
        return function(result) {
            runForToken(result);
        };
    }

    function getConnectionTypeCallback() {
        var connectionType = GUISTATE_C.getConnectionTypeEnum();
        if (GUISTATE_C.getConnection() === connectionType.AUTO || GUISTATE_C.getConnection() === connectionType.LOCAL) {
            return function(result) {
                runForAutoConnection(result);
                PROG_C.reloadProgram(result);
                GUISTATE_C.setPing(true);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.AGENT || GUISTATE_C.getConnection() === connectionType.AGENTORTOKEN && GUISTATE_C.getIsAgent()) {
            return function(result) {
                runForAgentConnection(result);
                PROG_C.reloadProgram(result);
                GUISTATE_C.setPing(true);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.WEBVIEW) {
            return function(result) {
                runForWebviewConnection(result);
                PROG_C.reloadProgram(result);
                GUISTATE_C.setPing(true);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.JSPLAY) {
            return function(result) {
                runForJSPlayConnection(result);
                PROG_C.reloadProgram(result);
                GUISTATE_C.setPing(true);
            };
        }
        return function(result) {
            runForToken(result);
            PROG_C.reloadProgram(result);
            GUISTATE_C.setPing(true);
        };
    }

    function runForAutoConnection(result) {
        GUISTATE_C.setState(result);
        if (result.rc == "ok") {
            var filename = (result.programName || GUISTATE_C.getProgramName()) + "." + GUISTATE_C.getBinaryFileExtension();
            if (GUISTATE_C.getBinaryFileExtension() === "bin" || GUISTATE_C.getBinaryFileExtension() === "uf2") {
                result.compiledCode = UTIL.base64decode(result.compiledCode);
            }
            if (GUISTATE_C.isProgramToDownload() || navigator.userAgent.toLowerCase().match(/iPad|iPhone|android/i) !== null) {
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
                });

                $("#save-client-compiled-program").one("shown.bs.modal", function(e) {
                    $('#download-instructions li').each(function(index) {
                        $(this).delay(750 * index).animate({
                            opacity: 1
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

    /**
     * Creates the pop-up for robots that play sound inside the browser instead
     * of downloading a file (f.e. Edison) This function is very similar to
     * runForAutoConnection, but instead of a download link a Play button is
     * created. Also, some parts of the autoConnection pop-up are hidden: - the
     * "I've changed my download folder" checkbox - the "OK" button in the
     * footer
     * 
     * @param result
     *            the result that is received from the server after sending the
     *            program to it
     */
    function runForJSPlayConnection(result) {
        if (result.rc !== "ok") {
            GUISTATE_C.setConnectionState("wait");
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        } else {
            var wavFileContent = UTIL.base64decode(result.compiledCode);
            var audio;
            $('#changedDownloadFolder').addClass('hidden');

            //This detects IE11 (and IE11 only), see: https://developer.mozilla.org/en-US/docs/Web/API/Window/crypto
            if (window.msCrypto) {
                //Internet Explorer (all ver.) does not support playing WAV files in the browser
                //If the user uses IE11 the file will not be played, but downloaded instead
                //See: https://caniuse.com/#feat=wav, https://www.w3schools.com/html/html5_audio.asp
                createDownloadLink(GUISTATE_C.getProgramName() + '.wav', wavFileContent);
            } else {
                //All non-IE browsers can play WAV files in the browser, see: https://www.w3schools.com/html/html5_audio.asp
                $('#OKButtonModalFooter').addClass('hidden');
                var contentAsBlob = new Blob([wavFileContent], {
                    type: 'audio/wav'
                });
                audio = new Audio(window.URL.createObjectURL(contentAsBlob));
                createPlayButton(audio);
            }

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

            $("#save-client-compiled-program").one("shown.bs.modal", function(e) {
                $('#download-instructions li').each(function(index) {
                    $(this).delay(750 * index).animate({
                        opacity: 1
                    }, 1000);
                });
            });

            $('#save-client-compiled-program').one('hidden.bs.modal', function(e) {
                if (!window.msCrypto) {
                    audio.pause();
                    audio.load();
                }
                var textH = $("#popupDownloadHeader").text();
                $("#popupDownloadHeader").text(textH.replace($.trim(GUISTATE_C.getRobotRealName()), "$"));
                $('#programLink').remove();
                $('#download-instructions').empty();
                GUISTATE_C.setConnectionState("wait");
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());

                //Un-hide the div if it was hidden before
                $('#changedDownloadFolder').removeClass('hidden');
                $('#OKButtonModalFooter').removeClass('hidden');
            });

            $('#save-client-compiled-program').modal('show');
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
        MSG.displayInformation(result, result.message, result.message, result.programName || GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        if (result.rc == "ok") {
            if (Blockly.Msg['MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase()]) {
                MSG.displayMessage('MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase(), 'TOAST');
            }
        } else {
            GUISTATE_C.setConnectionState("error");
        }
    }

    function stopBrick() {
        if (interpreter !== null) {
            interpreter.terminate();
        }
    }

    function runForWebviewConnection(result) {
        MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
        if (result.rc === "ok") {
            var programSrc = result.compiledCode;
            var program = JSON.parse(programSrc);
            interpreter = WEBVIEW_C.getInterpreter(program);
            if (interpreter !== null) {
                GUISTATE_C.setConnectionState("busy");
                blocklyWorkspace.robControls.switchToStop();
                try {
                    runStepInterpreter();
                } catch (error) {
                    interpreter.terminate();
                    interpreter = null;
                    alert(error);
                }
            }
            // TODO
        }
    }

    function runStepInterpreter() {
        while (!interpreter.isTerminated() && !reset) {
            var maxRunTime = new Date().getTime() + 100;
            var waitTime = interpreter.run(maxRunTime);

            if (waitTime > 0) {
                timeout(runStepInterpreter, waitTime);
                return;
            }
        }
    }

    /**
     * after the duration specified, call the callback function given. The
     * duration is partitioned into 100 millisec intervals to allow termination
     * of the running interpreter during a timeout. Be careful: the termination
     * is NOT effected here, but by the callback function (this should be *
     * 
     * @see evalOperation() in ALMOST ALL cases).
     * 
     * @param callback
     *            called when the time has elapsed .
     * @param durationInMilliSec
     *            time that should elapse before the callback is called
     */
    function timeout(callback, durationInMilliSec) {
        if (durationInMilliSec > 100) {
            // U.p( 'waiting for 100 msec from ' + durationInMilliSec + ' msec' );
            durationInMilliSec -= 100;
            setTimeout(timeout, 100, callback, durationInMilliSec);
        } else {
            // U.p( 'waiting for ' + durationInMilliSec + ' msec' );
            setTimeout(callback, durationInMilliSec);
        }
    }

    /**
     * Creates a blob from the program content for file download and a
     * click-able html download link for the blob: <a download="PROGRAM_NAME"
     * href="CONTENT_AS_BLOB" style="font-size:36px">PROGRAM_NAME</a>
     * 
     * This is needed f.e. for Calliope where the file has to be downloaded and
     * copied onto the brick manually
     * 
     * @param fileName
     *            the file name (for PROGRAM_NAME)
     * @param content
     *            for the blob (for CONTENT_AS_BLOB)
     */
    function createDownloadLink(fileName, content) {
        if (!('msSaveOrOpenBlob' in navigator)) {
            $('#trA').removeClass('hidden');
        } else {
            $('#trA').addClass('hidden');
            UTIL.download(fileName, content);
            GUISTATE_C.setConnectionState("error");
        }
        var downloadLink;
        if ('Blob' in window) {
            var contentAsBlob = new Blob([content], {
                type: 'application/octet-stream'
            });
            if ('msSaveOrOpenBlob' in navigator) {
                navigator.msSaveOrOpenBlob(contentAsBlob, fileName);
            } else {
                downloadLink = document.createElement('a');
                downloadLink.download = fileName;
                downloadLink.innerHTML = fileName;
                downloadLink.href = window.URL.createObjectURL(contentAsBlob);
            }
        } else {
            downloadLink = document.createElement('a');
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

    /**
     * Creates a Play button for an Audio object so that the sound can be played
     * and paused/restarted inside the browser:
     * 
     * <button type="button" class="btn btn-primary" style="font-size:36px">
     * <span class="typcn typcn-media-play" style="color : black"></span>
     * </button>
     * 
     * 
     * @param fileName
     *            the name of the program
     * @param content
     *            the content of the WAV file as a Base64 encoded String
     */
    function createPlayButton(audio) {
        $('#trA').removeClass('hidden');
        var playButton;
        if ('Blob' in window) {
            //Create a bootstrap button
            playButton = document.createElement('button');
            playButton.setAttribute('type', 'button');
            playButton.setAttribute('class', 'btn btn-primary');

            var playing = false;
            playButton.onclick = function() {
                if (playing == false) {
                    audio.play();
                    playIcon.setAttribute('class', 'typcn typcn-media-stop');
                    playing = true;
                    audio.addEventListener("ended", function() {
                        $('#save-client-compiled-program').modal('hide');
                    });
                } else {
                    playIcon.setAttribute('class', 'typcn typcn-media-play');
                    audio.pause();
                    audio.load();
                    playing = false;
                }

            };

            //Create the play icon inside the button
            var playIcon = document.createElement('span');
            playIcon.setAttribute('class', 'typcn typcn-media-play');
            playIcon.setAttribute('style', 'color : black');
            playButton.appendChild(playIcon);
        }

        if (playButton) {
            var programLinkDiv = document.createElement('div');
            programLinkDiv.setAttribute('id', 'programLink');
            programLinkDiv.setAttribute('style', 'text-align: center;');
            programLinkDiv.appendChild(document.createElement('br'));
            programLinkDiv.appendChild(playButton);
            playButton.setAttribute('style', 'font-size:36px');
            $('#downloadLink').append(programLinkDiv);
        }
    }

    function reset2DefaultFirmware() {
        if (GUISTATE_C.hasRobotDefaultFirmware()) {
            var connectionType = GUISTATE_C.getConnectionTypeEnum();
            if (GUISTATE_C.getConnection() == connectionType.AUTO || GUISTATE_C.getConnection() == connectionType.LOCAL) {
                PROGRAM.resetProgram(function(result) {
                    runForAutoConnection(result);
                });
            } else {
                PROGRAM.resetProgram(function(result) {
                    runForToken(result);
                });
            }
        } else {
            MSG.displayInformation({
                rc: "error"
            }, "", "should not happen!");
        }
    }
    exports.reset2DefaultFirmware = reset2DefaultFirmware;
});