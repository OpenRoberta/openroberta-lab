define([ 'exports', 'jquery', guiState.controller ], function(exports, $, guiStateController) {

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
        } else {
            language = 'en';
        }
        if (language === 'de') {
            $('.EN').css('display', 'none');
            $('.DE').css('display', 'inline');
        } else {
            $('.DE').css('display', 'none');
            $('.EN').css('display', 'inline');
        }
        $('#language li a[lang=' + language + ']').parent().addClass('disabled');
        userState.language = language;
        var url = 'blockly/msg/js/' + language + '.js';
        getCachedScript(url).done(function(data) {
            translate();
            ready.resolve();
        });

        initEvents();
        return ready.promise();
    }

    exports.init = init;

    function initEvents() {

        $('#language').on('click', 'li a', function() {
            var language = $(this).attr('lang');
            switchLanguage(language);
        });
    }

    /**
     * Switch to another language
     * 
     * @param {langCode}
     *            Code of language to switch to
     * @param {forceSwitch}
     *            force the language setting
     */
    function switchLanguage(language) {

        if (guiStateController.getLanguage == language) {
            return;
        }
        guiStateController.setLanguage(language);

        var url = 'blockly/msg/js/' + language.toLowerCase() + '.js';
        var future = $.getScript(url);
        future.then(function(newLanguageScript) {
            switchLanguageInBlockly();
//            BRICKLY.switchLanguageInBrickly();
//            ROBERTA_USER.initValidationMessages();
        });

    }
    exports.switchLanguage = switchLanguage;

    /**
     * Switch blockly to another language
     */
    function switchLanguageInBlockly() {
        workspace = ROBERTA_PROGRAM.getBlocklyWorkspace();
        translate();
        var programBlocks = null;
        if (workspace !== null) {
            var xmlProgram = Blockly.Xml.workspaceToDom(workspace);
            programBlocks = Blockly.Xml.domToText(xmlProgram);
        }
        // translate programming tab
//        ROBERTA_TOOLBOX.loadToolbox(userState.toolbox);
//        ROBERTA_PROGRAM.updateRobControls();
//        ROBERTA_PROGRAM.initProgramEnvironment(programBlocks);
    }

    /**
     * Translate the web page
     */
    function translate() {
        $("[lkey]").each(function(index) {
            var lkey = $(this).attr('lkey');
            var key = lkey.replace("Blockly.Msg.", "");
            var value = Blockly.Msg[key];
            if (value == undefined) {
                console.log('UNDEFINED    key : value = ' + key + ' : ' + value);
            }
            if (lkey === 'Blockly.Msg.MENU_EDIT_TOOLTIP') {
                $('#head-navi-tooltip-program').attr('data-original-title', value);
                $('#head-navi-tooltip-configuration').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_ROBOT_TOOLTIP') {
                $('#head-navi-tooltip-robot').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_HELP_TOOLTIP') {
                $('#head-navi-tooltip-help').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_USER_TOOLTIP') {
                $('#head-navi-tooltip-user').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_LANGUAGE_TOOLTIP') {
                $('#head-navi-tooltip-language').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_USER_STATE_TOOLTIP') {
                $('#iconDisplayLogin').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_ROBOT_STATE_TOOLTIP') {
                $('#iconDisplayRobotState').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.MENU_SIM_BACK_TOOLTIP") {
                $('#simBack').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.MENU_SIM_SCENE_TOOLTIP") {
                $('#simScene').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.MENU_SIM_ROBOT_TOOLTIP") {
                $('#simRobot').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.BUTTON_EMPTY_LIST") {
                $('#logList>.bootstrap-table').find('button[name="refresh"]').attr('data-original-title', value);
            } else {
                $(this).html(value);
                $(this).attr('value', value);
            }
        });
    }

    /**
     * $.getScript() will append a timestamped query parameter to the url to
     * prevent caching. The cache control should be handled using http-headers.
     * see https://api.jquery.com/jquery.getscript/#caching-requests
     */
    function getCachedScript(url, options) {
        // Allow user to set any option except for dataType, cache, and url
        options = $.extend(options || {}, {
            dataType : "script",
            cache : true,
            url : url
        });

        // Use $.ajax() since it is more flexible than $.getScript
        // Return the jqXHR object so we can chain callbacks
        return jQuery.ajax(options);
    }
});
