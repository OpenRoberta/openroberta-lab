define([ 'exports', 'comm', 'message', 'log', 'util', 'guiState.controller', 'blocks', 'jquery', 'jquery-validate', 'jquery-hotkeys', 'bootstrap.wysiwyg', 'blocks-msg' ], function(exports, COMM, MSG, LOG, UTIL, GUISTATE_C, Blockly, $) {

    var blocklyWorkspace;
    /**
     * 
     */
    function init(workspace) {
        blocklyWorkspace = workspace;
        initView();
        initEvents();
        LOG.info('init info view');
    }
    exports.init = init;

    function initView() {
        $('#infoContent').wysiwyg();
        if (GUISTATE_C.getLanguage() == 'de') {
            $('#infoContent').attr('data-placeholder', 'Beschreibe dein Programm hier ...');
        } else {
            $('#infoContent').attr('data-placeholder', 'Document your program here ...');
        }
    }

    function initEvents() {
        $('#progInfo').off('click touchend');
        $('#progInfo').on('click touchend', function(event) {
            toggleInfo();
            return false;
        });
    }

    function toggleInfo() {
        if ($('#infoDiv').hasClass('rightActive')) {
            $('.blocklyToolboxDiv').css('display', 'inherit');
            $('.nav > li > ul > .robotType').removeClass('disabled');
            $('.' + GUISTATE_C.getRobot()).addClass('disabled');
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
                }
            });
        } else {
            $('#infoContent').html(blocklyWorkspace.description);
            $('#blocklyDiv').addClass('rightActive');
            $('#infoDiv').addClass('rightActive');
            $('.nav > li > ul > .robotType').addClass('disabled');
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
                    var ready;
                    window.onresize = function() {
                        clearTimeout(ready);
                        ready = setTimeout(resizedw, 100);
                    };
                    function resizedw() {
                        $('#infoContent').css({
                            "width" : $('#infoContent').outerWidth(),
                            "height" : $('#infoDiv').outerHeight() - $('.btn-toolbar.editor').outerHeight(),
                        });
                    }
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                    $('#infoContent').on('change', function() {
                        blocklyWorkspace.description = $('#infoContent').html();
                    });
                }
            });
        }
    }
});
