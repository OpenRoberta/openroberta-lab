package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.action.nao.Modus;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "HAND", category = "ACTOR", blocklyNames = {"naoActions_hand"})
public final class Hand<V> extends Action<V> {

    public final TurnDirection turnDirection;
    public final Modus modus;

    public Hand(TurnDirection turnDirection, Modus modus, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(turnDirection, "Missing turn direction in Hand block!");
        Assert.notNull(modus, "Missing modus in Hand block!");
        this.turnDirection = turnDirection;
        this.modus = modus;
        setReadOnly();
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);

        String turnDirection = Jaxb2Ast.extractField(fields, BlocklyConstants.SIDE);
        String modus = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);

        return new Hand<V>(TurnDirection.get(turnDirection), Modus.get(modus), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public String toString() {
        return "Hand [" + this.turnDirection + ", " + this.modus + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SIDE, this.turnDirection.toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.modus.getValues()[0]);

        return jaxbDestination;
    }
}