package eu.union.dev.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Fentis on 06/06/2016.
 */
public class HGFeastSpawnEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    private Location loc;
    public HGFeastSpawnEvent(Location loc) {
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
