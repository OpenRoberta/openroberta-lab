package de.fhg.iais.roberta.syntax.action.light;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoBasic(name = "LIGHT_STATUS_ACTION", category = "ACTOR", blocklyNames = {"robActions", "robActions_brickLight_reset", "robActions_brickLight_off", "mbedActions_leds_off", "robActions_led_off"})
public final class LightStatusAction extends Action implements WithUserDefinedPort {
    public final Status status;
    public final String userDefinedPort;

    public LightStatusAction(String userDefinedPort, Status status, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(status != null);
        this.status = status;
        this.userDefinedPort = userDefinedPort;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "LightStatusAction [" + this.userDefinedPort + ", " + this.status + "]";
    }

    @Override
    public String getUserDefinedPort() {
        return this.userDefinedPort;
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        Status status = LightStatusAction.Status.RESET;
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT, "0");
        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_BRICK_LIGHT_OFF)
            || block.getType().equals("mbedActions_leds_off")
            || block.getType().equals("robActions_leds_off") ) {
            status = LightStatusAction.Status.OFF;
        }
        return new LightStatusAction(Jaxb2Ast.sanitizePort(port), status, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        if ( !this.userDefinedPort.toString().equals("0") ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, getUserDefinedPort().toString());
        }
        return jaxbDestination;
    }

    /**
     * Status in which user can set the lights.
     */
    public static enum Status {
        OFF, RESET;
    }
}
