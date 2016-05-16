package couchbase.com.mvig_android_user;

/**
 * Created by Mustafa on 16.5.2016.
 */
public class Message {
    private String message_body;
    private String messag_receiver;

    public Message(String message_body, String messag_receiver) {
        this.message_body = message_body;
        this.messag_receiver = messag_receiver;
    }

    public String getMessage_body() {
        return message_body;
    }

    public void setMessage_body(String message_body) {
        this.message_body = message_body;
    }

    public String getMessag_receiver() {
        return messag_receiver;
    }

    public void setMessag_receiver(String messag_receiver) {
        this.messag_receiver = messag_receiver;
    }
}
