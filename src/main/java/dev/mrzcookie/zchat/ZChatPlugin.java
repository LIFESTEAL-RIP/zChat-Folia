package dev.mrzcookie.zchat;

import dev.mrzcookie.zchat.commands.CommandManager;
import dev.mrzcookie.zchat.listeners.EventManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ZChatPlugin extends JavaPlugin {
    private MessageManager messageManager;
    private CommandManager commandManager;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.messageManager = new MessageManager(this);
        this.commandManager = new CommandManager(this);
        this.eventManager = new EventManager(this);

        new Metrics(this, 21533);

        this.getLogger().log(Level.INFO, "Plugin enabled!");
    }

    @Override
    public void onDisable() {
        this.messageManager.getAdventure().close();

        this.getLogger().log(Level.INFO, "Plugin disabled!");
    }

    public MessageManager getMessageManager() {
        return this.messageManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }
}
