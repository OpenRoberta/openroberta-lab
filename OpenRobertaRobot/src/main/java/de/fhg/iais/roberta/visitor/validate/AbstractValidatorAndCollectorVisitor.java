package de.fhg.iais.roberta.visitor.validate;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.ErrorAndWarningBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.BaseVisitor;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * Abstract class which all ValidatorAndCollectorVisitor (delegated as well as not delegeated) should extend from
 */
public abstract class AbstractValidatorAndCollectorVisitor extends BaseVisitor<Void> {
    private final IVisitor<Void> mainVisitor;

    protected final ConfigurationAst robotConfiguration;
    protected final ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders;
    protected final UsedMethodBean.Builder usedMethodBuilder;
    protected final UsedHardwareBean.Builder usedHardwareBuilder;
    protected final ErrorAndWarningBean.Builder errorAndWarningBuilder;
    protected final NNBean.Builder nnBeanBuilder;

    /**
     * @param robotConfiguration for the program that should be validated and collected
     * @param beanBuilders must contain the builders as checked in the Assert.notNull below
     */
    public AbstractValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        this.robotConfiguration = robotConfiguration;
        this.beanBuilders = beanBuilders;

        this.usedMethodBuilder = getBuilder(UsedMethodBean.Builder.class);
        this.usedHardwareBuilder = getBuilder(UsedHardwareBean.Builder.class);
        this.errorAndWarningBuilder = getBuilder(ErrorAndWarningBean.Builder.class);
        this.nnBeanBuilder = getBuilder(NNBean.Builder.class);

        this.mainVisitor = this;

        Assert.notNull(robotConfiguration, "There must be a robotConfiguration given");
        Assert.notNull(usedHardwareBuilder, "beanBuilders must contain a instance of UsedMethodBean.Builder");
        Assert.notNull(usedMethodBuilder, "beanBuilders must contain a instance of UsedHardwareBean.Builder");
        Assert.notNull(errorAndWarningBuilder, "beanBuilders must contain a instance of ErrorAndWarningBean.Builder");
        Assert.notNull(nnBeanBuilder, "beanBuilders must contain a instance of NNBean.Builder");
    }

    /**
     * @param mainVisitor visitor which is used when {@link AbstractValidatorAndCollectorVisitor#optionalComponentVisited(Phrase)},
     *     {@link AbstractValidatorAndCollectorVisitor#requiredComponentVisited(Phrase, List)}, or
     *     {@link AbstractValidatorAndCollectorVisitor#requiredComponentVisited(Phrase, Phrase[])} get called
     * @param robotConfiguration for the program that should be validates and collected
     * @param beanBuilders must contain {@link UsedHardwareBean.Builder}, {@link UsedMethodBean.Builder} and {@link ErrorAndWarningBean.Builder} and {@link NNBean.Builder}
     */
    public AbstractValidatorAndCollectorVisitor(
        IVisitor<Void> mainVisitor,
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {

        this.mainVisitor = mainVisitor;
        this.robotConfiguration = robotConfiguration;
        this.beanBuilders = beanBuilders;

        this.usedMethodBuilder = getBuilder(UsedMethodBean.Builder.class);
        this.usedHardwareBuilder = getBuilder(UsedHardwareBean.Builder.class);
        this.errorAndWarningBuilder = getBuilder(ErrorAndWarningBean.Builder.class);
        this.nnBeanBuilder = getBuilder(NNBean.Builder.class);

        Assert.notNull(robotConfiguration, "There must be a robotConfiguration given");
        Assert.notNull(mainVisitor, "mainVisitor cannot be null");
        Assert.notNull(usedHardwareBuilder, "beanBuilders must contain a instance of UsedMethodBean.Builder");
        Assert.notNull(usedMethodBuilder, "beanBuilders must contain a instance of UsedHardwareBean.Builder");
        Assert.notNull(errorAndWarningBuilder, "beanBuilders must contain a instance of ErrorAndWarningBean.Builder");
        Assert.notNull(nnBeanBuilder, "beanBuilders must contain a instance of NNBean.Builder");
    }

    protected final <T extends IProjectBean.IBuilder> T getBuilder(Class<T> clazz) {
        return this.beanBuilders.getInstance(clazz);
    }

    /**
     * if the subPhrase is not the {@link EmptyExpr}, visit the subPhrase. There are almost no reasons to use this method, because
     * all subpharses are required. Only bad design of blockly blocks require to use this method (RGBA, for instance)
     *
     * @param subPhrase to be visited, if not empty
     */
    protected final void optionalComponentVisited(Phrase subPhrase) {
        if ( !(subPhrase instanceof EmptyExpr) ) {
            subPhrase.accept(mainVisitor);
        }
    }

    /**
     * for the superPhrase check, that subPhrases are not empty. If true, visit the subPhrases, otherwise add error information to the superPhrase.<br>
     * public for tests :-<
     *
     * @param superPhrase phrase, whose components should be checked and visited
     * @param subPhrases the component of superPhrase to be checked and visited
     */
    @SafeVarargs
    public final void requiredComponentVisited(Phrase superPhrase, Phrase... subPhrases) {
        if ( subPhrases.length <= 0 ) {
            throw new DbcException("at least one sub phrase is required");
        }
        for ( Phrase subPhrase : subPhrases ) {
            mkEmptyOrDisabledCheck(superPhrase, subPhrase);
            subPhrase.accept(mainVisitor);
        }
    }

    /**
     * for the superPhrase check, that subPhrases are not empty. If true, visit the subPhrases, otherwise add error information to the superPhrase.
     *
     * @param superPhrase phrase, whose components should be checked and visited
     * @param subPhrases the component of superPhrase to be checked and visited
     */
    protected final <T extends Phrase> void requiredComponentVisited(Phrase superPhrase, List<T> subPhrases) {
        for ( Phrase subPhrase : subPhrases ) {
            mkEmptyOrDisabledCheck(superPhrase, subPhrase);
            subPhrase.accept(mainVisitor);
        }
    }

    /**
     * Appends an error to the phrase and remembers the error by delegating it to a {@link ErrorAndWarningBean}<br>
     * public for tests :-<
     */
    public final void addErrorToPhrase(final Phrase phrase, final String message) {
        NepoInfo info = NepoInfo.error(message);
        if ( phrase instanceof SensorExpr ) {
            ((SensorExpr) phrase).sensor.addInfo(info);
        } else if ( phrase instanceof ActionExpr ) {
            ((ActionExpr) phrase).action.addInfo(info);
        } else if ( phrase instanceof MethodExpr ) {
            ((MethodExpr) phrase).method.addInfo(info);
        } else if ( phrase instanceof FunctionExpr ) {
            ((FunctionExpr) phrase).function.addInfo(info);
        } else if ( phrase instanceof StmtExpr ) {
            ((StmtExpr) phrase).stmt.addInfo(info);
        } else {
            phrase.addInfo(info);
        }
        errorAndWarningBuilder.addError(message);
    }

    /**
     * Appends a warning to the phrase and remembers the warning by delegating it to a {@link ErrorAndWarningBean}<br>
     * public for tests :-<
     */
    public final void addWarningToPhrase(final Phrase phrase, final String message) {
        NepoInfo info = NepoInfo.warning(message);
        if ( phrase instanceof SensorExpr ) {
            ((SensorExpr) phrase).sensor.addInfo(info);
        } else if ( phrase instanceof ActionExpr ) {
            ((ActionExpr) phrase).action.addInfo(info);
        } else if ( phrase instanceof MethodExpr ) {
            ((MethodExpr) phrase).method.addInfo(info);
        } else if ( phrase instanceof FunctionExpr ) {
            ((FunctionExpr) phrase).function.addInfo(info);
        } else if ( phrase instanceof StmtExpr ) {
            ((StmtExpr) phrase).stmt.addInfo(info);
        } else {
            phrase.addInfo(info);
        }
        errorAndWarningBuilder.addWarning(message);
    }

    protected final void addToPhraseIfUnsupportedInSim(final Phrase phrase, final boolean isError, final boolean isSim) {
        if ( isSim ) {
            if ( isError ) {
                addErrorToPhrase(phrase, "SIM_BLOCK_NOT_SUPPORTED");
            } else {
                addWarningToPhrase(phrase, "SIM_BLOCK_NOT_SUPPORTED");
            }
        }
    }

    private final void mkEmptyOrDisabledCheck(Phrase superPhrase, Phrase subPhrase) {
        if ( subPhrase instanceof EmptyExpr || subPhrase.getProperty().isDisabled() ) {
            addErrorToPhrase(superPhrase, "ERROR_MISSING_PARAMETER");
        } else if ( subPhrase instanceof ExprList ) {
            for ( Expr expr : ((ExprList) subPhrase).get() ) {
                if ( expr instanceof EmptyExpr || expr.getProperty().isDisabled() ) {
                    addErrorToPhrase(superPhrase, "ERROR_MISSING_PARAMETER");
                }
            }
        } else if ( subPhrase instanceof StmtList ) {
            for ( Stmt stmt : ((StmtList) subPhrase).get() ) {
                if ( stmt instanceof ExprStmt ) {
                    if ( ((ExprStmt) stmt).expr instanceof EmptyExpr || ((ExprStmt) stmt).expr.getProperty().isDisabled() ) {
                        addErrorToPhrase(superPhrase, "ERROR_MISSING_PARAMETER");
                    }
                }
            }
        }
    }
}
