package de.fhg.iais.roberta.components;

public class NAOConfiguration extends Configuration {
    private final String ipAddress;
    private final String portNumber;
    private final String userName;
    private final String password;

    public NAOConfiguration(String ipAddress, String portNumber, String userName, String password) {
        super(null, null, 0, 0);
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.userName = userName;
        this.password = password;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getPortNumber() {
        return this.portNumber;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    /**
     * This class is a builder of {@link Configuration}
     */
    public static class Builder extends Configuration.Builder<Builder> {
        private String ipAddress;
        private String portNumber;
        private String userName;
        private String password;

        public Builder setIpAddres(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder setPortNumber(String portNumber) {
            this.portNumber = portNumber;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        @Override
        public Configuration build() {
            return new NAOConfiguration(this.ipAddress, this.portNumber, this.userName, this.password);
        }

        @Override
        public String toString() {
            return "Builder [" + this.ipAddress + ", " + this.portNumber + ", " + this.userName + ", " + this.password + "]";
        }
    }

}
