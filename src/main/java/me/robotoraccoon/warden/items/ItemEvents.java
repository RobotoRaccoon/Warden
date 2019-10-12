package me.robotoraccoon.warden.items;

import me.robotoraccoon.warden.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class ItemEvents implements Listener {

    private static final String FORMAT = "%s named %d %s [%s]";

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onItemName(InventoryClickEvent event) {
        if (!Main.getPlugin().getConfig().getBoolean("items.enabled")) return;

        // Gotta be an Anvil Inventory
        Inventory inv = event.getInventory();
        if (!(inv instanceof AnvilInventory)) return;

        // Don't log if the player has bypass on.
        Player player = (Player) event.getWhoClicked();
        if (player.hasPermission("warden.bypass.items")) return;

        InventoryView view = event.getView();
        int slot = event.getRawSlot();

        // Check we're in the result slot of the Anvil area
        if (slot == 2 && slot == view.convertSlot(slot)) {
            ItemStack item = event.getCurrentItem();
            if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

            String name = item.getItemMeta().getDisplayName();
            String alert = String.format(FORMAT, player.getName(), item.getAmount(), item.getType(), name);

            // Log to file and console, depending on config and permissions.
            ConfigurationSection options = Main.getPlugin().getConfig().getConfigurationSection("items.options");
            if (options.getBoolean("write-console"))
                Main.getPlugin().getLogger().info(alert);
            if (options.getBoolean("write-file"))
                Main.wardenLog.info("[Item] " + alert);
        }
    }
}
