package de.fhg.iais.roberta.syntax.lang.blocksequence;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * This class stores the information of the location of the top block in a blockly program.
 */
@NepoBasic(name = "LOCATION", category = "HELPER", blocklyNames = {})
public final class Location extends Task {
    public final String x;
    public final String y;

    public Location(String x, String y) {
        super(new BlocklyProperties("t", "t", true, false, false, false, false, true, false, false, null));
        Assert.isTrue(!x.equals("") && !y.equals(""));
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public Assoc getAssoc() {
        return null;
    }

    @Override
    public String toString() {
        return "Location [x=" + this.x + ", y=" + this.y + "]";
    }

    @Override
    public Block ast2xml() {
        return null;
    }

}
