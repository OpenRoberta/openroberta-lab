package de.fhg.iais.roberta.robotCommunication;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class RobotCommunicatorTest {
    static RobotCommunicationData badRegistration = new RobotCommunicationData(null, null, null, null, null, null, null, null, null);
    static RobotCommunicationData goodRegistration1 = new RobotCommunicationData("12345678", null, "00:11:22:33:44:55", null, null, null, null, null, null);
    static RobotCommunicationData goodRegistration2 = new RobotCommunicationData("12345678", null, "13:05:98:29:12:99", null, null, null, null, null, null);

    @Test
    public void testFirstCanRegister() throws Exception {
        RobotCommunicator rc = new RobotCommunicator();
        expect(rc.addNewRegistration(goodRegistration1), RobotCommunicator.RegistrationRequest.NEW_REGISTRATION_REQUEST);
    }

    @Test(expected = DbcException.class)
    public void testBadRegistrationIsRejected() throws Exception {
        RobotCommunicator rc = new RobotCommunicator();
        rc.addNewRegistration(badRegistration);
    }

    @Test
    public void testRegisterTwiceWithSameIp() {
        RobotCommunicator rc = new RobotCommunicator();
        rc.addNewRegistration(goodRegistration1);
        expect(rc.addNewRegistration(goodRegistration1), RobotCommunicator.RegistrationRequest.REPEATED_REGISTRATION_REQUEST);
    }

    @Test
    public void testCantRegisterTwice() {
        RobotCommunicator rc = new RobotCommunicator();
        rc.addNewRegistration(goodRegistration1);
        expect(rc.addNewRegistration(goodRegistration2), RobotCommunicator.RegistrationRequest.TOKEN_INVALID);
    }
    
    private void expect(RobotCommunicator.RegistrationRequest result, RobotCommunicator.RegistrationRequest expected) {
        Assert.assertEquals(expected, result);
    }
}