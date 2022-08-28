package de.fhg.iais.roberta;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.bean.ErrorAndWarningBean;

public class ErrorAndWarningBeanTest {

    private ErrorAndWarningBean.Builder builder;

    @Before
    public void setUp() throws Exception {
        this.builder = new ErrorAndWarningBean.Builder();
    }

    @Test
    public void addError() {
        builder.addError("ERROR_MISSING_PARAMETER");

        ErrorAndWarningBean build = builder.build();
        Assertions.assertThat(build.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(build.getWarningCount()).isEqualTo(0);
        Assertions.assertThat(build.getErrorAndWarningMessages()).contains("ERROR_MISSING_PARAMETER");
    }

    @Test
    public void addWarning() {
        builder.addWarning("SIM_BLOCK_NOT_SUPPORTED");

        ErrorAndWarningBean build = builder.build();
        Assertions.assertThat(build.getWarningCount()).isEqualTo(1);
        Assertions.assertThat(build.getErrorCount()).isEqualTo(0);
        Assertions.assertThat(build.getErrorAndWarningMessages()).contains("SIM_BLOCK_NOT_SUPPORTED");
    }
}