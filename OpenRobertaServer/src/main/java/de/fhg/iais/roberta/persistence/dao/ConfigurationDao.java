package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;

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
     * @param name the name of the program, never null
     * @param owner the user who owns the configuration, never null
     * @param configurationText the configuration text, maybe null
     * @return the persisted configuration object
     */
    public boolean persistConfigurationText(String name, User owner, String configurationText, boolean mayExist) {
        Assert.notNull(name);
        Assert.notNull(owner);
        Configuration configuration = load(name, owner);
        if ( configuration == null ) {
            configuration = new Configuration(name, owner);
            this.session.save(configuration);
            return true;
        } else if ( mayExist ) {
            configuration.setConfigurationText(configurationText);
            return true;
        } else {
            return false;
        }
    }

    /**
     * load a configuration from the database, identified by its owner and its name (both make up the "business" key of a configuration)
     *
     * @param projectName the project, never null
     * @param programName the name of the program, never null
     * @return the program, null if the program is not found
     */
    public Configuration load(String name, User user) {
        Assert.notNull(name);
        Query hql;
        if ( user != null ) {
            hql = this.session.createQuery("from Configuration where name=:name and (owner is null or owner=:owner)");
            hql.setString("name", name);
            hql.setEntity("owner", user);
        } else {
            hql = this.session.createQuery("from Configuration where name=:name and owner is null");
            hql.setString("name", name);
        }
        @SuppressWarnings("unchecked")
        List<Configuration> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    public int deleteByName(String name, User owner) {
        Configuration toBeDeleted = load(name, owner);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }

    /**
     * load all configurations persisted in the database which are owned by a user given
     *
     * @return the list of all configurations, may be an empty list, but never null
     */
    public List<Configuration> loadAll(User owner) {
        Query hql = this.session.createQuery("from Configuration where owner=:owner");
        hql.setEntity("owner", owner);
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
}
