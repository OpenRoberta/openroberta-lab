define(["require", "exports", "guiState.controller", "blockly", "jquery", "jquery-validate", "jquery-hotkeys", "bootstrap-tagsinput", "bootstrap.wysiwyg"], function (require, exports, GUISTATE_C, Blockly, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.switchLanguage = exports.init = void 0;
    var INITIAL_WIDTH = 0.3;
    var blocklyWorkspace;
    /**
     *
     */
    function init() {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        initView();
        initEvents();
    }
    exports.init = init;
    function initView() {
        //@ts-ignore
        $('#infoContent').wysiwyg();
        //@ts-ignore
        $('#infoTags').tagsinput('removeAll');
        $('#infoContent').attr('data-placeholder', Blockly.Msg.INFO_DOCUMENTATION_HINT || 'Document your program here ...');
        $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
    }
    function switchLanguage() {
        $('#infoContent').attr('data-placeholder', Blockly.Msg.INFO_DOCUMENTATION_HINT || 'Document your program here ...');
        $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
    }
    exports.switchLanguage = switchLanguage;
    function initEvents() {
        $('#infoButton').off('click touchend');
        $('#infoButton').onWrap('click touchend', function (event) {
            toggleInfo($(this));
            return false;
        });
        $(window).on('resize', function (e) {
            if ($('#infoDiv').hasClass('rightActive')) {
                $('#infoContainer').css({
                    width: $('#infoDiv').outerWidth(),
                    height: $('#infoDiv').outerHeight() - $('.btn-toolbar.editor').outerHeight() - 57
                });
            }
        });
        $('#infoContent, #infoTags').on('change', function () {
            // TODO: here should be an onWrap. But this change is called during a run of another wrapped callback
            blocklyWorkspace.description = $('#infoContent').html();
            blocklyWorkspace.tags = $('#infoTags').val();
            if (GUISTATE_C.isProgramSaved()) {
                GUISTATE_C.setProgramSaved(false);
            }
            if (typeof $('#infoContent').html() === 'string' && $('#infoContent').html().length) {
                $('#infoButton').addClass('notEmpty');
            }
            else {
                $('#infoButton').removeClass('notEmpty');
            }
        });
        // prevent to copy eg ms word formatting!
        $('[contenteditable]#infoContent').onWrap('paste', function (e) {
            e.preventDefault();
            var text = '';
            if (e.clipboardData || e.originalEvent.clipboardData) {
                text = (e.originalEvent || e).clipboardData.getData('text/plain');
                //@ts-ignore
            }
            else if (window.clipboardData) {
                //@ts-ignore
                text = window.clipboardData.getData('Text');
            }
            if (document.queryCommandSupported('insertText')) {
                document.execCommand('insertText', false, text);
            }
            else {
                document.execCommand('paste', false, text);
            }
            $('#infoContent').trigger('change');
        });
    }
    function toggleInfo($button) {
        if ($('#infoButton').hasClass('rightActive')) {
            $('#blocklyDiv').closeRightView();
        }
        else {
            $('#infoContent').html(blocklyWorkspace.description);
            $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
            //@ts-ignore
            $('#infoTags').tagsinput('add', blocklyWorkspace.tags);
            $button.openRightView($('#infoDiv'), INITIAL_WIDTH);
        }
    }
});
