define([ 'exports', 'message', 'log', 'util', 'guiState.controller', 'blockly', 'jquery', 'jquery-validate', 'jquery-hotkeys', 'bootstrap-tagsinput',
        'bootstrap.wysiwyg' ], function(exports, MSG, LOG, UTIL, GUISTATE_C, Blockly, $) {

    const INITIAL_WIDTH = 0.3;
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
        $('#infoContent').wysiwyg();
        $('#infoTags').tagsinput('removeAll');
        $('#infoContent').attr('data-placeholder', Blockly.Msg.INFO_DOCUMENTATION_HINT || 'Document your program here ...');
        $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
    }

    function initEvents() {
        $('#infoButton').off('click touchend');
        $('#infoButton').on('click touchend', function(event) {
            toggleInfo();
            return false;
        });
        $(window).on('resize', function(e) {
            if ($('#infoDiv').hasClass('rightActive')) {
                $('#infoContainer').css({
                    "width" : $('#infoDiv').outerWidth(),
                    "height" : $('#infoDiv').outerHeight() - $('.btn-toolbar.editor').outerHeight() - 57,
                });
            }
        });
        $('#infoContent, #infoTags').on('change', function() {
            blocklyWorkspace.description = $('#infoContent').html();
            blocklyWorkspace.tags = $('#infoTags').val();
            if (GUISTATE_C.isProgramSaved()) {
                GUISTATE_C.setProgramSaved(false);
            }
            if (typeof $('#infoContent').html() === 'string' && $('#infoContent').html().length) {
                $('#infoButton').addClass('notEmpty');
            } else {
                $('#infoButton').removeClass('notEmpty');
            }
        });
        // prevent to copy eg ms word formatting!
        $('[contenteditable]#infoContent').on('paste', function(e) {
            e.preventDefault();
            var text = '';
            if (e.clipboardData || e.originalEvent.clipboardData) {
                text = (e.originalEvent || e).clipboardData.getData('text/plain');
            } else if (window.clipboardData) {
                text = window.clipboardData.getData('Text');
            }
            if (document.queryCommandSupported('insertText')) {
                document.execCommand('insertText', false, text);
            } else {
                document.execCommand('paste', false, text);
            }
            $('#infoContent').trigger('change');
        });
    }

    function toggleInfo() {
        Blockly.hideChaff();
        if ($('#infoButton').hasClass('rightActive')) {
            $('#blockly').closeRightView();
        } else {
            $('#infoContent').html(blocklyWorkspace.description);
            $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
            $('#infoTags').tagsinput('add', blocklyWorkspace.tags);
            $('#blockly').openRightView('info', INITIAL_WIDTH);
        }
    }
});
