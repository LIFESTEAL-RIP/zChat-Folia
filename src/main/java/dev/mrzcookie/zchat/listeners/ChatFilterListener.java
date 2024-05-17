package dev.mrzcookie.zchat.listeners;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

public class ChatFilterListener implements Listener {
    private final ZChatPlugin plugin;

    public ChatFilterListener(ZChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        FileConfiguration config = this.plugin.getConfig();
        Player player = event.getPlayer();

        if (config.getBoolean("chat-filter.enabled", false) && !player.hasPermission(config.getString("chat-filter.permissions.bypass"))) {
            for (String phrase : config.getStringList("chat-filter.blocked-phrases")) {
                Pattern pattern = Pattern.compile(Pattern.quote(phrase), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                if (pattern.matcher(event.getMessage()).find()) {
                    if (!event.isCancelled()) {
                        event.setCancelled(true);
                        this.plugin.getMessageManager().send(player, config.getString("chat-filter.messages.error.blocked-phrase"));
                    }
                }
            }
        }
    }
}
