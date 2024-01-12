package com.lynsis.velocitymail.storage;

import java.time.LocalDateTime;

public class Message {
    public final String message;
    public final String receiverUuid;
    public final String senderUuid;
    public final LocalDateTime dateTime;

    public Message(String senderUuid, String receiverUuid, String message, LocalDateTime dateTime) {
        this.message = message;
        this.receiverUuid = receiverUuid;
        this.senderUuid = senderUuid;
        this.dateTime = dateTime;
    }
}
