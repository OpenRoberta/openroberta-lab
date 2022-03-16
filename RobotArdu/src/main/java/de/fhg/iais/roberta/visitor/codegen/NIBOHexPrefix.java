package de.fhg.iais.roberta.visitor.codegen;

public enum NIBOHexPrefix {
    ROB3RTA_HEX("rob3rta",
                ":02000002BE330B\n" +
                ":020000020000FC\n"),
    BOB3_HEX(   "bob3",
                ":02000002B0B399\n" +
                ":020000020000FC\n");

    private final String robot;
    private final String hexPrefix;

    NIBOHexPrefix(String robot, String hexPrefix) {
        this.robot = robot;
        this.hexPrefix = hexPrefix;
    }

    @Override
    public String toString() {
        return this.robot;
    }

    private String getHexPrefix() {
        return this.hexPrefix;
    }

    public static String getHexPrefixForRobot(String robot) {
        for ( NIBOHexPrefix type : NIBOHexPrefix.values() ) {
            if (robot.equalsIgnoreCase(type.toString())) {
                return type.getHexPrefix();
            }
        }
        throw new IllegalArgumentException("No hex prefix for robot " + robot + " found");
    }
}
