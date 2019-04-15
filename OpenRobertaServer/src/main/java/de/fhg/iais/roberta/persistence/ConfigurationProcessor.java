package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.fhg.iais.roberta.util.Util1;

public class ConfigurationProcessor extends AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProcessor.class);

    public ConfigurationProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public String getConfigurationText(String configName, int userId, String robotName) {
        if ( !Util1.isValidJavaIdentifier(configName) ) {
            setError(Key.CONFIGURATION_ERROR_ID_INVALID, configName);
            return null;
        } else {
            ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
            Configuration configuration = null;
            RobotDao robotDao = new RobotDao(this.dbSession);
            Robot robot = robotDao.loadRobot(robotName);
            if ( this.httpSessionState.isUserLoggedIn() ) {
                UserDao userDao = new UserDao(this.dbSession);
                User owner = userDao.get(userId);
                configuration = configurationDao.load(configName, owner, robot);
            } else {
                configuration = configurationDao.load(configName, null, robot);
            }
            if ( configuration == null ) {
                setError(Key.CONFIGURATION_GET_ONE_ERROR_NOT_FOUND);
                return null;
            } else {
                ConfigurationData configurationData = configurationDao.load(configuration.getConfigurationHash());
                if ( configurationData == null ) {
                    setError(Key.CONFIGURATION_GET_ONE_ERROR_NOT_FOUND);
                    return null;
                } else {
                    setSuccess(Key.CONFIGURATION_GET_ONE_SUCCESS);
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
        if ( !Util1.isValidJavaIdentifier(configurationName) ) {
            setError(Key.CONFIGURATION_ERROR_ID_INVALID, configurationName);
            return;
        }
        this.httpSessionState.setConfigurationNameAndConfiguration(configurationName, configurationText);
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ConfigurationDao configurationDao = new ConfigurationDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Robot robot = robotDao.loadRobot(robotName);
            if ( configurationDao.persistConfigurationText(configurationName, owner, robot, configurationText, mayExist) ) {
                setSuccess(Key.CONFIGURATION_SAVE_SUCCESS);
            } else {
                setError(Key.CONFIGURATION_SAVE_ERROR);
            }
        } else {
            setError(Key.USER_ERROR_NOT_LOGGED_IN);
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
        setSuccess(Key.CONFIGURATION_GET_ALL_SUCCESS, "" + configurationInfos.length());
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
            setSuccess(Key.CONFIGURATION_DELETE_SUCCESS);
        } else {
            setError(Key.CONFIGURATION_DELETE_ERROR);
        }
    }
}
