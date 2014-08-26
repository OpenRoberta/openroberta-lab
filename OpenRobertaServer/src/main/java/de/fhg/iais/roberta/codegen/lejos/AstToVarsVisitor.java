package de.fhg.iais.roberta.codegen.lejos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class is implementing {@link Visitor}.<br>
 * A list of all vars used in a program are generated
 */
public class AstToVarsVisitor extends AstDefaultVisitor<Set<String>> {
    private final Set<String> allVars = new HashSet<>();

    /**
     * initialize the Java code generator visitor.
     * 
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    AstToVarsVisitor() {
    }

    /**
     * factory method to generate Java code from an AST.<br>
     * 
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static Set<String> generate(List<Phrase<Set<String>>> phrases) //
    {
        Assert.isTrue(phrases.size() >= 1);
        AstToVarsVisitor astVisitor = new AstToVarsVisitor();
        for ( Phrase<Set<String>> phrase : phrases ) {
            phrase.visit(astVisitor);
        }
        return astVisitor.allVars;
    }

    @Override
    public Set<String> defaultResult() {
        return this.allVars;
    }

    @Override
    public Set<String> visitVar(Var<Set<String>> var) {
        String varName = var.getValue();
        this.allVars.add(varName);
        return this.allVars;
    }

    @Override
    public Set<String> visitNumConst(NumConst<Set<String>> numConst) {
        String numName = numConst.getValue();
        this.allVars.add(numName);
        return this.allVars;
    }

    @Override
    public Set<String> visitMotorOnAction(MotorOnAction<Set<String>> motorOnAction) {
        return defaultResult();
    }

    @Override
    public Set<String> visitTurnAction(TurnAction<Set<String>> turnAction) {
        return defaultResult();
    }

    @Override
    public Set<String> combine(List<Set<String>> vs) {
        for ( Set<String> list : vs ) {
            this.allVars.addAll(list);
        }
        return this.allVars;
    }

    @Override
    public Set<String> combine(Set<String> v1, Set<String> v2) {
        this.allVars.addAll(v1);
        this.allVars.addAll(v2);
        return this.allVars;
    }

    @Override
    public Set<String> combine(Set<String> v1, Set<String> v2, Set<String> v3) {
        // TODO Auto-generated method stub
        return null;
    }
}
