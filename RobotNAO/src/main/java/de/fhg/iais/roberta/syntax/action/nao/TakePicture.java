package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "TAKE_PICTURE", category = "ACTOR", blocklyNames = {"naoActions_takePicture"})
public final class TakePicture extends Action {

    @NepoField(name = "CAMERA")
    public final String camera;

    @NepoValue(name = "FILENAME", type = BlocklyType.STRING)
    public final Expr pictureName;

    public TakePicture(BlocklyProperties properties, String camera, Expr pictureName) {
        super(properties);
        Assert.notNull(camera, "Missing camera in TakePicture block!");
        Assert.isTrue(pictureName != null);
        this.camera = camera;
        this.pictureName = pictureName;
        setReadOnly();
    }
}