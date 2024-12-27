package com.example.courierapp.auth;

import android.os.AsyncTask;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender extends AsyncTask<Void, Void, Void> {
    private String username;
    private String password;
    private String recipient;
    private String subject;
    private String body;

    public EmailSender(String username, String password, String recipient, String subject, String body) {
        this.username = username;
        this.password = password;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // Создайте свойство для настройки подключения SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Создайте объект MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setHeader("Content-Type", "text/html");
            message.setSubject(subject, "UTF-8");
//            message.setText(body);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = body;
            messageBodyPart.setContent(htmlText,
                    "text/html; charset=utf-8");

            // Multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            // Отправить сообщение
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


