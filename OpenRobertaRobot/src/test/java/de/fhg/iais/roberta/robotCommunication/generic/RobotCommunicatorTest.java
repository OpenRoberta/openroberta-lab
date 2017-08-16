package de.fhg.iais.roberta.robotCommunication.generic;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class RobotCommunicatorTest {
    static RobotCommunicationData badRegistration = new RobotCommunicationData(null, null, null, null, null, null, null, null, null);
    static RobotCommunicationData goodRegistration1 = new RobotCommunicationData("12345678", null, "00:11:22:33:44:55", null, null, null, null, null, null);
    static RobotCommunicationData goodRegistration2 = new RobotCommunicationData("12345678", null, "13:05:98:29:12:99", null, null, null, null, null, null);

    @Test
    public void testFirstCanRegister() throws Exception {
        RobotCommunicator robotCommunicator = new RobotCommunicator();
        Assert.assertTrue(robotCommunicator.addNewRegistration(goodRegistration1));
    }

    @Test(expected = DbcException.class)
    public void testBadRegistrationIsRejected() throws Exception {
        RobotCommunicator robotCommunicator = new RobotCommunicator();
        robotCommunicator.addNewRegistration(badRegistration);
    }

    @Test
    public void testRegisterTwiceWithSameIp() throws Exception {
        RobotCommunicator robotCommunicator = new RobotCommunicator();
        robotCommunicator.addNewRegistration(goodRegistration1);
        Assert.assertTrue(robotCommunicator.addNewRegistration(goodRegistration1));
    }

    @Test
    public void testCantRegisterTwice() throws Exception {
        RobotCommunicator robotCommunicator = new RobotCommunicator();
        robotCommunicator.addNewRegistration(goodRegistration1);
        Assert.assertFalse(robotCommunicator.addNewRegistration(goodRegistration2));
    }
}