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
        @Named("reset.url") String resetUrl,
        @Named("reset.text.de") String resetTextDe,
        @Named("reset.text.en") String resetTextEn,
        @Named("reset.subject.de") String resetSubjectDe,
        @Named("reset.subject.en") String resetSubjectEn,
        @Named("activate.url") String activateUrl,
        @Named("activate.text.young.de") String activateTextYoungDe,
        @Named("activate.text.young.en") String activateTextYoungEn,
        @Named("activate.text.old.de") String activateTextOldDe,
        @Named("activate.text.old.en") String activateTextOldEn,
        @Named("activate.subject.de") String activateSubjectDe,
        @Named("activate.subject.en") String activateSubjectEn,
        @Named("username") final String username,
        @Named("password") final String password) {
        this.props.put("mail.smtp.auth", auth);
        this.props.put("mail.smtp.starttls.enable", starttls);
        this.props.put("mail.smtp.host", host);
        this.props.put("mail.smtp.ssl.trust", host);
        this.props.put("mail.smtp.port", port);
        this.props.put("reset.url", resetUrl);
        this.props.put("reset.text.de", resetTextDe);
        this.props.put("reset.text.en", resetTextEn);
        this.props.put("reset.subject.de", resetSubjectDe);
        this.props.put("reset.subject.en", resetSubjectEn);
        this.props.put("activate.url", activateUrl);
        this.props.put("activate.text.old.de", activateTextOldDe);
        this.props.put("activate.text.old.en", activateTextOldEn);
        this.props.put("activate.text.young.de", activateTextYoungDe);
        this.props.put("activate.text.young.en", activateTextYoungEn);
        this.props.put("activate.subject.de", activateSubjectDe);
        this.props.put("activate.subject.en", activateSubjectEn);
        this.props.put("username", username);

        this.session = Session.getInstance(this.props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void send(String to, String subject, String[] body, String lang, boolean isYoungerThen14) throws AddressException, MessagingException {
        // TODO support for more languages
        String language = lang.toLowerCase().equals("de") ? "DE" : "en";
        String mailText = body[0];
        String mailSubject = "";
        if ( subject.equals("reset") ) {
            mailSubject = this.props.getProperty("reset.subject." + language.toLowerCase());
            mailText = this.props.getProperty("reset.text." + language.toLowerCase());
            String url =
                this.props.getProperty("reset.url") != null && !this.props.getProperty("reset.url").isEmpty()
                    ? this.props.getProperty("reset.url")
                    : "https://lab.open-roberta.org/";
            mailText = mailText.replace("$1", body[0]);
            mailText = mailText.replace("$2", url);
            mailText = mailText.replace("$3", body[1]);
        } else if ( subject.equals("activate") ) {
            mailSubject = this.props.getProperty("activate.subject." + language.toLowerCase());
            if ( isYoungerThen14 ) {
                mailText = this.props.getProperty("activate.text.young." + language.toLowerCase());
            } else {
                mailText = this.props.getProperty("activate.text.old." + language.toLowerCase());
            }
            String url =
                this.props.getProperty("activate.url") != null && !this.props.getProperty("activate.url").isEmpty()
                    ? this.props.getProperty("activate.url")
                    : "https://lab.open-roberta.org/";
            mailText = mailText.replace("$1", body[0]);
            mailText = mailText.replace("$2", url);
            mailText = mailText.replace("$3", body[1]);
        }
        Message message = new MimeMessage(this.session);
        message.setFrom(new InternetAddress(this.props.getProperty("username")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(mailSubject);
        message.setText(mailText);

        Transport.send(message);
    }

}