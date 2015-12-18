package me.robotoraccoon.warden.books;

import me.robotoraccoon.warden.Main;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class BookEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBookEdit(PlayerEditBookEvent event) {
        if (!Main.getPlugin().getConfig().getBoolean("books.enabled")) return;

        // Don't log if the player has bypass on.
        Player player = event.getPlayer();
        if (player.hasPermission("warden.bypass.books")) return;

        BookMeta meta = event.getNewBookMeta();
        ConfigurationSection options = Main.getPlugin().getConfig().getConfigurationSection("books.options");

        String alertLog = player.getName() + (event.isSigning() ? " signed \"" + meta.getTitle() + "\"" : " edited a book");

        if (options.getBoolean("log-location")) {
            Location loc = player.getLocation();
            // Format location string
            String logLocation = String.format("%s/%d,%d,%d",
                    loc.getWorld().getName(),
                    loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()
            );
            alertLog += " @" + logLocation;
        }

        alertLog += ":";

        List<String> pages = meta.getPages();
        for (Integer i = 0; i < pages.size(); i++) {
            alertLog += String.format(" [%d]\"%s\"", i + 1, pages.get(i));
        }

        // Log to file and console, depending on config and permissions.
        if (options.getBoolean("write-console"))
            Main.getPlugin().getLogger().info(alertLog);
        if (options.getBoolean("write-file"))
            Main.wardenLog.info("[Book] " + alertLog);
    }
}
