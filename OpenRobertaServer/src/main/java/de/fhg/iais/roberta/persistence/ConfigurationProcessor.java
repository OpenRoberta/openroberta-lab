package de.fhg.iais.roberta.persistence;

import java.util.ArrayList;
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

    public Configuration getConfiguration(String configurationName, int ownerId) {
        if ( !Util.isValidJavaIdentifier(configurationName) ) {
            setError("configuration name name is not a valid identifier: " + configurationName);
            return null;
        } else if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Configuration configuration = configurationDao.load(configurationName, owner);
            setResult(configuration != null, "loading of configuration " + configurationName + ".");
            return configuration;
        } else {
            setError("configuration load illegal if not logged in");
            return null;
        }
    }

    public void updateConfiguration(String configurationName, int ownerId, String configurationText) {
        if ( !Util.isValidJavaIdentifier(configurationName) ) {
            setError("configuration name is not a valid identifier: " + configurationName);
        } else if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Configuration configuration = configurationDao.persistConfigurationText(configurationName, owner, configurationText);
            setResult(configuration != null, "saving configuration " + configurationName + " to db.");
        } else {
            this.httpSessionState.setConfigurationNameAndConfiguration(configurationName, configurationText);
            setResult(true, "saving configuration " + configurationName + " to session.");
        }
    }

    public List<String> getConfigurationNames(int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        List<Configuration> programs = configurationDao.loadAll(owner);
        List<String> configurationNames = new ArrayList<>();
        for ( Configuration program : programs ) {
            configurationNames.add(program.getName());
        }
        setSuccess("found " + configurationNames.size() + " configurations");
        return configurationNames;
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
            configurationInfo.put(program.getIconNumber());
            configurationInfo.put(program.getCreated().toString());
            configurationInfo.put(program.getLastChanged().toString());
        }
        setSuccess("found " + configurationInfos.length() + " configuration(s)");
        return configurationInfos;
    }

    public void deleteByName(String configurationName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        int rowCount = configurationDao.deleteByName(configurationName, owner);
        setResult(rowCount > 0, "delete of configuration " + configurationName + ".");
    }
}