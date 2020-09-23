define([ 'exports', 'log', 'util', 'comm', 'message', 'guiState.controller',  'neuralnetwork-lib','d3','neuralnetwork.playground', 'jquery', 'jquery-validate' ], function(exports,
        LOG, UTIL, COMM, MSG, GUISTATE_C, LIB, d3, PG, $) {

    var $formSingleModal;

    function init() {
        initView();
        initEvents();
        initNNForms();
        initNNEnvironment();            
    }

    exports.init = init;

    function initView() {
        PG.runPlayground();
    }

    function initEvents() {
        $('#tabNN').on('show.bs.tab', function(e) {
            GUISTATE_C.setView('tabNN');
        });

        $('#tabNN').onWrap('shown.bs.tab', function(e) {
            $(window).resize();
        }, 'tabNN clicked');

        $('#tabNN').on('hide.bs.tab', function(e) {
        });

        $('#tabNN').on('hide.bs.tab', function(e) {
        });

        $('#tabNN').on('hidden.bs.tab', function(e) {

        });

    }

    function initNNForms() {
        $formSingleModal = $('#single-modal-form');
    }
    exports.initNNForms = initNNForms;

    /**
     * Save nn to server
     */
    function saveToServer() {
        $('.modal').modal('hide'); // close all opened popups
        if (GUISTATE_C.isNNStandard() || GUISTATE_C.isNNAnonymous()) {
            LOG.error('saveToServer may only be called with an explicit nnig name');
            return;
        }
        // TODO get the NN from the dom
        CONFIGURATION.saveNNToServer(GUISTATE_C.getNNName(), xmlText, function(result) {
            if (result.rc === 'ok') {
                GUISTATE_C.setNNSaved(true);
                LOG.info('save brick nn ' + GUISTATE_C.getNNName());
            }
            MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION", result.message, GUISTATE_C.getNNName());
        });
    }
    exports.saveToServer = saveToServer;

    /**
     * Save nn with new name to server
     */
    function saveAsToServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            $('.modal').modal('hide'); // close all opened popups
            var nnName = $('#singleModalInput').val().trim();
            if (GUISTATE_C.getNNStandardName() === nnName) {
                LOG.error('saveAsToServer may NOT use the nnig standard name');
                return;
            }
            var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xmlText = Blockly.Xml.domToText(dom);
            CONFIGURATION.saveAsNNToServer(nnName, xmlText, function(result) {
                if (result.rc === 'ok') {
                    result.name = nnName;
                    GUISTATE_C.setNN(result);
                    GUISTATE_C.setProgramSaved(false);
                    LOG.info('save brick nn ' + GUISTATE_C.getNNName());
                }
                MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION_AS", result.message, GUISTATE_C.getNNName());
            });
        }
    }
    exports.saveAsToServer = saveAsToServer;

    /**
     * Load the nn that was selected in nns list
     */
     // TODO check if we want /need a listing
    function loadFromListing(nn) {
        LOG.info('loadFromList ' + nn[0]);
        CONFIGURATION.loadNNFromListing(nn[0], nn[1], function(result) {
            if (result.rc === 'ok') {
                result.name = nn[0];
                $('#tabNN').one('shown.bs.tab', function() {
                    showNN(result);
                });
                $('#tabNN').trigger('click');
            }
            MSG.displayInformation(result, "", result.message);
        });
    }
    exports.loadFromListing = loadFromListing;

    function initNNEnvironment() {

    }
    exports.initNNEnvironment = initNNEnvironment;

    function showSaveAsModal() {
        var regexString = new RegExp('^(?!\\b' + GUISTATE_C.getNNStandardName() + '\\b)([a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]*)$');
        $.validator.addMethod("regex", function(value, element, regexp) {
            value = value.trim();
            return value.match(regexp);
        }, "No special Characters allowed here. Use only upper and lowercase letters (A through Z; a through z) and numbers.");

        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg["MENU_SAVE_AS"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_NAME"]);
        }, saveAsToServer, function() {

        }, {
            rules : {
                singleModalInput : {
                    required : true,
                    regex : regexString
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                singleModalInput : {
                    required : jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"]),
                    regex : jQuery.validator.format(Blockly.Msg["MESSAGE_INVALID_CONF_NAME"])
                }
            }
        });
    }
    exports.showSaveAsModal = showSaveAsModal;

    /**
     * New nn
     */
    function newNN(opt_further) {
        var further = opt_further || false;
        if (further || GUISTATE_C.isNNSaved()) {
            var result = {};
            result.name = GUISTATE_C.getRobotGroup().toUpperCase() + "basis";
            result.lastChanged = '';
            GUISTATE_C.setNN(result);
            initNNEnvironment();
        } else {
            $('#show-message-nnirm').one('shown.bs.modal', function(e) {
                $('#nnirm').off();
                $('#nnirm').on('click', function(e) {
                    e.preventDefault();
                    newNN(true);
                });
                $('#nnirmCancel').off();
                $('#nnirmCancel').on('click', function(e) {
                    e.preventDefault();
                    $('.modal').modal('hide');
                });
            });
            if (GUISTATE_C.isUserLoggedIn()) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
            }
        }
    }
    exports.newNN = newNN;

    /**
     * Show nn
     * 
     * @param {load}
     *            load nn
     * @param {data}
     *            data of server call
     */
    function showNN(result) {
        if (result.rc == 'ok') {
            GUISTATE_C.setNN(result);
            LOG.info('show nn ' + GUISTATE_C.getNNName());
        }
    }
    exports.showNN = showNN;

    function getBricklyWorkspace() {
        return bricklyWorkspace;
    }
    exports.getBricklyWorkspace = getBricklyWorkspace;

    function reloadNN(opt_result) {
        var nn;
        if (opt_result) {
            nn = opt_result.nnXML;
        } else {
            nn = GUISTATE_C.getNNXML();
        }
        if (!seen) {
            nnToBricklyWorkspace(nn);
            var x, y;
            if ($(window).width() < 768) {
                x = $(window).width() / 50;
                y = 25;
            } else {
                x = $(window).width() / 5;
                y = 50;
            }
            var blocks = bricklyWorkspace.getTopBlocks(true);
            for (var i = 0; i < blocks.length; i++) {
                var coord = Blockly.getSvgXY_(blocks[i].svgGroup_, bricklyWorkspace);
                var coordBlock = blocks[i].getRelativeToSurfaceXY();
                blocks[i].moveBy(coordBlock.x - coord.x + x, coordBlock.y - coord.y + y);
            }
        } else {
            nnToBricklyWorkspace(nn);
        }
    }
    exports.reloadNN = reloadNN;

    function reloadView() {
        if (isVisible()) {
            var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            nnToBricklyWorkspace(xml);
        } else {
            seen = false;
        }
        var toolbox = GUISTATE_C.getNNToolbox();
        bricklyWorkspace.updateToolbox(toolbox);
    }
    exports.reloadView = reloadView;

    function resetView() {
        bricklyWorkspace.setDevice({
            group : GUISTATE_C.getRobotGroup(),
            robot : GUISTATE_C.getRobot()
        });
        initNNEnvironment();
        var toolbox = GUISTATE_C.getNNToolbox();
        bricklyWorkspace.updateToolbox(toolbox);
    }
    exports.resetView = resetView;

    function isVisible() {
        return GUISTATE_C.getView() == 'tabNN';
    }

});
