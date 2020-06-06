import * as Blockly from "blockly";

export class MutatorPlus extends Blockly.Mutator {
	constructor() {
		super([]);
		(this as any).clicked_ = false;
	}

	drawIcon_(group: any) {
		// Square.
		(Blockly.utils as any).dom.createSvgElement('rect',
			{
				'class': 'blocklyIconShape',
				'height': '16', 'width': '16',
				'fill-opacity': '0',
				'stroke-opacity': '0'
			},
			group);
		// +
		(Blockly.utils as any).dom.createSvgElement('path',
			{
				'class': 'blocklyIconSymbol',
				'd': 'M18 10h-4v-4c0-1.104-.896-2-2-2s-2 .896-2 2l.071 4h-4.071' +
					'c-1.104 0-2 .896-2 2s.896 2 2 2l4.071-.071-.071 4.071' +
					'c0 1.104.896 2 2 2s2-.896 2-2v-4.071l4 .071c1.104 0 2-.896 2-2s-.896-2-2-2z',
				'transform': 'scale(0.67)'
			},
			group);
	}
	iconClick_() {
		if (this.block_.isEditable() && !this.block_.isInFlyout) {
			(this.block_ as any).updateShape_(1);
		}
	};
}
