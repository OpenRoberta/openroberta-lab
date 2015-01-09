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
     * it should not be available in any toolbox. This is also the block where
     * variable declaration can be instantiated via the plus mutator. For new
     * task see {@link Block.robControls_activity}.
     * 
     * @constructs robControls_start
     * @this.Blockly.Block
     * @returns immediately
     * @see {@link robControls_activity}
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIVITY_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.START + ' ' + Blockly.Msg.START_PROGRAM);
        this.setMutatorPlus(new Blockly.MutatorPlus(this));
        this.declare_ = false;
        this.setPreviousStatement(false);
        this.setNextStatement(true);
        this.setDeletable(false);
        this.setHelp(new Blockly.Help(Blockly.Msg.START_HELP, 'test1.gif'));
        this.setTooltip(Blockly.Msg.START_TOOLTIP);
    },
    /**
     * Create XML to represent whether a statement list of variable declarations
     * should be present.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        if (!this.declare_ === undefined) {
            return false;
        }
        var container = document.createElement('mutation');
        container.setAttribute('declare', (this.declare_ == true));
        return container;
    },

    /**
     * Parse XML to restore the statement list.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        this.declare_ = (xmlElement.getAttribute('declare') != 'false');
        if (this.declare_) {
            this.appendStatementInput('ST');
        }
    },
    /**
     * Update the shape according, if declarations exists.
     * 
     * @param {Number}
     *            number 1 add a variable declaration, -1 remove a variable
     *            declaration.
     * @this Blockly.Block
     */
    updateShape_ : function(num) {
        if (num == 1) {
            if (!this.declare_) {
                this.appendStatementInput('ST');
                this.declare_ = true;
            }
            var vd = Blockly.Block.obtain(Blockly.mainWorkspace, 'robGlobalvariables_declare');
            vd.initSvg();
            vd.render();
            var value = vd.getInput('VALUE');
            var block = Blockly.Block.obtain(Blockly.mainWorkspace, 'math_number');
            block.initSvg();
            block.render();
            value.connection.connect(block.outputConnection);
            var connection;
            if (this.getInput('ST').connection.targetConnection) {
                var block = this.getInput('ST').connection.targetConnection.sourceBlock_;
                if (block) {
                    // look for the last variable declaration block in the sequence
                    while (block.getNextBlock()) {
                        block = block.getNextBlock();
                    }
                }
                block.setNext(true);
                connection = block.nextConnection;
            } else {
                connection = this.getInput('ST').connection;
            }
            connection.connect(vd.previousConnection);
        } else if (num == -1) {
            this.removeInput('ST');
            this.declare_ = false;
        }
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
        this.setColourRGB(Blockly.CAT_ROBACTIVITY_RGB);
        this.appendDummyInput().appendField(new Blockly.FieldTextInput(Blockly.Msg.START_ACTIVITY, Blockly.Procedures.rename), 'ACTIVITY');
        this.setPreviousStatement(false);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.ACTIVITY_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBACTIVITY_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.START).appendField(new Blockly.FieldTextInput(Blockly.Msg.START_ACTIVITY, Blockly.Procedures.rename), 'ACTIVITY');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.START_ACTIVITY_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBCONTROLS_RGB);
        // this.setInputsInline(true);
        this.appendValueInput('WAIT0').appendField(Blockly.Msg.WAIT_UNTIL).setCheck('Boolean');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.waitCount_ = 0;
        this.setMutatorPlus(new Blockly.MutatorPlus(this));
        this.setTooltip(Blockly.Msg.WAIT_TOOLTIP);
    },
    /**
     * Create XML to represent the number of wait counts.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
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
                this.appendStatementInput('DO0').appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
            }
            this.appendValueInput('WAIT' + x).appendField(Blockly.Msg.CONTROLS_WAIT_OR).setCheck('Boolean');
            this.appendStatementInput('DO' + x).appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
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
                this.appendStatementInput('DO0').appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
            this.appendValueInput('WAIT' + this.waitCount_).appendField(Blockly.Msg.WAIT_OR).setCheck('Boolean');
            this.appendStatementInput('DO' + this.waitCount_).appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
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
            this.render();
        }
    }
};

/**
 * Block waiting for some time.
 * 
 * @constructs robControls_wait_time
 * @param {Boolean} -
 *            any condition.
 * @returns after (first) condition is true.
 * @memberof Block
 */

Blockly.Blocks['robControls_wait_time'] = {

    init : function() {
        this.setColourRGB(Blockly.CAT_ROBCONTROLS_RGB);
        this.setInputsInline(true);
        this.appendValueInput('WAIT').appendField(Blockly.Msg.WAIT).setCheck('Number');
        this.appendDummyInput().appendField('ms');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.WAIT_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBCONTROLS_RGB);
        this.appendValueInput('WAIT0').appendField(Blockly.Msg.WAIT_UNTIL).setCheck('Boolean');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.waitCount_ = 0;
        this.setMutatorPlus(new Blockly.MutatorPlus(this));
        this.setTooltip(Blockly.Msg.WAIT_FOR_TOOLTIP);
    },
    /**
     * Create XML to represent the number of wait counts.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
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
                this.appendStatementInput('DO0').appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
            }
            this.appendValueInput('WAIT' + x).appendField(Blockly.Msg.WAIT_UNTIL).setCheck('Boolean');
            this.appendStatementInput('DO' + x).appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
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
                this.appendStatementInput('DO0').appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
            this.appendValueInput('WAIT' + this.waitCount_).appendField(Blockly.Msg.WAIT_OR).setCheck('Boolean');
            this.appendStatementInput('DO' + this.waitCount_).appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
            var lc = Blockly.Block.obtain(Blockly.mainWorkspace, 'logic_compare');
            lc.initSvg();
            lc.render();
            var connection = this.getInput('WAIT' + this.waitCount_).connection;
            connection.connect(lc.outputConnection);

            var s = Blockly.Block.obtain(Blockly.mainWorkspace, 'robSensors_getSample');
            s.initSvg();
            s.render();
            connection = lc.getInput('A').connection;
            connection.connect(s.outputConnection);

            var v = Blockly.Block.obtain(Blockly.mainWorkspace, 'logic_boolean');
            v.initSvg();
            v.render();
            connection = lc.getInput('B').connection;
            connection.connect(v.outputConnection);
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
            this.render();
        }
    }
};

Blockly.Blocks['robControls_loopForever'] = {
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBCONTROLS_RGB);
        var title = new Blockly.FieldLabel(Blockly.Msg.LOOP_FOREVER);
        this.appendDummyInput().appendField(title, 'TITLE_FOREVER');
        this.appendStatementInput('DO').appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
        // this.setInputsInline(true);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.LOOPFOREVER_TOOLTIP);
    }
};

Blockly.Blocks['robControls_ifElse'] = {

    init : function() {
        this.setColourRGB(Blockly.CAT_ROBCONTROLS_RGB);
        this.appendValueInput('IF0').appendField(Blockly.Msg.CONTROLS_IF_MSG_IF).setCheck('Boolean');
        this.appendStatementInput('DO0').appendField(Blockly.Msg.CONTROLS_IF_MSG_THEN);
        this.appendStatementInput('ELSE').appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSE);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setMutatorPlus(new Blockly.MutatorPlus(this));
        this.elseIfCount_ = 0;
        this.elseCount_ = 1;
        // this.updateShape_(0);
        this.setTooltip(Blockly.Msg.IFELSE_TOOLTIP);
    },
    mutationToDom : function() {
        if (!this.elseIfCount_ && !this.elseCount_) {
            return null;
        }
        var container = document.createElement('mutation');
        if (this.elseIfCount_) {
            container.setAttribute('elseIf', this.elseIfCount_);
        }
        if (this.elseCount_) {
            container.setAttribute('else', 1);
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
        if (xmlElement.hasAttribute('elseif')) {
            this.elseIfCount_ = parseInt(xmlElement.getAttribute('elseif'), 10);
        }
        this.removeInput('ELSE');
        for (var x = 1; x <= this.elseIfCount_; x++) {
            this.appendValueInput('IF' + x).appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
            this.appendStatementInput('DO' + x).appendField(Blockly.Msg.CONTROLS_IF_MSG_THEN);
        }
        this.appendStatementInput('ELSE').appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSE);
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
            this.appendValueInput('IF' + this.elseIfCount_).appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
            this.appendStatementInput('DO' + this.elseIfCount_).appendField(Blockly.Msg.CONTROLS_IF_MSG_THEN);
            this.appendStatementInput('ELSE').appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSE);
        } else if (num == 0) {
            this.elseIfCount_ = 0;
        } else if (num == -1) {
            this.removeInput('ELSE');
            this.removeInput('DO' + this.elseIfCount_);
            this.removeInput('IF' + this.elseIfCount_);
            this.appendStatementInput('ELSE').appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSE);
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
        this.setColourRGB(Blockly.CAT_ROBCONTROLS_RGB);
        this.appendValueInput('IF0').appendField(Blockly.Msg.CONTROLS_IF_MSG_IF).setCheck('Boolean');
        this.appendStatementInput('DO0').appendField(Blockly.Msg.CONTROLS_IF_MSG_THEN);
        // this.setInputsInline(true);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setMutatorPlus(new Blockly.MutatorPlus(this));
        this.elseIfCount_ = 0;
        this.setTooltip(Blockly.Msg.IF_TOOLTIP);
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
            this.appendValueInput('IF' + x).appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
            this.appendStatementInput('DO' + x).appendField(Blockly.Msg.CONTROLS_IF_MSG_THEN);
        }
        if (this.elseIfCount_ >= 1) {
            this.setMutatorMinus(new Blockly.MutatorMinus(this));
        }
    },
    /**
     * Update the shape according to the number of elseIf inputs.
     * 
     * @param {Number}
     *            number of else inputs.
     * @this Blockly.Block
     */
    updateShape_ : function(num) {
        if (num == 1) {
            this.elseIfCount_++;
            this.appendValueInput('IF' + this.elseIfCount_).appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
            this.appendStatementInput('DO' + this.elseIfCount_).appendField(Blockly.Msg.CONTROLS_IF_MSG_THEN);
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
