package me.robotoraccoon.warden.signs;

import me.robotoraccoon.warden.Main;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignEvents implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent event) {
        if (!Main.getPlugin().getConfig().getBoolean("signs.enabled")) return;

        // Don't log if the player has bypass on.
        Player player = event.getPlayer();
        if (player.hasPermission("warden.bypass.signs")) return;

        SignManager.logSign(player, event.getLines(), event.getBlock().getLocation());
    }

}
