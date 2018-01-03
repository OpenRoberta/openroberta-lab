define([ 'exports', 'message', 'log', 'util', 'guiState.controller', 'blocks', 'jquery', 'jquery-validate', 'jquery-hotkeys', 'bootstrap-tagsinput',
        'bootstrap.wysiwyg', 'blocks-msg' ], function(exports, MSG, LOG, UTIL, GUISTATE_C, Blockly, $) {

    var blocklyWorkspace;
    /**
     * 
     */
    function init() {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        initView();
        initEvents();
        LOG.info('init info view');
    }
    exports.init = init;

    function initView() {
        $('#infoContent').wysiwyg();
        $('#infoTags').tagsinput('removeAll');
        $('#infoContent').attr('data-placeholder', Blockly.Msg.INFO_DOCUMENTATION_HINT || 'Document your program here ...');
        $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
    }

    function initEvents() {
        $('#progInfo').off('click touchend');
        $('#progInfo').on('click touchend', function(event) {
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
        // prevent to copy eg ms word formatting!
        $('[contenteditable]').on('paste', function(e) {
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
        });
    }

    function toggleInfo() {
        Blockly.hideChaff();
        if ($('#infoDiv').hasClass('rightActive')) {
            $('#infoContent, #infoTags').off('change');
            $('.blocklyToolboxDiv').css('display', 'inherit');
            Blockly.svgResize(blocklyWorkspace);
            $('#progInfo').animate({
                right : '0px',
            }, {
                duration : 750
            });
            $('#blocklyDiv').animate({
                width : '100%'
            }, {
                duration : 750,
                step : function() {
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                },
                done : function() {
                    $('#blocklyDiv').removeClass('rightActive');
                    $('#infoDiv').removeClass('rightActive');
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                    $('#infoContent').css({
                        "width" : '100 %',
                        "height" : '100 %',
                    });
                    $('#infoContent').off('change');
                    $('#sliderDiv').hide();
                    $('#progInfo').removeClass('shifted');
                }
            });
        } else {
            $('#infoContent').html(blocklyWorkspace.description);
            $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
            $('#infoTags').tagsinput('add', blocklyWorkspace.tags);
            $('#blocklyDiv').addClass('rightActive');
            $('#infoDiv').addClass('rightActive');
            var width;
            var smallScreen = $('#device-size').find('div:visible').first().attr('id') == 'xs';
            if (smallScreen) {
                width = 52;
            } else {
                width = $('#blocklyDiv').width() * 0.7;
            }
            $('#progInfo').animate({
                right : $('#blocklyDiv').width() - width + 4,
            }, {
                duration : 750
            });
            $('#blocklyDiv').animate({
                width : width
            }, {
                duration : 750,
                step : function() {
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                },
                done : function() {
                    if (smallScreen) {
                        $('.blocklyToolboxDiv').css('display', 'none');
                    }
                    $('#sliderDiv').css({
                        'left' : width - 10
                    });
                    $('#sliderDiv').show();
                    $('#progInfo').addClass('shifted');
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                    $('#infoContent, #infoTags').on('change', function() {
                        blocklyWorkspace.description = $('#infoContent').html();
                        blocklyWorkspace.tags = $('#infoTags').val();
                        if (GUISTATE_C.isProgramSaved()) {
                            GUISTATE_C.setProgramSaved(false);
                        }
                    });
                }
            });
        }
    }
});
