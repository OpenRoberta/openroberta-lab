import * as MSG from 'message';
import * as COMM from 'comm';
import * as WRAP from 'wrap';
import * as USER_C from 'user.controller';
import * as NOTIFICATION_C from 'notification.controller';
import * as GUISTATE_C from 'guiState.controller';
import * as PROGRAM_C from 'program.controller';
import * as RUN_C from 'progRun.controller';
import * as CONFIGURATION_C from 'configuration.controller';
import * as IMPORT_C from 'import.controller';
import * as TOUR_C from 'tour.controller';
import * as SOURCECODE_C from 'sourceCodeEditor.controller';
import * as TUTORIAL_C from 'progTutorial.controller';
import * as UTIL from 'util.roberta';
import * as CONNECTION_C from 'connection.controller';
// @ts-ignore
import * as Blockly from 'blockly';

let n = 0;

const QUERY_START = '?';
const QUERY_DELIMITER = '&';
const QUERY_ASSIGNMENT = '=';
const Q_FORGOT_PASSWORD = 'forgotPassword';
const Q_ACTIVATE_ACCOUNT = 'activateAccount';
const Q_LOAD_SYSTEM = 'loadSystem';
const Q_TUTORIAL = 'tutorial';
const Q_GALLERY = 'gallery';
const Q_TOUR = 'tour';
const Q_KIOSK = 'kiosk';
const Q_EXAMPLE_VIEW = 'exampleView';
const Q_LOAD_PROGRAM = 'loadProgram';
const Q_EXTENSIONS = 'extensions';
let mainCallback: Function;

// from https://stackoverflow.com/questions/19491336/get-url-parameter-jquery-or-how-to-get-query-string-values-in-js/21903119#21903119
function getUrlParameter(sParam: string): string {
    let url: string = decodeURIComponent(document.location.toString());
    const queryStart: number = url.indexOf('?');
    url = url.substring(queryStart + 1);
    let sPageURL: string = url;
    const xmlStart: number = sPageURL.indexOf('<');
    let lastParam: string;
    if (xmlStart >= 0) {
        sPageURL = url.substring(0, xmlStart);
        lastParam = url.substring(xmlStart);
    }
    const sURLVariables: string[] = sPageURL.split(QUERY_DELIMITER);
    let sParameterName: string[];
    for (let i: number = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split(QUERY_ASSIGNMENT);
        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? undefined : sParameterName[0] === 'loadProgram' ? lastParam : sParameterName[1];
        }
    }
}

function handleQuery() {
    let location: URL = new URL(document.location.toString());
    let domain: string = location.protocol + '//' + location.host;
    let oldStyle: boolean = (location.hash && location.toString().indexOf('#') <= domain.length + 1) || false;
    let newStyle: boolean = location.search !== '' || false;
    /* old style queries, start with # *** deprecated ***
        e.g.
        localhost:1999#overview
        localhost:1999#loadSystem&&ev3lejosv1
        localhost:1999#tutorial
        localhost:1999#tutorial&&bionics4education1
        localhost:1999#tutorial&&bionics4education1&&kiosk
     */
    if (oldStyle) {
        let deprecated: boolean = true;
        let newUrl: string;
        const target: string[] = decodeURI(document.location.hash).split('&&');
        if (target[0] === '#overview') {
            GUISTATE_C.setStartWithoutPopup();
            mainCallback('ev3lejosv1', {}, function () {
                PROGRAM_C.newProgram(true);
                TOUR_C.start('overview');
            });
            newUrl = domain + QUERY_START + 'tour' + QUERY_ASSIGNMENT + 'overview';
        } else if (target[0] === '#loadProgram' && target.length >= 4) {
            GUISTATE_C.setStartWithoutPopup();
            mainCallback && mainCallback instanceof Function && mainCallback(target[1], {}, IMPORT_C.loadProgramFromXML, [target[2], target[3]]);
            newUrl = domain + QUERY_START + 'loadSystem' + QUERY_ASSIGNMENT + target[1] + QUERY_DELIMITER + 'loadProgram' + QUERY_ASSIGNMENT + target[3];
        } else if (target[0] === '#loadSystem' && target.length >= 2) {
            GUISTATE_C.setStartWithoutPopup();
            mainCallback && mainCallback instanceof Function && mainCallback(target[1], {});
            newUrl = domain + QUERY_START + 'loadSystem' + QUERY_ASSIGNMENT + target[1];
        } else if (target[0] === '#gallery') {
            deprecated = false;
            newUrl = domain + QUERY_START + 'loadSystem' + QUERY_ASSIGNMENT + '<ROBOT_SYSTEM>' + QUERY_DELIMITER + Q_GALLERY;
        } else if (target[0] === '#tutorial') {
            deprecated = false;
            newUrl = domain + QUERY_START + 'loadSystem' + QUERY_ASSIGNMENT + '<ROBOT_SYSTEM>' + QUERY_DELIMITER + Q_TUTORIAL;
            if (target.length > 1) {
                newUrl += QUERY_ASSIGNMENT + target[1];
                if (target.length > 2) {
                    newUrl += QUERY_DELIMITER + target[2];
                }
            }
        }
        let message: string;
        let germanDeprecated =
            'Die eingegebenen URL-Parameter sind in dieser Form veraltet und werden bald nicht mehr unterstützt.\nBitte verwende ab sofort nur noch folgende Schreibweise:\n';
        let englishDeprecated =
            'The URL parameters entered are outdated in this form and will soon no longer be supported.\n' +
            'Please use only the following spelling from now on:';
        let germanNotDeprecated =
            'Die eingegebenen URL-Parameter sind in dieser Form veraltet und werden nicht mehr unterstützt.\nBitte verwende ab sofort nur noch folgende Schreibweise:\n';
        let englishNotDeprecated =
            'The URL parameters entered are outdated in this form and are no longer supported.\n' + 'Please use only the following spelling from now on:';
        if (GUISTATE_C.getLanguage() === 'de' && deprecated) {
            message = germanDeprecated;
        } else if (GUISTATE_C.getLanguage() === 'en' && deprecated) {
            message = englishDeprecated;
        } else if (GUISTATE_C.getLanguage() === 'de' && !deprecated) {
            message = germanNotDeprecated;
        } else if (GUISTATE_C.getLanguage() === 'en' && !deprecated) {
            message = englishNotDeprecated;
        }
        message += newUrl;
        alert(message);
    }
    /* new style queries, start with ?
        e.g.
        localhost:1999?forgotPassword=_vC353v-LPr_
        localhost:1999?activateAccount=_vC353v-LPr_
        localhost:1999?tour=overview
        localhost:1999?tour=welcome
        localhost:1999?loadSystem=ev3lejosv1
        localhost:1999?loadSystem=ev3lejosv1&tutorial
        localhost:1999?loadSystem=ev3lejosv1&tutorial=bionics4education1
        localhost:1999?loadSystem=ev3lejosv1&tutorial=bionics4education1&kiosk
        localhost:1999?loadSystem=ev3lejosv1&exampleView
        localhost:1999?loadSystem=ev3lejosv1&loadProgram=<export> ... </export>
        localhost:1999?loadSystem=ev3lejosv1&extensions=nn,...
     */
    if (newStyle) {
        const forgotPasswort = getUrlParameter(Q_FORGOT_PASSWORD);
        if (forgotPasswort) {
            USER_C.showResetPassword(forgotPasswort);
        }
        const activateAccount = getUrlParameter(Q_ACTIVATE_ACCOUNT);
        if (activateAccount) {
            USER_C.activateAccount(activateAccount);
        }
        const tour = getUrlParameter(Q_TOUR);
        if (tour) {
            GUISTATE_C.setStartWithoutPopup();
            mainCallback('ev3lejosv1', {}, function () {
                PROGRAM_C.newProgram(true);
                TOUR_C.start(tour);
            });
        }
        let loadSystem = getUrlParameter(Q_LOAD_SYSTEM);
        if (loadSystem !== undefined) {
            GUISTATE_C.setStartWithoutPopup();
            let callback: Function;
            let extend = {};
            let parameter: string[] = [];
            let tutorial: string = getUrlParameter(Q_TUTORIAL);
            let loadProgram: string = getUrlParameter(Q_LOAD_PROGRAM);
            let exampleView: string = getUrlParameter(Q_EXAMPLE_VIEW);
            let gallery: string = getUrlParameter(Q_GALLERY);
            let extensions: string = getUrlParameter(Q_EXTENSIONS);
            if (tutorial) {
                let kiosk = getUrlParameter(Q_KIOSK);
                if (kiosk) {
                    GUISTATE_C.setKioskMode(true);
                }
                callback = function (tutorial: any) {
                    TUTORIAL_C.loadFromTutorial(tutorial);
                };
                parameter.push(tutorial);
            } else if (loadProgram) {
                callback = IMPORT_C.loadProgramFromXML;
                parameter.push('NEPOprog');
                parameter.push(loadProgram);
            } else if (exampleView) {
                callback = function () {
                    $('#menuListExamples').clickWrap();
                };
            } else if (gallery) {
                callback = function () {
                    $('#tabGalleryList').tabWrapShow();
                };
            } else if (extensions) {
                let extensionsArray = extensions.split(',');
                extensionsArray.forEach(function (extension: string) {
                    // TODO remove this when the server can handle all extensions -> robot variants
                    if (loadSystem === 'calliope2017NoBlue' && extension === 'blue') {
                        loadSystem = 'calliope2017';
                    } else {
                        extend[extension] = true;
                    }
                });
            }
            if (mainCallback && mainCallback instanceof Function) {
                mainCallback(loadSystem, extend, callback, parameter);
            }
        }
    }
}

export function init(callback: Function) {
    if (callback && callback instanceof Function) {
        mainCallback = callback;
    } else {
        alert('Problem');
        return;
    }
    initMenuEvents();
    /**
     * Regularly ping the server to keep status information up-to-date
     */
    function pingServer() {
        setTimeout(function () {
            n += 1000;
            if (n >= GUISTATE_C.getPingTime() && GUISTATE_C.doPing()) {
                COMM.ping(function (result: any) {
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
    if (CONNECTION_C.getConnectionInstance() != null) {
        CONNECTION_C.getConnectionInstance().terminate();
    }
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
    $('.navbar-collapse a:not(.dropdown-toggle)').onWrap('click', function () {
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
        $(this).find('[autofocus]').trigger('focus');
    });

    /* TODO $('#navbarCollapse').collapse({
        toggle: false,
    });*/

    $('.navbar-collapse').on('click', '.dropdown-menu a,.visible-xs', function () {
        $('#navbarCollapse').collapse('hide');
    });
    // for gallery
    $('#head-navigation-gallery').on('click', 'a,.visible-xs', function () {
        $('#navbarCollapse').collapse('hide');
    });
    if (GUISTATE_C.isPublicServerVersion()) {
        const feedbackButton =
            '<div href="#" id="feedbackButton" class="rightMenuButton" rel="tooltip" data-bs-original-title="" title="">' +
            '<span id="" class="feedbackButton typcn typcn-feedback"></span>' +
            '</div>';
        $('#rightMenuDiv').append(feedbackButton);
        window.onmessage = function (msg: { data: string }) {
            if (msg.data === 'closeFeedback') {
                $('#feedbackIframe').oneWrap('load', function () {
                    setTimeout(function () {
                        $('#feedbackIframe').attr('src', 'about:blank');
                        $('#feedbackModal').modal('hide');
                    }, 1000);
                });
            } else if (msg.data.indexOf('feedbackHeight') >= 0) {
                const height = msg.data.split(':')[1] || 400;
                $('#feedbackIframe').height(height);
            }
        };
        $('#feedbackButton').on('click', '', function () {
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
        const fn = function (event: Event) {
            let target: HTMLElement = event.target as HTMLElement;
            let currentTarget: HTMLElement = event.currentTarget as HTMLElement;
            const targetId = target.id || currentTarget.id;
            switch (targetId) {
                case 'menuRunProg':
                    RUN_C.runOnBrick();
                    break;
                case 'menuRunSim':
                    $('#simButton').clickWrap();
                    break;
                case 'menuCheckProg':
                    // @ts-ignore
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
                case 'menuImportProg':
                    IMPORT_C.importXml();
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
                    CONNECTION_C.getConnectionInstance().reset2DefaultFirmware();
                    break;
                default:
                    break;
            }
        };
        WRAP.wrapUI(fn, 'edit menu click')(event);
    });

    // CONF Menu
    $('#head-navigation-configuration-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function (event: Event) {
        let target: HTMLElement = event.target as HTMLElement;
        let currentTarget: HTMLElement = event.currentTarget as HTMLElement;
        $('.modal').modal('hide'); // close all opened popups
        const targetId = target.id || currentTarget.id;
        switch (targetId) {
            case 'menuCheckConfig':
                MSG.displayMessage('MESSAGE_NOT_AVAILABLE', 'POPUP', '', null, null);
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
    });

    // ROBOT Menu
    $('#head-navigation-robot').onWrap('click', '.dropdown-menu li:not(.disabled) a', function (event: Event) {
        $('.modal').modal('hide');
        let target = event.currentTarget as HTMLElement;
        if (target.id === 'menuConnect') {
            CONNECTION_C.getConnectionInstance().showConnectionModal();
        } else if (target.id === 'menuRobotInfo') {
            CONNECTION_C.getConnectionInstance().showRobotInfo();
        } else if (target.id === 'menuWlan') {
            CONNECTION_C.getConnectionInstance().showWlanModal();
        } else if (target.id === 'menuRobotSwitch') {
            backToStartView();
        }
    });

    $('#head-navigation-help').onWrap('click', '.dropdown-menu li:not(.disabled) a', function (event: Event) {
        $('.modal').modal('hide'); // close all opened popups
        let target: HTMLElement = event.target as HTMLElement;
        if (target.id === 'menuAbout') {
            $('#version').text(GUISTATE_C.getServerVersion());
            $('#show-about').modal('show');
        } else if (target.id === 'menuLogging') {
            $('#tabLogList').tabWrapShow();
        }
    });
    $('.menuGeneral').onWrap(
        'click',
        function () {
            window.open('https://jira.iais.fraunhofer.de/wiki/display/ORInfo');
        },
        'head navigation menu item general clicked'
    );
    $('.menuFaq').onWrap(
        'click',
        function () {
            window.open('https://jira.iais.fraunhofer.de/wiki/display/ORInfo/FAQ');
        },
        'head navigation menu item faq clicked'
    );
    $('.menuAboutProject').onWrap(
        'click',
        function () {
            if (GUISTATE_C.getLanguage() == 'de') {
                window.open('https://www.roberta-home.de/index.php?id=135');
            } else {
                window.open('https://www.roberta-home.de/index.php?id=135&L=1');
            }
        },
        'head navigation menu item about clicked'
    );
    $('.menuShowStart').onWrap('click', () => backToStartView());

    $('#head-navigation-user').onWrap('click', '.dropdown-menu li:not(.disabled) a', function (event: Event) {
        let target: HTMLElement = event.target as HTMLElement;
        $('.modal').modal('hide'); // close all opened popups
        switch (target.id) {
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
    });
    $('.menuLogin').onWrap(
        'click',
        function () {
            USER_C.showLoginForm();
        },
        'head navigation menu item login clicked'
    );

    $('#head-navigation-gallery').onWrap(
        'click',
        function () {
            $('#tabGalleryList').tabWrapShow();
            return false;
        },
        'gallery clicked'
    );
    $('#head-navigation-tutorial').onWrap(
        'click',
        function () {
            $('#tabTutorialList').tabWrapShow();
            return false;
        },
        'tutorial clicked'
    );

    // Close submenu on mouseleave
    $('.navbar-fixed-top').on('mouseleave', function () {
        $('.navbar-fixed-top .dropdown').removeClass('open');
    });

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

    $(window).on('beforeunload', function () {
        return 'ok';
    });

    // help Bootstrap to calculate the correct size for the collapse element when the screen height is smaller than the element's height.
    $('#navbarCollapse').on('shown.bs.collapse', function () {
        const newHeight = Math.min($(this).height(), Math.max($('#blocklyDiv').height(), $('#brickly').height(), $('#nn').height()));
        $(this).css('height', newHeight);
    });

    $(document).onWrap('keydown', function (e: any) {
        if (GUISTATE_C.getView() === 'tabProgram') {
            //Overriding the Ctrl + 2 for creating a debug block
            if ((e.metaKey || e.ctrlKey) && e.which == 50) {
                e.preventDefault();
                const debug = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_debug');
                debug.initSvg();
                debug.render();
                debug.setInTask(false);

                return false;
            }
            //Overriding the Ctrl + 3 for creating an assertion block
            if ((e.metaKey || e.ctrlKey) && e.which == 51) {
                e.preventDefault();
                const assert = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_assert');
                assert.initSvg();
                assert.setInTask(false);
                assert.render();
                const logComp = GUISTATE_C.getBlocklyWorkspace().newBlock('logic_compare');
                logComp.initSvg();
                logComp.setMovable(false);
                logComp.setInTask(false);
                logComp.setDeletable(false);
                logComp.render();
                const parentConnection = assert.getInput('OUT').connection;
                const childConnection = logComp.outputConnection;
                parentConnection.connect(childConnection);
                return false;
            }
            //Overriding the Ctrl + 4 for creating an evaluate expression block
            if ((e.metaKey || e.ctrlKey) && e.which == 52) {
                e.preventDefault();
                let expr = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_eval_expr');
                expr.initSvg();
                expr.render();
                expr.setInTask(false);
                return false;
            }
            //Overriding the Ctrl + 5 for creating an evaluate statement block
            if ((e.metaKey || e.ctrlKey) && e.which == 53) {
                e.preventDefault();
                let expr = GUISTATE_C.getBlocklyWorkspace().newBlock('robActions_eval_stmt');
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
                    MSG.displayMessage('ORA_PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN', 'POPUP', '', null, null);
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
                    $('#tabProgList').tabWrapShow();
                } else {
                    MSG.displayMessage('ORA_PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN', 'POPUP', '', null, null);
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
        } else {
            if (GUISTATE_C.getView() === 'tabConfiguration') {
                //Overriding the Ctrl + S for saving the configuration in the database on the server
                if ((e.metaKey || e.ctrlKey) && e.which == 83) {
                    e.preventDefault();
                    if (GUISTATE_C.isUserLoggedIn()) {
                        if (GUISTATE_C.isConfigurationStandard() || e.shiftKey) {
                            CONFIGURATION_C.showSaveAsModal();
                        } else if (!GUISTATE_C.isProgramSaved()) {
                            CONFIGURATION_C.saveToServer();
                        }
                    } else {
                        MSG.displayMessage('ORA_PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN', 'POPUP', '', null, null);
                    }
                }
                //Overriding the Ctrl + M for viewing all configurations
                if ((e.metaKey || e.ctrlKey) && e.which == 77) {
                    e.preventDefault();
                    if (GUISTATE_C.isUserLoggedIn()) {
                        $('#tabConfList').tabWrapShow();
                    } else {
                        MSG.displayMessage('ORA_PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN', 'POPUP', '', null, null);
                    }
                }
            }
        }
    });
}
