package dev.mrzcookie.zchat.commands;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ToggleCommand implements CommandExecutor {
    private final ZChatPlugin plugin;

    public ToggleCommand(ZChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = this.plugin.getConfig();

        String executer = sender instanceof Player ? sender.getName() : "Console";

        boolean chatEnabled = !config.getBoolean("toggle-chat.chat-enabled", true);

        config.set("toggle-chat.chat-enabled", chatEnabled);
        this.plugin.saveConfig();

        if (chatEnabled) {
            this.plugin.getMessageManager().broadcast(config.getString("commands.togglechat.messages.chat-enabled-broadcast").replace("{executer}", executer));
        } else {
            this.plugin.getMessageManager().broadcast(config.getString("commands.togglechat.messages.chat-disabled-broadcast").replace("{executer}", executer));
        }

        return true;
    }
}
