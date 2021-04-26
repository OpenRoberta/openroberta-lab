package de.fhg.iais.roberta.visitor;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.visitor.collect.AbstractUsedHardwareCollectorVisitor;

public class OrbUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor {

    public OrbUsedHardwareCollectorVisitor(ConfigurationAst configuration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(configuration, beanBuilders);
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        return null;
    }

}
