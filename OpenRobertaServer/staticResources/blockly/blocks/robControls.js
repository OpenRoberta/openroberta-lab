/**
 * @fileoverview Controle blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */

'use strict';

goog.provide('Blockly.Blocks.robControls');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['robControls_start'] = {
	/**
	 * The starting point for the main program. This block is not deletable and
	 * it should not be available in any toolbox. For new task see
	 * {@link Block.robControls_activity}.
	 * 
	 * @constructs robControls_start
	 * @this.Blockly.Block
	 * @returns immediately
	 * @see {@link robControls_activity}
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(226, 0, 26);
		this.appendDummyInput().appendField('Task')
				.appendField('Hauptprogramm');
		this.setInputsInline(true);
		this.setPreviousStatement(false);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robControls_activity'] = {
	/**
	 * Marker for a new task. The main program will start this task when it
	 * executes {@link robControls_start_activity}.
	 * 
	 * @constructs robControls_activity
	 * @this.Blockly.Block
	 * @param {String} -
	 *            ACTIVITY's name.
	 * @returns immediately
	 * @see {@link robControls_start_activity}
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(226, 0, 26);
		this.appendValueInput('ACTIVITY').appendField('Task')
				.setCheck('String');
		this.setInputsInline(true);
		this.setPreviousStatement(false);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robControls_start_activity'] = {
	/**
	 * Block starting additional activity.
	 * 
	 * @constructs robControls_start_activity
	 * @param {String} -
	 *            ACTIVITY's name.
	 * @returns immediately
	 * @see {@link robControls_activity}
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(226, 0, 26);
		this.appendValueInput('ACTIVITY').appendField('starte Task').setCheck(
				'String');
		this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

/**
 * Block waiting for a condition becoming true.
 * 
 * @constructs robControls_wait
 * @param {Boolean} -
 *            any condition.
 * @returns after (first) condition is true.
 * @memberof Block
 */

Blockly.Blocks['robControls_wait'] = {

	init : function() {
		this.setColourRGB(235, 106, 10);
		// this.setInputsInline(true);
		this.appendValueInput('WAIT0').appendField(Blockly.Msg.WAIT_TITLE)
				.setCheck('Boolean');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.waitCount_ = 0;
		this.setMutatorPlus(new Blockly.MutatorPlus(this));
	},

	mutationToDom : function() {
		if (!this.waitCount_) {
			return null;
		}
		var container = document.createElement('mutation');
		if (this.waitCount_) {
			container.setAttribute('wait', this.waitCount_);
		}
		return container;
	},

	/**
	 * Parse XML to restore the wait inputs.
	 * 
	 * @param {!Element}
	 *            xmlElement XML storage element.
	 * @this Blockly.Block
	 */
	domToMutation : function(xmlElement) {
		this.waitCount_ = parseInt(xmlElement.getAttribute('wait'), 10);
		for (var x = 1; x <= this.waitCount_; x++) {
			if (x == 1) {
				this.appendStatementInput('DO0').appendField(
						Blockly.Msg.CONTROLS_IF_MSG_THEN);
			}
			this.appendValueInput('WAIT' + x).appendField('oder warte bis')
					.setCheck('Boolean');
			this.appendStatementInput('DO' + x).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_THEN);
		}
		if (this.waitCount_ >= 1) {
			this.setMutatorMinus(new Blockly.MutatorMinus(this));
		}
	},
	/**
	 * Update the shape according to the number of wait inputs.
	 * 
	 * @param {Number}
	 *            number of waits inputs.
	 * @this Blockly.Block
	 */
	updateShape_ : function(num) {
		if (num == 1) {
			this.waitCount_++;
			if (this.waitCount_ == 1)
				this.appendStatementInput('DO0').appendField(
						Blockly.Msg.CONTROLS_IF_MSG_THEN);
			this.appendValueInput('WAIT' + this.waitCount_).appendField(
					'oder warte bis').setCheck('Boolean');
			this.appendStatementInput('DO' + this.waitCount_).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_THEN);
		} else if (num == -1) {
			this.removeInput('DO' + this.waitCount_);
			this.removeInput('WAIT' + this.waitCount_);
			this.waitCount_--;
			if (this.waitCount_ == 0) {
				this.removeInput('DO0');
			}
		}
		if (this.waitCount_ >= 1) {
			if (this.waitCount_ == 1) {
				this.setMutatorMinus(new Blockly.MutatorMinus(this));
				this.render();
			}
		} else {
			this.mutatorMinus.dispose();
			this.mutatorMinus = null;
		}
	}
};

/**
 * Block waiting for sensor values.
 * 
 * @constructs robControls_wait_for
 * @param {Boolean} -
 *            sensor condition.
 * @returns after (first) condition is true.
 * @memberof Block
 */

Blockly.Blocks['robControls_wait_for'] = {

	init : function() {
		this.setColourRGB(235, 106, 10);
		// this.setInputsInline(true);
		this.appendValueInput('VALUE0').appendField(Blockly.Msg.WAIT_TITLE)
				.setCheck('Boolean');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.waitCount_ = 0;
		this.setMutatorPlus(new Blockly.MutatorPlus(this));
	},

	mutationToDom : function() {
		if (!this.waitCount_) {
			return null;
		}
		var container = document.createElement('mutation');
		if (this.waitCount_) {
			container.setAttribute('wait', this.waitCount_);
		}
		return container;
	},

	/**
	 * Parse XML to restore the wait inputs.
	 * 
	 * @param {!Element}
	 *            xmlElement XML storage element.
	 * @this Blockly.Block
	 */
	domToMutation : function(xmlElement) {
		this.waitCount_ = parseInt(xmlElement.getAttribute('wait'), 10);
		for (var x = 1; x <= this.waitCount_; x++) {
			if (x == 1) {
				this.appendStatementInput('DO0').appendField(
						Blockly.Msg.CONTROLS_IF_MSG_THEN);
			}
			this.appendValueInput('VALUE' + x).appendField('oder warte bis')
					.setCheck('Boolean');
			this.appendStatementInput('DO' + x).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_THEN);
		}
		if (this.waitCount_ >= 1) {
			this.setMutatorMinus(new Blockly.MutatorMinus(this));
		}
	},
	/**
	 * Update the shape according to the number of wait inputs.
	 * 
	 * @param {Number}
	 *            number of waits inputs.
	 * @this Blockly.Block
	 */
	updateShape_ : function(num) {
		if (num == 1) {
			this.waitCount_++;
			if (this.waitCount_ == 1)
				this.appendStatementInput('DO0').appendField(
						Blockly.Msg.CONTROLS_IF_MSG_THEN);
			this.appendValueInput('VALUE' + this.waitCount_).appendField(
					'oder warte bis').setCheck('Boolean');
			this.appendStatementInput('DO' + this.waitCount_).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_THEN);
			var lc = Blockly.Block.obtain(Blockly.mainWorkspace,
					'logic_compare');
			lc.initSvg();
			lc.render();
			var connection = this.getInput('VALUE' + this.waitCount_).connection;
			connection.connect(lc.outputConnection);

			var s = Blockly.Block.obtain(Blockly.mainWorkspace,
					'robSensors_getSample');
			s.initSvg();
			s.render();
			connection = lc.getInput('A').connection;
			connection.connect(s.outputConnection);

			var v = Blockly.Block
					.obtain(Blockly.mainWorkspace, 'logic_boolean');
			v.initSvg();
			v.render();
			connection = lc.getInput('B').connection;
			connection.connect(v.outputConnection);
		} else if (num == -1) {
			this.removeInput('DO' + this.waitCount_);
			this.removeInput('VALUE' + this.waitCount_);
			this.waitCount_--;
			if (this.waitCount_ == 0) {
				this.removeInput('DO0');
			}
		}
		if (this.waitCount_ >= 1) {
			if (this.waitCount_ == 1) {
				this.setMutatorMinus(new Blockly.MutatorMinus(this));
				this.render();
			}
		} else {
			this.mutatorMinus.dispose();
			this.mutatorMinus = null;
		}
	}
};

Blockly.Blocks['robControls_loopForever'] = {
	init : function() {
		this.setColourRGB(235, 106, 10);
		var title = new Blockly.FieldLabel(Blockly.Msg.LOOP_TITLE_FOREVER);
		this.appendDummyInput().appendField(title, 'TITLE_FOREVER');
		this.appendStatementInput('DO').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		// this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robControls_ifElse'] = {

	init : function() {
		this.setColourRGB(235, 106, 10);
		this.appendValueInput('IF0').appendField(
				Blockly.Msg.CONTROLS_IF_MSG_IF).setCheck('Boolean');
		this.appendStatementInput('DO0').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		this.appendStatementInput('ELSE').appendField(
				Blockly.Msg.CONTROLS_IF_MSG_ELSE);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setMutatorPlus(new Blockly.MutatorPlus(this));
		this.elseIfCount_ = 0;
	},
	mutationToDom : function() {
		if (!this.elseIfCount_) {
			return null;
		}
		var container = document.createElement('mutation');
		if (this.elseIfCount_) {
			container.setAttribute('elseIf', this.elseIfCount_);
		}
		return container;
	},

	/**
	 * Parse XML to restore the ifElse inputs.
	 * 
	 * @param {!Element}
	 *            xmlElement XML storage element.
	 * @this Blockly.Block
	 */
	domToMutation : function(xmlElement) {
		this.elseIfCount_ = parseInt(xmlElement.getAttribute('elseif'), 10);
		this.removeInput('ELSE');
		for (var x = 1; x <= this.elseIfCount_; x++) {
			this.appendValueInput('IF' + x).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
			this.appendStatementInput('DO' + x).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_THEN);
		}
		this.appendStatementInput('ELSE').appendField(
				Blockly.Msg.CONTROLS_IF_MSG_ELSE);
		if (this.elseIfCount_ >= 1) {
			this.setMutatorMinus(new Blockly.MutatorMinus(this));
		}
	},
	/**
	 * Update the shape according to the number of elseIf inputs.
	 * 
	 * @param {Number}
	 *            number of elseIf inputs.
	 * @this Blockly.Block
	 */
	updateShape_ : function(num) {
		if (num == 1) {
			this.elseIfCount_++;
			this.removeInput('ELSE');
			this.appendValueInput('IF' + this.elseIfCount_).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
			this.appendStatementInput('DO' + this.elseIfCount_).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_THEN);
			this.appendStatementInput('ELSE').appendField(
					Blockly.Msg.CONTROLS_IF_MSG_ELSE);
		} else if (num == -1) {
			this.removeInput('ELSE');
			this.removeInput('DO' + this.elseIfCount_);
			this.removeInput('IF' + this.elseIfCount_);
			this.appendStatementInput('ELSE').appendField(
					Blockly.Msg.CONTROLS_IF_MSG_ELSE);
			this.elseIfCount_--;
		}
		if (this.elseIfCount_ >= 1) {
			if (this.elseIfCount_ == 1) {
				this.setMutatorMinus(new Blockly.MutatorMinus(this));
				this.render();
			}
		} else {
			this.mutatorMinus.dispose();
			this.mutatorMinus = null;
		}
	}
};

Blockly.Blocks['robControls_if'] = {

	init : function() {
		this.setColourRGB(235, 106, 10);
		this.appendValueInput('IF0').appendField(
				Blockly.Msg.CONTROLS_IF_MSG_IF).setCheck('Boolean');
		this.appendStatementInput('DO0').appendField(
				Blockly.Msg.CONTROLS_IF_MSG_THEN);
		// this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setMutatorPlus(new Blockly.MutatorPlus(this));
		this.elseIfCount_ = 0;
	},
	mutationToDom : function() {
		if (!this.elseIfCount_) {
			return null;
		}
		var container = document.createElement('mutation');
		if (this.elseIfCount_) {
			container.setAttribute('elseIf', this.elseIfCount_);
		}
		return container;
	},

	/**
	 * Parse XML to restore the wait inputs.
	 * 
	 * @param {!Element}
	 *            xmlElement XML storage element.
	 * @this Blockly.Block
	 */
	domToMutation : function(xmlElement) {
		this.elseIfCount_ = parseInt(xmlElement.getAttribute('elseif'), 10);
		for (var x = 1; x <= this.elseIfCount_; x++) {
			this.appendValueInput('IF' + x).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
			this.appendStatementInput('DO' + x).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_THEN);
		}
		if (this.elseIfCount_ >= 1) {
			this.setMutatorMinus(new Blockly.MutatorMinus(this));
		}
	},
	/**
	 * Update the shape according to the number of elseIf inputs.
	 * 
	 * @param {Number}
	 *            number of waits inputs.
	 * @this Blockly.Block
	 */
	updateShape_ : function(num) {
		if (num == 1) {
			this.elseIfCount_++;
			this.appendValueInput('IF' + this.elseIfCount_).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
			this.appendStatementInput('DO' + this.elseIfCount_).appendField(
					Blockly.Msg.CONTROLS_IF_MSG_THEN);
		} else if (num == -1) {
			this.removeInput('DO' + this.elseIfCount_);
			this.removeInput('IF' + this.elseIfCount_);
			this.elseIfCount_--;
		}
		if (this.elseIfCount_ >= 1) {
			if (this.elseIfCount_ == 1) {
				this.setMutatorMinus(new Blockly.MutatorMinus(this));
				this.render();
			}
		} else {
			this.mutatorMinus.dispose();
			this.mutatorMinus = null;
		}
	}
};
