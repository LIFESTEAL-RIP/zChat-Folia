package dev.mrzcookie.zchat.listeners;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Level;

public class EventManager {
    public EventManager(ZChatPlugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        pluginManager.registerEvents(new ToggleChatListener(plugin), plugin);
        pluginManager.registerEvents(new ChatCooldownListener(plugin), plugin);
        pluginManager.registerEvents(new ChatFilterListener(plugin), plugin);
        pluginManager.registerEvents(new FormattedChatListener(plugin), plugin);

        plugin.getLogger().log(Level.INFO, "Events registered!");
    }
}
