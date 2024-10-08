package de.fhg.iais.roberta.visitor;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;


/**
 * This class is implementing {@link IVisitor}, and keeps some shared functions for Lego and Pybricks.<br>
 * All functions should append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public abstract class AbstractSpikePythonVisitor extends AbstractPythonVisitor implements ISpikeVisitor<Void> {


    protected final ConfigurationAst configurationAst;

    protected AbstractSpikePythonVisitor(
        List<List<Phrase>> programPhrases,
        ClassToInstanceMap<IProjectBean> beans, ConfigurationAst configurationAst) {
        super(programPhrases, beans);
        this.configurationAst = configurationAst;
    }

    @Override
    protected final void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.src.add("def main():");
        incrIndentation();
        nlIndent();
        this.src.add("try:");
        incrIndentation();
        nlIndent();
        this.src.add("run()");
        decrIndentation();
        nlIndent();
        this.src.add("except Exception as e:");
        incrIndentation();
        nlIndent();
        addExceptionSadFaceToCode();
        decrIndentation();
        //TODO finally close open ports
        decrIndentation();
        nlIndent();
        nlIndent();

        this.src.add("main()");
    }

    /**
     * add command to show sad face to code
     */
    protected abstract void addExceptionSadFaceToCode();

    @Override
    public Void visitMainTask(MainTask mainTask) {
        visitorGenerateUserVariables(mainTask);

        nlIndent();
        this.src.add("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.src.add("global ", String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    @Override
    public final Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        decrIndentation();
        return null;
    }

    @Override
    public final Void visitRgbColor(RgbColor rgbColor) {
        this.src.add("(");
        rgbColor.R.accept(this);
        this.src.add(", ");
        rgbColor.G.accept(this);
        this.src.add(", ");
        rgbColor.B.accept(this);
        this.src.add(")");
        return null;
    }

    protected final String getPortFromConfig(String name) {
        ConfigurationComponent block = configurationAst.getConfigurationComponent(name);
        return block.getComponentProperties().get("PORT");
    }

}


