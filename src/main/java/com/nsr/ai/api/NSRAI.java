package com.nsr.ai.api;

import org.bukkit.entity.Player;
import java.util.Map;
import java.util.Collections; // For emptyMap

/**
 * This is a utility class with static methods for addons to interact with the NSR-AI plugin.
 * The methods in this class are empty stubs and only contain comments.
 * The actual implementation is in the closed-source NSR-AI plugin.
 */
public class NSRAI {

    /**
     * Sends a message to the AI on behalf of a player. This method is a stub and contains no logic.
     * The actual implementation is handled by the core NSR-AI plugin.
     *
     * @param player  The player sending the message.
     * @param message The message to send.
     */
    public static void sendAIMessage(Player player, String message) {
        // This method is a stub. The actual implementation is in the closed-source NSR-AI plugin.
        // Addons should call this method to allow the core plugin to process AI interactions.
    }

    /**
     * Registers an addon with NSR-AI. This method is a stub and contains no logic.
     * The actual implementation is handled by the core NSR-AI plugin.
     *
     * @param addon The addon to register. It must implement the {@link AIAddon} interface.
     */
    public static void registerAddon(AIAddon addon) {
        // This method is a stub. The actual implementation is in the closed-source NSR-AI plugin.
        // Addons should call this method during their onEnable to register themselves with NSR-AI.
    }

    /**
     * Adds a knowledge entry to the AI's knowledge base. This method is a stub and contains no logic.
     * The actual implementation is handled by the core NSR-AI plugin.
     *
     * @param player The player initiating the action (for context/permissions).
     * @param keyword The main keyword for the knowledge entry.
     * @param aiGeneratedHeading The AI-generated heading for the entry.
     * @param messageContent The content of the knowledge entry.
     */
    public static void addKnowledgeEntry(Player player, String keyword, String aiGeneratedHeading, String messageContent) {
        // This method is a stub. The actual implementation is in the closed-source NSR-AI plugin.
    }

    /**
     * Removes a knowledge entry from the AI's knowledge base. This method is a stub and contains no logic.
     * The actual implementation is handled by the core NSR-AI plugin.
     *
     * @param player The player initiating the action (for context/permissions).
     * @param key The key of the knowledge entry to remove.
     * @return The content of the removed entry, or null if not found.
     */
    public static String removeKnowledgeEntry(Player player, String key) {
        // This method is a stub. The actual implementation is in the closed-source NSR-AI plugin.
        return null;
    }

    /**
     * Retrieves all knowledge base entries. This method is a stub and contains no logic.
     * The actual implementation is handled by the core NSR-AI plugin.
     *
     * @param player The player initiating the action (for context/permissions).
     * @return A map of all knowledge base entries (keyword/heading -> content).
     */
    public static Map<String, String> getKnowledgeBase(Player player) {
        // This method is a stub. The actual implementation is in the closed-source NSR-AI plugin.
        return Collections.emptyMap();
    }

    /**
     * Retrieves information about a player's active pet. This method is a stub and contains no logic.
     * The actual implementation is handled by the core NSR-AI plugin.
     *
     * @param player The player whose pet information is requested.
     * @return A map containing pet information (e.g., "name", "type", "mood", "bond"), or an empty map if no pet is found.
     */
    public static Map<String, String> getPetInfo(Player player) {
        // This method is a stub. The actual implementation is in the closed-source NSR-AI plugin.
        return Collections.emptyMap();
    }

    /**
     * Attempts to tame a pet for a player. This method is a stub and contains no logic.
     * The actual implementation is handled by the core NSR-AI plugin.
     *
     * @param player The player attempting to tame a pet.
     * @param petType The type of pet to tame (e.g., "dog", "cat").
     * @return True if the pet was successfully tamed (in the core plugin's logic), false otherwise.
     */
    public static boolean tamePet(Player player, String petType) {
        // This method is a stub. The actual implementation is in the closed-source NSR-AI plugin.
        return false;
    }
}