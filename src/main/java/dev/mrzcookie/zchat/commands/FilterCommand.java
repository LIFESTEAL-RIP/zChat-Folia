package dev.mrzcookie.zchat.commands;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterCommand implements CommandExecutor, TabCompleter {
    private final ZChatPlugin plugin;

    public FilterCommand(ZChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = this.plugin.getConfig();

        if (args.length == 0) {
            this.plugin.getMessageManager().send(sender, config.getString("messages.error.usage").replace("{usage}", "/" + label + " <add/remove/toggle> [<seconds>]"));
            return true;
        }

        List<String> blockedPhrases = config.getStringList("chat-filter.blocked-phrases");

        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length != 2) {
                    this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.error.specify-phrase"));
                    break;
                }

                if (blockedPhrases.contains(args[1])) {
                    this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.error.already-added"));
                    break;
                }

                blockedPhrases.add(args[1]);

                config.set("chat-filter.blocked-phrases", blockedPhrases);
                this.plugin.saveConfig();

                this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.added-phrase").replace("{phrase}", args[1]));
                break;
            case "remove":
                if (args.length != 2) {
                    this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.error.specify-phrase"));
                    break;
                }

                if (!blockedPhrases.contains(args[1])) {
                    this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.error.already-added"));
                    break;
                }

                blockedPhrases.remove(args[1]);

                config.set("chat-filter.blocked-phrases", blockedPhrases);
                this.plugin.saveConfig();

                this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.removed-phrase").replace("{phrase}", args[1]));
                break;
            case "toggle":
                boolean isEnabled = config.getBoolean("chat-filter.enabled", false);

                config.set("chat-filter.enabled", !isEnabled);
                this.plugin.saveConfig();

                this.plugin.getMessageManager().send(sender, config.getString(isEnabled ? "commands.chatfilter.messages.disabled-filter" : "commands.chatfilter.messages.enabled-filter"));
                break;
            default:
                this.plugin.getMessageManager().send(sender, config.getString("messages.error.usage").replace("{usage}", "/" + label + " <add/remove/toggle> [<seconds>]"));
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> arguments = new ArrayList<>();

        if (args.length == 1) {
            arguments.add("add");
            arguments.add("remove");
            arguments.add("toggle");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")) {
                arguments.addAll(this.plugin.getConfig().getStringList("chat-filter.blocked-phrases"));
            }
        }

        return arguments;
    }
}
