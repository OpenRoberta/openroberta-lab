define(['exports', 'jquery', 'guiState.controller'], function(exports, $, GUISTATE_C) {
    var FADE_DURATION = 400;
    var $releaseInfo = $('#releaseInfo');
    var $title = $releaseInfo.children('#releaseInfoTitle');
    var $content = $releaseInfo.children('#releaseInfoContent');

    var TITLES = new Map([['en', 'Release info'], ['de', 'Veröffentlichungsinfo']]);
    var messages = new Map();
    var robotWasShown = new Set();

    function init() {
        $releaseInfo.children('button').click(hide);
        initMessages();
    }
    exports.init = init;

    function initMessages() {
        var enCalliopeMessage = 'Hey, we have made some changes to Calliope! Check out how the new configuration works <a href="https://jira.iais.fraunhofer.de/wiki/display/ORInfo/Configuration+Calliope+mini">here</a>!';
        var enMicrobitMessage = 'Hey, we have made some changes to micro:bit! Check out how the new configuration works <a href="https://jira.iais.fraunhofer.de/wiki/display/ORInfo/Configuration">here</a>!';
        var deCalliopeMessage = 'Hey, wir haben einige Änderungen bei Calliope durchgeführt! Schau dir <a href="https://jira.iais.fraunhofer.de/wiki/display/ORInfo/Konfiguration+Calliope+Mini">hier</a> an wie die neue Konfiguration funktioniert!';
        var deMicrobitMessage = 'Hey, wir haben einige Änderungen bei micro:bit durchgeführt! Schau dir <a href="https://jira.iais.fraunhofer.de/wiki/display/ORInfo/Konfiguration">hier</a> an wie die neue Konfiguration funktioniert!';

        initMessagesForLang('en', new Map([
            ['calliope', enCalliopeMessage],
            ['microbit', enMicrobitMessage],
        ]));
        initMessagesForLang('de', new Map([
            ['calliope', deCalliopeMessage],
            ['microbit', deMicrobitMessage],
        ]));
    }

    function initMessagesForLang(lang, robotMessages) {
        messages.set(lang, robotMessages);
    }

    function showForRobot(robot) {
        var lang = GUISTATE_C.getLanguage();
        var group = GUISTATE_C.findGroup(robot);
        var robotForMessage = robot;

        if (!languageExists(lang)) {
            lang = 'en';
        }
        if (!messageExists(lang, robot)) {
            robotForMessage = group;
        }
        if (!robotWasShown.has(robotForMessage) && messageExists(lang, robotForMessage)) {
            robotWasShown.add(robotForMessage);
            setContent(lang, robotForMessage);
            show();
        } else {
            hide();
        }
    }
    exports.showForRobot = showForRobot;

    function languageExists(lang) {
        return messages.has(lang);
    }

    function messageExists(lang, robot) {
        return messages.get(lang).has(robot)
    }

    function show() {
        $releaseInfo.fadeIn(FADE_DURATION);
    }

    function hide() {
        $releaseInfo.fadeOut(FADE_DURATION);
    }

    function setContent(lang, robot) {
        $title.html(TITLES.get(lang));
        $content.html(messages.get(lang).get(robot));
    }
});
