package de.fhg.iais.roberta.bean;

/**
 * Container for all compiler setup related information, to execute the runBuild, usually.
 * Paths necessary for the compilers and specific compiler options are stored here.
 */
public class CompilerSetupBean implements IProjectBean {

    private String compilerBinDir;
    private String compilerResourcesDir;
    private String tempDir;
    private String ip;

    public String getIp() {
        return ip;
    }

    public String getCompilerBinDir() {
        return compilerBinDir;
    }

    public String getCompilerResourcesDir() {
        return compilerResourcesDir;
    }

    public String getTempDir() {
        return tempDir;
    }

    public static class Builder implements IBuilder<CompilerSetupBean> {
        private final CompilerSetupBean compilerWorkflowBean = new CompilerSetupBean();

        public Builder setCompilerBinDir(String compilerBinDir) {
            compilerWorkflowBean.compilerBinDir = compilerBinDir;
            return this;
        }

        public Builder setCompilerResourcesDir(String compilerResourcesDir) {
            compilerWorkflowBean.compilerResourcesDir = compilerResourcesDir;
            return this;
        }

        public Builder setTempDir(String tempDir) {
            compilerWorkflowBean.tempDir = tempDir;
            return this;
        }

        public Builder setIp(String ip) {
            compilerWorkflowBean.ip = ip;
            return this;
        }

        public CompilerSetupBean build() {
            return this.compilerWorkflowBean;
        }
    }
}
