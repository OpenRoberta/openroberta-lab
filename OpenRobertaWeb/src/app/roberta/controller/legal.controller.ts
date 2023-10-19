import * as LOG from 'log';
import * as GUISTATE_C from 'guiState.controller';
import * as $ from 'jquery';
import 'jquery-validate';

const INITIAL_WIDTH = 0.5;

/**
 * The blocklyWorkspace is used to
 */
let blocklyWorkspace,
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
export function init(): void {
    blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
    initView();
    initEvents();
}

export function initView() {
    $legalDiv = $('#legalDiv');
    $legalButton = $('#legalButton');
    $legalHeader = $legalDiv.children('#legalDivHeader');

    let imprintDocumentType: string = 'imprint_',
        $imprintStorage = $legalDiv.children('#legalDivImprint'),
        $imprintLink = $legalHeader.children('[data-href="#legalDivImprint"]'),
        privacyPolicyDocumentType: string = 'privacy_policy_',
        $privacyPolicyStorage = $legalDiv.children('#legalDivPrivacyPolicy'),
        $privacyPolicyLink = $legalHeader.children('[data-href="#legalDivPrivacyPolicy"]'),
        termsOfUseDocumentType: string = 'terms_of_use_',
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

function initEvents(): void {
    let setScrollEventForDocumentType: Function = function (documentType): void {
        let $link = links[documentType],
            $storage = storages[documentType];

        if ($storage && $link) {
            $link.onWrap('click touchend', function (evt): void {
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
    $legalButton.onWrap('click touchend', function (event): void {
        event.preventDefault();
        toggleLegal($(this));
    });

    for (let documentType in links) {
        if (links.hasOwnProperty(documentType)) {
            setScrollEventForDocumentType(documentType);
        }
    }
}

export function loadLegalTexts(): void {
    let language: string = GUISTATE_C.getLanguage().toLowerCase(),
        legalTextsMap = GUISTATE_C.getLegalTextsMap(),
        loadFile = function (documentType, language): void {
            let $storage = storages[documentType],
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

    for (let documentType in storages) {
        if (storages.hasOwnProperty(documentType)) {
            loadFile(documentType, language);
        }
    }

    if (
        $legalHeader.children().filter(function (): boolean {
            return $(this).css('display') !== 'none';
        }).length === 0
    ) {
        $legalButton.hide();
    } else {
        $legalButton.show();
    }
}

function toggleLegal($button: any): void {
    if ($('#legalButton').hasClass('rightActive')) {
        $('#blocklyDiv').closeRightView();
    } else {
        LOG.info('legal view opened');
        $legalDiv.animate(
            {
                scrollTop: 0,
            },
            'fast'
        );
        $button.openRightView($('#legalDiv'), INITIAL_WIDTH);
    }
}
