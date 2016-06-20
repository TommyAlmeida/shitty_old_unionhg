package eu.union.dev.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Fentis on 20/06/2016.
 */
public class HGDeathMatchEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    private Location loc;
    public HGDeathMatchEvent(Location loc) {
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
