package de.fhg.iais.roberta;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.bean.ErrorAndWarningBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.TestSimplePhrase;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.typecheck.NepoInfos;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.TestValidatorAndCollectorVisitor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ValidatorAndCollectorVisitorTest {

    private TestValidatorAndCollectorVisitor delegatedValidatorAndCollectorVisitor;
    private TestValidatorAndCollectorVisitor mainValidatorAndCollectorVisitor;
    private ConfigurationAst robotConfiguration;
    private ClassToInstanceMap<IProjectBean.IBuilder> beanBuilder;
    private UsedMethodBean.Builder usedMethodBeanBuilder;
    private UsedHardwareBean.Builder usedHardwareBeanBuilder;
    private ErrorAndWarningBean.Builder errorAndWarningBeanBuilder;
    private NNBean.Builder nnBeanBuilder;

    @Before
    public void setUp() throws Exception {
        this.robotConfiguration = new ConfigurationAst.Builder().build();
        ImmutableClassToInstanceMap.Builder<IProjectBean.IBuilder> mapBuilder = new ImmutableClassToInstanceMap.Builder<>();
        usedMethodBeanBuilder = new UsedMethodBean.Builder();
        mapBuilder.put(UsedMethodBean.Builder.class, usedMethodBeanBuilder);
        usedHardwareBeanBuilder = new UsedHardwareBean.Builder();
        mapBuilder.put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder);
        errorAndWarningBeanBuilder = new ErrorAndWarningBean.Builder();
        mapBuilder.put(ErrorAndWarningBean.Builder.class, errorAndWarningBeanBuilder);
        nnBeanBuilder = new NNBean.Builder();
        mapBuilder.put(NNBean.Builder.class, nnBeanBuilder);

        beanBuilder = mapBuilder.build();

        this.mainValidatorAndCollectorVisitor = spy(new TestValidatorAndCollectorVisitor(robotConfiguration, beanBuilder));
        this.delegatedValidatorAndCollectorVisitor = new TestValidatorAndCollectorVisitor(mainValidatorAndCollectorVisitor, robotConfiguration, beanBuilder);
    }

    @Test
    public void requiredComponentVisitedVarArgsWithoutEmptyExpression() {
        Phrase phrase = new TestSimplePhrase();
        TestSimplePhrase childPhrase1 = new TestSimplePhrase();
        TestSimplePhrase childPhrase2 = new TestSimplePhrase();

        delegatedValidatorAndCollectorVisitor.requiredComponentVisited(phrase, childPhrase1, childPhrase2);

        ErrorAndWarningBean errorAndWarningBean = errorAndWarningBeanBuilder.build();

        assertThat(errorAndWarningBean.getErrorCount()).isEqualTo(0);
        assertThat(errorAndWarningBean.getWarningCount()).isEqualTo(0);
        assertThat(errorAndWarningBean.getErrorAndWarningMessages()).isEmpty();

        NepoInfos infos = phrase.getInfos();
        assertThat(infos.getInfos()).isEmpty();
        assertThat(infos.getErrorCount()).isEqualTo(0);

        verify(mainValidatorAndCollectorVisitor).visitTestSimplePhrase(childPhrase1);
        verify(mainValidatorAndCollectorVisitor).visitTestSimplePhrase(childPhrase2);
    }


    @Test
    public void requiredComponentVisitedVarArgs() {
        Phrase phrase = new TestSimplePhrase();
        Phrase emptyExpression = new EmptyExpr(BlocklyType.ANY);

        delegatedValidatorAndCollectorVisitor.requiredComponentVisited(phrase, emptyExpression);

        ErrorAndWarningBean errorAndWarningBean = errorAndWarningBeanBuilder.build();

        assertThat(errorAndWarningBean.getErrorCount()).isEqualTo(1);
        assertThat(errorAndWarningBean.getWarningCount()).isEqualTo(0);
        assertThat(errorAndWarningBean.getErrorAndWarningMessages()).contains("ERROR_MISSING_PARAMETER");

        NepoInfos infos = phrase.getInfos();
        assertThat(infos.getInfos()).anySatisfy((info) -> {
            assertThat(info.getSeverity()).isEqualTo(NepoInfo.Severity.ERROR);
            assertThat(info.getMessage()).isEqualTo("ERROR_MISSING_PARAMETER");
        });

        assertThat(infos.getErrorCount()).isEqualTo(1);
    }

    @Test
    public void requiredComponentVisitedListWithoutEmptyExpression() {
        Phrase phrase = new TestSimplePhrase();
        TestSimplePhrase childPhrase1 = new TestSimplePhrase();
        TestSimplePhrase childPhrase2 = new TestSimplePhrase();

        delegatedValidatorAndCollectorVisitor.requiredComponentVisited(phrase, childPhrase1, childPhrase2);

        ErrorAndWarningBean errorAndWarningBean = errorAndWarningBeanBuilder.build();

        assertThat(errorAndWarningBean.getErrorCount()).isEqualTo(0);
        assertThat(errorAndWarningBean.getWarningCount()).isEqualTo(0);
        assertThat(errorAndWarningBean.getErrorAndWarningMessages()).isEmpty();

        NepoInfos infos = phrase.getInfos();
        assertThat(infos.getInfos()).isEmpty();
        assertThat(infos.getErrorCount()).isEqualTo(0);

        verify(mainValidatorAndCollectorVisitor).visitTestSimplePhrase(childPhrase1);
        verify(mainValidatorAndCollectorVisitor).visitTestSimplePhrase(childPhrase2);
    }

    @Test
    public void requiredComponentVisitedList() {
        Phrase phrase = new TestSimplePhrase();
        Phrase emptyExpression = new EmptyExpr(BlocklyType.ANY);

        delegatedValidatorAndCollectorVisitor.requiredComponentVisited(phrase, emptyExpression);

        ErrorAndWarningBean errorAndWarningBean = errorAndWarningBeanBuilder.build();

        assertThat(errorAndWarningBean.getErrorCount()).isEqualTo(1);
        assertThat(errorAndWarningBean.getWarningCount()).isEqualTo(0);
        assertThat(errorAndWarningBean.getErrorAndWarningMessages()).contains("ERROR_MISSING_PARAMETER");

        NepoInfos infos = phrase.getInfos();
        assertThat(infos.getInfos()).anySatisfy((info) -> {
            assertThat(info.getSeverity()).isEqualTo(NepoInfo.Severity.ERROR);
            assertThat(info.getMessage()).isEqualTo("ERROR_MISSING_PARAMETER");
        });

        assertThat(infos.getErrorCount()).isEqualTo(1);
    }


    @Test
    public void addErrorToPhrase() {
        Phrase phrase = new TestSimplePhrase();

        String errorMessage = "WHATEVER";
        delegatedValidatorAndCollectorVisitor.addErrorToPhrase(phrase, errorMessage);

        ErrorAndWarningBean errorAndWarningBean = errorAndWarningBeanBuilder.build();

        assertThat(errorAndWarningBean.getErrorCount()).isEqualTo(1);
        assertThat(errorAndWarningBean.getWarningCount()).isEqualTo(0);
        assertThat(errorAndWarningBean.getErrorAndWarningMessages()).contains(errorMessage);

        NepoInfos infos = phrase.getInfos();
        assertThat(infos.getInfos()).anySatisfy((info) -> {
            assertThat(info.getSeverity()).isEqualTo(NepoInfo.Severity.ERROR);
            assertThat(info.getMessage()).isEqualTo(errorMessage);
        });

        assertThat(infos.getErrorCount()).isEqualTo(1);
    }

    @Test
    public void addWarningToPhrase() {
        Phrase phrase = new TestSimplePhrase();

        String warningMessage = "WHATEVER";
        delegatedValidatorAndCollectorVisitor.addWarningToPhrase(phrase, warningMessage);

        ErrorAndWarningBean errorAndWarningBean = errorAndWarningBeanBuilder.build();

        assertThat(errorAndWarningBean.getErrorCount()).isEqualTo(0);
        assertThat(errorAndWarningBean.getWarningCount()).isEqualTo(1);
        assertThat(errorAndWarningBean.getErrorAndWarningMessages()).contains(warningMessage);

        NepoInfos infos = phrase.getInfos();
        assertThat(infos.getInfos()).anySatisfy((info) -> {
            assertThat(info.getSeverity()).isEqualTo(NepoInfo.Severity.WARNING);
            assertThat(info.getMessage()).isEqualTo(warningMessage);
        });

        assertThat(infos.getErrorCount()).isEqualTo(0);
    }


    @Test
    public void missingParameterInConstructor() {
        // mainVisitor
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(null, robotConfiguration, beanBuilder)).isInstanceOf(DbcException.class);

        // ConfigurationAst
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(null, beanBuilder)).isInstanceOf(DbcException.class);
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(delegatedValidatorAndCollectorVisitor, null, beanBuilder)).isInstanceOf(DbcException.class);

        // ErrorAndWarninBean
        ImmutableClassToInstanceMap<IProjectBean.IBuilder> withoutErrorAndWarnings = (new ImmutableClassToInstanceMap.Builder<IProjectBean.IBuilder>())
            .put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder)
            .put(UsedMethodBean.Builder.class, usedMethodBeanBuilder)
            .put(NNBean.Builder.class, nnBeanBuilder)
            .build();
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(delegatedValidatorAndCollectorVisitor, robotConfiguration, withoutErrorAndWarnings)).isInstanceOf(DbcException.class);
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(robotConfiguration, withoutErrorAndWarnings)).isInstanceOf(DbcException.class);

        // HardwareBean
        ImmutableClassToInstanceMap<IProjectBean.IBuilder> withoutHardware = (new ImmutableClassToInstanceMap.Builder<IProjectBean.IBuilder>())
            .put(UsedMethodBean.Builder.class, usedMethodBeanBuilder)
            .put(ErrorAndWarningBean.Builder.class, errorAndWarningBeanBuilder)
            .put(NNBean.Builder.class, nnBeanBuilder)
            .build();
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(delegatedValidatorAndCollectorVisitor, robotConfiguration, withoutHardware)).isInstanceOf(DbcException.class);
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(robotConfiguration, withoutHardware)).isInstanceOf(DbcException.class);

        // MethodBean
        ImmutableClassToInstanceMap<IProjectBean.IBuilder> withoutMethod = (new ImmutableClassToInstanceMap.Builder<IProjectBean.IBuilder>())
            .put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder)
            .put(ErrorAndWarningBean.Builder.class, errorAndWarningBeanBuilder)
            .put(NNBean.Builder.class, nnBeanBuilder)
            .build();
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(delegatedValidatorAndCollectorVisitor, robotConfiguration, withoutMethod)).isInstanceOf(DbcException.class);
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(robotConfiguration, withoutMethod)).isInstanceOf(DbcException.class);

        // NNBean
        ImmutableClassToInstanceMap<IProjectBean.IBuilder> withoutNN = (new ImmutableClassToInstanceMap.Builder<IProjectBean.IBuilder>())
            .put(UsedMethodBean.Builder.class, usedMethodBeanBuilder)
            .put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder)
            .put(ErrorAndWarningBean.Builder.class, errorAndWarningBeanBuilder)
            .build();
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(delegatedValidatorAndCollectorVisitor, robotConfiguration, withoutNN)).isInstanceOf(DbcException.class);
        assertThatThrownBy(() -> new TestValidatorAndCollectorVisitor(robotConfiguration, withoutNN)).isInstanceOf(DbcException.class);

        // EVERYTHING IS FINE
        ImmutableClassToInstanceMap<IProjectBean.IBuilder> everything = (new ImmutableClassToInstanceMap.Builder<IProjectBean.IBuilder>())
            .put(UsedMethodBean.Builder.class, usedMethodBeanBuilder)
            .put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder)
            .put(ErrorAndWarningBean.Builder.class, errorAndWarningBeanBuilder)
            .put(NNBean.Builder.class, nnBeanBuilder)
            .build();
        assertThat(new TestValidatorAndCollectorVisitor(delegatedValidatorAndCollectorVisitor, robotConfiguration, everything)).isInstanceOf(TestValidatorAndCollectorVisitor.class);
        assertThat(new TestValidatorAndCollectorVisitor(robotConfiguration, everything)).isInstanceOf(TestValidatorAndCollectorVisitor.class);
    }
}