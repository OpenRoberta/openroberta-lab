import * as Blockly from "blockly";
import { Log } from "utils/nepo.logger";

const LOG = new Log();

export function flyoutCallback(workspace: Blockly.Workspace): Array<string> {
	// add the new scope block always on top:
	// <block type="variable_scope" > </block>
	var xmlList = [];
	let scope = (Blockly.utils as any).xml.createElement("block");
	scope.setAttribute("type", "variable_scope");
	scope.setAttribute("gap", 16);
	xmlList.push(scope);
	let variableModelList: Array<Blockly.VariableModel> = getUniqueVariables(workspace);
	if (variableModelList.length > 0) {
		variableModelList.forEach(
			variable => {
				if (Blockly.Blocks["variables_set"]) {
					var block = (Blockly.utils as any).xml.createElement("block");
					block.setAttribute("type", "variables_set");
					block.setAttribute("gap", 8);
					block.setAttribute("id", variable.getId());
					block.appendChild(Blockly.Variables.generateVariableFieldDom(variable));
					xmlList.push(block);
				}
				if (Blockly.Blocks["variables_get"]) {
					var block = (Blockly.utils as any).xml.createElement("block");
					block.setAttribute("type", "variables_get");
					block.setAttribute("gap", 16);
					block.appendChild(Blockly.Variables.generateVariableFieldDom(variable));
					xmlList.push(block);
				}
			})
	};
	return xmlList;
}

/**
 * Finds all variables unique by name and returns them. For variables with same names only the first instance found 
 * will be considered. 
 * @param {!Blockly.Workspace} workspace Workspace of the global variables.
 * @return {!Array.<!Blockly.VariableModel>} All variables unique by name or null if no variables are declared.
 */
export function getUniqueVariables(workspace: Blockly.Workspace): Array<Blockly.VariableModel> {
	let prev: Blockly.VariableModel;
	let uniqueList: Array<Blockly.VariableModel> = [];
	workspace.getAllVariables().sort(Blockly.VariableModel.compareByName).forEach((variable: Blockly.VariableModel) => {
		if (!prev || variable.name !== prev.name) {
			uniqueList.push(variable);
		}
		prev = variable;
	})
	return uniqueList;
}


export const INTERNAL_VARIABLE_DECLARATION_EXTENSION = function() {
	this.mixin(INTERNAL_VARIABLE_DECLARATION_MIXIN, true);
	this.varScope = true;
	this.scopeId_ = this.id;
	this.dataType_ = "Number";
	this.scopeType = "LOOP";
	this.varDecl = true;
	this.internalVarDecl = true;
	this.scopeId_ = this.id;
	this.setFieldValue(Blockly.Msg["VARIABLES_LOOP_DEFAULT_NAME"], "VAR");
	this.getField("VAR").setValidator(VARIABLE_DECLARATION_VALIDATOR);
}


export const INTERNAL_VARIABLE_DECLARATION_MIXIN = {
	getScopeId: function() {
		return this.scopeId_;
	},
	getDataType: function() {
		return this.dataType_;
	},
	getVarName: function() {
		return this.getField("VAR").getValue();
	},
	mutationToDom: function() {
		var container = document.createElement('mutation');
		var variable = (this.variable_ && this.variable_.getId());
		container.setAttribute('var_decl', variable);
		return container;
	},
	domToMutation: function(xmlElement) {
		var varDecl = xmlElement.getAttribute('var_decl');
		if (varDecl === this.id) {
			this.variable_ = this.workspace.getVariableById(varDecl);
		} else {
			let name = getUniqueName(this, [], Blockly.Msg["VARIABLES_LOOP_DEFAULT_NAME"]);
			this.variable_ = this.workspace.createVariable(name, "Number", this.id);
			this.getField("VAR").setValue(name);
		}
	},

	onchange: function(e) {
		if (this.isInFlyout) {
			return;
		} else if (e.blockId === this.id && e.type === Blockly.Events.BLOCK_MOVE) {
			checkScope(this);
		}
	},
	dispose: function(healStack, animate) {
		if (this.variable_ && this.workspace.getVariableById(this.variable_.getId())) {
			this.workspace.deleteVariableById(this.variable_.getId());
			LOG.warn("delete loop variable", this);
		}
		(Blockly.BlockSvg as any).prototype.dispose.call(this, !!healStack, animate);
	}
}

export const VARIABLE_DECLARATION_EXTENSION = function() {
	this.previousConnection.setCheck("declaration_only");
	this.setMovable(false);
	let name: string;
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
}

export const VARIABLE_DECLARATION_MIXIN = {
	/**
   * Create XML to represent variable declaration insides.
   * @return {Element} XML storage element.
   * @this Blockly.Block
   */
	mutationToDom: function() {
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
	domToMutation: function(xmlElement) {
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
	getScopeId: function() {
		return this.scopeId_;
	},
	getDataType: function() {
		return this.dataType_;
	},
	getVarName: function() {
		return this.getField("VAR").getValue();
	},
	updateShape_: function(num) {
		if (num == -1) {
			let procBlock: Blockly.Block = this.getSurroundParent();
			let callerList = Blockly.Procedures.getCallers(procBlock.getFieldValue("NAME"), this.workspace);
			let thisVarName = this.variable_.name;
			let thisscopeType = this.variable_.type;
			// remove this variable declaration
			this.workspace.deleteVariableById(this.id);
			// check if the user confirmed the deletion
			let variableTmp = this.workspace.getVariableById(this.id);
			if (!variableTmp) {
				let surroundParent = this.getSurroundParent();
				let parent = this.getParent();
				var nextBlock = this.getNextBlock();
				this.unplug(true, true);
				if (parent === surroundParent && !nextBlock) {
					parent.updateShape_(num);
				} else if (!nextBlock) {
					parent.setNextStatement(false);
				}
				callerList.forEach(caller => (caller as any).removeParam(thisVarName, thisscopeType));
				this.dispose();
			}
		}
	},
	updateDataType: function(dataType) {
		let variableMap: Blockly.VariableMap = this.workspace.getVariableMap().variableMap_;
		let variableList = variableMap[this.dataType_];
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
	setscopeType: function(scopeType: string) {
		this.scopeType = scopeType;
		setStyle(this, this.scopeType);
	},
	dispose: function(healStack, animate) {
		if (this.variable_ && this.workspace.getVariableById(this.variable_.getId())) {
			this.workspace.deleteVariableById(this.variable_.getId());
			LOG.warn("delete loop variable", this);
		}
		(Blockly.BlockSvg as any).prototype.dispose.call(this, !!healStack, animate);
	}
}

const VARIABLE_DECLARATION_VALIDATOR = function(newName: string) {
	if (newName === this.value_) {
		return newName;
	}
	let thisBlock = this.getSourceBlock();
	let scopeVars = [];
	let scopeBlock = thisBlock.workspace.getBlockById((thisBlock as any).getScopeId());
	if (scopeBlock) {
		if (scopeBlock.scopeType === "GLOBAL") {
			scopeVars = getUniqueVariables(scopeBlock.workspace);
		} else {
			scopeVars = getVarScopeList(scopeBlock);
		}
	}
	let name = getUniqueName(thisBlock, scopeVars, newName);
	let varId = thisBlock.variable_ && thisBlock.variable_.getId();
	if (varId) {
		thisBlock.workspace.renameVariableById(varId, name);
	} else {
		thisBlock.workspace.createVariable(name);
	}
	return name;
}

export const VARIABLE_MIXIN = {
	onchange: function() {
		if (this.isInFlyout) {
			return;
		}
		let id = this.getFieldValue("VAR");
		let thisVariable = Blockly.Variables.getVariable(this.workspace, id);
		// correct the type checker of this instance, only necessary, if there are more than one variable with the same name
		if (this.type.indexOf("variables") >= 0) {
			let dataTypes = [];
			let varList = getVariablesByName(this.workspace, thisVariable.name);
			for (let variable in varList) {
				dataTypes.push(varList[variable].type);
			};
			this.updateDataType(dataTypes);
			LOG.info(dataTypes);
		}
		let tmpVariable: Blockly.VariableModel;
		this.setWarningText(null);
		// allow not connected instances of this (no code generation for this anyway)
		if (this.type === "variables_get" && !this.outputConnection) {
			return;
		}
		if (this.type === "variables_set" && !this.getNextBlock() && !this.getPreviousBlock()) {
			return;
		}
		// global variables can appear everywhere
		let varType = this.workspace.getBlockById(this.getField("VAR").variable_.getId()).scopeType;
		if (varType && varType === "GLOBAL") {
			return;
		};
		let valid: boolean = false;
		let surroundParent = this.getSurroundParent();
		if (surroundParent) {
			let surrVarDeclList = getSurrScopeList(this.getSurroundParent());
			tmpVariable = Object.values(surrVarDeclList).find(variable => variable.getId() === thisVariable.getId());
			if (tmpVariable === undefined) {
				let varNameList = getVariablesByName(this.workspace, thisVariable.name);
				for (let a of Object.values(varNameList)) {
					tmpVariable = Object.values(surrVarDeclList).find(variable => variable.getId() === a.getId());
					if (tmpVariable !== undefined) {
						this.getField("VAR").doValueUpdate_(tmpVariable.getId());
						valid = true;
						this.updateDataType(tmpVariable.type);
						break;
					}
				};
			} else {
				valid = true;
				this.updateDataType(tmpVariable.type);
			}
		}
		if (!valid) {
			this.setWarningText((Blockly.Msg as any).VARIABLES_SCOPE_WARNING);
		}
	},
	updateDataType: function(dataType) {
		this.dataType_ = dataType;
		if (this.outputConnection) {
			this.outputConnection.setCheck(dataType);
		} else {
			for (var i = 0, input; (input = this.inputList[i]); i++) {
				if (input.connection) {
					input.connection.setCheck(dataType);
				}
			}
		}
	},
	customContextMenu: function(options) {
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
			} else {
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
}

export const VARIABLE_PLUS_MUTATOR_MIXIN = {
	declare_: false,
	/**
 * Create XML to represent whether a statement list of variable declarations
 * should be present.
 * 
 * @return {Element} XML storage element.
 * @this Blockly.Block
 */
	mutationToDom: function() {
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
	domToMutation: function(xmlElement) {
		this.declare_ = (xmlElement.getAttribute('declare') != 'false');
		if (this.declare_) {
			let variableDeclareStatement = new Blockly.Input(Blockly.NEXT_STATEMENT, "DECL", this, this.makeConnection_(Blockly.NEXT_STATEMENT));
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
	updateShape_: function(num) {
		if (!this.workspace.isDragging || this.workspace.isDragging() || this.workspace.isFlyout) {
			return;
		}
		if (num === 1) {
			Blockly.Events.setGroup(true);
			if (!this.declare_) {
				let variableDeclareStatement = new Blockly.Input(Blockly.NEXT_STATEMENT, "DECL", this, this.makeConnection_(Blockly.NEXT_STATEMENT));
				this.inputList.splice(1, 0, variableDeclareStatement);
				// making sure only declarations can connect to the statement list
				this.getInput("DECL").connection.setCheck('declaration_only');
				this.declare_ = true;
			}
			let variableDeclare: Blockly.BlockSvg = this.type.indexOf("procedures") >= 0 ? this.workspace.newBlock('variable_param_declare') : this.workspace.newBlock('variable_declare');
			let scopeVars = getVarScopeList(this);
			if (this.scopeType === "GLOBAL") {
				scopeVars = getUniqueVariables(this.workspace);
			}
			let name: string;
			name = Blockly.Msg["VARIABLES_" + this.scopeType + "_DEFAULT_NAME"];
			setUniqueName(variableDeclare, scopeVars, name);
			(variableDeclare as any).setscopeType(this.scopeType);
			variableDeclare.initSvg();
			variableDeclare.render();
			let connection: Blockly.Connection;
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
			} else {
				connection = this.getInput("DECL").connection;
			}
			connection.connect(variableDeclare.previousConnection);
			checkScope(this);
			if (this.scopeType === "PROC") {
				let callerList = Blockly.Procedures.getCallers(this.getFieldValue("NAME"), this.workspace);
				callerList.forEach(caller => (caller as any).addParam(variableDeclare.getFieldValue("VAR")));
			}
			Blockly.Events.setGroup(false);
		} else if (num == -1) {
			// if the last declaration in the stack has been removed, remove the declaration statement
			this.removeInput("DECL");
			this.declare_ = false;
		}
	},
	// scope extension
	onchange: function(e) {
		// no need to check scope for global vars (start block) nor for blocks in the toolbox's flyout'
		if (this.scopeType === "GLOBAL" || this.isInFlyout) {
			return;
		} else if (e.blockId == this.id && e.type == Blockly.Events.BLOCK_MOVE) {
			checkScope(this);
		}
	}
}

export const VARIABLE_SCOPE_EXTENSION = function() {
	this.varScope = true;
	switch (this.type) {
		case "controls_start":
			this.scopeType = "GLOBAL";
			break;
		case "variable_scope":
			this.scopeType = "LOCAL"
			break;
		case "procedures_defnoreturn":
		case "procedures_defreturn":
			this.scopeType = "PROC"
			break;
		case "controls_for":
		case "controls_forEach":
			this.scopeType = "LOOP"
			break;
		default:
			this.scopeType = "LOCAL"
	}
}


/**
 * Should be called whenever a scope block has been moved to check if the variable names are still valid. If
 * not, the variables (and fields) are renamed. 
 * @param {!Blockly.Block} scopeBlock Block of the scope.
 */
function checkScope(scopeBlock: Blockly.Block) {
	let scopeVars = getVarScopeList(scopeBlock);

	let succDeclBlocks: Array<Blockly.Block> = getSuccDeclList(scopeBlock);
	succDeclBlocks.forEach(block => {
		if (!isUniqueName(block, scopeVars)) {
			setUniqueName(block, scopeVars);
		}
	});
}

function isUniqueName(thisBlock: Blockly.Block, scopeList: Array<Blockly.VariableModel>): boolean {
	let name = thisBlock.getField("VAR") && thisBlock.getField("VAR").getValue();
	for (let variable of Object.values(scopeList)) {
		if (variable.getId() !== thisBlock.id && variable.name === name) {
			return false;
		}
	}
	return true;
};

function getUniqueName(thisBlock: Blockly.Block, scopeList: Array<Blockly.VariableModel>, opt_name?: string) {
	let name = opt_name || (thisBlock.getField("VAR") && thisBlock.getField("VAR").getValue());
	let names: Array<string> = scopeList.filter(variable => {
		if (variable.getId() !== thisBlock.id) {
			return variable;
		}
	}).map(variableModel => variableModel.name);
	let newName = name;
	while (newName && (names.indexOf(newName) >= 0 || Blockly.Procedures.isNameUsed(newName, (thisBlock as any).workspace))) {
		var r = newName.match(/^(.*?)(\d+)$/);
		if (!r) {
			r = newName.match(/^[a-zA-Z]{1}$/);
			if (!r) {
				newName += "2";
			} else {
				// special case variable names in loops, e.g. i,j ...
				newName = Blockly.Variables.generateUniqueName(thisBlock.workspace);
			}
		} else {
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
export function getVariablesByName(workspace: Blockly.Workspace, name: string): Array<Blockly.VariableModel> {
	return workspace.getAllVariables().
		filter(variable => variable.name === name);
}

/**
 * Finds all variables of this blocks' scope. Including global variables and variables of the same scope level.
 * @param {!Blockly.Block} scopeBlock Block of the scope.
 * @return {!Array.<!Blockly.VariableModel>} The variables of the scope.
 */
function getVarScopeList(scopeBlock: Blockly.Block): Array<Blockly.VariableModel> {
	let surrScopeVars = getSurrScopeList(scopeBlock);
	let succScopeVars = getSuccVarList(scopeBlock);
	let varScopeList = surrScopeVars.concat(succScopeVars);
	return varScopeList;
}

function getSurrScopeList(scopeBlock: Blockly.Block): Array<Blockly.VariableModel> {
	let scopeVars = getGlobalVars(scopeBlock.workspace);
	let surroundParent: Blockly.Block = scopeBlock;
	while (!!surroundParent) {
		if ((surroundParent as any).varScope) {
			let declBlock = surroundParent.getFirstStatementConnection() &&
				surroundParent.getFirstStatementConnection().targetBlock();
			// special case internal variable declarations, e.g. in loops
			if ((surroundParent as any).internalVarDecl) {
				declBlock = surroundParent;
			}
			while (declBlock) {
				if ((declBlock as any).varDecl) {
					scopeVars.push((declBlock as any).variable_);

				}
				declBlock = (declBlock as any).getNextBlock();
			}
		}
		surroundParent = surroundParent.getSurroundParent();
	}
	return scopeVars;
}

function getSuccVarList(thisBlock: Blockly.Block): Array<Blockly.VariableModel> {
	return getSuccDeclList(thisBlock).map(block => (block as any).variable_);
}

function getSuccDeclList(thisBlock: Blockly.Block): Array<Blockly.Block> {
	let firstDeclBlock: Blockly.Block;
	let list = [];
	if (thisBlock.getInput("DECL")) {
		firstDeclBlock = thisBlock.getInput("DECL").connection && thisBlock.getInput("DECL").connection.targetBlock();
		list = firstDeclBlock ? firstDeclBlock.getDescendants(true) : [];
		let firstDoBlock = thisBlock.getInput("DO") && thisBlock.getInput("DO").connection && thisBlock.getInput("DO").connection.targetBlock();
		list = list.concat(firstDoBlock ? firstDoBlock.getDescendants(true) : []);
	} else if ((thisBlock as any).internalVarDecl) {
		// special case internal variable declarations, e.g. in loops
		list = [thisBlock];
		let firstBlock = thisBlock.getFirstStatementConnection() && thisBlock.getFirstStatementConnection().targetBlock();
		if (firstBlock) {
			list = list.concat(firstBlock.getDescendants(true));
		}
	}
	return list && list.filter(block => (block as any).varDecl);
}

/**
 * Finds all global variables and returns them.
 * @param {!Blockly.Workspace} workspace Workspace of the global variables.
 * @return {!Array.<!Blockly.VariableModel>} The global variables or null, if none are declared.
 */
function getGlobalVars(workspace: Blockly.Workspace): Array<Blockly.VariableModel> {
	let startBlocks: Array<Blockly.Block> = workspace.getTopBlocks(false).filter(block => block.type.indexOf("start") >= 0);
	let globalVars = [];
	if (startBlocks.length >= 1) {
		let declBlock = startBlocks[0].getFirstStatementConnection() &&
			startBlocks[0].getFirstStatementConnection().targetBlock();
		while (declBlock) {
			if ((declBlock as any).varDecl) {
				globalVars.push((declBlock as any).variable_);
			}
			declBlock = declBlock && (declBlock as any).getNextBlock();
		}
	}
	return globalVars;
}

function setStyle(thisBlock: Blockly.Block, scopeType: string) {
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

function setUniqueName(thisBlock: Blockly.Block, scopeList: Array<Blockly.VariableModel>, opt_name?: string) {
	let newName = getUniqueName(thisBlock, scopeList, opt_name);
	thisBlock.workspace.renameVariableById(thisBlock.id, newName);
	thisBlock.getField("VAR").value_ = newName;
	(thisBlock.getField("VAR") as any).setEditorValue_(newName);
	thisBlock.getField("VAR").forceRerender();
}
