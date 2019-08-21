define([ 'exports', 'message', 'log', 'util', 'guiState.controller', 'blocks', 'jquery', 'jquery-validate', 'blocks-msg' ], function(exports, MSG, LOG, UTIL,
        GUISTATE_C, Blockly, $) {

    const INITIAL_WIDTH = 0.5;
    
    /**
     * The blocklyWorkspace is used to 
     */
    var blocklyWorkspace,
        $legalButton,
        $legalDiv,
        $legalHeader,
        storages = {},
        links= {},
        
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
        LOG.info('init legal view');
    }
    exports.init = init;

    function initView() {
        $legalDiv = $('#legalDiv');
        $legalButton = $('#legalButton');
        $legalHeader = $legalDiv.children('#legalDivHeader');
        
        var imprintDocumentType = 'imprint_',
            $imprintStorage = $legalDiv.children('#legalDivImprint'),
            $imprintLink = $legalHeader.children('[href="#legalDivImprint"]'),

            privacyPolicyDocumentType = 'privacy_policy_',
            $privacyPolicyStorage = $legalDiv.children('#legalDivPrivacyPolicy'),
            $privacyPolicyLink = $legalHeader.children('[href="#legalDivPrivacyPolicy"]'),
            
            termsOfUseDocumentType = 'terms_of_use_',
            $termsOfUseStorage = $legalDiv.children('#legalDivTermsOfUse'),
            $termsOfUseLink = $legalHeader.children('[href="#legalDivTermsOfUse"]');

        storages[imprintDocumentType] = $imprintStorage;
        storages[privacyPolicyDocumentType] = $privacyPolicyStorage;
        storages[termsOfUseDocumentType] = $termsOfUseStorage;
        
        links[imprintDocumentType] = $imprintLink;
        links[privacyPolicyDocumentType] = $privacyPolicyLink;
        links[termsOfUseDocumentType] = $termsOfUseLink;
        
        loadLegalTexts();
        
        /*
        if (GUISTATE_C.getAvailableHelp().indexOf(helpFileName) > -1) {
            loadHelpFile(helpFileName);
        } else if (GUISTATE_C.getAvailableHelp().indexOf(helpFileNameDefault) > -1) {
            loadHelpFile(helpFileNameDefault);
        } else {
            $('#helpButton').hide();
        }
        */
    }
    exports.initView = initView;

    function initEvents() {
        $legalButton.off('click touchend');
        $legalButton.on('click touchend', function(event) {
            event.preventDefault();
            toggleLegal();
        });
    }
    
    function loadLegalTexts() {
        var language = GUISTATE_C.getLanguage().toLowerCase(),
            loadFile = function(documentType, language) {
                var urlPath = '../legal/',
                    url = urlPath + documentType + language + '.html',
                    $storage = storages[documentType],
                    $link = links[documentType];
                
                if ($storage) {
                    $storage.children().remove();
                    $link.show();
                    $storage.load(url, function(response, status, xhr) {
                        if (status === "error" || response.trim() === '') {
                            if (language !== 'en') {
                                var englishFallBackUrl = urlPath + documentType + 'en.html';
                                $storage.load(englishFallBackUrl, function(englishFallBackResponse, englishFallBackStatus, englishFallBackXhr) {
                                    if (englishFallBackStatus === "error" || englishFallBackResponse.trim() === '') {
                                        if (language !== 'de') {
                                            var germanFallBackUrl = urlPath + documentType + 'de.html';
                                            $storage.load(germanFallBackUrl, function(germanFallBackResponse, germanFallBackStatus, germanFallBackXhr) {
                                                if (germanFallBackStatus === "error" || germanFallBackResponse.trim() === '') {
                                                    $link.hide();
                                                }
                                            });
                                        } else {
                                            $link.hide();
                                        }
                                    }
                                });
                            } else {
                                var germanFallBackUrl = urlPath + documentType + 'de.html';
                                $storage.load(germanFallBackUrl, function(germanFallBackResponse, germanFallBackStatus, germanFallBackXhr) {
                                    if (germanFallBackStatus === "error" || germanFallBackResponse.trim() === '') {
                                        $link.hide();
                                    }
                                });
                            }
                        }
                    });
                }
            };
        
        for (documentType in storages) {
            if (storages.hasOwnProperty(documentType)) {
                loadFile(documentType, language);
            }
        }
    }
    exports.loadLegalTexts = loadLegalTexts;

    function toggleLegal() {
        var $blocklyWrapper = $('#blockly');
        
        Blockly.hideChaff();
        
        if ($blocklyWrapper.hasClass('rightActive')) {
            $blocklyWrapper.closeRightView();
        } else {
            $blocklyWrapper.openRightView('legal', INITIAL_WIDTH, function() {});
        }
    }
});
