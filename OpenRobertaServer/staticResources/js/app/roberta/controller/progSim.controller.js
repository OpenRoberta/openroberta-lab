define(['exports', 'message', 'log', 'util', 'simulation.simulation', 'simulation.constants', 'guiState.controller', 'tour.controller', 'program.controller', 'program.model',
    'blockly', 'jquery', 'jquery-validate'], function(exports, MSG, LOG, UTIL, SIM, CONST, GUISTATE_C, TOUR_C, PROG_C, PROGRAM, Blockly, $) {

        const INITIAL_WIDTH = 0.5;
        var blocklyWorkspace;

        function init() {
            blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
            initEvents();
        }
        exports.init = init;

        function initEvents() {
            $('#simButton').off('click touchend');
            $('#simButton').on('click touchend', function(event) {
                // Workaround for IOS speech synthesis, speech must be triggered once by a button click explicitly before it can be used programmatically
                if (window.speechSynthesis && GUISTATE_C.getRobot().indexOf("ev3") !== -1) {
                    window.speechSynthesis.speak(new SpeechSynthesisUtterance(""));
                }

                toggleSim();
                return false;
            });

            $('#simRobotModal').removeClass("modal-backdrop");
            $('#simControl').onWrap('click', function(event) {
                if (SIM.getNumRobots() == 1) {
                    if ($('#simControl').hasClass('typcn-media-play-outline')) {
                        Blockly.hideChaff();
                        var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
                        var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);

                        var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
                        var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
                        var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

                        var language = GUISTATE_C.getLanguage();

                        PROGRAM.runInSim(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
                            if (result.rc == "ok") {
                                MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", GUISTATE_C.getProgramName());
                                $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
                                setTimeout(function() {
                                    SIM.setPause(false);
                                }, 500);
                                SIM.init([result], false, GUISTATE_C.getRobotGroup());
                            } else {
                                MSG.displayInformation(result, "", result.message, "");
                            }
                            PROG_C.reloadProgram(result);
                        });
                    } else {
                        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                        SIM.stopProgram();

                    }
                } else {
                    if ($('#simControl').hasClass('typcn-media-play-outline')) {
                        MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", "multiple simulation");
                        $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
                        SIM.run(false, GUISTATE_C.getRobotGroup());
                        setTimeout(function() {
                            SIM.setPause(false);
                        }, 500);
                    } else {
                        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                        SIM.stopProgram();
                    }
                }
            });
            $('#simImport').onWrap('click', function(event) {
                SIM.importImage();
            }, 'simImport clicked');

            $('#simButtonsCollapse').collapse({
                'toggle': false
            });
            $('#simRobotModal').removeClass("modal-backdrop");

            $('.simInfo').onWrap('click', function(event) {
                SIM.setInfo();
                $("#simButtonsCollapse").collapse('hide');
            }, 'simInfo clicked');

            $('#simRobot').on('click', function(event) {
                $("#simRobotModal").modal("toggle");
                var robot = GUISTATE_C.getRobot();
                var position = $("#simDiv").position();
                position.top += 12;
                if (robot == 'calliope' || robot == 'microbit') {
                    position.left = $("#blocklyDiv").width() + 12;
                    $("#simRobotModal").css({
                        top: position.top,
                        left: position.left
                    });
                } else {
                    position.left += 48;
                    $("#simRobotModal").css({
                        top: position.top,
                        left: position.left
                    });
                }
                $('#simRobotModal').draggable();
                $("#simButtonsCollapse").collapse('hide');
            });

            $('#simValues').onWrap('click', function(event) {
                $("#simValuesModal").modal("toggle");
                var position = $("#simDiv").position();
                position.top += 12;
                $("#simValuesModal").css({
                    top: position.top,
                    right: 12,
                    left: 'initial',
                    bottom: 'inherit'
                });
                $('#simValuesModal').draggable();

                $("#simButtonsCollapse").collapse('hide');
            }, 'simValues clicked');

            $('#simResetPose').onWrap('click', function(event) {
                SIM.resetPose();
            }, 'simResetPose clicked');

            $('#debugMode').onWrap('click', function(event) {
                if ($('#debugMode').attr('data-original-title') === Blockly.Msg.MENU_DEBUG_START_TOOLTIP) {
                    $('#debugMode').attr('data-original-title', Blockly.Msg.MENU_DEBUG_STOP_TOOLTIP);
                    $('#simControlBreakPoint,#simControlStepOver,#simControlStepInto,#simVariables').show();
                    SIM.updateDebugMode(true);

                } else {
                    $('#debugMode').attr('data-original-title', Blockly.Msg.MENU_DEBUG_START_TOOLTIP);
                    $('#simControlBreakPoint,#simControlStepOver,#simControlStepInto,#simVariables').hide();
                    SIM.endDebugging();
                }

            }, 'debugMode clicked');


            $('#simControlBreakPoint').onWrap('click', function(event) {
                toggleSimEvent(CONST.DEBUG_BREAKPOINT);
            }, 'simControlBreakPoint clicked');

            $('#simControlStepInto').onWrap('click', function(event) {
                toggleSimEvent(CONST.DEBUG_STEP_INTO);

            }, 'simControlStepInto clicked');

            $('#simControlStepOver').onWrap('click', function(event) {
                toggleSimEvent(CONST.DEBUG_STEP_OVER);
            }, 'simControlStepOver clicked');

            $('#simVariables').onWrap('click', function(event) {
                $("#simVariablesModal").modal("toggle");
                var position = $("#simDiv").position();
                position.top += 12;
                $("#simVariablesModal").css({
                    top: position.top,
                    right: 12,
                    left: 'initial',
                    bottom: 'inherit'
                });
                $('#simVariablesModal').draggable();

                $("#simButtonsCollapse").collapse('hide');
            }, 'simVariables clicked');
        }

        function toggleSim() {
            if ($('#simButton').hasClass('rightActive')) {
                SIM.cancel();
                $(".sim").addClass('hide');
                $("#simButtonsCollapse").collapse('hide');
                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                $('#debugMode').attr('data-original-title', Blockly.Msg.MENU_DEBUG_START_TOOLTIP).hide();
                $('#blockly').closeRightView(function() {
                    $('#menuSim').parent().addClass('disabled');
                    $('.nav > li > ul > .robotType').removeClass('disabled');
                    $('.' + GUISTATE_C.getRobot()).addClass('disabled');
                });
                SIM.endDebugging();
            } else {
                var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
                var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
                var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
                var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
                var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
                var language = GUISTATE_C.getLanguage();

                PROGRAM.runInSim(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
                    if (result.rc == "ok") {
                        SIM.init([result], true, GUISTATE_C.getRobotGroup())
                        $(".sim").removeClass('hide');
                        $('#simButtonsCollapse').collapse({
                            'toggle': false
                        });
                        if (SIM.getNumRobots() === 1) {
                            $('#debugMode').show();
                            $('#simControlBreakPoint, #simControlStepOver, #simControlStepInto, #simVariables').hide();
                        } 
                        if (TOUR_C.getInstance() && TOUR_C.getInstance().trigger) {
                            TOUR_C.getInstance().trigger('startSim');
                        }
                        $('#blockly').openRightView('sim', INITIAL_WIDTH);
                    } else {
                        MSG.displayInformation(result, "", result.message, "");
                    }
                    PROG_C.reloadProgram(result);
                });
            }
        }

        function toggleSimEvent(event) {
            if ($('#simControl').hasClass('typcn-media-play-outline')) {
                var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
                var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
                var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
                var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
                var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
                var language = GUISTATE_C.getLanguage();

                PROGRAM.runInSim(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
                    if (result.rc == "ok") {
                        setTimeout(function() {
                            SIM.setPause(false);
                            SIM.interpreterAddEvent(event);
                        }, 500);
                        SIM.init([result], false, GUISTATE_C.getRobotGroup());
                    }
                    $('#simControl').removeClass('typcn-media-play-outline').addClass('typcn-media-stop');
                });
            } else {
                SIM.setPause(false);
                SIM.interpreterAddEvent(event);
            }
        }

        function callbackOnTermination() {
            GUISTATE_C.setConnectionState("wait");
            blocklyWorkspace.robControls.switchToStart();
        }
    });
