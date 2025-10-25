package com.nsr.ai.api;

/**
 * Represents a response received from the AI.
 */
public class AIResponse {
    private final String response;
    private final boolean success;

    /**
     * Constructs a new AIResponse.
     * @param response The text content of the AI's response.
     * @param success True if the AI successfully generated a response, false otherwise.
     */
    public AIResponse(String response, boolean success) {
        this.response = response;
        this.success = success;
    }

    /**
     * Gets the text content of the AI's response.
     * @return The AI's response text.
     */
    public String getResponse() { return response; }
    /**
     * Checks if the AI response was successful.
     * @return True if the response was successful, false otherwise.
     */
    public boolean isSuccess() { return success; }
}
