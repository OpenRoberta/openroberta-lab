package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Camera;
import de.fhg.iais.roberta.mode.action.nao.Resolution;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "RECORD_VIDEO", category = "ACTOR", blocklyNames = {"naoActions_recordVideo"})
public final class RecordVideo<V> extends Action<V> {

    @Override
    public String toString() {
        return "RecordVideo [" + this.resolution + ", " + this.camera + ", " + this.duration + ", " + this.videoName + "]";
    }

    public final Resolution resolution;
    public final Camera camera;
    public final Expr<V> duration;
    public final Expr<V> videoName;

    public RecordVideo(Resolution resolution, Camera camera, Expr<V> duration, Expr<V> videoName, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(resolution, "Missing resolution in RecordVideo block!");
        Assert.notNull(camera, "Missing camera in RecordVideo block!");
        this.resolution = resolution;
        this.camera = camera;
        this.duration = duration;
        this.videoName = videoName;
        setReadOnly();
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);

        String resolution = Jaxb2Ast.extractField(fields, BlocklyConstants.RESOLUTION);
        String camera = Jaxb2Ast.extractField(fields, BlocklyConstants.CAMERA);
        Phrase<V> duration = helper.extractValue(values, new ExprParam(BlocklyConstants.DURATION, BlocklyType.NUMBER_INT));
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.FILENAME, BlocklyType.NUMBER_INT));

        return new RecordVideo<>(Resolution.get(resolution), Camera.get(camera), Jaxb2Ast.convertPhraseToExpr(duration), Jaxb2Ast.convertPhraseToExpr(msg), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.RESOLUTION, this.resolution.getValues()[0]);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.CAMERA, this.camera.getValues()[0]);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.DURATION, this.duration);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FILENAME, this.videoName);

        return jaxbDestination;
    }
}