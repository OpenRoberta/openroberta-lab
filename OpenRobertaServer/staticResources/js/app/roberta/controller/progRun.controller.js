define([ 'exports', 'util', 'log', 'message', 'guiState.model', 'socket.controller', 'guiState.controller', 'jquery', 'program.controller' ], function(exports, UTIL, LOG, MSG,
        GUISTATE, SOCKET_C, GUISTATE_C, $, PROGRAM_C) {

    function init() {

    }

    function runForAutoConnection(result) {
        GUISTATE_C.setState(result);
        if (result.rc == "ok") {
            if (GUISTATE_C.isProgramToDownload()) {
                var filename = GUISTATE_C.getProgramName() + '.hex';
                UTIL.download(filename, result.compiledCode);
                GUISTATE_C.setAutoConnectedBusy(false);
            } else {
                //create link with content
                var programLink = "<div id='programLink' style='text-align: center;'><br><a style='font-size:36px; padding: 20px' download='"
                        + GUISTATE_C.getProgramName() + ".hex' href='data:Application/octet-stream;content-disposition:attachment;charset=utf-8,"
                        + encodeURIComponent(result.compiledCode) + "'>" + GUISTATE_C.getProgramName() + "</a></div>";
                if (navigator.userAgent.indexOf('Edge') < 0) {
                    $("#liA").css('background-image', 'url(\'data:image/svg+xml;charset=UTF-8,'
                            + '<svg xmlns="http://www.w3.org/2000/svg" width="102" height="77" viewBox="0 0 77 102">'
                            + '<text fill="%23337ab7" x="3" y="34" style="font-family:Arial;font-size:20px;text-decoration:underline">'
                            + GUISTATE_C.getProgramName()
                            + '</text><rect x="1" y="1" stroke="%23BBBBBB" width="100" height="75" rx="2" ry="2" style="fill:none;stroke-width:3"/><g fill="%23DF01D7"><path d="M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z" transform="matrix(-2.40165 0 0 2.44495 146.371 -56.5809)" fill="%23ff0000"/></g></svg>\')');
                    $("#liA").css('background-repeat', 'no-repeat');
                    $('#trA').removeClass('hidden');
                } else {
                    $('#trA').addClass('hidden');
                    var filename = GUISTATE_C.getProgramName() + '.hex';
                    UTIL.download(filename, result.compiledCode);
                    GUISTATE_C.setAutoConnectedBusy(false);
                }
                $("#liB").css('background-image', 'url(\'data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" width="102" height="77" viewBox="0 0 77 102"><rect x="0" y="0" width="102" height="77" fill="%23dddddd"/><text fill="%23333333" x="3" y="34" style="font-family:Arial;font-size:14">'
                        + Blockly.Msg.POPUP_DOWNLOAD_SAVE_AS
                        + '</text><rect x="1" y="1" stroke="%23BBBBBB" width="100" height="75" rx="2" ry="2" style="fill:none;stroke-width:2"/><g fill="%23DF01D7"><path d="M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z" transform="matrix(2.44495 0 0 2.44495 -47.8835 -56.5809)" fill="%239400D3"/></g></svg>\')');
                $("#liB").css('background-repeat', 'no-repeat');
                var usb;
                if (GUISTATE_C.getGuiRobot().indexOf("calliope") >= 0) {
                    usb = "MINI";
                } else {
                    usb = GUISTATE_C.getGuiRobot().toUpperCase();
                }
                $("#liC").css('background-image', 'url(\'data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" width="102" height="77" viewBox="0 0 77 102"><rect x="0" y="0" width="102" height="77" fill="%23dddddd"/><text fill="%23333333" x="3" y="34" style="font-family:Arial;font-size:14">'
                        + usb
                        + '</text><rect x="1" y="1" stroke="%23BBBBBB" width="100" height="75" rx="2" ry="2" style="fill:none;stroke-width:2"/><g fill="%23DF01D7"><path d="M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z" transform="matrix(2.44495 0 0 2.44495 -47.8835 -56.5809)" fill="%239400D3"/></g></svg>\')');
                $("#liC").css('background-repeat', 'no-repeat');
                $("#liD").css('background-image', 'url(\'data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" width="102" height="77" viewBox="0 0 77 102"><rect x="0" y="0" width="102" height="77" fill="%23dddddd"/><text fill="%23333333" x="3" y="40" style="font-family:Arial;font-size:20px">'
                        + Blockly.Msg.POPUP_DOWNLOAD_SAVE
                        + '</text><rect x="1" y="1" stroke="%23BBBBBB" width="100" height="75" rx="2" ry="2" style="fill:none;stroke-width:2"/><g fill="%23DF01D7"><path d="M33.2 34.9h2.2c-0.2 0.3-0.3 0.6-0.3 1h-1.9c-0.3 0-0.5-0.2-0.5-0.5s0.2-0.5 0.5-0.5ZM39.1 29c0-0.3 0.2-0.5 0.5-0.5 0.3 0 0.5 0.2 0.5 0.5v1.7c-0.4 0.1-0.7 0.2-1 0.4v-2.1ZM34.8 31.3c-0.1-0.2-0.1-0.5 0-0.7s0.5-0.2 0.7 0l1.6 1.6c-0.2 0.3-0.4 0.5-0.6 0.8l-1.7-1.7ZM47.5 37.3c0-3.2-2.6-5.7-5.7-5.7 0 0 0 0-0.1 0 0 0 0 0 0 0s0 0 0 0c-0.3 0-0.5 0-0.8 0.1 0 0-0.1 0-0.1 0 -2.5 0.3-4.5 2.3-4.8 4.9 0 0 0 0 0 0.1 0 0.2 0 0.5 0 0.7 0 0 0 0 0 0v7.1c0 3.2 2.6 5.8 5.8 5.8 3.2 0 5.8-2.6 5.8-5.8l-0.1-7.2c0 0 0 0 0 0ZM41.7 36.6c0.4 0 0.8 0.6 0.8 1.3 0 0.7-0.3 1.3-0.8 1.3s-0.8-0.6-0.8-1.3c0.1-0.8 0.4-1.3 0.8-1.3ZM45.5 44.5c0 2.1-1.7 3.8-3.8 3.8s-3.8-1.7-3.8-3.8v-5.3h2.4c0.3 0.5 0.8 0.9 1.4 0.9 1 0 1.7-1 1.7-2.3 0-1.1-0.5-1.9-1.2-2.2v-2c1.9 0.2 3.3 1.8 3.3 3.8v7.1Z" transform="matrix(2.44495 0 0 2.44495 -47.8835 -56.5809)" fill="%239400D3"/></g></svg>\')');
                $("#liD").css('background-repeat', 'no-repeat');
                var textH
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
                    GUISTATE_C.setAutoConnectedBusy(false);
                });
                var robotRealName;
                var list = GUISTATE_C.getRobots();

                for ( var robot in list) {
                    if (!list.hasOwnProperty(robot)) {
                        continue;
                    }
                    if (list[robot].name == GUISTATE_C.getGuiRobot()) {
                        robotRealName = list[robot].realName;
                    }
                }
                // fix header$(selector).attr(attribute)
                textH = $("#popupDownloadHeader").text();
                $("#popupDownloadHeader").text(textH.replace("$", $.trim(robotRealName)));
                textC = $("#download-instructions").find("tr").eq(2).find("td").eq(1).html();
                $("#download-instructions").find("tr").eq(2).find("td").eq(1).html(textC.replace("$", usb));
                $('#save-client-compiled-program').modal('show');
            }
        } else {
            MSG.displayInformation(result, "", result.message, "");
            GUISTATE_C.setAutoConnectedBusy(false);
        }
        PROGRAM_C.reloadProgram(result);
    }

    exports.runForAutoConnection = runForAutoConnection;

    function runForAgentConnection(result) {
        GUISTATE_C.setAutoConnectedBusy(true);
        GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
        $('#menuRunProg').parent().addClass('disabled');
        $('#head-navi-icon-robot').addClass('busy');
        GUISTATE_C.setState(result);
        if (result.rc == "ok") {
            MSG.displayMessage(Blockly.Msg["MESSAGE_EDIT_START"], 'TOAST', GUISTATE_C.getProgramName());
            SOCKET_C.uploadProgram(result.compiledCode, GUISTATE_C.getRobotPort());
            GUISTATE_C.setAutoConnectedBusy(false);
            $('#head-navi-icon-robot').removeClass('busy');
            GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
            $('#menuRunProg').parent().removeClass('disabled');
        } else {

            console.log("result not ok");
            MSG.displayInformation(result, "", result.message, "");
            GUISTATE_C.setAutoConnectedBusy(false);
            $('#head-navi-icon-robot').removeClass('busy');
        }
    }

    exports.runForAgentConnection = runForAgentConnection;

    function runForToken(result) {
        GUISTATE_C.setState(result);
        if (result.rc == "ok") {
            MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", GUISTATE_C.getProgramName());
        } else {
            MSG.displayInformation(result, "", result.message, "");
        }
        PROGRAM_C.reloadProgram(result);
    }

    exports.runForToken = runForToken;
});
