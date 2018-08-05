package me.robotoraccoon.warden.signs;

import me.robotoraccoon.warden.Main;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignManager {

    public static void alertSound(Player player) {
        Location loc = player.getEyeLocation();
        Sound soundID = Sound.BLOCK_NOTE_BLOCK_PLING;
        Double soundVolume = 1.0;
        Double soundPitch = 1.0;

        player.playSound(loc, soundID, soundVolume.floatValue(), soundPitch.floatValue());
    }

    public static Boolean isNaughty(String[] lines) {
        List<?> phrases = Main.getPlugin().getConfig().getList("signs.naughty-phrases");
        String patternString = "(" + StringUtils.join(phrases, "|") + ")";

        if (Main.getPlugin().getConfig().getBoolean("signs.options.ignore-case"))
            patternString = "(?i)" + patternString;

        Matcher matcher1 = Pattern.compile(patternString).matcher(StringUtils.join(lines, ""));
        Matcher matcher2 = Pattern.compile(patternString).matcher(StringUtils.join(lines, " "));

        return matcher1.find() || matcher2.find();
    }

    public static void logSign(Player player, String[] lines, Location loc) {
        ConfigurationSection options = Main.getPlugin().getConfig().getConfigurationSection("signs.options");

        Boolean naughty = isNaughty(lines);
        String alert = "&4" + player.getName() + " &cplaced a " + (naughty ? "naughty sign" : "sign");

        if (options.getBoolean("log-location")) {
            // Format location string
            String logLocation = String.format("%s/%d,%d,%d",
                    loc.getWorld().getName(),
                    loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()
            );
            alert += " &8@" + logLocation;
        }

        // Format sign lines
        String logLines = String.format("&c[%s&c] [%s&c] [%s&c] [%s&c]",
                lines[0], lines[1], lines[2], lines[3]
        );
        alert += ": &c" + logLines;

        // get in-game and log file lines.
        alert = ChatColor.translateAlternateColorCodes('&', alert);
        String alertLog = ChatColor.stripColor(alert);

        // Log to staff, file, and console, depending on config and permissions.
        if (naughty) {
            for (Player user : Bukkit.getOnlinePlayers()) {
                if (user.hasPermission("warden.staff.signs")) {
                    user.sendMessage(alert);
                    alertSound(user);
                }
            }

            if (options.getBoolean("write-console"))
                Main.getPlugin().getLogger().info(alertLog);
        }
        if (options.getBoolean("write-file"))
            Main.wardenLog.info("[Sign] " + alertLog);
    }

}
