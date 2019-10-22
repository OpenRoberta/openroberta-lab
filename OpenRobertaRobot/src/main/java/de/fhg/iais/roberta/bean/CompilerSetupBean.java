package de.fhg.iais.roberta.bean;

public class CompilerSetupBean {

    private String compilerBinDir;
    private String compilerResourcesDir;
    private String tempDir;
    private String fqbn;
    private String ip;

    public String getIp() {
        return ip;
    }

    public String getFqbn() {
        return fqbn;
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

    public static class Builder {
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

        public Builder setFqbn(String fqbn) {
            compilerWorkflowBean.fqbn = fqbn;
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
