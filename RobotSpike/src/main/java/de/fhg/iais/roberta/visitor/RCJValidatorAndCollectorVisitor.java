package de.fhg.iais.roberta.visitor;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.rcj.DisplayTextAction;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchKeySensor;
import de.fhg.iais.roberta.syntax.sensor.rcj.InductiveSensor;
import de.fhg.iais.roberta.visitor.spikePybricks.SpikePybricksValidatorAndCollectorVisitor;

public class RCJValidatorAndCollectorVisitor extends SpikePybricksValidatorAndCollectorVisitor implements IRCJVisitor<Void> {

    public RCJValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitTouchKeySensor(TouchKeySensor touchKeySensor) {
        return null;
    }

    @Override
    public Void visitInductiveSensor(InductiveSensor inductiveSensor) {
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        requiredComponentVisited(displayTextAction, displayTextAction.text, displayTextAction.row);
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        return null;
    }
}
