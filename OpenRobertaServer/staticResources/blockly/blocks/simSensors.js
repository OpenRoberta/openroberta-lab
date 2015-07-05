/**
 * @fileoverview Action blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */
'use strict';

goog.provide('Blockly.Blocks.simSensors');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['sim_getSample'] = {
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
                [ Blockly.Msg.MODE_COLOUR + ' ' + Blockly.Msg.SENSOR_COLOUR, 'COLOUR_COLOUR' ],
                [ Blockly.Msg.MODE_LIGHT + ' ' + Blockly.Msg.SENSOR_COLOUR, 'COLOUR_LIGHT' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('SENSORTYPE') !== option) {
                this.sourceBlock_.updateShape_(option);
            }
        });
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        this.appendDummyInput('DROPDOWN').appendField(Blockly.Msg.GET, 'GET').appendField(sensorType, 'SENSORTYPE').appendField(sensorPort, 'SENSORPORT');
        this.sensorType_ = 'TOUCH';
        this.setOutput(true, 'Boolean');
        this.setTooltip(Blockly.Msg.GETSAMPLE_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.SENSOR_GET_SAMPLE_HELP));
        this.setMovable(false);
        this.setDeletable(false);
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
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
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
        
            input.appendField(sensorPort, 'SENSORPORT');
            if (this.sensorType_ == 'TOUCH') {
                this.appendValue_('BOOL');
                this.changeOutput('Boolean');
            } else if (this.sensorType_ == 'ULTRASONIC_DISTANCE') {
                this.appendValue_('NUM');
                sensorPort.setValue('4');
                this.changeOutput('Number');
            } else if (this.sensorType_ == 'COLOUR_COLOUR') {
                this.appendValue_('COLOUR');
                sensorPort.setValue('3');
                this.changeOutput('Colour');
            } else if (this.sensorType_ == 'COLOUR_LIGHT') {
                this.appendValue_('NUM');
                sensorPort.setValue('3');
                this.changeOutput('Number');
            } 
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

Blockly.Blocks['sim_ultrasonic_getSample'] = {
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
            var mode = new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_DISTANCE, 'DISTANCE' ]], function(option) {
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

    Blockly.Blocks['sim_colour_getSample'] = {
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
            var mode = new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_COLOUR, 'COLOUR' ], [ Blockly.Msg.MODE_LIGHT, 'RED' ] ], function(option) {
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
        mutationToDom : Blockly.Blocks['sim_ultrasonic_getSample'].mutationToDom,
        domToMutation : Blockly.Blocks['sim_ultrasonic_getSample'].domToMutation,
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

