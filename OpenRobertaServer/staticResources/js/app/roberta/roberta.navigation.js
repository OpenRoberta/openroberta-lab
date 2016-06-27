define([ 'exports', 'util', 'message', 'comm', 'rest.robot', 'rest.program', 'rest.configuration', 'roberta.toolbox', 'roberta.robot', 'roberta.user',
        'roberta.brick-configuration', 'roberta.user-state', 'roberta.program', 'roberta.brickly', 'enjoyHint', 'roberta.tour', 'simulation.simulation',
        'jquery', 'blocks', 'jquery-ui' ], function(exports, UTIL, MSG, COMM, ROBOT, PROGRAM, CONFIGURATION, ROBERTA_TOOLBOX, ROBERTA_ROBOT, ROBERTA_USER,
        ROBERTA_BRICK_CONFIGURATION, userState, ROBERTA_PROGRAM, BRICKLY, EnjoyHint, ROBERTA_TOUR, SIM, $, Blockly) {

    var bricklyActive = false;

    function initNavigation() {
        $('#backLogging').onWrap('click', function() {
            activateProgConfigMenu();
            if (bricklyActive) {
                $('#tabConfiguration').trigger('click');
            } else {
                $('#tabProgram').trigger('click');
            }
        });

        $('#backConfiguration').onWrap('click', function() {
            activateProgConfigMenu();
            $('#tabConfiguration').trigger('click');
        });

        $('#backProgram').onWrap('click', function() {
            activateProgConfigMenu();
            $('#tabProgram').trigger('click');
        });

    }
    exports.initNavigation = initNavigation;

    /**
     * Switch to Blockly tab
     */
    function switchToBlockly() {
        Blockly.hideChaff(true);
        var workspace = ROBERTA_PROGRAM.getBlocklyWorkspace();
        workspace.markFocused();
        workspace.setVisible(true);
        Blockly.svgResize(workspace);
        bricklyActive = false;
    }
    exports.switchToBlockly = switchToBlockly;

    /**
     * Switch to Brickly tab
     */
    function switchToBrickly() {
        Blockly.hideChaff(true);
        var workspace = BRICKLY.getBricklyWorkspace();
        workspace.markFocused();
        workspace.setVisible(true);
        Blockly.svgResize(workspace);
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
        if (!userState.programSaved && userState.id === -1 && userState.program !== 'NEPOprog') {
            $('#menuSaveProg').parent().removeClass('login');
            $('#menuSaveProg').parent().removeClass('disabled');
            ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.enable('saveProgram');
        }
        if (!userState.configurationSaved && userState.id === -1 && userState.configuration != 'EV3basis') {
            $('#menuSaveConfig').parent().removeClass('login');
            $('#menuSaveConfig').parent().removeClass('disabled');
            BRICKLY.getBricklyWorkspace().robControls.enable('saveProgram');
        }
        if (!$(".sim").hasClass('hide')) {
            $('#menuShowCode').parent().addClass('disabled');
        }
    }
    exports.activateProgConfigMenu = activateProgConfigMenu;

    /**
     * Switch robot
     */

    function switchRobot(robot, opt_continue) {
        if (robot === userState.robot) {
            return;
        }
        var further = opt_continue || false;
        if (further || (userState.programSaved && userState.configurationSaved)) {
            ROBOT.setRobot(robot, function(result) {
                if (result.rc === "ok") {
                    $('.robotType').removeClass('disabled');
                    $('.' + robot).addClass('disabled');
                    $('#iconDisplayRobotState').removeClass('typcn-' + userState.robot);
                    $('#iconDisplayRobotState').addClass('typcn-' + robot);
                    userState.robot = robot;
                    ROBERTA_PROGRAM.getBlocklyWorkspace().setDevice(robot);
                    BRICKLY.getBricklyWorkspace().setDevice(robot);
                    ROBERTA_ROBOT.setState(result);
                    ROBERTA_TOOLBOX.loadToolbox(userState.toolbox);
                    BRICKLY.loadToolboxAndConfiguration();
                    ROBERTA_PROGRAM.newProgram(true);
                    ROBERTA_BRICK_CONFIGURATION.newConfiguration(true);
                } else {
                    alert('Robot not available');
                }
            });
        } else {
            $('#confirmContinue').data('type', 'switchRobot');
            $('#confirmContinue').data('robot', robot);
            if (userState.id === -1) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
            }
        }
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
        $('#navbarCollapse').collapse({
            'toggle' : false
        });
        $('#simButtonsCollapse').collapse({
            'toggle' : false
        });
        $('#navbarCollapse').onWrap('click', '.dropdown-menu a,.visible-xs', function(event) {
            $('#navbarCollapse').collapse('hide');
        });
        $('#simButtonsCollapse').onWrap('click', 'a', function(event) {
            $('#simButtonsCollapse').collapse('hide');
        });
        $('#navbarButtonsHead').onWrap('click', '', function(event) {
            $('#simButtonsCollapse').collapse('hide');
        });
        $('#simButtonsHead').onWrap('click', '', function(event) {
            $('#navbarCollapse').collapse('hide');
        });

        $('#head-navigation-program-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            var domId = event.target.id;
            if (domId === 'menuRunProg') { //  Submenu 'Program'   
                ROBERTA_PROGRAM.runOnBrick();
            } else if (domId === 'menuRunSim') { //  Submenu 'Program'
                ROBERTA_PROGRAM.runInSim();
            } else if (domId === 'menuCheckProg') { //  Submenu 'Program'
                ROBERTA_PROGRAM.checkProgram();
            } else if (domId === 'menuNewProg') { //  Submenu 'Program'
                ROBERTA_PROGRAM.newProgram();
            } else if (domId === 'menuListProg') { //  Submenu 'Program'
                deactivateProgConfigMenu();
                $('#tabProgList').data('type', 'userProgram');
                $('#tabProgList').click();
            } else if (domId === 'menuListExamples') { //  Submenu 'Program'
                deactivateProgConfigMenu();
                $('#tabProgList').data('type', 'exampleProgram');
                $('#tabProgList').click();
            } else if (domId === 'menuSaveProg') { //  Submenu 'Program'
                ROBERTA_PROGRAM.save();
            } else if (domId === 'menuSaveAsProg') { //  Submenu 'Program'
                ROBERTA_PROGRAM.showSaveAsModal();
            } else if (domId === 'menuShowCode') { //  Submenu 'Program'
                ROBERTA_PROGRAM.showCode();
            } else if (domId === 'menuImportProg') { //  Submenu 'Program'
                ROBERTA_PROGRAM.importXml();
            } else if (domId === 'menuExportProg') { //  Submenu 'Program'
                var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
                var xmlText = Blockly.Xml.domToText(xml);
                UTIL.download(userState.program + ".xml", xmlText);
                MSG.displayMessage("MENU_MESSAGE_DOWNLOAD", "TOAST", userState.program);
            } else if (domId === 'menuToolboxBeginner') { // Submenu 'Program'
                ROBERTA_TOOLBOX.loadToolbox('beginner');
            } else if (domId === 'menuToolboxExpert') { // Submenu 'Program'
                ROBERTA_TOOLBOX.loadToolbox('expert');
            }
        }, 'program edit clicked');

        $('#head-navigation-configuration-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuCheckConfig') { //  Submenu 'Configuration'
                MSG.displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP", "");
            } else if (domId === 'menuNewConfig') { //  Submenu 'Configuration'
                ROBERTA_BRICK_CONFIGURATION.newConfiguration();
            } else if (domId === 'menuListConfig') { //  Submenu 'Configuration'
                deactivateProgConfigMenu();
                $('#tabConfList').click();
            } else if (domId === 'menuSaveConfig') { //  Submenu 'Configuration'
                ROBERTA_BRICK_CONFIGURATION.save();
            } else if (domId === 'menuSaveAsConfig') { //  Submenu 'Configuration'
                ROBERTA_BRICK_CONFIGURATION.showSaveAsModal();
            }
        }, 'configuration edit clicked');

        $('#head-navigation-robot').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menu-ev3') { // Submenu 'Robot'             
                switchRobot('ev3');
            } else if (domId === 'menu-nxt') { // Submenu 'Robot'
                switchRobot('nxt');
            } else if (domId === 'menu-bayduino') { // Submenu 'Robot'
                switchRobot('bayduino');
            } else if (domId === 'menuConnect') { // Submenu 'Robot'
                $('#buttonCancelFirmwareUpdate').css('display', 'inline');
                $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
                ROBERTA_ROBOT.showSetTokenModal();
            } else if (domId === 'menuRobotInfo') { // Submenu 'Robot'
                ROBERTA_ROBOT.showRobotInfo();
            }
        }, 'robot clicked');

        $('#head-navigation-help').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuGeneral') { // Submenu 'Help'
                window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/BIAM");
            } else if (domId === 'menuFaq') { // Submenu 'Help'
                window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/BoAd");
            } else if (domId === 'menuShowRelease') { // Submenu 'Help'
                if ($.cookie("OpenRoberta_hideStartUp")) {
                    $('#checkbox_id').prop('checked', true);
                }
                $("#show-startup-message").modal("show");
            } else if (domId === 'menuStateInfo') { // Submenu 'Help'
                ROBERTA_USER.showUserInfo();
            } else if (domId === 'menuAbout') { // Submenu 'Help'
                $("#version").text(userState.version);
                $("#show-about").modal("show");
            } else if (domId === 'menuLogging') { // Submenu 'Help'
                deactivateProgConfigMenu();
                $('#tabLogList').click();
            }
        }, 'help clicked');

        $('#head-navigation-user').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuLogin') { // Submenu 'Login'
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
        }, 'user clicked');

        $('.sim-nav').onWrap('click', 'li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // head-navigation-sim-controle
            var domId = event.target.id;
            if (domId === 'menuSimSimple') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simSimple').parent().addClass('disabled');
                SIM.setBackground(1);
                $("#simButtonsCollapse").collapse('hide');
            } else if (domId === 'menuSimDraw') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simDraw').parent().addClass('disabled');
                SIM.setBackground(2);
                $("#simButtonsCollapse").collapse('hide');
            } else if (domId === 'menuSimRoberta') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simRoberta').parent().addClass('disabled');
                SIM.setBackground(3);
                $("#simButtonsCollapse").collapse('hide');
            } else if (domId === 'menuSimRescue') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simRescue').parent().addClass('disabled');
                SIM.setBackground(4);
                $("#simButtonsCollapse").collapse('hide');
            } else if (domId === 'menuSimMath') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simMath').parent().addClass('disabled');
                SIM.setBackground(5);
                $("#simButtonsCollapse").collapse('hide');
            }
        }, 'sim clicked');

        $('.simBack').onWrap('click', function(event) {
            SIM.cancel();
            $(".sim").addClass('hide');
            $('.nav > li > ul > .robotType').removeClass('disabled');
            $('#menuShowCode').parent().removeClass('disabled');
            $("#simButtonsCollapse").collapse('hide');
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
                    $('.nav > li > ul > .robotType').removeClass('disabled');
                    $('.' + userState.robot).addClass('disabled');
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

        $('#menuTabProgram').onWrap('click', '', function(event) {
            if ($('#tabSimulation').hasClass('tabClicked')) {
                $('.scroller-left').click();
            }
            $('.scroller-left').click();
            $('#tabProgram').click();
        }, 'tabProgram clicked');

        $('#menuTabConfiguration').onWrap('click', '', function(event) {
            if ($('#tabProgram').hasClass('tabClicked')) {
                $('.scroller-right').click();
            } else if ($('#tabConfiguration').hasClass('tabClicked')) {
                $('.scroller-right').click();
            }
            $('#tabConfiguration').click();
        }, 'tabConfiguration clicked');

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

        $('#tabProgram').onWrap('shown.bs.tab', function() {
            activateProgConfigMenu();
            $('#head-navigation-program-edit').css('display', 'inline');
            $('#head-navigation-configuration-edit').css('display', 'none');
            $('#menuTabProgram').parent().addClass('disabled');
            $('#menuTabConfiguration').parent().removeClass('disabled');
            switchToBlockly();
        }, 'tabProgram clicked');

        $('#tabConfiguration').onWrap('shown.bs.tab', function(e) {
            activateProgConfigMenu();
            $('#head-navigation-program-edit').css('display', 'none');
            $('#head-navigation-configuration-edit').css('display', 'inline');
            $('#menuTabProgram').parent().removeClass('disabled');
            $('#menuTabConfiguration').parent().addClass('disabled');
            switchToBrickly();
        }, 'tabConfiguration clicked');

        $('.menuBuildingInstructions').onWrap('click', function(event) {
            window.open("TODO");
        }, 'head navigation menu item clicked');
        $('.menuEV3conf').onWrap('click', function(event) {
            window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/RIAd");
        }, 'head navigation menu item clicked');
        $('.menuProgramming').onWrap('click', function(event) {
            window.open("https://mp-devel.iais.fraunhofer.de/wiki/x/CwA-/");
        }, 'head navigation menu item clicked');
        $('.menuPrivacy').onWrap('click', function(event) {
            window.open("TODO");
        }, 'head navigation menu item clicked');

        $('#simRobotModal').removeClass("modal-backdrop");
        $('#simRobotModal').draggable();
        //  $('#simRobotModal').resizable();
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
            $(".code").addClass('hide');
        }, 'codeBack clicked');
        $('#show-startup-message').on('hidden.bs.modal', function() {
            if ($('#checkbox_id').is(':checked')) {
                $.cookie("OpenRoberta_hideStartUp", true, {
                    expires : 99,
                    secure : true,
                    domain : ''
                });
                // check if it is really stored: chrome issue
                if (!$.cookie("OpenRoberta_hideStartUp")) {
                    $.cookie("OpenRoberta_hideStartUp", true, {
                        expires : 99,
                        domain : ''
                    });
                }
            } else {
                $.removeCookie("OpenRoberta_hideStartUp");
            }
        });
        $('#moreReleases').onWrap('click', function(event) {
            $('#oldReleases').show({
                start : function() {
                    $('#moreReleases').addClass('hidden');
                }
            });
        }, 'show more releases clicked');

        $('#codeDownload').onWrap('click', function(event) {
            // TODO get the programming language type from robot table in the database.
            var extension = userState.robotFWName === "ev3dev" ? ".py" : ".java";
            var filename = userState.program + extension;
            UTIL.download(filename, userState.programSource);
            MSG.displayMessage("MENU_MESSAGE_DOWNLOAD", "TOAST", userState.program);
        }, 'codeDownload clicked');

        $('.newRelease').onWrap('click', function(event) {
            $('#show-release').modal("show");
        }, 'show release clicked');

        $('#confirmContinue').onWrap('click', function(event) {
            if ($('#confirmContinue').data('type') === 'program') {
                ROBERTA_PROGRAM.newProgram(true);
            } else if ($('#confirmContinue').data('type') === 'configuration') {
                ROBERTA_BRICK_CONFIGURATION.newConfiguration(true);
            } else if ($('#confirmContinue').data('type') === 'switchRobot') {
                switchRobot($('#confirmContinue').data('robot'), true);
            } else {
                console.log('Confirmation with unknown data type clicked');
            }
        }, 'continue new program clicked');
        $('#takeATour').onWrap('click', function(event) {
            ROBERTA_TOUR.start('welcome');
        }, 'take a tour clicked');

    }

    exports.initHeadNavigation = initHeadNavigation;

    function beforeActivateProgList() {
        if ($('#tabProgList').data('type') === 'userProgram') {
            $("#deleteFromListing").show();
            $("#shareFromListing").show();
            $("#refreshListing").show();
            $('.bootstrap-table').find('button[name="refresh"]').trigger('click');
        } else {
            $("#deleteFromListing").hide();
            $("#shareFromListing").hide();
            $("#refreshListing").hide();
            $('.bootstrap-table').find('button[name="refresh"]').trigger('click');
        }
    }
    exports.beforeActivateProgList = beforeActivateProgList;

    function beforeActivateConfList() {
        CONFIGURATION.refreshList(ROBERTA_BRICK_CONFIGURATION.showConfigurations);
    }
    exports.beforeActivateConfList = beforeActivateConfList;
});
