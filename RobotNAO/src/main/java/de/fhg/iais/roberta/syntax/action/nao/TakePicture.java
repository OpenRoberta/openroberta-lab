package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Camera;
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
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "TAKE_PICTURE", category = "ACTOR", blocklyNames = {"naoActions_takePicture"})
public final class TakePicture<V> extends Action<V> {

    public final Camera camera;
    public final Expr<V> pictureName;

    public TakePicture(Camera camera, Expr<V> pictureName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        Assert.notNull(camera, "Missing camera in TakePicture block!");
        Assert.isTrue(pictureName != null);
        this.camera = camera;
        this.pictureName = pictureName;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "TakePicture [" + this.camera + ", " + this.pictureName + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        String camera = Jaxb2Ast.extractField(fields, BlocklyConstants.CAMERA);
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.FILENAME, BlocklyType.NUMBER_INT));

        return new TakePicture<>(Camera.get(camera), Jaxb2Ast.convertPhraseToExpr(msg), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.CAMERA, this.camera.getValues()[0]);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FILENAME, this.pictureName);

        return jaxbDestination;
    }
}