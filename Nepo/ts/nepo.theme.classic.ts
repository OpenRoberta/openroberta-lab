import * as Blockly from "blockly";


var classic = {};

classic["defaultBlockStyles"] = {
	"colour_blocks": {
		"colourPrimary": "#EBC300"
	},
	"list_blocks": {
		"colourPrimary": "#39378B"
	},
	"logic_blocks": {
		"colourPrimary": "#33B8CA"
	},
	"control_blocks": {
		"colourPrimary": "#EB6A0A"
	},
	"math_blocks": {
		"colourPrimary": "#005A94"
	},
	"procedure_blocks": {
		"colourPrimary": "#179C7D"
	},
	"text_blocks": {
		"colourPrimary": "#BACC1E"
	},
	"variable_blocks": {
		"colourPrimary": "#9085BA"
	},
	"start_blocks": {
		"colourPrimary": "#E23C39"
	},
	"action_blocks": {
		"colourPrimary": "#F29400"
	},
	"sensor_blocks": {
		"colourPrimary": "#8FA402"
	},
	"communication_blocks": {
		"colourPrimary": "#FF69B4"
	},
	"image_blocks": {
		"colourPrimary": "#DF01D7"
	}
};

classic["categoryStyles"] = {
	"colour_category": {
		"colour": "#EBC300"
	},
	"list_category": {
		"colour": "#39378B"
	},
	"logic_category": {
		"colour": "#33B8CA"
	},
	"control_category": {
		"colour": "#EB6A0A"
	},
	"math_category": {
		"colour": "#005A94"
	},
	"procedure_category": {
		"colour": "#179C7D"
	},
	"text_category": {
		"colour": "#BACC1E"
	},
	"variable_category": {
		"colour": "#9085BA"
	},
	"variable_dynamic_category": {
		"colour": "#9085BA"
	},
	"action_category": {
		"colour": "#F29400"
	},
	"sensor_category": {
		"colour": "#8FA402"
	},
	"communication_category": {
		"colour": "#FF69B4"
	},
	"image_category": {
		"colour": "#DF01D7"
	}
};
export const nepoStyle: Blockly.Theme = new Blockly.Theme("nepoClassic", classic["defaultBlockStyles"], classic["categoryStyles"]);
