package com.example.chatbotapp;

public class Message {
    private String sender;
    private String message;
    private boolean isSentByUser;

    public Message(String sender, String message, boolean isSentByUser) {
        this.sender = sender;
        this.message = message;
        this.isSentByUser = isSentByUser;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

}
