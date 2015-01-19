package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Util;

public class ConfigurationProcessor extends AbstractProcessor {
    public ConfigurationProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public Configuration getConfiguration(String configurationName, int userId) {
        if ( !Util.isValidJavaIdentifier(configurationName) ) {
            setError("configuration.error.id_invalid", configurationName);
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
                setError("configuration.get_one.error.not_found");
            } else {
                setSuccess("configuration.get_one.success");
            }
            return configuration;
        }
    }

    public void updateConfiguration(String configurationName, int ownerId, String configurationText) {
        if ( !Util.isValidJavaIdentifier(configurationName) ) {
            setError("configuration.error.id_invalid", configurationName);
            return;
        }
        this.httpSessionState.setConfigurationNameAndConfiguration(configurationName, configurationText);
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Configuration configuration = configurationDao.persistConfigurationText(configurationName, owner, configurationText);
            if ( configuration == null ) {
                setError("configuration.save.error.not_saved_to_db");
                return;
            }
        }
        setSuccess("configuration.save.success");
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
        setSuccess("configuration.get_all.success", "" + configurationInfos.length());
        return configurationInfos;
    }

    public void deleteByName(String configurationName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        int rowCount = configurationDao.deleteByName(configurationName, owner);
        if ( rowCount > 0 ) {
            setSuccess("configuration.delete.success");
        } else {
            setError("configuration.delete.error");
        }
    }
}