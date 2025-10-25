package com.nsr.ai.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * Interface for addons to provide custom GUI definitions to the NSR-AI core plugin.
 * Addons implementing this interface can define their own inventory layouts and handle click events.
 */
public interface CustomGUIProvider {

    /**
     * Creates and returns the Inventory object for the custom GUI.
     * This method defines the layout and initial contents of the GUI.
     * @param player The player for whom the GUI is being created.
     * @return The Inventory object representing the custom GUI.
     */
    Inventory createInventory(Player player);

    /**
     * Handles an inventory click event within the custom GUI.
     * Addons should implement their logic for responding to player clicks here.
     * @param event The InventoryClickEvent to handle.
     */
    void handleClick(InventoryClickEvent event);

    /**
     * Returns the title of the custom GUI.
     * @return The title of the GUI.
     */
    String getTitle();

    /**
     * Returns the size of the custom GUI (number of slots).
     * Must be a multiple of 9, and between 9 and 54 (inclusive).
     * @return The size of the GUI.
     */
    int getSize();
}
