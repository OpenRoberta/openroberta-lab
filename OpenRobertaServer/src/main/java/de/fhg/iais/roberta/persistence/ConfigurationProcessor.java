package de.fhg.iais.roberta.persistence;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.javaServer.resources.OpenRobertaSessionState;
import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;

public class ConfigurationProcessor {
    private final OpenRobertaSessionState httpSessionState;
    private final SessionWrapper dbSession;

    public ConfigurationProcessor(SessionWrapper dbSession, OpenRobertaSessionState httpSessionState) {
        this.dbSession = dbSession;
        this.httpSessionState = httpSessionState;
    }

    public Configuration getConfiguration(String configurationName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Configuration program = configurationDao.load(configurationName, owner);
        return program;
    }

    public Configuration updateConfiguration(String configurationName, int ownerId, String configurationText) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Configuration configuration = configurationDao.persistConfigurationText(configurationName, owner, configurationText);
        return configuration;
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
        return configurationInfos;
    }

    public int deleteByName(String programName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configuraionDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        return configuraionDao.deleteByName(programName, owner);
    }

}
