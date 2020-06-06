define(["require", "exports", "blockly", "utils/nepo.logger"], function (require, exports, Blockly, nepo_logger_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.PROCEDURE_MIXIN = exports.PROCEDURE_EXTENSION = exports.PROCEDURE_CALL_MIXIN = exports.PROCEDURE_CALL_EXTENSION = exports.flyoutCallback = void 0;
    var LOG = new nepo_logger_1.Log();
    LOG;
    function flyoutCallback(workspace) {
        workspace;
        var xmlList = [];
        if (Blockly.Blocks['procedures_defnoreturn']) {
            // <block type="procedures_defnoreturn" gap="16">
            //     <field name="NAME">do something</field>
            // </block>
            var block = Blockly.utils.xml.createElement('block');
            block.setAttribute('type', 'procedures_defnoreturn');
            block.setAttribute('gap', "16");
            var nameField = Blockly.utils.xml.createElement('field');
            nameField.setAttribute('name', 'NAME');
            nameField.appendChild(Blockly.utils.xml.createTextNode(Blockly.Msg['PROCEDURES_DEF_PROCEDURE']));
            block.appendChild(nameField);
            xmlList.push(block);
        }
        if (Blockly.Blocks['procedures_defreturn']) {
            // <block type="procedures_defreturn" gap="16">
            //     <field name="NAME">do something</field>
            // </block>
            var block = Blockly.utils.xml.createElement('block');
            block.setAttribute('type', 'procedures_defreturn');
            block.setAttribute('gap', "16");
            var nameField = Blockly.utils.xml.createElement('field');
            nameField.setAttribute('name', 'NAME');
            nameField.appendChild(Blockly.utils.xml.createTextNode(Blockly.Msg['PROCEDURES_DEF_PROCEDURE']));
            block.appendChild(nameField);
            xmlList.push(block);
        }
        if (xmlList.length) {
            // Add slightly larger gap between system blocks and user calls.
            xmlList[xmlList.length - 1].setAttribute('gap', "24");
        }
        function populateProcedures(procedureList, templateName) {
            for (var i = 0; i < procedureList.length; i++) {
                var name = procedureList[i][0];
                var args = procedureList[i][1];
                // <block type="procedures_callnoreturn" gap="16">
                //   <mutation name="do something">
                //     <arg name="x" dataType="Number" varId="idxyz"></arg>
                //   </mutation>
                // </block>
                var block = Blockly.utils.xml.createElement('block');
                block.setAttribute('type', templateName);
                block.setAttribute('gap', "16");
                var mutation = Blockly.utils.xml.createElement('mutation');
                mutation.setAttribute('name', name);
                block.appendChild(mutation);
                for (var j = 0; j < args.length; j++) {
                    var variable = workspace.getVariableById(args[j]);
                    var arg = Blockly.utils.xml.createElement('arg');
                    arg.setAttribute('name', variable.name);
                    arg.setAttribute('dataType', variable.type);
                    //arg.setAttribute('varId', args[j].getId());
                    mutation.appendChild(arg);
                }
                xmlList.push(block);
            }
        }
        var tuple = Blockly.Procedures.allProcedures(workspace);
        populateProcedures(tuple[0], 'procedures_callnoreturn');
        populateProcedures(tuple[1], 'procedures_callreturn');
        return xmlList;
    }
    exports.flyoutCallback = flyoutCallback;
    exports.PROCEDURE_CALL_EXTENSION = function () {
        this.args_ = 0;
        this.mixin(exports.PROCEDURE_CALL_MIXIN, true);
    };
    exports.PROCEDURE_CALL_MIXIN = {
        mutationToDom: function () {
            var container = Blockly.utils.xml.createElement('mutation');
            container.setAttribute("name", this.getFieldValue("NAME"));
            var i = 1;
            var input;
            while (input = this.getInput("ARG" + i)) {
                var arg = Blockly.utils.xml.createElement('arg');
                arg.setAttribute("name", input.fieldRow[0].value_);
                arg.setAttribute("datatype", input.connection.getCheck()[0]);
                container.appendChild(arg);
                i++;
                input = this.getInput("ARG" + i);
            }
            return container;
        },
        /**
         * Parse XML to restore the argument inputs.
         * @param {!Element} xmlElement XML storage element.
         * @this {Blockly.Block}
         */
        domToMutation: function (xmlElement) {
            var name = xmlElement.getAttribute("name");
            this.renameProcedure(this.getProcedureCall(), name);
            for (var x = 0, childNode; childNode = xmlElement.childNodes[x]; x++) {
                if (childNode.nodeName.toLowerCase() == 'arg') {
                    this.addParam(childNode.getAttribute("name"), childNode.getAttribute("datatype"));
                }
            }
        },
        /**
        * Returns the name of the procedure this block calls.
        * @return {string} Procedure name.
        * @this {Blockly.Block}
        */
        getProcedureCall: function () {
            // The NAME field is guaranteed to exist, null will never be returned.
            return /** @type {string} */ (this.getFieldValue("NAME"));
        },
        /**
      * Notification that a procedure is renaming.
      * If the name matches this block's procedure, rename it.
      * @param {string} oldName Previous name of procedure.
      * @param {string} newName Renamed procedure.
      * @this {Blockly.Block}
      */
        renameProcedure: function (oldName, newName) {
            if (Blockly.Names.equals(oldName, this.getProcedureCall())) {
                this.setFieldValue(newName, "NAME");
                var baseMsg = this.outputConnection ?
                    Blockly.Msg['PROCEDURES_CALLRETURN_TOOLTIP'] :
                    Blockly.Msg['PROCEDURES_CALLNORETURN_TOOLTIP'];
                this.setTooltip(baseMsg.replace('%1', newName));
            }
        },
        /**
        * Add a parameter from this procedure definition block.
        * @private
        * @this {Blockly.Block}
         */
        addParam: function (name, opt_type) {
            this.args_++;
            var thisType = opt_type || "Number";
            var input = this.appendValueInput('ARG' + this.args_).
                setAlign(Blockly.ALIGN_RIGHT).
                appendField(name).
                setCheck(thisType);
            input.init();
        },
        /**
        * Add a parameter from this procedure definition block.
        * @private
        * @this {Blockly.Block}
         */
        removeParam: function (name, type) {
            var input;
            var i = 1;
            var found = false;
            while (input = this.getInput("ARG" + i)) {
                if (input.fieldRow[0].value_ === name && input.connection.getCheck()[0] === type) {
                    this.removeInput("ARG" + i, true);
                    found = true;
                    break;
                }
                i++;
                input = this.getInput("ARG" + i);
            }
            if (found) {
                i++;
                input = this.getInput("ARG" + i);
                while (input) {
                    input.name = "ARG" + (i - 1);
                    i++;
                    input = this.getInput("ARG" + i);
                }
            }
        }
    };
    exports.PROCEDURE_EXTENSION = function () {
        this.mixin(exports.PROCEDURE_MIXIN, true);
        this.varScope = true;
        this.scopeId_ = this.id;
        this.varDecl = true;
        this.setNextStatement(false);
        this.setPreviousStatement(false);
        this.getField("NAME").setValidator(Blockly.Procedures.rename);
    };
    exports.PROCEDURE_MIXIN = {
        /**
         * Return the signature of this procedure definition.
         * @return {!Array} Tuple containing three elements:
         *     - the name of the defined procedure,
         *     - a list of all its arguments,
         *     - that it DOES NOT have a return value.
         * @this {Blockly.Block}
         */
        getProcedureDef: function () {
            var argList = [];
            var declBlock = this.getInput("DECL") && this.getInput("DECL").connection && this.getInput("DECL").connection.targetBlock();
            while (declBlock) {
                argList.push(declBlock.variable_.getId());
                declBlock = declBlock.getNextBlock();
            }
            return [this.getFieldValue("NAME"), argList, this.type.indexOf("no") >= 0 ? false : true];
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
        },
        dispose: function (healStack, animate) {
            if (healStack) {
                var callerList = Blockly.Procedures.getCallers(this.getFieldValue("NAME"), this.workspace);
                callerList.forEach(function (caller) { return caller.dispose(true); });
            }
            Blockly.BlockSvg.prototype.dispose.call(this, !!healStack, animate);
        }
    };
});
//# sourceMappingURL=nepo.procedures.js.map