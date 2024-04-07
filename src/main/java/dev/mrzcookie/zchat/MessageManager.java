package dev.mrzcookie.zchat;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MessageManager {
    private final BukkitAudiences adventure;

    public MessageManager(ZChatPlugin plugin) {
        this.adventure = BukkitAudiences.create(plugin);
    }

    public static Component format(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public void send(CommandSender sender, String message) {
        this.adventure.sender(sender).sendMessage(format(message));
    }

    public void broadcast(String message) {
        this.adventure.all().sendMessage(format(message));
    }

    public @NotNull BukkitAudiences getAdventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }

        return this.adventure;
    }
}
