package com.example.book_a_court.ui.chat;

public class Messages {
    String message;
    String senderId;
    String receiverId;
    long timeStamp;

    public Messages() {
    }

    public Messages(String message, String senderId, String receiverId, long timeStamp) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timeStamp = timeStamp;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


}
