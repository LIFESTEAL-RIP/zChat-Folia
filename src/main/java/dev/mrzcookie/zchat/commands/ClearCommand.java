package dev.mrzcookie.zchat.commands;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ClearCommand implements CommandExecutor {
    private final ZChatPlugin plugin;

    public ClearCommand(ZChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = this.plugin.getConfig();

        String executer = sender instanceof Player ? sender.getName() : "Console";

        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (!player.hasPermission(config.getString("commands.clearchat.permissions.bypass"))) {
                for (int i = 0; i <= 100; i++) {
                    this.plugin.getMessageManager().send(player, " ");
                }
            }

            this.plugin.getMessageManager().send(player, config.getString("commands.clearchat.messages.broadcast").replace("{executer}", executer));
        }

        this.plugin.getLogger().log(Level.INFO, "Chat has been cleared by " + executer + ".");
        return true;
    }
}
