define([ 'exports', 'util', 'log', 'message', 'guiState.controller', 'robot.model', 'program.controller', 'configuration.controller', 'jquery', 'jquery-validate' ], function(exports, UTIL, LOG, MSG, GUISTATE_C, ROBOT, PROGRAM_C, CONFIGURATION_C, $) {

    var $formSingleModal;

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
            LOG.info('init robot forms');
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
                    MSG.displayInformation(result, "MESSAGE_ROBOT_CONNECTED", result.message, GUISTATE_C.getRobotName());
                    handleFirmwareConflict();
                } else {
                    if (result.message === 'ORA_TOKEN_SET_ERROR_WRONG_ROBOTTYPE') {
                        $('.modal').modal('hide');
                    }
                }
                UTIL.response(result);
            });
        }
    }

    function initRobotForms() {
        $('#iconDisplayRobotState').onWrap('click', function() {
            showRobotInfo();
        }, 'icon robot click');

        $('#doUpdateFirmware').onWrap('click', function() {
            $('#set-token').modal('hide');
            $('#confirmUpdateFirmware').modal('hide');
            updateFirmware();
        }, 'update firmware of robot');

        $formSingleModal = $('#single-modal-form');
    }

    function showSetTokenModal() {
        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg["MENU_CONNECT"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_VALUE"]);
            $('#singleModalInput').addClass('capitalLetters');
            $('#single-modal a[href]').text(Blockly.Msg["POPUP_STARTUP_HELP"]);
            $('#single-modal a[href]').attr("href", "https://wiki.open-roberta.org");
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
     * Handle firmware conflict between server and robot
     */
    function handleFirmwareConflict() {
        if (GUISTATE_C.getRobotFWName() != "lejos") {
            return false;
        }
        var regex = '^([^\.]+\.[^\.]+)[\..+]*'; // get x.y from version x.y.z
        var mainversionServer = GUISTATE_C.getServerVersion().match(regex)[1];
        var mainversionRobot = GUISTATE_C.getRobotVersion().match(regex)[1];
        if (mainversionServer > mainversionRobot) {
            LOG.info("The firmware version '" + GUISTATE_C.getServerVersion() + "' on the server is newer than the firmware version '" + GUISTATE_C.getRobotVersion() + "' on the robot");
            $("#confirmUpdateFirmware").modal('show');
            return true;
        } else if (mainversionServer < mainversionRobot) {
            LOG.info("The firmware version '" + GUISTATE_C.getServerVersion() + "' on the server is older than the firmware version '" + GUISTATE_C.getRobotVersion() + "' on the robot");
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
//                guiState.robot.state = 'disconnected';
//                guiState.robot.name = '';
            } else {
                MSG.displayInformation(result, "", result.message, GUISTATE_C.getRobotFWName());
            }
        });
    }
    exports.updateFirmware = updateFirmware;

    /**
     * Switch robot
     */
    function switchRobot(robot, opt_continue) {
        if (robot === GUISTATE_C.getRobot) {
            return;
        }
        var further = opt_continue || false;
        if (further || (GUISTATE_C.isProgramSaved() && GUISTATE_C.isConfigurationSaved())) {
            ROBOT.setRobot(robot, function(result) {
                if (result.rc === "ok") {
                    GUISTATE_C.setRobot(robot, result);
                    PROGRAM_C.resetView();
                    CONFIGURATION_C.resetView();
                    if (GUISTATE_C.getView() == 'tabConfList') {
                        $('#confList>.bootstrap-table').find('button[name="refresh"]').trigger('click');
                    }
                    if (GUISTATE_C.getView() == 'tabProgList') {
                        $('#progList>.bootstrap-table').find('button[name="refresh"]').trigger('click');
                    }
                } else {
                    alert('Robot not available');
                }
            });
        } else {
            $('#confirmContinue').data('type', 'switchRobot');
            $('#confirmContinue').data('robot', robot);
            if (GUISTATE_C.isUserLoggedIn) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
            }
        }
    }
    exports.switchRobot = switchRobot;
});
