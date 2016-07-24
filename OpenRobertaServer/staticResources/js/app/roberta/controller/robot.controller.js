define([ 'exports', 'util', 'log', 'message', 'guiState.controller', 'rest.robot', 'program.controller', 'configuration.controller', 'jquery',
        'jquery-validate' ], function(exports, UTIL, LOG, MSG, guiStateController, ROBOT, programController, configurationController, $) {

    var $formSingleModal;

    /**
     * Initialize robot
     */
    function init() {

        var a = $.Deferred();
        $.when(ROBOT.setRobot(guiStateController.getRobot(), function(result) {
            if (result.rc == 'ok')
                guiStateController.setRobot(guiStateController.getRobot(), result, true);
        })).then(function() {
            initRobotForms;
            LOG.info('init robot forms');
            a.resolve();
        });
        return a;
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
                    guiState.robot.token = token;
                }
                MSG.displayInformation(result, "MESSAGE_ROBOT_CONNECTED", result.message, guiState.robot.name);
                setState(result);
                handleFirmwareConflict();
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
        if (guiState.robot.time) {
            $("#robotName").text(guiState.robot.name);
            $("#robotSystem").text(guiState.robot.fWName);
            if (guiState.robot.state === "wait") {
                $("#robotStateWait").css('display', 'inline');
                $("#robotStateDisconnected").css('display', 'none');
                $("#robotStateBusy").css('display', 'none');
            } else if (guiState.robot.state === "busy") {
                $("#robotStateWait").css('display', 'none');
                $("#robotStateDisconnected").css('display', 'none');
                $("#robotStateBusy").css('display', 'inline');
            } else {
                $("#robotStateWait").css('display', 'none');
                $("#robotStateDisconnected").css('display', 'inline');
                $("#robotStateBusy").css('display', 'none');
            }
            if (guiState.language == 'EN') {
                $("#robotBattery").text(guiState.robot.battery + ' V');
            } else {
                $("#robotBattery").text(guiState.robot.battery.toString().replace(".", ",") + ' V');
            }
            var robotWait = parseInt(guiState.robot.time, 10);
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
        var regex = '(.+\..+)\..+'; // get x.y from version x.y.z
        var mainversionServer = guiState.server.version.match(regex)[1];
        var mainversionRobot = guiState.robot.version.match(regex)[1];
        if (mainversionServer > mainversionRobot) {
            LOG.info("The firmware version '" + guiState.server.version + "' on the server is newer than the firmware version '" + guiState.robot.version
                    + "' on the robot");
            $("#confirmUpdateFirmware").modal('show');
            return true;
        } else if (mainversionServer < mainversionRobot) {
            LOG.info("The firmware version '" + guiState.server.version + "' on the server is older than the firmware version '" + guiState.robot.version
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
            setState(result);
            if (result.rc === "ok") {
                MSG.displayMessage("MESSAGE_RESTART_ROBOT", "POPUP", "");
                guiState.robot.state = 'disconnected';
                guiState.robot.name = '';
            } else {
                MSG.displayInformation(result, "", result.message, guiState.robot.fWName);
            }
        });
    }
    exports.updateFirmware = updateFirmware;

    /**
     * Switch robot
     */
    function switchRobot(robot, opt_continue) {
        if (robot === guiStateController.getRobot) {
            return;
        }
        var further = opt_continue || false;
        if (further || (guiStateController.isProgramSaved() && guiStateController.isConfSaved)) {
            ROBOT.setRobot(robot, function(result) {
                if (result.rc === "ok") {
                    guiStateController.setRobot(robot, result);
                    programController.resetView();
                    configurationController.resetView();
                } else {
                    alert('Robot not available');
                }
            });
        } else {
            $('#confirmContinue').data('type', 'switchRobot');
            $('#confirmContinue').data('robot', robot);
            if (guiStateController.isUserLoggedIn) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
            }
        }
    }
    exports.switchRobot = switchRobot;

    //ROBERTA_NAVIGATION.initNavigation();
//        ROBOT.setRobot(guiState.robot, function(result) {
//            UTIL.response(result);
//            if (result.rc === "ok") {
//                ROBERTA_BRICK_CONFIGURATION.setConfiguration("EV3basis");
//                ROBERTA_TOOLBOX.loadToolbox(guiState.toolbox);
//                $('#blocklyDiv').removeClass('simBackground');
//                $('#menuEv3').parent().addClass('disabled');
//                $('#menuSim').parent().removeClass('disabled');
//                $('#menuConnect').parent().removeClass('disabled');
//                $('#iconDisplayRobotState').removeClass('typcn-Roberta');
//                $('#iconDisplayRobotState').addClass('typcn-ev3');
//                $('#menuShowCode').parent().removeClass('disabled');
//            }
});
