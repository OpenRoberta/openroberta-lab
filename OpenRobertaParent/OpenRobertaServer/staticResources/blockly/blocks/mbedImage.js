/**
 * @fileoverview Action blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */
'use strict';

goog.provide('Blockly.Blocks.mbedImage');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['mbedImage_image'] = {
    /**
     * Represents an image.
     * 
     * @constructs mbedImage_image
     * @this.Blockly.Block
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_IMAGE_RGB);
        this.appendDummyInput().appendField("    0       1       2       3       4");
        this.appendDummyInput().appendField("0").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P00").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P10").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P20").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P30").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P40");
        this.appendDummyInput().appendField("1").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P01").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P11").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P21").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P31").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P41");
        this.appendDummyInput().appendField("2").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P02").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P12").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P22").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P32").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P42");
        this.appendDummyInput().appendField("3").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P03").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P13").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P23").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P33").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P43");
        this.appendDummyInput().appendField("4").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P04").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P14").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P24").appendField(new Blockly.FieldPixelbox(
                ' ', this.validate_), "P34").appendField(new Blockly.FieldPixelbox(' ', this.validate_), "P44");
        this.setOutput(true, 'Image');
        this.setTooltip(Blockly.Msg.IMAGE_TOOLTIP);
    },
    validate_ : function(p) {
        p = p.trim();
        if (p == '' || p == '  ' || p.substring(1, 1) == ' ') {
            if (Blockly.FieldTextInput.htmlInput_)
                Blockly.FieldTextInput.htmlInput_.value = '#';
            return '';
        } else if (p.substring(0, 1) == '#') {
            if (Blockly.FieldTextInput.htmlInput_)
                Blockly.FieldTextInput.htmlInput_.value = '';
            return '#';
        } else if (p.substring(0, 1) == '0') {
            return '';
        } else if (p.substring(0, 1) == '9') {
            return '#';
        } else if (p.match(/^[1-8#]$/)) {
            return p;
        } else if (p.substring(0, 1).match(/^[1-8#]/)) {
            return p.substring(0, 1);
        } else {
            return null;
        }
    }
};

Blockly.Blocks['mbedImage_shift'] = {
    init : function() {
        this.jsonInit({
            "message0" : Blockly.Msg.IMAGE_SHIFT + " %1 %2 %3",
            "args0" : [ {
                "type" : "input_value",
                "name" : "A",
                "check" : "Image"
            }, {
                "type" : "field_dropdown",
                "name" : "OP",
                "options" : [ [ '↑', 'UP' ], [ '↓', 'DOWN' ], [ '→', 'RIGHT' ], [ '←', 'LEFT' ] ]
            }, {
                "type" : "input_value",
                "name" : "B",
                "check" : "Number"
            } ],
            "inputsInline" : true,
            "output" : "Image",
            "colour" : Blockly.CAT_IMAGE_RGB,
            "tooltip" : Blockly.Msg.IMAGE_SHIFT_TOOLTIP
        });
    }
};

Blockly.Blocks['mbedImage_invert'] = {
    /**
     * Block to invert an image.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.jsonInit({
            "message0" : Blockly.Msg.IMAGE_INVERT + " %1",
            "args0" : [ {
                "type" : "input_value",
                "name" : "VAR",
                "check" : "Image"
            } ],
            "output" : "Image",
            "colour" : Blockly.CAT_IMAGE_RGB,
            "tooltip" : Blockly.Msg.IMAGE_INVERT_TOOLTIP
        });
    }
};

// This is just a draft. TODO if or how many images we would like to provide.
Blockly.Blocks['mbedImage_get_image'] = {
    /**
     * Block to get a predefined image.
     * 
     * @this Blockly.Block
     */
    init : function() {
        var options = [ [ 'heart', 'HEART' ], [ 'heart small', 'HEART_SMALL' ], [ 'happy', 'HAPPY' ], [ 'smile', 'SMILE' ], [ 'confused', 'CONFUSED' ],
                [ 'angry', 'ANGRY' ], [ 'asleep', 'ASLEEP' ], [ 'surprised', 'SURPRISED' ], [ 'silly', 'SILLY' ], [ 'fabulous', 'FABULOUS' ],
                [ 'meh!', 'MEH' ], [ 'yes', 'YES' ], [ 'no', 'NO' ], [ 'triangle', 'TRIANGLE' ], [ 'triangle left', 'TRIANGLE_LEFT' ],
                [ 'chessboard', 'CHESSBOARD' ], [ 'diamond', 'DIAMOND' ], [ 'diamond small', 'DIAMOND_SMALL' ], [ 'square', 'SQUARE' ],
                [ 'square small', 'SQUARE_SMALL' ], [ 'rabbit', 'RABBIT' ], [ 'cow', 'COW' ], [ 'music crotchet', 'MUSIC_CROTCHET' ],
                [ 'music quaver', 'MUSIC_QUAVER' ], [ 'music quavers', 'MUSIC_QUAVERS' ], [ 'pitchfork', 'PITCHFORK' ], [ 'xmas', 'XMAS' ],
                [ 'pacman', 'PACMAN' ], [ 'target', 'TARGET' ], [ 'T-shirt', 'TSHIRT' ], [ 'rollerskate', 'ROLLERSKATE' ], [ 'duck', 'DUCK' ],
                [ 'house', 'HOUSE' ], [ 'tortoise', 'TORTOISE' ], [ 'butterfly', 'BUTTERFLY' ], [ 'stickfigure', 'STICKFIGURE' ], [ 'ghost', 'GHOST' ],
                [ 'sword', 'SWORD' ], [ 'giraffe', 'GIRAFFE' ], [ 'skull', 'SKULL' ], [ 'umbrella', 'UMBRELLA' ], [ 'snake', 'SNAKE' ], [ 'sad', 'SAD' ] ];
        var dropdown = new Blockly.FieldDropdownImage(options, '/dropDowns/', 24, 24, 'png');
        this.setColour(Blockly.CAT_IMAGE_RGB);
        this.appendDummyInput().appendField(dropdown, 'IMAGE');
        this.setOutput(true, 'Image');
        var thisBlock = this;
        this.setTooltip(function() {
            var mode = thisBlock.getFieldValue('IMAGE');
            var TOOLTIPS = {
                'HEART' : Blockly.Msg.IMAGE_GET_TOOLTIP_HEART,
                'HEART_SMALL' : Blockly.Msg.IMAGE_GET_TOOLTIP_HEART_SMALL,
                'SMILE' : Blockly.Msg.IMAGE_GET_TOOLTIP_SMILE,
                'CONFUSED' : Blockly.Msg.IMAGE_GET_TOOLTIP_CONFUSED,
                'ANGRY' : Blockly.Msg.IMAGE_GET_TOOLTIP_ANGRY,
                'ASLEEP' : Blockly.Msg.IMAGE_GET_TOOLTIP_ASLEEP,
                'SILLY' : Blockly.Msg.IMAGE_GET_TOOLTIP_SILLY,
                'FABULOUS' : Blockly.Msg.IMAGE_GET_TOOLTIP_FABULOUS,
                'MEH' : Blockly.Msg.IMAGE_GET_TOOLTIP_MEH,
                'YES' : Blockly.Msg.IMAGE_GET_TOOLTIP_YES,
                'NO' : Blockly.Msg.IMAGE_GET_TOOLTIP_NO,
                'TRIANGLE' : Blockly.Msg.IMAGE_GET_TOOLTIP_TRIANGLE,
                'TRIANGLE_LEFT' : Blockly.Msg.IMAGE_GET_TOOLTIP_TRIANGLE_LEFT,
                'CHESSBOARD' : Blockly.Msg.IMAGE_GET_TOOLTIP_CHESSBOARD,
                'DIAMOND' : Blockly.Msg.IMAGE_GET_TOOLTIP_DIAMOND,
                'DIAMOND_SMALL' : Blockly.Msg.IMAGE_GET_TOOLTIP_DIAMOND_SMALL,
                'SQUARE' : Blockly.Msg.IMAGE_GET_TOOLTIP_SQUARE,
                'SQUARE_SMALL' : Blockly.Msg.IMAGE_GET_TOOLTIP_SQUARE_SMALL,
                'RABBIT' : Blockly.Msg.IMAGE_GET_TOOLTIP_RABBIT,
                'COW' : Blockly.Msg.IMAGE_GET_TOOLTIP_COW,
                'MUSIC_CROTCHET' : Blockly.Msg.IMAGE_GET_TOOLTIP_MUSIC_CROTCHET,
                'MUSIC_QUAVER' : Blockly.Msg.IMAGE_GET_TOOLTIP_MUSIC_QUAVER,
                'MUSIC_QUAVERS' : Blockly.Msg.IMAGE_GET_TOOLTIP_MUSIC_QUAVERS,
                'PITCHFORK' : Blockly.Msg.IMAGE_GET_TOOLTIP_PITCHFORK,
                'XMAS' : Blockly.Msg.IMAGE_GET_TOOLTIP_XMAS,
                'PACMAN' : Blockly.Msg.IMAGE_GET_TOOLTIP_PACMAN,
                'TARGET' : Blockly.Msg.IMAGE_GET_TOOLTIP_TARGET,
                'TSHIRT' : Blockly.Msg.IMAGE_GET_TOOLTIP_TSHIRT,
                'ROLLERSKATE' : Blockly.Msg.IMAGE_GET_TOOLTIP_ROLLERSKATE,
                'DUCK' : Blockly.Msg.IMAGE_GET_TOOLTIP_DUCK,
                'HOUSE' : Blockly.Msg.IMAGE_GET_TOOLTIP_HOUSE,
                'TORTOISE' : Blockly.Msg.IMAGE_GET_TOOLTIP_TORTOISE,
                'BUTTERFLY' : Blockly.Msg.IMAGE_GET_TOOLTIP_BUTTERFLY,
                'STICKFIGURE' : Blockly.Msg.IMAGE_GET_TOOLTIP_STICKFIGURE,
                'GHOST' : Blockly.Msg.IMAGE_GET_TOOLTIP_GHOST,
                'SWORD' : Blockly.Msg.IMAGE_GET_TOOLTIP_SWORD,
                'GIRAFFE' : Blockly.Msg.IMAGE_GET_TOOLTIP_GIRAFFE,
                'SKULL' : Blockly.Msg.IMAGE_GET_TOOLTIP_SKULL,
                'UMBRELLA' : Blockly.Msg.IMAGE_GET_TOOLTIP_UMBRELLA,
                'SNAKE' : Blockly.Msg.IMAGE_GET_TOOLTIP_SNAKE,
                'SAD' : Blockly.Msg.IMAGE_GET_TOOLTIP_SAD
            };
            return TOOLTIPS[mode];
        });
    }
};
