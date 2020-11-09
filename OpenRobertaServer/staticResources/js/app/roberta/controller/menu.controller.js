define([ 'exports', 'log', 'util', 'message', 'comm', 'robot.controller', 'socket.controller', 'user.controller', 'user.model', 'notification.controller','userGroup.controller' ,'guiState.controller',
        'program.controller', 'program.model', 'multSim.controller', 'progRun.controller', 'configuration.controller', 'import.controller', 'enjoyHint',
        'tour.controller', 'simulation.simulation', 'progList.model', 'jquery', 'blockly', 'slick' ], function(exports, LOG, UTIL, MSG, COMM, ROBOT_C, SOCKET_C,
        USER_C, USER,NOTIFICATION_C, USERGROUP_C, GUISTATE_C, PROGRAM_C, PROGRAM_M, MULT_SIM, RUN_C, CONFIGURATION_C, IMPORT_C, EnjoyHint, TOUR_C, SIM, PROGLIST, $, Blockly) {

    var n = 0;

    const QUERY_START = '?';
    const QUERY_DELIMITER = '&';
    const QUERY_ASSIGNMENT = '=';
    const LOAD_SYSTEM_CALL = 'loadSystem';

    function cleanUri() {
        var uri = window.location.toString();
        var clean_uri = uri.substring(0, uri.lastIndexOf("/"));
        window.history.replaceState({}, document.title, clean_uri);
    }

    // from https://stackoverflow.com/questions/19491336/get-url-parameter-jquery-or-how-to-get-query-string-values-in-js/21903119#21903119
    function getUrlParameter(sParam) {
        var sPageURL = window.location.search.substring(1), sURLVariables = sPageURL.split(QUERY_DELIMITER), sParameterName, i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split(QUERY_ASSIGNMENT);

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
            }
        }
    }

    function handleQuery() {
        // old style queries
        var target = decodeURI(document.location.hash).split("&&");
        if (target[0] === "#forgotPassword") {
            USER_C.showResetPassword(target[1]);
        } else if (target[0] === "#loadProgram" && target.length >= 4) {
            GUISTATE_C.setStartWithoutPopup();
            IMPORT_C.openProgramFromXML(target);
        } else if (target[0] === "#activateAccount") {
            USER_C.activateAccount(target[1]);
        } else if (target[0] === "#overview") {
            GUISTATE_C.setStartWithoutPopup();
            TOUR_C.start('overview');
        } else if (target[0] === "#gallery") {
            GUISTATE_C.setStartWithoutPopup();
            $('#tabGalleryList').click();
        } else if (target[0] === "#tutorial") {
            GUISTATE_C.setStartWithoutPopup();
            $('#tabTutorialList').click();
        } else if (target[0] === "#loadSystem" && target.length >= 2) {
            GUISTATE_C.setStartWithoutPopup();
            ROBOT_C.switchRobot(target[1], true);
        }

        // new style queries
        var loadSystem = getUrlParameter(LOAD_SYSTEM_CALL);
        if (loadSystem) {
            GUISTATE_C.setStartWithoutPopup();
            ROBOT_C.switchRobot(loadSystem, true);
        }
    }

    function init() {
        initMenu();
        initMenuEvents();
        /**
         * Regularly ping the server to keep status information up-to-date
         */
        function pingServer() {
            setTimeout(function() {
                n += 1000;
                if (n >= GUISTATE_C.getPingTime() && GUISTATE_C.doPing()) {
                    COMM.ping(function(result) {
                        GUISTATE_C.setState(result);
                    });
                    n = 0;
                }
                pingServer();
            }, 1000);
        }
        pingServer();

        handleQuery();
        cleanUri();

        var firsttime = true
        $('#show-startup-message').on('shown.bs.modal', function(e) {
            $(function() {
                if (firsttime) {
                    // *******************
                    // This is a draft to make other/more robots visible.
                    var autoplaySpeed = 2000;
                    var autoplayOn = true;
                    $('#popup-robot-main').on('init', function() {
                        $('#slick-container').mouseenter(function() {
                            autoplayOn = false;
                        });
                        $('#slick-container').mouseleave(function() {
                            autoplayOn = true;
                        });

                        window.setInterval(function() {
                            if (!autoplayOn)
                                return;
                            $('#popup-robot-main').slick('slickPrev');
                        }, autoplaySpeed);
                    });
                    // ******************
                    $('#popup-robot-main').slick({
                        centerMode : true,
                        dots : true,
                        infinite : true,
                        centerPadding : '60px',
                        slidesToShow : 3,
                        index : 2,
                        swipeToSlide : true,
                        setPosition : true,
                        prevArrow : "<button type='button' class='slick-prev slick-arrow typcn typcn-arrow-left-outline'></button>",
                        nextArrow : "<button type='button' class='slick-next slick-arrow typcn typcn-arrow-right-outline'></button>",
                        responsive : [ {
                            breakpoint : 992,
                            settings : {
                                centerPadding : '5px',
                                slidesToShow : 1,
                                swipeToSlide : true,
                                variableWidth : true
                            }
                        } ]
                    });
                    firsttime = false
                } else {
                    $('#popup-robot-main').slick("refresh");
                }
            })
        })
    }

    exports.init = init;

    function initMenu() {
        // fill dropdown menu robot
        $("#startupVersion").text(GUISTATE_C.getServerVersion());
        var robots = GUISTATE_C.getRobots();
        var proto = $('.robotType');
        var length = Object.keys(robots).length

        for (var i = 0; i < length; i++) {
            if (robots[i].name == 'sim') {
                proto.attr('data-type', GUISTATE_C.getDefaultRobot());
                i++;
            }
            var clone = proto.clone();
            var robotName = robots[i].name;
            var robotGroup = robots[i].group;
            clone.find('.typcn').addClass('typcn-' + robotGroup);
            clone.find('.typcn').html(robots[i].realName);
            clone.find('.typcn').attr('id', 'menu-' + robotName);
            clone.attr('data-type', robotName);
            clone.addClass(robotName);
            $("#navigation-robot>.anchor").before(clone);
        }
        proto.remove();
        // fill start popup
        proto = $('#popup-sim');

        var groupsDict = {};

        var addPair = function(key, value) {
            if (typeof groupsDict[key] != 'undefined') {
                groupsDict[key].push(value);
            } else {
                groupsDict[key] = [ value ];
            }
        }

        var giveValue = function(key) {
            return groupsDict[key];
        }

        /**
         * This method either changes or removes the info link for further
         * information to the buttons in the carousel/group member view
         */
        var addInfoLink = function(clone, robotName) {
            var robotInfoDE = GUISTATE_C.getRobotInfoDE(robotName);
            var robotInfoEN = GUISTATE_C.getRobotInfoEN(robotName);
            if (robotInfoDE !== "#" || robotInfoEN !== "#") {
                var $de = clone.find('a');
                var $en = $de.clone();
                if (robotInfoDE === "#") {
                    robotInfoDE = robotInfoEN;
                }
                if (robotInfoEN === "#") {
                    robotInfoEN = robotInfoDE;
                }
                $de.attr({
                    'onclick' : 'window.open("' + robotInfoDE + '");return false;',
                    'class' : 'DE'
                });
                $en.attr({
                    'onclick' : 'window.open("' + robotInfoEN + '");return false;',
                    'class' : 'EN'
                });
                $en.appendTo(clone);
            } else {
                clone.find('a').remove();
            }
            return clone;
        }

        for (var i = 0; i < length; i++) {
            if (robots[i].name == 'sim') {
                i++;
                // TODO check this hardcoded Open Roberta Sim again (maybe some day there is a better choise for us)
                proto.attr('data-type', GUISTATE_C.getDefaultRobot());
            }
            addPair(robots[i].group, robots[i].name);
        }
        for ( var key in groupsDict) {
            if (groupsDict.hasOwnProperty(key)) {
                if (groupsDict[key].length == 1 || key === "calliope") { //this means that a robot has no subgroup

                    var robotName = key; // robot name is the same as robot group
                    var clone = proto.clone().prop('id', 'menu-' + robotName);
                    clone.find('span:eq( 0 )').removeClass('typcn-open');
                    clone.find('span:eq( 0 )').addClass('typcn-' + robotName);
                    if (key === "calliope") {
                        robotName = "calliope2017NoBlue";
                    }
                    clone.find('span:eq( 1 )').html(GUISTATE_C.getMenuRobotRealName(robotName));
                    clone.attr('data-type', robotName);
                    addInfoLink(clone, robotName);
                    if (!GUISTATE_C.getIsRobotBeta(robotName)) {
                        clone.find('img.img-beta').css('visibility', 'hidden');
                    }
                    $("#popup-robot-main").append(clone);

                } else { // till the next for loop we create groups for robots
                    var robotGroup = key;
                    var clone = proto.clone().prop('id', 'menu-' + robotGroup);
                    clone.attr('data-type', robotGroup);
                    if (robotGroup == "arduino") {
                        clone.find('span:eq( 1 )').html("Nepo4Arduino");
                    } else {
                        clone.find('span:eq( 1 )').html(robotGroup.charAt(0).toUpperCase() + robotGroup.slice(1)); // we have no real name for group
                    }
                    clone.find('span:eq( 0 )').removeClass('typcn-open');
                    clone.find('span:eq( 0 )').addClass('typcn-' + robotGroup); // so we just capitalize the first letter + add typicon
                    clone.find('img.img-beta').css('visibility', 'hidden'); // groups do not have 'beta' labels
                    addInfoLink(clone, robotGroup); // this will just kill the link tag, because no description for group
                    clone.attr('data-group', true);
                    $("#popup-robot-main").append(clone);
                    for (var i = 0; i < groupsDict[key].length; i++) { // and here we put robots in subgroups
                        var robotName = groupsDict[key][i];
                        var clone = proto.clone().prop('id', 'menu-' + robotName);
                        clone.addClass('hidden');
                        clone.addClass('robotSubGroup');
                        clone.addClass(robotGroup);
                        if (!GUISTATE_C.getIsRobotBeta(robotName)) {
                            clone.find('img.img-beta').css('visibility', 'hidden');
                        }
                        addInfoLink(clone, robotName);
                        clone.attr('data-type', robotName);
                        clone.find('span:eq( 0 )').removeClass('typcn-open');
                        clone.find('span:eq( 0 )').addClass('img-' + robotName); // there are no typicons for robots
                        clone.find('span:eq( 1 )').html(GUISTATE_C.getMenuRobotRealName(robotName)); // instead we use images
                        clone.attr('data-type', robotName);
                        $("#popup-robot-subgroup").append(clone);
                    }
                }
            }
        }

        proto.find('.img-beta').css('visibility', 'hidden');
        proto.find('a[href]').css('visibility', 'hidden');
        $('#show-startup-message>.modal-body').append('<input type="button" class="btn backButton hidden" data-dismiss="modal" lkey="Blockly.Msg.POPUP_CANCEL"></input>');
        if (GUISTATE_C.getLanguage() === 'de') {
            $('.popup-robot .EN').css('display', 'none');
            $('.popup-robot .DE').css('display', 'inline');
        } else {
            $('.popup-robot .DE').css('display', 'none');
            $('.popup-robot .EN').css('display', 'inline');
        }
        GUISTATE_C.setInitialState();
    }

    /**
     * Initialize the navigation bar in the head of the page
     */
    function initMenuEvents() {
        // TODO check if this prevents iPads and iPhones to only react on double clicks
        if (!navigator.userAgent.match(/iPad/i) && !(navigator.userAgent.match(/iPhone/i))) {
            $('[rel="tooltip"]').not('.rightMenuButton').tooltip({
                container : 'body',
                placement : "right"
            });
            $('[rel="tooltip"].rightMenuButton').tooltip({
                container : 'body',
                placement : "left"
            });
        }
        // prevent Safari 10. from zooming
        document.addEventListener('gesturestart', function(e) {
            e.preventDefault();
            e.stopPropagation();
        });

        $('.modal').on('shown.bs.modal', function() {
            $(this).find('[autofocus]').focus();
        });

        $('#navbarCollapse').collapse({
            'toggle' : false
        });

        $('#navbarCollapse').onWrap('click', '.dropdown-menu a,.visible-xs', function(event) {
            $('#navbarCollapse').collapse('hide');
        });
        // for gallery
        $('#head-navigation-gallery').onWrap('click', 'a,.visible-xs', function(event) {
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
        if (GUISTATE_C.isPublicServerVersion()) {
            var feedbackButton = '<div href="#" id="feedbackButton" class="rightMenuButton" rel="tooltip" data-original-title="" title="">'
                    +'<span id="" class="feedbackButton typcn typcn-feedback"></span>'
                    +'</div>'
            $("#rightMenuDiv").append(feedbackButton);
            window.onmessage = function(msg) {
	            if (msg.data ==="closeFeedback") {		             
                    $('#feedbackIframe').one('load', function () {
                        setTimeout(function() { 
	                        $("#feedbackIframe").attr("src" ,"about:blank");
	                        $('#feedbackModal').modal("hide");
                        }, 1000);
                    });
	            } else if (msg.data.indexOf("feedbackHeight") >= 0) {
		            var height = msg.data.split(":")[1]||400;
                    $('#feedbackIframe').height(height);
	            }
            };                  
            $('#feedbackButton').onWrap('click', '', function(event) {
                $('#feedbackModal').on('show.bs.modal', function () {
	                if (GUISTATE_C.getLanguage().toLowerCase() === "de") {
	                    $("#feedbackIframe").attr("src" ,"https://www.roberta-home.de/lab/feedback/");
                    } else {
	                    $("#feedbackIframe").attr("src" ,"https://www.roberta-home.de/en/lab/feedback/");
                    }
                });
                $('#feedbackModal').modal({show:true});
            });
        }

        // EDIT Menu
        $('#head-navigation-program-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            switch (event.target.id) {
            case 'menuRunProg':
                RUN_C.runOnBrick();
                break;
            case 'menuRunSim':
                $('#simButton').trigger('click');
                break;
            case 'menuCheckProg':
                PROGRAM_C.checkProgram();
                break;
            case 'menuNewProg':
                PROGRAM_C.newProgram();
                break;
            case 'menuListProg':
                $('#tabProgList').data('type', 'userProgram');
                $('#tabProgList').click();
                break;
            case 'menuListExamples':
                $('#tabProgList').data('type', 'exampleProgram');
                $('#tabProgList').click();
                break;
            case 'menuSaveProg':
                PROGRAM_C.saveToServer();
                break;
            case 'menuSaveAsProg':
                PROGRAM_C.showSaveAsModal();
                break;
            case 'menuShowCode':
                $('#codeButton').trigger("click");
                break;
            case 'menuSourceCodeEditor':
                $('#tabSourceCodeEditor').trigger("click");
                break;
            case 'menuImportProg':
                IMPORT_C.importXml();
                break;
            case 'menuExportProg':
                PROGRAM_C.exportXml();
                break;
            case 'menuLinkProg':
                PROGRAM_C.linkProgram();
                break;
            case 'menuToolboxBeginner':
                $('.levelTabs a[href="#beginner"]').tab('show');
                break;
            case 'menuToolboxExpert':
                $('.levelTabs a[href="#expert"]').tab('show');
                break;
            case 'menuRunMulipleSim':
                MULT_SIM.showListProg();
                break;
            case 'menuDefaultFirmware':
                RUN_C.reset2DefaultFirmware();
            default:
                break;
            }
        }, 'program edit clicked');

        // CONF Menu
        $('#head-navigation-configuration-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            switch (event.target.id) {
            case 'menuCheckConfig':
                MSG.displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP", "");
                break;
            case 'menuNewConfig':
                CONFIGURATION_C.newConfiguration();
                break;
            case 'menuListConfig':
                $('#tabConfList').click();
                break
            case 'menuSaveConfig':
                CONFIGURATION_C.saveToServer();
                break;
            case 'menuSaveAsConfig':
                CONFIGURATION_C.showSaveAsModal();
                break;
            default:
                break;
            }
        }, 'configuration edit clicked');

        // ROBOT Menu
        $('#head-navigation-robot').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide');
            var choosenRobotType = event.target.parentElement.dataset.type;
            //TODO: change from ardu to botnroll and mbot with friends
            //I guess it is changed now, check downstairs at menuConnect
            if (choosenRobotType) {
                ROBOT_C.switchRobot(choosenRobotType);
            } else {
                var domId = event.target.id;
                if (domId === 'menuConnect') {
                    //console.log(GUISTATE_C.getIsAgent());
                    //console.log(GUISTATE_C.getConnection());
                    if (GUISTATE_C.getConnection() == 'arduinoAgent'
                            || (GUISTATE_C.getConnection() == 'arduinoAgentOrToken' && GUISTATE_C.getIsAgent() == true)) {
                        var ports = SOCKET_C.getPortList();
                        var robots = SOCKET_C.getRobotList();
                        $('#singleModalListInput').empty();
                        i = 0;
                        ports.forEach(function(port) {
                            $('#singleModalListInput').append("<option value=\"" + port + "\" selected>" + robots[i] + " " + port + "</option>");
                            i++;
                        });
                        ROBOT_C.showListModal();
                    } else if (GUISTATE_C.getConnection() == 'webview') {
                        ROBOT_C.showScanModal();
                    } else {
                        $('#buttonCancelFirmwareUpdate').css('display', 'inline');
                        $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
                        ROBOT_C.showSetTokenModal();
                    }
                } else if (domId === 'menuRobotInfo') {
                    ROBOT_C.showRobotInfo();
                } else if (domId === 'menuWlan') {
                    ROBOT_C.showWlanForm();
                }
            }
        }, 'robot clicked');

        $('#head-navigation-help').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuShowStart') { // Submenu 'Help'
                $("#show-startup-message").modal("show");
            } else if (domId === 'menuAbout') { // Submenu 'Help'
                $("#version").text(GUISTATE_C.getServerVersion());
                $("#show-about").modal("show");
            } else if (domId === 'menuLogging') { // Submenu 'Help'
                $('#tabLogList').click();
            }
        }, 'help clicked');

        $('#head-navigation-user').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            switch (event.target.id) {
            case 'menuLogin':
                USER_C.showLoginForm();
                break;
            case 'menuUserGroupLogin':
                USER_C.showUserGroupLoginForm();	
                break;
            case 'menuLogout':
                USER_C.logout();
                break;
            case 'menuGroupPanel':	
                USERGROUP_C.showPanel();	
                break;
            case 'menuChangeUser':
                USER_C.showUserDataForm();
                break;
            case 'menuDeleteUser':
                USER_C.showDeleteUserModal();
                break;
            case 'menuStateInfo':
                USER_C.showUserInfo();
                break;
            case 'menuNotification':
                NOTIFICATION_C.showNotificationModal();
                break;
            default:
                break;
            }
            return false;
        }, 'user clicked');

        $('#head-navigation-gallery').onWrap('click', function(event) {
            $('#tabGalleryList').click();
            return false;
        }, 'gallery clicked');
        $('#head-navigation-tutorial').onWrap('click', function(event) {
            $('#tabTutorialList').click();
            return false;
        }, 'tutorial clicked');

        $('.sim-nav').onWrap('click', 'li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // head-navigation-sim-control
            $('.menuSim').parent().removeClass('disabled'); //these two were in all cases
            $("#simButtonsCollapse").collapse('hide'); //so I extracted them here
            switch (event.target.id) {
            case 'menuSimSimple':
                $('.simSimple').parent().addClass('disabled');
                SIM.setBackground(2, SIM.setBackground);
                break;
            case 'menuSimDraw':
                $('.simDraw').parent().addClass('disabled');
                SIM.setBackground(3, SIM.setBackground);
                break;
            case 'menuSimRoberta':
                $('.simRoberta').parent().addClass('disabled');
                SIM.setBackground(4, SIM.setBackground);
                break;
            case 'menuSimRescue':
                $('.simRescue').parent().addClass('disabled');
                SIM.setBackground(5, SIM.setBackground);
                break;
            case 'menuSimWRO':
                $('.simWRO').parent().addClass('disabled');
                SIM.setBackground(6, SIM.setBackground);
                break;
            case 'menuSimMath':
                $('.simMath').parent().addClass('disabled');
                SIM.setBackground(7, SIM.setBackground);
                break;
            default:
                break;
            }
        }, 'sim clicked');

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

        $('#img-nepo').onWrap('click', function() {
            $("#show-startup-message").modal("show");
        }, 'logo was clicked');

        $('.menuGeneral').onWrap('click', function(event) {
            window.open("https://jira.iais.fraunhofer.de/wiki/display/ORInfo");
        }, 'head navigation menu item clicked');
        $('.menuFaq').onWrap('click', function(event) {
            window.open("https://jira.iais.fraunhofer.de/wiki/display/ORInfo/FAQ");
        }, 'head navigation menu item clicked');
		$('.shortcut').onWrap('click', function(event) {
            window.open("https://jira.iais.fraunhofer.de/wiki/display/ORInfo/FAQ");
        }, 'head navigation menu item clicked');
        $('.menuAboutProject').onWrap('click', function(event) {
            if (GUISTATE_C.getLanguage() == 'de') {
                window.open("https://www.roberta-home.de/index.php?id=135");
            } else {
                window.open("https://www.roberta-home.de/index.php?id=135&L=1");
            }
        }, 'head navigation menu item clicked');

        $('.simScene').onWrap('click', function(event) {
            SIM.setBackground(-1, SIM.setBackground);
            var scene = $("#simButtonsCollapse").collapse('hide');
            $('.menuSim').parent().removeClass('disabled');
            switch (scene) {
            case 2:
                $('.simSimple').parent().addClass('disabled');
                break;
            case 3:
                $('.simDraw').parent().addClass('disabled');
                break;
            case 4:
                $('.simRoberta').parent().addClass('disabled');
                break;
            case 5:
                $('.simRescue').parent().addClass('disabled');
                break;
            case 6:
                $('.simWRO').parent().addClass('disabled');
                break;
            case 7:
                $('.simMath').parent().addClass('disabled');
                break;
            default:
                break;
            }
        }, 'simScene clicked');

        $('#startPopupBack').on('click', function(event) {
            $('#popup-robot-main').removeClass('hidden', 1000);
            $('.popup-robot.robotSubGroup').addClass('hidden', 1000);
            $('.robotSpecial').removeClass('robotSpecial');
            $('#startPopupBack').addClass('hidden');
            $('#popup-robot-main').slick("refresh");
        });
        var mousex = 0;
        var mousey = 0;
        $('.popup-robot').on('mousedown', function(event) {
            mousex = event.clientX;
            mousey = event.clientY;
        });
        $('.popup-robot').onWrap('click', function(event) {
            if (Math.abs(event.clientX - mousex) >= 3 || Math.abs(event.clientY - mousey) >= 3) {
                return;
            }
            event.preventDefault();
            $('#startPopupBack').trigger('click');
            var choosenRobotType = event.target.dataset.type || event.currentTarget.dataset.type;
            var choosenRobotGroup = event.target.dataset.group || event.currentTarget.dataset.group;
            if (event.target.className.indexOf("info") >= 0) {
                var win = window.open(GUISTATE_C.getRobots()[choosenRobotType].info, '_blank');
            } else {
                if (choosenRobotType) {
                    if (choosenRobotGroup) {
                        $('#popup-robot-main').addClass('hidden');
                        $('.popup-robot.' + choosenRobotType).removeClass('hidden');
                        $('.popup-robot.' + choosenRobotType).addClass('robotSpecial');
                        $('#startPopupBack').removeClass('hidden');
                        return;
                    } else {
                        if ($('#checkbox_id').is(':checked')) {
                            cleanUri(); // removes # which may potentially be added by other operations
                            var uri = window.location.toString();
                            uri += QUERY_START + LOAD_SYSTEM_CALL + QUERY_ASSIGNMENT + choosenRobotType;
                            window.history.replaceState({}, document.title, uri);

                            $('#show-message').one('hidden.bs.modal', function(e) {
                                e.preventDefault();
                                cleanUri();
                                ROBOT_C.switchRobot(choosenRobotType, true);
                            });
                            MSG.displayMessage("POPUP_CREATE_BOOKMARK", "POPUP", "");
                        } else {
                            ROBOT_C.switchRobot(choosenRobotType, true);
                        }
                    }
                }

                $('#show-startup-message').modal('hide');
            }
        }, 'robot choosen in start popup');

        $('#moreReleases').onWrap('click', function(event) {
            $('#oldReleases').show({
                start : function() {
                    $('#moreReleases').addClass('hidden');
                }
            });
        }, 'show more releases clicked');

        $('#takeATour').onWrap('click', function(event) {
            if (GUISTATE_C.getView() !== "tabProgram") {
                $('#tabProgram').click();
            }
            if (GUISTATE_C.getRobotGroup() !== 'ev3') {
                ROBOT_C.switchRobot('ev3lejosv1', true);
            }
            if (GUISTATE_C.getProgramToolboxLevel() !== 'beginner') {
                $('#beginner').trigger('click');
            }
            PROGRAM_C.newProgram(true);
            TOUR_C.start('welcome');
        }, 'take a tour clicked');

        $('#goToWiki').onWrap('click', function(event) {
            event.preventDefault();
            window.open('https://jira.iais.fraunhofer.de/wiki/display/ORInfo', '_blank');
            event.stopPropagation();
            $("#show-startup-message").modal("show");

        }, 'take a tour clicked');

        // init popup events

        $('.cancelPopup').onWrap('click', function() {
            $('.ui-dialog-titlebar-close').click();
        });

        $('#about-join').onWrap('click', function() {
            $('#show-about').modal('hide');
        });

        $(window).on('beforeunload', function(e) {
            return Blockly.Msg.POPUP_BEFOREUNLOAD;
            // the following code doesn't work anymore, TODO check for a better solution.
//            if (!GUISTATE_C.isProgramSaved || !GUISTATE_C.isConfigurationSaved) {
//                if (GUISTATE_C.isUserLoggedIn()) {
//                    // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD_LOGGEDIN']);
//                    return Blockly.Msg.POPUP_BEFOREUNLOAD_LOGGEDIN;
//                } else {
//                    // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD']);
//                    return Blockly.Msg.POPUP_BEFOREUNLOAD;
//                }
//            }
        });

        // help Bootstrap to calculate the correct size for the collapse element when the sceen height is smaller than the elements height.
        $('#navbarCollapse').on('shown.bs.collapse', function() {
            var newHeight = Math.min($(this).height(), Math.max($('#blockly').height(), $('#brickly').height()));
            $(this).css('height', newHeight);
        });

        // experimental
        $(document).on('keydown', function(e) {
            if ((e.metaKey || e.ctrlKey) && (String.fromCharCode(e.which) === '1')) {
                IMPORT_C.importSourceCodeToCompile();
                return false;
            }
            if ((e.metaKey || e.ctrlKey) && event.which == 73) {
                IMPORT_C.importNepoCodeToCompile();
                return false;
            }
			if ((e.metaKey || e.ctrlKey) && event.which == 69) {
                PROGRAM_C.exportXml();
                return false;
            }
            if ((e.metaKey || e.ctrlKey) && (String.fromCharCode(e.which) === '2')) {
                var debug = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_debug');
                debug.initSvg();
                debug.render();
                debug.setInTask(false);

                return false;
            }
            if ((e.metaKey || e.ctrlKey) && (String.fromCharCode(e.which) === '3')) {
                var assert = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_assert');
                assert.initSvg();
                assert.setInTask(false);
                assert.render();
                var logComp = GUISTATE_C.getBlocklyWorkspace().newBlock('logic_compare');
                logComp.initSvg();
                logComp.setMovable(false);
                logComp.setInTask(false);
                logComp.setDeletable(false);
                logComp.render();
                var parentConnection = assert.getInput('OUT').connection;
                var childConnection = logComp.outputConnection;
                parentConnection.connect(childConnection);
                return false;
            }
            if ((e.metaKey || e.ctrlKey) && (String.fromCharCode(e.which) === '4')) {
                var expr = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_eval_expr');
                expr.initSvg();
                expr.render();
                expr.setInTask(false);
                return false;
            }
            // for IMU sensors of Arduino Uno Wifi Rev2, go to config first to create brickly workspace
            if ((e.metaKey || e.ctrlKey) && (String.fromCharCode(e.which) === '5')) {
                var expr = GUISTATE_C.getBricklyWorkspace().newBlock('robConf_accelerometer');
                expr.initSvg();
                expr.render();
                expr.setInTask(false);
                var expr = GUISTATE_C.getBricklyWorkspace().newBlock('robConf_gyro');
                expr.initSvg();
                expr.render();
                expr.setInTask(false);
                var expr = GUISTATE_C.getBlocklyWorkspace().newBlock('robSensors_accelerometer_getSample');
                expr.initSvg();
                expr.render();
                expr.setInTask(false);
                var expr = GUISTATE_C.getBlocklyWorkspace().newBlock('robSensors_gyro_getSample');
                expr.initSvg();
                expr.render();
                expr.setInTask(false);
                return false;
            }
            if ((e.metaKey || e.ctrlKey) && (String.fromCharCode(e.which) === '7')) {
                var expr = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_nnstep');
                expr.initSvg();
                expr.render();
                expr.setInTask(false);
                return false;
            }
			//Overriding the Ctrl + Shift + S when not logged in
            if ((e.metaKey || e.ctrlKey) && (e.shiftKey) && event.which == 83 && !GUISTATE_C.isUserLoggedIn()) {
                e.preventDefault();
            }
            //Overriding the Ctrl + Shift + S for saving program with new name in the server
            if ((e.metaKey || e.ctrlKey) && (e.shiftKey) && event.which == 83 && GUISTATE_C.isUserLoggedIn()) {
                e.preventDefault();
				PROGRAM_C.showSaveAsModal();
            }
			//Overriding the Ctrl + S when not logged in
            if ((e.metaKey || e.ctrlKey) && (!e.shiftKey) && event.which == 83 && !GUISTATE_C.isUserLoggedIn()) {
                e.preventDefault();
            }
			//Overriding the Ctrl + S for saving the program in the database on the server
            if ((e.metaKey || e.ctrlKey) && (!e.shiftKey) && event.which == 83 && GUISTATE_C.isUserLoggedIn()) {
                e.preventDefault();
				if(GUISTATE_C.getProgramName() === "NEPOprog") {
					PROGRAM_C.showSaveAsModal();
				} else {
					PROGRAM_C.saveToServer();
				}
				return false;	
			}     
			//Overriding the Ctrl + R for running the program
            if ((e.metaKey || e.ctrlKey) && event.which == 82) {
                e.preventDefault();
                RUN_C.runOnBrick();
				return false;	
			}
			//Overriding the Ctrl + Z for showing the source code
            if ((e.metaKey || e.ctrlKey) && event.which == 90 && !(e.shiftKey)) {
                e.preventDefault();
                $('#codeButton').trigger("click");
				return false;
			}
			//Overriding the Ctrl + Shift + Z for showing the source code editor
            if ((e.metaKey || e.ctrlKey) && (e.shiftKey) && event.which == 90) {
                e.preventDefault();
				$('#tabSourceCodeEditor').trigger("click");
				return false;
			}
			//Overriding the Ctrl + Shift + G when not logged in
            if ((e.metaKey || e.ctrlKey) && (e.shiftKey) && event.which == 71 && !GUISTATE_C.isUserLoggedIn()) {
                e.preventDefault();
            }
			//Overriding the Ctrl + Shift + G for multiple robot simulation
            if ((e.metaKey || e.ctrlKey) && (e.shiftKey) && event.which == 71 && GUISTATE_C.isUserLoggedIn()) {
				if(GUISTATE_C.hasMultiSim()) {
					e.preventDefault();
					MULT_SIM.showListProg();
					return false;	
				} else {
					e.preventDefault();
					return false;
				}
			}	
			//Overriding the Ctrl + M when not logged in
            if ((e.metaKey || e.ctrlKey) && event.which == 77 && !GUISTATE_C.isUserLoggedIn()) {
                e.preventDefault();
            }
			//Overriding the Ctrl + M for viewing all programs
            if ((e.metaKey || e.ctrlKey) && event.which == 77 && GUISTATE_C.isUserLoggedIn()) {
                e.preventDefault();
				$('#tabProgList').click();
				return false;     
			}	
			//Overriding the Ctrl + G for viewing the simulation window
            if ((e.metaKey || e.ctrlKey) && event.which == 71 && !(e.shiftKey)) {
				if(GUISTATE_C.hasSim()) {
					e.preventDefault();
					$('#simButton').trigger('click');
					return false;	
				} else {
					e.preventDefault();
					return false;
				}                
			}
			//Overriding the Ctrl + I for importing NEPO code to compile
			if ((e.metaKey || e.ctrlKey) && event.which == 73) {
                IMPORT_C.importNepoCodeToCompile();
                return false;
            }
			//Overriding the Ctrl + E for exporting the NEPO code
			if ((e.metaKey || e.ctrlKey) && event.which == 69) {
                PROGRAM_C.exportXml();
                return false;
            }
        });
    }
});