package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
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

@NepoBasic(name = "LED_MATRIX_IMAGE_ACTION", category = "ACTOR", blocklyNames = {"mBotActions_display_image"})
public final class LEDMatrixImageAction<V> extends Action<V> {

    public final String port;
    public final Expr<V> valuesToDisplay;
    public final String displayImageMode;

    public LEDMatrixImageAction(String port, String displayImageMode, Expr<V> valuesToDisplay, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(port != null && displayImageMode != null && valuesToDisplay != null);
        this.port = port;
        this.displayImageMode = displayImageMode;
        this.valuesToDisplay = valuesToDisplay;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "DisplayImageAction [" + this.port + ", " + this.displayImageMode + ", " + this.valuesToDisplay + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        final String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
        String mode = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
        Phrase<V> image = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.STRING));
        return new LEDMatrixImageAction<>(port, mode, Jaxb2Ast.convertPhraseToExpr(image), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setType(this.displayImageMode);
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.displayImageMode);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.valuesToDisplay);

        return jaxbDestination;
    }
}
