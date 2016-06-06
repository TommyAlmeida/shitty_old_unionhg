package eu.union.dev.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Fentis on 06/06/2016.
 */
public class HGPlayerWinEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private Player winner;

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public HGPlayerWinEvent(Player winner)
    {
        this.winner = winner;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getWinner() {
        return this.winner;
    }
}