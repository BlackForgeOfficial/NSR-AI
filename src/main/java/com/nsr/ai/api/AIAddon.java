package com.nsr.ai.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin; // Import generic Plugin
import java.util.Map;

/**
 * This interface must be implemented by all NSR-AI addons.
 */
public interface AIAddon {

    void onEnable(Plugin plugin); // Changed to generic Plugin

    void onDisable();

    String onCommand(Player player, String[] args);

    Map<String, String> getCommands();

    Map<String, String> getFeatures();

    String getName();

    String getVersion();

    String getAuthor();

    String getDescription();
}
