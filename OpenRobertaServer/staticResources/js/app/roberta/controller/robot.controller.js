define([ 'exports', 'util', 'log', 'message', 'guiState.controller', 'guiState.model', 'robot.model', 'program.controller', 'configuration.controller',
        'webview.controller', 'socket.controller', 'sourceCodeEditor.controller', 'progCode.controller', 'jquery', 'jquery-validate' ], function(exports, UTIL, LOG, MSG, GUISTATE_C, GUISTATE, ROBOT, PROGRAM_C,
        CONFIGURATION_C, WEBVIEW_C, SOCKET_C, CODEEDITOR_C, PROGCODE_C,  $) {

    var $formSingleModal;
    var $formSingleListModal;
    var robotPort;

    /**
     * Initialize robot
     */
    function init() {

        var ready = $.Deferred();
        $.when(ROBOT.setRobot(GUISTATE_C.getRobot(), function(result) {
            if (result.rc == 'ok') {
                GUISTATE_C.setRobot(GUISTATE_C.getRobot(), result, true);
            }
        })).then(function() {
            initRobotForms();
            ready.resolve();
        });
        return ready.promise();
    }
    exports.init = init;

    /**
     * Set token
     * 
     * @param {token}
     *            Token value to be set
     */
    function setToken(token) {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            ROBOT.setToken(token, function(result) {
                if (result.rc === "ok") {
                    GUISTATE_C.setRobotToken(token);
                    GUISTATE_C.setState(result);
                    // console.log(result.message);
                    MSG.displayInformation(result, "MESSAGE_ROBOT_CONNECTED", result.message, GUISTATE_C.getRobotName());
                    handleFirmwareConflict(result['robot.update'], result['robot.serverVersion']);
                } else {
                    if (result.message === 'ORA_TOKEN_SET_ERROR_WRONG_ROBOTTYPE') {
                        $('.modal').modal('hide');
                    }
                }
                UTIL.response(result);
            });
        }
    }

    function setPort(port) {
        robotPort = port;
        $('#single-modal-list').modal('hide');
        GUISTATE_C.setRobotPort(port);
    }
    exports.setPort = setPort;

    function getPort() {
        return robotPort;
    }
    exports.getPort = getPort;

    function initRobotForms() {
        $('#iconDisplayRobotState').onWrap('click', function() {
            showRobotInfo();
        }, 'icon robot click');

        $('#wlan-form').removeData('validator');
        $.validator.addMethod("wlanRegex", function(value, element) {
            return this.optional(element) || /^[a-zA-Z0-9$ *\(\)\{\}\[\]><~`\'\\\/|=+!?.,%#+&^@_\-äöüÄÖÜß]+$/gi.test(value);
        }, "This field contains nonvalid symbols.");
        $('#wlan-form').validate({
            rules : {
                wlanSsid : {
                    required : true,
                    wlanRegex : true
                },
                wlanPassword : {
                    required : true,
                    wlanRegex : true
                },
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertBefore(element.parent());
            },
            messages : {
                wlanSsid : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    wlanRegex : Blockly.Msg["VALIDATION_CONTAINS_SPECIAL_CHARACTERS"]
                },
                wlanPassword : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    wlanRegex : Blockly.Msg["VALIDATION_CONTAINS_SPECIAL_CHARACTERS"]
                }
            }
        });

        $('#setWlanCredentials').onWrap('click', function(e) {
            e.preventDefault();
            $('#wlan-form').validate();
            if ($('#wlan-form').valid()) {
                PROGRAM_C.SSID = document.getElementById("wlanSsid").value;
                PROGRAM_C.password = document.getElementById("wlanPassword").value;
                $("#menu-wlan").modal('hide');
            }
        }, 'wlan form submitted');

        $('#doUpdateFirmware').onWrap('click', function() {
            $('#set-token').modal('hide');
            $('#confirmUpdateFirmware').modal('hide');
            updateFirmware();
        }, 'update firmware of robot');

        $formSingleModal = $('#single-modal-form');

        $('#connectionsTable').bootstrapTable({
            formatNoMatches : function() {
                return '<div class="lds-ellipsis"><div></div><div></div><div></div><div></div></div>';
            },
            columns : [ {
                // TODO: translations
                title : 'Name',
                field : 'name',
            }, {
                visible : false,
                field : 'id'
            } ]
        });
        $('#connectionsTable').onWrap('click-row.bs.table', function(e, row) {
            WEBVIEW_C.jsToAppInterface({
                'target' : GUISTATE_C.getRobot(),
                'type' : 'connect',
                'robot' : row.id
            });
        }, "connect to robot");
        $('#show-available-connections').on('hidden.bs.modal', function(e) {
            WEBVIEW_C.jsToAppInterface({
                'target' : GUISTATE_C.getRobot(),
                'type' : 'stopScan'
            });
        });

        $('#show-available-connections').onWrap('add', function(event, data) {
            $('#connectionsTable').bootstrapTable('insertRow', {
                index : 999,
                row : {
                    name : data.brickname,
                    id : data.brickid
                }
            });
        });

        $('#show-available-connections').onWrap('connect', function(event, data) {
            var result = {};
            result["robot.name"] = data.brickname;
            result["robot.state"] = 'wait';
            GUISTATE_C.setState(result);
            $('#show-available-connections').modal('hide');
        }, "");
    }

    function showSetTokenModal() {
        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg["MENU_CONNECT"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_VALUE"]);
            $('#singleModalInput').addClass('capitalLetters');
            $('#single-modal a[href]').text(Blockly.Msg["POPUP_STARTUP_HELP"]);
            $('#single-modal a[href]').attr("href", "http://wiki.open-roberta.org");
        }, function() {
            setToken($('#singleModalInput').val().toUpperCase());
        }, function() {
            $('#singleModalInput').removeClass('capitalLetters');
        }, {
            rules : {
                singleModalInput : {
                    required : true,
                    minlength : 8,
                    maxlength : 8
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                singleModalInput : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    minlength : Blockly.Msg["VALIDATION_TOKEN_LENGTH"],
                    maxlength : Blockly.Msg["VALIDATION_TOKEN_LENGTH"]
                }
            }
        });
    }
    exports.showSetTokenModal = showSetTokenModal;

    function showScanModal() {
        if ($('#show-available-connections').is(':visible')) {
            return;
        }
        $('#connectionsTable').bootstrapTable('removeAll');
        WEBVIEW_C.jsToAppInterface({
            'target' : GUISTATE_C.getRobot(),
            'type' : 'startScan'
        });
        $('#show-available-connections').modal('show');
    }
    exports.showScanModal = showScanModal;

    function showListModal() {
        UTIL.showSingleListModal(function() {
            $('#single-modal-list h3').text(Blockly.Msg["MENU_CONNECT"]);
            $('#single-modal-list label').text(Blockly.Msg["POPUP_VALUE"]);
            $('#single-modal-list a[href]').text(Blockly.Msg["POPUP_STARTUP_HELP"]);
            $('#single-modal-list a[href]').attr("href", "http://wiki.open-roberta.org");
        }, function() {
            //console.log(document.getElementById("singleModalListInput").value);
            setPort(document.getElementById("singleModalListInput").value);
        }, function() {
        });
    }
    exports.showListModal = showListModal;

    /**
     * Show robot info
     */
    function showRobotInfo() {
        if (GUISTATE_C.isRobotConnected()) {
            $("#robotName").text(GUISTATE_C.getRobotName());
            $("#robotSystem").text(GUISTATE_C.getRobotFWName());
            if (GUISTATE_C.getRobotState() === "wait") {
                $("#robotStateWait").css('display', 'inline');
                $("#robotStateDisconnected").css('display', 'none');
                $("#robotStateBusy").css('display', 'none');
            } else if (GUISTATE_C.getRobotState() === "busy") {
                $("#robotStateWait").css('display', 'none');
                $("#robotStateDisconnected").css('display', 'none');
                $("#robotStateBusy").css('display', 'inline');
            } else {
                $("#robotStateWait").css('display', 'none');
                $("#robotStateDisconnected").css('display', 'inline');
                $("#robotStateBusy").css('display', 'none');
            }
            if (GUISTATE_C.getLanguage() == 'EN') {
                $("#robotBattery").text(GUISTATE_C.getRobotBattery() + ' V');
            } else {
                $("#robotBattery").text(GUISTATE_C.getRobotBattery().toString().replace(".", ",") + ' V');
            }
            var robotWait = parseInt(GUISTATE_C.getRobotTime(), 10);
            if (robotWait < 1000) {
                $("#robotWait").text(robotWait + ' ms');
            } else {
                $("#robotWait").text(Math.round(robotWait / 1000) + ' s');
            }
            $("#show-robot-info").modal("show");
        } else {
            MSG.displayMessage("ORA_ROBOT_NOT_CONNECTED", "POPUP", "");
        }
    }
    exports.showRobotInfo = showRobotInfo;

    /**
     * Show WLAN credentials form to save them for further REST calls.
     */
    function showWlanForm() {
        $("#menu-wlan").modal('show');
    }
    exports.showWlanForm = showWlanForm;

    /**
     * Handle firmware conflict between server and robot
     */
    function handleFirmwareConflict(updateInfo, robotServerVersion) {
        if (updateInfo < 0) {
            LOG.info("The firmware version '" + robotServerVersion + "' on the server is newer than the firmware version '" + GUISTATE_C.getRobotVersion()
                    + "' on the robot");
            $("#confirmUpdateFirmware").modal('show');
            return true;
        } else if (updateInfo > 0) {
            LOG.info("The firmware version '" + robotServerVersion + "' on the server is older than the firmware version '" + GUISTATE_C.getRobotVersion()
                    + "' on the robot");
            MSG.displayMessage("MESSAGE_FIRMWARE_ERROR", "POPUP", "");
            return true;
        }
        return false;
    }
    exports.handleFirmwareConflict = handleFirmwareConflict;

    /**
     * Update robot firmware
     */
    function updateFirmware() {
        ROBOT.updateFirmware(function(result) {
            GUISTATE_C.setState(result);
            if (result.rc === "ok") {
                MSG.displayMessage("MESSAGE_RESTART_ROBOT", "POPUP", "");
            } else {
                MSG.displayInformation(result, "", result.message, GUISTATE_C.getRobotFWName());
            }
        });
    }
    exports.updateFirmware = updateFirmware;

    /**
     * Switch robot
     */
    function switchRobot(robot, opt_continue, opt_callback) {

        PROGRAM_C.SSID = null;
        PROGRAM_C.password = null;
        document.getElementById("wlanSsid").value = "";
        document.getElementById("wlanPassword").value = "";
        
        var further;
        // no need to ask for saving programs if you switch the robot in between a group
        if (typeof opt_continue === 'undefined' && GUISTATE_C.findGroup(robot) == GUISTATE_C.getRobotGroup()) {
            further = true;
        } else {
            further = opt_continue || false;
        }
        if (further || (GUISTATE_C.isProgramSaved() && GUISTATE_C.isConfigurationSaved())) {
            if (robot === GUISTATE_C.getRobot()) {
                typeof opt_callback === "function" && opt_callback();
                return;
            }
            ROBOT.setRobot(robot, function(result) {
                if (result.rc === "ok") {
                    if (GUISTATE_C.findGroup(robot) != GUISTATE_C.getRobotGroup()) {
                        GUISTATE_C.setRobot(robot, result);
                        CONFIGURATION_C.resetView();
                        PROGRAM_C.resetView();
                    } else {
                        GUISTATE_C.setRobot(robot, result);
                    }
                    CONFIGURATION_C.changeRobotSvg();
                    if (GUISTATE_C.getView() == 'tabConfList') {
                        $('#confList>.bootstrap-table').find('button[name="refresh"]').trigger('click');
                    }
                    if (GUISTATE_C.getView() == 'tabProgList') {
                        $('#progList>.bootstrap-table').find('button[name="refresh"]').trigger('click');
                    }
                    if ($('.rightMenuButton.rightActive')) {
                        $('.rightMenuButton.rightActive').trigger('click');
                    }
                    PROGCODE_C.setCodeLanguage(GUISTATE_C.getSourceCodeFileExtension());
                    CODEEDITOR_C.setCodeLanguage(GUISTATE_C.getSourceCodeFileExtension());
                    CODEEDITOR_C.resetScroll();
                    //TODO inform app if one is there
//                    WEBVIEW_C.jsToAppInterface({
//                        'target' : 'wedo',
//                        'op' : {'type''disconnect'
//                    });
                    typeof opt_callback === "function" ? opt_callback()
                            : MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getRobotRealName());

                } else {
                    MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getRobotRealName());
                }
            });
        } else {
            $('#show-message-confirm').one('shown.bs.modal', function(e) {
                $('#confirm').off();
                $('#confirm').on('click', function(e) {
                    e.preventDefault();
                    switchRobot(robot, true, opt_callback);
                });
                $('#confirmCancel').off();
                $('#confirmCancel').on('click', function(e) {
                    e.preventDefault();
                    $('.modal').modal('hide');
                });
            });
            if (GUISTATE_C.isUserLoggedIn()) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
            }
        }
    }
    exports.switchRobot = switchRobot;

});
