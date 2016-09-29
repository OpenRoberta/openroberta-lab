package de.fhg.iais.roberta.main;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MailManagement {
    private Properties props = new Properties();
    private Session session;

    @Inject
    public MailManagement(
        @Named("mail.smtp.auth") String auth,
        @Named("mail.smtp.starttls.enable") String starttls,
        @Named("mail.smtp.host") String host,
        @Named("mail.smtp.port") String port,
        @Named("username") final String username,
        @Named("password") final String password) {
        this.props.put("mail.smtp.auth", auth);
        this.props.put("mail.smtp.starttls.enable", starttls);
        this.props.put("mail.smtp.host", host);
        this.props.put("mail.smtp.port", port);
        this.props.put("username", username);

        this.session = Session.getInstance(this.props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

    }

    public void send(String to, String subject, String body) throws AddressException, MessagingException {
        Message message = new MimeMessage(this.session);
        message.setFrom(new InternetAddress(this.props.getProperty("username")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

}