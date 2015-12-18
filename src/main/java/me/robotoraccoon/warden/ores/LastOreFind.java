package me.robotoraccoon.warden.ores;

import org.bukkit.Location;

import java.util.HashMap;

public class LastOreFind {

    private HashMap<LoggedOre, Location> findHistory;

    public LastOreFind() {
        findHistory = new HashMap<>();
    }

    public void setLastFind(LoggedOre ore, Location loc) {
        findHistory.put(ore, loc);
    }

    public Location getLastFind(LoggedOre ore) {
        // Returns null if the key does not exist.
        return findHistory.get(ore);
    }

    public Boolean isOutsideRadius(LoggedOre ore, Location loc) {
        Location lastFind = getLastFind(ore);
        return (lastFind == null || lastFind.distance(loc) > ore.getRadius());
    }
}
