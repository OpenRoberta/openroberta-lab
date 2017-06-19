define([ 'exports', 'comm', 'message', 'log', 'blocks', 'jquery', 'jquery-scrollto', 'enjoyHint', 'blocks-msg' ], function(exports, COMM, MSG, LOG, Blockly, $) {

    var enjoyhint_instance;

    function start(tour) {
        var ja = true;
        enjoyhint_instance = new EnjoyHint({
            onSkip : function() {
                $("#show-startup-message").modal("show");
            },
            onEnd : function() {
                if (ja) {
                    $("#show-startup-message").modal("show");
                    ja = false;
                }
            }
        });
        var enjoyhint_script_steps = [ {} ];
        switch (tour) {
        case 'welcome':
            for (var i = 0, len = welcome.length; i < len; ++i) {
                enjoyhint_script_steps[i] = jQuery.extend(true, {}, welcome[i]);
            }
            break;
        default:
            return;
        }
        // for translation
        for (var i = 0; i < enjoyhint_script_steps.length; i++) {
            if (enjoyhint_script_steps[i].description) {
                var a = Blockly.Msg[enjoyhint_script_steps[i].description];
                enjoyhint_script_steps[i].description = a;
            }
            if (enjoyhint_script_steps[i].nextButton && enjoyhint_script_steps[i].nextButton.text) {
                var b = Blockly.Msg[enjoyhint_script_steps[i].nextButton.text];
                enjoyhint_script_steps[i].nextButton.text = b;
            }
            if (enjoyhint_script_steps[i].skipButton && enjoyhint_script_steps[i].skipButton.text) {
                var c = Blockly.Msg[enjoyhint_script_steps[i].skipButton.text];
                enjoyhint_script_steps[i].skipButton.text = c;
            }
        }
        enjoyhint_instance.set(enjoyhint_script_steps);
        enjoyhint_instance.run();
    }
    exports.start = start;

    function getInstance() {
        return enjoyhint_instance;
    }
    exports.getInstance = getInstance;

    var offsetLeft = $('#blockly').width() * -0.15;
    var offsetTop = $('#blockly').height() * -0.1;
    var welcome = [ {
        'event_type' : 'next',
        'selector' : '.logo',
        'description' : 'TOUR1_DESCRIPTION01',
        'top' : 77,
        'nextButton' : {
            text : 'TOUR1_DESCRIPTION00'
        },
        'showSkip' : false
    }, /*
         * { 'event_type' : 'next', 'selector' : '#head-navigation',
         * 'description' : 'TOUR1_DESCRIPTION02', 'nextButton' : { text :
         * 'TOUR1_DESCRIPTION00' }, 'showSkip' : false }, { 'event_type' :
         * 'next', 'selector' : '.navbar-nav', 'description' :
         * 'TOUR1_DESCRIPTION03', 'nextButton' : { text : 'TOUR1_DESCRIPTION00' },
         * 'showSkip' : false }, { 'event' : 'click', 'selector' :
         * 'a#tabConfiguration', 'description' : 'TOUR1_DESCRIPTION04',
         * 'nextButton' : { text : 'TOUR1_DESCRIPTION00' }, 'showSkip' : false }, {
         * 'event_type' : 'next', 'selector' : '#bricklyDiv
         * .blocklyBlockCanvas', 'description' : 'TOUR1_DESCRIPTION05',
         * 'nextButton' : { text : 'TOUR1_DESCRIPTION00' }, 'showSkip' : false,
         * onBeforeStart : function() { $('#tabConfiguration').trigger('click'); } }, {
         * 'event' : 'click', 'selector' : 'a#tabProgram', 'description' :
         * 'TOUR1_DESCRIPTION06', 'nextButton' : { text : 'TOUR1_DESCRIPTION00' },
         * 'showSkip' : false }, { 'event_type' : 'next', 'selector' :
         * '.blocklyTreeRoot', 'description' : 'TOUR1_DESCRIPTION07',
         * 'nextButton' : { text : 'TOUR1_DESCRIPTION00' }, 'showSkip' : false,
         * onBeforeStart : function() { $('#tabProgram').trigger('click'); } }, {
         * 'event_type' : 'next', 'selector' : '#blocklyDiv>svg>g>g:eq(1)',
         * 'description' : 'TOUR1_DESCRIPTION08', 'bottom' : -100, 'nextButton' : {
         * text : 'TOUR1_DESCRIPTION00' }, 'showSkip' : false, onBeforeStart :
         * function() { Blockly.hideChaff(); } }, { 'event_type' : 'next',
         * 'selector' : '.blocklyButtons:eq(1)', 'description' :
         * 'TOUR1_DESCRIPTION09', 'right' : -50, 'nextButton' : { text :
         * 'TOUR1_DESCRIPTION00' }, 'showSkip' : false },
         */
    {
        'event' : 'click touchend',
        'selector' : '.blocklyTreeRow:eq(1)',
        'description' : 'TOUR1_DESCRIPTION10',
        'showSkip' : false
    }, {
        'event' : 'mousedown touchstart',
        'selector' : '.blocklyFlyout>g>g>g',
        'description' : 'TOUR1_DESCRIPTION12',
        'showSkip' : false,
    }, {
        'event_type' : 'next',
        'selector' : '.blocklyBlockCanvas',
        'description' : 'TOUR1_DESCRIPTION12',
        'bottom' : -100,
        'showSkip' : false,
        'nextButton' : {
            text : 'TOUR1_DESCRIPTION00'
        },
    }, {
        'event' : 'SimLoaded',
        'event_type' : 'SimLoaded',
        'selector' : '#progSim', //'.blocklyButtons>g:eq(1)>rect',
        'description' : 'TOUR1_DESCRIPTION13',
        'showSkip' : false,
        'showNext' : true,
        onBeforeStart : function() {
            var blocks = Blockly.getMainWorkspace().getTopBlocks();
            if (!blocks[0].getNextBlock()) {
                enjoyhint_instance.setCurrentStepBack();
            }
        }
    }, {
        'event' : 'mousedown touchstart',
        'selector' : '#simControl',
        'description' : 'TOUR1_DESCRIPTION13a',
        'showSkip' : false,
    }, {
        'event_type' : 'next',
        'selector' : '#simDiv',
        'description' : 'TOUR1_DESCRIPTION15',
        'shape' : 'circle',
        'radius' : $('#blockly').width() / 10,
        'top' : offsetTop,
        'left' : offsetLeft,
        'showSkip' : false
    }, {
        'event' : 'click',
        'selector' : '#progSim',
        'description' : 'TOUR1_DESCRIPTION16',
        'showSkip' : false
    } ];
});
