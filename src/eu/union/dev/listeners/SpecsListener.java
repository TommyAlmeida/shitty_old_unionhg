package eu.union.dev.listeners;

import eu.union.dev.HGManager;
import eu.union.dev.events.HGTimerSecondsEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.util.Vector;

/**
 * Created by Fentis on 21/06/2016.
 */
public class SpecsListener implements Listener{

    @EventHandler
    public void onTarget(EntityTargetEvent e){
        if (e.getTarget() instanceof Player){
            Player p = (Player)e.getTarget();
            if (HGManager.getInstance().isSpec(p)){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e){
        if (HGManager.getInstance().isSpec(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onFood(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player)e.getEntity();
            if (HGManager.getInstance().isSpec(p)){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player p = (Player)e.getDamager();
            if (HGManager.getInstance().isSpec(p)){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (HGManager.getInstance().isSpec(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSeconds(HGTimerSecondsEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            for (Player ps : Bukkit.getOnlinePlayers()){
                if (HGManager.getInstance().isSpec(ps)){
                    if (!HGManager.getInstance().isSpec(p)){
                        p.hidePlayer(ps);
                    }
                }
            }
            for (Entity en : p.getNearbyEntities(5,5,5)){
                if (en instanceof Player){
                    Player p2 = (Player)en;
                    if (HGManager.getInstance().isSpec(p2)){
                        Vector v = p2.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(3);
                        p2.setVelocity(v);
                    }
                }
            }
            for (Player p2 : Bukkit.getOnlinePlayers()){
                if (HGManager.getInstance().isSpec(p2) &&
                        p2.getLocation().distance(p.getLocation()) <=1.5){
                    p2.teleport(p2.getLocation().add(0,5,0));
                }
            }
        }
    }
}
