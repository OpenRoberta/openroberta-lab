package de.fhg.iais.roberta.util;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class VersionChecker {
    private static final Logger LOG = LoggerFactory.getLogger(VersionChecker.class);
    private static final String DOT_REGEX = "\\.";

    private final String[] versionFrom;
    private final String[] versionTo;

    public VersionChecker(String versionFrom, String versionTo) {
        this.versionFrom = versionFrom.split(DOT_REGEX);
        this.versionTo = versionTo.split(DOT_REGEX);
    }

    public boolean validateServerSide() {
        return check("OpenRobertaRuntime", "OpenRobertaServer", "OpenRobertaShared");
    }

    public boolean validateRobotSide() {
        return check("EV3Menu", "OpenRobertaRuntime", "OpenRobertaShared");
    }

    @SuppressFBWarnings(value = "REC_CATCH_EXCEPTION", justification = "here any exception should generate a return value of FALSE")
    private boolean check(String... propertyPrefixes) {
        for ( String propertyPrefix : propertyPrefixes ) {
            try {
                Properties properties = new Properties();
                InputStream resourceAsStream = VersionChecker.class.getClassLoader().getResourceAsStream(propertyPrefix + ".properties");
                properties.load(resourceAsStream);
                String version = properties.getProperty("version", "?");
                if ( version.equals("?") || !versionValid(version) ) {
                    LOG.error("version of " + propertyPrefix + " is " + version + " and considered NOT ACCEPTABLE");
                    return false;
                } else {
                    LOG.info("version of " + propertyPrefix + " is " + version + " and considered ok");
                }
            } catch ( Exception e ) {
                LOG.error("properties from /" + propertyPrefix + ".properties could not be loaded. Versioncheck fails");
                return false;
            }
        }
        return true;
    }

    private boolean versionValid(String versionS) {
        int length = versionS.length();
        if ( length <= 0 ) {
            return false;
        }
        String[] version = versionS.split(DOT_REGEX);
        return versionIsLessEqual(this.versionFrom, version) && versionIsLessEqual(version, this.versionTo);
    }

    private boolean versionIsLessEqual(String[] v1, String[] v2) {
        int iterations = Math.min(v1.length, v2.length);
        if ( iterations <= 0 ) {
            return false;
        }
        for ( int i = 0; i < iterations; i++ ) {
            int i1;
            int i2;
            try {
                String s1 = v1[i];
                String s2 = v2[i];
                if ( s1.endsWith("-SNAPSHOT") ) {
                    s1 = s1.substring(0, s1.length() - 9);
                }
                if ( s2.endsWith("-SNAPSHOT") ) {
                    s2 = s2.substring(0, s2.length() - 9);
                }
                i1 = Integer.parseInt(s1);
                i2 = Integer.parseInt(s2);
                if ( i1 > i2 ) {
                    return false;
                }
            } catch ( Exception e ) {
                LOG.error("invalid version string. Either " + Arrays.toString(v1) + " or " + Arrays.toString(v2), e);
                return false;
            }
        }
        return true;
    }
}
