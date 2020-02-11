package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.ConfigurationData;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * DAO class to load and store configuration objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 *
 * @author rbudde
 */
public class ConfigurationDao extends AbstractDao<Program> {
    /**
     * create a new DAO for configurations. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public ConfigurationDao(DbSession session) {
        super(Program.class, session);
    }

    /**
     * make a configuration object and persist it (if the configuration, identified by owner&name, does not exist) or update it (if the configuration exists)
     *
     * @param name the name of the configuration, never null
     * @param owner the user who owns the configuration, never null
     * @param robot the robot the configuration is defined for, never null
     * @param configurationText the configuration text, never null * @return the configuration hash; null, if an error occurs (e.g. mayExist==false, but exists)
     */
    public boolean persistConfigurationText(String name, User owner, Robot robot, String configurationText, boolean mayExist) {
        Assert.notNull(name);
        Assert.notNull(owner);
        Assert.notNull(robot);
        Assert.notNull(configurationText);
        String configurationHash = optionalStore(configurationText);
        Configuration configuration = load(name, owner, robot);
        if ( configuration == null ) {
            configuration = new Configuration(name, owner, robot);
            configuration.setConfigurationHash(configurationHash);
            this.session.save(configuration);
            return true;
        } else if ( mayExist ) {
            configuration.setConfigurationHash(configurationHash);
            return true;
        } else {
            return false;
        }
    }

    /**
     * guarantee, that a ConfigurationData object is accessible with the configurationText supplied. The <i>key</i> of the ConfigurationData object is a hash of
     * its configurationText. A ConfigurationData object is either persisted or an existing is re-used.
     *
     * @param configurationText the configuration text, never null
     * @return the configuration hash; null, if an error occurs (e.g. mayExist==false, but exists)
     */
    public String persistConfigurationHash(String configurationText) {
        Assert.notNull(configurationText);
        return optionalStore(configurationText);
    }

    public int deleteByName(String name, User owner, Robot robot) {
        Configuration toBeDeleted = load(name, owner, robot);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }

    /**
     * load a configuration from the database, identified by its name, its owner and the robot it is usable for (these make up the "business" key of a
     * configuration)
     *
     * @param name the name of the configuration, never null
     * @param user the owner of the configuration, never null
     * @param robot the robot this configuration is usable for
     * @return the configuration, null if the configuration is not found
     */
    public Configuration load(String name, User user, Robot robot) {
        Assert.notNull(name);
        Query hql;
        if ( user != null ) {
            hql = this.session.createQuery("from Configuration where name=:name and (owner is null or owner=:owner) and robot=:robot");
            hql.setString("name", name);
            hql.setEntity("owner", user);
            hql.setEntity("robot", robot);
        } else {
            hql = this.session.createQuery("from Configuration where name=:name and owner is null and robot=:robot");
            hql.setString("name", name);
            hql.setEntity("robot", robot);
        }
        @SuppressWarnings("unchecked")
        List<Configuration> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * load a configuration from the database, identified by its content hash
     *
     * @param configHash never null
     * @return the program, null if the program is not found
     */
    public ConfigurationData load(String configHash) {
        Assert.notNull(configHash);
        Query hql = this.session.createQuery("from ConfigurationData where configurationHash=:configurationHash");
        hql.setString("configurationHash", configHash);
        @SuppressWarnings("unchecked")
        List<ConfigurationData> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * @return
     */

    public List<ConfigurationData> loadAllConfigurationData() {
        Query hql = this.session.createQuery("from ConfigurationData");
        @SuppressWarnings("unchecked")
        List<ConfigurationData> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * load all configurations persisted in the database which are owned by a user given
     *
     * @return the list of all configurations, may be an empty list, but never null
     */
    public List<Configuration> loadAll(User owner, Robot robot) {
        Query hql = this.session.createQuery("from Configuration where owner=:owner and robot=:robot");
        hql.setEntity("owner", owner);
        hql.setEntity("robot", robot);
        @SuppressWarnings("unchecked")
        List<Configuration> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * load all Configurations persisted in the database
     *
     * @return the list of all programs, may be an empty list, but never null
     */
    public List<Configuration> loadAll() {
        Query hql = this.session.createQuery("from Configuration");
        @SuppressWarnings("unchecked")
        List<Configuration> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    private String optionalStore(String configurationText) {
        String configurationHash = ConfigurationData.createHash(configurationText);
        if ( this.session.getSession().get(ConfigurationData.class, configurationHash) == null ) {
            this.session.save(new ConfigurationData(configurationText));
        }
        return configurationHash;
    }
}
