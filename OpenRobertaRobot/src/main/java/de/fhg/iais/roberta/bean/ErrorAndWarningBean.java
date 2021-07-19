package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorAndWarningBean implements IProjectBean {

    private int errorCount = 0;
    private int warningCount = 0;
    private List<String> errorAndWarningMessages = new ArrayList<>();

    public List<String> getErrorAndWarningMessages() {
        return Collections.unmodifiableList(errorAndWarningMessages);
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    public int getWarningCount() {
        return this.warningCount;
    }

    public static class Builder implements IProjectBean.IBuilder<ErrorAndWarningBean> {
        private final ErrorAndWarningBean errorAndWarningBean = new ErrorAndWarningBean();

        public Builder addError(String errorMessage) {
            errorAndWarningBean.errorCount++;
            errorAndWarningBean.errorAndWarningMessages.add(errorMessage);
            return this;
        }

        public Builder addWarning(String warningMessage) {
            errorAndWarningBean.warningCount++;
            errorAndWarningBean.errorAndWarningMessages.add(warningMessage);
            return this;
        }

        @Override
        public ErrorAndWarningBean build() {
            return errorAndWarningBean;
        }
    }


}
