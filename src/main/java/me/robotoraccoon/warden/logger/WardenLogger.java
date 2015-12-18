package me.robotoraccoon.warden.logger;

import me.robotoraccoon.warden.Main;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WardenLogger {

    public static Logger setupLog(Plugin plugin, String filename) {
        filename = plugin.getDataFolder() + File.separator + filename;
        Logger log = Logger.getAnonymousLogger();
        log.setUseParentHandlers(false);

        try {
            FileHandler fh = new FileHandler(filename, true);
            log.addHandler(fh);
            fh.setFormatter(new WardenLogFormatter());
        } catch (SecurityException e) {
            Main.getPlugin().getLogger().log(Level.SEVERE, String.format("SecurityException while opening %s!", filename), e);
        } catch (IOException e) {
            Main.getPlugin().getLogger().log(Level.WARNING, String.format("Could not open %s! Continuing anyway...", filename), e);
        }

        return log;
    }

    public static void destroyLog(Logger log) {
        // Retrieve and close each handler on the log (there should only be one)
        Handler[] handlers = log.getHandlers();
        for(Handler h : handlers) {
            Main.handlers.remove(h);
            log.removeHandler(h);
            h.close();
        }
    }

}
