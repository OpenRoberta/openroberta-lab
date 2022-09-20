package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "RECORD_VIDEO", category = "ACTOR", blocklyNames = {"naoActions_recordVideo"})
public final class RecordVideo extends Action {

    @NepoField(name = "RESOLUTION")
    public final String resolution;

    @NepoField(name = "CAMERA")
    public final String camera;

    @NepoValue(name = "DURATION", type = BlocklyType.NUMBER_INT)
    public final Expr duration;

    @NepoValue(name = "FILENAME", type = BlocklyType.STRING)
    public final Expr videoName;

    public RecordVideo(BlocklyProperties properties, String resolution, String camera, Expr duration, Expr videoName) {
        super(properties);
        Assert.notNull(resolution, "Missing resolution in RecordVideo block!");
        Assert.notNull(camera, "Missing camera in RecordVideo block!");
        this.resolution = resolution;
        this.camera = camera;
        this.duration = duration;
        this.videoName = videoName;
        setReadOnly();
    }
}