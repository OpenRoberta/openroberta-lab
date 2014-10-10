package de.fhg.iais.roberta.ast.transformer;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Comment;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.DbcException;

public class AstJaxbTransformer<V> {

    public Block astToBlock(Phrase<V> astSource) {
        String fieldValue;
        Phrase<V> value;
        String blockType;
        Block jaxbDestination;
        switch ( astSource.getKind() ) {
            case BOOL_CONST:
                jaxbDestination = new Block();
                fieldValue = String.valueOf(((BoolConst<V>) astSource).isValue()).toUpperCase();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "BOOL", fieldValue);

                return jaxbDestination;

            case PICK_COLOR_CONST:
                jaxbDestination = new Block();
                fieldValue = ((ColorConst<V>) astSource).getValue().getHex();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "COLOUR", fieldValue);

                return jaxbDestination;

            case MATH_CONST:
                jaxbDestination = new Block();
                fieldValue = ((MathConst<V>) astSource).getMathConst().name();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "CONSTANT", fieldValue);

                return jaxbDestination;

            case NULL_CONST:
                jaxbDestination = new Block();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                return jaxbDestination;

            case STRING_CONST:
                jaxbDestination = new Block();
                fieldValue = ((StringConst<V>) astSource).getValue();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "TEXT", fieldValue);

                return jaxbDestination;

            case NUM_CONST:
                jaxbDestination = new Block();
                fieldValue = ((NumConst<V>) astSource).getValue();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "NUM", fieldValue);

                return jaxbDestination;

            case VAR:
                jaxbDestination = new Block();
                fieldValue = ((Var<V>) astSource).getValue();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "VAR", fieldValue);

                return jaxbDestination;

            case UNARY:
                jaxbDestination = new Block();
                value = ((Unary<V>) astSource).getExpr();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                if ( blockType.equals("math_single") ) {
                    fieldValue = ((Unary<V>) astSource).getOp().name();
                    addField(jaxbDestination, "OP", fieldValue);
                    addValue(jaxbDestination, "NUM", value);
                } else {
                    addValue(jaxbDestination, "BOOL", value);
                }
                return jaxbDestination;

            case CLEAR_DISPLAY_ACTION:
                jaxbDestination = new Block();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                return jaxbDestination;

            case DRIVE_ACTION:
                jaxbDestination = new Block();
                DriveAction<V> driveAction = (DriveAction<V>) astSource;
                fieldValue = driveAction.getDirection().name();
                value = driveAction.getParam().getSpeed();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "DIRECTION", fieldValue);
                addValue(jaxbDestination, "POWER", value);
                if ( driveAction.getParam().getDuration() != null ) {
                    addValue(jaxbDestination, driveAction.getParam().getDuration().getType().name(), driveAction.getParam().getDuration().getValue());
                }

                return jaxbDestination;

            case LIGHT_ACTION:
                jaxbDestination = new Block();
                LightAction<V> lightAction = (LightAction<V>) astSource;
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "SWITCH_COLOR", lightAction.getColor().name());
                addField(jaxbDestination, "SWITCH_BLINK", lightAction.isBlinkingOnOff());

                return jaxbDestination;

            case LIGHT_STATUS_ACTION:
                jaxbDestination = new Block();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                return jaxbDestination;

            case STOP_ACTION:
                jaxbDestination = new Block();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                return jaxbDestination;

            case MOTOR_GET_POWER_ACTION:
                jaxbDestination = new Block();
                fieldValue = ((MotorGetPowerAction<V>) astSource).getPort().name();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "MOTORPORT", fieldValue);

                return jaxbDestination;

            case MOTOR_ON_ACTION:
                jaxbDestination = new Block();
                MotorOnAction<V> motorOnAction = (MotorOnAction<V>) astSource;
                fieldValue = motorOnAction.getPort().name();
                value = motorOnAction.getParam().getSpeed();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "MOTORPORT", fieldValue);
                addValue(jaxbDestination, "POWER", value);
                if ( motorOnAction.getParam().getDuration() != null ) {
                    addField(jaxbDestination, "MOTORROTATION", motorOnAction.getDurationMode().name());
                    addValue(jaxbDestination, "VALUE", motorOnAction.getDurationValue());
                }

                return jaxbDestination;

            case MOTOR_SET_POWER_ACTION:
                jaxbDestination = new Block();
                MotorSetPowerAction<V> motorSetPowerAction = (MotorSetPowerAction<V>) astSource;
                fieldValue = motorSetPowerAction.getPort().name();
                value = motorSetPowerAction.getPower();
                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "MOTORPORT", fieldValue);
                addValue(jaxbDestination, "POWER", value);

                return jaxbDestination;

            case MOTOR_STOP_ACTION:
                jaxbDestination = new Block();
                MotorStopAction<V> motorStopAction = (MotorStopAction<V>) astSource;
                fieldValue = motorStopAction.getPort().name();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "MOTORPORT", fieldValue);
                addField(jaxbDestination, "MODE", motorStopAction.getMode().name());

                return jaxbDestination;

            case PLAY_FILE_ACTION:
                jaxbDestination = new Block();
                fieldValue = ((PlayFileAction<V>) astSource).getFileName();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "FILE", fieldValue);

                return jaxbDestination;

            case SHOW_PICTURE_ACTION:
                jaxbDestination = new Block();
                fieldValue = ((ShowPictureAction<V>) astSource).getPicture();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);
                addField(jaxbDestination, "PICTURE", fieldValue);
                addValue(jaxbDestination, "X", ((ShowPictureAction<V>) astSource).getX());
                addValue(jaxbDestination, "Y", ((ShowPictureAction<V>) astSource).getY());

                return jaxbDestination;

            case SHOW_TEXT_ACTION:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addValue(jaxbDestination, "OUT", ((ShowTextAction<V>) astSource).getMsg());
                addValue(jaxbDestination, "COL", ((ShowTextAction<V>) astSource).getX());
                addValue(jaxbDestination, "ROW", ((ShowTextAction<V>) astSource).getY());

                return jaxbDestination;

            case TONE_ACTION:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addValue(jaxbDestination, "FREQUENCE", ((ToneAction<V>) astSource).getFrequency());
                addValue(jaxbDestination, "DURATION", ((ToneAction<V>) astSource).getDuration());

                return jaxbDestination;

            case TURN_ACTION:
                jaxbDestination = new Block();

                TurnAction<V> turnAction = (TurnAction<V>) astSource;
                fieldValue = turnAction.getDirection().name();
                value = turnAction.getParam().getSpeed();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "DIRECTION", fieldValue);
                addValue(jaxbDestination, "POWER", value);

                if ( turnAction.getParam().getDuration() != null ) {
                    addValue(jaxbDestination, turnAction.getParam().getDuration().getType().name(), turnAction.getParam().getDuration().getValue());
                }

                return jaxbDestination;

            case VOLUME_ACTION:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                if ( ((VolumeAction<V>) astSource).getMode() == VolumeAction.Mode.SET ) {
                    addValue(jaxbDestination, "VOLUME", ((VolumeAction<V>) astSource).getVolume());
                }

                return jaxbDestination;

            case EMPTY_EXPR:
            case LOCATION:
                return null;
            default:
                throw new DbcException("Invalid AST object!");
        }

    }

    private void addValue(Block block, String name, Phrase<V> value) {
        if ( value.getKind() != Kind.EMPTY_EXPR ) {
            Value blockValue = new Value();
            blockValue.setName(name);
            blockValue.setBlock(astToBlock(value));
            block.getValue().add(blockValue);
        }
    }

    private void addField(Block block, String name, String value) {
        Field field = new Field();
        field.setName(name);
        field.setValue(value);
        block.getField().add(field);
    }

    private void setProperties(Phrase<V> astObject, Block block, String type) {
        block.setType(type);
        block.setId(astObject.getProperty().getBlocklyId());
        setDisabled(astObject, block);
        setCollapsed(astObject, block);
        setInline(astObject, block);
    }

    private void setInline(Phrase<V> astObject, Block block) {
        if ( astObject.getProperty().isInline() != null ) {
            block.setInline(astObject.getProperty().isInline());
        }
    }

    private void setCollapsed(Phrase<V> astObject, Block block) {
        if ( astObject.getProperty().isCollapsed() ) {
            block.setCollapsed(astObject.getProperty().isCollapsed());
        }
    }

    private void setDisabled(Phrase<V> astObject, Block block) {
        if ( astObject.getProperty().isDisabled() ) {
            block.setDisabled(astObject.getProperty().isDisabled());
        }
    }

    private void addComment(Phrase<V> astObject, Block block) {
        if ( astObject.getComment() != null ) {
            Comment comment = new Comment();
            comment.setValue(astObject.getComment().getComment());
            comment.setPinned(astObject.getComment().isPinned());
            comment.setH(astObject.getComment().getHeight());
            comment.setW(astObject.getComment().getWidth());
            block.setComment(comment);
        }
    }
}