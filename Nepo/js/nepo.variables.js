define(["require", "exports", "blockly", "utils/nepo.logger"], function (require, exports, Blockly, nepo_logger_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getVariablesByName = exports.VARIABLE_SCOPE_EXTENSION = exports.VARIABLE_PLUS_MUTATOR_MIXIN = exports.VARIABLE_MIXIN = exports.VARIABLE_DECLARATION_MIXIN = exports.VARIABLE_DECLARATION_EXTENSION = exports.INTERNAL_VARIABLE_DECLARATION_MIXIN = exports.INTERNAL_VARIABLE_DECLARATION_EXTENSION = exports.getUniqueVariables = exports.flyoutCallback = void 0;
    var LOG = new nepo_logger_1.Log();
    function flyoutCallback(workspace) {
        // add the new scope block always on top:
        // <block type="variable_scope" > </block>
        var xmlList = [];
        var scope = Blockly.utils.xml.createElement("block");
        scope.setAttribute("type", "variable_scope");
        scope.setAttribute("gap", 16);
        xmlList.push(scope);
        var variableModelList = getUniqueVariables(workspace);
        if (variableModelList.length > 0) {
            variableModelList.forEach(function (variable) {
                if (Blockly.Blocks["variables_set"]) {
                    var block = Blockly.utils.xml.createElement("block");
                    block.setAttribute("type", "variables_set");
                    block.setAttribute("gap", 8);
                    block.setAttribute("id", variable.getId());
                    block.appendChild(Blockly.Variables.generateVariableFieldDom(variable));
                    xmlList.push(block);
                }
                if (Blockly.Blocks["variables_get"]) {
                    var block = Blockly.utils.xml.createElement("block");
                    block.setAttribute("type", "variables_get");
                    block.setAttribute("gap", 16);
                    block.appendChild(Blockly.Variables.generateVariableFieldDom(variable));
                    xmlList.push(block);
                }
            });
        }
        ;
        return xmlList;
    }
    exports.flyoutCallback = flyoutCallback;
    /**
     * Finds all variables unique by name and returns them. For variables with same names only the first instance found
     * will be considered.
     * @param {!Blockly.Workspace} workspace Workspace of the global variables.
     * @return {!Array.<!Blockly.VariableModel>} All variables unique by name or null if no variables are declared.
     */
    function getUniqueVariables(workspace) {
        var prev;
        var uniqueList = [];
        workspace.getAllVariables().sort(Blockly.VariableModel.compareByName).forEach(function (variable) {
            if (!prev || variable.name !== prev.name) {
                uniqueList.push(variable);
            }
            prev = variable;
        });
        return uniqueList;
    }
    exports.getUniqueVariables = getUniqueVariables;
    exports.INTERNAL_VARIABLE_DECLARATION_EXTENSION = function () {
        this.mixin(exports.INTERNAL_VARIABLE_DECLARATION_MIXIN, true);
        this.varScope = true;
        this.scopeId_ = this.id;
        this.dataType_ = "Number";
        this.scopeType = "LOOP";
        this.varDecl = true;
        this.internalVarDecl = true;
        this.scopeId_ = this.id;
        this.setFieldValue(Blockly.Msg["VARIABLES_LOOP_DEFAULT_NAME"], "VAR");
        this.getField("VAR").setValidator(VARIABLE_DECLARATION_VALIDATOR);
    };
    exports.INTERNAL_VARIABLE_DECLARATION_MIXIN = {
        getScopeId: function () {
            return this.scopeId_;
        },
        getDataType: function () {
            return this.dataType_;
        },
        getVarName: function () {
            return this.getField("VAR").getValue();
        },
        mutationToDom: function () {
            var container = document.createElement('mutation');
            var variable = (this.variable_ && this.variable_.getId());
            container.setAttribute('var_decl', variable);
            return container;
        },
        domToMutation: function (xmlElement) {
            var varDecl = xmlElement.getAttribute('var_decl');
            if (varDecl === this.id) {
                this.variable_ = this.workspace.getVariableById(varDecl);
            }
            else {
                var name_1 = getUniqueName(this, [], Blockly.Msg["VARIABLES_LOOP_DEFAULT_NAME"]);
                this.variable_ = this.workspace.createVariable(name_1, "Number", this.id);
                this.getField("VAR").setValue(name_1);
            }
        },
        onchange: function (e) {
            if (this.isInFlyout) {
                return;
            }
            else if (e.blockId === this.id && e.type === Blockly.Events.BLOCK_MOVE) {
                checkScope(this);
            }
        },
        dispose: function (healStack, animate) {
            if (this.variable_ && this.workspace.getVariableById(this.variable_.getId())) {
                this.workspace.deleteVariableById(this.variable_.getId());
                LOG.warn("delete loop variable", this);
            }
            Blockly.BlockSvg.prototype.dispose.call(this, !!healStack, animate);
        }
    };
    exports.VARIABLE_DECLARATION_EXTENSION = function () {
        this.previousConnection.setCheck("declaration_only");
        this.setMovable(false);
        var name;
        this.scopeId_ = this.id;
        this.scopeType = "LOCAL";
        name = getUniqueName(this, [], Blockly.Msg["VARIABLES_LOCAL_DEFAULT_NAME"]);
        this.variable_ = this.workspace.createVariable(name, "Number", this.id);
        this.varDecl = true;
        this.dataType_ = "Number";
        this.setNextStatement(false, "declaration_only");
        this.next_ = false;
        this.getField("VAR").setValue(name);
        this.getField("VAR").setValidator(VARIABLE_DECLARATION_VALIDATOR);
    };
    exports.VARIABLE_DECLARATION_MIXIN = {
        /**
       * Create XML to represent variable declaration insides.
       * @return {Element} XML storage element.
       * @this Blockly.Block
       */
        mutationToDom: function () {
            if (this.next_ === undefined || this.dataType_ === undefined) {
                return false;
            }
            var container = document.createElement('mutation');
            container.setAttribute("next", this.next_);
            container.setAttribute("datatype", this.dataType_);
            container.setAttribute("scopetype", this.scopeType);
            return container;
        },
        /**
         * Parse XML to restore variable declarations.
         * @param {!Element} xmlElement XML storage element.
         * @this Blockly.Block
         */
        domToMutation: function (xmlElement) {
            this.next_ = xmlElement.getAttribute("next") == 'true';
            if (this.next_) {
                this.setNextStatement(this.next_, "declaration_only");
            }
            this.dataType_ = xmlElement.getAttribute("datatype");
            if (this.dataType_ && this.getInput["VALUE"]) {
                this.getInput('VALUE').setCheck(this.dataType_);
            }
            this.scopeType = xmlElement.getAttribute("scopetype");
            if (this.scopeType) {
                this.setscopeType(this.scopeType);
            }
        },
        getScopeId: function () {
            return this.scopeId_;
        },
        getDataType: function () {
            return this.dataType_;
        },
        getVarName: function () {
            return this.getField("VAR").getValue();
        },
        updateShape_: function (num) {
            if (num == -1) {
                var procBlock = this.getSurroundParent();
                var callerList = Blockly.Procedures.getCallers(procBlock.getFieldValue("NAME"), this.workspace);
                var thisVarName_1 = this.variable_.name;
                var thisscopeType_1 = this.variable_.type;
                // remove this variable declaration
                this.workspace.deleteVariableById(this.id);
                // check if the user confirmed the deletion
                var variableTmp = this.workspace.getVariableById(this.id);
                if (!variableTmp) {
                    var surroundParent = this.getSurroundParent();
                    var parent_1 = this.getParent();
                    var nextBlock = this.getNextBlock();
                    this.unplug(true, true);
                    if (parent_1 === surroundParent && !nextBlock) {
                        parent_1.updateShape_(num);
                    }
                    else if (!nextBlock) {
                        parent_1.setNextStatement(false);
                    }
                    callerList.forEach(function (caller) { return caller.removeParam(thisVarName_1, thisscopeType_1); });
                    this.dispose();
                }
            }
        },
        updateDataType: function (dataType) {
            var variableMap = this.workspace.getVariableMap().variableMap_;
            var variableList = variableMap[this.dataType_];
            for (var i = 0, tempVar; (tempVar = variableList[i]); i++) {
                if (tempVar.getId() == this.variable_.getId()) {
                    variableList.splice(i, 1);
                }
            }
            var variables = variableMap[dataType] || [];
            variables.push(this.variable_);
            variableMap[dataType] = variables;
            this.dataType_ = dataType;
            this.variable_.type = dataType;
            if (this.getInput("VALUE")) {
                this.getInput("VALUE").setCheck(dataType);
            }
        },
        setscopeType: function (scopeType) {
            this.scopeType = scopeType;
            setStyle(this, this.scopeType);
        },
        dispose: function (healStack, animate) {
            if (this.variable_ && this.workspace.getVariableById(this.variable_.getId())) {
                this.workspace.deleteVariableById(this.variable_.getId());
                LOG.warn("delete loop variable", this);
            }
            Blockly.BlockSvg.prototype.dispose.call(this, !!healStack, animate);
        }
    };
    var VARIABLE_DECLARATION_VALIDATOR = function (newName) {
        if (newName === this.value_) {
            return newName;
        }
        var thisBlock = this.getSourceBlock();
        var scopeVars = [];
        var scopeBlock = thisBlock.workspace.getBlockById(thisBlock.getScopeId());
        if (scopeBlock) {
            if (scopeBlock.scopeType === "GLOBAL") {
                scopeVars = getUniqueVariables(scopeBlock.workspace);
            }
            else {
                scopeVars = getVarScopeList(scopeBlock);
            }
        }
        var name = getUniqueName(thisBlock, scopeVars, newName);
        var varId = thisBlock.variable_ && thisBlock.variable_.getId();
        if (varId) {
            thisBlock.workspace.renameVariableById(varId, name);
        }
        else {
            thisBlock.workspace.createVariable(name);
        }
        return name;
    };
    exports.VARIABLE_MIXIN = {
        onchange: function () {
            if (this.isInFlyout) {
                return;
            }
            var id = this.getFieldValue("VAR");
            var thisVariable = Blockly.Variables.getVariable(this.workspace, id);
            // correct the type checker of this instance, only necessary, if there are more than one variable with the same name
            if (this.type.indexOf("variables") >= 0) {
                var dataTypes = [];
                var varList = getVariablesByName(this.workspace, thisVariable.name);
                for (var variable in varList) {
                    dataTypes.push(varList[variable].type);
                }
                ;
                this.updateDataType(dataTypes);
                LOG.info(dataTypes);
            }
            var tmpVariable;
            this.setWarningText(null);
            // allow not connected instances of this (no code generation for this anyway)
            if (this.type === "variables_get" && !this.outputConnection) {
                return;
            }
            if (this.type === "variables_set" && !this.getNextBlock() && !this.getPreviousBlock()) {
                return;
            }
            // global variables can appear everywhere
            var varType = this.workspace.getBlockById(this.getField("VAR").variable_.getId()).scopeType;
            if (varType && varType === "GLOBAL") {
                return;
            }
            ;
            var valid = false;
            var surroundParent = this.getSurroundParent();
            if (surroundParent) {
                var surrVarDeclList = getSurrScopeList(this.getSurroundParent());
                tmpVariable = Object.values(surrVarDeclList).find(function (variable) { return variable.getId() === thisVariable.getId(); });
                if (tmpVariable === undefined) {
                    var varNameList = getVariablesByName(this.workspace, thisVariable.name);
                    var _loop_1 = function (a) {
                        tmpVariable = Object.values(surrVarDeclList).find(function (variable) { return variable.getId() === a.getId(); });
                        if (tmpVariable !== undefined) {
                            this_1.getField("VAR").doValueUpdate_(tmpVariable.getId());
                            valid = true;
                            this_1.updateDataType(tmpVariable.type);
                            return "break";
                        }
                    };
                    var this_1 = this;
                    for (var _i = 0, _a = Object.values(varNameList); _i < _a.length; _i++) {
                        var a = _a[_i];
                        var state_1 = _loop_1(a);
                        if (state_1 === "break")
                            break;
                    }
                    ;
                }
                else {
                    valid = true;
                    this.updateDataType(tmpVariable.type);
                }
            }
            if (!valid) {
                this.setWarningText(Blockly.Msg.VARIABLES_SCOPE_WARNING);
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
        },
        customContextMenu: function (options) {
            // Getter blocks have the option to create a setter block, and vice versa.
            if (!this.isInFlyout) {
                var opposite_type;
                var contextMenuMsg;
                var id = this.getFieldValue('VAR');
                var variableModel = this.workspace.getVariableById(id);
                var scopeType = variableModel.type;
                if (this.type == 'variables_get') {
                    opposite_type = 'variables_set';
                    contextMenuMsg = Blockly.Msg['VARIABLES_GET_CREATE_SET'];
                }
                else {
                    opposite_type = 'variables_get';
                    contextMenuMsg = Blockly.Msg['VARIABLES_SET_CREATE_GET'];
                }
                var option = { enabled: this.workspace.remainingCapacity() > 0 };
                var name = this.getField('VAR').getText();
                option["text"] = contextMenuMsg.replace('%1', name);
                var xmlField = Blockly.utils.xml.createElement('field');
                xmlField.setAttribute("name", 'VAR');
                xmlField.setAttribute('variabletype', scopeType);
                xmlField.appendChild(Blockly.utils.xml.createTextNode(name));
                var xmlBlock = Blockly.utils.xml.createElement('block');
                xmlBlock.setAttribute('type', opposite_type);
                xmlBlock.appendChild(xmlField);
                option["callback"] = Blockly.ContextMenu.callbackFactory(this, xmlBlock);
                options.push(option);
            }
        }
    };
    exports.VARIABLE_PLUS_MUTATOR_MIXIN = {
        declare_: false,
        /**
     * Create XML to represent whether a statement list of variable declarations
     * should be present.
     *
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
        mutationToDom: function () {
            if (!this.declare_ === undefined) {
                return false;
            }
            var container = document.createElement('mutation');
            container.setAttribute('declare', (this.declare_ == true).toString());
            return container;
        },
        /**
         * Parse XML to restore the statement list.
         *
         * @param {!Element}
         *            xmlElement XML storage element.
         * @this Blockly.Block
         */
        domToMutation: function (xmlElement) {
            this.declare_ = (xmlElement.getAttribute('declare') != 'false');
            if (this.declare_) {
                var variableDeclareStatement = new Blockly.Input(Blockly.NEXT_STATEMENT, "DECL", this, this.makeConnection_(Blockly.NEXT_STATEMENT));
                this.inputList.splice(1, 0, variableDeclareStatement);
                // making sure only declarations can connect to the statement list
                this.getInput("DECL").connection.setCheck('declaration_only');
                this.declare_ = true;
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
        updateShape_: function (num) {
            if (!this.workspace.isDragging || this.workspace.isDragging() || this.workspace.isFlyout) {
                return;
            }
            if (num === 1) {
                Blockly.Events.setGroup(true);
                if (!this.declare_) {
                    var variableDeclareStatement = new Blockly.Input(Blockly.NEXT_STATEMENT, "DECL", this, this.makeConnection_(Blockly.NEXT_STATEMENT));
                    this.inputList.splice(1, 0, variableDeclareStatement);
                    // making sure only declarations can connect to the statement list
                    this.getInput("DECL").connection.setCheck('declaration_only');
                    this.declare_ = true;
                }
                var variableDeclare_1 = this.type.indexOf("procedures") >= 0 ? this.workspace.newBlock('variable_param_declare') : this.workspace.newBlock('variable_declare');
                var scopeVars = getVarScopeList(this);
                if (this.scopeType === "GLOBAL") {
                    scopeVars = getUniqueVariables(this.workspace);
                }
                var name_2;
                name_2 = Blockly.Msg["VARIABLES_" + this.scopeType + "_DEFAULT_NAME"];
                setUniqueName(variableDeclare_1, scopeVars, name_2);
                variableDeclare_1.setscopeType(this.scopeType);
                variableDeclare_1.initSvg();
                variableDeclare_1.render();
                var connection = void 0;
                if (this.getInput("DECL").connection.targetConnection) {
                    var block = this.getInput("DECL").connection.targetConnection.sourceBlock_;
                    if (block) {
                        // look for the last variable declaration block in the sequence
                        while (block.getNextBlock()) {
                            block = block.getNextBlock();
                        }
                    }
                    block.setNextStatement(true, "declaration_only");
                    block.next_ = true;
                    connection = block.nextConnection;
                }
                else {
                    connection = this.getInput("DECL").connection;
                }
                connection.connect(variableDeclare_1.previousConnection);
                checkScope(this);
                if (this.scopeType === "PROC") {
                    var callerList = Blockly.Procedures.getCallers(this.getFieldValue("NAME"), this.workspace);
                    callerList.forEach(function (caller) { return caller.addParam(variableDeclare_1.getFieldValue("VAR")); });
                }
                Blockly.Events.setGroup(false);
            }
            else if (num == -1) {
                // if the last declaration in the stack has been removed, remove the declaration statement
                this.removeInput("DECL");
                this.declare_ = false;
            }
        },
        // scope extension
        onchange: function (e) {
            // no need to check scope for global vars (start block) nor for blocks in the toolbox's flyout'
            if (this.scopeType === "GLOBAL" || this.isInFlyout) {
                return;
            }
            else if (e.blockId == this.id && e.type == Blockly.Events.BLOCK_MOVE) {
                checkScope(this);
            }
        }
    };
    exports.VARIABLE_SCOPE_EXTENSION = function () {
        this.varScope = true;
        switch (this.type) {
            case "controls_start":
                this.scopeType = "GLOBAL";
                break;
            case "variable_scope":
                this.scopeType = "LOCAL";
                break;
            case "procedures_defnoreturn":
            case "procedures_defreturn":
                this.scopeType = "PROC";
                break;
            case "controls_for":
            case "controls_forEach":
                this.scopeType = "LOOP";
                break;
            default:
                this.scopeType = "LOCAL";
        }
    };
    /**
     * Should be called whenever a scope block has been moved to check if the variable names are still valid. If
     * not, the variables (and fields) are renamed.
     * @param {!Blockly.Block} scopeBlock Block of the scope.
     */
    function checkScope(scopeBlock) {
        var scopeVars = getVarScopeList(scopeBlock);
        var succDeclBlocks = getSuccDeclList(scopeBlock);
        succDeclBlocks.forEach(function (block) {
            if (!isUniqueName(block, scopeVars)) {
                setUniqueName(block, scopeVars);
            }
        });
    }
    function isUniqueName(thisBlock, scopeList) {
        var name = thisBlock.getField("VAR") && thisBlock.getField("VAR").getValue();
        for (var _i = 0, _a = Object.values(scopeList); _i < _a.length; _i++) {
            var variable = _a[_i];
            if (variable.getId() !== thisBlock.id && variable.name === name) {
                return false;
            }
        }
        return true;
    }
    ;
    function getUniqueName(thisBlock, scopeList, opt_name) {
        var name = opt_name || (thisBlock.getField("VAR") && thisBlock.getField("VAR").getValue());
        var names = scopeList.filter(function (variable) {
            if (variable.getId() !== thisBlock.id) {
                return variable;
            }
        }).map(function (variableModel) { return variableModel.name; });
        var newName = name;
        while (newName && (names.indexOf(newName) >= 0 || Blockly.Procedures.isNameUsed(newName, thisBlock.workspace))) {
            var r = newName.match(/^(.*?)(\d+)$/);
            if (!r) {
                r = newName.match(/^[a-zA-Z]{1}$/);
                if (!r) {
                    newName += "2";
                }
                else {
                    // special case variable names in loops, e.g. i,j ...
                    newName = Blockly.Variables.generateUniqueName(thisBlock.workspace);
                }
            }
            else {
                newName = r[1] + (parseInt(r[2], 10) + 1);
            }
        }
        return newName;
    }
    /**
     * Finds variable models by name. This method searches in all defined variables.
     * @param {!Blockly.Workspace} workspace Workspace where to search.
     * @param {!string} name of the variable.
     * @return {!Array.<!Blockly.VariableModel>} The variables found with the name, empty list if no variable found.
     */
    function getVariablesByName(workspace, name) {
        return workspace.getAllVariables().
            filter(function (variable) { return variable.name === name; });
    }
    exports.getVariablesByName = getVariablesByName;
    /**
     * Finds all variables of this blocks' scope. Including global variables and variables of the same scope level.
     * @param {!Blockly.Block} scopeBlock Block of the scope.
     * @return {!Array.<!Blockly.VariableModel>} The variables of the scope.
     */
    function getVarScopeList(scopeBlock) {
        var surrScopeVars = getSurrScopeList(scopeBlock);
        var succScopeVars = getSuccVarList(scopeBlock);
        var varScopeList = surrScopeVars.concat(succScopeVars);
        return varScopeList;
    }
    function getSurrScopeList(scopeBlock) {
        var scopeVars = getGlobalVars(scopeBlock.workspace);
        var surroundParent = scopeBlock;
        while (!!surroundParent) {
            if (surroundParent.varScope) {
                var declBlock = surroundParent.getFirstStatementConnection() &&
                    surroundParent.getFirstStatementConnection().targetBlock();
                // special case internal variable declarations, e.g. in loops
                if (surroundParent.internalVarDecl) {
                    declBlock = surroundParent;
                }
                while (declBlock) {
                    if (declBlock.varDecl) {
                        scopeVars.push(declBlock.variable_);
                    }
                    declBlock = declBlock.getNextBlock();
                }
            }
            surroundParent = surroundParent.getSurroundParent();
        }
        return scopeVars;
    }
    function getSuccVarList(thisBlock) {
        return getSuccDeclList(thisBlock).map(function (block) { return block.variable_; });
    }
    function getSuccDeclList(thisBlock) {
        var firstDeclBlock;
        var list = [];
        if (thisBlock.getInput("DECL")) {
            firstDeclBlock = thisBlock.getInput("DECL").connection && thisBlock.getInput("DECL").connection.targetBlock();
            list = firstDeclBlock ? firstDeclBlock.getDescendants(true) : [];
            var firstDoBlock = thisBlock.getInput("DO") && thisBlock.getInput("DO").connection && thisBlock.getInput("DO").connection.targetBlock();
            list = list.concat(firstDoBlock ? firstDoBlock.getDescendants(true) : []);
        }
        else if (thisBlock.internalVarDecl) {
            // special case internal variable declarations, e.g. in loops
            list = [thisBlock];
            var firstBlock = thisBlock.getFirstStatementConnection() && thisBlock.getFirstStatementConnection().targetBlock();
            if (firstBlock) {
                list = list.concat(firstBlock.getDescendants(true));
            }
        }
        return list && list.filter(function (block) { return block.varDecl; });
    }
    /**
     * Finds all global variables and returns them.
     * @param {!Blockly.Workspace} workspace Workspace of the global variables.
     * @return {!Array.<!Blockly.VariableModel>} The global variables or null, if none are declared.
     */
    function getGlobalVars(workspace) {
        var startBlocks = workspace.getTopBlocks(false).filter(function (block) { return block.type.indexOf("start") >= 0; });
        var globalVars = [];
        if (startBlocks.length >= 1) {
            var declBlock = startBlocks[0].getFirstStatementConnection() &&
                startBlocks[0].getFirstStatementConnection().targetBlock();
            while (declBlock) {
                if (declBlock.varDecl) {
                    globalVars.push(declBlock.variable_);
                }
                declBlock = declBlock && declBlock.getNextBlock();
            }
        }
        return globalVars;
    }
    function setStyle(thisBlock, scopeType) {
        switch (scopeType) {
            case "GLOBAL":
                thisBlock.setStyle("start_blocks");
                break;
            case "LOCAL":
                thisBlock.setStyle("variable_blocks");
                break;
            case "LOOP":
                thisBlock.setStyle("control_blocks");
                break;
            case "PROC":
                thisBlock.setStyle("procedure_blocks");
                break;
            default:
                thisBlock.setStyle("variable_blocks");
        }
    }
    function setUniqueName(thisBlock, scopeList, opt_name) {
        var newName = getUniqueName(thisBlock, scopeList, opt_name);
        thisBlock.workspace.renameVariableById(thisBlock.id, newName);
        thisBlock.getField("VAR").value_ = newName;
        thisBlock.getField("VAR").setEditorValue_(newName);
        thisBlock.getField("VAR").forceRerender();
    }
});
//# sourceMappingURL=nepo.variables.js.map