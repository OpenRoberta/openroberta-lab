package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robGlobalvariables_declare</b> blocks from Blockly in the AST (abstract syntax tree). Object from this class will generate
 * code for creating a variable.
 */
@NepoBasic(name = "VAR_DECLARATION", category = "EXPR", blocklyNames = {"robLocalVariables_declare", "robGlobalvariables_declare"})
public final class VarDeclaration extends Expr {
    public final BlocklyType typeVar;
    public final String name;
    public final Phrase value;
    public final boolean next;
    public final boolean global;
    public final static String CODE_SAFE_PREFIX = "___";

    public VarDeclaration(
        BlocklyType typeVar,
        String name,
        Phrase value,
        boolean next,
        boolean global,
        BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(!name.equals("") && typeVar != null && value.isReadOnly());
        this.name = Util.sanitizeProgramProperty(name);
        this.typeVar = typeVar;
        this.value = value;
        this.next = next;
        this.global = global;
        setReadOnly();
    }

    public String getCodeSafeName() {
        return CODE_SAFE_PREFIX + this.name;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public BlocklyType getVarType() {
        return this.typeVar;
    }

    @Override
    public String toString() {
        return "VarDeclaration [" + this.typeVar + ", " + this.name + ", " + this.value + ", " + this.next + ", " + this.global + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        boolean isGlobalVariable = block.getType().equals(BlocklyConstants.ROB_LOCAL_VARIABLES_DECLARE) ? false : true;
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        BlocklyType typeVar = BlocklyType.get(Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE));
        String name = Jaxb2Ast.extractField(fields, BlocklyConstants.VAR);
        Phrase expr = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, typeVar));
        boolean next = block.getMutation().isNext();

        return new VarDeclaration(typeVar, name, Jaxb2Ast.convertPhraseToExpr(expr), next, isGlobalVariable, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {

        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setNext(this.next);
        mutation.setDeclarationType(this.typeVar.getBlocklyName());
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VAR, this.name);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.typeVar.getBlocklyName());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);

        return jaxbDestination;
    }

}
