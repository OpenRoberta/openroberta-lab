define(["require", "exports", "blockly", "nepo.mutator.minus", "utils/nepo.logger"], function (require, exports, Blockly, nepo_mutator_minus_1, nepo_logger_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.QUOTE_IMAGE_MIXIN = exports.TEXT_JOIN_MUTATOR_MIXIN = exports.IS_DIVISIBLEBY_MUTATOR_MIXIN = exports.COMMON_TYPE_MIXIN = exports.COMMENTS_IMAGE_MIXIN = exports.CONTROLS_WAIT_FOR_MUTATOR_MIXIN = exports.CONTROLS_IF_MUTATOR_MIXIN = void 0;
    var LOG = new nepo_logger_1.Log();
    LOG;
    exports.CONTROLS_IF_MUTATOR_MIXIN = {
        elseifCount_: 0,
        elseCount_: 0,
        /**
         * Don't automatically add STATEMENT_PREFIX and STATEMENT_SUFFIX to generated
         * code.  These will be handled manually in this block's generators.
         */
        suppressPrefixSuffix: true,
        /**
         * Create XML to represent the number of else-if and else inputs.
         * @return {Element} XML storage element.
         * @this {Blockly.Block}
         */
        mutationToDom: function () {
            if (!this.elseifCount_ && !this.elseCount_) {
                return null;
            }
            var container = Blockly.utils.xml.createElement('mutation');
            if (this.elseifCount_) {
                container.setAttribute('elseif', this.elseifCount_);
            }
            if (this.elseCount_) {
                container.setAttribute('else', 1);
            }
            return container;
        },
        /**
         * Parse XML to restore the else-if and else inputs.
         * @param {!Element} xmlElement XML storage element.
         * @this {Blockly.Block}
         */
        domToMutation: function (xmlElement) {
            this.elseifCount_ = parseInt(xmlElement.getAttribute('elseif'), 10) || 0;
            this.elseCount_ = parseInt(xmlElement.getAttribute('else'), 10) || 0;
            for (var x = 1; x <= this.elseifCount_; x++) {
                this.appendValueInput('IF' + x).appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
                this.appendStatementInput('DO' + x).appendField(Blockly.Msg.CONTROLS_IF_MSG_THEN);
            }
            if (this.elseifCount_ >= 1) {
                this.setMutator(new nepo_mutator_minus_1.MutatorMinus());
            }
            if (this.elseCount_ > 0) {
                this.appendStatementInput('ELSE').appendField(Blockly.Msg['CONTROLS_IF_MSG_ELSE']);
            }
        },
        /**
         * Update the shape according to the input
         * @param {Number} number +1 add at the end, -1 remove last.
         * @this Blockly.Block
         */
        updateShape_: function (num) {
            var elseStatementConnection;
            if (this.getInput('ELSE')) {
                elseStatementConnection = this.getInput('ELSE').connection.targetConnection;
                this.removeInput("ELSE");
            }
            if (num == 1) {
                this.elseifCount_++;
                this.appendValueInput('IF' + this.elseifCount_).appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSEIF).setCheck('Boolean');
                this.appendStatementInput('DO' + this.elseifCount_).appendField(Blockly.Msg.CONTROLS_IF_MSG_THEN);
            }
            else if (num == -1) {
                var target = this.getInput('IF' + this.elseifCount_).connection.targetConnection;
                if (target) {
                    target.disconnect();
                }
                this.removeInput('DO' + this.elseifCount_, true);
                this.removeInput('IF' + this.elseifCount_), true;
                this.elseifCount_--;
            }
            if (this.elseifCount_ >= 1) {
                if (this.elseifCount_ == 1) {
                    this.setMutator(new nepo_mutator_minus_1.MutatorMinus());
                    this.render();
                }
            }
            else {
                this.mutatorMinus.dispose();
                this.mutatorMinus = null;
                this.render();
            }
            if (this.elseCount_ > 0) {
                this.appendStatementInput('ELSE').appendField(Blockly.Msg['CONTROLS_IF_MSG_ELSE']);
                if (elseStatementConnection != undefined) {
                    Blockly.Mutator.reconnect(elseStatementConnection, this, 'ELSE');
                }
            }
        }
    };
    exports.CONTROLS_WAIT_FOR_MUTATOR_MIXIN = {
        waitCount_: 0,
        /**
         * Don't automatically add STATEMENT_PREFIX and STATEMENT_SUFFIX to generated
         * code.  These will be handled manually in this block's generators.
         */
        suppressPrefixSuffix: true,
        /**
         * Create XML to represent the number of else-if and else inputs.
         * @return {Element} XML storage element.
         * @this {Blockly.Block}
         */
        mutationToDom: function () {
            if (!this.waitCount_ && !this.elseCount_) {
                return null;
            }
            var container = Blockly.utils.xml.createElement('mutation');
            if (this.waitCount_) {
                container.setAttribute('wait', this.waitCount_);
            }
            return container;
        },
        /**
         * Parse XML to restore the else-if and else inputs.
         * @param {!Element} xmlElement XML storage element.
         * @this {Blockly.Block}
         */
        domToMutation: function (xmlElement) {
            this.waitCount_ = parseInt(xmlElement.getAttribute('wait'), 10) || 0;
            if (this.waitCount_ >= 1) {
                this.setMutator(new nepo_mutator_minus_1.MutatorMinus());
                this.appendStatementInput('DO' + x).appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
            }
            for (var x = 1; x <= this.waitCount_; x++) {
                this.appendValueInput('WAIT' + x).appendField(Blockly.Msg.CONTROLS_WAIT_FOR_OR).setCheck('Boolean');
                this.appendStatementInput('DO' + x).appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
            }
        },
        /**
         * Update the shape according to the number of wait inputs.
         * @param {Number} number of else inputs.
         * @this Blockly.Block
         */
        updateShape_: function (num) {
            if (num == 1) {
                this.waitCount_++;
                if (this.waitCount_ == 1) {
                    this.appendStatementInput('DO0').appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
                }
                this.appendValueInput('WAIT' + this.waitCount_).appendField(Blockly.Msg.CONTROLS_WAIT_FOR_OR).setCheck('Boolean');
                this.appendStatementInput('DO' + this.waitCount_).appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
            }
            else if (num == -1) {
                var target = this.getInput('WAIT' + this.waitCount_).connection.targetConnection;
                if (target) {
                    target.disconnect();
                }
                this.removeInput('DO' + this.waitCount_, true);
                this.removeInput('WAIT' + this.waitCount_, true);
                if (this.waitCount_ == 1) {
                    this.removeInput('DO0', true);
                }
                this.waitCount_--;
            }
            if (this.waitCount_ == 1) {
                this.setMutator(new nepo_mutator_minus_1.MutatorMinus());
                this.render();
            }
            if (this.waitCount_ == 0) {
                this.mutatorMinus.dispose();
                this.mutatorMinus = null;
                this.render();
            }
        }
    };
    exports.COMMENTS_IMAGE_MIXIN = {
        /**
         * Image data URI of an LTR opening double quote (same as RTL closing double quote).
         * @readonly
         */
        COMMENTS_IMAGE_LEFT: 'data:image/svg+xml;charset=UTF-8,<svg version="1.1" baseProfile="full" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ev="http://www.w3.org/2001/xml-events" width="32" height="32" viewBox="0 0 32 32" fill="%23ffffff" ><path d="M24 8h-17.333c-2.2 0-4 1.8-4 4v9.333c0 2.2 1.8 4 4 4h1.333v4l4-4h12c2.2 0 4-1.8 4-4v-9.333c0-2.2-1.8-4-4-4zM25.333 21.333c0 0.723-0.611 1.333-1.333 1.333h-17.333c-0.723 0-1.333-0.611-1.333-1.333v-9.333c0-0.723 0.611-1.333 1.333-1.333h17.333c0.723 0 1.333 0.611 1.333 1.333v9.333zM9.333 19.333c-1.472 0-2.667-1.195-2.667-2.667s1.195-2.667 2.667-2.667 2.667 1.195 2.667 2.667-1.195 2.667-2.667 2.667zM9.333 15.333c-0.736 0-1.333 0.597-1.333 1.333s0.597 1.333 1.333 1.333 1.333-0.597 1.333-1.333-0.597-1.333-1.333-1.333zM15.333 19.333c-1.472 0-2.667-1.195-2.667-2.667s1.195-2.667 2.667-2.667 2.667 1.195 2.667 2.667-1.195 2.667-2.667 2.667zM15.333 15.333c-0.736 0-1.333 0.597-1.333 1.333s0.597 1.333 1.333 1.333 1.333-0.597 1.333-1.333-0.597-1.333-1.333-1.333zM21.333 19.333c-1.472 0-2.667-1.195-2.667-2.667s1.195-2.667 2.667-2.667 2.667 1.195 2.667 2.667-1.195 2.667-2.667 2.667zM21.333 15.333c-0.736 0-1.333 0.597-1.333 1.333s0.597 1.333 1.333 1.333 1.333-0.597 1.333-1.333-0.597-1.333-1.333-1.333z"></path></svg>',
        COMMENTS_IMAGE_RIGHT: 'data:image/svg+xml;charset=UTF-8,<svg version="1.1" baseProfile="full" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ev="http://www.w3.org/2001/xml-events" width="32" height="32" viewBox="-32 0 32 32" fill="%23ffffff"><path transform="scale(-1,1)" d="M24 8h-17.333c-2.2 0-4 1.8-4 4v9.333c0 2.2 1.8 4 4 4h1.333v4l4-4h12c2.2 0 4-1.8 4-4v-9.333c0-2.2-1.8-4-4-4zM25.333 21.333c0 0.723-0.611 1.333-1.333 1.333h-17.333c-0.723 0-1.333-0.611-1.333-1.333v-9.333c0-0.723 0.611-1.333 1.333-1.333h17.333c0.723 0 1.333 0.611 1.333 1.333v9.333zM9.333 19.333c-1.472 0-2.667-1.195-2.667-2.667s1.195-2.667 2.667-2.667 2.667 1.195 2.667 2.667-1.195 2.667-2.667 2.667zM9.333 15.333c-0.736 0-1.333 0.597-1.333 1.333s0.597 1.333 1.333 1.333 1.333-0.597 1.333-1.333-0.597-1.333-1.333-1.333zM15.333 19.333c-1.472 0-2.667-1.195-2.667-2.667s1.195-2.667 2.667-2.667 2.667 1.195 2.667 2.667-1.195 2.667-2.667 2.667zM15.333 15.333c-0.736 0-1.333 0.597-1.333 1.333s0.597 1.333 1.333 1.333 1.333-0.597 1.333-1.333-0.597-1.333-1.333-1.333zM21.333 19.333c-1.472 0-2.667-1.195-2.667-2.667s1.195-2.667 2.667-2.667 2.667 1.195 2.667 2.667-1.195 2.667-2.667 2.667zM21.333 15.333c-0.736 0-1.333 0.597-1.333 1.333s0.597 1.333 1.333 1.333 1.333-0.597 1.333-1.333-0.597-1.333-1.333-1.333z"></path></svg>',
        COMMENTS_IMAGE_WIDTH: 18,
        /**
         * Pixel height of QUOTE_IMAGE_LEFT_DATAURI and QUOTE_IMAGE_RIGHT_DATAURI.
         * @readonly
         */
        COMMENTS_IMAGE_HEIGHT: 18,
        /**
         * Inserts appropriate quote images before and after the named field.
         * @param {string} fieldName The name of the field to wrap with quotes.
         * @this {Blockly.Block}
         */
        commentField_: function (fieldName) {
            for (var i = 0, input; (input = this.inputList[i]); i++) {
                for (var j = 0, field; (field = input.fieldRow[j]); j++) {
                    if (fieldName == field.name) {
                        input.insertFieldAt(j, this.newComment_(true));
                        input.insertFieldAt(j + 2, this.newComment_(false));
                        return;
                    }
                }
            }
            console.warn('field named "' + fieldName + '" not found in ' + this.toDevString());
        },
        /**
         * A helper function that generates a FieldImage of an opening or
         * closing double quote. The selected quote will be adapted for RTL blocks.
         * @param {boolean} open If the image should be open quote (“ in LTR).
         *                       Otherwise, a closing quote is used (” in LTR).
         * @return {!Blockly.FieldImage} The new field.
         * @this {Blockly.Block}
         */
        newComment_: function (open) {
            var isLeft = this.RTL ? !open : open;
            var dataUri = isLeft ?
                this.COMMENTS_IMAGE_LEFT :
                this.COMMENTS_IMAGE_RIGHT;
            return new Blockly.FieldImage(dataUri, this.COMMENTS_IMAGE_WIDTH, this.COMMENTS_IMAGE_HEIGHT, isLeft ? '\u201C' : '\u201D');
        }
    };
    exports.COMMON_TYPE_MIXIN = {
        mutationToDom: function () {
            var container = document.createElement('mutation');
            if (this.dataType_) {
                container.setAttribute("datatype", this.dataType_);
                return container;
            }
        },
        domToMutation: function (xmlElement) {
            var dataType = xmlElement.getAttribute("datatype");
            if (dataType) {
                this.updateDataType(dataType);
            }
        },
        updateDataType: function (dataType) {
            this.dataType_ = dataType;
            if (this.outputConnection) {
                this.outputConnection.setCheck(dataType);
            }
            else {
                for (var i = 0, input; (input = this.inputList[i]); i++) {
                    if (input.connection) {
                        input.connection.setCheck(dataType);
                    }
                }
            }
        }
    };
    exports.IS_DIVISIBLEBY_MUTATOR_MIXIN = {
        /**
         * Create XML to represent whether the 'divisorInput' should be present.
         * @return {!Element} XML storage element.
         * @this {Blockly.Block}
         */
        mutationToDom: function () {
            var container = Blockly.utils.xml.createElement('mutation');
            var divisorInput = (this.getFieldValue('PROPERTY') == 'DIVISIBLE_BY');
            container.setAttribute('divisor_input', divisorInput);
            return container;
        },
        /**
         * Parse XML to restore the 'divisorInput'.
         * @param {!Element} xmlElement XML storage element.
         * @this {Blockly.Block}
         */
        domToMutation: function (xmlElement) {
            var divisorInput = (xmlElement.getAttribute('divisor_input') == 'true');
            this.updateShape_(divisorInput);
        },
        /**
         * Modify this block to have (or not have) an input for 'is divisible by'.
         * @param {boolean} divisorInput True if this block has a divisor input.
         * @private
         * @this {Blockly.Block}
         */
        updateShape_: function (divisorInput) {
            // Add or remove a Value Input.
            var inputExists = this.getInput('DIVISOR');
            if (divisorInput) {
                if (!inputExists) {
                    this.appendValueInput('DIVISOR')
                        .setCheck('Number');
                }
            }
            else if (inputExists) {
                this.removeInput('DIVISOR');
            }
        }
    };
    exports.TEXT_JOIN_MUTATOR_MIXIN = {
        itemCount_: 1,
        /**
         * Create XML to represent number of text inputs.
         * @return {!Element} XML storage element.
         * @this {Blockly.Block}
         */
        mutationToDom: function () {
            var container = Blockly.utils.xml.createElement('mutation');
            container.setAttribute('items', this.itemCount_);
            return container;
        },
        /**
         * Parse XML to restore the text inputs.
         * @param {!Element} xmlElement XML storage element.
         * @this {Blockly.Block}
         */
        domToMutation: function (xmlElement) {
            this.itemCount_ = parseInt(xmlElement.getAttribute('items'), 10);
            for (var x = 2; x <= this.itemCount_; x++) {
                this.appendValueInput('ADD' + x).setCheck('Boolean');
            }
        },
        updateShape_: function (num) {
            if (num == 1) {
                this.itemCount_++;
                this.appendValueInput('ADD' + this.itemCount_);
            }
            else if (num == -1) {
                var target = this.getInput('ADD' + this.itemCount_).connection.targetConnection;
                if (target) {
                    target.disconnect();
                }
                this.removeInput('ADD' + this.itemCount_, true);
                this.itemCount_--;
            }
            if (this.itemCount_ > 1) {
                if (this.itemCount_ == 2) {
                    this.setMutator(new nepo_mutator_minus_1.MutatorMinus());
                    this.render();
                }
            }
            else {
                this.mutatorMinus.dispose();
                this.mutatorMinus = null;
                this.render();
            }
        }
    };
    exports.QUOTE_IMAGE_MIXIN = {
        /**
         * Image data URI of an LTR opening double quote (same as RTL closing double quote).
         * @readonly
         */
        QUOTE_IMAGE_LEFT_DATAURI: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAKCAQAAAAqJXdxAAAA' +
            'n0lEQVQI1z3OMa5BURSF4f/cQhAKjUQhuQmFNwGJEUi0RKN5rU7FHKhpjEH3TEMtkdBSCY' +
            '1EIv8r7nFX9e29V7EBAOvu7RPjwmWGH/VuF8CyN9/OAdvqIXYLvtRaNjx9mMTDyo+NjAN1' +
            'HNcl9ZQ5oQMM3dgDUqDo1l8DzvwmtZN7mnD+PkmLa+4mhrxVA9fRowBWmVBhFy5gYEjKMf' +
            'z9AylsaRRgGzvZAAAAAElFTkSuQmCC',
        /**
         * Image data URI of an LTR closing double quote (same as RTL opening double quote).
         * @readonly
         */
        QUOTE_IMAGE_RIGHT_DATAURI: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAKCAQAAAAqJXdxAAAA' +
            'qUlEQVQI1z3KvUpCcRiA8ef9E4JNHhI0aFEacm1o0BsI0Slx8wa8gLauoDnoBhq7DcfWhg' +
            'gONDmJJgqCPA7neJ7p934EOOKOnM8Q7PDElo/4x4lFb2DmuUjcUzS3URnGib9qaPNbuXvB' +
            'O3sGPHJDRG6fGVdMSeWDP2q99FQdFrz26Gu5Tq7dFMzUvbXy8KXeAj57cOklgA+u1B5Aos' +
            'lLtGIHQMaCVnwDnADZIFIrXsoXrgAAAABJRU5ErkJggg==',
        /**
         * Pixel width of QUOTE_IMAGE_LEFT_DATAURI and QUOTE_IMAGE_RIGHT_DATAURI.
         * @readonly
         */
        QUOTE_IMAGE_WIDTH: 12,
        /**
         * Pixel height of QUOTE_IMAGE_LEFT_DATAURI and QUOTE_IMAGE_RIGHT_DATAURI.
         * @readonly
         */
        QUOTE_IMAGE_HEIGHT: 12,
        /**
         * Inserts appropriate quote images before and after the named field.
         * @param {string} fieldName The name of the field to wrap with quotes.
         * @this {Blockly.Block}
         */
        quoteField_: function (fieldName) {
            for (var i = 0, input; (input = this.inputList[i]); i++) {
                for (var j = 0, field; (field = input.fieldRow[j]); j++) {
                    if (fieldName == field.name) {
                        input.insertFieldAt(j, this.newQuote_(true));
                        input.insertFieldAt(j + 2, this.newQuote_(false));
                        return;
                    }
                }
            }
            console.warn('field named "' + fieldName + '" not found in ' + this.toDevString());
        },
        /**
         * A helper function that generates a FieldImage of an opening or
         * closing double quote. The selected quote will be adapted for RTL blocks.
         * @param {boolean} open If the image should be open quote (“ in LTR).
         *                       Otherwise, a closing quote is used (” in LTR).
         * @return {!Blockly.FieldImage} The new field.
         * @this {Blockly.Block}
         */
        newQuote_: function (open) {
            var isLeft = this.RTL ? !open : open;
            var dataUri = isLeft ?
                this.QUOTE_IMAGE_LEFT_DATAURI :
                this.QUOTE_IMAGE_RIGHT_DATAURI;
            return new Blockly.FieldImage(dataUri, this.QUOTE_IMAGE_WIDTH, this.QUOTE_IMAGE_HEIGHT, isLeft ? '\u201C' : '\u201D');
        }
    };
});
//# sourceMappingURL=nepo.constants.mixins.js.map