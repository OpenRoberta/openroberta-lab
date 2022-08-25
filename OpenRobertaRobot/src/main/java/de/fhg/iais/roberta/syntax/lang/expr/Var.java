package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
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
 * This class represents the <b>variables_set</b> and <b>variables_get</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will
 * generate code for creating a variable.<br/>
 * <br>
 * User must provide name of the variable and type of the variable, if the variable is created before in the code TypeVar should be <b>NONE</b>. To create an
 * instance from this class use the method {@link #make(BlocklyType, String, boolean, String)}.<br>
 */
@NepoBasic(name = "VAR", category = "EXPR", blocklyNames = {"variables_get"})
public final class Var extends Expr {
    public final BlocklyType typeVar;
    public final String name;
    public final static String CODE_SAFE_PREFIX = "___";

    public Var(BlocklyType typeVar, String value, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(!value.equals("") && typeVar != null);
        this.name = Util.sanitizeProgramProperty(value);
        this.typeVar = typeVar;
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
        return "Var [" + this.name + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        return Jaxb2Ast.extractVar(block);
    }

    @Override
    public Block ast2xml() {

        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setDatatype(getVarType().getBlocklyName());
        jaxbDestination.setMutation(mutation);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VAR, this.name);
        return jaxbDestination;
    }
}
