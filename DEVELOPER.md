# NSR-AI Developer Guide

Welcome, addon developers! This guide provides essential information for creating powerful and integrated addons for the **NSR-AI Minecraft plugin**, now featuring an **expanded Open-Source Addon API**. This API serves as a robust and secure layer, enabling your addons to interact deeply with NSR-AI's core functionalities, offering unprecedented possibilities for customization and extension.

## 1. API Versioning and Compatibility

The NSR-AI API uses a strict versioning system to ensure compatibility between the core plugin and your addons.

*   **`NSRaiAPI.API_VERSION`**: This `public static final int` constant in the `NSRaiAPI` class indicates the current major API version. Addons should check this value to ensure compatibility.

### Checking API Compatibility

It is crucial to check the API version at your addon's `onEnable()` stage.

```java
import com.nsr.ai.api.NSRaiAPI;
import org.bukkit.plugin.Plugin; // Use generic Plugin for compatibility
import org.bukkit.plugin.java.JavaPlugin;

public class MyAddon extends JavaPlugin {

    // Define the API version your addon is built against.
    // This should match the API_VERSION constant in the NSR-AI API you are using.
    private static final int REQUIRED_API_VERSION = 2; 

    @Override
    public void onEnable() {
        // Always check the API version to ensure your addon is compatible with the running NSR-AI core.
        if (NSRaiAPI.getApiVersion() < REQUIRED_API_VERSION) {
            getLogger().severe("NSR-AI API version is too old! " 
                + "Required: " + REQUIRED_API_VERSION 
                + ", Found: " + NSRaiAPI.getApiVersion());
            getServer().getPluginManager().disablePlugin(this); // Disable your addon if incompatible
            return;
        }
        getLogger().info("NSR-AI API version " + NSRaiAPI.getApiVersion() + " detected. Addon enabled.");
        // Proceed with your addon's specific initialization logic here.
    }
}
```

## 2. New Enhanced API Features

The expanded NSR-AI Addon API provides powerful new capabilities, allowing for deeper integration and more dynamic addon development:

*   **Admin Mode Management**: Programmatically toggle and monitor a player's administrative status within NSR-AI.
    *   `NSRaiAPI.toggleAdminMode(Player player, String activationCode)`
    *   `NSRaiAPI.isAdminModeEnabled(Player player)`

*   **Advanced GUI Customization**: Register and manage your own custom Graphical User Interfaces.
    *   `NSRaiAPI.registerCustomGUI(String guiId, CustomGUIProvider provider)` (requires implementing `com.nsr.ai.api.CustomGUIProvider`)
    *   `NSRaiAPI.openCustomGUI(Player player, String guiId)`

*   **Internal Player States**: Safely access and manage player-specific AI interaction parameters.
    *   `NSRaiAPI.isPlayerOnCooldown(Player player)`
    *   `NSRaiAPI.getPlayerCooldownRemaining(Player player)`
    *   `NSRaiAPI.isAiEnabled(Player player)`
    *   `NSRaiAPI.setAiEnabled(Player player, boolean enabled)`
    *   *Note*: The API does not expose raw API keys or allow direct modification of API key limits/providers.

*   **Knowledge Base Direct Manipulation**: Directly interact with NSR-AI's knowledge base.
    *   `NSRaiAPI.addKnowledgeEntry(String keyword, String heading, String content)`
    *   `NSRaiAPI.removeKnowledgeEntry(String keyword)`
    *   `NSRaiAPI.getAllKnowledge()`

*   **Conversation History Control**: Programmatically manage player conversation histories.
    *   `NSRaiAPI.clearConversationHistory(Player player)`
    *   `NSRaiAPI.summarizeConversation(Player player)`
    *   `NSRaiAPI.refreshConversation(Player player)`
    *   `NSRaiAPI.getConversationHistory(Player player)`

*   **Configuration Reloading**: Trigger reloads of core plugin configurations.
    *   `NSRaiAPI.reloadMainConfig()`
    *   `NSRaiAPI.reloadFeaturesConfig()`
    *   `NSRaiAPI.reloadKnowledgeBase()`

## 3. Safely Detecting Missing Features

The NSR-AI core plugin may not have all features (like GUI or specific services) enabled or implemented in every version. The public API is designed to gracefully handle these situations.

*   **Conditional Features:** Methods for features that might not be present (e.g., `openCustomGUI`, `addKnowledgeEntry` if the knowledge base is disabled) will throw an `IllegalStateException` if the underlying service is not available in the core plugin.

### Example: Handling Conditional Features

Always wrap calls to conditional features in `try-catch` blocks to prevent your addon from crashing.

```java
import com.nsr.ai.api.NSRaiAPI;
import com.nsr.ai.api.CustomGUIProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MyGUIAddon implements CustomGUIProvider {

    private final String GUI_ID = "my_custom_gui";

    public MyGUIAddon() {
        try {
            NSRaiAPI.registerCustomGUI(GUI_ID, this);
            NSRaiAPI.getLogger().info("Custom GUI registered successfully!");
        } catch (IllegalStateException e) {
            NSRaiAPI.getLogger().warning("Could not register custom GUI: " + e.getMessage());
        }
    }

    @Override
    public Inventory createInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, getSize(), getTitle());
        // Populate inventory with items
        inv.setItem(0, new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_SWORD));
        return inv;
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true); // Prevent item dragging
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == org.bukkit.Material.DIAMOND_SWORD) {
                player.sendMessage(ChatColor.GREEN + "You clicked the diamond sword!");
                player.closeInventory();
            }
        }
    }

    @Override
    public String getTitle() {
        return ChatColor.BLUE + "My Custom Addon GUI";
    }

    @Override
    public int getSize() {
        return 9; // Must be a multiple of 9
    }

    public void tryOpenGUI(Player player) {
        try {
            NSRaiAPI.openCustomGUI(player, GUI_ID);
            player.sendMessage(ChatColor.GREEN + "Opened custom GUI!");
        } catch (IllegalStateException e) {
            player.sendMessage(ChatColor.RED + "NSR-AI GUI System is not supported by this core version or GUI not registered.");
            NSRaiAPI.getLogger().warning("Could not open GUI: " + e.getMessage());
        }
    }
}
```

*   **Optional Returns:** Methods that return data (e.g., `getPetData`, `getSharedMemory`) will return `Optional.empty()` if the feature is not supported or no data is available. Always check if the `Optional` is present.

```java
import com.nsr.ai.api.NSRaiAPI;
import com.nsr.ai.api.PetDataSnapshot;
import org.bukkit.entity.Player;
import java.util.Optional;
import java.util.UUID;

public class MyPetHelper {

    public void displayPetData(Player player, UUID ownerId) {
        // Attempt to retrieve pet data.
        Optional<PetDataSnapshot> petData = NSRaiAPI.getPetData(ownerId);
        if (petData.isPresent()) {
            // If pet data is available, process it.
            player.sendMessage("Owner " + ownerId + " has pet data: " + petData.get().getData());
        } else {
            // Handle the case where no pet data is found or the pet system is not supported.
            player.sendMessage("No pet data found for owner " + ownerId + ", or pet system not supported.");
        }
    }
}
```

## 4. Asynchronous Operations

All AI-related operations (e.g., `sendMessageToAI`, `getAIResponse`) are asynchronous and return `CompletableFuture`. This prevents your addon from blocking the main server thread, ensuring a smooth player experience.

### Example: Handling AI Responses

```java
import com.nsr.ai.api.NSRaiAPI;
import com.nsr.ai.api.AIMessage;
import com.nsr.ai.api.AIResponse;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MyAIInteraction {

    public void askAI(Player player, String question) {
        AIMessage userMessage = new AIMessage(question, player.getUniqueId());
        // Call the AI asynchronously and handle the response when it's ready.
        NSRaiAPI.getAIResponse(userMessage)
                .thenAccept(aiResponse -> {
                    // This block executes on the main thread after the AI responds.
                    if (aiResponse.isSuccess()) {
                        player.sendMessage("AI says: " + aiResponse.getResponse());
                    } else {
                        player.sendMessage("AI failed to respond: " 
                        + aiResponse.getResponse());
                    }
                })
                .exceptionally(ex -> {
                    // Handle any exceptions that occurred during the asynchronous operation.
                    player.sendMessage("An error occurred while getting AI response: " 
                        + ex.getMessage());
                    ex.printStackTrace(); // Log the full stack trace for debugging
                    return null; // Return null to complete the CompletableFuture exceptionally
                });
    }
}
```

## 4. Asynchronous Operations

All AI-related operations (e.g., `sendMessageToAI`, `getAIResponse`) are asynchronous and return `CompletableFuture`. This prevents your addon from blocking the main server thread, ensuring a smooth player experience.

### Example: Handling AI Responses

```java
import com.nsr.ai.api.NSRaiAPI;
import com.nsr.ai.api.AIMessage;
import com.nsr.ai.api.AIResponse;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MyAIInteraction {

    public void askAI(Player player, String question) {
        AIMessage userMessage = new AIMessage(question, player.getUniqueId());
        // Call the AI asynchronously and handle the response when it's ready.
        NSRaiAPI.getAIResponse(userMessage)
                .thenAccept(aiResponse -> {
                    // This block executes on the main thread after the AI responds.
                    if (aiResponse.isSuccess()) {
                        player.sendMessage("AI says: " + aiResponse.getResponse());
                    } else {
                        player.sendMessage("AI failed to respond: " 
                        + aiResponse.getResponse());
                    }
                })
                .exceptionally(ex -> {
                    // Handle any exceptions that occurred during the asynchronous operation.
                    player.sendMessage("An error occurred while getting AI response: " 
                        + ex.getMessage());
                    ex.printStackTrace(); // Log the full stack trace for debugging
                    return null; // Return null to complete the CompletableFuture exceptionally
                });
    }
}
```

## 5. Complete Addon Registration Guide

To successfully integrate your addon with NSR-AI, you need to follow a three-step registration process: implement the `AIAddon` interface, configure your `addon.yml` manifest, and correctly place your compiled JAR file.

### Step 1: Implement the `AIAddon` Interface

Your addon's main class must implement the `com.nsr.ai.api.AIAddon` interface. This interface defines the contract for how your addon interacts with the NSR-AI core plugin, allowing it to manage your addon's lifecycle and retrieve essential information.

Here's a basic example of an `AIAddon` implementation:

```java
package com.example.myaddon; // Your addon's package

import com.nsr.ai.api.AIAddon;
import com.nsr.ai.api.NSRaiAPI; // Import the API for core functionalities
import org.bukkit.entity.Player;
import org.bukkit.ChatColor; // For sending colored messages
import org.bukkit.plugin.Plugin; // Use generic Plugin for compatibility

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class MySimpleAddon implements AIAddon {

    /**
     * Called when your addon is enabled by the NSR-AI core plugin.
     * Perform initialization logic here (e.g., registering event listeners).
     *
     * @param plugin The main instance of the Bukkit Plugin that loaded this addon.
     *               Use NSRaiAPI static methods to access core functionalities.
     */
    @Override
    public void onEnable(Plugin plugin) {
        NSRaiAPI.getLogger().info(getName() + " v" + getVersion() + " by " + getAuthor() + " enabled!");
        // Example: Register a Bukkit event listener
        // plugin.getServer().getPluginManager().registerEvents(new MyAddonListener(plugin), plugin);
    }

    /**
     * Called when your addon is disabled. Perform cleanup tasks here.
     */
    @Override
    public void onDisable() {
        NSRaiAPI.getLogger().info(getName() + " v" + getVersion() + " by " + getAuthor() + " disabled!");
    }

    /** Returns the official name of your addon. */
    @Override
    public String getName() {
        return "MySimpleAddon";
    }

    /** Returns the current version of your addon. */
    @Override
    public String getVersion() {
        return "1.0";
    }

    /** Returns the author(s) of your addon. */
    @Override
    public String getAuthor() {
        return "Gemini";
    }

    /** Returns a brief description of your addon. */
    @Override
    public String getDescription() {
        return "A simple addon demonstrating API usage.";
    }

    /**
     * Handles commands starting with `/ai` that are not handled by the core plugin.
     * Return a message to the player if handled, or `null` otherwise.
     */
    @Override
    public String onCommand(Player player, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("myaddon")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("hello")) {
                player.sendMessage(ChatColor.GREEN + "Hello from MySimpleAddon!");
                return "Handled by MySimpleAddon";
            }
        }
        return null;
    }

    /** Returns a map of commands provided by your addon. */
    @Override
    public Map<String, String> getCommands() {
        Map<String, String> commands = new HashMap<>();
        commands.put("myaddon hello", "Says hello from the addon.");
        return commands;
    }

    /** Returns a map of features provided by your addon. */
    @Override
    public Map<String, String> getFeatures() {
        Map<String, String> features = new HashMap<>();
        features.put("simple-greeting", "Provides a basic greeting command for demonstration.");
        return features;
    }
}
```

### Step 2: Create `addon.yml`

Every NSR-AI addon **must** include an `addon.yml` file in its `src/main/resources` directory. This file serves as the manifest for your addon, providing essential metadata that the NSR-AI core plugin uses to load, identify, and manage it.

Here's an example `addon.yml` and a detailed explanation of each field:

```yaml
# addon.yml
# This file must be located in your addon's src/main/resources directory.

# The official name of your addon. This should be unique and descriptive.
# It is displayed in the /ai addon list command.
name: MySimpleAddon

# The current version of your addon. Follow semantic versioning (e.g., 1.0.0, 1.2-BETA).
# It is displayed in the /ai addon list command.
version: 1.0

# The author(s) of the addon. This is displayed in the /ai addon list command.
author: Gemini

# A brief description of your addon's functionality.
# This is displayed in the /ai addon <name> command.
description: A simple addon demonstrating API usage.

# The fully qualified name of your addon's main class.
# This class must implement the com.nsr.ai.api.AIAddon interface.
# Example: com.yourcompany.youraddon.MainClass
main: com.example.myaddon.MySimpleAddon
```

**Explanation of `addon.yml` fields:**

*   **`name`**: (Required) A unique string identifying your addon. This name will be used in logs and in the `/ai addon list` command.
*   **`version`**: (Required) The current version of your addon. It's recommended to follow [Semantic Versioning](https://semver.org/).
*   **`author`**: (Required) The name(s) of the addon developer(s).
*   **`description`**: (Required) A brief, concise description of what your addon does. This is displayed when users request details about your addon.
*   **`main`**: (Required) The full path to your addon's main class, including its package. This class must implement the `com.nsr.ai.api.AIAddon` interface. The NSR-AI core plugin will instantiate this class when loading your addon.

### Step 3: Package and Install Your Addon

After implementing your `AIAddon` class and configuring `addon.yml`, you need to compile your addon into a JAR file and place it in the correct directory for NSR-AI to load it.

1.  **Build Your Addon**: Use your build tool (e.g., Maven, Gradle) to compile your project into a JAR file. Ensure that the `nsr-ai-api` dependency is set to `provided` in your build configuration, as the core plugin will provide it at runtime.
2.  **Install the JAR**: Your compiled addon JAR file **must** be placed in the following directory:

    ```
    /plugins/NSR-AI/addons/
    ```

    Addons placed in the main `/plugins/` directory or any other location will not be loaded by the addon manager. This ensures a clean separation between standard plugins and NSR-AI addons.

    Standard Bukkit/Spigot plugins that do not interact with the NSR-AI API can be placed in the main `/plugins/` folder as usual.

## 6. Addon Command Guidelines

## 7. Addon Command Guidelines

To prevent conflicts with the core plugin's commands and to ensure a consistent user experience, all addons must follow these command registration rules:

### Standard Addon Commands

All general addon commands must be prefixed with either `/aiaddon` or its shorter alias, `/aia`.

-   **Correct:** `/aiaddon myfeature`
-   **Correct:** `/aia stats`
-   **Incorrect:** `/myfeature`

### Advanced Commands (Conditional)

In specific cases, you may register a sub-command under the main `/ai` command (e.g., `/ai playerstats`). This is permitted **only if** your command logic meets the following criteria:

1.  **No Conflict:** It must not override or interfere with any existing or future core `/ai` sub-commands.
2.  **No Conversation Interference:** It must not disrupt a player's ongoing conversation with the AI. Your command must be distinct and not something a player would say in a normal chat. For example, an addon like `Advance-Player-Stats` could use `/ai stats` because it's a specific, non-conversational keyword.

Failure to follow these guidelines may result in your addon being blocked by the core plugin's security manager.

## 8. Further Assistance

For any further questions or issues, please refer to the main `README.md` or contact the NSR-AI development team (blackforge31@gmail.com).

## 9. Addon Submission and Review Process

To ensure the security, stability, and compliance of the NSR-AI ecosystem, especially for addons that interact with core functionalities, violets our rules or introduce
new commands, we have established a submission and review process. This process is designed to prevent your addon from being blocked or banned by the
core plugin's security manager. (Especially api,offline mode etc. Genral addons you can build without the permission)

### 9.1 Requesting Permission to Build

Before embarking on the development of a potentially high-risk or deeply integrated addon, we encourage developers to reach out to the NSR-AI
development team. This allows us to provide guidance, clarify API usage, and confirm the feasibility of your addon idea in advance.

   Contact:* Please email the NSR-AI development team at blackforge31@gmail.com with a brief description of your addon's intended functionality and how
it plans to interact with the NSR-AI API.

### 9.2 Post-Development Validation

Once your addon is developed, it must undergo a validation process to ensure it adheres to all guidelines, including the "Commons Clause" of the
license, and does not pose any security risks.

   Inform the Team:* After completing your addon, please inform the NSR-AI development team at blackforge31@gmail.com. We will then initiate the
validation process.
   Validation Outcome:* We will review your addon's functionality and inform you whether it is validated for use within the NSR-AI ecosystem.

### 9.3 Source Code Review for High-Risk Addons

For addons deemed "high-risk" (e.g., those interacting with security features, modifying core AI behavior, or handling sensitive player data), a source
 code review will be required as part of the validation process.

   Confidentiality:* We assure you that any source code provided for review will be stored privately and confidentially. It will not be shared with any
 third parties.
   Developer Rights:* Your intellectual property rights to your addon's source code remain entirely yours. The review is solely for security and
compliance verification.

Failure to comply with this submission and review process, especially for high-risk addons, may result in your addon being blocked by the core plugin's
 security manager.