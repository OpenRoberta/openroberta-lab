import * as Blockly from "blockly";
import * as sync from "nepo.programConfigSync";
import { Log } from "utils/nepo.logger";

Log;

export class Configuration {
	conf: object;
	robot: string;
	robotGroup: string;
	dropModes: Blockly.FieldDropdown;
	dropSlots: Array<Blockly.FieldDropdown>;

	constructor(conf: object, robot: string, robotGroup: string) {
		this.robot = robot;
		this.robotGroup = robotGroup;
		this.conf = conf;
	}

	init = function () {
		this.type = this.conf["category"].toLowerCase() + "_" + this.conf["name"].toLowerCase();
		this.typeUpper = this.type.toUpperCase();
		this.setStyle(this.conf.category + "_blocks");
		var thisConf = this;

		var validateName = function (name) {
			var block = this.sourceBlock_;
			name = name.replace(/[\s\xa0]+/g, '').replace(/^ | $/g, '');
			// no name set -> invalid
			if (name === '')
				return null;
			if (!name.match(/^[a-zA-Z][a-zA-Z_$0-9]*$/))
				return null;
			// Ensure two identically-named variables don't exist.
			name = sync.findLegalName(name, block);
			if (!block.isInsertionMarker_) {
				sync.renameConfig(this.sourceBlock_, block.nameOld, name);
			}
			block.nameOld = name;
			return name;
		};

		var msg = Blockly.Msg[this.type.toUpperCase() + "_" + this.robotGroup.toUpperCase()] || Blockly.Msg[this.type.toUpperCase()];
		var name = msg.charAt(0).toUpperCase();
		if (!this.isInsertionMarker_)
			name = sync.findLegalName(name, this);
		this.nameOld = name;
		var nameField = new Blockly.FieldTextInput(name, validateName);
		this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg[this.typeUpper + this.robotGroup.toUpperCase()]
			|| Blockly.Msg[this.typeUpper] || this.typeUpper, this.conf.category.toUpperCase()).appendField(nameField, 'NAME');

		if (this.conf.bricks) {
			var container = Blockly.Workspace.getById("bricklyDiv");
			if (container) {
				var topBlocks = Blockly.getMainWorkspace().getTopBlocks(true);
				var variableList = [];
				for (var i = 0; i < topBlocks.length; i++) {
					var block = topBlocks[i];
					if (block.type.indexOf('robBrick_') !== -1) {
						if ((block as any).getVarDecl) {
							variableList.push([(block as any).getVarDecl()[0], (block as any).getVarDecl()[0]]);
						}
					}
				}
			}
			if (variableList.length == 0) {
				variableList.push([['INVALID_NAME', 'INVALID_NAME']]);
			}
			var brickName = new Blockly.FieldDropdown(variableList);
			this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg['BRICKNAME_' + this.robotGroup.toUpperCase()]).appendField(brickName, 'VAR');
			this.getVars = function () {
				return [this.getFieldValue('VAR')];
			};
			this.renameVar = function (oldName, newName) {
				if (Blockly.Names.equals(oldName, this.getFieldValue('VAR'))) {
					this.setFieldValue(newName, 'VAR');
				}
			};
		}

        /**
         * Checking for generic block parts like text inputs or dropdowns
         */
		if (this.conf.inputs) {
			for (var i = 0; i < this.conf.inputs.length; i++) {
				var textFieldName = this.conf.inputs[i][0];
				var textField = new Blockly.FieldTextInput(this.conf.inputs[i][1]);
				this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg[this.conf.inputs[i][0]]).appendField(textField, textFieldName);
			}
		}
		if (this.conf.dropdowns) {
			for (var i = 0; i < this.conf.dropdowns.length; i++) {
				var dropDownName = Blockly.Msg[this.conf.dropdowns[i][0]];
				var fieldDropDown = new Blockly.FieldDropdown(this.conf.dropdowns[i][1]);
				this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(dropDownName).appendField(fieldDropDown, dropDownName);
			}
		}

		var portList = [];
		if (this.conf.ports) {
			for (var i = 0; i < this.conf.ports.length; i++) {
				portList.push([Blockly.Msg[this.conf.ports[i][0]] || this.conf.ports[i][0], this.conf.ports[i][1]]);
			}
			//ports = new Blockly.FieldDropdown(portList);
		} else {
			//ports = new HiddenField();
		}

		if (this.conf.pins) {
			for (var i = 0; i < portList.length; i++) {
				let pins = [];
				for (var j = 0; j < this.conf.pins.length; j++) {
					pins.push(this.conf.pins[j]);
				}
				let pinsDropdown = new Blockly.FieldDropdown(pins);
				if (this.conf.standardPins) {
					pinsDropdown.setValue(this.conf.standardPins[i]);
				}
				this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(portList[i][0]).appendField(pinsDropdown, portList[i][1]);
			}
		}

		if (this.conf.fixedPorts) {
			for (var i = 0; i < this.conf.fixedPorts.length; i++) {
				var dropDown = new Blockly.FieldDropdown([[this.conf.fixedPorts[i][1], this.conf.fixedPorts[i][1]]]);
				this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(this.conf.fixedPorts[i][0]).appendField(dropDown);
			}
		}
		var that = this;
		this.setTooltip(function () {
			return Blockly.Msg[thisConf.type + '_TOOLTIP_' + thisConf.robotGroup] || Blockly.Msg[thisConf.type + '_TOOLTIP']
				|| thisConf.type + '_TOOLTIP';
		});
		this.getConfigDecl = function () {
			return {
				'type': thisConf.type,
				'name': that.getFieldValue('NAME')
			}
		};
		this.dispose = function (healStack, animate) {
			if (!this.isInsertionMarker_ && !this.isInFlyout) {
				sync.disposeConfig(this);
			}
			Blockly.BlockSvg.prototype.dispose.call(this, !!healStack, animate);
		};
	}
}
