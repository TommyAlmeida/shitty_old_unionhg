package eu.union.dev.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Fentis on 09/06/2016.
 */
public class HGMiniFeastSpawnEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    private Location loc;
    private int n;
    public HGMiniFeastSpawnEvent(Location loc, int n) {
        this.loc  = loc;
        this.n = n;
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

    public int getMiniFeast() {
        return n;
    }
}
