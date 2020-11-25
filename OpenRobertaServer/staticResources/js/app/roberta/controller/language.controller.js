define(['exports', 'log', 'jquery', 'guiState.controller', 'program.controller', 'configuration.controller', 'user.controller', 'notification.controller', 'blockly'], function(exports, LOG, $,
    GUISTATE_C, PROGRAM_C, CONFIGURATION_C, USER_C, NOTIFICATION_C, Blockly) {

    /**
     * Initialize language switching
     */
    function init() {
        var ready = new $.Deferred();
        var language;
        if (navigator.language.indexOf("de") > -1) {
            language = 'de';
        } else if (navigator.language.indexOf("fi") > -1) {
            language = 'fi';
        } else if (navigator.language.indexOf("da") > -1) {
            language = 'da';
        } else if (navigator.language.indexOf("es") > -1) {
            language = 'es';
        } else if (navigator.language.indexOf("fr") > -1) {
            language = 'fr';
        } else if (navigator.language.indexOf("it") > -1) {
            language = 'it';
        } else if (navigator.language.indexOf("ca") > -1) {
            language = 'ca';
        } else if (navigator.language.indexOf("pt") > -1) {
            language = 'pt';
        } else if (navigator.language.indexOf("pl") > -1) {
            language = 'pl';
        } else if (navigator.language.indexOf("ru") > -1) {
            language = 'ru';
        } else if (navigator.language.indexOf("be") > -1) {
            language = 'be';
        } else if (navigator.language.indexOf("cs") > -1) {
            language = 'cs';
        } else if (navigator.language.indexOf("tr") > -1) {
            language = 'tr';
        } else if (navigator.language.indexOf("nl") > -1) {
            language = 'nl';
        } else if (navigator.language.indexOf("sv") > -1) {
            language = 'sv';
        } else if (navigator.language.indexOf("zh-hans") > -1) {
            language = 'zh-hans';
        } else if (navigator.language.indexOf("zh-hant") > -1) {
            language = 'zh-hant';
        } else if (navigator.language.indexOf("ro") > -1) {
            language = 'ro';
        } else if (navigator.language.indexOf("eu") > -1) {
            language = 'eu';
        } else if (navigator.language.indexOf("uk") > -1) {
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
        $('#language li a[lang=' + language + ']').parent().addClass('disabled');
        var url = 'blockly/msg/js/' + language + '.js';
        getCachedScript(url).done(function(data) {
            translate();
            ready.resolve(language);
        });

        initEvents();
        return ready.promise(language);
    }

    exports.init = init;

    function initEvents() {

        $('#language').on('click', 'li a', function() {
            LOG.info('language clicked');
            var language = $(this).attr('lang');
            switchLanguage(language);
        }), 'switch language clicked';
    }

    function switchLanguage(language) {

        if (GUISTATE_C.getLanguage == language) {
            return;
        }

        var url = 'blockly/msg/js/' + language.toLowerCase() + '.js';
        getCachedScript(url).done(function(data) {
            translate();
            GUISTATE_C.setLanguage(language);
            PROGRAM_C.reloadView();
            CONFIGURATION_C.reloadView();
            USER_C.initValidationMessages();
            NOTIFICATION_C.reloadNotifications();
            var value = Blockly.Msg.MENU_START_BRICK;
            if (value.indexOf("$") >= 0) {
                value = value.replace("$", GUISTATE_C.getRobotRealName());
            }
            $('#menuRunProg').text(value);
            if (GUISTATE_C.getBlocklyWorkspace()) {
                GUISTATE_C.getBlocklyWorkspace().robControls.refreshTooltips(GUISTATE_C.getRobotRealName());
            }
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

        $domElement.find("[lkey]").each(function(index) {
            var lkey = $(this).attr('lkey');
            var key = lkey.replace("Blockly.Msg.", "");
            var value = Blockly.Msg[key];
            if (value == undefined) {
                console.log('UNDEFINED    key : value = ' + key + ' : ' + value);
            }
            if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_PLACEHOLDER') {
                $("#sourceCodeEditorTextArea").attr("placeholder", value);
            } else if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_UPLOAD_TOOLTIP') {
                $('#uploadSourceCodeEditor').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_IMPORT_TOOLTIP') {
                $('#importSourceCodeEditor').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_BUILD_TOOLTIP') {
                $('#buildSourceCodeEditor').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.SOURCE_CODE_EDITOR_RUN_TOOLTIP') {
                $('#runSourceCodeEditor').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_EDIT_TOOLTIP') {
                $('#head-navi-tooltip-program').attr('data-original-title', value);
                $('#head-navi-tooltip-configuration').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_RIGHT_CODE_TOOLTIP') {
                $('#codeButton').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_RIGHT_SIM_TOOLTIP') {
                $('#simButton').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_RIGHT_INFO_TOOLTIP') {
                $('#infoButton').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_RIGHT_HELP_TOOLTIP') {
                $('#helpButton').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_RIGHT_LEGAL_TOOLTIP') {
                $('#legalButton').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_ROBOT_TOOLTIP') {
                $('#head-navi-tooltip-robot').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_HELP_TOOLTIP') {
                $('#head-navi-tooltip-help').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_USER_TOOLTIP') {
                $('#head-navi-tooltip-user').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_GALLERY_TOOLTIP') {
                $('#head-navi-tooltip-gallery').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_LANGUAGE_TOOLTIP') {
                $('#head-navi-tooltip-language').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_USER_STATE_TOOLTIP') {
                $('#iconDisplayLogin').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_ROBOT_STATE_TOOLTIP') {
                $('#iconDisplayRobotState').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.MENU_SIM_START_TOOLTIP") {
                $('#simControl').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.MENU_SIM_SCENE_TOOLTIP") {
                $('#simScene').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.MENU_SIM_ROBOT_TOOLTIP") {
                $('#simRobot').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_SIM_VALUES_TOOLTIP') {
                $('#simValues').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_SIM_IMPORT_TOOLTIP') {
                $('#simImport').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_SIM_POSE_TOOLTIP') {
                $('#simResetPose').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_DEBUG_START_TOOLTIP') {
                $('#debugMode').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_DEBUG_STEP_BREAKPOINT_TOOLTIP') {
                $('#simControlBreakPoint').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_DEBUG_STEP_INTO_TOOLTIP') {
                $('#simControlStepInto').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_DEBUG_STEP_OVER_TOOLTIP') {
                $('#simControlStepOver').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_SIM_VARIABLES_TOOLTIP') {
                $('#simVariables').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_CODE_DOWNLOAD_TOOLTIP') {
                $('#codeDownload').attr('data-original-title', value);
                $('#downloadSourceCodeEditor').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_CODE_REFRESH_TOOLTIP') {
                $('#codeRefresh').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_TUTORIAL_TOOLTIP') {
                $('#head-navi-tooltip-tutorials').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_RIGHT_TUTORIAL_TOOLTIP') {
                $('#tutorialButton').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.BUTTON_EMPTY_LIST") {
                $('#logList>.bootstrap-table').find('button[name="refresh"]').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.LIST_BACK_TOOLTIP") {
                $('.bootstrap-table').find('.backList').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.PROGLIST_DELETE_ALL_TOOLTIP') {
                $('#deleteSomeProg').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.PROGLIST_DELETE_TOOLTIP') {
                $('#programNameTable').find('.delete').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.PROGLIST_SHARE_TOOLTIP') {
                $('#programNameTable').find('.share').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.PROGLIST_SHARE_WITH_GALLERY_TOOLTIP') {
                $('#programNameTable').find('.gallery').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.PROGLIST_LOAD_TOOLTIP') {
                $('#programNameTable').find('.load').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.CONFLIST_DELETE_ALL_TOOLTIP') {
                $('#deleteSomeConf').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.CONFLIST_DELETE_TOOLTIP') {
                $('#confNameTable').find('.delete').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.CONFLIST_LOAD_TOOLTIP') {
                $('#confNameTable').find('.load').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.OLDER_THEN_14' || lkey == 'Blockly.Msg.YOUNGER_THEN_14') {
                $(this).html(value);
            } else if ($(this).data('translationTargets')) {
                var attributeTargets = $(this).data('translationTargets').split(' ');
                for (var key in attributeTargets) {
                    if (attributeTargets[key] === 'text' || attributeTargets[key] === 'html') {
                        $(this)[attributeTargets[key]](value);
                    } else {
                        $(this).attr(attributeTargets[key], value);
                    }
                };
            } else {
                $(this).html(value);
                $(this).attr('value', value);
            }
        });
    }
    exports.translate = translate;

    /**
     * $.getScript() will append a timestamped query parameter to the url to
     * prevent caching. The cache control should be handled using http-headers.
     * see https://api.jquery.com/jquery.getscript/#caching-requests
     */
    function getCachedScript(url, options) {
        // Allow user to set any option except for dataType, cache, and url
        options = $.extend(options || {}, {
            dataType: "script",
            cache: true,
            url: url
        });

        // Use $.ajax() since it is more flexible than $.getScript
        // Return the jqXHR object so we can chain callbacks
        return jQuery.ajax(options);
    }
});
