package com.lynsis.velocitymail.storage;

public class Message {
    public final String message;
    public final String receiverUuid;
    public final String senderUuid;

    public Message(String senderUuid, String receiverUuid, String message) {
        this.message = message;
        this.receiverUuid = receiverUuid;
        this.senderUuid = senderUuid;
    }
}
