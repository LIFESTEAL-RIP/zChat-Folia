package dev.mrzcookie.zchat.listeners;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ToggleChatListener implements Listener {
    private final ZChatPlugin plugin;

    public ToggleChatListener(ZChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        FileConfiguration config = this.plugin.getConfig();
        Player player = event.getPlayer();

        if (!config.getBoolean("toggle-chat.chat-enabled", true) && !player.hasPermission(config.getString("toggle-chat.permissions.bypass"))) {
            event.setCancelled(true);
            this.plugin.getMessageManager().send(player, config.getString("toggle-chat.messages.error.chat-disabled"));
        }
    }
}
