package eu.union.dev.listeners;

import eu.union.dev.HGManager;
import eu.union.dev.Timer;
import eu.union.dev.events.HGStartEvent;
import eu.union.dev.events.HGTimerSecondsEvent;
import eu.union.dev.invs.TrollMenu;
import eu.union.dev.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

/**
 * Created by Fentis on 22/06/2016.
 */
public class AdminListener implements Listener{

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (HGManager.getInstance().inAdminMode(p)){
            if (p.getItemInHand().getType() == Material.CHEST ||
                    p.getItemInHand().getType() == Material.ENDER_CHEST){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onClickEnt(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        if (HGManager.getInstance().inAdminMode(p)){
            if (e.getRightClicked() instanceof Player){
                Player p2 = (Player)e.getRightClicked();
                if (p.getItemInHand().getType() == Material.CHEST){
                    p.openInventory(p2.getInventory());
                }
                if (p.getItemInHand().getType() == Material.ENDER_CHEST){
                    Inventory inv = Bukkit.createInventory(null,3*9,"Troll");
                    new TrollMenu().setItems(p,p2,inv);
                    p.openInventory(inv);
                }
            }
        }
    }
    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e){
        Player p = e.getPlayer();
        if (HGManager.getInstance().inAdminMode(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player)e.getEntity();
            if (HGManager.getInstance().inAdminMode(p)){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onSeconds(HGTimerSecondsEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (HGManager.getInstance().inAdminMode(p)){
                for (Player pl : Bukkit.getOnlinePlayers()){
                    if (!Perms.isStaff(pl)){
                        pl.hidePlayer(p);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onStart(HGStartEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (HGManager.getInstance().inAdminMode(p)){
                HGManager.getInstance().removePlayersVivos(p);
                Timer.getInstace().detectWin();
            }
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if (HGManager.getInstance().inAdminMode(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onTarget(EntityTargetEvent e){
        if (e.getTarget() instanceof Player){
            Player p = (Player)e.getTarget();
            if (HGManager.getInstance().inAdminMode(p)){
                e.setCancelled(true);
            }
        }
    }
}
