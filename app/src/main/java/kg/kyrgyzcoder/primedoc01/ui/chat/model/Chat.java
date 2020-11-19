package kg.kyrgyzcoder.primedoc01.ui.chat.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Chat {

    private String clientId;
    private String adminId;
    private String adminPhone;
    private String lastMessage;
    private String lastMessageSenderId;
    private Boolean chatStarted;
    private Timestamp lastMessageTime;
    private String userPhone;
    private String doctorName;
    private String doctorSurname;

    public Chat(String clientId, String adminId, String adminPhone, String lastMessage, String lastMessageSenderId, Boolean chatStarted, Timestamp lastMessageTime, String userPhone, String doctorName, String doctorSurname) {
        this.clientId = clientId;
        this.adminId = adminId;
        this.adminPhone = adminPhone;
        this.lastMessage = lastMessage;
        this.lastMessageSenderId = lastMessageSenderId;
        this.chatStarted = chatStarted;
        this.lastMessageTime = lastMessageTime;
        this.userPhone = userPhone;
        this.doctorName = doctorName;
        this.doctorSurname = doctorSurname;
    }

    public Chat() {
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorSurname() {
        return doctorSurname;
    }

    public void setDoctorSurname(String doctorSurname) {
        this.doctorSurname = doctorSurname;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public Boolean getChatStarted() {
        return chatStarted;
    }

    public void setChatStarted(Boolean chatStarted) {
        this.chatStarted = chatStarted;
    }

    public Timestamp getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "clientId='" + clientId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", adminPhone='" + adminPhone + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageSenderId='" + lastMessageSenderId + '\'' +
                ", chatStarted=" + chatStarted +
                ", lastMessageTime=" + lastMessageTime +
                '}';
    }
}
