package dev.mrzcookie.zchat.commands;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class FilterCommand implements CommandExecutor, TabCompleter {
    private final ZChatPlugin plugin;

    public FilterCommand(ZChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        YamlConfiguration config = this.plugin.getConfigManager().getConfig("config");

        if (args.length > 0) {
            YamlConfiguration phrasesConfig = this.plugin.getConfigManager().getConfig("phrases");

            switch (args[0].toLowerCase()) {
                case "add":
                    if (args.length == 2) {
                        List<String> blockedPhrases = this.plugin.getConfigManager().getConfig("phrases").getStringList("blocked-phrases");

                        if (!blockedPhrases.contains(args[1])) {
                            blockedPhrases.add(args[1]);

                            phrasesConfig.set("blocked-phrases", blockedPhrases);
                            this.plugin.getConfigManager().saveConfig("phrases");

                            this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.added-phrase").replace("{phrase}", args[1]));
                        } else {
                            this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.error.already-added"));
                        }
                    } else {
                        this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.error.specify-phrase"));
                    }
                    break;
                case "remove":
                    if (args.length == 2) {
                        List<String> blockedPhrases = this.plugin.getConfigManager().getConfig("phrases").getStringList("blocked-phrases");

                        if (!blockedPhrases.contains(args[1])) {
                            blockedPhrases.remove(args[1]);

                            phrasesConfig.set("blocked-phrases", blockedPhrases);
                            this.plugin.getConfigManager().saveConfig("phrases");

                            this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.removed-phrase").replace("{phrase}", args[1]));
                        } else {
                            this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.error.already-added"));
                        }
                    } else {
                        this.plugin.getMessageManager().send(sender, config.getString("commands.chatfilter.messages.error.specify-phrase"));
                    }
                    break;
                case "toggle":
                    boolean isEnabled = config.getBoolean("chat-filter.enabled", false);

                    config.set("chat-filter.enabled", !isEnabled);
                    this.plugin.getConfigManager().saveConfig("config");

                    this.plugin.getMessageManager().send(sender, config.getString(isEnabled ? "commands.chatfilter.messages.disabled-filter" : "commands.chatfilter.messages.enabled-filter"));
                    break;
                default:
                    this.plugin.getMessageManager().send(sender, config.getString("messages.error.usage").replace("{usage}", "/" + label + " <add/remove/toggle> [<seconds>]"));
                    break;
            }
        } else {
            this.plugin.getMessageManager().send(sender, config.getString("messages.error.usage").replace("{usage}", "/" + label + " <add/remove/toggle> [<seconds>]"));
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
        }

        return arguments;
    }
}
