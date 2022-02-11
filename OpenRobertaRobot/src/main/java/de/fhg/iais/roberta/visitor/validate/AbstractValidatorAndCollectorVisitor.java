package de.fhg.iais.roberta.visitor.validate;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.ErrorAndWarningBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.BaseVisitor;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * Abstract class which all ValidatorAndCollectorVisitor (delegated as well as not delegeated) should extend from
 */
public abstract class AbstractValidatorAndCollectorVisitor extends BaseVisitor<Void> {
    private final IVisitor<Void> mainVisitor;

    protected final ConfigurationAst robotConfiguration;
    protected final ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders;
    protected final UsedMethodBean.Builder usedMethodBuilder;
    protected final UsedHardwareBean.Builder usedHardwareBuilder;
    protected final ErrorAndWarningBean.Builder errorAndWarningBuilder;

    /**
     * @param robotConfiguration for the program that should be validates and collected
     * @param beanBuilders must contain {@link UsedHardwareBean.Builder}, {@link UsedMethodBean.Builder} and {@link ErrorAndWarningBean.Builder}
     */
    public AbstractValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        this.robotConfiguration = robotConfiguration;
        this.beanBuilders = beanBuilders;

        this.usedMethodBuilder = getBuilder(UsedMethodBean.Builder.class);
        this.usedHardwareBuilder = getBuilder(UsedHardwareBean.Builder.class);
        this.errorAndWarningBuilder = getBuilder(ErrorAndWarningBean.Builder.class);

        this.mainVisitor = this;

        assert robotConfiguration != null : "There must be a robotConfiguration given";
        assert usedHardwareBuilder != null : "beanBuilders must contain a instance of UsedMethodBean.Builder";
        assert usedMethodBuilder != null : "beanBuilders must contain a instance of UsedHardwareBean.Builder";
        assert errorAndWarningBuilder != null : "beanBuilders must contain a instance of ErrorAndWarningBean.Builder";
    }

    /**
     * @param mainVisitor visitor which is used when {@link AbstractValidatorAndCollectorVisitor#optionalComponentVisited(Phrase)},
     *  {@link AbstractValidatorAndCollectorVisitor#requiredComponentVisited(Phrase, List)}, or
     *  {@link AbstractValidatorAndCollectorVisitor#requiredComponentVisited(Phrase, Phrase[])} get called
     * @param robotConfiguration for the program that should be validates and collected
     * @param beanBuilders must contain {@link UsedHardwareBean.Builder}, {@link UsedMethodBean.Builder} and {@link ErrorAndWarningBean.Builder}
     */
    public AbstractValidatorAndCollectorVisitor(
        IVisitor<Void> mainVisitor,
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {

        assert mainVisitor != null;

        this.mainVisitor = mainVisitor;
        this.robotConfiguration = robotConfiguration;
        this.beanBuilders = beanBuilders;

        this.usedMethodBuilder = getBuilder(UsedMethodBean.Builder.class);
        this.usedHardwareBuilder = getBuilder(UsedHardwareBean.Builder.class);
        this.errorAndWarningBuilder = getBuilder(ErrorAndWarningBean.Builder.class);

        assert robotConfiguration != null : "There must be a robotConfiguration given";
        assert mainVisitor != null : "mainVisitor cannot be null";
        assert usedHardwareBuilder != null : "beanBuilders must contain a instance of UsedMethodBean.Builder";
        assert usedMethodBuilder != null : "beanBuilders must contain a instance of UsedHardwareBean.Builder";
        assert errorAndWarningBuilder != null : "beanBuilders must contain a instance of ErrorAndWarningBean.Builder";
    }

    protected <T extends IProjectBean.IBuilder<?>> T getBuilder(Class<T> clazz) {
        return this.beanBuilders.getInstance(clazz);
    }

    /**
     * if the subPhrase is not the {@link EmptyExpr}, visit the subPhrase. There are almost no reasons to use this method, because
     * all subpharses are required. Only bad design of blockly blocks require to use this method (RGBA, for instance)
     *
     * @param subPhrase to be visited, if not empty
     */
    protected void optionalComponentVisited(Phrase<Void> subPhrase) {
        if ( !(subPhrase instanceof EmptyExpr<?>) ) {
            subPhrase.accept(mainVisitor);
        }
    }

    /**
     * for the superPhrase check, that subPhrases are not empty. If true, visit the subPhrases, otherwise add error information to the superPhrase.
     *
     * @param superPhrase phrase, whose components should be checked and visited
     * @param subPhrases the component of superPhrase to be checked and visited
     */
    @SafeVarargs
    protected final void requiredComponentVisited(Phrase<Void> superPhrase, Phrase<Void>... subPhrases) {
        for ( Phrase<Void> subPhrase : subPhrases ) {
            if ( subPhrase instanceof EmptyExpr<?> ) {
                addErrorToPhrase(superPhrase, "ERROR_MISSING_PARAMETER");
            } else {
                subPhrase.accept(mainVisitor);
            }
        }
    }

    /**
     * for the superPhrase check, that subPhrases are not empty. If true, visit the subPhrases, otherwise add error information to the superPhrase.
     *
     * @param superPhrase phrase, whose components should be checked and visited
     * @param subPhrases the component of superPhrase to be checked and visited
     */
    protected final <T extends Phrase<Void>> void requiredComponentVisited(Phrase<Void> superPhrase, List<T> subPhrases) {
        for ( Phrase<Void> subPhrase : subPhrases ) {
            if ( subPhrase instanceof EmptyExpr<?> ) {
                addErrorToPhrase(superPhrase, "ERROR_MISSING_PARAMETER");
            } else {
                subPhrase.accept(mainVisitor);
            }
        }
    }


    /**
     * Appends an error to the phrase and remembers the error by delegating it to a {@link ErrorAndWarningBean}
     *
     * @param phrase
     * @param message
     */
    protected void addErrorToPhrase(final Phrase<Void> phrase, final String message) {
        phrase.addInfo(NepoInfo.error(message));
        errorAndWarningBuilder.addError(message);
    }

    /**
     * Appends a warning to the phrase and remembers the warning by delegating it to a {@link ErrorAndWarningBean}
     *
     * @param phrase
     * @param message
     */
    protected void addWarningToPhrase(final Phrase<Void> phrase, final String message) {
        phrase.addInfo(NepoInfo.warning(message));
        errorAndWarningBuilder.addWarning(message);
    }
}
