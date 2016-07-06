package de.fhg.iais.roberta.robotCommunication.generic;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class RobotCommunicatorTest {
    static RobotCommunicationData badRegistration = new RobotCommunicationData(null, null, null, null, null, null, null);
    static RobotCommunicationData goodRegistration = new RobotCommunicationData("12345678", "00:11:22:33:44:55", null, null, null, null, null);

    @Test
    public void testFirstCanRegister() throws Exception {
        Assert.assertTrue(new RobotCommunicator().addNewRegistration(goodRegistration));
    }

    @Test(expected = DbcException.class)
    public void testBadRegistrationIsRejected() throws Exception {
        new RobotCommunicator().addNewRegistration(badRegistration);
    }

    @Test(expected = DbcException.class)
    public void testCantRegisterTwice() throws Exception {
        RobotCommunicator c = new RobotCommunicator();
        c.addNewRegistration(goodRegistration);
        c.addNewRegistration(goodRegistration);
    }

}