define(["require", "exports", "blockly", "nepo.constants.extensions", "nepo.constants.mixins", "nepo.procedures", "nepo.variables"], function (require, exports, Blockly, NepoExt, NepoMix, Procedures, Variables) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    Blockly.Extensions.registerMutators("controls_if_mutator", "mutatorPlus", NepoMix.CONTROLS_IF_MUTATOR_MIXIN);
    Blockly.Extensions.registerMutators("controls_wait_for_mutator", "mutatorPlus", NepoMix.CONTROLS_WAIT_FOR_MUTATOR_MIXIN);
    Blockly.Extensions.registerMutators("math_is_divisibleby_mutator", null, NepoMix.IS_DIVISIBLEBY_MUTATOR_MIXIN, NepoExt.IS_DIVISIBLE_MUTATOR_EXTENSION);
    Blockly.Extensions.registerMutators("text_join_mutator", "mutatorPlus", NepoMix.TEXT_JOIN_MUTATOR_MIXIN, NepoExt.TEXT_JOIN_EXTENSION);
    Blockly.Extensions.registerMutators("variable_minus_mutator", "mutatorMinus", Variables.VARIABLE_DECLARATION_MIXIN);
    Blockly.Extensions.registerMutators("variable_plus_mutator", "mutatorPlus", Variables.VARIABLE_PLUS_MUTATOR_MIXIN);
    Blockly.Extensions.register("controls_if_tooltip", NepoExt.CONTROLS_IF_TOOLTIP_EXTENSION);
    Blockly.Extensions.register("datatype_dropdown_validator_extension", NepoExt.DATATYPE_DROPDOWN_VALIDATOR_EXTENSION);
    Blockly.Extensions.register("parent_tooltip_extension", NepoExt.COMMON_PARENT_TOOLTIP_EXTENSION);
    Blockly.Extensions.register("internal_variable_declaration_extension", Variables.INTERNAL_VARIABLE_DECLARATION_EXTENSION);
    Blockly.Extensions.register("procedure_extension", Procedures.PROCEDURE_EXTENSION);
    Blockly.Extensions.register("procedure_call_extension", Procedures.PROCEDURE_CALL_EXTENSION);
    Blockly.Extensions.register("text_comments", NepoExt.TEXT_COMMENTS_EXTENSION);
    Blockly.Extensions.register("text_comment_validator", NepoExt.TEXT_COMMENTS_VALIDATOR);
    Blockly.Extensions.register("text_quotes", NepoExt.TEXT_QUOTES_EXTENSION);
    Blockly.Extensions.register("tooltip_extension", NepoExt.COMMON_TOOLTIP_EXTENSION);
    Blockly.Extensions.register("variable_declaration_extension", Variables.VARIABLE_DECLARATION_EXTENSION);
    Blockly.Extensions.register("variable_scope_extension", Variables.VARIABLE_SCOPE_EXTENSION);
    Blockly.Extensions.registerMixin('common_type_mixin', NepoMix.COMMON_TYPE_MIXIN);
    Blockly.Extensions.registerMixin("variable_mixin", Variables.VARIABLE_MIXIN);
});
//# sourceMappingURL=nepo.extensions.js.map