package dev.mrzcookie.zchat.listeners;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Level;

public class EventManager {
    public EventManager(ZChatPlugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        pluginManager.registerEvents(new ChatListener(plugin), plugin);

        plugin.getLogger().log(Level.INFO, "Events registered!");
    }
}
