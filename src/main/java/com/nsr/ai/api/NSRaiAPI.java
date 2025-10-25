package com.nsr.ai.api;

import org.bukkit.entity.Player;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.nsr.ai.api.AIMessage;
import com.nsr.ai.api.AIResponse;
import com.nsr.ai.api.PetDataSnapshot;
import com.nsr.ai.api.PetListener;
import com.nsr.ai.api.NPCListener;
import com.nsr.ai.api.GUIBuilder;
import com.nsr.ai.api.GUIListener;


/**
 * ⚠️ This API is read-only, cannot bypass NSR-AI security, cannot store or reveal API keys,
 * and cannot be used to create scripted/canned AI responses.
 */
/**
 * The main entry point for the NSR-AI Open-Source API.
 * This final class provides static methods for addons to interact with the NSR-AI core plugin.
 * It acts as a facade, forwarding calls to the internal plugin implementation via reflection.
 *
 * ⚠️ This API is read-only, cannot bypass NSR-AI security, cannot store or reveal API keys,
 * and cannot be used to create scripted/canned AI responses.
 */
public final class NSRaiAPI {

    /**
     * The current major version of the NSR-AI Open-Source API.
     * Addons should check this value for compatibility.
     */
    public static final int API_VERSION = 2;

    // Internal core plugin reference (set via reflection by the core plugin)
    private static Object internalApiInstance; // Represents the internal com.nsr.ai.plugin.api.NSRaiAPI

    private NSRaiAPI() {
        // Private constructor to prevent instantiation
    }

    /**
     * Internal method used by the NSR-AI core plugin to set the internal API instance.
     * Addon developers should NOT call this method.
     * @param instance The internal API instance.
     */
    public static void setInternalApiInstance(Object instance) {
        NSRaiAPI.internalApiInstance = instance;
    }

    /**
     * Calls an internal method of the NSR-AI core plugin via reflection.
     * This method handles the forwarding of public API calls to the actual internal implementation.
     * @param methodName The name of the internal method to call.
     * @param paramTypes An array of Class objects representing the parameter types of the method.
     * @param args The arguments to pass to the method.
     * @param <T> The return type of the method.
     * @return The result of the internal method call.
     * @throws IllegalStateException if the internal API is not initialized, the method is not found,
     *                                an access error occurs, or the internal method throws an exception.
     */
    private static <T> T callInternalMethod(String methodName, Class<?>[] paramTypes, Object... args) {
        if (internalApiInstance == null) {
            throw new IllegalStateException("NSR-AI core plugin not initialized or API not ready.");
        }
        try {
            // Using reflection to call the internal API methods
            // This ensures the public API is a thin facade over the internal implementation
            java.lang.reflect.Method method = internalApiInstance.getClass().getMethod(methodName, paramTypes);
            return (T) method.invoke(internalApiInstance, args);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("NSR-AI core plugin does not support method: " + methodName + ". API mismatch?", e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // Unwrap the real exception thrown by the internal method
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            } else {
                throw new IllegalStateException("Error calling internal NSR-AI API method: " + methodName, e.getTargetException());
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access internal NSR-AI API method: " + methodName, e);
        }
    }

    // --- Chat API ---
    /**
     * Sends a message from a player to the AI. This operation is asynchronous.
     * @param player The player sending the message.
     * @param message The AI message to send.
     * @return A CompletableFuture that completes when the message has been processed by the AI.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the chat service is not available.
     */
    public static CompletableFuture<Void> sendMessageToAI(Player player, AIMessage message) {
        return callInternalMethod("sendMessageToAI", new Class<?>[]{Player.class, AIMessage.class}, player, message);
    }

    /**
     * Gets an asynchronous AI response to a given message.
     * @param message The AI message to get a response for.
     * @return A CompletableFuture that will contain the AI's response.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the chat service is not available.
     */
    public static CompletableFuture<AIResponse> getAIResponse(AIMessage message) {
        return callInternalMethod("getAIResponse", new Class<?>[]{AIMessage.class}, message);
    }

    // --- Pets API ---
    /**
     * Retrieves a snapshot of pet data for a given owner UUID.
     * Returns an empty Optional if the pet service is not available or no data is found.
     * @param owner The UUID of the pet owner.
     * @return An Optional containing PetDataSnapshot if available, otherwise empty.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized.
     */
    public static Optional<PetDataSnapshot> getPetData(UUID owner) {
        try {
            return Optional.ofNullable(callInternalMethod("getPetData", new Class<?>[]{UUID.class}, owner));
        } catch (IllegalStateException e) {
            // If the internal method throws an IllegalStateException (e.g., service not available),
            // we return Optional.empty() as per requirement.
            return Optional.empty();
        }
    }

    /**
     * Registers a listener to receive pet-related events.
     * @param listener The PetListener instance to register.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the pet service is not available.
     */
    public static void registerPetListener(PetListener listener) {
        callInternalMethod("registerPetListener", new Class<?>[]{PetListener.class}, listener);
    }

    // --- NPC API ---
    /**
     * Registers a listener to receive NPC-related events.
     * @param listener The NPCListener instance to register.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the NPC service is not available.
     */
    public static void registerNPCListener(NPCListener listener) {
        callInternalMethod("registerNPCListener", new Class<?>[]{NPCListener.class}, listener);
    }

    /**
     * Updates the skin of a specified NPC.
     * @param npcName The name of the NPC to update.
     * @param texture The base64 encoded texture string.
     * @param signature The base64 encoded signature string for the texture.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the NPC service is not available.
     */
    public static void updateNPCSkin(String npcName, String texture, String signature) {
        callInternalMethod("updateNPCSkin", new Class<?>[]{String.class, String.class, String.class}, npcName, texture, signature);
    }

    // --- GUI API (Conditional) ---
    /**
     * Opens a custom GUI for a player.
     * @param player The player for whom to open the GUI.
     * @param guiBuilder The GUIBuilder instance defining the GUI layout and behavior.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the GUI system is not supported by this version.
     */
    public static void openCustomGUI(Player player, GUIBuilder guiBuilder) {
        // Internal API throws UnsupportedOperationException if GUI service is null.
        // We re-throw it as IllegalStateException as per public API requirement.
        try {
            callInternalMethod("openCustomGUI", new Class<?>[]{Player.class, GUIBuilder.class}, player, guiBuilder);
        } catch (UnsupportedOperationException e) {
            throw new IllegalStateException("GUI system not supported by this NSR-AI version.", e);
        }
    }

    /**
     * Registers a listener to receive GUI-related events.
     * @param listener The GUIListener instance to register.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the GUI system is not supported by this version.
     */
    public static void registerGUIListener(GUIListener listener) {
        try {
            callInternalMethod("registerGUIListener", new Class<?>[]{GUIListener.class}, listener);
        } catch (UnsupportedOperationException e) {
            throw new IllegalStateException("GUI system not supported by this NSR-AI version.", e);
        }
    }

    // --- Memory API ---
    /**
     * Retrieves a value from the shared memory.
     * @param key The key of the memory entry to retrieve.
     * @return An Optional containing the value if found, otherwise empty.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the memory service is not available.
     */
    public static Optional<String> getSharedMemory(String key) {
        // Internal API logs warning and returns Optional.empty() for now.
        return callInternalMethod("getSharedMemory", new Class<?>[]{String.class}, key);
    }

    /**
     * Updates a value in the shared memory.
     * @param key The key of the memory entry to update.
     * @param value The new value to set.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the memory service is not available.
     */
    public static void updateSharedMemory(String key, String value) {
        // Internal API logs warning for now.
        callInternalMethod("updateSharedMemory", new Class<?>[]{String.class, String.class}, key, value);
    }



    // --- Versioning API ---
    /**
     * Retrieves the version string of the NSR-AI core plugin.
     * @return The version string of the core plugin.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized.
     */
    public static String getVersion() {
        return callInternalMethod("getVersion", new Class<?>[]{});
    }

    /**
     * Retrieves the current API version of the NSR-AI Open-Source API.
     * @return The API version.
     */
    public static int getApiVersion() {
        return API_VERSION;
    }

    // --- Addon Management ---

    public static java.util.List<AIAddon> getRegisteredAddons() {
        return callInternalMethod("getRegisteredAddons", new Class<?>[]{});
    }

    public static java.util.List<com.nsr.ai.api.AddonInfo> getLoadedAddons() {
        return callInternalMethod("getLoadedAddonInfo", new Class<?>[]{});
    }

    public static java.util.List<com.nsr.ai.api.AddonInfo> getFailedAddons() {
        return callInternalMethod("getFailedAddonInfo", new Class<?>[]{});
    }

    /**
     * Gets the logger instance for the NSR-AI plugin.
     * @return The logger instance.
     */
    public static java.util.logging.Logger getLogger() {
        return callInternalMethod("getLogger", new Class<?>[]{});
    }

    /**
     * Gets the NSR-AI plugin instance.
     * @return The plugin instance.
     */
    public static org.bukkit.plugin.Plugin getPlugin() {
        return callInternalMethod("getPlugin", new Class<?>[]{});
    }

    /**
     * Gets the NSR-AI API instance.
     * @return The API instance.
     */
    public static NSRaiAPI getApi() {
        return new NSRaiAPI(); // This is a static utility class, so we can just return a new instance
    }

    // --- Admin Mode Management ---
    /**
     * Attempts to toggle admin mode for a player.
     * The core plugin handles the validation of the activation code.
     * @param player The player for whom to toggle admin mode.
     * @param activationCode The admin activation code.
     * @return true if admin mode was successfully toggled, false otherwise.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the admin mode service is not available.
     */
    public static boolean toggleAdminMode(Player player, String activationCode) {
        return callInternalMethod("toggleAdminMode", new Class<?>[]{Player.class, String.class}, player, activationCode);
    }

    /**
     * Checks if admin mode is currently enabled for a player.
     * @param player The player to check.
     * @return true if admin mode is enabled, false otherwise.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the admin mode service is not available.
     */
    public static boolean isAdminModeEnabled(Player player) {
        return callInternalMethod("isAdminModeEnabled", new Class<?>[]{Player.class});
    }

    // --- Advanced GUI Customization ---
    /**
     * Registers a custom GUI definition with the NSR-AI core plugin.
     * Addons can then request to open this GUI for players.
     * @param guiId A unique identifier for the GUI.
     * @param provider An implementation of CustomGUIProvider that defines the GUI's layout and behavior.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the GUI system is not available.
     */
    public static void registerCustomGUI(String guiId, CustomGUIProvider provider) {
        callInternalMethod("registerCustomGUI", new Class<?>[]{String.class, CustomGUIProvider.class}, guiId, provider);
    }

    /**
     * Opens a previously registered custom GUI for a player.
     * @param player The player for whom to open the GUI.
     * @param guiId The unique identifier of the GUI to open.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized, the GUI system is not available, or the guiId is not registered.
     */
    public static void openCustomGUI(Player player, String guiId) {
        callInternalMethod("openCustomGUI", new Class<?>[]{Player.class, String.class}, player, guiId);
    }

    // --- Internal Player States (Controlled Access) ---
    /**
     * Checks if a player is currently on AI chat cooldown.
     * @param player The player to check.
     * @return true if the player is on cooldown, false otherwise.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the chat service is not available.
     */
    public static boolean isPlayerOnCooldown(Player player) {
        return callInternalMethod("isPlayerOnCooldown", new Class<?>[]{Player.class}, player);
    }

    /**
     * Gets the remaining AI chat cooldown time for a player in milliseconds.
     * @param player The player to check.
     * @return The remaining cooldown time in milliseconds, or 0 if not on cooldown.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the chat service is not available.
     */
    public static long getPlayerCooldownRemaining(Player player) {
        return callInternalMethod("getPlayerCooldownRemaining", new Class<?>[]{Player.class}, player);
    }

    /**
     * Checks if AI interaction is enabled for a specific player.
     * @param player The player to check.
     * @return true if AI is enabled for the player, false otherwise.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized.
     */
    public static boolean isAiEnabled(Player player) {
        return callInternalMethod("isAiEnabled", new Class<?>[]{Player.class}, player);
    }

    /**
     * Sets whether AI interaction is enabled for a specific player.
     * @param player The player for whom to set the AI enablement status.
     * @param enabled true to enable AI, false to disable.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized.
     */
    public static void setAiEnabled(Player player, boolean enabled) {
        callInternalMethod("setAiEnabled", new Class<?>[]{Player.class, boolean.class}, player, enabled);
    }

    // --- Knowledge Base Direct Modification ---
    /**
     * Adds a new knowledge entry to the core plugin's knowledge base.
     * @param keyword The primary keyword for the entry.
     * @param heading A descriptive heading for the entry.
     * @param content The content of the knowledge entry.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the knowledge base service is not available.
     */
    public static void addKnowledgeEntry(String keyword, String heading, String content) {
        callInternalMethod("addKnowledgeEntry", new Class<?>[]{String.class, String.class, String.class}, keyword, heading, content);
    }

    /**
     * Removes a knowledge entry from the core plugin's knowledge base by its keyword.
     * @param keyword The keyword of the entry to remove.
     * @return The content of the removed entry, or null if not found.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the knowledge base service is not available.
     */
    public static String removeKnowledgeEntry(String keyword) {
        return callInternalMethod("removeKnowledgeEntry", new Class<?>[]{String.class}, keyword);
    }

    /**
     * Retrieves all knowledge entries from the core plugin's knowledge base.
     * @return A map where keys are composite keys (e.g., "keyword/heading") and values are the content.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the knowledge base service is not available.
     */
    public static java.util.Map<String, String> getAllKnowledge() {
        return callInternalMethod("getAllKnowledge", new Class<?>[]{});
    }

    // --- Direct Conversation History Manipulation ---
    /**
     * Clears the conversation history for a specific player.
     * @param player The player whose history to clear.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the conversation service is not available.
     */
    public static void clearConversationHistory(Player player) {
        callInternalMethod("clearConversationHistory", new Class<?>[]{Player.class}, player);
    }

    /**
     * Triggers the core plugin to summarize a player's conversation history.
     * The result of the summary will be handled internally by the core plugin (e.g., saved to file).
     * @param player The player whose conversation to summarize.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the conversation service is not available.
     */
    public static void summarizeConversation(Player player) {
        callInternalMethod("summarizeConversation", new Class<?>[]{Player.class}, player);
    }

    /**
     * Triggers the core plugin to refresh a player's conversation history, typically by loading a summary.
     * @param player The player whose conversation to refresh.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the conversation service is not available.
     */
    public static void refreshConversation(Player player) {
        callInternalMethod("refreshConversation", new Class<?>[]{Player.class}, player);
    }

    /**
     * Retrieves a player's conversation history.
     * Note: The returned list contains simplified AIMessage objects to prevent exposing internal details.
     * @param player The player whose conversation history to retrieve.
     * @return A list of AIMessage objects representing the conversation history.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the conversation service is not available.
     */
    public static java.util.List<AIMessage> getConversationHistory(Player player) {
        return callInternalMethod("getConversationHistory", new Class<?>[]{Player.class}, player);
    }

    // --- Configuration Reloading ---
    /**
     * Triggers a reload of the main configuration (config.yml) of the NSR-AI core plugin.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized.
     */
    public static void reloadMainConfig() {
        callInternalMethod("reloadMainConfig", new Class<?>[]{});
    }

    /**
     * Triggers a reload of the features configuration (features.yml) of the NSR-AI core plugin.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized.
     */
    public static void reloadFeaturesConfig() {
        callInternalMethod("reloadFeaturesConfig", new Class<?>[]{});
    }

    /**
     * Triggers a reload of the knowledge base of the NSR-AI core plugin.
     * @throws IllegalStateException if the NSR-AI core plugin is not initialized or the knowledge base service is not available.
     */
    public static void reloadKnowledgeBase() {
        callInternalMethod("reloadKnowledgeBase", new Class<?>[]{});
    }
}

