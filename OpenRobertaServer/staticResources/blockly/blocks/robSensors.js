/**
 * @fileoverview Sensor blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */

'use strict';

goog.provide('Blockly.Blocks.robSensors');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['robSensors_getSample'] = {
    /**
     * Get the current reading from choosen sensor.
     * 
     * @constructs robSensors_getSample
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            SENSORPORT - 1, 2, 3 or 4
     * @returns immediately
     * @returns {Number}
     * @memberof Block
     */
    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        var sensorType = new Blockly.FieldDropdown([ [ Blockly.Msg.SENSOR_TOUCH + Blockly.Msg.SENSOR_PRESSED, 'TOUCH' ],
                [ Blockly.Msg.MODE_DISTANCE + ' ' + Blockly.Msg.SENSOR_ULTRASONIC, 'ULTRASONIC_DISTANCE' ],
                [ Blockly.Msg.MODE_PRESENCE + ' ' + Blockly.Msg.SENSOR_ULTRASONIC, 'ULTRASONIC_PRESENCE' ],
                [ Blockly.Msg.MODE_COLOUR + ' ' + Blockly.Msg.SENSOR_COLOUR, 'COLOUR_COLOUR' ],
                [ Blockly.Msg.MODE_LIGHT + ' ' + Blockly.Msg.SENSOR_COLOUR, 'COLOUR_LIGHT' ],
                [ Blockly.Msg.MODE_AMBIENTLIGHT + ' ' + Blockly.Msg.SENSOR_COLOUR, 'COLOUR_AMBIENTLIGHT' ],
                [ Blockly.Msg.MODE_DISTANCE + ' ' + Blockly.Msg.SENSOR_INFRARED, 'INFRARED_DISTANCE' ],
                [ Blockly.Msg.MODE_ROTATION + ' ' + Blockly.Msg.SENSOR_ENCODER, 'ENCODER_ROTATION' ],
                [ Blockly.Msg.MODE_DEGREE + ' ' + Blockly.Msg.SENSOR_ENCODER, 'ENCODER_DEGREE' ],
                [ Blockly.Msg.SENSOR_KEY + ' ' + Blockly.Msg.SENSOR_PRESSED, 'KEYS_PRESSED' ],
                [ Blockly.Msg.MODE_ANGLE + ' ' + Blockly.Msg.SENSOR_GYRO, 'GYRO_ANGLE' ],
                [ Blockly.Msg.MODE_RATE + ' ' + Blockly.Msg.SENSOR_GYRO, 'GYRO_RATE' ], [ Blockly.Msg.SENSOR_TIME, 'TIME' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('SENSORTYPE') !== option) {
                this.sourceBlock_.updateShape_(option);
            }
        });
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        this.appendDummyInput('DROPDOWN').appendField(Blockly.Msg.GET, 'GET').appendField(sensorType, 'SENSORTYPE').appendField(sensorPort, 'SENSORPORT');
        this.sensorType_ = 'TOUCH';
        this.setOutput(true, 'Boolean');
        this.setTooltip(Blockly.Msg.GETSAMPLE_TOOLTIP);
    },
    /**
     * Create XML to represent whether the sensor type has changed.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        var container = document.createElement('mutation');
        container.setAttribute('input', this.sensorType_);
        return container;
    },
    /**
     * Parse XML to restore the sensor type.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        var input = xmlElement.getAttribute('input');
        this.sensorType_ = input;
        this.updateShape_(this.sensorType_);
    },

    /**
     * Called whenever anything on the workspace changes.
     * 
     * @this Blockly.Block
     */
    /*
     * onchange : function() { if (!this.workspace) { // Block has been deleted.
     * return; } else if (this.update) this.updateShape_(); },
     */
    /**
     * Called whenever the shape has to change.
     * 
     * @this Blockly.Block
     */
    updateShape_ : function(option) {
        this.sensorType_ = option;
        var key = new Blockly.FieldDropdown([ [ Blockly.Msg.SENSOR_KEY_ENTER, 'ENTER' ], [ Blockly.Msg.SENSOR_KEY_UP, 'UP' ],
                [ Blockly.Msg.SENSOR_KEY_DOWN, 'DOWN' ], [ Blockly.Msg.SENSOR_KEY_LEFT, 'LEFT' ], [ Blockly.Msg.SENSOR_KEY_RIGHT, 'RIGHT' ],
                [ Blockly.Msg.SENSOR_KEY_ESCAPE, 'ESCAPE' ], [ Blockly.Msg.SENSOR_KEY_ANY, 'ANY' ] ]);
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        var motorPort = new Blockly.FieldDropdown([ [ 'Motor Port A', 'A' ], [ 'Motor Port B', 'B' ], [ 'Motor Port C', 'C' ], [ 'Motor Port D', 'D' ] ]);
        var sensorNum = new Blockly.FieldDropdown([ [ Blockly.Msg.SENSOR_TIMER + ' 1', '1' ], [ Blockly.Msg.SENSOR_TIMER + ' 2', '2' ],
                [ Blockly.Msg.SENSOR_TIMER + ' 3', '3' ], [ Blockly.Msg.SENSOR_TIMER + ' 4', '4' ], [ Blockly.Msg.SENSOR_TIMER + ' 5', '5' ] ]);
        var input = this.getInput('DROPDOWN');
        var toRemove = [];
        for (var i = 0, field; field = input.fieldRow[i]; i++) {
            if (field.name === 'SENSORTYPE' || field.name === 'GET') {
                continue;
            }
            toRemove.push(field.name);
        }
        for (var j = 0; j < toRemove.length; j++) {
            input.removeField(toRemove[j]);
        }
        if (this.sensorType_ == 'ENCODER_ROTATION') {
            input.appendField(motorPort, 'MOTORPORT');
            this.appendValue_('NUM_REV', 2);
            this.changeOutput('Number');
        } else if (this.sensorType_ == 'ENCODER_DEGREE') {
            input.appendField(motorPort, 'MOTORPORT');
            this.appendValue_('NUM_REV', 180);
            this.changeOutput('Number');
        } else if (this.sensorType_ == 'KEYS_PRESSED') {
            input.appendField(key, 'KEY');
            this.appendValue_('BOOL');
            this.changeOutput('Boolean');
        } else if (this.sensorType_ == 'TIME') {
            input.appendField(Blockly.Msg.SENSOR_MS_TIMER).appendField(sensorNum, 'SENSORNUM');
            this.appendValue_('NUM_REV', 500);
            this.changeOutput('Number');
        } else {
            input.appendField(sensorPort, 'SENSORPORT');
            if (this.sensorType_ == 'TOUCH') {
                this.appendValue_('BOOL');
                this.changeOutput('Boolean');
            } else if (this.sensorType_ == 'GYRO_ANGLE' || this.sensorType_ == 'GYRO_RATE') {
                this.appendValue_('NUM_REV', 90);
                sensorPort.setValue('2');
                this.changeOutput('Number');
            } else if (this.sensorType_ == 'ULTRASONIC_DISTANCE') {
                this.appendValue_('NUM');
                sensorPort.setValue('4');
                this.changeOutput('Number');
            } else if (this.sensorType_ == 'ULTRASONIC_PRESENCE') {
                this.appendValue_('BOOL');
                sensorPort.setValue('4');
                this.changeOutput('Boolean');
            } else if (this.sensorType_ == 'COLOUR_COLOUR') {
                this.appendValue_('COLOUR');
                sensorPort.setValue('3');
                this.changeOutput('Colour');
            } else if (this.sensorType_ == 'COLOUR_LIGHT' || this.sensorType_ == 'COLOUR_AMBIENTLIGHT') {
                this.appendValue_('NUM');
                sensorPort.setValue('3');
                this.changeOutput('Number');
            } else if (this.sensorType_ == 'INFRARED_DISTANCE') {
                this.appendValue_('NUM');
                sensorPort.setValue('4');
                this.changeOutput('Number');
            }
        }
        // sensorType.setValue(this.sensorType_);
    },

    /**
     * Called whenever the blocks shape has changed.
     * 
     * @this Blockly.Block
     */
    appendValue_ : function(type, value) {
        value = value || 30;
        var logComp = this.getParent();
        if (logComp && logComp.type != 'logic_compare')
            logComp = null;
        if (logComp) {
            // change inputs, if block is in logic_compare and not rebuild from mutation.
            if (logComp.getInputTargetBlock('B')) {
                logComp.getInputTargetBlock('B').dispose();
            }
            var block = null;
            logComp.updateShape(type);
            if (type == 'NUM') {
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'math_number');
                block.setFieldValue(value.toString(), 'NUM');
            } else if (type == 'NUM_REV') {
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'math_number');
                block.setFieldValue(value.toString(), 'NUM');
            } else if (type == 'BOOL') {
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'logic_boolean');
            } else {
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'robColour_picker');
                block.setFieldValue('#b30006', 'COLOUR')
            }
            block.initSvg();
            block.render();
            var valueB = logComp.getInput('B');
            valueB.connection.connect(block.outputConnection);
        }
    }
};

Blockly.Blocks['robSensors_touch_isPressed'] = {
    /**
     * Is the touch sensor pressed?
     * 
     * @constructs robSensors_touch_isPressed
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            SENSORPORT - 1, 2, 3 or 4
     * @returns immediately
     * @returns {Boolean}
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        // this.setInputsInline(true);
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_TOUCH).appendField(sensorPort, 'SENSORPORT').appendField(Blockly.Msg.SENSOR_IS_PRESSED);
        this.setOutput(true, 'Boolean');
        this.setTooltip(Blockly.Msg.TOUCH_ISPRESSED_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_key_isPressed'] = {
    /**
     * Is the specific key (button of the brick) pressed?
     * 
     * @constructs robSensors_key_isPressed
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            KEY - Enter, Up, Down, Left, Right, Escape or Any
     * @returns immediately
     * @returns {Boolean}
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        // this.setInputsInline(true);
        var key = new Blockly.FieldDropdown([ [ Blockly.Msg.SENSOR_KEY_ENTER, 'ENTER' ], [ Blockly.Msg.SENSOR_KEY_UP, 'UP' ],
                [ Blockly.Msg.SENSOR_KEY_DOWN, 'DOWN' ], [ Blockly.Msg.SENSOR_KEY_LEFT, 'LEFT' ], [ Blockly.Msg.SENSOR_KEY_RIGHT, 'RIGHT' ],
                [ Blockly.Msg.SENSOR_KEY_ESCAPE, 'ESCAPE' ], [ Blockly.Msg.SENSOR_KEY_ANY, 'ANY' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_KEY).appendField(key, 'KEY').appendField(Blockly.Msg.SENSOR_IS_PRESSED);
        this.setOutput(true, 'Boolean');
        this.setTooltip(Blockly.Msg.KEY_ISPRESSED_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_ultrasonic_getSample'] = {
    /**
     * Get the current distance from the ultrasonic sensor.
     * 
     * @constructs robSensors_ultrasonic_getDistance
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MODE - Distance or Presence
     * @param {String/dropdown}
     *            SENSORPORT - 1, 2, 3 or 4
     * @returns immediately
     * @returns {Number}
     * @memberof Block
     */
    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        var mode = new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_DISTANCE, 'DISTANCE' ], [ Blockly.Msg.MODE_PRESENCE, 'PRESENCE' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('MODE') !== option) {
                this.sourceBlock_.updateShape_(option);
            }
        });
        this.appendDummyInput().appendField(Blockly.Msg.GET).appendField(mode, 'MODE').appendField(Blockly.Msg.SENSOR_ULTRASONIC).appendField(sensorPort,
                'SENSORPORT');
        this.sensorMode_ = 'DISTANCE';
        this.setOutput(true, 'Number');
        this.setTooltip(Blockly.Msg.ULTRASONIC_GETSAMPLE_TOOLTIP);
    },
    mutationToDom : function() {
        var container = document.createElement('mutation');
        container.setAttribute('mode', this.sensorMode_);
        return container;
    },
    domToMutation : function(xmlElement) {
        var mode = xmlElement.getAttribute('mode');
        this.sensorMode_ = mode;
        this.updateShape_(this.sensorMode_);
    },
    updateShape_ : function(option) {
        this.sensorMode_ = option;
        if (this.sensorMode_ == 'DISTANCE') {
            this.changeOutput('Number');
        } else if (this.sensorMode_ == 'PRESENCE') {
            this.changeOutput('Boolean');
        }
    }
}

Blockly.Blocks['robSensors_colour_getSample'] = {
    /**
     * Get the current reading from the colour sensor.
     * 
     * @constructs robSensors_colour_getSample
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MODE - Colour, Light or Ambient light
     * @param {String/dropdown}
     *            SENSORPORT - 1, 2, 3 or 4
     * @returns immediately
     * @returns {Number|Colour} Depending on the mode <br>
     *          Possible colours are: undefined(grey),black, blue, green, red,
     *          white, yellow, brown
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        var mode = new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_COLOUR, 'COLOUR' ], [ Blockly.Msg.MODE_LIGHT, 'RED' ], [ Blockly.Msg.MODE_RGB, 'RGB' ],
                [ Blockly.Msg.MODE_AMBIENTLIGHT, 'AMBIENTLIGHT' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('MODE') !== option) {
                this.sourceBlock_.updateShape_(option);
            }
        });
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.GET).appendField(mode, 'MODE').appendField(Blockly.Msg.SENSOR_COLOUR).appendField(sensorPort,
                'SENSORPORT');
        this.setOutput(true, 'Colour');
        this.setTooltip(Blockly.Msg.COLOUR_GETSAMPLE_TOOLTIP);
        this.sensorMode_ = 'COLOUR';
    },
    mutationToDom : Blockly.Blocks['robSensors_ultrasonic_getSample'].mutationToDom,
    domToMutation : Blockly.Blocks['robSensors_ultrasonic_getSample'].domToMutation,
    updateShape_ : function(option) {
        this.sensorMode_ = option;
        if (this.sensorMode_ == 'COLOUR') {
            this.changeOutput('Colour');
        } else if (this.sensorMode_ == 'RGB') {
            this.changeOutput('Array_Number');
        } else {
            this.changeOutput('Number')
        }
    }
};

Blockly.Blocks['robSensors_infrared_getSample'] = {
    /**
     * Get the current reading from the infrared sensor.
     * 
     * @constructs robSensors_infrared_getSample
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MODE - Distance or Seek
     * @param {String/dropdown}
     *            SENSORPORT - 1, 2, 3 or 4
     * @returns immediately
     * @returns {Number}
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        var mode = new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_DISTANCE, 'DISTANCE' ], [ Blockly.Msg.MODE_PRESENCE, 'SEEK' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('MODE') !== option) {
                this.sourceBlock_.updateShape_(option);
            }
        });
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.GET).appendField(mode, 'MODE').appendField(Blockly.Msg.SENSOR_INFRARED).appendField(sensorPort,
                'SENSORPORT');
        this.setOutput(true, 'Number');
        this.setTooltip(Blockly.Msg.INFRARED_GETSAMPLE_TOOLTIP);
        this.sensorMode_ = 'DISTANCE';
    },
    mutationToDom : Blockly.Blocks['robSensors_ultrasonic_getSample'].mutationToDom,
    domToMutation : Blockly.Blocks['robSensors_ultrasonic_getSample'].domToMutation,
    updateShape_ : function(option) {
        this.sensorMode_ = option;
        if (this.sensorMode_ == 'DISTANCE') {
            this.changeOutput('Number');
        } else if (this.sensorMode_ == 'SEEK') {
            this.changeOutput('Array_Number');
        }
    }
};

Blockly.Blocks['robSensors_encoder_reset'] = {
    /**
     * Reset the motor encoder.
     * 
     * @constructs robSensors_encoder_reset
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MOTORPORT - A, B, C or D
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        var motorport = new Blockly.FieldDropdown([ [ 'A', 'A' ], [ 'B', 'B' ], [ 'C', 'C' ], [ 'D', 'D' ] ]);
        // this.setInputsInline(true);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(Blockly.Msg.SENSOR_ENCODER).appendField(motorport, 'MOTORPORT').appendField(
                Blockly.Msg.SENSOR_RESET_II);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.ENCODER_RESET_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_encoder_getSample'] = {
    /**
     * Get the current reading from the motor encoder.
     * 
     * @constructs robSensors_encoder_getSample
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MODE - Rotation or Degree
     * @param {String/dropdown}
     *            MOTORPORT - A, B, C or D
     * @returns immediately
     * @returns {Number}
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        var mode = new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_ROTATION, 'ROTATION' ], [ Blockly.Msg.MODE_DEGREE, 'DEGREE' ] ]);
        var motorport = new Blockly.FieldDropdown([ [ 'A', 'A' ], [ 'B', 'B' ], [ 'C', 'C' ], [ 'D', 'D' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.GET).appendField(mode, 'MODE').appendField(Blockly.Msg.SENSOR_ENCODER).appendField(motorport,
                'MOTORPORT');
        this.setOutput(true, 'Number');
        this.setTooltip(Blockly.Msg.ENCODER_GETSAMPLE_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_gyro_reset'] = {
    /**
     * Reset the gyro sensor.
     * 
     * @constructs robSensors_gyro_reset
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            SENSORPORT - 1, 2, 3 or 4
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        // this.setInputsInline(true);
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(Blockly.Msg.SENSOR_GYRO).appendField(sensorPort, 'SENSORPORT').appendField(
                Blockly.Msg.SENSOR_RESET_II);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.GYRO_RESET_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_gyro_getSample'] = {
    /**
     * Get the current reading from the gyro sensor.
     * 
     * @constructs robSensors_gyro_getSample
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MODE - Angle or Rate
     * @param {String/dropdown}
     *            SENSORPORT - 1, 2, 3 or 4
     * @returns immediately
     * @returns {Number}
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        var mode = new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_ANGLE, 'ANGLE' ], [ Blockly.Msg.MODE_RATE, 'RATE' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.GET).appendField(mode, 'MODE').appendField(Blockly.Msg.SENSOR_GYRO).appendField(sensorPort,
                'SENSORPORT');
        this.setOutput(true, 'Number');
        this.setTooltip(Blockly.Msg.GYRO_GETSAMPLE_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_timer_reset'] = {
    /**
     * Reset the timer.
     * 
     * @constructs robSensors_timer_reset
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            TIMER - 1-10
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        // this.setInputsInline(true);
        var sensorNum = new Blockly.FieldDropdown([ [ Blockly.Msg.SENSOR_TIMER + ' 1', '1' ], [ Blockly.Msg.SENSOR_TIMER + ' 2', '2' ],
                [ Blockly.Msg.SENSOR_TIMER + ' 3', '3' ], [ Blockly.Msg.SENSOR_TIMER + ' 4', '4' ], [ Blockly.Msg.SENSOR_TIMER + ' 5', '5' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(sensorNum, 'SENSORNUM').appendField(Blockly.Msg.SENSOR_RESET_II);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.TIMER_RESET_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_timer_getSample'] = {
    /**
     * Get the current reading from the timer.
     * 
     * @constructs robSensors_timer_getSample
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            TIMER - 1-10
     * @returns immediately
     * @returns {Number}
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_SENSOR_RGB);
        var sensorNum = new Blockly.FieldDropdown([ [ Blockly.Msg.SENSOR_TIMER + ' 1 ', '1' ], [ Blockly.Msg.SENSOR_TIMER + ' 2', '2' ],
                [ Blockly.Msg.SENSOR_TIMER + ' 3', '3' ], [ Blockly.Msg.SENSOR_TIMER + ' 4', '4' ], [ Blockly.Msg.SENSOR_TIMER + ' 5', '5' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE).appendField(sensorNum, 'SENSORNUM').appendField(Blockly.Msg.SENSOR_MS_TIMER);
        this.setOutput(true, 'Number');
        this.setTooltip(Blockly.Msg.TIMER_GETSAMPLE_TOOLTIP);
    }
};
