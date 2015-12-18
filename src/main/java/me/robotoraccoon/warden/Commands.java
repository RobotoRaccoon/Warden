package me.robotoraccoon.warden;

import me.robotoraccoon.warden.ores.OreManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission("warden.command.reload")) {
            sender.sendMessage(colorize("&4Error: &cYou do not have permission to use this command."));
            return true;
        }

        if (args.length < 1) {
            args[0] = "";
        }

        switch (args[0].toLowerCase()) {
            case "":
            case "help":
                sender.sendMessage(colorize(" &7===== &5Warden Options &7=====" +
                    "\n&5Reload &f> &dReloads the configuration file"));
                break;

            case "reload":
                Main.getPlugin().reloadConfig();
                OreManager.loadOres();
                sender.sendMessage(colorize("&aSuccessfully reloaded the config file"));
                break;

            default:
                sender.sendMessage(colorize("&4Error: &cInvalid command"));
                break;
        }

        return true;
    }

    private String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}