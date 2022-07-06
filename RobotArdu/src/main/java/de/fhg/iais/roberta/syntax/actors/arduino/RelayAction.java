package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.IRelayMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "RELAY_ACTION", category = "ACTOR", blocklyNames = {"robactions_set_relay"})
public final class RelayAction extends Action {
    public final String port;
    public final IRelayMode mode;

    public RelayAction(String port, IRelayMode mode, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(mode != null);
        this.port = port;
        this.mode = mode;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "RelayAction [" + this.port + ", " + this.mode + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.EMPTY_PORT);
        String mode = Jaxb2Ast.extractField(fields, BlocklyConstants.RELAYSTATE, BlocklyConstants.DEFAULT);
        return new RelayAction(Jaxb2Ast.sanitizePort(port), factory.getRelayMode(mode), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port.toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.RELAYSTATE, this.mode.toString());
        return jaxbDestination;

    }
}