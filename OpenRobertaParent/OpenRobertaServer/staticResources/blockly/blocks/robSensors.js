/**
 * @fileoverview Sensor blocks for all systems.
 * @requires Blockly.Blocks
 * @author Beate
 */

'use strict';

goog.provide('Blockly.Blocks.robSensors');

goog.require('Blockly.Blocks.robSensorDefinitions');
goog.require('Blockly.Blocks');

// define non standard sensor blocks here e.g.**********************************************************

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
        this.setColour(Blockly.CAT_SENSOR_RGB);
        var motorport = new Blockly.FieldDropdown([ [ 'A', 'A' ], [ 'B', 'B' ], [ 'C', 'C' ], [ 'D', 'D' ] ]);
        if (this.workspace.device === 'botnroll') {
            motorport = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_LEFT, 'B' ], [ Blockly.Msg.MOTOR_RIGHT, 'C' ] ]);
        }
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(Blockly.Msg.SENSOR_ENCODER).appendField(motorport, 'SENSORPORT').appendField(Blockly.Msg.SENSOR_RESET_II);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.ENCODER_RESET_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_gyro_reset'] = {
    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        // this.setInputsInline(true);
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(Blockly.Msg.SENSOR_GYRO).appendField(sensorPort, 'SENSORPORT').appendField(Blockly.Msg.SENSOR_RESET_II);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.GYRO_RESET_TOOLTIP);
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
        this.setColour(Blockly.CAT_SENSOR_RGB);
        var sensorNum;
        if (this.workspace.device === 'nxt' || this.workspace.device === 'botnroll' || this.workspace.device === 'bob3') 
{
            sensorNum = new Blockly.FieldDropdown([ [ Blockly.Msg.SENSOR_TIMER + ' 1', '1' ] ]);
        } else {
            sensorNum = new Blockly.FieldDropdown([ [ Blockly.Msg.SENSOR_TIMER + ' 1', '1' ], [ Blockly.Msg.SENSOR_TIMER + ' 2', '2' ],
                    [ Blockly.Msg.SENSOR_TIMER + ' 3', '3' ], [ Blockly.Msg.SENSOR_TIMER + ' 4', '4' ], [ Blockly.Msg.SENSOR_TIMER + ' 5', '5' ] ]);
        }
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(sensorNum, 'SENSORPORT').appendField(Blockly.Msg.SENSOR_RESET_II);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.TIMER_RESET_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_compass_calibrate'] = {
    /**
     * Calibrate the compass.
     * 
     * @constructs robSensors_compass_calibrate
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MOTORPORT - 1, 2, 3 or 4
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_CALIBRATE).appendField(Blockly.Msg.SENSOR_COMPASS_EV3).appendField(sensorPort, 'SENSORPORT');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.COMPASS_CALIBRATE_TOOLTIP);
    }
};

Blockly.Blocks['mbedSensors_timer_reset'] = {
    /**
     * Reset the timer.
     * 
     * @constructs mbedSensors_timer_reset
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            TIMER - 1-10
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        var sensorNum;
        sensorNum = new Blockly.FieldDropdown([ [ Blockly.Msg.SENSOR_TIMER + ' 1', '1' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(sensorNum, 'SENSORPORT').appendField(Blockly.Msg.SENSOR_RESET_II);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.TIMER_RESET_TOOLTIP);
    }
};

Blockly.Blocks['robSensors_generic'] = {
    /*- Generic sensor definition. Will create e.g. the following xml:
     *
     * <block type="robSensors_ultrasonic_getSample" id="vG?X/lTw]%:p!z.},u;r" intask="false">
     *     <mutation mode="DISTANCE"></mutation>
     *     <field name="MODE">DISTANCE</field>
     *     <field name="SENSORPORT">4</field>
     *     <field name="slots"></field>
     * </block>
     *
     */
    /**
     * @param {Object
     *            sensor}
     * 
     * @memberof Block
     */
    init : function(sensor) {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        var thisBlock = this;
        // do what kind of modes do we have?
        var modes;
        if (sensor.modes[0].name && !sensor.modes[0].question) {
            var modes = [];
            for (var i = 0; i < sensor.modes.length; i++) {
                modes.push([ Blockly.Msg['MODE_' + sensor.modes[i].name] || Blockly.checkMsgKey('MODE_' + sensor.modes[i].name), sensor.modes[i].name ]);
            }
            modes = new Blockly.FieldDropdown(modes, function(option) {
                if (option && this.sourceBlock_.getFieldValue('MODE') !== option) {
                    this.sourceBlock_.updateShape_(option);
                }
            });
        } else {
            modes = new Blockly.FieldHidden(sensor.modes[0].name);
        }
        // do we have ports?

        var ports;
        this.sensorPort_ = 'NO';
        if (typeof sensor.ports === 'object') {
            var portsList = [];
            if (!sensor.ports[0].port) {
                for (var i = 0; i < sensor.ports.length; i++) {
                    portsList.push([ Blockly.Msg[sensor.ports[i][0]] || sensor.ports[i][0], sensor.ports[i][1] ]);
                }
                ports = new Blockly.FieldDropdown(portsList);
            } else {
                for (var i = 0; i < sensor.ports.length; i++) {
                    portsList.push([ Blockly.Msg[sensor.ports[i].port[0]] || sensor.ports[i].port[0], sensor.ports[i].port[1] ]);
                }
                ports = new Blockly.FieldDropdown(portsList, function(option) {
                    if (option && this.sourceBlock_.getFieldValue('SENSORPORT') !== option) {
                        this.sourceBlock_.updatePort_(option);
                    }
                });
                this.sensorPort_ = portsList[0][1];
            }
        } else if (sensor.modes && sensor.modes[0].ports) {
            var portsList = [];
            for (var i = 0; i < sensor.modes[0].ports.length; i++) {
                portsList.push([ Blockly.Msg[sensor.modes[0].ports[i][0]] || sensor.modes[0].ports[i][0], sensor.modes[0].ports[i][1] ]);
            }
            ports = new Blockly.FieldDropdown(portsList);
        } else if (sensor.ports === 'CONFIGURATION') {
            var sensorTitle = sensor.title;
            if (sensorTitle === 'OUT') {
                sensorTitle = sensor.modes[0].name + sensorTitle;
            }
            ports = getConfigPorts(sensorTitle.toLowerCase());
            this.dependConfig = {
                'type' : this.sensor,
                'dropDown' : ports
            };
        } else {
            ports = new Blockly.FieldHidden();
        }
        // do we have a slots?
        var slots;
        if (sensor.slots) {
            var slotsList = [];
            for (var i = 0; i < sensor.slots.length; i++) {
                slotsList.push([ Blockly.Msg[sensor.slots[i][0]] || sensor.slots[i][0], sensor.slots[i][1] ]);
            }
            slots = new Blockly.FieldDropdown(slotsList);
        } else if (sensor.ports && sensor.ports[0].slots) {
            var slotsList = [];
            for (var i = 0; i < sensor.ports[0].slots.length; i++) {
                slotsList.push([ Blockly.Msg[sensor.ports[0].slots[i][0]] || sensor.ports[0].slots[i][0], sensor.ports[0].slots[i][1] ]);
            }
            slots = new Blockly.FieldDropdown(slotsList);
        } else {
            slots = new Blockly.FieldHidden();
        }

        var firstMode = sensor.modes[0];
        // question or not?
        if (firstMode.question) {
            this.appendDummyInput('ROW').appendField(Blockly.Msg['SENSOR_' + sensor.title + '_' + this.workspace.device.toUpperCase()]
                    || Blockly.Msg['SENSOR_' + sensor.title] || Blockly.checkMsgKey('SENSOR_' + sensor.title), 'SENSORTITLE').appendField(modes, 'MODE').appendField(ports, 'SENSORPORT').appendField(slots, 'SLOT').appendField(Blockly.Msg['SENSOR_IS_'
                    + firstMode.name]
                    || firstMode.name);
        } else {
            this.appendDummyInput('ROW').appendField(Blockly.Msg.GET).appendField(modes, 'MODE').appendField(Blockly.Msg['SENSOR_UNIT_' + firstMode.unit]
                    || Blockly.checkMsgKey(firstMode.unit), 'UNIT').appendField(Blockly.Msg['SENSOR_' + sensor.title + '_'
                    + this.workspace.device.toUpperCase()]
                    || Blockly.Msg['SENSOR_' + sensor.title] || Blockly.checkMsgKey('SENSOR_' + sensor.title), 'SENSORTITLE').appendField(ports, 'SENSORPORT').appendField(slots, 'SLOT');
        }
        if (sensor.standardPort) {
            ports.setValue(sensor.standardPort);
        }
        this.sensorMode_ = firstMode.name;
        this.setOutput(true, firstMode.type);

        this.setTooltip(function() {
            var mode = thisBlock.getFieldValue('MODE');
            return Blockly.Msg['SENSOR_' + sensor.title + '_' + mode + '_GETSAMPLE_TOOLTIP_' + thisBlock.workspace.device.toUpperCase()]
                    || Blockly.Msg['SENSOR_' + sensor.title + '_' + mode + '_GETSAMPLE_TOOLTIP']
                    || Blockly.Msg['SENSOR_' + sensor.title + '_GETSAMPLE_TOOLTIP'] || Blockly.checkMsgKey('SENSOR_' + sensor.title + '_GETSAMPLE_TOOLTIP');
        });
        this.type = 'robSensors_' + this.sensor + '_getSample';

        if (this.sensorMode_) {
            this.mutationToDom = function() {
                var container = document.createElement('mutation');
                container.setAttribute('mode', this.sensorMode_);
                if (this.sensorPort_ !== 'NO') {
                    container.setAttribute('port', this.sensorPort_);
                }
                return container;
            };
            this.domToMutation = function(xmlElement) {
                this.sensorMode_ = xmlElement.getAttribute('mode');
                this.updateShape_(this.sensorMode_);
                this.sensorPort_ = xmlElement.getAttribute('port') || 'NO';
                if (this.sensorPort_ !== 'NO') {
                    this.updatePort_(this.sensorPort_);
                }
            };
            this.updateShape_ = function(option) {
                for (var i = 0; i < sensor.modes.length; i++) {
                    if (sensor.modes[i].name === option) {
                        this.setOutput(true, sensor.modes[i].type);
                        var unit = this.getField('UNIT');
                        if (unit) {
                            unit.setText(Blockly.Msg['SENSOR_UNIT_' + sensor.modes[i].unit] || Blockly.checkMsgKey(sensor.modes[i].unit));
                        }
                        // this is a really special case for calliope so far
                        if (sensor.modes[i].ports) {
                            var input = this.getInput('ROW');
                            var toRemove = [];
                            for (var j = input.fieldRow.length - 1; j > 0; j--) {
                                if (input.fieldRow[j].name === 'SENSORTITLE') {
                                    break;
                                }
                                toRemove.push(input.fieldRow[j].name);
                            }
                            for (var j = 0; j < toRemove.length; j++) {
                                input.removeField(toRemove[j]);
                            }
                            // add new ports
                            var portsList = [];
                            for (var j = 0; j < sensor.modes[i].ports.length; j++) {
                                portsList.push([ Blockly.Msg[sensor.modes[i].ports[j][0]] || sensor.modes[i].ports[j][0], sensor.modes[i].ports[j][1] ]);
                            }
                            input.appendField(new Blockly.FieldDropdown(portsList), 'SENSORPORT').appendField(new Blockly.FieldHidden(), 'SLOT');
                        }
                        // this is a special case for arduino 
                        if (sensor.title === 'OUT') {
                            var configBlockName = option.toLowerCase() + 'out';
                            var dropDownPorts = getConfigPorts(configBlockName);
                            var fieldSensorPort = thisBlock.getField('SENSORPORT');
                            thisBlock.dependConfig.type = configBlockName;
                            fieldSensorPort.menuGenerator_ = dropDownPorts.menuGenerator_;
                            fieldSensorPort.setValue(fieldSensorPort.menuGenerator_[0][0]);
                            fieldSensorPort.arrow_ && fieldSensorPort.arrow_.replaceChild(document.createTextNode(' '), fieldSensorPort.arrow_.childNodes[0]);
                            if (fieldSensorPort.menuGenerator_.length > 1) {
                                fieldSensorPort.arrow_
                                        && fieldSensorPort.arrow_.replaceChild(document.createTextNode(' ' + Blockly.FieldDropdown.ARROW_CHAR), fieldSensorPort.arrow_.childNodes[0]);
                            }
                        }
                    }
                }
                this.sensorMode_ = option;
            }
            // this is a really special case for nao so far
            this.updatePort_ = function(option) {
                for (var i = 0; i < sensor.ports.length; i++) {
                    if (sensor.ports[i].port[1] === option) {
                        var input = this.getInput('ROW');
                        var toRemove = [];
                        for (var j = input.fieldRow.length - 1; j > 0; j--) {
                            if (input.fieldRow[j].name === 'SENSORPORT') {
                                break;
                            }
                            toRemove.push(input.fieldRow[j].name);
                        }
                        for (var j = 0; j < toRemove.length; j++) {
                            input.removeField(toRemove[j]);
                        }
                        // add new slots
                        var slotList = [];
                        for (var j = 0; j < sensor.ports[i].slots.length; j++) {
                            slotList.push([ Blockly.Msg[sensor.ports[i].slots[j][0]] || sensor.ports[i].slots[j][0], sensor.ports[i].slots[j][1] ]);
                        }
                        if (sensor.modes[0].question) {
                            input.appendField(new Blockly.FieldDropdown(slotList), 'SLOT').appendField(Blockly.Msg['SENSOR_IS_' + sensor.modes[0].name]
                                    || sensor.modes[0].name);
                        } else {
                            input.appendField(new Blockly.FieldDropdown(slotList), 'SLOT');
                        }
                        this.render();
                    }
                }
                this.sensorPort_ = option;
            }
        }
    }
};

Blockly.Blocks['robSensors_generic_all'] = {
    /*- Generic sensor definition. Will create the following xml:
     *
     * <block type="robSensors_getSample" id=",eb_si_guT_Xi24OesW" intask="false">
     *     <mutation input="COLOUR_COLOUR"></mutation>
     *     <fieldname="SENSORTYPE">COLOUR_COLOUR</field>
     *     <field name="SENSORPORT">3</field>
     *     <field name="slots"></field>
     * </block>
     *
     */

    /**
     * 
     * @param {Object
     *            sensors} /* /*
     * @memberof Block /
     */
    init : function(sensors) {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.sensors = [];
        this.ports = [];
        this.mutPorts = [];
        this.slots = [];

        var modeSensor = [];
        for (var i = 0; i < sensors.length; i++) {
            for (var j = 0; j < sensors[i].modes.length; j++) {
                // we can not provide sensors in this block with array output
                if (sensors[i].modes[j].type.indexOf('Array') > -1) {
                    continue;
                }
                modeSensor.push([
                        (Blockly.Msg['MODE_' + sensors[i].modes[j].name] || Blockly.checkMsgKey(sensors[i].modes[j].name))
                                + ' '
                                + (Blockly.Msg['SENSOR_UNIT_' + sensors[i].modes[j].unit] || Blockly.checkMsgKey(sensors[i].modes[j].unit))
                                + ' '
                                + (Blockly.Msg['SENSOR_' + sensors[i].title + '_' + this.workspace.device.toUpperCase()]
                                        || Blockly.Msg['SENSOR_' + sensors[i].title] || Blockly.checkMsgKey(sensors[i].title)),
                        sensors[i].title + '_' + sensors[i].modes[j].name ]);
                if (sensors[i].ports) {
                    var portsList = [];
                    if (sensors[i].ports === 'CONFIGURATION') {
                        var container = Blockly.Workspace.getByContainer("bricklyDiv");
                        if (container) {
                            var sensorTitle = sensors[i].title;
                            if (sensorTitle === 'OUT') {
                                sensorTitle = sensors[i].modes[0].name + sensorTitle;
                            }
                            var blocks = Blockly.Workspace.getByContainer("bricklyDiv").getAllBlocks();
                            for (var x = 0; x < blocks.length; x++) {
                                var func = blocks[x].getConfigDecl;
                                if (func) {
                                    var config = func.call();
                                    if (config.type.toUpperCase() === sensorTitle) {
                                        portsList.push([ config.name, config.name.toUpperCase() ]);
                                    }
                                }
                            }
                        }
                        if (portsList.length === 0) {
                            portsList.push([ Blockly.Msg.CONFIGURATION_NO_PORT || Blockly.checkMsgKey('CONFIGURATION_NO_PORT'),
                                    (Blockly.Msg.CONFIGURATION_NO_PORT || Blockly.checkMsgKey('CONFIGURATION_NO_PORT')).toUpperCase() ]);
                        }
                        this.ports.push(portsList);
                        this.mutPorts.push('NO');
                    } else if (!sensors[i].ports[0].port) {
                        for (var k = 0; k < sensors[i].ports.length; k++) {
                            portsList.push([ Blockly.Msg[sensors[i].ports[k][0]] || sensors[i].ports[k][0], sensors[i].ports[k][1] ]);
                        }
                        this.ports.push(portsList);
                        this.mutPorts.push('NO');
                    } else {
                        for (var k = 0; k < sensors[i].ports.length; k++) {
                            portsList.push([ Blockly.Msg[sensors[i].ports[k].port[0]] || sensors[i].ports[k].port[0], sensors[i].ports[k].port[1] ]);
                        }
                        this.ports.push(portsList);
                        this.mutPorts.push(portsList[0][1]);
                    }
                } else if (sensors[i].modes && sensors[i].modes[j].ports) {
                    var portsList = [];
                    for (var l = 0; l < sensors[i].modes[j].ports.length; l++) {
                        portsList.push([ Blockly.Msg[sensors[i].modes[j].ports[l][0]] || sensors[i].modes[j].ports[l][0], sensors[i].modes[j].ports[l][1] ]);
                    }
                    this.ports.push(portsList);
                    this.mutPorts.push('NO');
                } else {
                    this.ports.push([]);
                    this.mutPorts.push('NO');
                }

                if (sensors[i].slots) {
                    var portsList = [];
                    for (var l = 0; l < sensors[i].slots.length; l++) {
                        portsList.push([ Blockly.Msg[sensors[i].slots[l][0]] || sensors[i].slots[l][0], sensors[i].slots[l][1] ]);
                    }
                    this.slots.push(new Blockly.FieldDropdown(portsList));
                } else if (sensors[i].ports && sensors[i].ports[0].slots) {
                    var slotsList = [];
                    for (var m = 0; m < sensors[i].ports.length; m++) {
                        var portSlotsList = [];
                        for (var l = 0; l < sensors[i].ports[m].slots.length; l++) {
                            portSlotsList.push([ Blockly.Msg[sensors[i].ports[m].slots[l][0]] || sensors[i].ports[m].slots[l][0],
                                    sensors[i].ports[m].slots[l][1] ]);
                        }
                        slotsList.push(new Blockly.FieldDropdown(portSlotsList));
                    }
                    this.slots.push(slotsList);
                } else {
                    this.slots.push(new Blockly.FieldHidden());
                }
                this.sensors.push({
                    name : sensors[i].title,
                    mode : sensors[i].modes[j].name,
                    type : sensors[i].modes[j].type,
                    standardPort : sensors[i].standardPort,
                    unit : sensors[i].modes[j].unit,
                    op : sensors[i].modes[j].op,
                    value : sensors[i].modes[j].value
                });
            }
        }
        var dropdownModes = new Blockly.FieldDropdown(modeSensor, function(option) {
            if (option && this.sourceBlock_.getFieldValue('SENSORTYPE') !== option) {
                this.sourceBlock_.updateShape_(option);
            }
        });
        if (this.mutPorts[0] == 'NO') {
            if (this.ports[0].length <= 0) {
                this.dropDownPorts = new Blockly.FieldHidden();
            } else {
                this.dropDownPorts = new Blockly.FieldDropdown(this.ports[0]);
            }
        } else {
            this.dropDownPorts = new Blockly.FieldDropdown(this.ports[0], function(option) {
                if (option && this.sourceBlock_.getFieldValue('SENSORPORT') !== option) {
                    this.sourceBlock_.updatePort_(option);
                }
            });
        }
        var slots = this.slots[0];
        if (Array.isArray(this.slots[0])) {
            slots = this.slots[0][0];
        }
        this.appendDummyInput('ROW').appendField(Blockly.Msg.GET, 'GET').appendField(dropdownModes, 'SENSORTYPE').appendField(this.dropDownPorts, 'SENSORPORT').appendField(slots, 'SLOT');

        this.setOutput(true, sensors[0].modes[0].type);
        var thisBlock = this;

        this.setTooltip(function() {
            var mode = thisBlock.getFieldValue('SENSORTYPE');
            return Blockly.Msg['SENSOR_' + mode + '_GETSAMPLE_TOOLTIP_' + thisBlock.workspace.device.toUpperCase()]
                    || Blockly.Msg['SENSOR_' + mode + '_GETSAMPLE_TOOLTIP']
                    || Blockly.Msg['SENSOR_' + mode.substr(0, mode.indexOf('_')) + '_GETSAMPLE_TOOLTIP']
                    || Blockly.checkMsgKey('SENSOR_' + mode.substr(0, mode.indexOf('_')) + '_GETSAMPLE_TOOLTIP');
        });
        this.type = 'robSensors_getSample';
        this.sensorType_ = modeSensor[0][1];
        this.sensorPort_ = this.mutPorts[0];
        this.mutationToDom = function() {
            var container = document.createElement('mutation');
            container.setAttribute('input', this.sensorType_);
            if (this.sensorPort_ !== 'NO') {
                container.setAttribute('port', this.sensorPort_);
            }
            return container;
        };
        this.domToMutation = function(xmlElement) {
            this.sensorType_ = xmlElement.getAttribute('input');
            this.updateShape_(this.sensorType_);
            this.sensorPort_ = xmlElement.getAttribute('port') || 'NO';
            if (this.sensorPort_ !== 'NO') {
                this.updatePort_(this.sensorPort_);
            }
        };
        this.updatePort_ = function(option) {
            var input = this.getInput('ROW');
            var toRemove = [];
            for (var j = input.fieldRow.length - 1; j > 0; j--) {
                if (input.fieldRow[j].name === 'SENSORPORT') {
                    break;
                }
                toRemove.push(input.fieldRow[j].name);
            }
            for (var j = 0; j < toRemove.length; j++) {
                input.removeField(toRemove[j]);
            }
            // define in which sensor / mode we are => index
            var sensorType = this.getField('SENSORTYPE');
            var sensorTypeOptions = sensorType.getOptions_();
            var index = 0;
            for (var i = 0; i < sensorTypeOptions.length; i++) {
                if (sensorTypeOptions[i][1] == this.sensorType_) {
                    index = i;
                    break;
                }
            }
            // add new slots
            var portType = this.getField('SENSORPORT');
            var portTypeOptions = portType.getOptions_();
            var portI = 0;
            for (var i = 0; i < portTypeOptions.length; i++) {
                if (portTypeOptions[i][1] == option) {
                    portI = i;
                    break;
                }
            }
            input.appendField(this.slots[index][portI], 'SLOT');
            this.render();
            this.sensorPort_ = option;
        };
        this.updateShape_ = function(option) {
            this.sensorType_ = option;
            // remove all dynamic fields
            var input = this.getInput('ROW');
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
            // define in which sensor / mode we are => index
            var sensor_mode = this.sensorType_.split("_");
            var sensor = sensor_mode[0];
            var mode = sensor_mode[1];

            var index = 0;
            for (var i = 0; i < this.sensors.length; i++) {
                if (this.sensors[i].name == sensor && this.sensors[i].mode == mode) {
                    index = i;
                    break;
                }
            }
            // add ports again
            if (this.mutPorts[index] == 'NO') {
                if (this.ports[index].ports === 'CONFIGURATION' || (sensors[index] && sensors[index].ports === 'CONFIGURATION')) {
                    var portsList = [];
                    var sensorTitle = this.sensorType_.split("_")[0];
                    // special case for get sample analog/digital out
                    if (sensorTitle === 'OUT') {
                        sensorTitle = this.sensorType_.split("_")[1] + sensorTitle;
                    }
                    var container = Blockly.Workspace.getByContainer("bricklyDiv");
                    if (container) {
                        var blocks = Blockly.Workspace.getByContainer("bricklyDiv").getAllBlocks();
                        for (var x = 0; x < blocks.length; x++) {
                            var func = blocks[x].getConfigDecl;
                            if (func) {
                                var config = func.call();
                                if (config.type.toUpperCase() === sensorTitle) {
                                    portsList.push([ config.name, config.name.toUpperCase() ]);
                                }
                            }
                        }
                    }
                    if (portsList.length === 0) {
                        portsList.push([ Blockly.Msg.CONFIGURATION_NO_PORT || Blockly.checkMsgKey('CONFIGURATION_NO_PORT'),
                                (Blockly.Msg.CONFIGURATION_NO_PORT || Blockly.checkMsgKey('CONFIGURATION_NO_PORT')).toUpperCase() ]);
                    }
                    this.ports[index] = portsList;
                    this.dropDownPorts = new Blockly.FieldDropdown(this.ports[index]);
                    this.dependConfig = function() {
                        return {
                            'type' : sensorTitle.toLowerCase(),
                            'dropDown' : this.dropDownPorts
                        };
                    }
                } else {
                    delete this.dependConfig;
                }
                if (this.ports[index].length <= 0) {
                    this.dropDownPorts = new Blockly.FieldHidden();
                } else {
                    this.dropDownPorts = new Blockly.FieldDropdown(this.ports[index]);
                }
            } else {
                this.dropDownPorts = new Blockly.FieldDropdown(this.ports[index], function(option) {
                    if (option && this.sourceBlock_.getFieldValue('SENSOPORT') !== option) {
                        this.sourceBlock_.updatePort_(option);
                    }
                });
            }
            input.appendField(this.dropDownPorts, 'SENSORPORT');
            if (!this.slots[index].length) {
                input.appendField(this.slots[index], 'SLOT');
            } else {
                input.appendField(this.slots[index][0], 'SLOT');
            }

            if (this.sensors[index].standardPort) {
                this.dropDownPorts.setValue(this.sensors[index].standardPort);
            }
            this.sensorPort_ = this.mutPorts[index];

            // set output
            this.setOutput(true, this.sensors[index].type);
            // update the surrounding logic_compare block
            var value = this.sensors[index].value || 30;
            var logComp = this.getParent();
            if (logComp && logComp.type != 'logic_compare') {
                logComp = null;
            }
            if (logComp) {
                // change inputs, if block is in logic_compare and not rebuild from mutation.
                if (logComp.getInputTargetBlock('B')) {
                    logComp.getInputTargetBlock('B').dispose();
                }
                var block = null;
                if (this.sensors[index].type == 'Number') {
                    logComp.updateShape(this.sensors[index].op || 'NUM');
                    block = this.workspace.newBlock('math_number');
                    block.setFieldValue(value.toString(), 'NUM');
                } else if (this.sensors[index].type == 'Boolean') {
                    logComp.updateShape('BOOL');
                    block = this.workspace.newBlock('logic_boolean');
                } else if (this.sensors[index].type == 'String') {
                    logComp.updateShape('TEXT');
                    block = this.workspace.newBlock('text');
                    block.setFieldValue(value.toString(), 'TEXT');
                } else if (this.sensors[index].type == 'Colour') {
                    logComp.updateShape('COLOUR');
                    block = this.workspace.newBlock('robColour_picker');
                    block.setFieldValue(this.sensors[index].value, 'COLOUR')
                } else {
                    logComp.updateShape('BOOL');
                    block = this.workspace.newBlock('logic_boolean');
                }
                block.initSvg();
                block.render();
                if (!logComp.inTask) {
                    block.setInTask(false);
                }
                var valueB = logComp.getInput('B');
                valueB.connection.connect(block.outputConnection);
            }
        };
    }
};

function getConfigPorts(actorName) {
    var ports = [];
    var container = Blockly.Workspace.getByContainer("bricklyDiv");
    if (container) {
        var blocks = Blockly.Workspace.getByContainer("bricklyDiv").getAllBlocks();
        for (var x = 0; x < blocks.length; x++) {
            var func = blocks[x].getConfigDecl;
            if (func) {
                var config = func.call(blocks[x]);
                if (config.type === actorName) {
                    ports.push([ config.name, config.name.toUpperCase() ]);
                }
            }
        }
    }

    if (ports.length === 0) {
        ports.push([ Blockly.Msg.CONFIGURATION_NO_PORT || Blockly.checkMsgKey('CONFIGURATION_NO_PORT'),
                (Blockly.Msg.CONFIGURATION_NO_PORT || Blockly.checkMsgKey('CONFIGURATION_NO_PORT')).toUpperCase() ]);
    }
    return new Blockly.FieldDropdown(ports);
};
