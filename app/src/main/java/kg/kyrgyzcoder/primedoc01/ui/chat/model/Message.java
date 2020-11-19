package kg.kyrgyzcoder.primedoc01.ui.chat.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Message {

    private String sender;
    private String receiver;
    private String message;
    private Timestamp time;
    private String type;
    private String image;
    private String senderName;

    public Message(String sender, String receiver, String message, String type, String image) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
        this.image = image;
    }

    public Message() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderUid='" + sender + '\'' +
                ", receiverUid='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", type='" + type + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
