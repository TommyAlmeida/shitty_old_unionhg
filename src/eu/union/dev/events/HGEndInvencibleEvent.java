package eu.union.dev.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Fentis on 06/06/2016.
 */
public class HGEndInvencibleEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
