import * as Blockly from "blockly";

export class HiddenField extends Blockly.FieldLabel {
	constructor() {
		super();
		this.size_ = { width: 0, height: 0 };

		this.isVisible = function () {
			return false;
		};

		this.render_ = function () {
			// nothing to render.
		}
		this.isSerializable = function () {

			return true;
		};
	}
}
