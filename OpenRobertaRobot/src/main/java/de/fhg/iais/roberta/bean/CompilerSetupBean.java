package de.fhg.iais.roberta.bean;

/**
 * Container for all compiler setup related information, to execute the runBuild, usually. Paths necessary for the compilers and specific compiler options are
 * stored here.
 */
public class CompilerSetupBean implements IProjectBean {

    private String compilerBinDir;
    private String compilerResourcesDir;
    private String tempDir;
    private String ip;

    public String getIp() {
        return this.ip;
    }

    public String getCompilerBinDir() {
        return this.compilerBinDir;
    }

    public String getCompilerResourcesDir() {
        return this.compilerResourcesDir;
    }

    public String getTempDir() {
        return this.tempDir;
    }

    public static class Builder implements IBuilder<CompilerSetupBean> {
        private final CompilerSetupBean compilerWorkflowBean = new CompilerSetupBean();

        public Builder setCompilerBinDir(String compilerBinDir) {
            this.compilerWorkflowBean.compilerBinDir = compilerBinDir;
            return this;
        }

        public Builder setCompilerResourcesDir(String compilerResourcesDir) {
            this.compilerWorkflowBean.compilerResourcesDir = compilerResourcesDir;
            return this;
        }

        public Builder setTempDir(String tempDir) {
            this.compilerWorkflowBean.tempDir = tempDir;
            return this;
        }

        public Builder setIp(String ip) {
            this.compilerWorkflowBean.ip = ip;
            return this;
        }

        @Override
        public CompilerSetupBean build() {
            return this.compilerWorkflowBean;
        }
    }
}
