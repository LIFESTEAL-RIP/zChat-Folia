package dev.mrzcookie.zchat.listeners;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;

public class ChatCooldownListener implements Listener {
    private final ZChatPlugin plugin;
    private final HashSet<Player> onCooldown;

    public ChatCooldownListener(ZChatPlugin plugin) {
        this.plugin = plugin;
        this.onCooldown = new HashSet<>();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        FileConfiguration config = this.plugin.getConfig();
        Player player = event.getPlayer();

        if (config.getBoolean("chat-cooldown.enabled", false) && !player.hasPermission(config.getString("chat-cooldown.permissions.bypass"))) {
            if (this.onCooldown.contains(player)) {
                event.setCancelled(true);
                this.plugin.getMessageManager().send(player, config.getString("chat-cooldown.messages.error.on-cooldown"));
            } else {
                this.onCooldown.add(player);

                ZChatPlugin.getInstance().foliaLib.getImpl().runLaterAsync(() -> this.onCooldown.remove(player), 20L * config.getInt("chat-cooldown.interval", 0));
//                BukkitScheduler scheduler = Bukkit.getScheduler();
//                scheduler.runTaskLater(this.plugin, () -> {
//                    this.onCooldown.remove(player);
//                }, 20L * config.getInt("chat-cooldown.interval", 0));
            }
        }
    }
}
