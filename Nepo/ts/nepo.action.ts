import * as NepoExt from "nepo.constants.extensions";
import * as sync from "nepo.programConfigSync";
import { Log } from "utils/nepo.logger";
import Blockly = require("blockly");

Log;

export class Action {
	action: object;
	robot: string;
	robotGroup: string;

	constructor(action: object, robot: string, robotGroup: string) {
		this.robot = robot;
		this.robotGroup = robotGroup;
		this.action = action;
	}

	init = function () {
		this.type = "action_" + this.action["name"].toLowerCase() + "_do";
		this.setStyle("action_blocks");
		let args = [];
		let msg = "%{BKY_" + this.type.toUpperCase() + "}";
		let transMsg = Blockly.Msg[msg.substring(6, msg.length - 1)];
		let mrkCount = 0;

		// ports
		if (this.action["ports"]) {
			let ports = sync.getConfigPorts("action_" + this.action["ports"].toLowerCase());
			this.dependConfig = {
				'type': "action_" + this.action["ports"].toLowerCase(),
				'dropDown': ports
			};
			args.push({
				"type": "field_dropdown",
				"name": "PORT",
				"options": ports
			})
			transMsg = transMsg.replace(/%port/i, "%" + ++mrkCount);
		}

		// modes
		let modeArray = [];
		if (this.action["modes"]) {
			for (var i = 0; i < this.action["modes"].length; i++) {
				modeArray.push(["%{BKY_MODE_" + this.action["modes"][i].name.toUpperCase() + "}", this.action["modes"][i].name.toUpperCase()]);
			}
		}

		if (modeArray.length > 0) {
			let modeObj = {
				"type": "field_dropdown",
				"name": "MODE",
				"options": modeArray
			};
			args.push(modeObj);
			transMsg = transMsg.replace(/%mode/i, "%" + ++mrkCount);
		}

		// inputs
		if (this.action.input) {
			this.action.input.forEach(input => {
				let inp = {
					"type": "input_value",
					"align": "RIGHT",
					"name": input.name,
					"check": input.check
				}
				args.push(inp);
				transMsg = transMsg.replace(/%input/i, "%" + ++mrkCount);
			});
		}
		Blockly.Msg[this.type.toUpperCase()] = transMsg;
		this.interpolate_(msg, args || []);
		// TODO check if we could improve the HiddenField class so that it takes no space in the rendered block
		// if (!this.action["modes"]) {
		// 	this.inputList[0].insertFieldAt(0, new HiddenField(), "MODE");
		// }
		// if (!this.action["ports"]) {
		// 	this.inputList[0].insertFieldAt(0, new HiddenField(), "PORTS");
		// }
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		let dropDownPort: Blockly.FieldDropdown = this.getField("PORT");
		if (dropDownPort && this.dependConfig) {
			this.dependConfig["dropDown"] = dropDownPort;
		}

		NepoExt.COMMON_TOOLTIP_EXTENSION.apply(this);
	}
}
