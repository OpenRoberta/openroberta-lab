import * as MSG from 'message';
import * as COMM from 'comm';
import * as WRAP from 'wrap';
import * as ROBOT_C from 'robot.controller';
import * as SOCKET_C from 'socket.controller';
import * as USER_C from 'user.controller';
import * as NOTIFICATION_C from 'notification.controller';
import * as GUISTATE_C from 'guiState.controller';
import * as PROGRAM_C from 'program.controller';
import * as RUN_C from 'progRun.controller';
import * as CONFIGURATION_C from 'configuration.controller';
import * as IMPORT_C from 'import.controller';
import * as TOUR_C from 'tour.controller';
import * as SOURCECODE_C from 'sourceCodeEditor.controller';
import * as $ from 'jquery';
import * as Blockly from 'blockly';
import 'slick';
import * as TUTORIAL_C from 'progTutorial.controller';
import * as UTIL from 'util.roberta';

var n = 0;

const QUERY_START = '?';
const QUERY_DELIMITER = '&';
const QUERY_ASSIGNMENT = '=';
const LOAD_SYSTEM_CALL = 'loadSystem';
const TUTORIAL = 'tutorial';
const KIOSK = 'kiosk';
const EXAMPLE_VIEW = 'exampleView';
var mainCallback;

// from https://stackoverflow.com/questions/19491336/get-url-parameter-jquery-or-how-to-get-query-string-values-in-js/21903119#21903119
function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split(QUERY_DELIMITER),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split(QUERY_ASSIGNMENT);

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
}

function handleQuery() {
    // old style queries
    var target = decodeURI(document.location.hash).split('&&');
    if (target[0] === '#forgotPassword') {
        USER_C.showResetPassword(target[1]);
    } else if (target[0] === '#loadProgram' && target.length >= 4) {
        GUISTATE_C.setStartWithoutPopup();
        IMPORT_C.openProgramFromXML(target);
    } else if (target[0] === '#activateAccount') {
        USER_C.activateAccount(target[1]);
    } else if (target[0] === '#overview') {
        GUISTATE_C.setStartWithoutPopup();
        TOUR_C.start('overview');
    } else if (target[0] === '#gallery') {
        GUISTATE_C.setStartWithoutPopup();
        $('.navbahead-navigation-configuration-editr-nav a[href="#galleryList"]').tab('show');
    } else if (target[0] === '#tutorial') {
        GUISTATE_C.setStartWithoutPopup();
        $('.navbar-nav a[href="#tutorialList"]').tab('show');
    } else if (target[0] === '#loadSystem' && target.length >= 2) {
        GUISTATE_C.setStartWithoutPopup();
        ROBOT_C.switchRobot(target[1], true);
    }

    // new style queries
    var loadSystem = getUrlParameter(LOAD_SYSTEM_CALL);
    if (loadSystem) {
        GUISTATE_C.setStartWithoutPopup();
        if (mainCallback && mainCallback instanceof Function) {
            mainCallback(loadSystem);
        } else {
            alert('Problem');
        }
    }
    let tutorial = getUrlParameter(TUTORIAL);
    if (tutorial) {
        if (tutorial === 'true' || tutorial === true) {
            GUISTATE_C.setStartWithoutPopup();
            $('.navbar-nav a[href="#tutorialList"]').tab('show');
        } else {
            let kiosk = getUrlParameter(KIOSK);
            if (kiosk && kiosk === 'true') {
                GUISTATE_C.setKioskMode(true);
            }
            GUISTATE_C.setStartWithoutPopup();
            TUTORIAL_C.loadFromTutorial(tutorial);
        }
    }
    let exampleView = getUrlParameter(EXAMPLE_VIEW);
    if (exampleView) {
        $('#menuListExamples').clickWrap();
    }
}

export function init(callback) {
    if (callback && callback instanceof Function) {
        mainCallback = callback;
    } else {
        alert('Problem');
        return;
    }
    $('#startupVersion').text(GUISTATE_C.getServerVersion());
    initMenuEvents();
    /**
     * Regularly ping the server to keep status information up-to-date
     */
    function pingServer() {
        setTimeout(function () {
            n += 1000;
            if (n >= GUISTATE_C.getPingTime() && GUISTATE_C.doPing()) {
                COMM.ping(function (result) {
                    GUISTATE_C.setState(result);
                });
                n = 0;
            }
            pingServer();
        }, 1000);
    }
    pingServer();
    handleQuery();
    UTIL.cleanUri();
}

function backToStartView() {
    if ($('.fromRight.rightActive').length > 0) {
        $('#blocklyDiv').closeRightView(function () {
            UTIL.closeSimRobotWindow();
            return $('#tabStart').tabWrapShow();
        });
    } else {
        $('#tabStart').tabWrapShow();
    }
}

/**
 * Initialize the navigation bar in the head of the page
 */
function initMenuEvents() {
    // TODO check if this prevents iPads and iPhones to only react on double clicks'
    $('.navbar-collapse a:not(.dropdown-toggle)').click(function () {
        $('.dropdown-menu.show').collapse('hide');
        $('.navbar-collapse.show').collapse('hide');
    });

    if (!navigator.userAgent.match(/iPad/i) && !navigator.userAgent.match(/iPhone/i)) {
        $('[rel="tooltip"]').not('.rightMenuButton').tooltip({
            container: 'body',
            placement: 'right',
            trigger: 'hover',
        });
        $('[rel="tooltip"].rightMenuButton').tooltip({
            container: 'body',
            placement: 'left',
            trigger: 'hover',
        });
    }
    // prevent Safari 10. from zooming
    document.addEventListener('gesturestart', function (e) {
        e.preventDefault();
        e.stopPropagation();
    });

    $('.blocklyButtonBack, .blocklyWidgetDiv, #head-navigation, #main-section, #tutorial-navigation').on('mousedown touchstart keydown', function (e) {
        if (
            $(e.target).not(
                '.blocklyTreeLabel, .blocklytreerow, .toolboxicon, .goog-palette-colorswatch, .goog-menu-vertical, .goog-menuitem-checkbox,' +
                    ' div.goog-menuitem-content, div.goog-menuitem, img'
            ).length > 0
        ) {
            if ($(e.target).filter('.blocklyHtmlInput').length > 0 && !e.metaKey) {
                return;
            }
            Blockly && Blockly.getMainWorkspace() && Blockly.hideChaff(); //TODO
        }
    });

    $('.modal').onWrap('shown.bs.modal', function () {
        $(this).find('[autofocus]').focus();
    });

    /* TODO $('#navbarCollapse').collapse({
        toggle: false,
    });*/

    $('.navbar-collapse').on('click', '.dropdown-menu a,.visible-xs', function (event) {
        $('#navbarCollapse').collapse('hide');
    });
    // for gallery
    $('#head-navigation-gallery').on('click', 'a,.visible-xs', function (event) {
        $('#navbarCollapse').collapse('hide');
    });
    if (GUISTATE_C.isPublicServerVersion()) {
        var feedbackButton =
            '<div href="#" id="feedbackButton" class="rightMenuButton" rel="tooltip" data-bs-original-title="" title="">' +
            '<span id="" class="feedbackButton typcn typcn-feedback"></span>' +
            '</div>';
        $('#rightMenuDiv').append(feedbackButton);
        window.onmessage = function (msg) {
            if (msg.data === 'closeFeedback') {
                $('#feedbackIframe').oneWrap('load', function () {
                    setTimeout(function () {
                        $('#feedbackIframe').attr('src', 'about:blank');
                        $('#feedbackModal').modal('hide');
                    }, 1000);
                });
            } else if (msg.data.indexOf('feedbackHeight') >= 0) {
                var height = msg.data.split(':')[1] || 400;
                $('#feedbackIframe').height(height);
            }
        };
        $('#feedbackButton').on('click', '', function (event) {
            $('#feedbackModal').on('show.bs.modal', function () {
                if (GUISTATE_C.getLanguage().toLowerCase() === 'de') {
                    $('#feedbackIframe').attr('src', 'https://www.roberta-home.de/lab/feedback/');
                } else {
                    $('#feedbackIframe').attr('src', 'https://www.roberta-home.de/en/lab/feedback/');
                }
            });
            $('#feedbackModal').modal('show');
        });
    }

    // EDIT Menu  --- don't use onWrap here, because the export xml target must be enabled always
    $('#head-navigation-program-edit').on('click', '.dropdown-menu li:not(.disabled) a', function (event) {
        var fn = function (event) {
            var targetId = event.target.id || event.currentTarget.id;
            switch (targetId) {
                case 'menuRunProg':
                    RUN_C.runOnBrick();
                    break;
                case 'menuRunSim':
                    $('#simButton').clickWrap();
                    break;
                case 'menuCheckProg':
                    PROGRAM_C.checkProgram();
                    break;
                case 'menuNewProg':
                    PROGRAM_C.newProgram();
                    break;
                case 'menuListProg':
                    $('#tabProgList').data('type', 'Programs');
                    $('#tabProgList').tabWrapShow();
                    break;
                case 'menuListExamples':
                    $('#tabProgList').data('type', 'Examples');
                    $('#tabProgList').tabWrapShow();
                    break;
                case 'menuSaveProg':
                    PROGRAM_C.saveToServer();
                    break;
                case 'menuSaveAsProg':
                    PROGRAM_C.showSaveAsModal();
                    break;
                case 'menuShowCode':
                    $('#codeButton').clickWrap();
                    break;
                case 'menuSourceCodeEditor':
                    SOURCECODE_C.clickSourceCodeEditor();
                    break;
                case 'menuExportProg':
                    PROGRAM_C.exportXml();
                    break;
                case 'menuExportAllProgs':
                    PROGRAM_C.exportAllXml();
                    break;
                case 'menuLinkProg':
                    PROGRAM_C.linkProgram();
                    break;
                case 'menuToolboxBeginner':
                    $('.levelTabs a[href="#beginner"]').tabWrapShow();
                    break;
                case 'menuToolboxExpert':
                    $('.levelTabs a[href="#expert"]').tabWrapShow();
                    break;
                case 'menuDefaultFirmware':
                    RUN_C.reset2DefaultFirmware();
                    break;
                default:
                    break;
            }
        };
        WRAP.wrapUI(fn, 'edit menu click')(event);
    });

    // CONF Menu
    $('#head-navigation-configuration-edit').onWrap(
        'click',
        '.dropdown-menu li:not(.disabled) a',
        function (event) {
            $('.modal').modal('hide'); // close all opened popups
            switch (event.target.id) {
                case 'menuCheckConfig':
                    MSG.displayMessage('MESSAGE_NOT_AVAILABLE', 'POPUP', '');
                    break;
                case 'menuNewConfig':
                    CONFIGURATION_C.newConfiguration();
                    break;
                case 'menuListConfig':
                    $('#tabConfList').tabWrapShow();
                    break;
                case 'menuSaveConfig':
                    CONFIGURATION_C.saveToServer();
                    break;
                case 'menuSaveAsConfig':
                    CONFIGURATION_C.showSaveAsModal();
                    break;
                default:
                    break;
            }
        },
        'configuration edit clicked'
    );

    // ROBOT Menu
    $('#head-navigation-robot').onWrap(
        'click',
        '.dropdown-menu li:not(.disabled) a',
        function (event) {
            $('.modal').modal('hide');
            var choosenRobotType = event.target.parentElement.dataset.type;
            //TODO: change from ardu to botnroll and mbot with friends
            //I guess it is changed now, check downstairs at menuConnect
            if (choosenRobotType) {
                ROBOT_C.switchRobot(choosenRobotType);
            } else {
                var domId = event.currentTarget.id;
                if (domId === 'menuConnect') {
                    if (
                        GUISTATE_C.getConnection() == 'arduinoAgent' ||
                        (GUISTATE_C.getConnection() == 'arduinoAgentOrToken' && GUISTATE_C.getIsAgent() == true)
                    ) {
                        var ports = SOCKET_C.getPortList();
                        var robots = SOCKET_C.getRobotList();
                        $('#singleModalListInput').empty();
                        let i = 0;
                        ports.forEach(function (port) {
                            $('#singleModalListInput').append('<option value="' + port + '" selected>' + robots[i] + ' ' + port + '</option>');
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
                } else if (domId === 'menuRobotSwitch') {
                    backToStartView();
                }
            }
        },
        'robot clicked'
    );

    $('#head-navigation-help').onWrap(
        'click',
        '.dropdown-menu li:not(.disabled) a',
        function (event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuShowStart') {
                // Submenu 'Help'
                backToStartView();
            } else if (domId === 'menuAbout') {
                // Submenu 'Help'
                $('#version').text(GUISTATE_C.getServerVersion());
                $('#show-about').modal('show');
            } else if (domId === 'menuLogging') {
                $('#tabLogList').tabWrapShow();
            }
        },
        'help clicked'
    );

    $('#head-navigation-user').onWrap(
        'click',
        '.dropdown-menu li:not(.disabled) a',
        function (event) {
            $('.modal').modal('hide'); // close all opened popups
            switch (event.target.id) {
                case 'menuUserGroupLogin':
                    USER_C.showUserGroupLoginForm();
                    break;
                case 'menuLogout':
                    USER_C.logout();
                    break;
                case 'menuGroupPanel':
                    $('#tabUserGroupList').tabWrapShow();
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
        },
        'user clicked'
    );

    $('#logoShowStart').onWrap('click', () => backToStartView());

    $('#menuTabProgram').onWrap(
        'click',
        '',
        function (event) {
            if ($('#tabSimulation').hasClass('tabClicked')) {
                $('.scroller-left').clickWrap();
            }
            $('.scroller-left').clickWrap();
            $('#tabProgram').tabWrapShow();
        },
        'tabProgram clicked'
    );

    $('#head-navigation-gallery').onWrap(
        'click',
        function (event) {
            $('#tabGalleryList').tabWrapShow();
            return false;
        },
        'gallery clicked'
    );
    $('#head-navigation-tutorial').onWrap(
        'click',
        function (event) {
            $('#tabTutorialList').tabWrapShow();
            return false;
        },
        'tutorial clicked'
    );

    $('#menuTabConfiguration').onWrap(
        'click',
        '',
        function (event) {
            if ($('#tabProgram').hasClass('tabClicked')) {
                $('.scroller-right').clickWrap();
            } else if ($('#tabConfiguration').hasClass('tabClicked')) {
                $('.scroller-right').clickWrap();
            }
            $('#tabConfiguration').clickWrap();
        },
        'tabConfiguration clicked'
    );
    $('#menuTabNN').onWrap(
        'click',
        '',
        function (event) {
            if ($('#tabProgram').hasClass('tabClicked')) {
                $('.scroller-right').clickWrap();
            } else if ($('#tabConfiguration').hasClass('tabClicked')) {
                $('.scroller-right').clickWrap();
            } else if ($('#tabNN').hasClass('tabClicked')) {
                $('.scroller-right').clickWrap();
            }
            $('#tabNN').clickWrap();
        },
        'tabNN clicked'
    );
    $('#menuTabNNLearn').onWrap(
        'click',
        '',
        function (event) {
            if ($('#tabProgram').hasClass('tabClicked')) {
                $('.scroller-right').clickWrap();
            } else if ($('#tabConfiguration').hasClass('tabClicked')) {
                $('.scroller-right').clickWrap();
            } else if ($('#tabNNlearn').hasClass('tabClicked')) {
                $('.scroller-right').clickWrap();
            }
            $('#tabNNlearn').clickWrap();
        },
        'tabNNlearn clicked'
    );

    // Close submenu on mouseleave
    $('.navbar-fixed-top').on('mouseleave', function (event) {
        $('.navbar-fixed-top .dropdown').removeClass('open');
    });

    $('.menuGeneral').onWrap(
        'click',
        function (event) {
            window.open('https://jira.iais.fraunhofer.de/wiki/display/ORInfo');
        },
        'head navigation menu item general clicked'
    );
    $('.menuFaq').onWrap(
        'click',
        function (event) {
            window.open('https://jira.iais.fraunhofer.de/wiki/display/ORInfo/FAQ');
        },
        'head navigation menu item faq clicked'
    );
    $('.shortcut').onWrap(
        'click',
        function (event) {
            window.open('https://jira.iais.fraunhofer.de/wiki/display/ORInfo/FAQ');
        },
        'head navigation menu item faq (shortcut) clicked'
    );
    $('.menuAboutProject').onWrap(
        'click',
        function (event) {
            if (GUISTATE_C.getLanguage() == 'de') {
                window.open('https://www.roberta-home.de/index.php?id=135');
            } else {
                window.open('https://www.roberta-home.de/index.php?id=135&L=1');
            }
        },
        'head navigation menu item about clicked'
    );
    $('.menuLogin').onWrap(
        'click',
        function (event) {
            USER_C.showLoginForm();
        },
        'head navigation menu item login clicked'
    );
    $('.menuImportProg').onWrap('click', function (event) {
        IMPORT_C.importXml();
    }),
        'import program clicked';

    $('#startPopupBack').on('click', function (event) {
        $('#popup-robot-main').removeClass('hidden', 1000);
        $('.popup-robot.robotSubGroup').addClass('hidden', 1000);
        $('.robotSpecial').removeClass('robotSpecial');
        $('#startPopupBack').addClass('hidden');
        $('#popup-robot-main').slick('refresh');
    });
    var mousex = 0;
    var mousey = 0;
    $('.popup-robot').on('mousedown', function (event) {
        mousex = event.clientX;
        mousey = event.clientY;
    });
    $('.popup-robot').onWrap(
        'click',
        function (event) {
            if (Math.abs(event.clientX - mousex) >= 3 || Math.abs(event.clientY - mousey) >= 3) {
                return;
            }
            event.preventDefault();
            $('#startPopupBack').clickWrap();
            var choosenRobotType = event.target.dataset.type || event.currentTarget.dataset.type;
            var choosenRobotGroup = event.target.dataset.group || event.currentTarget.dataset.group;
            if (event.target.className.indexOf('info') >= 0) {
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
                            UTIL.cleanUri(); // removes # which may potentially be added by other operations
                            var uri = window.location.toString();
                            uri += QUERY_START + LOAD_SYSTEM_CALL + QUERY_ASSIGNMENT + choosenRobotType;
                            window.history.replaceState({}, document.title, uri);

                            $('#show-message').oneWrap('hidden.bs.modal', function (e) {
                                e.preventDefault();
                                UTIL.cleanUri();
                                ROBOT_C.switchRobot(choosenRobotType, true);
                            });
                            MSG.displayMessage('POPUP_CREATE_BOOKMARK', 'POPUP', '');
                        } else {
                            ROBOT_C.switchRobot(choosenRobotType, true);
                        }
                    }
                }

                $('#show-startup-message').modal('hide');
            }
        },
        'robot choosen in start popup'
    );

    $('#moreReleases').onWrap(
        'click',
        function (event) {
            $('#oldReleases').show({
                start: function () {
                    $('#moreReleases').addClass('hidden');
                },
            });
        },
        'show more releases clicked'
    );

    $('#goToWiki').onWrap(
        'click',
        function (event) {
            event.preventDefault();
            window.open('https://jira.iais.fraunhofer.de/wiki/display/ORInfo', '_blank');
            event.stopPropagation();
            $('#show-startup-message').modal('show');
        },
        'go to wiki clicked'
    );

    // init popup events

    $('.cancelPopup').onWrap(
        'click',
        function () {
            $('.ui-dialog-titlebar-close').clickWrap();
        },
        'cancel popup clicked'
    );

    $('#about-join').onWrap(
        'click',
        function () {
            $('#show-about').modal('hide');
        },
        'hide show about clicked'
    );

    $(window).on('beforeunload', function (e) {
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

    // help Bootstrap to calculate the correct size for the collapse element when the screen height is smaller than the elements height.
    $('#navbarCollapse').on('shown.bs.collapse', function () {
        var newHeight = Math.min($(this).height(), Math.max($('#blocklyDiv').height(), $('#brickly').height(), $('#nn').height()));
        $(this).css('height', newHeight);
    });

    $(document).onWrap('keydown', function (e) {
        if (GUISTATE_C.getView() != 'tabProgram') {
            return;
        }
        //Overriding the Ctrl + 2 for creating a debug block
        if ((e.metaKey || e.ctrlKey) && e.which == 50) {
            e.preventDefault();
            var debug = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_debug');
            debug.initSvg();
            debug.render();
            debug.setInTask(false);

            return false;
        }
        //Overriding the Ctrl + 3 for creating an assertion block
        if ((e.metaKey || e.ctrlKey) && e.which == 51) {
            e.preventDefault();
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
        //Overriding the Ctrl + 4 for creating an evaluate expression block
        if ((e.metaKey || e.ctrlKey) && e.which == 52) {
            e.preventDefault();
            var expr = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_eval_expr');
            expr.initSvg();
            expr.render();
            expr.setInTask(false);
            return false;
        }
        //Overriding the Ctrl + 5 for creating an evaluate statement block
        if ((e.metaKey || e.ctrlKey) && e.which == 53) {
            e.preventDefault();
            var expr = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_eval_stmt');
            expr.initSvg();
            expr.render();
            expr.setInTask(false);
            return false;
        }
        //Overriding the Ctrl + S for saving the program in the database on the server
        if ((e.metaKey || e.ctrlKey) && e.which == 83) {
            e.preventDefault();
            if (GUISTATE_C.isUserLoggedIn()) {
                if (GUISTATE_C.getProgramName() === 'NEPOprog' || e.shiftKey) {
                    PROGRAM_C.showSaveAsModal();
                } else if (!GUISTATE_C.isProgramSaved()) {
                    PROGRAM_C.saveToServer();
                }
            } else {
                MSG.displayMessage('ORA_PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN', 'POPUP', '');
            }
        }
        //Overriding the Ctrl + R for running the program
        if ((e.metaKey || e.ctrlKey) && e.which == 82) {
            e.preventDefault();
            if (GUISTATE_C.isRunEnabled()) {
                RUN_C.runOnBrick();
            }
        }
        //Overriding the Ctrl + M for viewing all programs
        if ((e.metaKey || e.ctrlKey) && e.which == 77) {
            e.preventDefault();
            if (GUISTATE_C.isUserLoggedIn()) {
                $('#progList').trigger('Programs');
                $('.navbar-nav a[href="#progList"]').tab('show');
            } else {
                MSG.displayMessage('ORA_PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN', 'POPUP', '');
            }
        }
        //Overriding the Ctrl + I for importing NEPO Xml
        if ((e.metaKey || e.ctrlKey) && e.which == 73) {
            e.preventDefault();
            IMPORT_C.importXml();
            return false;
        }
        //Overriding the Ctrl + E for exporting the NEPO code
        if ((e.metaKey || e.ctrlKey) && e.which == 69) {
            e.preventDefault();
            PROGRAM_C.exportXml();
            return false;
        }
    });
}
