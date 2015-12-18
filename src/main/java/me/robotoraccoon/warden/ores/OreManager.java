package me.robotoraccoon.warden.ores;

import me.robotoraccoon.warden.Main;
import me.robotoraccoon.warden.signs.SignManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Set;

public class OreManager {

    private static final HashMap<String, LoggedOre> loggedOres = new HashMap<>();
    private static final HashMap<Player, LastOreFind> findHistory = new HashMap<>();

    public static void loadOres() {
        loggedOres.clear();
        ConfigurationSection ores = Main.getPlugin().getConfig().getConfigurationSection("ores.blocks");

        for (String name : ores.getKeys(false)) {
            LoggedOre ore = new LoggedOre(name, ores.getConfigurationSection(name));
            loggedOres.put(name, ore);
        }

        Main.getPlugin().getLogger().info("Registered ores: " + StringUtils.join(loggedOres.keySet(), ", "));
    }

    public static LoggedOre getOre(String name) {
        return loggedOres.get(name);
    }

    public static LastOreFind getLastOreFind(Player player) {
        if (!findHistory.containsKey(player))
            findHistory.put(player, new LastOreFind());

        return findHistory.get(player);
    }

    // Misc functions //

    //Returns the amount of blocks in a radius which are the same as the centre
    public static int blocksInArea(Block block, int rad) {
        int areaBlocks = 0;

        for (int x = -rad; x <= rad; x++) {
            for (int y = -rad; y <= rad; y++) {
                for (int z = -rad; z <= rad; z++) {
                    if (block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z).getType() == block.getType()) {
                        areaBlocks++;
                    }
                }
            }
        }

        return areaBlocks;
    }

    public static void logOreFind(Player player, Block block, LoggedOre ore) {
        Integer areaBlocks = blocksInArea(block, ore.getRadius());

        //Create the base message to be logged.
        String alert = String.format("&8%s found %s %s",
                player.getName(),
                areaBlocks,
                ore.getName()
        );

        if (ore.logLocation()) {
            // Format location string
            Location loc = block.getLocation();
            String logLocation = String.format("%s/%d,%d,%d",
                    loc.getWorld().getName(),
                    loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()
            );

            alert += " @" + logLocation;
        }

        if (ore.logLightLevel()) {
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                alert += " (Night Vision)";
            } else {

                //Get light level of block
                Material blockType = block.getType();
                block.setType(Material.AIR);
                Byte lightLevel = block.getLightLevel();
                block.setType(blockType);

                alert += String.format(" (Light: %s%s&8)", (lightLevel < 1 ? "&7" : "&8"), lightLevel);
            }
        }

        alert = ChatColor.translateAlternateColorCodes('&', alert);
        String alertLog = ChatColor.stripColor(alert);

        ConfigurationSection options = Main.getPlugin().getConfig().getConfigurationSection("ores.options");

        // Log to staff, file, and console, depending on config and permissions.
        for (Player user : Bukkit.getOnlinePlayers()) {
            if (user.hasPermission("warden.staff.ores"))
                user.sendMessage(alert);
        }
        if (options.getBoolean("write-console"))
            Main.getPlugin().getLogger().info(alertLog);
        if (options.getBoolean("write-file"))
            Main.wardenLog.info("[Ore] " + alertLog);
    }


}
