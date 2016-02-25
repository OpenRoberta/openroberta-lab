define([ 'exports', 'util', 'message', 'roberta.brick-configuration', 'roberta.user-state', 'roberta.toolbox', 'rest.robot', 'jquery', 'jquery-validate' ],
        function(exports, UTIL, MSG, ROBERTA_BRICK_CONFIGURATION, userState, ROBERTA_TOOLBOX, ROBOT, $) {

            var $formSingleModal;

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
                            userState.token = token;
                        }
                        displayInformation(result, "MESSAGE_ROBOT_CONNECTED", result.message, userState.robotName);
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
            exports.initRobotForms = initRobotForms;

            function showSetTokenModal() {
                UTIL.showSingleModal(function() {
                    $('#singleModalInput').attr('type', 'text');
                    $('#single-modal h3').text(Blockly.Msg["MENU_CONNECT"]);
                    $('#single-modal label').text(Blockly.Msg["POPUP_VALUE"]);
                }, function() {
                    setToken($('#singleModalInput').val());
                }, function() {

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
                            required : jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"]),
                            minlength : jQuery.validator.format(Blockly.Msg["VALIDATION_TOKEN_LENGTH"]),
                            maxlength : jQuery.validator.format(Blockly.Msg["VALIDATION_TOKEN_LENGTH"])
                        }
                    }
                });
            }
            exports.showSetTokenModal = showSetTokenModal;

            /**
             * Show robot info
             */
            function showRobotInfo() {
                if (userState.robotName) {
                    if (userState.robot === "oraSim") {
                        $("#robotName").text("ORSim");
                    } else {
                        $("#robotName").text(userState.robotName);
                    }
                    if (userState.robotState === "wait") {
                        $("#robotStateWait").css('display', 'inline');
                        $("#robotStateDisconnected").css('display', 'none');
                        $("#robotStateBusy").css('display', 'none');
                    } else if (userState.robotState === "busy") {
                        $("#robotStateWait").css('display', 'none');
                        $("#robotStateDisconnected").css('display', 'none');
                        $("#robotStateBusy").css('display', 'inline');
                    } else {
                        $("#robotStateWait").css('display', 'none');
                        $("#robotStateDisconnected").css('display', 'inline');
                        $("#robotStateBusy").css('display', 'none');
                    }
                    if (userState.language == 'EN') {
                        $("#robotBattery").text(userState.robotBattery + ' V');
                    } else {
                        $("#robotBattery").text(userState.robotBattery.toString().replace(".", ",") + ' V');
                    }
                    var robotWait = parseInt(userState.robotWait, 10);
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
                var mainversionServer = userState.serverVersion.match(regex)[1];
                var mainversionRobot = userState.robotVersion.match(regex)[1];
                if (mainversionServer > mainversionRobot) {
                    LOG.info("The firmware version '" + userState.serverVersion + "' on the server is newer than the firmware version '"
                            + userState.robotVersion + "' on the robot");
                    $("#confirmUpdateFirmware").modal('show');
                    return true;
                } else if (mainversionServer < mainversionRobot) {
                    LOG.info("The firmware version '" + userState.serverVersion + "' on the server is older than the firmware version '"
                            + userState.robotVersion + "' on the robot");
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
                        userState.robotState = 'disconnected';
                    } else {
                        MSG.displayInformation(result, "", result.message, userState.robotFirmware);
                    }
                });
            }
            exports.updateFirmware = updateFirmware;

            /**
             * Set robot state
             * 
             * @param {result}
             *            result of server call
             */
            function setState(result) {
                if (result['version']) {
                    userState.version = result.version;
                }
                if (result['server.version']) {
                    userState.serverVersion = result['server.version'];
                }
                if (result['robot.version']) {
                    userState.robotVersion = result['robot.version'];
                }
                if (result['robot.wait'] != undefined) {
                    userState.robotWait = result['robot.wait'];
                } else {
                    userState.robotWait = '';
                }
                if (result['robot.battery'] != undefined) {
                    userState.robotBattery = result['robot.battery'];
                } else {
                    userState.robotBattery = '';
                }
                if (result['robot.name'] != undefined) {
                    userState.robotName = result['robot.name'];
                } else {
                    userState.robotName = '';
                }
                if (result['robot.state'] != undefined) {
                    userState.robotState = result['robot.state'];
                } else {
                    userState.robotState = '';
                }
                if (result['robot.sensorvalues'] != undefined) {
                    userState.sensorValues = result['robot.sensorvalues'];
                } else {
                    userState.sensorValues = '';
                }
				if (result['robot.nepoexitvalue'] != undefined) {
					userState.nepoExitValue = result['robot.nepoexitvalue'];
				} else {
					userState.nepoExitValue = 0;
				}
                if (userState.accountName) {
                    $('#iconDisplayLogin').removeClass('error');
                    $('#iconDisplayLogin').addClass('ok');
                } else {
                    $('#iconDisplayLogin').removeClass('ok');
                    $('#iconDisplayLogin').addClass('error');
                }
                if (userState.robotState === 'wait') {
                    $('#iconDisplayRobotState').removeClass('error');
                    $('#iconDisplayRobotState').removeClass('busy');
                    $('#iconDisplayRobotState').addClass('wait');
                    if (Blockly.hasStartButton) {
                        // Blockly.getMainWorkspace().startButton.enable();
                    }
                } else if (userState.robotState === 'busy') {
                    $('#iconDisplayRobotState').removeClass('wait');
                    $('#iconDisplayRobotState').removeClass('error');
                    $('#iconDisplayRobotState').addClass('busy');
                } else {
                    $('#iconDisplayRobotState').removeClass('busy');
                    $('#iconDisplayRobotState').removeClass('wait');
                    $('#iconDisplayRobotState').addClass('error');
                }

            }
            exports.setState = setState;

            /**
             * Init robot
             */
            function initRobot() {
                ROBOT.setRobot(userState.robot, function(result) {
                    UTIL.response(result);
                    if (result.rc === "ok") {
                        ROBERTA_BRICK_CONFIGURATION.setConfiguration("EV3basis");
                        ROBERTA_TOOLBOX.loadToolbox(userState.toolbox);
                        $('#blocklyDiv').removeClass('simBackground');
                        $('#menuEv3').parent().addClass('disabled');
                        $('#menuSim').parent().removeClass('disabled');
                        $('#menuConnect').parent().removeClass('disabled');
                        $('#iconDisplayRobotState').removeClass('typcn-Roberta');
                        $('#iconDisplayRobotState').addClass('typcn-ev3');
                        $('#menuShowCode').parent().removeClass('disabled');
                    }
                });
            }

            exports.initRobot = initRobot;

        });
