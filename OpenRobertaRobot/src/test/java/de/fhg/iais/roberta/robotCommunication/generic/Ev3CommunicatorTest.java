package de.fhg.iais.roberta.robotCommunication.generic;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.robotCommunication.Ev3CommunicationData;
import de.fhg.iais.roberta.robotCommunication.Ev3Communicator;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Ev3CommunicatorTest {
    static Ev3CommunicationData badRegistration = new Ev3CommunicationData(null, null, null, null, null, null, null);
    static Ev3CommunicationData goodRegistration = new Ev3CommunicationData("12345678", "00:11:22:33:44:55", null, null, null, null, null);

    @Test
    public void testFirstCanRegister() throws Exception {
        Assert.assertTrue(new Ev3Communicator().addNewRegistration(goodRegistration));
    }

    @Test(expected = DbcException.class)
    public void testBadRegistrationIsRejected() throws Exception {
        new Ev3Communicator().addNewRegistration(badRegistration);
    }

    @Test(expected = DbcException.class)
    public void testCantRegisterTwice() throws Exception {
        Ev3Communicator c = new Ev3Communicator();
        c.addNewRegistration(goodRegistration);
        c.addNewRegistration(goodRegistration);
    }

}