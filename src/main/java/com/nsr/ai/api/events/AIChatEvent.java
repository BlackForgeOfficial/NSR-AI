package com.nsr.ai.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is fired when a player interacts with the AI.
 * It contains the player, the message they sent, and the AI's response.
 * Addons can listen to this event to modify the AI's response or perform custom actions.
 * <p>
 * To listen to this event, create a new class that implements the {@link org.bukkit.event.Listener} interface
 * and has a method annotated with {@link org.bukkit.event.EventHandler}.
 * <p>
 * Example:
 * <pre>{@code
 * public class MyListener implements Listener {
 * 
 *     @EventHandler
 *     public void onAIChat(AIChatEvent event) {
 *         Player player = event.getPlayer();
 *         String message = event.getMessage();
 *         String response = event.getResponse();
 * 
 *         // Modify the response
 *         event.setResponse("[Modified] " + response);
 * 
 *         // Or cancel the event to prevent the AI's response from being sent
 *         // event.setCancelled(true);
 *     }
 * }
 * }</pre>
 */
public class AIChatEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String message;
    private String response;
    private boolean cancelled;

    public AIChatEvent(Player player, String message, String response) {
        this.player = player;
        this.message = message;
        this.response = response;
        this.cancelled = false;
    }

    /**
     * Gets the player who interacted with the AI.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the message the player sent to the AI.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the AI's response to the player's message.
     *
     * @return The AI's response.
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the AI's response to the player's message.
     *
     * @param response The new response.
     */
    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}