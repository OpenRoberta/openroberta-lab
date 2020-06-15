package de.fhg.iais.roberta.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.ConfigurationData;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

public class ConfigurationProcessor extends AbstractProcessor {
    public ConfigurationProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState.getUserId());
    }

    public String getConfigurationText(String configurationName, int userId, String robotName) {
        if ( !Util.isValidJavaIdentifier(configurationName) ) {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("CONFIG_NAME", configurationName);
            setStatus(ProcessorStatus.FAILED, Key.CONFIGURATION_ERROR_ID_INVALID, processorParameters);
            return null;
        } else {
            ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
            Configuration configuration = null;
            RobotDao robotDao = new RobotDao(this.dbSession);
            Robot robot = robotDao.loadRobot(robotName);
            if ( isUserLoggedIn() ) {
                UserDao userDao = new UserDao(this.dbSession);
                User owner = userDao.get(userId);
                configuration = configurationDao.load(configurationName, owner, robot);
            } else {
                configuration = configurationDao.load(configurationName, null, robot);
            }
            if ( configuration == null ) {
                setStatus(ProcessorStatus.FAILED, Key.CONFIGURATION_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
                return null;
            } else {
                ConfigurationData configurationData = configurationDao.load(configuration.getConfigurationHash());
                if ( configurationData == null ) {
                    setStatus(ProcessorStatus.FAILED, Key.CONFIGURATION_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
                    return null;
                } else {
                    setStatus(ProcessorStatus.SUCCEEDED, Key.CONFIGURATION_GET_ONE_SUCCESS, new HashMap<>());
                    return configurationData.getConfigurationText();
                }
            }
        }
    }

    /**
     * update a given configuration owned by a given user. Overwrites an existing configuration if mayExist == true.
     *
     * @param configurationName the name of the configuration
     * @param ownerId the owner of the configuration
     * @param robotName
     * @param configurationText the new configuration text
     * @param mayExist true, if an existing configuration may be changed; false if a configuration may be stored only, if it does not exist in the database
     */
    public void updateConfiguration(String configurationName, int ownerId, String robotName, String configurationText, boolean mayExist) {
        if ( !Util.isValidJavaIdentifier(configurationName) ) {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("CONFIG_NAME", configurationName);
            setStatus(ProcessorStatus.FAILED, Key.CONFIGURATION_ERROR_ID_INVALID, processorParameters);
            return;
        }
        if ( isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Robot robot = robotDao.loadRobot(robotName);
            if ( configurationDao.persistConfigurationText(configurationName, owner, robot, configurationText, mayExist) ) {
                setStatus(ProcessorStatus.SUCCEEDED, Key.CONFIGURATION_SAVE_SUCCESS, new HashMap<>());
            } else {
                setStatus(ProcessorStatus.FAILED, Key.CONFIGURATION_SAVE_ERROR, new HashMap<>());
            }
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_ERROR_NOT_LOGGED_IN, new HashMap<>());
        }
    }

    public JSONArray getConfigurationInfo(int ownerId, String robotName) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        RobotDao robotDao = new RobotDao(this.dbSession);
        Robot robot = robotDao.loadRobot(robotName);
        List<Configuration> programs = configurationDao.loadAll(owner, robot);
        JSONArray configurationInfos = new JSONArray();
        for ( Configuration program : programs ) {
            JSONArray configurationInfo = new JSONArray();
            configurationInfos.put(configurationInfo);
            configurationInfo.put(program.getName());
            configurationInfo.put(program.getOwner().getAccount());
            configurationInfo.put(program.getCreated().getTime());
            configurationInfo.put(program.getLastChanged().getTime());
        }
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("CONFIG_LENGTH", "" + configurationInfos.length());
        setStatus(ProcessorStatus.SUCCEEDED, Key.CONFIGURATION_GET_ALL_SUCCESS, processorParameters);
        return configurationInfos;
    }

    public void deleteByName(String configurationName, int ownerId, String robotName) {
        UserDao userDao = new UserDao(this.dbSession);
        ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
        User owner = userDao.get(ownerId);
        RobotDao robotDao = new RobotDao(this.dbSession);
        Robot robot = robotDao.loadRobot(robotName);
        int rowCount = configurationDao.deleteByName(configurationName, owner, robot);
        if ( rowCount > 0 ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.CONFIGURATION_DELETE_SUCCESS, new HashMap<>());
        } else {
            setStatus(ProcessorStatus.FAILED, Key.CONFIGURATION_DELETE_ERROR, new HashMap<>());
        }
    }
}
