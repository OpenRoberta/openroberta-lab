import * as Blockly from "blockly";
import * as Ajv from "ajv";
import * as schemaRobot from "nepo.schema.robot";
import * as schemaCommon from "nepo.schema.common";
import { Nepo } from "nepo.blockly";


const $blocklyArea: JQuery = $("#blocklyArea");
const $bricklyArea: JQuery = $("#bricklyArea");
const $blocklyDiv: JQuery = $("#blocklyDiv");
const $bricklyDiv: JQuery = $("#bricklyDiv");

declare var blocklyWorkspace;
declare var bricklyWorkspace;

var ajv = new Ajv();

(window as any).LOG = true;

export function init() {
	$("#exportP").click(function() {
		toXml($("#importExportP"), blocklyWorkspace);
	});
	$("#importP").click(function() {
		fromXml($("#importExportP"), blocklyWorkspace);
	});
	$("#exportC").click(function() {
		toXml($("#importExportC"), bricklyWorkspace);
	});
	$("#importC").click(function() {
		fromXml($("#importExportC"), bricklyWorkspace);
	});

	let programToolbox = $("#program-toolbox")[0];
	let configToolbox = $("#config-toolbox")[0];
	let commonBlockDescription = null;
	let robotBlockDescription = null;
	$.getJSON("../OpenRobertaRobot/src/main/resources/nepoBlocks.json", function(result) {
		if (validateSchema(result, schemaCommon.schema)) {
			commonBlockDescription = result;
		}
	}).always(function() {
		$.getJSON("../RobotMbed/src/main/resources/calliope/nepoBlocks.json", function(result) {
			if (validateSchema(result, schemaRobot.schema)) {
				robotBlockDescription = result;
			}
		}).always(function() {
			if (!(commonBlockDescription && robotBlockDescription)) {
				console.assert(commonBlockDescription && robotBlockDescription ? true : false, "error TODO more");
				return;
			}
			Nepo.defineBlocks(commonBlockDescription, robotBlockDescription);
			let workspaces: Object = Nepo.inject("blocklyDiv", "bricklyDiv", programToolbox, configToolbox);
			(window as any).blocklyWorkspace = workspaces["blocklyWorkspace"];
			(window as any).bricklyWorkspace = workspaces["bricklyWorkspace"];
			resizeAll();
			Blockly.svgResize(blocklyWorkspace);
			Blockly.svgResize(bricklyWorkspace);
		});
	});
}

function validateSchema(json: object, schema: any): boolean {
	var validate: any = ajv.compile(schema);
	var valid: boolean = validate(json);
	if (!valid) {
		console.error(validate.errors);
	} else {
		console.log("validation against schema " + schema.title + " succeded");
	};
	return valid;
}
function toXml($out, workspace) {
	var xml = Blockly.Xml.workspaceToDom(workspace);
	$out.html(Blockly.Xml.domToPrettyText(xml));


}

function fromXml($in, workspace) {
	if (!$in.text()) {
		return;
	}
	var xml = Blockly.Xml.textToDom($in.text());
	Blockly.Xml.domToWorkspace(xml, workspace);
}
function resizeAll() {
	function resizeDiv($div: JQuery, $area: JQuery) {
		var x = $area.offset().left;
		var y = $area.offset().top;
		$div.offset({ top: y, left: x });
		$div.width($area.width());
		$div.height($area.height());
	}
	resizeDiv($blocklyDiv, $blocklyArea);
	resizeDiv($bricklyDiv, $bricklyArea);
	Blockly.svgResize(blocklyWorkspace);
	Blockly.svgResize(bricklyWorkspace);
};

$(window).on("resize", function() {
	resizeAll();
});
