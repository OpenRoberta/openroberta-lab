define([ 'exports', 'comm', 'message', 'log', 'util', 'guiState.controller', 'blocks', 'jquery', 'jquery-validate', 'blocks-msg' ], function(exports, COMM, MSG, LOG, UTIL, GUISTATE_C, Blockly, $) {

    var blocklyWorkspace;
    /**
     * 
     */
    function init(workspace) {
        blocklyWorkspace = workspace;
        initView();
        initEvents();
        LOG.info('init help view');
    }
    exports.init = init;

    function initView() {
        $('#helpContent').remove();
        var url = '../help/progHelp_' + GUISTATE_C.getRobot() + '_' + GUISTATE_C.getLanguage().toLowerCase() + '.html';
        $('#helpDiv').load(url, function(response, status, xhr) {
            if (status == "error") {
                url = '../help/progHelp_' + GUISTATE_C.getRobot() + '_de.html';
//                    url = 'progHelp_en.html';
                $('#helpDiv').load(url, function(response, status, xhr) {
                    if (status == "error") {
                        $('#progHelp').hide();
                    } else {
                        $('#progHelp').show();
                    }
                })
            } else {
                $('#progHelp').show();
            }
        });
    }

    function initEvents() {
        $('#progHelp').off('click touchend');
        $('#progHelp').on('click touchend', function(event) {
            toggleHelp();
            return false;
        });
    }

    function toggleHelp() {
        if ($('#blocklyDiv').hasClass('rightActive')) {
            $('.nav > li > ul > .robotType').removeClass('disabled');
            $('.' + GUISTATE_C.getRobot()).addClass('disabled');
            $('.blocklyToolboxDiv').css('display', 'inherit');
            Blockly.svgResize(blocklyWorkspace);
            $('#progHelp').animate({
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
                    $('#helpDiv').removeClass('rightActive');
                    $('.nav > li > ul > .robotType').removeClass('disabled');
                    $('.' + GUISTATE_C.getRobot()).addClass('disabled');
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                    $('#sliderDiv').show();
                    $('#progHelp').removeClass('shifted');
                }
            });
        } else {
            $('#blocklyDiv').addClass('rightActive');
            $('#helpDiv').addClass('rightActive');
            $('.nav > li > ul > .robotType').addClass('disabled');
            $('#menuShowCode').parent().addClass('disabled');
            if (GUISTATE_C.getProgramToolboxLevel() === 'beginner') {
                $('.help.expert').hide();
            } else {
                $('.help.expert').show();
            }
            var width;
            var smallScreen;
            if ($(window).width() < 768) {
                smallScreen = true;
                width = 52;
            } else {
                smallScreen = false;
                width = $('#blocklyDiv').width() * 0.7;
            }
            $('#progHelp').animate({
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
                    $('#sliderDiv').css({'left': width - 10});
                    $('#sliderDiv').show();
                    $('#progHelp').addClass('shifted');
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                }
            });
        }
    }
});
