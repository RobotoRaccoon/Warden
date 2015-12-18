package me.robotoraccoon.warden.ores;

import me.robotoraccoon.warden.Main;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class LoggedOre {

    // Mandatory
    private String name;
    private Integer maxHeight;
    private Integer minHeight;
    private Integer radius;
    // Can use the defaults from config;
    private Boolean logLocation;
    private Boolean logLightLevel;
    private List<String> logWorlds;

    public LoggedOre(String name, ConfigurationSection config) {
        this.name = name;
        maxHeight = config.getInt("max-height");
        minHeight = config.getInt("min-height");
        radius = config.getInt("radius");

        ConfigurationSection defaults = Main.getPlugin().getConfig().getConfigurationSection("ores.defaults");

        logLocation = config.contains("location") ? config.getBoolean("location") : defaults.getBoolean("location");
        logLightLevel = config.contains("light-level") ? config.getBoolean("light-level") : defaults.getBoolean("light-level");
        logWorlds = config.contains("worlds") ? config.getStringList("worlds") : defaults.getStringList("worlds");
    }

    // Getters
    public String getName() {
        return name;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public Integer getRadius() {
        return radius;
    }

    public Boolean logLocation() {
        return logLocation;
    }

    public Boolean logLightLevel() {
        return logLightLevel;
    }

    public List<String> getWorlds() {
        return logWorlds;
    }
}
