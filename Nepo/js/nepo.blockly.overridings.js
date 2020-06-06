define(["require", "exports", "blockly", "nepo.mutator.plus", "nepo.mutator.minus", "nepo.variables", "utils/nepo.logger"], function (require, exports, Blockly, nepo_mutator_plus_1, nepo_mutator_minus_1, Variables, nepo_logger_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var LOG = new nepo_logger_1.Log();
    Blockly.BlockSvg.prototype.getIcons = function () {
        var icons = [];
        if (this.mutatorPlus) {
            icons.push(this.mutatorPlus);
        }
        if (this.mutatorMinus) {
            icons.push(this.mutatorMinus);
        }
        if (this.commentIcon_) {
            icons.push(this.commentIcon_);
        }
        if (this.warning) {
            icons.push(this.warning);
        }
        return icons;
    };
    /**
    * Give this block a mutator.
    * @param {Blockly.Mutator} mutator A mutator instance or null to remove.
    */
    Blockly.BlockSvg.prototype.setMutator = function (mutator) {
        if (this.mutatorPlus && mutator instanceof nepo_mutator_plus_1.MutatorPlus && this.mutatorPlus != mutator) {
            this.mutatorPlus.dispose();
        }
        if (this.mutatorMinus && mutator instanceof nepo_mutator_minus_1.MutatorMinus && this.mutatorMinus != mutator) {
            this.mutatorMinus.dispose();
        }
        if (mutator) {
            mutator.setBlock(this);
            if (mutator instanceof nepo_mutator_plus_1.MutatorPlus) {
                this.mutatorPlus = mutator;
            }
            else if (mutator instanceof nepo_mutator_minus_1.MutatorMinus) {
                this.mutatorMinus = mutator;
            }
            mutator.createIcon();
        }
        if (this.rendered) {
            this.render();
            // Adding or removing a mutator icon will cause the block to change shape.
            this.bumpNeighbours();
        }
    };
    Blockly.Extensions.apply = function (name, block, isMutator) {
        var extensionFn = Blockly.Extensions.ALL_[name];
        if (typeof extensionFn != 'function') {
            return; //throw Error('Error: Extension "' + name + '" not found.');
        }
        if (isMutator) {
            // Fail early if the block already has mutation properties.
            Blockly.Extensions.checkNoMutatorProperties_(name, block);
        }
        extensionFn.apply(block);
        if (isMutator) {
            var errorPrefix = 'Error after applying mutator "' + name + '": ';
            Blockly.Extensions.checkBlockHasMutatorProperties_(errorPrefix, block);
        }
    };
    Blockly.Extensions.registerMutators = function (name, mutator, mixinObj, opt_helperFn) {
        var errorPrefix = 'Error when registering mutator "' + name + '": ';
        // Sanity check the mixin object before registering it.
        Blockly.Extensions.checkHasFunction_(errorPrefix, mixinObj["domToMutation"], 'domToMutation');
        Blockly.Extensions.checkHasFunction_(errorPrefix, mixinObj["mutationToDom"], 'mutationToDom');
        var hasMutator = mutator != undefined && (mutator == "mutatorPlus" || mutator == "mutatorMinus");
        if (opt_helperFn && (typeof opt_helperFn != 'function')) {
            throw Error('Extension "' + name + '" is not a function');
        }
        // Sanity checks passed.
        Blockly.Extensions.register(name, function () {
            if (hasMutator) {
                if (!nepo_mutator_plus_1.MutatorPlus || !nepo_mutator_minus_1.MutatorMinus) {
                    throw Error(errorPrefix + 'Missing require for Blockly.Mutator');
                }
                if (mutator === "mutatorPlus") {
                    this.setMutator(new nepo_mutator_plus_1.MutatorPlus());
                }
                else if (mutator === "mutatorMinus") {
                    this.setMutator(new nepo_mutator_minus_1.MutatorMinus());
                    this.setNextStatement(false);
                }
            }
            // Mixin the object.
            this.mixin(mixinObj, true);
            if (opt_helperFn) {
                opt_helperFn.apply(this);
            }
        });
    };
    Blockly.VariableMap.prototype.createVariable = function (name, opt_type, opt_id) {
        var variable = this.getVariable(name, opt_type);
        if (variable) {
            if (opt_id && variable.getId() != opt_id) {
                // this is ok and may happen in different scopes
            }
            else {
                // The variable already exists and has the same ID.
                return variable;
            }
        }
        if (opt_id && this.getVariableById(opt_id)) {
            throw Error('Variable id, "' + opt_id + '", is already in use.');
        }
        var id = opt_id || Blockly.utils.genUid();
        var type = opt_type || '';
        variable = new Blockly.VariableModel(this.workspace, name, type, id);
        var variables = this.variableMap_[type] || [];
        variables.push(variable);
        // Delete the list of variables of this type, and re-add it so that
        // the most recent addition is at the end.
        // This is used so the toolbox's set block is set to the most recent variable.
        delete this.variableMap_[type];
        this.variableMap_[type] = variables;
        LOG.warn("create variable", variable.getId(), variable.name);
        return variable;
    };
    Blockly.FieldVariable.prototype.doClassValidation_ = function (opt_newValue) {
        if (opt_newValue === null) {
            return null;
        }
        var newId = /** @type {string} */ (opt_newValue);
        var variable = Blockly.Variables.getVariable(this.sourceBlock_.workspace, newId);
        if (!variable) {
            console.warn('Variable id doesn\'t point to a real variable! ' +
                'ID was ' + newId);
            return null;
        }
        // Type Checks. Not wanted for Nepo
        return newId;
    };
    Blockly.FieldVariable.dropdownCreate = function () {
        if (!this.variable_) {
            throw Error('Tried to call dropdownCreate on a variable field with no' +
                ' variable selected.');
        }
        var variableModelList = [];
        if (this.sourceBlock_ && this.sourceBlock_.workspace) {
            variableModelList = Variables.getUniqueVariables(this.sourceBlock_.workspace);
        }
        var options = [];
        variableModelList.forEach(function (variable) {
            return options.push([variable.name, variable.getId()]);
        });
        return options;
    };
    Blockly.FieldVariable.prototype.fromXml = function (fieldElement) {
        var id = fieldElement.getAttribute('id');
        var variableName = fieldElement.textContent;
        // 'variabletype' should be lowercase, but until July 2019 it was sometimes
        // recorded as 'variableType'.  Thus we need to check for both.
        var variableType = fieldElement.getAttribute('variabletype') ||
            fieldElement.getAttribute('variableType') || '';
        var variable = Blockly.Variables.getOrCreateVariablePackage(this.sourceBlock_.workspace, id, variableName, variableType);
        // This should never happen :)
        if (variableType != null && variableType !== variable.type) {
            throw Error('Serialized variable type with id \'' +
                variable.getId() + '\' had type ' + variable.type + ', and ' +
                'does not match variable field that references it: ' +
                Blockly.Xml.domToText(fieldElement) + '.');
        }
        // Nepo allows sometimes multiple Datatypes
        this.sourceBlock_.updateDataType(null);
        this.setValue(variable.getId());
    };
    Blockly.VariableMap.prototype.renameVariable = function (variable, newName) {
        //var type = variable.type;
        var conflictVar = null; // TODO if this is ok for all cases //this.getVariable(newName, type);
        var blocks = this.workspace.getAllBlocks(false);
        Blockly.Events.setGroup(true);
        try {
            // The IDs may match if the rename is a simple case change (name1 -> Name1).
            if (!conflictVar || conflictVar.getId() == variable.getId()) {
                this.renameVariableAndUses_(variable, newName, blocks);
            }
            else {
                this.renameVariableWithConflict_(variable, newName, conflictVar, blocks);
            }
        }
        finally {
            Blockly.Events.setGroup(false);
        }
    };
    Blockly.WorkspaceSvg.prototype.createVariable = function (name, opt_type, opt_id) {
        var newVar = Blockly.WorkspaceSvg.superClass_.createVariable.call(this, name, opt_type, opt_id);
        //this.refreshToolboxSelection(); // for nepo this is not used!
        return newVar;
    };
    Blockly.FieldDropdown.prototype.initView = function () {
        if (this.shouldAddBorderRect_()) {
            this.createBorderRect_();
        }
        else {
            this.clickTarget_ = this.sourceBlock_.getSvgRoot();
        }
        this.createTextElement_();
        this.imageElement_ = /** @type {!SVGImageElement} */
            (Blockly.utils.dom.createSvgElement('image', {}, this.fieldGroup_));
        // do not create an arrow if there is only one entry in the dropdown!
        if (this.getOptions().length >= 2) {
            if (this.getConstants().FIELD_DROPDOWN_SVG_ARROW) {
                this.createSVGArrow_();
            }
            else {
                this.createTextArrow_();
            }
        }
        else {
            // but create an empty "arrow"
            this.arrow_ = (Blockly.utils.dom.createSvgElement('tspan', {}, this.textElement_));
            this.arrow_.appendChild(document.createTextNode(''));
        }
        if (this.borderRect_) {
            Blockly.utils.dom.addClass(this.borderRect_, 'blocklyDropdownRect');
        }
    };
    Blockly.Procedures.isNameUsed = function (name, workspace, opt_exclude) {
        var blocks = workspace.getAllBlocks(false);
        // Iterate through every block and check the name.
        for (var i = 0; i < blocks.length; i++) {
            if (blocks[i] == opt_exclude) {
                continue;
            }
            if (blocks[i].getProcedureDef) {
                var procedureBlock = blocks[i];
                var procName = procedureBlock.getProcedureDef();
                if (Blockly.Names.equals(procName[0], name)) {
                    return true;
                }
            }
        }
        // for Nepo also check if this name is already used as a variable name. Check for opt_exlude to prevend dead locks when called from Variables.
        if (opt_exclude && Variables.getVariablesByName(workspace, name).length > 0) {
            return true;
        }
        return false;
    };
});
//# sourceMappingURL=nepo.blockly.overridings.js.map