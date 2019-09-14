package me.robotoraccoon.warden.ores;

import me.robotoraccoon.warden.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OreEvents implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!Main.getPlugin().getConfig().getBoolean("ores.enabled")) return;

        // Don't log if the player has bypass on.
        Player player = event.getPlayer();
        if (player.hasPermission("warden.bypass.ores")) return;

        Block block = event.getBlock();
        String blockName = block.getType().name();
        LoggedOre ore = OreManager.getOre(blockName);
        ConfigurationSection options = Main.getPlugin().getConfig().getConfigurationSection("ores.options");

        // Don't log if the block isn't in the loggedOres list.
        if (ore == null) return;

        // Don't log players in Creative mode, if enabled.
        if (player.getGameMode() == GameMode.CREATIVE && options.getBoolean("ignore-creative")) return;

        // Don't log if in the incorrect world.
        if (!ore.getWorlds().contains(block.getWorld().getName())) return;

        // Log if the block is outside the given radius for the last find.
        Location loc = block.getLocation();
        LastOreFind find = OreManager.getLastOreFind(player);
        if (find.isOutsideRadius(ore, loc)
                && loc.getBlockY() <= ore.getMaxHeight()
                && loc.getBlockY() >= ore.getMinHeight()) {

            find.setLastFind(ore, loc);
            OreManager.logOreFind(player, block, ore);
        }
    }
}