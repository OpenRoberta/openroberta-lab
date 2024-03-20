package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IMicrobitVisitor;

public class MicrobitPythonVisitor extends MbedPythonVisitor implements IMicrobitVisitor<Void> {
    public MicrobitPythonVisitor(List<List<Phrase>> programPhrases, ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, robotConfiguration, beans);
    }


    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        switch ( playFileAction.fileName ) {
            case "DADADADUM":
            case "ENTERTAINER":
            case "PRELUDE":
            case "ODE":
            case "NYAN":
            case "RINGTONE":
            case "FUNK":
            case "BLUES":
            case "BIRTHDAY":
            case "WEDDING":
            case "FUNERAL":
            case "PUNCHLINE":
            case "PYTHON":
            case "BADDY":
            case "CHASE":
            case "BA_DING":
            case "WAWAWAWAA":
            case "JUMP_UP":
            case "JUMP_DOWN":
            case "POWER_UP":
            case "POWER_DOWN": {
                this.src.add("music.play(music.", playFileAction.fileName, ")");
                break;
            }
            default: {
                throw new DbcException("Invalid play file name: " + playFileAction.fileName);
            }
        }
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        if ( switchLedMatrixAction.activated.equals("ON") ) {
            this.src.add("microbit.display.on()");
        } else {
            this.src.add("microbit.display.off()");
        }
        return null;
    }
}
