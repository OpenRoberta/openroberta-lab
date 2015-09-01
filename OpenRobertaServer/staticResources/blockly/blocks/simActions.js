/**
 * @fileoverview Action blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */
'use strict';

goog.provide('Blockly.Blocks.simActions');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['sim_motor_on'] = {
        /**
         * Turn motor on with specific power.
         * 
         * @constructs sim_motor_on
         * @this.Blockly.Block
         * @param {String/dropdown}
         *            MOTORPORT - B, C
         * @param {Number}
         *            POWER relative - -100-100
         * @returns immediately
         * @memberof Block
         */

        init : function() {
            this.setHelpUrl(Blockly.Msg.MOTOR_ON_HELPURL);
            this.setColourRGB(Blockly.CAT_ACTION_RGB);
            var motorPort = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR + ' ' + Blockly.Msg.MOTOR_RIGHT, 'B' ], [ Blockly.Msg.MOTOR + ' ' + Blockly.Msg.MOTOR_LEFT, 'C' ] ]);
            this.appendValueInput('POWER').appendField(motorPort, 'MOTORPORT').appendField(Blockly.Msg.ON).appendField(Blockly.Msg.MOTOR_SPEED).setCheck('Number');
            this.setPreviousStatement(true);
            this.setNextStatement(true);
            this.setTooltip(Blockly.Msg.MOTOR_ON_TOOLTIP);
            this.setHelp(new Blockly.Help(Blockly.Msg.MOTOR_ON_HELP));
        }
    };

    Blockly.Blocks['sim_motor_on_for'] = {
        /**
         * Turn motor on and stop motor after execution of rotations/degrees.
         * 
         * @constructs robActions_motor_on_for
         * @this.Blockly.Block
         * @param {String/dropdown}
         *            MOTORPORT - A, B, C, or D
         * @param {String/dropdown}
         *            MOTORROTATION - Rotations or Degrees
         * @param {Number}
         *            POWER Speed relative - -100-100
         * @param {Number}
         *            VALUE Number of rotations/degrees
         * @returns after execution
         * @memberof Block
         */
        init : function() {
            this.setHelpUrl(Blockly.Msg.MOTOR_ON_FOR_HELPURL);
            this.setColourRGB(Blockly.CAT_ACTION_RGB);
            // this.setInputsInline(true);
            var motorPort = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR + ' ' + Blockly.Msg.MOTOR_RIGHT, 'B' ], [ Blockly.Msg.MOTOR + ' ' + Blockly.Msg.MOTOR_LEFT, 'C' ] ]);
            var motorRotation = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_ROTATION, 'ROTATIONS' ], [ Blockly.Msg.MOTOR_DEGREE, 'DEGREE' ] ]);
            this.appendValueInput('POWER').appendField(motorPort, 'MOTORPORT').appendField(Blockly.Msg.ON).appendField(Blockly.Msg.MOTOR_SPEED).setCheck('Number');
            this.appendValueInput('VALUE').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.FOR).appendField(motorRotation, 'MOTORROTATION')
                    .setCheck('Number');
            this.setPreviousStatement(true);
            this.setNextStatement(true);
            this.setTooltip(Blockly.Msg.MOTOR_ON_FOR_TOOLTIP);
            this.setHelp(new Blockly.Help(Blockly.Msg.MOTOR_ON_FOR_HELP));
        }
    };
    
    Blockly.Blocks['sim_motor_stop'] = {
            /**
             * Stop this motor.
             * 
             * @constructs robActions_motor_stop
             * @this.Blockly.Block
             * @param {String/dropdown}
             *            MOTORPORT - A, B, C, or D
             * @param {String/dropdown}
             *            MODE - Float or Non Float
             * @returns immediately
             * @memberof Block
             */
            init : function() {
                this.setHelpUrl(Blockly.Msg.MOTOR_STOP_HELPURL);
                this.setColourRGB(Blockly.CAT_ACTION_RGB);
                var motorPort = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR + ' ' + Blockly.Msg.MOTOR_RIGHT, 'B' ], [ Blockly.Msg.MOTOR + ' ' + Blockly.Msg.MOTOR_LEFT, 'C' ] ]);
                var mode = new Blockly.FieldDropdown([ [ '', 'FLOAT' ], [ '', 'NONFLOAT' ] ]);
                mode.setVisible(false);
                this.appendDummyInput().appendField(Blockly.Msg.MOTOR_STOP).appendField(motorPort, 'MOTORPORT').appendField(mode, 'MODE');
                this.setPreviousStatement(true);
                this.setNextStatement(true);
                this.setTooltip(Blockly.Msg.MOTOR_STOP_TOOLTIP);
                this.setHelp(new Blockly.Help(Blockly.Msg.MOTOR_STOP_HELP));
            }
        };

Blockly.Blocks['sim_LED_on'] = {
    /**
     * Turn LED on.
     * 
     * @constructs simActions_LED_on
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            SWITCH_COLOR - Green, Orange or Blue
     * @param {Boolean/dropdown}
     *            SWITCH_BLINK - True or False
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.BRICKLIGHT_ON_HELPURL);
        this.setColourRGB(Blockly.CAT_ACTION_RGB);
        // this.setInputsInline(true);
        var dropdownColor = new Blockly.FieldDropdown([ [ Blockly.Msg.BRICKLIGHT_GREEN, 'GREEN' ], [ Blockly.Msg.BRICKLIGHT_ORANGE, 'ORANGE' ],
                [ Blockly.Msg.BRICKLIGHT_RED, 'RED' ] ]);
        var dropdownLightState = new Blockly.FieldDropdown([ [ Blockly.Msg.BRICKLIGHT_ON, 'ON' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.BRICKLIGHT);
        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.BRICKLIGHT_COLOR).appendField(dropdownColor, 'SWITCH_COLOR');
        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.MOD).appendField(dropdownLightState, 'SWITCH_BLINK');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BRICKLIGHT_ON_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.BRICKLIGHT_ON_HELP));
    }
};

Blockly.Blocks['sim_LED_off'] = {
    /**
     * Turn bricklight off.
     * 
     * @constructs simActions_LED_off
     * @this.Blockly.Block
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.BRICKLIGHT_OFF_HELP);
        this.setColourRGB(Blockly.CAT_ACTION_RGB);
         this.appendDummyInput().appendField(Blockly.Msg.BRICKLIGHT).appendField(Blockly.Msg.OFF);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BRICKLIGHT_OFF_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.BRICKLIGHT_OFF_HELP));
    }
};
