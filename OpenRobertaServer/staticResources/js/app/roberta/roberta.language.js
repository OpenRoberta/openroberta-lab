define([ 'exports', 'jquery', 'roberta.toolbox', 'roberta.user-state', 'roberta.program', 'roberta.user', 'roberta.brickly' ], function(exports, $,
        ROBERTA_TOOLBOX, userState, ROBERTA_PROGRAM, ROBERTA_USER, BRICKLY) {

    /**
     * Initialize language switching
     */
    function initializeLanguages() {
        var language;
        if (navigator.language.indexOf("de") > -1) {
            language = 'DE';
            $('#chosenLanguage').text('DE');
        } else if (navigator.language.indexOf("fi") > -1) {
            language = 'FI';
            $('#chosenLanguage').text('FI');
        } else if (navigator.language.indexOf("da") > -1) {
            language = 'DA';
            $('#chosenLanguage').text('DA');
        } else if (navigator.language.indexOf("es") > -1) {
            language = 'ES';
            $('#chosenLanguage').text('ES');
        } else {
            language = 'EN';
            $('#chosenLanguage').text('EN');
        }

        $('#language').on('click', '.dropdown-menu li a', function() {
            var chosenLanguage = $(this).text();
            $('#chosenLanguage').text(chosenLanguage);
            switchLanguage(chosenLanguage, false);
        });
        return language;
    }

    exports.initializeLanguages = initializeLanguages;

    /**
     * Switch to another language
     * 
     * @param {langCode}
     *            Code of language to switch to
     * @param {forceSwitch}
     *            force the language setting
     */
    function switchLanguage(langCode, forceSwitch) {
        if (forceSwitch || userState.language != langCode) {
            var langs = [ 'DE', 'EN', 'FI', 'DA', 'ES' ];
            if (langs.indexOf(langCode) < 0) {
                langCode = "EN";
            }

            for (i = 0; i < langs.length; i++) {
                $('.' + langs[i] + '').css('display', 'none');
            }
            $('.' + langCode + '').css('display', 'inline');

            userState.language = langCode;
            var url = 'blockly/msg/js/' + langCode.toLowerCase() + '.js';
            var future = $.getScript(url);
            future.then(function(newLanguageScript) {
                switchLanguageInBlockly();
                BRICKLY.switchLanguageInBrickly();
                ROBERTA_USER.initValidationMessages();
            });
        }

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
        ROBERTA_TOOLBOX.loadToolbox(userState.toolbox);
        ROBERTA_PROGRAM.initProgramEnvironment(programBlocks);
    }

    /**
     * Translate the web page
     */
    function translate(jsdata) {
        $("[lkey]").each(function(index) {
            var lkey = $(this).attr('lkey');
            var key = lkey.replace("Blockly.Msg.", "");
            var value = Blockly.Msg[key];
            if (value == undefined) {
                console.log('UNDEFINED    key : value = ' + key + ' : ' + value);
            }
//           if (lkey === 'Blockly.Msg.MENU_LOG_IN') {
//                $('#loginLabel').text(value);
//                $(this).html(value);
//            } else if (lkey === 'Blockly.Msg.MENU_SAVE_AS') {
//                $(this).html(value);
//            } else if (lkey === 'Blockly.Msg.POPUP_HIDE_STARTUP_MESSAGE') {
//                $('#hideStartupMessage').text(value);
//            } else if (lkey === 'Blockly.Msg.POPUP_TEXT_STARTUP_MESSAGE') {
//                $('#popupTextStartupMessage').html(value);
//            } else if (lkey === 'Blockly.Msg.POPUP_ATTENTION') {
//                $('#show-message h3').text(value);
//                $('#show-startup-message h3').text(value);
//            } else if (lkey === 'Blockly.Msg.POPUP_CANCEL') {
//                $('.cancelPopup').attr('value', value);
//                $('.backButton').attr('value', value);
//            } else if (lkey === 'Blockly.Msg.POPUP_CHANGE_PASSWORD') {
//                $('#showChangeUserPassword').attr('value', value);
//            } else if (lkey === 'Blockly.Msg.POPUP_ABOUT_JOIN') {
//                $('#about-join').html(value);
//            } else if (lkey === 'Blockly.Msg.BUTTON_LOAD') {
//                $('.buttonLoad').attr('value', value);
//            } else if (lkey === 'Blockly.Msg.BUTTON_DO_DELETE') {
//                $('.buttonDelete').attr('value', value);
//            } else if (lkey === 'Blockly.Msg.BUTTON_DO_SHARE') {
//                $('.buttonShare').attr('value', value);
//                $('#show-relations h2').text(value);
//            } else if (lkey === 'Blockly.Msg.BUTTON_REFRESH') {
//                $('.buttonRefresh').attr('value', value);
//            } else if (lkey === 'Blockly.Msg.BUTTON_EMPTY_LIST') {
//                $('#clearLog').attr('value', value);
//            } else if (lkey === 'Blockly.Msg.MENU_ROBOT_STATE_INFO') {
//                $('#show-robot-info h3').text(value);
//                $(this).html(value);
//            } else if (lkey === 'Blockly.Msg.MENU_STATE_INFO') {
//                $('#show-state-info h3').text(value);
//                $(this).html(value);
//            } else if (lkey === 'Blockly.Msg.MENU_ABOUT') {
//                $('#show-about h3').text(value);
//                $(this).html(value);
//            } else 
            if (lkey === 'Blockly.Msg.MENU_EDIT_TOOLTIP') {
                $('#head-navi-tooltip-program').attr('data-original-title', value).tooltip('fixTitle');
                $('#head-navi-tooltip-configuration').attr('data-original-title', value).tooltip('fixTitle');
            } else if (lkey === 'Blockly.Msg.MENU_ROBOT_TOOLTIP') {
                $('#head-navi-tooltip-robot').attr('data-original-title', value).tooltip('fixTitle');
            } else if (lkey === 'Blockly.Msg.MENU_HELP_TOOLTIP') {
                $('#head-navi-tooltip-help').attr('data-original-title', value).tooltip('fixTitle');
            } else if (lkey === 'Blockly.Msg.MENU_USER_TOOLTIP') {
                $('#head-navi-tooltip-user').attr('data-original-title', value).tooltip('fixTitle');
            } else if (lkey === 'Blockly.Msg.MENU_USER_STATE_TOOLTIP') {
                $('#iconDisplayLogin').attr('data-original-title', value).tooltip('fixTitle');
            } else if (lkey === 'Blockly.Msg.MENU_ROBOT_STATE_TOOLTIP') {
                $('#iconDisplayRobotState').attr('data-original-title', value).tooltip('fixTitle');
            } else {
                $(this).html(value);
                $(this).attr('value', value);
            }
        });
    }
});
