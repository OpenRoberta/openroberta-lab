package de.fhg.iais.roberta.testutil;

import java.util.Properties;

import org.junit.Test;

import de.fhg.iais.roberta.main.MailManagement;
import de.fhg.iais.roberta.util.Util;

public class SendMail {

    @Test
    public void test() {
        Properties properties = Util.loadProperties("classpath:openRobertaMailServer.properties");
        String auth = properties.getProperty("mail.smtp.auth");
        String starttls = properties.getProperty("mail.smtp.starttls.enable");
        String host = properties.getProperty("mail.smtp.host");
        String port = properties.getProperty("mail.smtp.port");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        MailManagement mailManagement = new MailManagement(auth, starttls, host, port, username, password);
        mailManagement.send("Kostadin.Cvejoski@iais.fraunhofer.de", "Recover Password", ";ADKHSKDJSHSKASDGASHDGJAHSD");
    }

}
