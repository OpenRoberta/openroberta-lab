define([ 'exports', 'util', 'log', 'message', 'program.controller', 'program.model', 'socket.controller', 'guiState.controller', 'jquery' ], function(exports,
        UTIL, LOG, MSG, PROG_C, PROGRAM, SOCKET_C, GUISTATE_C, $) {

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
        if (GUISTATE_C.getConnection() == 'token') {
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
        GUISTATE_C.setConnectionBusy(true);
        LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick');
        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);

        var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

        var language = GUISTATE_C.getLanguage();

        var connectionType = GUISTATE_C.getConnectionTypeEnum();
        if (GUISTATE_C.getConnection() == connectionType.AUTO) {
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
                runForAutoConnection(result);
                PROG_C.reloadProgram(result);
            });
        } else if (GUISTATE_C.getConnection() == connectionType.AGENT || GUISTATE_C.getConnection() == connectionType.AGENTORTOKEN && GUISTATE_C.getIsAgent()) {
            PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
                runForAgentConnection(result);
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
            if (GUISTATE_C.isProgramToDownload() || navigator.userAgent.toLowerCase().match(/iPad|iPhone|android/i) != null) {
                var filename = GUISTATE_C.getProgramName() + '.hex';
                UTIL.download(filename, result.compiledCode);
                setTimeout(function() {
                    GUISTATE_C.setConnectionBusy(false);
                }, 5000);
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            } else {
                //create link with content
                var programLink = "<div id='programLink' style='text-align: center;'><br><a style='font-size:36px; padding: 20px' download='"
                        + GUISTATE_C.getProgramName() + ".hex' href='data:application/octet-stream;content-disposition:attachment;charset=utf-8,"
                        + encodeURIComponent(result.compiledCode) + "'>" + GUISTATE_C.getProgramName() + "</a></div>";
                var rawSvg;
                if (navigator.userAgent.indexOf('Edge') < 0) {
                    rawSvg = '<svg xmlns=\'http://www.w3.org/2000/svg\' width=\'102\' height=\'77\' viewBox=\'0 0 77 102\'>'
                            + '<text fill=\'#337ab7\' x=\'3\' y=\'34\' style=\'font-family:Arial;font-size:20px;text-decoration:underline\'>'
                            + GUISTATE_C.getProgramName()
                            + '</text><rect x=\'1\' y=\'1\' stroke=\'#BBBBBB\' width=\'100\' height=\'75\' rx=\'2\' ry=\'2\' style=\'fill:none;stroke-width:3\'/><g fill=\'#DF01D7\'>'
                            + '<path d=\'M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z\' '
                            + 'transform=\'matrix(-2.40165 0 0 2.44495 146.371 -56.5809)\' fill=\'#ff0000\'/></g></svg>';

                    $("#liA").css('background-image', 'url("data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(rawSvg) + '")');
                    $('#trA').removeClass('hidden');
                } else {
                    $('#trA').addClass('hidden');
                    var filename = GUISTATE_C.getProgramName() + '.hex';
                    UTIL.download(filename, result.compiledCode);
                    GUISTATE_C.setConnectionBusy(false);
                }

                rawSvg = '<svg xmlns=\'http://www.w3.org/2000/svg\' width=\'102\' height=\'77\' viewBox=\'0 0 77 102\'><rect x=\'0\' y=\'0\' width=\'102\' height=\'77\' fill=\'#dddddd\'/><text fill=\'#333333\' x=\'3\' y=\'40\' style=\'font-family:Arial;font-size:16px\'>'
                        + Blockly.Msg.POPUP_DOWNLOAD_SAVE_AS
                        + '</text><rect x=\'1\' y=\'1\' stroke=\'#BBBBBB\' width=\'100\' height=\'75\' rx=\'2\' ry=\'2\' style=\'fill:none;stroke-width:2\'/><g fill=\'#DF01D7\'><path d=\'M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z\' transform=\'matrix(2.44495 0 0 2.44495 -47.8835 -56.5809)\' fill=\'#9400D3\'/></g></svg>';

                $("#liB").css('background-image', 'url("data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(rawSvg) + '")');
                var usb;
                if (GUISTATE_C.getGuiRobot().indexOf("calliope") >= 0) {
                    usb = "MINI";
                } else {
                    usb = GUISTATE_C.getGuiRobot().toUpperCase();
                }

                rawSvg = '<svg xmlns=\'http://www.w3.org/2000/svg\' width=\'102\' height=\'77\' viewBox=\'0 0 77 102\'><rect x=\'0\' y=\'0\' width=\'102\' height=\'77\' fill=\'#dddddd\'/><text fill=\'#333333\' x=\'3\' y=\'34\' style=\'font-family:Arial;font-size:16px\'>'
                        + usb
                        + '</text><rect x=\'1\' y=\'1\' stroke=\'#BBBBBB\' width=\'100\' height=\'75\' rx=\'2\' ry=\'2\' style=\'fill:none;stroke-width:2\'/><g fill=\'#DF01D7\'><path d=\'M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z\' transform=\'matrix(2.44495 0 0 2.44495 -47.8835 -56.5809)\' fill=\'#9400D3\'/></g></svg>';

                $("#liC").css('background-image', 'url("data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(rawSvg) + '")');

                rawSvg = '<svg xmlns=\'http://www.w3.org/2000/svg\' width=\'102\' height=\'77\' viewBox=\'0 0 77 102\'><rect x=\'0\' y=\'0\' width=\'102\' height=\'77\' fill=\'#dddddd\'/><text fill=\'#333333\' x=\'3\' y=\'34\' style=\'font-family:Arial;font-size:20px\'>'
                        + Blockly.Msg.POPUP_DOWNLOAD_SAVE
                        + '</text><rect x=\'1\' y=\'1\' stroke=\'#BBBBBB\' width=\'100\' height=\'75\' rx=\'2\' ry=\'2\' style=\'fill:none;stroke-width:2\'/><g fill=\'#DF01D7\'><path d=\'M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z\' transform=\'matrix(2.44495 0 0 2.44495 -47.8835 -56.5809)\' fill=\'#9400D3\'/></g></svg>';

                $("#liD").css('background-image', 'url("data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(rawSvg) + '")');
                var textH;
                var textC;
                $("#save-client-compiled-program").one("shown.bs.modal", function(e) {
                    if (navigator.userAgent.indexOf('Edge') < 0) {
                        $('#downloadLink').append(programLink);
                    } else {
                        $('#downloadLink').append("<div id='programLink' style='text-align: center;'><br><span style='font-size:36px; padding: 20px'>"
                                + GUISTATE_C.getProgramName() + "</span></div>");
                    }
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
                    if (textC) {
                        $("#download-instructions").find("tr").eq(2).find("td").eq(1).html(textC);
                    }
                    if (textH) {
                        $("#popupDownloadHeader").text(textH);
                    }
                    GUISTATE_C.setConnectionBusy(false);
                    MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                });
                // fix header$(selector).attr(attribute)
                textH = $("#popupDownloadHeader").text();
                $("#popupDownloadHeader").text(textH.replace("$", $.trim(GUISTATE_C.getRobotRealName())));
                textC = $("#download-instructions").find("tr").eq(2).find("td").eq(1).html();
                $("#download-instructions").find("tr").eq(2).find("td").eq(1).html(textC.replace("$", usb));
                $('#save-client-compiled-program').modal('show');
            }
        } else {
            GUISTATE_C.setConnectionBusy(false);
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
                GUISTATE_C.setConnectionBusy(false);
            }, 5000);
        } else {
            GUISTATE_C.setConnectionBusy(false);
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
            GUISTATE_C.setConnectionBusy(false);
        }
    }
});
