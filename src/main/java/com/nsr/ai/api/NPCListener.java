package com.nsr.ai.api;

import org.bukkit.entity.Player;

/**
 * Listener interface for NPC-related events.
 */
public interface NPCListener {
    /**
     * Called when a player interacts with an NPC.
     * @param player The player who interacted with the NPC.
     * @param npcName The name of the NPC.
     */
    void onNPCInteract(Player player, String npcName);
}
