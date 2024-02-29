package org.example.services;



import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;



public class Sendmail {

    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    Properties props = System.getProperties();
    final String username = "slimfady.hanafi7@gmail.com";
    final String password = "mslf xsvw cdof jfzi";


    public void envoyer(String Toemail, String Subject , String Object) {

        props.setProperty("mail.smtp.host", "imap.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465"); // Use port 465 for SMTPS
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.transport.protocol", "smtp");

        try {
            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            // -- Create a new message --
            MimeMessage msg = new MimeMessage(session);

            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Toemail, false));
            msg.setSubject(Subject);
            msg.setText(Object);
            msg.setSentDate(new Date());
            Transport.send(msg);
            System.out.println("Message sent.");
        } catch (MessagingException e) {
            System.out.println("Erreur d'envoi, cause: " + e);
        }


    }

}