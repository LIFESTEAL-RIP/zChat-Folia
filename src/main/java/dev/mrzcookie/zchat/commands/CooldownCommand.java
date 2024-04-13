package dev.mrzcookie.zchat.commands;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CooldownCommand implements CommandExecutor, TabCompleter {
    private final ZChatPlugin plugin;

    public CooldownCommand(ZChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = this.plugin.getConfig();

        if (args.length == 0) {
            this.plugin.getMessageManager().send(sender, config.getString("messages.error.usage").replace("{usage}", "/" + label + " <set/toggle> [<seconds>]"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "set":
                if (args.length != 2) {
                    this.plugin.getMessageManager().send(sender, config.getString("commands.chatcooldown.messages.error.specify-cooldown"));
                    break;
                }

                config.set("commands.chatcooldown.interval", Integer.parseInt(args[1]));
                this.plugin.saveConfig();

                this.plugin.getMessageManager().send(sender, config.getString("commands.chatcooldown.messages.cooldown-set").replace("{cooldown}", args[1]));
                break;
            case "toggle":
                boolean isEnabled = config.getBoolean("chat-cooldown.enabled", false);

                config.set("chat-cooldown.enabled", !isEnabled);
                this.plugin.saveConfig();

                this.plugin.getMessageManager().send(sender, config.getString(isEnabled ? "commands.chatcooldown.messages.disabled-cooldown" : "commands.chatcooldown.messages.enabled-cooldown"));
                break;
            default:
                this.plugin.getMessageManager().send(sender, config.getString("messages.error.usage").replace("{usage}", "/" + label + " <set/toggle> [<seconds>]"));
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> arguments = new ArrayList<>();

        if (args.length == 1) {
            arguments.add("set");
            arguments.add("toggle");
        }

        return arguments;
    }
}
