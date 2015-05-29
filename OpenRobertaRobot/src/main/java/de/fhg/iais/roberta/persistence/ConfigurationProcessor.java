package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

public class ConfigurationProcessor extends AbstractProcessor {
    public ConfigurationProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public Configuration getConfiguration(String configurationName, int userId) {
        if ( !Util.isValidJavaIdentifier(configurationName) ) {
            setError(Key.CONFIGURATION_ERROR_ID_INVALID, configurationName);
            return null;
        } else {
            ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
            Configuration configuration = null;
            if ( this.httpSessionState.isUserLoggedIn() ) {
                UserDao userDao = new UserDao(this.dbSession);
                User owner = userDao.get(userId);
                configuration = configurationDao.load(configurationName, owner);
            } else {
                configuration = configurationDao.load(configurationName, null);
            }
            if ( configuration == null ) {
                setError(Key.CONFIGURATION_GET_ONE_ERROR_NOT_FOUND);
            } else {
                setSuccess(Key.CONFIGURATION_GET_ONE_SUCCESS);
            }
            return configuration;
        }
    }

    /**
     * update a given configuration owned by a given user. Overwrites an existing configuration if mayExist == true.
     *
     * @param configurationName the name of the configuration
     * @param ownerId the owner of the configuration
     * @param configurationText the new configuration text
     * @param mayExist TODO
     * @param mayExist true, if an existing configuration may be changed; false if a configuration may be stored only, if it does not exist in the database
     */
    public void updateConfiguration(String configurationName, int ownerId, String configurationText, boolean mayExist) {
        if ( !Util.isValidJavaIdentifier(configurationName) ) {
            setError(Key.CONFIGURATION_ERROR_ID_INVALID, configurationName);
            return;
        }
        this.httpSessionState.setConfigurationNameAndConfiguration(configurationName, configurationText);
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
            User owner = userDao.get(ownerId);
            boolean success = configurationDao.persistConfigurationText(configurationName, owner, configurationText, mayExist);
            if ( success ) {
                setSuccess(Key.CONFIGURATION_SAVE_SUCCESS);
            } else {
                setError(Key.CONFIGURATION_SAVE_ERROR_NOT_SAVED_TO_DB);
            }
        } else {
            setError(Key.USER_ERROR_NOT_LOGGED_IN);
        }
    }

    public JSONArray getConfigurationInfo(int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        List<Configuration> programs = configurationDao.loadAll(owner);
        JSONArray configurationInfos = new JSONArray();
        for ( Configuration program : programs ) {
            JSONArray configurationInfo = new JSONArray();
            configurationInfos.put(configurationInfo);
            configurationInfo.put(program.getName());
            configurationInfo.put(program.getOwner().getAccount());
            configurationInfo.put(program.getCreated().toString());
            configurationInfo.put(program.getLastChanged().toString());
        }
        setSuccess(Key.CONFIGURATION_GET_ALL_SUCCESS, "" + configurationInfos.length());
        return configurationInfos;
    }

    public void deleteByName(String configurationName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        int rowCount = configurationDao.deleteByName(configurationName, owner);
        if ( rowCount > 0 ) {
            setSuccess(Key.CONFIGURATION_DELETE_SUCCESS);
        } else {
            setError(Key.CONFIGURATION_DELETE_ERROR);
        }
    }
}
