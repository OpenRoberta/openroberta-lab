package de.fhg.iais.roberta.bean;

import de.fhg.iais.roberta.util.NNStepDecl;

public class NNBean implements IProjectBean {

    private NNStepDecl nnStepDecl;

    public static class Builder implements IBuilder<NNBean> {
        private final NNBean nnBean = new NNBean();

        public Builder addNNStepDecl(NNStepDecl nnStepDecl) {
            this.nnBean.nnStepDecl = nnStepDecl;
            return this;
        }

        @Override
        public NNBean build() {
            return nnBean;
        }
    }

    public NNStepDecl getNnStepDecl() {
        return nnStepDecl;
    }
}
