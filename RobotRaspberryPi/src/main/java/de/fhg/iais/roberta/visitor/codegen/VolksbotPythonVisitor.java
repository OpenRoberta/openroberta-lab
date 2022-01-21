package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.Dummy;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateLeft;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateRight;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepBackward;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepForward;
import de.fhg.iais.roberta.syntax.lang.blocksequence.raspberrypi.MainTaskSimple;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.IVolksbotVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 *
 * @param <T>
 */
public class VolksbotPythonVisitor extends RaspberryPiPythonVisitor implements IVolksbotVisitor<Void> {

    public VolksbotPythonVisitor(List<List<Phrase<Void>>> programPhrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, brickConfiguration, beans);
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.usedGlobalVarInFunctions.clear();
        this.sb.append("#!/usr/bin/python");
        nlIndent();
        nlIndent();
        this.sb.append("from __future__ import absolute_import");
        nlIndent();
        this.sb.append("import sys");
        nlIndent();
        // this.sb.append("sys.path.append('/home/pi/epos4.py')");
        // nlIndent();
        this.sb.append("from epos4 import *");
        nlIndent();
        this.sb.append("import time");
        nlIndent();
        this.sb.append("import gpiozero as gpz");
        nlIndent();
        this.sb.append("import colorzero as cz");
        nlIndent();
        this.sb.append("import math");
        nlIndent();
        nlIndent();
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this
                    .getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.sb.append(helperMethodImpls);
            nlIndent();
            nlIndent();
        }
        this.sb.append("class BreakOutOfALoop(Exception): pass");
        nlIndent();
        this.sb.append("class ContinueLoop(Exception): pass");
        nlIndent();
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        nlIndent();
        this.sb.append("time.sleep(5)");
        nlIndent();
        this.sb.append("Drive(keyhandle, -10, -10)");
        nlIndent();
        this.sb.append("time.sleep(1)");
        nlIndent();
        this.sb.append("Turn(keyhandle, 60, -60)");
        nlIndent();
        this.sb.append("time.sleep(1)");
        nlIndent();
        this.sb.append("Drive(keyhandle, 115, 115)");
        nlIndent();
        this.sb.append("time.sleep(1)");
        nlIndent();
        this.sb.append("Align(keyhandle)");
        nlIndent();
        this.sb.append("time.sleep(1)");
        nlIndent();
        this.sb.append("Drive(keyhandle, -185, -185)");
        nlIndent();
        this.sb.append("# End ");

        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.sb.append("def main():");
        incrIndentation();
        nlIndent();
        this.sb.append("try:");
        incrIndentation();
        nlIndent();
        this.sb.append("pErrorCode = c_uint()");
        nlIndent();
        this.sb.append("keyhandle = 0");
        nlIndent();
        this.sb.append("keyhandle = openDevice()");
        nlIndent();
        this.sb.append("activateCVP(keyhandle, NodeID_1, pErrorCode)");
        nlIndent();
        this.sb.append("activateCVP(keyhandle, NodeID_2, pErrorCode)");
        nlIndent();
        this.sb.append("setState(keyhandle, 'enable')");
        nlIndent();
        this.sb.append("setState(keyhandle, 'quick_stop')");
        nlIndent();
        this.sb.append("run(keyhandle, pErrorCode)");
        decrIndentation();
        nlIndent();
        this.sb.append("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.sb.append("print('Fehler im RaspberryPi')");
        nlIndent();
        this.sb.append("print(e.__class__.__name__)");
        nlIndent();
        // FIXME: we can only print about 30 chars
        this.sb.append("print(e)");
        decrIndentation();
        nlIndent();
        this.sb.append("finally:");
        incrIndentation();
        nlIndent();
        this.sb.append("if keyhandle != 0:");
        incrIndentation();
        nlIndent();
        this.sb.append("setState(keyhandle, 'disable')");
        nlIndent();
        this.sb.append("closeDevice(keyhandle, pErrorCode)");
        decrIndentation();
        decrIndentation();
        nlIndent();
        decrIndentation();
        decrIndentation();
        nlIndent();
        nlIndent();
        this.sb.append("if __name__ == \"__main__\":");
        incrIndentation();
        nlIndent();
        this.sb.append("main()");

    }

    @Override
    public Void visitStepForward(StepForward<Void> stepForward) {
        this.sb.append("Drive(keyhandle, 45, 45)");
        nlIndent();
        this.sb.append("time.sleep(1)");
        return null;
    }

    @Override
    public Void visitStepBackward(StepBackward<Void> stepBackward) {
        this.sb.append("Drive(keyhandle, -45, -45");
        nlIndent();
        this.sb.append("time.sleep(1)");
        return null;
    }

    @Override
    public Void visitRotateRight(RotateRight<Void> rotateRight) {
        this.sb.append("Turn(keyhandle, -29.29, 29.29)");
        nlIndent();
        this.sb.append("time.sleep(1)");
        return null;

    }

    @Override
    public Void visitRotateLeft(RotateLeft<Void> rotateLeft) {
        this.sb.append("Turn(keyhandle, 29.29, -29.29)");
        nlIndent();
        this.sb.append("time.sleep(1)");
        return null;
    }

    public Void visitMainTaskSimple(MainTaskSimple<Void> mainTask) {
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("def run(keyhandle, pErrorCode):");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    @Override
    public Void visitDummy(Dummy<Void> dummy) {
        return null;
    }
}
