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
        GUISTATE_C.setLanguage(language);
        translate();
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
            console.error('UNDEFINED    key : value = ' + key + ' : ' + value);
            return true;
        }
        if ($(this).attr('rel') === 'tooltip') {
            $(this).attr('data-bs-original-title', value);
        } else {
            $(this).html(value);
            $(this).attr('value', value);
        }
    });
    $('#start input.form-control.search-input').attr('placeholder', Blockly.Msg.START_FORMATSEARCH);
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
