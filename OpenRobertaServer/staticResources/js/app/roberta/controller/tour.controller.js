define(['exports', 'comm', 'message', 'log', 'blockly', 'jquery', 'jquery-scrollto', 'enjoyHint'], function(exports, COMM, MSG, LOG, Blockly, $) {

    var enjoyhint_instance;
    var touchEvent;

    function is_touch_device() {
        try {
            document.createEvent("TouchEvent");
            return 'click touchend';
        } catch (e) {
            return 'click';
        }
    }

    function start(tour) {
        enjoyhint_instance = new EnjoyHint({
            onSkip: function() {
                Blockly.mainWorkspace.clear();    
                enjoyhint_instance = {};
                $('#tabProgram').trigger('click');
                if ($('.rightMenuButton.rightActive')) {
                    $('.rightMenuButton.rightActive').trigger('click');
                }
                $("#show-startup-message").modal("show");
            },
            onEnd: function() {
                Blockly.mainWorkspace.clear();
                enjoyhint_instance = {};
                $('#tabProgram').trigger('click');
                if ($('.rightMenuButton.rightActive')) {
                    $('.rightMenuButton.rightActive').trigger('click');
                }
                setTimeout(function() {
                    $("#show-startup-message").modal("show");                  
                }, 1000);                 
            }
        });
        var enjoyhint_script_steps = [{}];
        switch (tour) {
            case 'welcome':
                for (var i = 0, len = welcome.length; i < len; ++i) {
                    enjoyhint_script_steps[i] = jQuery.extend(true, {}, welcome[i]);
                }
                break;
            case 'overview':
                for (var i = 0, len = overview.length; i < len; ++i) {
                    enjoyhint_script_steps[i] = jQuery.extend(true, {}, overview[i]);
                }
                break;
            default:
                return;
        }
        
        // async translation
        var key, keyParts, translation;
        for (var i = 0; i < enjoyhint_script_steps.length; i++) {
            if (enjoyhint_script_steps[i].description) {
                key = enjoyhint_script_steps[i].description;
                keyParts = key.split('.');
                translation = window;
                
                for (var j = 0; j < keyParts.length && translation !== null; j++) {
                    translation = translation[keyParts[j]];
                }
                enjoyhint_script_steps[i].description = translation || enjoyhint_script_steps[i].description;
            }
            if (enjoyhint_script_steps[i].nextButton && enjoyhint_script_steps[i].nextButton.text) {
                key = enjoyhint_script_steps[i].nextButton.text;
                keyParts = key.split('.');
                translation = window;
                
                for (var j = 0; j < keyParts.length && translation !== null; j++) {
                    translation = translation[keyParts[j]];
                }
                enjoyhint_script_steps[i].nextButton.text = translation || enjoyhint_script_steps[i].nextButton.text;
            }
            if (enjoyhint_script_steps[i].skipButton && enjoyhint_script_steps[i].skipButton.text) {
                key = enjoyhint_script_steps[i].skipButton.text;
                keyParts = key.split('.');
                translation = window;
                
                for (var j = 0; j < keyParts.length && translation !== null; j++) {
                    translation = translation[keyParts[j]];
                }
                enjoyhint_script_steps[i].skipButton.text = translation || enjoyhint_script_steps[i].skipButton.text;
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
    var welcome = [{
            'event_type': 'next',
            'selector': '.logo',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION01',
            'top': 77,
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false,
        },
        {
            'event': 'click touchend',
            'selector': '.blocklyTreeRow:eq(1)',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION10',
            'showSkip': false,
        }, {
            'event': 'mousedown touchstart',
            'selector': '.blocklyFlyout>g>g>g',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION12',
            'showSkip': false,
        }, {
            'event_type': 'next',
            'selector': '.blocklyBlockCanvas',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION12',
            'bottom': -100,
            'showSkip': false,
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
        }, {
            'event_type': 'custom',
            'event': 'startSim',
            'selector': '#simButton',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION13',
            'showSkip': false,
            onBeforeStart: function() {
                var blocks = Blockly.getMainWorkspace().getTopBlocks();
                if (!blocks[0].getNextBlock()) {
                    enjoyhint_instance.setCurrentStepBack();
                }
            }
        }, {
            'event': 'mousedown touchstart',
            'timeout':1000,            
            'selector': '#simControl',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION13a',
            'showSkip': false,
        }, {
            'event_type': 'next',
            'selector': '#simDiv',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION15',
            'shape': 'circle',
            'radius': $('#blockly').width() / 10 + $('#blockly').height() / 10,
            'top': offsetTop,
            'left': offsetLeft,
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false,

        }, {
            'event': 'mousedown touchstart',
            'selector': '#simButton',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION16',
            'showSkip': false,
        }
    ];

    var overview = [{
            'event_type': 'next',
            'selector': '.logo',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION01',
            'top': 77,
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false
        },
        {
            'event_type': 'next',
            'selector': '#head-navigation',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION02',
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false
        },
        {
            'event_type': 'next',
            'selector': '#mainNavigationBar',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION03',
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false
        },
        {
            'event': 'click',
            'selector': 'a#tabConfiguration',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION04',
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false
        }, {
            'event_type': 'next',
            'selector': '#bricklyDiv .blocklyBlockCanvas',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION05',
            'shape':'circle',
            'radius':100,
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false,
            onBeforeStart: function() {
                $('#tabConfiguration').trigger('click');
            }
        }, {
            'event': 'click',
            'selector': 'a#tabProgram',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION06',
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false
        }, {
            'event_type': 'next',
            'selector': '.blocklyTreeRoot',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION07',          
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false,
            onBeforeStart: function() {
                $('#tabProgram').trigger('click');
            }
       }, {
           'event_type': 'next',
           'selector': '.nav.nav-tabs.levelTabs',
           'description': 'Blockly.Msg.TOUR1_DESCRIPTION07a',          
           'nextButton': {
               text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
           },
           onBeforeStart: function() {
               Blockly.hideChaff(false);
           },
           'showSkip': false,          
       }, {
           'event_type': 'next',
           'selector': '#blocklyDiv>svg>g>g:eq(1)',
           'description': 'Blockly.Msg.TOUR1_DESCRIPTION08',
           'bottom': -100,
           'nextButton': {
               text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
           },
           'showSkip': false,          
            onBeforeStart: function() {
                $('#beginner').trigger('click');
            }
        }, {
            'event_type': 'next',
            'selector': '.blocklyButtons:eq(1)',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION09',
            'right': -50,
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false
        },
        {
            'event': 'click touchend',
            'selector': '.blocklyTreeRow:eq(1)',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION10',
            'showSkip': false
        }, {
            'event': 'mousedown touchstart',
            'selector': '.blocklyFlyout>g>g>g',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION12',
            'showSkip': false,
        }, {
            'event_type': 'next',
            'selector': '.blocklyBlockCanvas',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION12',
            'bottom': -100,
            'showSkip': false,
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
        }, {
            'event_type': 'custom',
            'event': 'startSim',
            'selector': '#simButton', 
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION13',
            'showSkip': false,
            onBeforeStart: function() {
                var blocks = Blockly.getMainWorkspace().getTopBlocks();
                if (!blocks[0].getNextBlock()) {
                    enjoyhint_instance.setCurrentStepBack();
                }
            }
        }, {
            'event': 'mousedown touchstart',
            'timeout':1000,            
            'selector': '#simControl',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION13a',
            'showSkip': false,
        }, {
            'event_type': 'next',
            'selector': '#simDiv',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION15',
            'shape': 'circle',
            'radius': $('#blockly').width() / 10 + $('#blockly').height() / 10,
            'top': offsetTop,
            'left': offsetLeft,
            'nextButton': {
                text: 'Blockly.Msg.TOUR1_DESCRIPTION00'
            },
            'showSkip': false,

        }, {
            'event': 'mousedown touchstart',
            'selector': '#simButton',
            'description': 'Blockly.Msg.TOUR1_DESCRIPTION16',
            'showSkip': false,
        }
    ];
});
