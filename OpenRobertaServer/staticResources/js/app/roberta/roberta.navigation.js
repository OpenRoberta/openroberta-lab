define([ 'exports', 'util', 'message', 'comm', 'rest.robot', 'rest.program', 'rest.configuration', 'roberta.toolbox', 'roberta.robot', 'roberta.user',
        'roberta.brick-configuration', 'roberta.user-state', 'roberta.program', 'roberta.brickly', 'simulation.simulation', 'jquery', 'blocks', 'jquery-ui' ],
        function(exports, UTIL, MSG, COMM, ROBOT, PROGRAM, CONFIGURATION, ROBERTA_TOOLBOX, ROBERTA_ROBOT, ROBERTA_USER, ROBERTA_BRICK_CONFIGURATION, userState,
                ROBERTA_PROGRAM, BRICKLY, SIM, $, Blockly) {

            var bricklyActive = false;

            function initNavigation() {
                $('#backLogging').onWrap('click', function() {
                    activateProgConfigMenu();
                    if (bricklyActive) {
                        switchToBrickly();
                    } else {
                        switchToBlockly();
                    }
                });

                $('#backConfiguration').onWrap('click', function() {
                    activateProgConfigMenu();
                    switchToBrickly();
                });

                $('#backProgram').onWrap('click', function() {
                    activateProgConfigMenu();
                    switchToBlockly();
                });

            }
            exports.initNavigation = initNavigation;

            /**
             * Switch to Blockly tab
             */
            function switchToBlockly() {
                workspace = ROBERTA_PROGRAM.getBlocklyWorkspace();

                $('#brickly').css('display', 'none');
                $('#simConfiguration').css('display', 'none');
                $('#tabBlockly').click();
                Blockly.svgResize(workspace);
                workspace.setVisible(true);
                bricklyActive = false;
            }
            exports.switchToBlockly = switchToBlockly;

            /**
             * Switch to Brickly tab
             */
            function switchToBrickly() {
                if (userState.robot === "oraSim") { //simulation has no configuration, TODO add flag to robot in database
                    $('#simConfiguration').css('display', 'block');
                } else {
                    $('#brickly').css('display', 'inline');
                    //$('#tabs').css('display', 'none');
                    // This is only for firefox necessary, should be removed with new Blockly
                    BRICKLY.getBricklyWorkspace().render();
                    BRICKLY.loadToolbox();
                }
                $('#tabBrickly').click();
                bricklyActive = true;
            }
            exports.switchToBrickly = switchToBrickly;

            function setHeadNavigationMenuState(state) {
                $('.nav > li > ul > .login, .logout').removeClass('disabled');
                if (state === 'login') {
                    $('.nav > li > ul > .login').addClass('disabled');
                } else if (state === 'logout') {
                    $('.nav > li > ul > .logout').addClass('disabled');
                }
            }
            exports.setHeadNavigationMenuState = setHeadNavigationMenuState;

            /**
             * Activate program and config menu when in frames that hides
             * blockly/brickly.
             */
            function activateProgConfigMenu() {
                $('#head-navigation-program-edit > ul > li').removeClass('disabled');
                $('#head-navigation-configuration-edit > ul > li').removeClass('disabled');
                setHeadNavigationMenuState(userState.id === -1 ? 'logout' : 'login');
                if (userState.programSaved) {
                    $('#menuSaveProg').parent().removeClass('login');
                    $('#menuSaveProg').parent().removeClass('disabled');
                    ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.enable('saveProgram');
                }
                if (userState.configurationSaved) {
                    $('#menuSaveConfig').parent().removeClass('login');
                    $('#menuSaveConfig').parent().removeClass('disabled');
                    BRICKLY.getBricklyWorkspace().robControls.enable('saveProgram');
                }
            }
            exports.activateProgConfigMenu = activateProgConfigMenu;

            /**
             * Switch robot
             */
            function switchRobot(robot) {
                if (robot === userState.robot) {
                    return;
                }
                ROBOT.setRobot(robot, function(result) {
                    if (result.rc === "ok") {
                        userState.robot = robot;
                        ROBERTA_ROBOT.setState(result);
                        if (robot === "ev3") {
                            ROBERTA_BRICK_CONFIGURATION.setConfiguration("EV3basis");
                            $('#blocklyDiv').removeClass('simBackground');
                            $('#menuEv3').parent().addClass('disabled');
                            $('#menuSim').parent().removeClass('disabled');
                            $('#menuConnect').parent().removeClass('disabled');
                            $('#iconDisplayRobotState').removeClass('typcn-Roberta');
                            $('#iconDisplayRobotState').addClass('typcn-ev3');
                            $('#menuShowCode').parent().removeClass('disabled');
                            ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.enable('showCode');
                            BRICKLY.loadToolboxAndConfiguration();
                        } else if (robot === "nxt") {
                            // coming soon
                        }
                        ROBERTA_TOOLBOX.loadToolbox(userState.toolbox);
                    }
                });
            }

            /**
             * Deactivate program and config menu.
             */
            function deactivateProgConfigMenu() {
                $('#head-navigation-program-edit > ul > li').addClass('disabled');
                $('#head-navigation-configuration-edit > ul > li').addClass('disabled');
            }

            /**
             * Initialize the navigation bar in the head of the page
             */
            function initHeadNavigation() {
                $('.navbar-fixed-top').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
                    $('.modal').modal('hide'); // close all opened popups
                    var domId = event.target.id;
                    if (domId === 'menuRunProg') { //  Submenu 'Program'   
                        ROBERTA_PROGRAM.runOnBrick();
                    } else if (domId === 'menuRunSim') { //  Submenu 'Program'
                        ROBERTA_PROGRAM.runInSim()();
                    } else if (domId === 'menuCheckProg') { //  Submenu 'Program'
                        ROBERTA_PROGRAM.checkProgram();
                    } else if (domId === 'menuNewProg') { //  Submenu 'Program'
                        ROBERTA_PROGRAM.newProgram();
                    } else if (domId === 'menuListProg') { //  Submenu 'Program'
                        deactivateProgConfigMenu();
                        $('#tabListing').click();
                    } else if (domId === 'menuSaveProg') { //  Submenu 'Program'
                        ROBERTA_PROGRAM.save();
                    } else if (domId === 'menuSaveAsProg') { //  Submenu 'Program'
                        ROBERTA_PROGRAM.showSaveAsModal();
                    } else if (domId === 'menuShowCode') { //  Submenu 'Program'
                        ROBERTA_PROGRAM.showCode();
                        // $("#navbarCollapse").collapse('hide');
                    } else if (domId === 'menuToolboxBeginner') { // Submenu 'Program'
                        ROBERTA_TOOLBOX.loadToolbox('beginner');
                    } else if (domId === 'menuToolboxExpert') { // Submenu 'Program'
                        ROBERTA_TOOLBOX.loadToolbox('expert');
                    } else if (domId === 'menuCheckConfig') { //  Submenu 'Configuration'
                        MSG.displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP", "");
                    } else if (domId === 'menuNewConfig') { //  Submenu 'Configuration'
                        ROBERTA_BRICK_CONFIGURATION.setConfiguration("EV3basis");//            
                        BRICKLY.initConfigurationEnvironment();
                        $('#menuSaveConfig').parent().addClass('disabled');
                        BRICKLY.getBricklyWorkspace().robControls.disable('saveProgram');
                        BRICKLY.initConfigurationEnvironment();
                    } else if (domId === 'menuListConfig') { //  Submenu 'Configuration'
                        deactivateProgConfigMenu();
                        $('#tabs').css('display', 'inline');
                        $('#brickly').css('display', 'none');
                        $('#simConfiguration').css('display', 'none');
                        $('#tabConfigurationListing').click();
                    } else if (domId === 'menuSaveConfig') { //  Submenu 'Configuration'
                        ROBERTA_BRICK_CONFIGURATION.save();
                    } else if (domId === 'menuSaveAsConfig') { //  Submenu 'Configuration'
                        ROBERTA_BRICK_CONFIGURATION.showSaveAsModal();
                    } else if (domId === 'menuEv3') { // Submenu 'Robot'
                        alert('yes');
//                if (ROBERTA_PROGRAM.newProgram()) {
//                    switchRobot('ev3');
//                }
                    } else if (domId === 'menuNxt') { // Submenu 'Robot'
                        alert('NXT is coming soon!')
                    } else if (domId === 'menuConnect') { // Submenu 'Robot'
                        $('#buttonCancelFirmwareUpdate').css('display', 'inline');
                        $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
                        ROBERTA_ROBOT.showSetTokenModal();
                    } else if (domId === 'menuRobotInfo') { // Submenu 'Robot'
                        ROBERTA_ROBOT.showRobotInfo();
                    } else if (domId === 'menuGeneral') { // Submenu 'Help'
                        window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/BIAM");
                    } else if (domId === 'menuEV3conf') { // Submenu 'Help'
                        window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/RIAd");
                    } else if (domId === 'menuProgramming') { // Submenu 'Help'
                        window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/CwA-/");
                    } else if (domId === 'menuFaq') { // Submenu 'Help'
                        window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/BoAd");
                    } else if (domId === 'menuShowRelease') { // Submenu 'Help'
                        $("#show-release").modal("show");
                    } else if (domId === 'menuStateInfo') { // Submenu 'Help'
                        ROBERTA_USER.showUserInfo();
                    } else if (domId === 'menuAbout') { // Submenu 'Help'
                        $("#version").text(userState.version);
                        $("#show-about").modal("show");
                    } else if (domId === 'menuLogging') { // Submenu 'Help'
                        deactivateProgConfigMenu();
                        $('#tabs').css('display', 'inline');
                        $('#brickly').css('display', 'none');
                        $('#simConfiguration').css('display', 'none');
                        $('#tabLogging').click();
                    } else if (domId === 'menuLogin') { // Submenu 'Login'
                        ROBERTA_USER.showLoginForm();
                    } else if (domId === 'menuLogout') { // Submenu 'Login'
                        ROBERTA_USER.logout();
                    } else if (domId === 'menuNewUser') { // Submenu 'Login'
                        $("#register-user").modal('show');
                    } else if (domId === 'menuChangeUser') { // Submenu 'Login'
                        ROBERTA_USER.showUserDataForm();
                    } else if (domId === 'menuDeleteUser') { // Submenu 'Login'
                        ROBERTA_USER.showDeleteUserModal();
                    }
                    return false;
                }, 'head navigation menu item clicked');

                $('.navbar-fixed-top .navbar-nav').onWrap('click', 'li:not(.disabled) a', function(event) {
                    var domId = event.target.id;
                    if (domId === 'menuTabProgram') {
                        if ($('#tabSimulation').hasClass('tabClicked')) {
                            $('.scroller-left').click();
                        }
                        $('.scroller-left').click();
                        $('#tabProgram').click();
                    } else if (domId === 'menuTabConfiguration') {
                        if ($('#tabProgram').hasClass('tabClicked')) {
                            $('.scroller-right').click();
                        } else if ($('#tabConfiguration').hasClass('tabClicked')) {
                            $('.scroller-right').click();
                        }
                        $('#tabConfiguration').click();
                    }
                    return false;
                })

                // Close submenu on mouseleave
                $('.navbar-fixed-top').onWrap('mouseleave', function(event) {
                    $('.navbar-fixed-top .dropdown').removeClass('open');
                });

                $('#imgLogo, #imgBeta').onWrap('click', function() {
                    window.open('http://open-roberta.org');
                }, 'logo was clicked');

                $('#beta').onWrap('click', function() {
                    window.open('http://open-roberta.org');
                }, 'beta logo was clicked');

                $('#tabProgram').onWrap('click', function() {
                    activateProgConfigMenu();
                    $('#tabProgram').addClass('tabClicked');
                    $('#tabConfiguration').removeClass('tabClicked');
                    $('#tabSimulation').removeClass('tabClicked');
                    $('#head-navigation-program-edit').css('display', 'inline');
                    $('#head-navigation-configuration-edit').css('display', 'none');
                    $('#menuTabProgram').parent().addClass('disabled');
                    $('#menuTabConfiguration').parent().removeClass('disabled');
                    $('#menuTabSimulation').parent().removeClass('disabled');
                    switchToBlockly();
                }, 'tabProgram clicked');

                $('#tabConfiguration').onWrap('click', function() {
                    Blockly.hideChaff();
                    activateProgConfigMenu();
                    $('#tabProgram').removeClass('tabClicked');
                    $('#tabConfiguration').addClass('tabClicked');
                    $('#tabSimulation').removeClass('tabClicked');
                    $('#head-navigation-program-edit').css('display', 'none');
                    $('#head-navigation-configuration-edit').css('display', 'inline');
                    $('#menuTabProgram').parent().removeClass('disabled');
                    $('#menuTabConfiguration').parent().addClass('disabled');
                    $('#menuTabSimulation').parent().removeClass('disabled');
                    switchToBrickly();
                }, 'tabConfiguration clicked');

                // controle for simulation
                $('.simSimple').onWrap('click', function(event) {
                    $('.menuSim').parent().removeClass('disabled');
                    $('.simSimple').parent().addClass('disabled');
                    SIM.setBackground(1);
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simSimple clicked');

                $('.simDraw').onWrap('click', function(event) {
                    $('.menuSim').parent().removeClass('disabled');
                    $('.simDraw').parent().addClass('disabled');
                    SIM.setBackground(2);
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simDraw clicked');

                $('.simRoberta').onWrap('click', function(event) {
                    $('.menuSim').parent().removeClass('disabled');
                    $('.simRoberta').parent().addClass('disabled');
                    SIM.setBackground(3);
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simRoberta clicked');

                $('.simRescue').onWrap('click', function(event) {
                    $('.menuSim').parent().removeClass('disabled');
                    $('.simRescue').parent().addClass('disabled');
                    SIM.setBackground(4);
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simRescue clicked');

                $('.simMath').onWrap('click', function(event) {
                    $('.menuSim').parent().removeClass('disabled');
                    $('.simMath').parent().addClass('disabled');
                    SIM.setBackground(5);
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simRescue clicked');

                $('.simBack').onWrap('click', function(event) {
                    SIM.cancel();
                    $(".sim").addClass('hide');
                    $('.nav > li > ul > .robotType').removeClass('disabled');
                    $("#simButtonsCollapse").collapse('hide');
                    $("#head-navi-tooltip-program").removeClass('disabled');
                    $('#head-navigation-program-edit').removeClass('disabled');
                    $('#head-navigation-program-edit>ul').removeClass('hidden');
                    $('.blocklyToolboxDiv').css('display', 'inherit');
                    Blockly.svgResize(ROBERTA_PROGRAM.getBlocklyWorkspace());
                    ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.toogleSim();
                    $('#blocklyDiv').animate({
                        width : '100%'
                    }, {
                        duration : 750,
                        step : function() {
                            $(window).resize();
                            Blockly.svgResize(ROBERTA_PROGRAM.getBlocklyWorkspace());
                        },
                        done : function() {
                            $("#simRobotModal").modal("hide");
                            $('#simDiv').removeClass('simActive');
                            $('#menuSim').parent().addClass('disabled');
                            $(window).resize();
                            Blockly.svgResize(ROBERTA_PROGRAM.getBlocklyWorkspace());
                        }
                    });
                }, 'simBack clicked');

                $('.simStop').onWrap('click', function(event) {
                    SIM.stopProgram();
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simStop clicked');

                $('.simForward').onWrap('click', function(event) {
                    if ($('.simForward').hasClass('typcn-media-play')) {
                        SIM.setPause(false);
                    } else {
                        SIM.setPause(true);
                    }
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simForward clicked');

                $('.simStep').onWrap('click', function(event) {
                    SIM.setStep();
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simStep clicked');

                $('.simInfo').onWrap('click', function(event) {
                    SIM.setInfo();
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simInfo clicked');

                $('#simRobot').onWrap('click', function(event) {
                    $("#simRobotModal").modal("toggle");
                    $("#simButtonsCollapse").collapse('hide');
                }, 'simRobot clicked');
                $('#simRobotModal').removeClass("modal-backdrop");
                // not working :-(
//                $('#simRobotModal').draggable();
                $('.simScene').onWrap('click', function(event) {
                    SIM.setBackground(0);
                    var scene = $("#simButtonsCollapse").collapse('hide');
                    $('.menuSim').parent().removeClass('disabled');
                    if (scene == 1) {
                        $('.simSimple').parent().addClass('disabled');
                    } else if (scene == 2) {
                        $('.simDraw').parent().addClass('disabled');
                    } else if (scene == 3) {
                        $('.simRoberta').parent().addClass('disabled');
                    } else if (scene == 4) {
                        $('.simRescue').parent().addClass('disabled');
                    } else if (scene == 5) {
                        $('.simMath').parent().addClass('disabled');
                    }
                }, 'simScene clicked');

                // preliminary (not used)
                $('#startNXT').onWrap('click', function(event) {
                    switchRobot('nxt');
                }, 'start with nxt clicked');
                // preliminary (not used)
                $('#startEV3').onWrap('click', function(event) {
                    switchRobot('ev3');
                }, 'start with ev3 clicked');

                $('.codeBack').onWrap('click', function(event) {
                    $('#blocklyDiv').removeClass('codeActive');
                    $('#codeDiv').removeClass('codeActive');
                    $('#blocklyDiv').animate({
                        width : '100%'
                    }, {
                        duration : 750,
                        step : function() {
                            $(window).resize();
                            Blockly.svgResize(ROBERTA_PROGRAM.getBlocklyWorkspace());
                        },
                        done : function() {
                            $(window).resize();
                        }
                    });
                    if (userState.robot === "nxt") {
                        $('#menuEv3').parent().removeClass('disabled');
                    } else {
                        $('#menuNxt').parent().removeClass('disabled');
                    }
                    Blockly.svgResize(ROBERTA_PROGRAM.getBlocklyWorkspace());
                    COMM.json("/toolbox", {
                        "cmd" : "loadT",
                        "name" : userState.toolbox,
                        "owner" : " "
                    }, function(result) {
                        ROBERTA_PROGRAM.injectBlockly(result, userState.programBlocksSaved);
                    });
                    $("#head-navi-tooltip-program").removeClass('disabled');
                    $('#head-navigation-program-edit').removeClass('disabled');
                    $('#head-navigation-program-edit>ul').removeClass('hidden');
                    $(".code").addClass('hide');
                }, 'codeBack clicked');

                $('#codeDownload').onWrap('click', function(event) {
                    var blob = new Blob([ userState.programCode ]);
                    var element = document.createElement('a');
                    var myURL = window.URL || window.webkitURL;
                    element.setAttribute('href', myURL.createObjectURL(blob));
                    element.setAttribute('download', userState.program + ".java");
                    element.style.display = 'none';
                    document.body.appendChild(element);
                    element.click();
                    document.body.removeChild(element);
                }, 'codeDownload clicked');

                $('.newRelease').onWrap('click', function(event) {
                    $('#show-release').modal("show");
                }, 'show release clicked');
            }

            exports.initHeadNavigation = initHeadNavigation;

            function beforeActivateTab(event, ui) {
                $('#tabs').tabs("refresh");
                if (ui.newPanel.selector === '#progListing') {
                    PROGRAM.refreshList(ROBERTA_PROGRAM.showPrograms);
                } else if (ui.newPanel.selector === '#confListing') {
                    CONFIGURATION.refreshList(ROBERTA_BRICK_CONFIGURATION.showConfigurations);
                }
            }
            exports.beforeActivateTab = beforeActivateTab;
        });
