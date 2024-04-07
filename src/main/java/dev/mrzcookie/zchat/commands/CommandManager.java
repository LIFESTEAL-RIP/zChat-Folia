package dev.mrzcookie.zchat.commands;

import dev.mrzcookie.zchat.ZChatPlugin;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

public class CommandManager {
    private final ZChatPlugin plugin;

    private final HashSet<Command> pluginCommands;

    public CommandManager(ZChatPlugin plugin) {
        this.plugin = plugin;

        this.pluginCommands = new HashSet<>();

        FileConfiguration config = this.plugin.getConfig();

        if (config.getBoolean("commands.zchat.enabled", false)) this.register("zchat", "Manage your zChat plugin.", config.getStringList("commands.zchat.aliases"), "/zchat <version/reload>", config.getString("commands.zchat.permission"), new ZChatCommand(this.plugin), new ZChatCommand(this.plugin));
        if (config.getBoolean("commands.chatcooldown.enabled", false)) this.register("chatcooldown", "Set or toggle the cooldown on chat.", config.getStringList("commands.chatcooldown.aliases"), "/chatcooldown <set/toggle> [<seconds>]", config.getString("commands.chatcooldown.permission"), new CooldownCommand(plugin), new CooldownCommand(this.plugin));
        if (config.getBoolean("commands.chatfilter.enabled", false)) this.register("chatfilter", "Manage the chat's filter.", config.getStringList("commands.chatfilter.aliases"), "/chatfilter <set/toggle> [<seconds>]", config.getString("commands.chatfilter.permission"), new FilterCommand(this.plugin), new FilterCommand(this.plugin));
        if (config.getBoolean("commands.togglechat.enabled", false)) this.register("togglechat", "Toggle off and on the chat.", config.getStringList("commands.togglechat.aliases"), "/togglechat", config.getString("commands.togglechat.permission"), new ToggleCommand(this.plugin), null);
        if (config.getBoolean("commands.clearchat.enabled", false)) this.register("clearchat", "Clears the chat.", config.getStringList("commands.clearchat.aliases"), "/clearchat", config.getString("commands.clearchat.permission"), new ClearCommand(this.plugin), null);

        this.plugin.getLogger().log(Level.INFO, "Commands registered!");
    }

    public HashSet<Command> getPluginCommands() {
        return this.pluginCommands;
    }

    public void register(String commandName, String description, List<String> aliases, String usage, String permission, CommandExecutor executor, TabCompleter tabCompleter) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            Field commandMapField = this.plugin.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(this.plugin.getServer());

            PluginCommand command = constructor.newInstance(commandName, this.plugin);
            commandMap.register(this.plugin.getName(), command);
            this.plugin.getCommand(commandName).setDescription(description);
            this.plugin.getCommand(commandName).setUsage(usage);
            this.plugin.getCommand(commandName).setPermission(permission);
            this.plugin.getCommand(commandName).setExecutor(executor);
            this.plugin.getCommand(commandName).setTabCompleter(tabCompleter);
            this.pluginCommands.add(this.plugin.getCommand(commandName));

            for (String aliasName : aliases) {
                PluginCommand alias = constructor.newInstance(aliasName, this.plugin);
                commandMap.register(this.plugin.getName(), alias);
                this.plugin.getCommand(aliasName).setDescription(description);
                this.plugin.getCommand(aliasName).setUsage(usage);
                this.plugin.getCommand(aliasName).setPermission(permission);
                this.plugin.getCommand(aliasName).setExecutor(executor);
                this.plugin.getCommand(aliasName).setTabCompleter(tabCompleter);
                this.pluginCommands.add(this.plugin.getCommand(commandName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
