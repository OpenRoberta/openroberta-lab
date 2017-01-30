define([ 'exports', 'comm', 'message', 'log', 'util', 'guiState.controller', 'program.model', 'blocks', 'jquery', 'jquery-validate', 'jquery-hotkeys', 'bootstrap.wysiwyg', 'blocks-msg' ], function(exports, COMM, MSG, LOG, UTIL, GUISTATE_C, PROGRAM, Blockly, $) {

    var blocklyWorkspace;
    /**
     * 
     */
    function init(workspace) {
        blocklyWorkspace = workspace;
        initEvents();
    }
    exports.init = init;

    function initEvents() {
        $('#progCode').off('click touchend');
        $('#progCode').on('click touchend', function(event) {
            toggleCode();
            return false;
        });
        $('#codeDownload').onWrap('click', function(event) {
            var filename = GUISTATE_C.getProgramName() + '.' + GUISTATE_C.getProgramFileExtension();
            UTIL.download(filename, GUISTATE_C.getProgramSource());
            MSG.displayMessage("MENU_MESSAGE_DOWNLOAD", "TOAST", filename);
        }, 'codeDownload clicked');
        $('#codeRefresh').onWrap('click', function(event) {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgram = Blockly.Xml.domToText(dom);
            var xmlConfiguration = GUISTATE_C.getConfigurationXML();

            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlProgram, xmlConfiguration, function(result) {
                GUISTATE_C.setState(result);
                $('#codeContent').html('<pre class="prettyprint linenums">' + prettyPrintOne(result.sourceCode.escapeHTML(), null, true) + '</pre>');
                // TODO change javaSource to source on server                   // TODO change javaSource to source on server
                GUISTATE_C.setProgramSource(result.sourceCode);
                GUISTATE_C.setProgramFileExtension(result.fileExtension);
            });
        }, 'code refresh clicked');
    }

    function toggleCode() {
        if ($('#codeDiv').hasClass('rightActive')) {
            $('.blocklyToolboxDiv').css('display', 'inherit');
            Blockly.svgResize(blocklyWorkspace);
            $('#progCode').animate({
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
                    $('#codeDiv').removeClass('rightActive');
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                    $('#codeContent').css({
                        "width" : 'inherit',
                        "height" : 'inherit',
                    });
                    $('#sliderDiv').hide();
                    $('#progCode').removeClass('shifted');
                }
            });
        } else {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgram = Blockly.Xml.domToText(dom);
            var xmlConfiguration = GUISTATE_C.getConfigurationXML();

            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlProgram, xmlConfiguration, function(result) {
                GUISTATE_C.setState(result);
                $('#blocklyDiv').addClass('rightActive');
                $('#codeDiv').addClass('rightActive');
                var width;
                var smallScreen = $('#device-size').find('div:visible').first().attr('id') == 'xs';
                if (smallScreen) {
                    width = 52;
                } else {
                    width = $('#blocklyDiv').width() * 0.3;
                }
                $('#progCode').animate({
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
                        $('#progCode').addClass('shifted');
                        $(window).resize();
                        Blockly.svgResize(blocklyWorkspace);
                    }
                });
                $('#blocklyDiv').addClass('rightActive');
                $('#codeDiv').addClass('rightActive');
                $('#codeContent').html('<pre class="prettyprint linenums">' + prettyPrintOne(result.sourceCode.escapeHTML(), null, true) + '</pre>');
                // TODO change javaSource to source on server                   // TODO change javaSource to source on server
                GUISTATE_C.setProgramSource(result.sourceCode);
                GUISTATE_C.setProgramFileExtension(result.fileExtension);
            });
        }
    }
});
