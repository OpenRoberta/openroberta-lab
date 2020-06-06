import * as Blockly from "blockly";
import "nepo.msg";
import "nepo.blockly.overridings";
import "nepo.extensions";
import * as Variables from "nepo.variables";
import * as Procedures from "nepo.procedures";
import * as Theme from "nepo.theme.classic";
import { Sensor } from "nepo.sensor";
import { Configuration } from "nepo.configuration";
import { Action } from "nepo.action";
import { Log } from "utils/nepo.logger";

const LOG = new Log();

export abstract class Nepo {
	static bricklyWorkspace: Blockly.Workspace;
	static blocklyWorkspace: Blockly.Workspace;
	static commonBlockDescription: Object;
	static robotBlockDescription: Object;
	static robot: string;
	static robotGroup: string;
	static dataTypes: Array<string>;
	static listTypes: Array<string>;
	static dropdownTypes: Array<Array<string>>;
	static dropdownListTypes: Array<Array<string>>;


	public static inject(blocklyId: string, bricklyId: string, programToolbox: any, configToolbox: any): Object {

		// TODO: call this somewhere in the Open Roberta Lab static resources whenever a new plugin is chosen. Make sure you got the correct files from a rest call.

		this.bricklyWorkspace = Blockly.inject(bricklyId, {
			"toolbox": configToolbox,
			zoom:
			{
				controls: true,
				wheel: true,
				startScale: 1.0,
				maxScale: 3,
				minScale: 0.3,
				scaleSpeed: 1.2
			},
			move: {
				scrollbars: false,
				drag: true,
				wheel: false
			},
			trashcan: true,
			theme: Theme.nepoStyle
		});
		this.blocklyWorkspace = Blockly.inject(blocklyId, {
			"toolbox": programToolbox,
			zoom:
			{
				controls: true,
				wheel: true,
				startScale: 1.0,
				maxScale: 3,
				minScale: 0.3,
				scaleSpeed: 1.2
			},
			move: {
				scrollbars: false,
				drag: true,
				wheel: false
			},
			trashcan: true,
			theme: Theme.nepoStyle
		});
		(this.blocklyWorkspace as any).registerToolboxCategoryCallback("NEPO_VARIABLE", Variables.flyoutCallback);
		(this.blocklyWorkspace as any).registerToolboxCategoryCallback("NEPO_PROCEDURE", Procedures.flyoutCallback);
		// add default start block
		var xml = Blockly.Xml.textToDom("<xml xmlns='https://developers.google.com/blockly/xml'>" +
			"<block type='controls_start' id='mm;%5J+ugrVfS:/UE}.G' x='200' y='56'>" +
			"<mutation xmlns='http://www.w3.org/1999/xhtml' declare='false'></mutation>" +
			"</block>" +
			"</xml>");
		Blockly.Xml.domToWorkspace(xml, this.blocklyWorkspace);
		// TODO use this only for developement
		// blocklyWorkspace.addChangeListener(function(event) { console.log(event) });
		return { "blocklyWorkspace": this.blocklyWorkspace, "bricklyWorkspace": this.bricklyWorkspace };
	}

	public static initCommonBlocks(commonBlocks: Array<Blockly.Block>): Array<Object> {
		let b = [];
		for (let block of commonBlocks) {
			let a = {};
			for (let key in block) {
				let keyTmp = key;
				if (key.indexOf("args") >= 0) {
					keyTmp = "args";
				}
				if (key.indexOf("message") >= 0) {
					keyTmp = "message";
				}

				switch (keyTmp) {
					case "type":
					case "output":
					case "mutator":
					case "inputsInline":
					case "style":
					case "extensions":
					case "nextStatement":
						a[key] = block[key];
						break;
					case "message":
						this.checkMessages(block, key);
						a[key] = block[key];
						break;
					case "args":
						this.checkOptions(block, key);
						a[key] = block[key];
						break;
					default:
						console.error("error " + keyTmp);
				}
			}
			if (a["type"] == "controls_start")

				console.log(a);
			// define defaults:
			if (!a["output"] && !a["nextStatement"]) {
				a["previousStatement"] = true;
				a["nextStatement"] = true;
			}
			if (a["type"] == "controls_start")

				console.log(a);
			// extensions
			if (!(a["extensions"] instanceof Array)) {
				a["extensions"] = [];
			}
			if (!this.checkExtensionTooltip(a["extensions"])) {
				a["extensions"].push("tooltip_extension");
			}

			b.push(a);
		}
		return b;
	}

	public static defineBlocks(jsonCommonBlocks: object, jsonRobotBlocks: object) {
		Nepo.robot = jsonRobotBlocks["robot"];
		Nepo.robotGroup = jsonRobotBlocks["robotGroup"];
		Nepo.defineDataTypes(jsonRobotBlocks["dataTypes"]);
		Nepo.defineListTypes(jsonRobotBlocks["dataTypes"]);
		Nepo.defineSensors(jsonRobotBlocks["sensors"], Nepo.robot, Nepo.robotGroup);
		Nepo.defineConfigurations(jsonRobotBlocks["configurations"], Nepo.robot, Nepo.robotGroup);
		Nepo.defineActions(jsonRobotBlocks["actions"], Nepo.robot, Nepo.robotGroup);
		Nepo.defineCommonBlocks(Nepo.initCommonBlocks(jsonCommonBlocks['blocks']));
	}

	static defineSensors(sensors: Array<object>, robot: string, robotGroup: string) {
		let getSamples = sensors["getSample"];
		for (let getSample of getSamples) {
			Blockly.Blocks["sensor_" + getSample["name"].toLowerCase() + "_getSample"] = new Sensor(getSample, robot, robotGroup);
		}
		let others = sensors["other"];
		if (others) {
			Blockly.defineBlocksWithJsonArray(Nepo.initCommonBlocks(others));
		}
	}

	static defineConfigurations(configurations: Array<object>, robot: string, robotGroup: string) {
		for (let conf of configurations) {
			Blockly.Blocks[conf["category"].toLowerCase() + "_" + conf["name"].toLowerCase()] = new Configuration(conf, robot, robotGroup);
		}
		let others = configurations["other"];
		if (others) {
			Blockly.defineBlocksWithJsonArray(Nepo.initCommonBlocks(others))
		}
	}

	static defineActions(actions: Array<object>, robot: string, robotGroup: string) {
		for (let action of actions) {
			Blockly.Blocks["action_" + action["name"].toLowerCase() + "_do"] = new Action(action, robot, robotGroup);
		}
		// let others = configurations["other"];
		// if (others) {
		// 	Blockly.defineBlocksWithJsonArray(Nepo.initCommonBlocks(others))
		// }
	}

	static defineDataTypes(dataTypes: Array<string>) {
		this.dataTypes = dataTypes;
		let dropdownTypes: Array<Array<string>> = [];
		Object.values(this.dataTypes).forEach(type => {
			if (!!Blockly.Msg["DATA_TYPE_" + type.toUpperCase()]) {
				dropdownTypes.push([Blockly.Msg["DATA_TYPE_" + type.toUpperCase()], type]);
			} else {
				dropdownTypes.push(["DATA_TYPE_" + type.toUpperCase(), type]);
				LOG.warn("Blockly message does not exists", "DATA_TYPE_" + type.toUpperCase());
			}
		});
		this.dropdownTypes = dropdownTypes;
		LOG.info("defined data types", this.dataTypes);
	}

	static defineListTypes(listTypes: Array<string>) {
		this.listTypes = listTypes;
		let dropdownTypes: Array<Array<string>> = [];
		Object.values(this.dataTypes).forEach(type => {
			if (!!Blockly.Msg["DATA_TYPE_" + type.toUpperCase()]) {
				dropdownTypes.push([Blockly.Msg["DATA_TYPE_" + type.toUpperCase()], type]);
			} else {
				dropdownTypes.push(["DATA_TYPE_" + type.toUpperCase(), type]);
				LOG.warn("Blockly message does not exists", "DATA_TYPE_" + type.toUpperCase());
			}
		});
		this.dropdownListTypes = dropdownTypes;
		LOG.info("defined data list types", this.listTypes);
	}

	static defineCommonBlocks(commonBlocks: Object[]) {
		let commonBlocksExtended = commonBlocks;
		Blockly.defineBlocksWithJsonArray(commonBlocksExtended);
	}

	static checkExtensionTooltip(extensions: string[]) {
		for (var ex in extensions) {
			if (extensions[ex].indexOf("tooltip") >= 0) {
				return true;
			}
		}
		return false;
	}

	static checkMessages(block: Blockly.Block, key: string) {
		let value = block[key];
		let reg = new RegExp("message" + "(\\d+)");
		let m = key.match(reg);
		let msg = "%{BKY_" + block.type.toUpperCase() + "}";

		if (m != null) {
			if (m[1] == "0") {
				if (value.indexOf("BKY") >= 0 && !value.startsWith(msg)) {
					console.warn("Missing message for " + block.type + ": " + value);
					return;
				}
			}
			if (value.indexOf("BKY") >= 0) {
				let mes = value.slice(6, value.indexOf("}"));
				if (Blockly.Msg[mes] == undefined) {
					console.warn("No message for " + value + " defined!");
				}
			}
		} else {
			console.warn("Bad key for message: " + key);
			return;
		}
	}

	static checkOptions(block: Blockly.Block, key: string) {
		if (block[key] instanceof Array) {
			block[key].forEach(element => {
				if (element["options"] && element["options"] instanceof Array) {
					element["options"].forEach(option => {
						if (option[0].indexOf("BKY") >= 0) {
							let mes = option[0].slice(6, option[0].indexOf("}"));
							if (Blockly.Msg[mes] == undefined) {
								console.warn("No message for " + option[0] + " defined!");
							}
						}
					})
				}
			})
		}
	}

	static setCommonBlockDescription(commonBlockDescription: Object) {
		this.commonBlockDescription = commonBlockDescription;
	}
	static setRobotBlockDescription(robotBlockDescription: Object) {
		this.robotBlockDescription = robotBlockDescription;
	}
}

