/**
 * @fileoverview Sensor blocks for all systems.
 * @requires Blockly.Blocks
 * @author Beate
 */

'use strict';

goog.provide('Blockly.Blocks.robConfig');

goog.require('Blockly.Blocks.robConfigDefinitions');
goog.require('Blockly.Blocks');

Blockly.Blocks['robConf_generic'] = {
    /*- Generic sensor definition. Will create e.g. the following xml:
     *
     * <block type="robSensors_ultrasonic_getSample" id="vG?X/lTw]%:p!z.},u;r" intask="true">
     *     <mutation mode="DISTANCE"></mutation>
     *     <field name="NAME"></field>
     *     <field name="TRIG">1</field>
     *     <field name="ECHO">2</field>
     * </block>
     *
     */
    /**
     * @param {Object
     *            sensor}
     * 
     * @memberof Block
     */
    init : function(confBlock) {
        this.setColour(confBlock.sensor ? Blockly.CAT_SENSOR_RGB : Blockly.CAT_ACTION_RGB);

        var validateName = function(name) {
            var block = this.sourceBlock_;
            name = name.replace(/[\s\xa0]+/g, '').replace(/^ | $/g, '');
            // no name set -> invalid
            if (name === '')
                return null;
            if (!name.match(/^[a-zA-Z][a-zA-Z_$0-9]*$/))
                return null;
            // Ensure two identically-named variables don't exist.
            name = Blockly.RobConfig.findLegalName(name, block);
            Blockly.RobConfig.renameConfig(this.sourceBlock_, block.nameOld, name, Blockly.Workspace.getByContainer("blocklyDiv"));
            block.nameOld = name;
            return name;
        };

        var type = confBlock.sensor ? 'SENSOR_' : 'ACTION_';
        var name = Blockly.RobConfig.findLegalName(Blockly.Msg[type + confBlock.title].charAt(0).toUpperCase() || Blockly.Msg[type + confBlock.title]
                || Blockly.checkMsgKey('CONFIGURATION_PORT'), this);
        this.nameOld = name;
        var nameField = new Blockly.FieldTextInput(name, validateName);
        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg[type + confBlock.title + "_" + this.workspace.device.toUpperCase()]
                || Blockly.Msg[type + confBlock.title] || type + confBlock.title, 'SENSORTITLE').appendField(nameField, 'NAME');

        if (confBlock.bricks) {
            var container = Blockly.Workspace.getByContainer("bricklyDiv");
            if (container) {
                var topBlocks = Blockly.getMainWorkspace().getTopBlocks(true);
                var variableList = [];
                for (var i = 0; i < topBlocks.length; i++) {
                    var block = topBlocks[i];
                    if (block.type.indexOf('robBrick_') !== -1) {
                        if (block.getVarDecl) {
                            variableList.push([ block.getVarDecl()[0], block.getVarDecl()[0].toUpperCase() ]);
                        }
                    }
                }
            }
            if (variableList.length == 0) {
                variableList.push([ [ 'INVALID_NAME', 'INVALID_NAME' ] ]);
            }
            var brickName = new Blockly.FieldDropdown(variableList);
            this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg['BRICKNAME_' + this.workspace.device.toUpperCase()]).appendField(brickName, 'VAR');
            this.getVars = function() {
                return [ this.getFieldValue('VAR') ];
            };
            this.renameVar = function(oldName, newName) {
                if (Blockly.Names.equals(oldName, this.getFieldValue('VAR'))) {
                    this.setFieldValue(newName, 'VAR');
                }
            };
        }
        var ports, pins;
        var portList = [];
        if (confBlock.ports) {
            for (var i = 0; i < confBlock.ports.length; i++) {
                portList.push([ Blockly.Msg[confBlock.ports[i][0]] || confBlock.ports[i][0], confBlock.ports[i][1] ]);
            }
            ports = new Blockly.FieldDropdown(portList);
        } else {
            ports = new Blockly.FieldHidden();
        }

        if (confBlock.pins) {
            for (var i = 0; i < portList.length; i++) {
                pins = new Blockly.FieldDropdown(confBlock.pins.call(this, this.workspace.subDevice));
                if (confBlock.standardPins) {
                    pins.setValue(confBlock.standardPins[i]);
                }
                this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(portList[i][0]).appendField(pins, portList[i][1]);
            }
        }

        if (confBlock.fixedPorts) {
            for (var i = 0; i < confBlock.fixedPorts.length; i++) {
                var dropDown = new Blockly.FieldDropdown([ [ confBlock.fixedPorts[i][1], confBlock.fixedPorts[i][1] ] ]);
                this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(confBlock.fixedPorts[i][0]).appendField(dropDown);
            }
        }

        this.setTooltip(function() {
            return Blockly.Msg[confBlock.title + '_TOOLTIP'] || confBlock.title + '_TOOLTIP';
        });
        this.type = 'robConf_' + confBlock.title.toLowerCase();
        var that = this;
        this.getConfigDecl = function() {
            return {
                'type' : confBlock.title.toLowerCase(),
                'name' : that.getFieldValue('NAME')
            }
        };
        this.onDispose = function() {
            Blockly.RobConfig.disposeConfig(this);
        };
    }
};
