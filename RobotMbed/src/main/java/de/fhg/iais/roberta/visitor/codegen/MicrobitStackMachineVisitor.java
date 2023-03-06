package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IMicrobitVisitor;

public class MicrobitStackMachineVisitor extends MbedStackMachineVisitor implements IMicrobitVisitor<Void> {

    public MicrobitStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases) {
        super(configuration, phrases);
        Assert.isTrue(!phrases.isEmpty());

    }


    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        return null;
    }

}