package eu.union.dev.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Fentis on 06/07/2016.
 */
public class HGDungeonSpawnEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    private Location loc;
    public HGDungeonSpawnEvent(Location loc) {
        this.loc  = loc;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public Location getLocation() {
        return loc;
    }
}
