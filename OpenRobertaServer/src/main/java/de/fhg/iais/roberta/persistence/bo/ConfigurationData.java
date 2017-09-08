package de.fhg.iais.roberta.persistence.bo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.DbcException;

@Entity
@Table(name = "CONFIGURATION_DATA")
public class ConfigurationData {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationData.class);

    @Id
    @Column(name = "CONFIGURATION_HASH")
    private String configurationHash;

    @Column(name = "CONFIGURATION_TEXT")
    private String configurationText;

    protected ConfigurationData() {
        // Hibernate
    }

    /**
     * create a new configuration
     *
     * @param name the name of the configuration, not null
     * @param owner the user who created and thus owns the program
     */
    public ConfigurationData(String configurationText) {
        this.configurationHash = createHash(configurationText);
        this.configurationText = configurationText;
    }

    public String getConfigurationHash() {
        return configurationHash;
    }

    public String getConfigurationText() {
        return configurationText;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((configurationHash == null) ? 0 : configurationHash.hashCode());
        result = prime * result + ((configurationText == null) ? 0 : configurationText.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        ConfigurationData other = (ConfigurationData) obj;
        if ( configurationHash == null ) {
            if ( other.configurationHash != null ) {
                return false;
            }
        } else if ( !configurationHash.equals(other.configurationHash) ) {
            return false;
        }
        if ( configurationText == null ) {
            if ( other.configurationText != null ) {
                return false;
            }
        } else if ( !configurationText.equals(other.configurationText) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConfigurationData [configurationHash=" + configurationHash + "]";
    }

    public static String createHash(String inputString) {
        try (InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8))) {
            String hash = DigestUtils.sha256Hex(inputStream);
            return hash;
        } catch ( Exception e ) {
            String msg = "Severe Problem. Could not convert a string to a hash";
            LOG.error(msg, e);
            throw new DbcException(msg, e);
        }
    }

}
