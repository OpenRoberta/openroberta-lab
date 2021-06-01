define(['exports', 'message', 'log', 'util', 'webots.simulation', 'simulation.simulation', 'simulation.constants', 'guiState.controller', 'tour.controller', 'program.controller', 'program.model',
    'blockly', 'jquery', 'jquery-validate'], function(exports, MSG, LOG, UTIL, NAOSIM, SIM, CONST, GUISTATE_C, TOUR_C, PROG_C, PROGRAM, Blockly, $) {

    const INITIAL_WIDTH = 0.5;
    var blocklyWorkspace;
    var debug = false;

    function init() {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        initEvents();
    }
    exports.init = init;

    function initEvents() {
        $('#simButton').off('click touchend');
        $('#simButton').onWrap('click touchend', function(event) {
            debug = false;
            // Workaround for IOS speech synthesis, speech must be triggered once by a button click explicitly before it can be used programmatically
            if (window.speechSynthesis && GUISTATE_C.getRobot().indexOf("ev3") !== -1) {
                window.speechSynthesis.speak(new SpeechSynthesisUtterance(""));
            }
            toggleSim();
            return false;
        });

        $('#simDebugButton').off('click touchend');
        $('#simDebugButton').onWrap('click touchend', function(event) {
            debug = true;
            // Workaround for IOS speech synthesis, speech must be triggered once by a button click explicitly before it can be used programmatically
            if (window.speechSynthesis && GUISTATE_C.getRobot().indexOf("ev3") !== -1) {
                window.speechSynthesis.speak(new SpeechSynthesisUtterance(""));
            }
            toggleSim();
            return false;
        });

        $('#simRobotModal').removeClass("modal-backdrop");

        $('#simStop').onWrap('click', function (event) {
            $('#simStop').addClass("disabled");
            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
            SIM.stopProgram();
        }, 'sim stop clicked');
        $('#simControl').onWrap('click', function (event) {
            event.stopPropagation();

            if (SIM.getNumRobots() <= 1) {
                if (SIM.getDebugMode()) {
                    toggleSimEvent(CONST.DEBUG_BREAKPOINT);
                } else {
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
                                if (SIM.getDebugMode()) {
                                    $('#simControl').addClass('typcn-media-play').removeClass('typcn-media-play-outline');
                                } else {
                                    $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
                                }
                                if (GUISTATE_C.hasWebotsSim()) {
                                    NAOSIM.run(result.javaScriptProgram);
                                } else {
                                    setTimeout(function() {
                                        SIM.setPause(false);
                                    }, 500);
                                    SIM.init([result], false, GUISTATE_C.getRobotGroup());
                                }
                            } else {
                                MSG.displayInformation(result, "", result.message, "");
                            }
                            PROG_C.reloadProgram(result);
                        });
                    } else {
                        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                        if (GUISTATE_C.hasWebotsSim()) {
                            NAOSIM.stopProgram();
                        } else {
                            SIM.stopProgram();
                        }
                    }
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
        }, 'sim start clicked');
        $('#simImport').onWrap('click', function(event) {
            SIM.importImage();
        }, 'simImport clicked');

        $('#simRobotModal').removeClass("modal-backdrop");

        $('.simInfo').onWrap('click', function(event) {
            SIM.setInfo();
        }, 'sim info clicked');

        $('#simRobot').onWrap('click', function(event) {
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
        }, 'sim show robot clicked');

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
        }, 'sim show values clicked');

        $('#simResetPose').onWrap('click', function(event) {
            if (GUISTATE_C.hasWebotsSim()) {
                NAOSIM.resetPose();
                return;
            }
            SIM.resetPose();
        }, 'sim reset pose clicked');

        $('#simControlStepInto').onWrap('click', function(event) {
            toggleSimEvent(CONST.DEBUG_STEP_INTO);

        }, 'sim step into clicked');

        $('#simControlStepOver').onWrap('click', function(event) {
            toggleSimEvent(CONST.DEBUG_STEP_OVER);
        }, 'sim step over clicked');

        $('#simAddObstacleRectangle').onWrap('click', function(event) {
            SIM.addObstacle("rectangle");
            event.stopPropagation();
        }, 'sim add rectangle obstacle clicked');

        $('#simAddObstacleTriangle').onWrap('click', function(event) {
            SIM.addObstacle("triangle");
        }, 'sim add triangle obstacle clicked');

        $('#simAddObstacleCircle').onWrap('click', function(event) {
            SIM.addObstacle("circle");
            event.stopPropagation();
        }, 'sim add circle obstacle clicked');

        $('#simAddAreaRectangle').onWrap('click', function(event) {
            SIM.addColorArea("rectangle");
            event.stopPropagation();
        }, 'sim add rectangle area clicked');

        $('#simAddAreaTriangle').onWrap('click', function(event) {
            SIM.addColorArea("triangle");
            event.stopPropagation();
        }, 'sim add triangle area clicked');

        $('#simAddAreaCircle').onWrap('click', function(event) {
            SIM.addColorArea("circle");
            event.stopPropagation();
        }, 'sim add circle area clicked');

        $('#simChangeObjectColor').onWrap('click', function(event) {
            if (!$('#simChangeObjectColor').hasClass("disabled")) {
                SIM.toggleColorPicker();
            }
        }, 'sim edit object clicked');

        $('#simDeleteObject').onWrap('click', function(event) {
            if (!$('#simDeleteObject').hasClass("disabled")) {
                SIM.deleteSelectedObject();
            }
        }, 'sim delete object clicked');

        $('#simDownloadConfig').onWrap('click', function(event) {
            var filename = GUISTATE_C.getProgramName() + '-sim_configuration.json';
            UTIL.download(filename, JSON.stringify(SIM.exportConfigData()));
            MSG.displayMessage("MENU_MESSAGE_DOWNLOAD", "TOAST", filename);
        }, 'sim download config clicked');

        $('#simUploadConfig').onWrap('click', function(event) {
            SIM.importConfigData();
        }, 'sim upload config clicked');

        $('#simScene').onWrap('click', function(event) {
            SIM.setBackground(-1, SIM.setBackground);
        }, "sim toggle background clicked");
    }

    function initSimulation(result) {
        SIM.init([result], true, GUISTATE_C.getRobotGroup());
        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
        if (SIM.getNumRobots() === 1 && debug) {
            $('#simStop, #simControlStepOver, #simControlStepInto').show();
            $('#simControl').attr('data-original-title', Blockly.Msg.MENU_DEBUG_STEP_BREAKPOINT_TOOLTIP);
            $('#simControl').addClass("blue");
            SIM.updateDebugMode(true);
        } else {
            $('#simStop, #simControlStepOver, #simControlStepInto').hide();
            $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
            $('#simControl').removeClass("blue");
            SIM.endDebugging();
        }
        if (TOUR_C.getInstance() && TOUR_C.getInstance().trigger) {
            TOUR_C.getInstance().trigger('startSim');
        }
        let name = debug ? "simDebug" : "sim";
        $('#blockly').openRightView("sim", INITIAL_WIDTH, name);
    }

    function initNaoSimulation(result) {
        NAOSIM.init(result.javaScriptProgram);
        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');

        $('#simStop, #simControlStepOver, #simControlStepInto').hide();
        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
        $('#simControl').removeClass("blue");

        $('#blockly').openRightView("sim", INITIAL_WIDTH, "sim");
    }

        function toggleSim() {
        if ($('.fromRight.rightActive').hasClass('shifting')) {
                return;
            }
            if (($('#simButton').hasClass('rightActive') && !debug) || ($('#simDebugButton').hasClass('rightActive') && debug)) {
            if (GUISTATE_C.hasWebotsSim()) {
                NAOSIM.disconnect();
            } else {
                SIM.cancel();
            }
            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play').removeClass('typcn-media-stop');
            $('#blockly').closeRightView(function() {
                $('.nav > li > ul > .robotType').removeClass('disabled');
                $('.' + GUISTATE_C.getRobot()).addClass('disabled');
            });
            $('#simStop, #simControlStepOver,#simControlStepInto').hide();
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
                    if (GUISTATE_C.hasWebotsSim()) {
                        initNaoSimulation(result);
                    } else {
                        initSimulation(result);
                    }
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
                $('#simControl').removeClass('typcn-media-play-outline').addClass('typcn-media-play');
                $('#simStop').removeClass('disabled');
            });
        } else if ($('#simControl').hasClass('typcn-media-play')) {
            SIM.setPause(false);
            SIM.interpreterAddEvent(event);
        } else {
            if ($('#simControl').hasClass('typcn-media-stop')) {
                    $('#simControl').addClass("blue").removeClass('typcn-media-stop');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_DEBUG_STEP_BREAKPOINT_TOOLTIP);
                    $('#simStop').show();
                }$('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
            SIM.stopProgram();
        }
    }

    function callbackOnTermination() {
        GUISTATE_C.setConnectionState("wait");
        blocklyWorkspace.robControls.switchToStart();
    }
});
