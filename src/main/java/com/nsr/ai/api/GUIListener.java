package com.nsr.ai.api;

import org.bukkit.entity.Player;

/**
 * Listener interface for GUI-related events.
 */
public interface GUIListener {
    /**
     * Called when a GUI-related event occurs.
     * @param player The player involved in the GUI event.
     * @param eventType A string indicating the type of GUI event (e.g., "click", "close").
     */
    void onGUIEvent(Player player, String eventType);
}
