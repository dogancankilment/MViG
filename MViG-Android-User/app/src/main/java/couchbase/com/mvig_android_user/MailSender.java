package couchbase.com.mvig_android_user;

import android.os.AsyncTask;
import android.util.Log;

import com.sun.mail.smtp.SMTPTransport;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Mustafa on 1.5.2016.
 */
public class MailSender extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        try {
            String mailToSend = params[0];      //parametre olarak gelen mailadresi değişkene alınıyor.
            String activationCodeToSend = params[1];  //parametre olarak gelen aktivasyon kodu değişkene alınıyor.
            Log.i("Uzunluk",mailToSend);
            sendMail(mailToSend, activationCodeToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendMail(String mailAdress, String activationCode) throws Exception {

        Log.d("TAG", "sendMail");

        Properties props = System.getProperties();

        props.put("mail.smtp.host", "smtp.gmail.com");

        props.put("mail.smpt.auth", "true");

        props.put("mail.smtp.port", "587");

        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress("SEND_FROM"));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailAdress, false));

        msg.setSubject("MViG Application Activation Code");

        msg.setText("\nYour activation code: " + activationCode + "\n \n Please activate your account with code");

        msg.setSentDate(new Date());

        SMTPTransport smtp = (SMTPTransport) session.getTransport("smtp");
        try {
            smtp.connect("smtp.gmail.com", "musta4a15@gmail.com", "24469444984");
            smtp.sendMessage(msg, msg.getAllRecipients());
        } finally {
            smtp.close();
        }
    }
}
