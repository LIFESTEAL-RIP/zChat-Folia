package dev.mrzcookie.zchat.commands;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZChatCommand implements CommandExecutor, TabCompleter {
    private final ZChatPlugin plugin;

    public ZChatCommand(ZChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = this.plugin.getConfig();

        if (args.length == 0) {
            this.plugin.getMessageManager().send(sender, config.getString("messages.error.usage").replace("{usage}", "/" + label + " <version/reload>"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "version":
                this.plugin.getMessageManager().send(sender, "zChat <gray>v" + this.plugin.getDescription().getVersion() + "</gray><newline>Created by <color:#3399ff>MrZCookie</color>");
                break;
            case "reload":
                plugin.reloadConfig();
                plugin.saveDefaultConfig();
                plugin.saveConfig();

                this.plugin.getMessageManager().send(sender, config.getString("commands.zchat.messages.plugin-reloaded"));
                break;
            default:
                this.plugin.getMessageManager().send(sender, config.getString("messages.error.usage").replace("{usage}", "/" + label + " <version/reload>"));
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("version", "reload");
        }

        return new ArrayList<>();
    }
}
