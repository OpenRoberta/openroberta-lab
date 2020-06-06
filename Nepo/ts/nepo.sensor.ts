import * as Blockly from "blockly";
import { HiddenField } from "nepo.blockly.hiddenField";
import * as NepoExt from "nepo.constants.extensions";
import * as U from "utils/util";
import * as sync from "nepo.programConfigSync";
import { Log } from "utils/nepo.logger";

Log;

export class Sensor {
	sensor: object;
	robot: string;
	robotGroup: string;
	dropModes: Blockly.FieldDropdown;
	dropSlots: Array<Blockly.FieldDropdown>;

	constructor(sensor: object, robot: string, robotGroup: string) {
		this.robot = robot;
		this.robotGroup = robotGroup;
		this.sensor = sensor;
	}

	init = function () {
		this.type = "sensor_" + this.sensor["name"].toLowerCase() + '_getSample';
		this.setStyle("sensor_blocks");

		var thisBlock = this;

		// what kind of modes do we have?
		var modes;
		if (this.sensor.modes[0].name && !this.sensor.modes[0].question) {
			let modeArray = [];
			for (var i = 0; i < this.sensor.modes.length; i++) {
				modeArray.push([Blockly.Msg['MODE_' + this.sensor.modes[i].name] || U.checkMsgKey('MODE_' + this.sensor.modes[i].name), this.sensor.modes[i].name]);
			}
			modes = new Blockly.FieldDropdown(modeArray, function (option) {
				if (option && this.sourceBlock_.getFieldValue('MODE') !== option) {
					this.sourceBlock_.updateShape_(option);
				}
			});
		} else {
			modes = new HiddenField();
		}

		// do we have ports?
		var ports;
		this.sensorPort_ = 'NO';
		if (typeof this.sensor.ports === 'object') {
			var portsList = [];
			if (!this.sensor.ports[0].port) {
				for (var i = 0; i < this.sensor.ports.length; i++) {
					portsList.push([Blockly.Msg[this.sensor.ports[i][0]] || this.sensor.ports[i][0], this.sensor.ports[i][1]]);
				}
				ports = new Blockly.FieldDropdown(portsList);
			} else {
				for (var i = 0; i < this.sensor.ports.length; i++) {
					portsList.push([Blockly.Msg[this.sensor.ports[i].port[0]] || this.sensor.ports[i].port[0], this.sensor.ports[i].port[1]]);
				}
				ports = new Blockly.FieldDropdown(portsList, function (option) {
					if (option && this.sourceBlock_.getFieldValue('PORT') !== option) {
						this.sourceBlock_.updatePort_(option);
					}
				});
				this.sensorPort_ = portsList[0][1];
			}
		} else if (this.sensor.modes && this.sensor.modes[0].ports) {
			var portsList = [];
			for (var i = 0; i < this.sensor.modes[0].ports.length; i++) {
				portsList.push([Blockly.Msg[this.sensor.modes[0].ports[i][0]] || this.sensor.modes[0].ports[i][0], this.sensor.modes[0].ports[i][1]]);
			}
			ports = new Blockly.FieldDropdown(portsList);
		} else if (!this.sensor.ports) {
			var sensorName = "sensor_" + this.sensor["name"].toLowerCase();
			if (this.sensor.dependOnMode) {
				sensorName = "sensor_" + this.sensor.modes[0].name + 'OUT';
			}
			ports = sync.getConfigDropDown(sensorName.toLowerCase());
			this.dependConfig = {
				'type': sensorName.toLowerCase(),
				'dropDown': ports
			};
		} else {
			ports = new HiddenField();
		}

		// do we have a slots?
		var slots;
		if (this.sensor.slots) {
			var slotsList = [];
			for (var i = 0; i < this.sensor.slots.length; i++) {
				slotsList.push([Blockly.Msg[this.sensor.slots[i][0]] || this.sensor.slots[i][0], this.sensor.slots[i][1]]);
			}
			slots = new Blockly.FieldDropdown(slotsList);
		} else if (this.sensor.ports && this.sensor.ports[0].slots) {
			var slotsList = [];
			for (var i = 0; i < this.sensor.ports[0].slots.length; i++) {
				slotsList.push([Blockly.Msg[this.sensor.ports[0].slots[i][0]] || this.sensor.ports[0].slots[i][0], this.sensor.ports[0].slots[i][1]]);
			}
			slots = new Blockly.FieldDropdown(slotsList);
		} else {
			slots = new HiddenField();
		}

		var firstMode = this.sensor.modes[0];
		// question or not?
		if (firstMode.question) {
			this.appendDummyInput('ROW').appendField(Blockly.Msg['SENSOR_' + this.sensor.name + '_' + this.robot.toUpperCase()]
				|| Blockly.Msg['SENSOR_' + this.sensor.name] || U.checkMsgKey('SENSOR_' + this.sensor.name), 'SENSORTITLE').appendField(modes, 'MODE').appendField(ports, 'PORT').appendField(slots, 'SLOT').appendField(Blockly.Msg['SENSOR_IS_'
					+ firstMode.name]
					|| firstMode.name);
		} else {
			this.appendDummyInput('ROW').appendField(Blockly.Msg["GET"]).appendField(modes, 'MODE').appendField(Blockly.Msg['SENSOR_UNIT_' + firstMode.unit]
				|| U.checkMsgKey(firstMode.unit), 'UNIT').appendField(Blockly.Msg['SENSOR_' + this.sensor.name + '_'
					+ this.robot.toUpperCase()]
					|| Blockly.Msg['SENSOR_' + this.sensor.name] || U.checkMsgKey('SENSOR_' + this.sensor.name), 'SENSORTITLE').appendField(ports, 'PORT').appendField(slots, 'SLOT');
		}
		if (this.sensor.standardPort) {
			ports.setValue(this.sensor.standardPort);
		}
		this.sensorMode_ = firstMode.name;
		this.setOutput(true, firstMode.type);

		this.setTooltip(function () {
			var mode = thisBlock.getFieldValue('MODE');
			return Blockly.Msg['SENSOR_' + thisBlock.sensor.name + '_' + mode + '_GETSAMPLE_TOOLTIP_' + thisBlock.robot.toUpperCase()]
				|| Blockly.Msg['SENSOR_' + thisBlock.sensor.name + '_' + mode + '_GETSAMPLE_TOOLTIP']
				|| Blockly.Msg['SENSOR_' + thisBlock.sensor.name + '_GETSAMPLE_TOOLTIP'] || U.checkMsgKey('SENSOR_' + thisBlock.sensor.name + '_GETSAMPLE_TOOLTIP');
		});

		if (this.sensorMode_) {
			this.mutationToDom = function () {
				var container = document.createElement('mutation');
				container.setAttribute('mode', this.sensorMode_);
				if (this.sensorPort_ !== 'NO') {
					container.setAttribute('port', this.sensorPort_);
				}
				return container;
			};
			this.domToMutation = function (xmlElement) {
				this.sensorMode_ = xmlElement.getAttribute('mode');
				this.updateShape_(this.sensorMode_);
				this.sensorPort_ = xmlElement.getAttribute('port') || 'NO';
				if (this.sensorPort_ !== 'NO') {
					this.updatePort_(this.sensorPort_);
				}
			};
			this.updateShape_ = function (option) {
				for (var i = 0; i < this.sensor.modes.length; i++) {
					if (this.sensor.modes[i].name === option) {
						this.setOutput(true, this.sensor.modes[i].type);
						var unit = this.getField('UNIT');
						if (unit) {
							unit.setValue(Blockly.Msg['SENSOR_UNIT_' + this.sensor.modes[i].unit] || U.checkMsgKey(this.sensor.modes[i].unit));
						}
						// TODO: split these blocks into two, ANALOG and DIGITAL to completely get rid of this
						// TODO: remember this special case disappeared with new configuration for calliope					
						// this is a really special case for calliope so far
						/*if (this.sensor.modes[i].ports) {
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
							for (var j = 0; j < this.sensor.modes[i].ports.length; j++) {
								portsList.push([Blockly.Msg[this.sensor.modes[i].ports[j][0]] || this.sensor.modes[i].ports[j][0], this.sensor.modes[i].ports[j][1]]);
							}
							input.appendField(new Blockly.FieldDropdown(portsList), 'PORT').appendField(new HiddenField(), 'SLOT');
						}*/

						// TODO: split these blocks into two, ANALOG and DIGITAL to completely get rid of this					
						// this is a special case for arduino and calliope
						if (this.sensor.dependOnMode) {
							if (option === 'PULSEHIGH' || option === 'PULSELOW') {
								option = 'DIGITAL'; // workaround for calliope pulses
							}
							var configBlockName = option.toLowerCase() + 'out';
							var dropDownPorts = sync.getConfigDropDown(configBlockName);
							var fieldSensorPort = thisBlock.getField('PORT');
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
			this.updatePort_ = function (option) {
				for (var i = 0; i < this.sensor.ports.length; i++) {
					if (this.sensor.ports[i].port[1] === option) {
						let fieldSensorSlot = thisBlock.getField('SLOT');
						// add new slots
						let slotList = [];
						for (var j = 0; j < this.sensor.ports[i].slots.length; j++) {
							slotList.push([Blockly.Msg[this.sensor.ports[i].slots[j][0]] || this.sensor.ports[i].slots[j][0], this.sensor.ports[i].slots[j][1]]);
						}
						let dropDownSlots = new Blockly.FieldDropdown(slotList);
						fieldSensorSlot.menuGenerator_ = dropDownSlots.menuGenerator_;
						fieldSensorSlot.setValue(fieldSensorSlot.menuGenerator_[0][0]);
					}
				}
				this.sensorPort_ = option;
			}
			NepoExt.COMMON_TOOLTIP_EXTENSION.apply(this);
		}
	}
}
