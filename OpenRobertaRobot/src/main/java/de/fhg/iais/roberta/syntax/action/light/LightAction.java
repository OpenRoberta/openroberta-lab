package de.fhg.iais.roberta.syntax.action.light;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.ILightMode;
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

@NepoBasic(name = "LIGHT_ACTION", category = "ACTOR", blocklyNames = {"robActions_led_on", "sim_LED_on", "robActions_brickLight_on", "robActions_sensorLight_on"})
public final class LightAction extends Action {
    private static List<Field> fields;
    private static boolean isActor;
    private static boolean isBlink;
    public final Expr rgbLedColor;
    public final IBrickLedColor color;
    public final ILightMode mode;
    public final String port;

    public LightAction(String port, IBrickLedColor color, ILightMode mode, Expr rgbLedColor, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(mode != null);
        this.rgbLedColor = rgbLedColor;
        this.color = color;
        this.port = port;
        this.mode = mode;
        setReadOnly();
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase ledColor = helper.extractValue(values, new ExprParam(BlocklyConstants.COLOR, BlocklyType.COLOR));
        fields = Jaxb2Ast.extractFields(block, (short) 3);
        isActor = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT, BlocklyConstants.EMPTY_PORT).equals(BlocklyConstants.EMPTY_PORT);
        isBlink = Jaxb2Ast.extractField(fields, BlocklyConstants.SWITCH_STATE, BlocklyConstants.DEFAULT).equals(BlocklyConstants.DEFAULT);
        String port =
            isActor
                ? Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.EMPTY_PORT)
                : Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT, BlocklyConstants.EMPTY_PORT);

        String mode =
            isBlink
                ? Jaxb2Ast.extractField(fields, BlocklyConstants.SWITCH_BLINK, BlocklyConstants.DEFAULT)
                : Jaxb2Ast.extractField(fields, BlocklyConstants.SWITCH_STATE, BlocklyConstants.DEFAULT);
        String color = Jaxb2Ast.extractField(fields, BlocklyConstants.SWITCH_COLOR, BlocklyConstants.DEFAULT);
        return new LightAction(Jaxb2Ast.sanitizePort(port), factory.getBrickLedColor(color), factory.getBlinkMode(mode), Jaxb2Ast.convertPhraseToExpr(ledColor), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        if ( !this.color.toString().equals(BlocklyConstants.DEFAULT) ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SWITCH_COLOR, this.color.toString());
        }
        if ( !this.mode.toString().equals(BlocklyConstants.DEFAULT) ) {
            Ast2Jaxb.addField(jaxbDestination, isBlink ? BlocklyConstants.SWITCH_BLINK : BlocklyConstants.SWITCH_STATE, this.mode.toString());
        }
        if ( !this.port.toString().equals(BlocklyConstants.EMPTY_PORT) ) {
            Ast2Jaxb.addField(jaxbDestination, isActor ? BlocklyConstants.ACTORPORT : BlocklyConstants.SENSORPORT, this.port.toString());
        }
        if ( !this.rgbLedColor.toString().contains("EmptyExpr [defVal=COLOR]") ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.COLOR, this.rgbLedColor);
        }
        return jaxbDestination;

    }

    @Override
    public String toString() {
        return "LightAction [" + this.port + ", " + this.mode + ", " + this.color + ", " + this.rgbLedColor + "]";
    }
}