import * as LOG from 'log';
import * as $ from 'jquery';
import 'bootstrap';
import * as GUISTATE_C from 'guiState.controller';
import * as Blockly from 'blockly';

/**
 * Initialize language switching
 */
function init() {
    var ready = new $.Deferred();
    var language;
    if (navigator.language.indexOf('de') > -1) {
        language = 'de';
    } else if (navigator.language.indexOf('fi') > -1) {
        language = 'fi';
    } else if (navigator.language.indexOf('da') > -1) {
        language = 'da';
    } else if (navigator.language.indexOf('es') > -1) {
        language = 'es';
    } else if (navigator.language.indexOf('fr') > -1) {
        language = 'fr';
    } else if (navigator.language.indexOf('it') > -1) {
        language = 'it';
    } else if (navigator.language.indexOf('ca') > -1) {
        language = 'ca';
    } else if (navigator.language.indexOf('pt') > -1) {
        language = 'pt';
    } else if (navigator.language.indexOf('pl') > -1) {
        language = 'pl';
    } else if (navigator.language.indexOf('ru') > -1) {
        language = 'ru';
    } else if (navigator.language.indexOf('be') > -1) {
        language = 'be';
    } else if (navigator.language.indexOf('cs') > -1) {
        language = 'cs';
    } else if (navigator.language.indexOf('tr') > -1) {
        language = 'tr';
    } else if (navigator.language.indexOf('nl') > -1) {
        language = 'nl';
    } else if (navigator.language.indexOf('sv') > -1) {
        language = 'sv';
    } else if (navigator.language.indexOf('zh-hans') > -1) {
        language = 'zh-hans';
    } else if (navigator.language.indexOf('zh-hant') > -1) {
        language = 'zh-hant';
    } else if (navigator.language.indexOf('ro') > -1) {
        language = 'ro';
    } else if (navigator.language.indexOf('eu') > -1) {
        language = 'eu';
    } else if (navigator.language.indexOf('uk') > -1) {
        language = 'uk';
    } else {
        language = 'en';
    }
    if (language === 'de') {
        $('.EN').css('display', 'none');
        $('.DE').css('display', 'inline');
        $('li>a.DE').css('display', 'block');
    } else {
        $('.DE').css('display', 'none');
        $('.EN').css('display', 'inline');
        $('li>a.EN').css('display', 'block');
    }
    $('#language li a[lang=' + language + ']')
        .parent()
        .addClass('disabled');
    var url = 'blockly/msg/js/' + language + '.js';
    getCachedScript(url).done(function (data) {
        translate();
        ready.resolve(language);
    });

    initEvents();
    return ready.promise(language);
}

function initEvents() {
    $('#language').onWrap('click', 'li a', function () {
        LOG.info('language clicked');
        var language = $(this).attr('lang');
        switchLanguage(language);
    }),
        'switch language clicked';
}

function switchLanguage(language) {
    if (GUISTATE_C.getLanguage == language) {
        return;
    }

    var url = 'blockly/msg/js/' + language.toLowerCase() + '.js';
    getCachedScript(url).done(function (data) {
        translate();
        GUISTATE_C.setLanguage(language);
    });
    LOG.info('language switched to ' + language);
}

/**
 * Translate the web page
 */
function translate($domElement) {
    if (!$domElement || typeof $domElement !== 'object' || !$domElement.length) {
        $domElement = $(document.body);
    }

    $domElement.find('[lkey]').each(function (index) {
        var lkey = $(this).attr('lkey');
        var key, value;
        if (lkey.toString().indexOf('+') > -1) {
            key = lkey.split('+').map((k) => k.trim().replace('Blockly.Msg.', '')); //.forEach((k) => k.replace('Blockly.Msg.', ''));
            value = key.map((k) => Blockly.Msg[k]).join('');
        } else {
            key = lkey.replace('Blockly.Msg.', '');
            value = Blockly.Msg[key];
        }
        if (value == undefined) {
            console.log('UNDEFINED    key : value = ' + key + ' : ' + value);
        }
        $(this).attr('data-bs-original-title', value);
        if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_PLACEHOLDER') {
            $('#sourceCodeEditorTextArea').attr('placeholder', value);
        } else if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_UPLOAD_TOOLTIP') {
            $('#uploadSourceCodeEditor').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_IMPORT_TOOLTIP') {
            $('#importSourceCodeEditor').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_BUILD_TOOLTIP') {
            $('#buildSourceCodeEditor').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_RUN_TOOLTIP') {
            $('#runSourceCodeEditor').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_EDIT_TOOLTIP') {
            $('#head-navi-tooltip-program').attr('data-bs-original-title', value);
            $('#head-navi-tooltip-configuration').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_RIGHT_CODE_TOOLTIP') {
            $('#codeButton').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_RIGHT_SIM_TOOLTIP') {
            $('#simButton').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_RIGHT_SIM_DEBUG_TOOLTIP') {
            $('#simDebugButton').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_RIGHT_INFO_TOOLTIP') {
            $('#infoButton').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_RIGHT_HELP_TOOLTIP') {
            $('#helpButton').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_RIGHT_LEGAL_TOOLTIP') {
            $('#legalButton').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_ROBOT_TOOLTIP') {
            $('#head-navi-tooltip-robot').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_HELP_TOOLTIP') {
            $('#head-navi-tooltip-help').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_USER_TOOLTIP') {
            $('#head-navi-tooltip-user').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_GALLERY_TOOLTIP') {
            $('#head-navi-tooltip-gallery').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_LANGUAGE_TOOLTIP') {
            $('#head-navi-tooltip-language').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_USER_STATE_TOOLTIP') {
            $('#iconDisplayLogin').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_ROBOT_STATE_TOOLTIP') {
            $('#iconDisplayRobotState').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_START_TOOLTIP') {
            $('#simControl').attr('data-bs-original-title', value);
            $('#goto-sim') && $('#goto-sim').attr('data-bs-original-title', value).tooltip({ placement: 'top' });
            $('#explore-goto-sim') && $('#explore-goto-sim').attr('data-bs-original-title', value).tooltip({ placement: 'top' });
            $('#learn-goto-sim') && $('#learn-goto-sim').attr('data-bs-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey === 'Blockly.Msg.MENU_SIM_STOP_TOOLTIP') {
            $('#simCancel').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_SCENE_TOOLTIP') {
            $('#simScene').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_ADD_COLOR_OBJECT_TOOLTIP') {
            $('#simCustomColorObject').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_ADD_OBSTACLE_TOOLTIP') {
            $('#simCustomObstacle').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_ADD_MARKER_OBJECT_TOOLTIP') {
            $('#simMarkerObject').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_DELETE_ELEMENTS_TOOLTIP') {
            $('#simDeleteElements').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_CHANGE_COLOR_TOOLTIP') {
            $('#simChangeObjectColor').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_DELETE_OBJECT_TOOLTIP') {
            $('#simDeleteObject').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_CONFIG_EXPORT') {
            $('#simDownloadConfig').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_CONFIG_IMPORT') {
            $('#simUploadConfig').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.MENU_SIM_ROBOT_TOOLTIP') {
            $('#simRobot').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_SIM_VALUES_TOOLTIP') {
            $('#simValues').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_SIM_TRAIL_TOOLTIP') {
            $('#simTrail').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_SIM_IMPORT_TOOLTIP') {
            $('#simImport').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_SIM_POSE_TOOLTIP') {
            $('#simResetPose').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_DEBUG_START_TOOLTIP') {
            $('#debugMode').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_DEBUG_STEP_BREAKPOINT_TOOLTIP') {
            $('#simControlBreakPoint').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_DEBUG_STEP_INTO_TOOLTIP') {
            $('#simControlStepInto').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_DEBUG_STEP_OVER_TOOLTIP') {
            $('#simControlStepOver').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_CODE_DOWNLOAD_TOOLTIP') {
            $('#codeDownload').attr('data-bs-original-title', value);
            $('#downloadSourceCodeEditor').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_CODE_REFRESH_TOOLTIP') {
            $('#codeRefresh').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_TUTORIAL_TOOLTIP') {
            $('#head-navi-tooltip-tutorials').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.MENU_RIGHT_TUTORIAL_TOOLTIP') {
            $('#tutorialButton').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.BUTTON_EMPTY_LIST') {
            $('#logList>.bootstrap-table').find('button[name="refresh"]').attr('data-bs-original-title', value);
        } else if (lkey === 'Blockly.Msg.LIST_BACK_TOOLTIP') {
            $('.bootstrap-table').find('.backList').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.PROGLIST_DELETE_ALL_TOOLTIP') {
            $('#deleteSomeProg').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.PROGLIST_DELETE_TOOLTIP') {
            $('#programNameTable').find('.delete').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.PROGLIST_SHARE_TOOLTIP') {
            $('#programNameTable').find('.share').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.PROGLIST_SHARE_WITH_GALLERY_TOOLTIP') {
            $('#programNameTable').find('.gallery').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.PROGLIST_LOAD_TOOLTIP') {
            $('#programNameTable').find('.load').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.CONFLIST_DELETE_ALL_TOOLTIP') {
            $('#deleteSomeConf').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.CONFLIST_DELETE_TOOLTIP') {
            $('#confNameTable').find('.delete').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.CONFLIST_LOAD_TOOLTIP') {
            $('#confNameTable').find('.load').attr('data-bs-original-title', value);
        } else if (lkey == 'Blockly.Msg.OLDER_THEN_14' || lkey == 'Blockly.Msg.YOUNGER_THEN_14') {
            $(this).html(value);
        } else if (lkey == 'Blockly.Msg.NN_EXPLORE_RUN_FULL') {
            $('#nn-explore-run-full').attr('data-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey == 'Blockly.Msg.NN_EXPLORE_RUN_LAYER') {
            $('#nn-explore-run-layer').attr('data-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey == 'Blockly.Msg.NN_EXPLORE_RUN_NEURON') {
            $('#nn-explore-run-neuron').attr('data-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey == 'Blockly.Msg.NN_EXPLORE_RESET_VALUES') {
            $('#nn-explore-stop').attr('data-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey == 'Blockly.Msg.NN_LEARN_RUN') {
            $('#nn-learn-run').attr('data-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey == 'Blockly.Msg.NN_LEARN_EPOCH') {
            $('#nn-learn-run-epoch').attr('data-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey == 'Blockly.Msg.NN_LEARN_ONE_LINE') {
            $('#nn-learn-run-one-line').attr('data-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey == 'Blockly.Msg.NN_LEARN_RESET_VALUES') {
            $('#nn-learn-reset').attr('data-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey == 'Blockly.Msg.NN_LEARN_UPLOAD') {
            $('#nn-explore-upload').attr('data-original-title', value).tooltip({ placement: 'top' });
            $('#nn-learn-upload').attr('data-original-title', value).tooltip({ placement: 'top' });
        } else if (lkey == 'Blockly.Msg.NN_LEARN_UPLOAD_POPUP') {
            $('#nn-explore-upload-popup').attr('data-original-title', value).tooltip({ placement: 'top' });
            $('#nn-learn-upload-popup').attr('data-original-title', value).tooltip({ placement: 'top' });
            let searchPlaceholder = Blockly.Msg.START_FORMATSEARCH;
            $('#start input.form-control.search-input').attr('placeholder', searchPlaceholder);
        } else if ($(this).data('translationTargets')) {
            var attributeTargets = $(this).data('translationTargets').split(' ');
            for (var key in attributeTargets) {
                if (attributeTargets[key] === 'text' || attributeTargets[key] === 'html') {
                    $(this)[attributeTargets[key]](value);
                } else {
                    $(this).attr(attributeTargets[key], value);
                }
            }
        } else {
            $(this).html(value);
            $(this).attr('value', value);
        }
    });
    $('#start input.form-control.search-input').attr('placeholder', Blockly.Msg.START_FORMATSEARC);
}
export { init, translate };

/**
 * $.getScript() will append a timestamped query parameter to the url to
 * prevent caching. The cache control should be handled using http-headers.
 * see https://api.jquery.com/jquery.getscript/#caching-requests
 */
function getCachedScript(url, options) {
    // Allow user to set any option except for dataType, cache, and url
    options = $.extend(options || {}, {
        dataType: 'script',
        cache: true,
        url: url,
    });

    // Use $.ajax() since it is more flexible than $.getScript
    // Return the jqXHR object so we can chain callbacks
    return jQuery.ajax(options);
}
