import * as MSG from 'message';
import * as LOG from 'log';
import * as UTIL from 'util';
import * as GUISTATE_C from 'guiState.controller';
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'jquery-validate';

const INITIAL_WIDTH = 0.5;

/**
 * The blocklyWorkspace is used to
 */
var blocklyWorkspace,
    $legalButton,
    $legalDiv,
    $legalHeader,
    storages = {},
    links = {},
    /**
     * The fileStorage is used to store the loaded legal texts, so that even if the internet is lost and the legal texts
     * have to be loaded, the texts are available.
     *
     * fileStorage: {
     *     documentType: {
     *         languageKey: String fileContent
     *     }
     * }
     */
    fileStorage = {};

/**
 *
 */
function init() {
    blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
    initView();
    initEvents();
}

function initView() {
    $legalDiv = $('#legalDiv');
    $legalButton = $('#legalButton');
    $legalHeader = $legalDiv.children('#legalDivHeader');

    var imprintDocumentType = 'imprint_',
        $imprintStorage = $legalDiv.children('#legalDivImprint'),
        $imprintLink = $legalHeader.children('[data-href="#legalDivImprint"]'),
        privacyPolicyDocumentType = 'privacy_policy_',
        $privacyPolicyStorage = $legalDiv.children('#legalDivPrivacyPolicy'),
        $privacyPolicyLink = $legalHeader.children('[data-href="#legalDivPrivacyPolicy"]'),
        termsOfUseDocumentType = 'terms_of_use_',
        $termsOfUseStorage = $legalDiv.children('#legalDivTermsOfUse'),
        $termsOfUseLink = $legalHeader.children('[data-href="#legalDivTermsOfUse"]');

    storages[imprintDocumentType] = $imprintStorage;
    storages[privacyPolicyDocumentType] = $privacyPolicyStorage;
    storages[termsOfUseDocumentType] = $termsOfUseStorage;

    links[imprintDocumentType] = $imprintLink;
    links[privacyPolicyDocumentType] = $privacyPolicyLink;
    links[termsOfUseDocumentType] = $termsOfUseLink;

    loadLegalTexts();
}

function initEvents() {
    var setScrollEventForDocumentType = function (documentType) {
        var $link = links[documentType],
            $storage = storages[documentType];

        if ($storage && $link) {
            $link.onWrap('click touchend', function (evt) {
                evt.preventDefault();
                $legalDiv.animate(
                    {
                        scrollTop: $storage.offset().top - 92,
                    },
                    'slow'
                );
            });
        }
    };
    $legalButton.off('click touchend');
    $legalButton.onWrap('click touchend', function (event) {
        event.preventDefault();
        toggleLegal();
    });

    for (documentType in links) {
        if (links.hasOwnProperty(documentType)) {
            setScrollEventForDocumentType(documentType);
        }
    }
}

function loadLegalTexts() {
    var language = GUISTATE_C.getLanguage().toLowerCase(),
        legalTextsMap = GUISTATE_C.getLegalTextsMap(),
        loadFile = function (documentType, language) {
            var $storage = storages[documentType],
                $link = links[documentType],
                content =
                    legalTextsMap[documentType + language + '.html'] || legalTextsMap[documentType + 'en.html'] || legalTextsMap[documentType + 'de.html'];

            if ($storage) {
                $storage.children().remove();
                if (content) {
                    $storage.append($(content));
                    $link.show();
                } else {
                    $link.hide();
                }
            }
        };

    for (documentType in storages) {
        if (storages.hasOwnProperty(documentType)) {
            loadFile(documentType, language);
        }
    }

    if (
        $legalHeader.children().filter(function () {
            return $(this).css('display') !== 'none';
        }).length === 0
    ) {
        $legalButton.hide();
    } else {
        $legalButton.show();
    }
}
export { init, initView, loadLegalTexts };

function toggleLegal() {
    Blockly.hideChaff();
    if ($('#legalButton').hasClass('rightActive')) {
        $('#blockly').closeRightView();
    } else {
        LOG.info('legal view opened');
        $legalDiv.animate(
            {
                scrollTop: 0,
            },
            'fast'
        );
        $('#blockly').openRightView('legal', INITIAL_WIDTH);
    }
}
