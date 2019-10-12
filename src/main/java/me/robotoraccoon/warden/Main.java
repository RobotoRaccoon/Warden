package me.robotoraccoon.warden;

import me.robotoraccoon.warden.books.BookEvents;
import me.robotoraccoon.warden.items.ItemEvents;
import me.robotoraccoon.warden.ores.OreEvents;
import me.robotoraccoon.warden.signs.SignEvents;
import me.robotoraccoon.warden.logger.WardenLogger;
import me.robotoraccoon.warden.ores.OreManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Plugin plugin;

    // Loggers and log handlers
    public static List<Handler> handlers;
    public static Logger wardenLog;
    private int schedulerId;

    public void onEnable() {
        plugin = this;

        PluginManager pluginManager = getServer().getPluginManager();

        // Always save sample. Only save config if it's removed.
        getPlugin().saveResource("sampleConfig.yml", true);
        if (!new File(getDataFolder() + File.separator + "config.yml").exists())
            getPlugin().saveResource("config.yml", false);

        getCommand("warden").setExecutor(new Commands());

        pluginManager.registerEvents(new BookEvents(), this);
        pluginManager.registerEvents(new ItemEvents(), this);
        pluginManager.registerEvents(new OreEvents(), this);
        pluginManager.registerEvents(new SignEvents(), this);

        // Load ores
        OreManager.loadOres();

        // Logger
        if (handlers == null) handlers = new ArrayList<>();
        else handlers.clear();

        wardenLog = WardenLogger.setupLog(getPlugin(), "warden.log");

        // Flush logs to disk every 5 seconds
        schedulerId = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Iterator<Handler> i = handlers.iterator();
            while (i.hasNext()) {
                i.next().flush();
            }
        }, 60L, 30L);

    }

    public void onDisable() {
        // Kill the logger.
        getPlugin().getServer().getScheduler().cancelTask(schedulerId);
        WardenLogger.destroyLog(wardenLog);
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
