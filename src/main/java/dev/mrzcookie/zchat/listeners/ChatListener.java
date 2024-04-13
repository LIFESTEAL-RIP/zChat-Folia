package dev.mrzcookie.zchat.listeners;

import dev.mrzcookie.zchat.ZChatPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashSet;
import java.util.Optional;

public class ChatListener implements Listener {
    private final ZChatPlugin plugin;

    private final LuckPerms luckperms;

    private final HashSet<Player> onCooldown;

    public ChatListener(ZChatPlugin plugin) {
        this.plugin = plugin;

        this.luckperms = plugin.getServer().getServicesManager().load(LuckPerms.class);

        this.onCooldown = new HashSet<>();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = this.plugin.getConfig();

        if (!config.getBoolean("toggle-chat.chat-enabled", true) && !player.hasPermission(config.getString("toggle-chat.permissions.bypass"))) {
            if (!event.isCancelled()) {
                event.setCancelled(true);
                this.plugin.getMessageManager().send(player, config.getString("toggle-chat.messages.error.chat-disabled"));
            }
        }

        if (config.getBoolean("chat-cooldown.enabled", false) && !player.hasPermission(config.getString("chat-cooldown.permissions.bypass"))) {
            if (this.onCooldown.contains(player)) {
                if (!event.isCancelled()) {
                    event.setCancelled(true);
                    this.plugin.getMessageManager().send(player, config.getString("chat-cooldown.messages.error.on-cooldown"));
                }
            } else {
                this.onCooldown.add(player);

                BukkitScheduler scheduler = Bukkit.getScheduler();
                scheduler.runTaskLater(this.plugin, () -> {
                    this.onCooldown.remove(player);
                }, 20L * config.getInt("chat-cooldown.interval", 0));
            }
        }

        if (config.getBoolean("chat-filter.enabled", false) && !player.hasPermission(config.getString("chat-filter.permissions.bypass"))) {
            if (config.getStringList("chat-filter.blocked-phrases").contains(event.getMessage())) {
                if (!event.isCancelled()) {
                    event.setCancelled(true);
                    this.plugin.getMessageManager().send(player, config.getString("chat-filter.messages.error.blocked-phrase"));
                }
            }
        }

        if (config.getBoolean("formatted-chat.enabled")) {
            if (!event.isCancelled()) {
                Component formattedMessage = getFormattedMesssage(player, event.getMessage());
                if (formattedMessage != null) {
                    event.setCancelled(true);
                    plugin.getMessageManager().getAdventure().all().sendMessage(formattedMessage);
                }
            }
        }

        event.getFormat();
    }

    private Component getFormattedMesssage(Player player, String message) {
        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection formatsSection = config.getConfigurationSection("formatted-chat.formats");

        Component finalMessage = null;
        int highestPriority = -1;

        if (formatsSection != null) {
            for (String key : formatsSection.getKeys(false)) {
                ConfigurationSection formatSection = formatsSection.getConfigurationSection(key);

                if (formatSection != null) {
                    String permission = formatSection.getString("permission");
                    int priority = formatSection.getInt("priority", 0);

                    if (permission == null || player.hasPermission(permission) && priority > highestPriority) {
                        String format = formatSection.getString("format");

                        if (format != null) {
                            User luckpermsPlayer = this.luckperms.getUserManager().getUser(player.getUniqueId());

                            String prefix = Optional.ofNullable(luckpermsPlayer)
                                    .map(lpPlayer -> lpPlayer.getCachedData().getMetaData().getPrefix())
                                    .orElse("");

                            String suffix = Optional.ofNullable(luckpermsPlayer)
                                    .map(lpPlayer -> lpPlayer.getCachedData().getMetaData().getSuffix())
                                    .orElse("");

                            if (!player.hasPermission(config.getString("formatted-chat.permissions.colorized-chat"))) {
                                message = PlainTextComponentSerializer.plainText().serialize(Component.text(message));
                            }

                            format = format
                                    .replace("{prefix}", prefix)
                                    .replace("{username}", player.getName())
                                    .replace("{displayname}", player.getDisplayName())
                                    .replace("{suffix}", suffix)
                                    .replace("{message}", message);

                            LegacyComponentSerializer serializer = LegacyComponentSerializer.builder()
                                    .character('&')
                                    .hexColors()
                                    .useUnusualXRepeatedCharacterHexFormat()
                                    .build();

                            format = MiniMessage.miniMessage().serialize(serializer.deserialize(format));

                            finalMessage = MiniMessage.miniMessage().deserialize(format.replace("\\<", "<"));

                            highestPriority = priority;
                        }
                    }
                }
            }

            return finalMessage;
        }

        return null;
    }
}
