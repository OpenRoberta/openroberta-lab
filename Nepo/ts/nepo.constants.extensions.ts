import * as Blockly from "blockly";
import * as NepoMix from "nepo.constants.mixins";
import { Log } from "utils/nepo.logger";
import { Nepo } from "nepo.blockly";

const LOG = new Log();
LOG;

/**
 * Create tooltips for all blocks dynamically. The first dropdowns value , if a dropdown is available, will be added to the message key
 * so that the corresponding tooltip will be chosen. Apply this to all blocks that do not have a dedicated tooltip function.
 */
export const COMMON_TOOLTIP_EXTENSION = function() {
	var thisBlock = this;
	var type = thisBlock.type.toUpperCase();
	// define the standard tooltip
	var tooltip = "%{BKY_" + type + "_TOOLTIP}";
	this.setTooltip(function() {
		// check if there are dropdowns or variable used in the block
		let dropDownValue = "";
		let dropDownText = "";
		let variableName = "";
		let thisTooltip = tooltip;
		for (let input of thisBlock.inputList) {
			for (let field of input.fieldRow) {
				if (field instanceof Blockly.FieldVariable) {
					variableName = field.getText();
					break;
				}
				if (field instanceof Blockly.FieldDropdown) {
					dropDownValue = field.getValue();
					dropDownText = field.getText();
					break;
				}
			}
		}
		if (dropDownValue.length > 0 && Blockly.Msg[thisTooltip.slice(6, -1)] == undefined) {
			thisTooltip = "%{BKY_" + type + "_TOOLTIP_" + dropDownValue.toUpperCase() + "}";
		}
		// document missing tooltip
		if (Blockly.Msg[thisTooltip.slice(6, -1)] == undefined) {
			console.warn('No tooltip for ' + type + " defined!");
		}
		return Blockly.utils.replaceMessageReferences(thisTooltip).replace("%1", variableName.length > 0 ? variableName : dropDownText);
	});
}

/**
 * Special tooltip function  for blocks where the tooltip of the parent should be displayed. 
 */
export const COMMON_PARENT_TOOLTIP_EXTENSION = function() {
	// this refers to the block that the extension is being run on, we need
	// to cache it so that it can be used inside the tooltip function.
	var thisBlock = this;
	this.setTooltip(function() {
		var parent = thisBlock.getParent();
		return (parent && parent.getInputsInline() && parent.tooltip) ||
			Blockly.utils.replaceMessageReferences("%{BKY_" + thisBlock.type.toUpperCase() + "_TOOLTIP}");
	});
}
/**
 * Special tooltip function if blocks depending on the number of the else-if and else parts. 
 */
export const CONTROLS_IF_TOOLTIP_EXTENSION = function() {
	this.setTooltip(function() {
		if (!this.elseifCount_ && !this.elseCount_) {
			return Blockly.Msg['CONTROLS_IF_TOOLTIP_1'];
		} else if (!this.elseifCount_ && this.elseCount_) {
			return Blockly.Msg['CONTROLS_IF_TOOLTIP_2'];
		} else if (this.elseifCount_ && !this.elseCount_) {
			return Blockly.Msg['CONTROLS_IF_TOOLTIP_3'];
		} else if (this.elseifCount_ && this.elseCount_) {
			return Blockly.Msg['CONTROLS_IF_TOOLTIP_4'];
		}
		return '';
	}.bind(this));
}
/**
 * A common data type dropdown provider. To be used whenever the block offers a change of datatype of an input field.
 */
export const DATATYPE_DROPDOWN_VALIDATOR_EXTENSION = function() {
	(Blockly.FieldDropdown as any).validateOptions_(Nepo.dropdownTypes);
	this.getField("DATATYPE").menuGenerator_ = Nepo.dropdownTypes;
	this.getField("DATATYPE").setValidator(function(option: string) {
		if (option && option !== this.sourceBlock_.getFieldValue('DATATYPE')) {
			this.sourceBlock_.updateDataType(option);
		}
	});
	this.getField("DATATYPE").doValueUpdate_(Nepo.dropdownTypes[0][1]);
}

export const IS_DIVISIBLE_MUTATOR_EXTENSION = function() {
	this.getField('PROPERTY').setValidator(function(option: string) {
		var divisorInput = (option == 'DIVISIBLE_BY');
		this.getSourceBlock().updateShape_(divisorInput);
	});
}

export const TEXT_QUOTES_EXTENSION = function() {
	this.mixin(NepoMix.QUOTE_IMAGE_MIXIN);
	this.quoteField_('TEXT');
}

export const TEXT_JOIN_EXTENSION = function() {
	this.mixin(NepoMix.QUOTE_IMAGE_MIXIN);
}

export const TEXT_COMMENTS_EXTENSION = function() {
	this.mixin(NepoMix.COMMENTS_IMAGE_MIXIN);
	this.commentField_('TEXT');
}

export const TEXT_COMMENTS_VALIDATOR = function() {
	this.getField('TEXT').setValidator(function(content: string) {
		if (content && content.match(/[<>\$]/)) {
			return null;
		}
		return content;
	});
}

