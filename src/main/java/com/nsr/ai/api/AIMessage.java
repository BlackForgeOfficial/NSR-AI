package com.nsr.ai.api;

import java.util.UUID;

/**
 * Represents a message sent to or received from the AI.
 */
public class AIMessage {
    private final String content;
    private final UUID senderId;

    /**
     * Constructs a new AIMessage.
     * @param content The text content of the message.
     * @param senderId The UUID of the sender (e.g., player).
     */
    public AIMessage(String content, UUID senderId) {
        this.content = content;
        this.senderId = senderId;
    }

    /**
     * Gets the text content of the message.
     * @return The message content.
     */
    public String getContent() { return content; }
    /**
     * Gets the UUID of the message sender.
     * @return The sender's UUID.
     */
    public UUID getSenderId() { return senderId; }
}
