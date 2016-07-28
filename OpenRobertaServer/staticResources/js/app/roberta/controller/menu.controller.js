define([ 'exports', 'log', 'util', 'message', 'comm', 'robot.controller', 'user.controller', 'guiState.controller', 'program.controller',
        'configuration.controller', 'enjoyHint', 'tour.controller', 'simulation.simulation', 'jquery', 'blocks' ], function(exports, LOG, UTIL, MSG, COMM,
        ROBOT_C, USER_C, GUISTATE_C, PROGRAM_C, CONFIGURATION_C, EnjoyHint, TOUR_C, SIM, $, Blockly) {

    function init() {

        initMenu();
        initMenuEvents();
        /**
         * Regularly ping the server to keep status information up-to-date
         */
        function pingServer() {
            if (GUISTATE_C.doPing()) {
                COMM.ping(function(result) {
                    GUISTATE_C.setState(result);
                });
            }
        }
        var ping = setInterval(function() {
            pingServer();
        }, 3000);
        LOG.info('init menu view');
    }

    exports.init = init;

    function initMenu() {
        var proto = $('.robotType');
        var robots = GUISTATE_C.getRobots();
        for ( var robot in robots) {
            var clone = proto.clone();
            $("#navigation-robot>.divider").before(clone);
            $(clone).find('.typcn').addClass('typcn-' + robot);
            $(clone).find('.typcn').text(robots[robot]);
            $(clone).find('.typcn').attr('id', 'menu-' + robot);
            $(clone).attr('data-type', robot);
            $(clone).addClass(robot);
        }
        proto.remove();
        proto = $('#popup-sim');
        for ( var robot in robots) {
            var clone = proto.clone();
            $("#show-startup-message .modal-footer").append(clone);
            $(clone).find('span:eq( 0 )').removeClass('typcn-open');
            $(clone).find('span:eq( 0 )').addClass('typcn-' + robot);
            $(clone).find('span:eq( 1 )').text(robots[robot]);
            $(clone).attr('id', 'menu-' + robot);
            $(clone).attr('data-type', robot);
            $(clone).addClass('popup-robot');
        }

        GUISTATE_C.setInitialState();

//        $('#backLogging').onWrap('click', function() {
//            activateProgConfigMenu();
//            if (bricklyActive) {
//                $('#tabConfiguration').trigger('click');
//            } else {
//                $('#tabProgram').trigger('click');
//            }
//        });
//
//        $('#backConfiguration').onWrap('click', function() {
//            activateProgConfigMenu();
//            $('#tabConfiguration').trigger('click');
//        });
//
//        $('#backProgram').onWrap('click', function() {
//            activateProgConfigMenu();
//            $('#tabProgram').trigger('click');
//        });
    }

    /**
     * Initialize the navigation bar in the head of the page
     */
    function initMenuEvents() {
        $('[rel="tooltip"]').tooltip({
            placement : "right"
        });

        $('.modal').on('shown.bs.modal', function() {
            $(this).find('[autofocus]').focus();
        });

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

        // EDIT Menu
        $('#head-navigation-program-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            var domId = event.target.id;
            if (domId === 'menuRunProg') {
                PROGRAM_C.runOnBrick();
            } else if (domId === 'menuRunSim') {
                PROGRAM_C.runInSim();
            } else if (domId === 'menuCheckProg') {
                PROGRAM_C.checkProgram();
            } else if (domId === 'menuNewProg') {
                PROGRAM_C.newProgram();
            } else if (domId === 'menuListProg') {
                $('#tabProgList').data('type', 'userProgram');
                $('#tabProgList').click();
            } else if (domId === 'menuListExamples') {
                $('#tabProgList').data('type', 'exampleProgram');
                $('#tabProgList').click();
            } else if (domId === 'menuSaveProg') {
                PROGRAM_C.saveToServer();
            } else if (domId === 'menuSaveAsProg') {
                PROGRAM_C.showSaveAsModal();
            } else if (domId === 'menuShowCode') {
                PROGRAM_C.showCode();
            } else if (domId === 'menuImportProg') {
                PROGRAM_C.importXml();
            } else if (domId === 'menuExportProg') {
                PROGRAM_C.exportXml();
            } else if (domId === 'menuToolboxBeginner') { // Submenu 'Program'
                PROGRAM_C.loadToolbox('beginner');
            } else if (domId === 'menuToolboxExpert') { // Submenu 'Program'
                PROGRAM_C.loadToolbox('expert');
            }
        }, 'program edit clicked');

        // CONF Menu
        $('#head-navigation-configuration-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuCheckConfig') { //  Submenu 'Configuration'
                MSG.displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP", "");
            } else if (domId === 'menuNewConfig') { //  Submenu 'Configuration'
                CONFIGURATION_C.newConfiguration();
            } else if (domId === 'menuListConfig') { //  Submenu 'Configuration'
                $('#tabConfList').click();
            } else if (domId === 'menuSaveConfig') { //  Submenu 'Configuration'
                CONFIGURATION_C.saveToServer();
            } else if (domId === 'menuSaveAsConfig') { //  Submenu 'Configuration'
                CONFIGURATION_C.showSaveAsModal();
            }
        }, 'configuration edit clicked');

        // ROBOT Menu
        $('#head-navigation-robot').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide');
            var choosenRobotType = event.target.parentElement.dataset.type;
            if (choosenRobotType) {
                ROBOT_C.switchRobot(choosenRobotType);
            } else {
                var domId = event.target.id;
                if (domId === 'menuConnect') {
                    $('#buttonCancelFirmwareUpdate').css('display', 'inline');
                    $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
                    ROBOT_C.showSetTokenModal();
                } else if (domId === 'menuRobotInfo') {
                    ROBOT_C.showRobotInfo();
                }
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
                if ($.cookie("OpenRoberta_" + GUISTATE_C.getServerVersion())) {
                    $('#checkbox_id').prop('checked', true);
                }
                $("#show-startup-message").modal("show");
            } else if (domId === 'menuAbout') { // Submenu 'Help'
                $("#version").text(guiState.version);
                $("#show-about").modal("show");
            } else if (domId === 'menuLogging') { // Submenu 'Help'
                $('#tabLogList').click();
            }
        }, 'help clicked');

        $('#head-navigation-user').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuLogin') { // Submenu 'Login'
                USER_C.showLoginForm();
            } else if (domId === 'menuLogout') { // Submenu 'Login'
                USER_C.logout();
            } else if (domId === 'menuNewUser') { // Submenu 'Login'
                $("#register-user").modal('show');
            } else if (domId === 'menuChangeUser') { // Submenu 'Login'
                USER_C.showUserDataForm();
            } else if (domId === 'menuDeleteUser') { // Submenu 'Login'
                USER_C.showDeleteUserModal();
            } else if (domId === 'menuStateInfo') { // Submenu 'Help'
                USER_C.showUserInfo();
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
            Blockly.svgResize(PROGRAM_C.getBlocklyWorkspace());
            PROGRAM_C.getBlocklyWorkspace().robControls.toogleSim();
            $('#blocklyDiv').animate({
                width : '100%'
            }, {
                duration : 750,
                step : function() {
                    $(window).resize();
                    Blockly.svgResize(PROGRAM_C.getBlocklyWorkspace());
                },
                done : function() {
                    $("#simRobotModal").modal("hide");
                    $('#simDiv').removeClass('simActive');
                    $('#menuSim').parent().addClass('disabled');
                    $('.nav > li > ul > .robotType').removeClass('disabled');
                    $('.' + GUISTATE_C.getRobot()).addClass('disabled');
                    $(window).resize();
                    Blockly.svgResize(PROGRAM_C.getBlocklyWorkspace());
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
        // $('#simRobotModal').draggable();
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
            $(".code").addClass('hide');
            $('.nav > li > ul > .robotType').removeClass('disabled');
            $('.blocklyToolboxDiv').css('display', 'inherit');
            Blockly.svgResize(PROGRAM_C.getBlocklyWorkspace());
            $('#blocklyDiv').animate({
                width : '100%'
            }, {
                duration : 750,
                step : function() {
                    $(window).resize();
                    Blockly.svgResize(PROGRAM_C.getBlocklyWorkspace());
                },
                done : function() {
                    $('#codeDiv').removeClass('codeActive');
                    $('.nav > li > ul > .robotType').removeClass('disabled');
                    //$('.' + guiState.robot).addClass('disabled');
                    $(window).resize();
                    Blockly.svgResize(PROGRAM_C.getBlocklyWorkspace());
                }
            });
            Blockly.svgResize(PROGRAM_C.getBlocklyWorkspace());

        }, 'codeBack clicked');

        $('.popup-robot').onWrap('click', function(event) {
            var choosenRobotType = event.target.parentElement.dataset.type || event.target.dataset.type;
            if (choosenRobotType) {
                ROBOT_C.switchRobot(choosenRobotType, true);
            }
            if ($('#checkbox_id').is(':checked')) {
                $.cookie("OpenRoberta_" + GUISTATE_C.getServerVersion(), choosenRobotType, {
                    expires : 99,
                    secure : true,
                    domain : ''
                });
                // check if it is really stored: chrome issue
                if (!$.cookie("OpenRoberta_" + GUISTATE_C.getServerVersion())) {
                    $.cookie("OpenRoberta_" + GUISTATE_C.getServerVersion(), choosenRobotType, {
                        expires : 99,
                        domain : ''
                    });
                }
            } else {
                $.removeCookie("OpenRoberta_" + GUISTATE_C.getServerVersion());
            }
        }, 'hallo');

        $('#moreReleases').onWrap('click', function(event) {
            $('#oldReleases').show({
                start : function() {
                    $('#moreReleases').addClass('hidden');
                }
            });
        }, 'show more releases clicked');

        $('#codeDownload').onWrap('click', function(event) {
            // TODO get the programming language type from server.
            var extension = GUISTATE_C.getRobotFWName() === "ev3dev" ? ".py" : ".java";
            var filename = GUISTATE_C.getProgramName() + extension;
            UTIL.download(filename, GUISTATE_C.getProgramSource());
            MSG.displayMessage("MENU_MESSAGE_DOWNLOAD", "TOAST", GUISTATE_C.getProgramName());
        }, 'codeDownload clicked');

        $('.newRelease').onWrap('click', function(event) {
            $('#show-release').modal("show");
        }, 'show release clicked');

        $('#confirmContinue').onWrap('click', function(event) {
            if ($('#confirmContinue').data('type') === 'program') {
                PROGRAM_C.newProgram(true);
            } else if ($('#confirmContinue').data('type') === 'configuration') {
                CONFIGURATION_C.newConfiguration(true);
            } else if ($('#confirmContinue').data('type') === 'switchRobot') {
                ROBOT_C.switchRobot($('#confirmContinue').data('robot'), true);
            } else {
                console.log('Confirmation with unknown data type clicked');
            }
        }, 'continue new program clicked');
        $('#takeATour').onWrap('click', function(event) {
            TOUR_C.start('welcome');
        }, 'take a tour clicked');

        // init popup events

        $('.cancelPopup').onWrap('click', function() {
            $('.ui-dialog-titlebar-close').click();
        });

        $('#about-join').onWrap('click', function() {
            $('#show-about').modal('hide');
        });

        var target = document.location.hash.split("&");

        if (target[0] === "#forgotPassword") {
            $('#passOld').val(target[1]);
            $('#resetPassLink').val(target[1]);
            $('#show-startup-message').modal('hide');
            USER_C.showResetPassword();
        }

        $(window).on('beforeunload', function(e) {
            if (!GUISTATE_C.isProgramSaved || !GUISTATE_C.isConfigurationSaved) {
                if (GUISTATE_C.isUserLoggedIn()) {
                    // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD_LOGGEDIN']);
                    return Blockly.Msg.POPUP_BEFOREUNLOAD_LOGGEDIN;
                } else {
                    // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD']);
                    return Blockly.Msg.POPUP_BEFOREUNLOAD;
                }
            }
        });
    }
});
