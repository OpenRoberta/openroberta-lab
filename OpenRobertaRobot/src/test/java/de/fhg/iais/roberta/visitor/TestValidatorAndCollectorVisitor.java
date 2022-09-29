package de.fhg.iais.roberta.visitor;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.TestSimplePhrase;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.visitor.validate.AbstractValidatorAndCollectorVisitor;

public class TestValidatorAndCollectorVisitor extends AbstractValidatorAndCollectorVisitor {
    public TestValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    public TestValidatorAndCollectorVisitor(
        IVisitor<Void> mainVisitor,
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(mainVisitor, robotConfiguration, beanBuilders);
    }

    public Void visitTestSimplePhrase(TestSimplePhrase simplePhrase) {
        return null;
    }

    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        return null;
    }
}
