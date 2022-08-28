package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.mbed.DisplayImageMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "DISPLAY_IMAGE_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_display_image"})
public final class DisplayImageAction extends Action {

    public final DisplayImageMode displayImageMode;
    public final Expr valuesToDisplay;

    public DisplayImageAction(BlocklyProperties properties, DisplayImageMode displayImageMode, Expr valuesToDisplay) {
        super(properties);
        Assert.isTrue(displayImageMode != null && valuesToDisplay != null);
        this.displayImageMode = displayImageMode;
        this.valuesToDisplay = valuesToDisplay;
        setReadOnly();
    }

    /**
     * @return {@link Expr} image(s) to be displayed.
     */
    public Expr getValuesToDisplay() {
        return this.valuesToDisplay;
    }

    @Override
    public String toString() {
        return "DisplayImageAction [" + this.displayImageMode + ", " + this.valuesToDisplay + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        String mode = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
        Phrase image = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.STRING));
        return new DisplayImageAction(Jaxb2Ast.extractBlocklyProperties(block), DisplayImageMode.get(mode), Jaxb2Ast.convertPhraseToExpr(image));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setType(this.displayImageMode.name());

        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.displayImageMode.name());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.valuesToDisplay);

        return jaxbDestination;

    }
}
