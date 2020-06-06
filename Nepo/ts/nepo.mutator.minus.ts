import * as Blockly from "blockly";

export class MutatorMinus extends Blockly.Mutator {
	constructor() {
		super([]);
		(this as any).clicked_ = false;
	}

	drawIcon_(group: any) {
		(Blockly.utils as any).dom.createSvgElement("rect",
			{
				"class": "blocklyIconShape",
				"height": "16",
				"width": "16",
				"fill-opacity": "0",
				"stroke-opacity": "0"
			},
			group);

		(Blockly.utils as any).dom.createSvgElement("path",
			{
				"class": "blocklyIconSymbol",
				"d": "M18 11h-12c-1.104 0-2 .896-2 2s.896 2 2 2h12c1.104 0 2-.896 2-2s-.896-2-2-2z",
				"transform": "scale(0.67)"
			}, group);
	};
	iconClick_() {
		if (this.block_.isEditable()) {
			(this.block_ as any).updateShape_(-1);
		}
	};
}
