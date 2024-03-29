import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'jquery-scrollto';
import 'enjoyHint';

var enjoyhint_instance;
var touchEvent;

function is_touch_device() {
    try {
        document.createEvent('TouchEvent');
        return 'click touchend';
    } catch (e) {
        return 'click';
    }
}

function start(tour) {
    if (!tour || (tour !== 'welcome' && tour !== 'overview')) {
        end();
        LOG.error('Tour not available: ' + tour);
        return;
    }
    function end() {
        $('#enjoyHintAnchor').remove();
        $('#enjoyHintStart').remove();
        Blockly.mainWorkspace.clear();
        enjoyhint_instance = {};
        if ($('.rightMenuButton.rightActive')) {
            $('.rightMenuButton.rightActive').clickWrap();
        }
        $('#tabStart').tabWrapShow();
    }
    enjoyhint_instance = new EnjoyHint({
        onStart: function () {
            var size, x, y;
            if ($(window).width() < 768) {
                size = Math.min($(window).width() / 3, $(window).height() / 2);
                x = 100;
                y = ($(window).height() * 2) / 5;
            } else if ($(window).width() < $(window).height()) {
                size = Math.min($(window).width() / 3, $(window).height() / 2);
                x = $(window).width() / 2;
                y = ($(window).height() - size) / 2;
            } else {
                size = Math.min($(window).width() / 3, $(window).height() / 2);
                x = $(window).width() / 2;
                y = ($(window).height() - size) / 3;
            }
            $('<div>', {
                id: 'enjoyHintAnchor',
            })
                .css({ width: size, height: size, top: y, left: x, position: 'absolute' })
                .appendTo('body');
            $('<div>', {
                id: 'enjoyHintStart',
            })
                .css({ width: 0, height: 0, top: 30, right: 31, position: 'absolute' })
                .appendTo('body');
        },
        onSkip: function () {
            end();
        },
        onEnd: function () {
            end();
        },
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
            break;
    }

    // translation
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
    LOG.info('Tour ' + tour + ' started');
}

function getInstance() {
    return enjoyhint_instance;
}
export { start, getInstance };

var offsetLeft = $('#blocklyDiv').width() * -0.15;
var offsetTop = $('#blocklyDiv').height() * -0.1;
var welcome = [
    {
        event_type: 'next',
        selector: '#enjoyHintStart',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION01',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
    },
    {
        event: 'click touchend',
        selector: '.blocklyTreeRow:eq(1)',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION10',
        showSkip: false,
    },
    {
        event: 'mousedown touchstart',
        selector: '.blocklyFlyout>g>g>g',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION12',
        showSkip: false,
    },
    {
        event_type: 'next',
        selector: '.blocklyBlockCanvas',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION12',
        bottom: -100,
        showSkip: false,
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
    },
    {
        event_type: 'custom',
        event: 'startSim',
        selector: '#simButton',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION13',
        showSkip: false,
        onBeforeStart: function () {
            var blocks = Blockly.getMainWorkspace().getTopBlocks();
            if (!blocks[0].getNextBlock()) {
                enjoyhint_instance.setCurrentStepBack();
            }
        },
    },
    {
        event: 'mousedown touchstart',
        timeout: 1000,
        selector: '#simControl',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION13a',
        showSkip: false,
    },
    {
        event_type: 'next',
        selector: '#enjoyHintAnchor',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION15',
        shape: 'circle',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
    },
    {
        event: 'mousedown touchstart',
        selector: '#simButton',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION16',
        showSkip: false,
    },
    {
        event: 'mousedown touchstart',
        selector: '#simButton',
        timeout: 1000,
        showSkip: false,
        onBeforeStart: function () {
            setTimeout(function () {
                $('.enjoyhint_close_btn').trigger('click');
            }, 1000);
        },
    },
];

var overview = [
    {
        event_type: 'next',
        selector: '#enjoyHintStart',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION01',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
    },
    {
        event_type: 'next',
        selector: '#head-navigation',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION02',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
    },
    {
        event_type: 'next',
        selector: '#mainNavigationBar',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION03',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
    },
    {
        event: 'click',
        selector: 'a#tabConfiguration',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION04',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
    },
    {
        event_type: 'next',
        selector: '#bricklyDiv .blocklyBlockCanvas>.blocklyDraggable>.blocklyDraggable',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION05',
        shape: 'circle',
        radius: 200,
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
        onBeforeStart: function () {
            $('#tabConfiguration').clickWrap();
        },
    },
    {
        event: 'click',
        selector: 'a#tabProgram',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION06',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
    },
    {
        event_type: 'next',
        selector: '.blocklyTreeRoot',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION07',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
        onBeforeStart: function () {
            $('#tabProgram').tabWrapShow();
        },
    },
    {
        event_type: 'next',
        selector: '.nav.nav-tabs.levelTabs',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION07a',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        onBeforeStart: function () {
            Blockly.hideChaff(false);
        },
        showSkip: false,
    },
    {
        event_type: 'next',
        selector: '#blocklyDiv>svg>g>g:eq(1)',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION08',
        bottom: -100,
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
        onBeforeStart: function () {
            $('#beginner').clickWrap();
        },
    },
    {
        event_type: 'next',
        selector: '.blocklyButtons:eq(1)',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION09',
        right: -50,
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
    },
    {
        event: 'click touchend',
        selector: '.blocklyTreeRow:eq(1)',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION10',
        showSkip: false,
    },
    {
        event: 'mousedown touchstart',
        selector: '.blocklyFlyout>g>g>g',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION12',
        showSkip: false,
    },
    {
        event_type: 'next',
        selector: '.blocklyBlockCanvas',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION12',
        bottom: -100,
        showSkip: false,
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
    },
    {
        event_type: 'custom',
        event: 'startSim',
        selector: '#simButton',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION13',
        showSkip: false,
        onBeforeStart: function () {
            var blocks = Blockly.getMainWorkspace().getTopBlocks();
            if (!blocks[0].getNextBlock()) {
                enjoyhint_instance.setCurrentStepBack();
            }
        },
    },
    {
        event: 'mousedown touchstart',
        timeout: 1000,
        selector: '#simControl',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION13a',
        showSkip: false,
    },
    {
        event_type: 'next',
        selector: '#enjoyHintAnchor',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION15',
        shape: 'circle',
        nextButton: {
            text: 'Blockly.Msg.TOUR1_DESCRIPTION00',
        },
        showSkip: false,
    },
    {
        event: 'mousedown touchstart',
        selector: '#simButton',
        description: 'Blockly.Msg.TOUR1_DESCRIPTION16',
        showSkip: false,
    },
    {
        event: 'mousedown touchstart',
        selector: '#simButton',
        timeout: 1000,
        showSkip: false,
        onBeforeStart: function () {
            setTimeout(function () {
                $('.enjoyhint_close_btn').trigger('click');
            }, 1000);
        },
    },
];
