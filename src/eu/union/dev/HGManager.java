package eu.union.dev;

import eu.union.dev.events.*;
import eu.union.dev.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Fentis on 06/06/2016.
 */
public class HGManager implements Listener{

    private static HGManager instance = new HGManager();
    public static HGManager getInstance() {
        return instance;
    }
    private Status status;
    public enum Status {
        LOBBY("Lobby"),
        INVENCIBILITY("Invencibility"),
        POSINVINCIBILITY("PosInvencibility"),
        FEAST("Feast"),
        ENDGAME("EndGame");

        String d;

        Status(String d) {
            this.d = d;
        }

        public String value() {
            return this.d;
        }
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        //Give de items
    }
    @EventHandler
    public void onStart(HGStartEvent e){
        //Give kit e remove items
        Bukkit.broadcastMessage(Messages.PREFIX+" §bThe game started! And may the odds be ever in your favor!");
    }
    @EventHandler
    public void onEndInv(HGEndInvencibleEvent e){
        //Depois da invencibilidade
        Bukkit.broadcastMessage(Messages.PREFIX+" §cYou are not more invincible!!");
    }
    @EventHandler
    public void onFeastSpawn(HGFeastSpawnEvent e){
        //Spawn feast
        Bukkit.broadcastMessage(Messages.PREFIX+" §4The Feast appeared! Run or you'll lose it!");
    }
    @EventHandler
    public void onPlayerWin(HGPlayerWinEvent e){
        //Spawn estrutura do fim
        Player p = e.getWinner();
        Bukkit.broadcastMessage(Messages.PREFIX+" §aThe "+p.getDisplayName()+" won the game! Good game!");
    }
    @EventHandler
    public void onEnd(HGEndEvent e){
        //Da stop no server
        Bukkit.broadcastMessage(Messages.PREFIX+" §cNo winners! Opening next match!");
    }
}