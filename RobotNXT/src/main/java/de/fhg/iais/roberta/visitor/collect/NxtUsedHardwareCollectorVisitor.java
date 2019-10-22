package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public final class NxtUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor {

    public NxtUsedHardwareCollectorVisitor(UsedHardwareBean.Builder builder, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, ConfigurationAst configuration) {
        super(builder, configuration);
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.builder.addUsedSensor(new UsedSensor(lightAction.getPort(), SC.HT_COLOR, "COLOR"));
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        super.visitVolumeAction(volumeAction);
        this.builder.setVolumeVariableNeeded(true);
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        super.visitToneAction(toneAction);
        this.builder.setVolumeVariableNeeded(true);
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        super.visitPlayNoteAction(playNoteAction);
        this.builder.setVolumeVariableNeeded(true);
        return null;
    }
    /*TODO: rewrite it in a nicer way, check why it is not detecting inserted arrays, fix sensors
    public Void generateArrTmpVar(Expr<Void> expr) {
        String type;
        String defVal;
        if ( expr.toString().contains("NUMBER") ) {
            type = "float";
            defVal = "0";
        } else if ( expr.toString().contains("STRING") ) {
            type = "string";
            defVal = "";
        } else if ( expr.toString().contains("BOOLEAN") ) {
            type = "bool";
            defVal = "true";
        } else {
            type = "int";
            defVal = "0";
        }
    
        if ( !expr.getVarType().toString().contains("ARRAY") ) {
            this.tmpArrVarCount += 1;
            String str = expr.toString().replaceAll("defVal=ARRAY", defVal);
            String[] val = StringUtils.substringsBetween(str.substring(str.indexOf("ListCreate") + 15, str.length() - 1), "[", "]");
            String values = "";
            boolean first = true;
            for ( String value : val ) {
                if ( first ) {
                    first = false;
                } else {
                    values += ", ";
                }
                if ( expr.toString().contains("STRING") ) {
                    values += "\"" + value + "\"";
                } else {
                    values += value;
                }
            }
    
            this.tmpArrVar += "\n" + type + " __tmpArr" + this.tmpArrVarCount + "[] = {" + values + "};";
        }
        return null;
    }
    
    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        List<Expr<Void>> param = mathOnListFunct.getParam();
        for ( Expr<Void> expr : param ) {
            expr.visit(this);
        }
        generateArrTmpVar(mathOnListFunct.getParam().get(0));
        return null;
    
    }
    
    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        generateArrTmpVar(lengthOfIsEmptyFunct.getParam().get(0));
        return null;
    }
    
    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        int listsInLine = StringUtils.countMatches(indexOfFunct.getParam().get(1).toString(), "ListCreate");
        generateArrTmpVar(indexOfFunct.getParam().get(0));
        for ( int i = 0; i < listsInLine; i++ ) {
            generateArrTmpVar(indexOfFunct.getParam().get(1));
        }
        return null;
    }
    
    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        int listsInLine = StringUtils.countMatches(listGetIndex.getParam().get(1).toString(), "ListCreate");
        generateArrTmpVar(listGetIndex.getParam().get(0));
        for ( int i = 0; i < listsInLine; i++ ) {
            generateArrTmpVar(listGetIndex.getParam().get(1));
        }
        return null;
    }
    
    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        int listsInLine = StringUtils.countMatches(listSetIndex.getParam().get(0).toString(), "ListCreate");
        generateArrTmpVar(listSetIndex.getParam().get(1));
        for ( int i = 0; i < listsInLine; i++ ) {
            generateArrTmpVar(listSetIndex.getParam().get(0));
        }
        return null;
    }*/
}