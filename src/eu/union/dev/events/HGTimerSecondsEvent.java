package eu.union.dev.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Fentis on 06/06/2016.
 */
public class HGTimerSecondsEvent extends Event{

    private static final HandlerList handlers = new HandlerList();
    private int time;
    private String timeformated;
    public HGTimerSecondsEvent(int time, String timeformated) {
        this.time = time;
        this.timeformated = timeformated;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public int getTime() {
        return time;
    }
    public String getTimeFormated() {
        return timeformated;
    }
}
