package de.fhg.iais.roberta.syntax.action.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robactions_display_set_colour</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * stopping every movement of the robot.<br/>
 * <br/>
 */
@NepoPhrase(containerType = "DISPLAY_SET_COLOUR_ACTION")
public class DisplaySetColourAction<V> extends Action<V> implements WithUserDefinedPort<V> {
    @NepoValue(name = BlocklyConstants.COLOR, type = BlocklyType.COLOR)
    public final Expr<V> color;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public DisplaySetColourAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> color, String port, Hide hide) {
        super(kind, properties, comment);
        Assert.notNull(color);
        Assert.nonEmptyString(port);
        this.hide = hide;
        this.color = color;
        this.port = port;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
