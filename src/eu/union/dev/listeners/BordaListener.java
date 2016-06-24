package eu.union.dev.listeners;

import eu.union.dev.HGManager;
import eu.union.dev.events.HGTimerSecondsEvent;
import eu.union.dev.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Created by Fentis on 21/06/2016.
 */
public class BordaListener implements Listener{

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        Location loc = e.getEntity().getLocation();
        Location loc2 = new Location(e.getEntity().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= (HGManager.getInstance().getBordSize()-10)) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= (HGManager.getInstance().getBordSize()-10))))
        {
            e.setCancelled(true);
        }
        if (loc.getY() >= HGManager.getInstance().getCamadalimite()){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSeconds(HGTimerSecondsEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                if (p.getLocation().getY() <= HGManager.getInstance().getCamadalimite()){
                    p.teleport(HGManager.getInstance().getColiseuLoc().add(0,5,0));
                }
            }
            if (HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){
                if (p.getGameMode() == GameMode.SURVIVAL &&
                        p.getLocation().getY() >= HGManager.getInstance().getCamadalimite() &&
                        !HGManager.getInstance().isNoDamage(p)){
                    p.damage(4.0);
                }
            }
            Location loc = p.getLocation();
            Location loc2 = new Location(p.getWorld(), 0, 0, 0);
            if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= HGManager.getInstance().getBordSize()) ||
                    (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= HGManager.getInstance().getBordSize())))
            {
                if (HGManager.getInstance().isSpec(p)){
                    if (!Perms.isStaff(p)){
                        p.teleport(HGManager.getInstance().getColiseuLoc().add(0,5,0));
                    }
                }else{
                    double dmg = 2.5D;
                    if (p.getHealth() - dmg > 0.0D) {
                        p.damage(dmg);
                    } else {
                        p.setHealth(0.0D);
                        Bukkit.broadcastMessage("§c"+p.getDisplayName()+" killed by bord!");
                    }
                }
            }
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (e.getBlock().getY() >=HGManager.getInstance().getCamadalimite() && !HGManager.getInstance().inBuild(p)){
            e.setCancelled(true);
        }
        Location loc = e.getBlock().getLocation();
        Location loc2 = new Location(e.getBlock().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= (HGManager.getInstance().getBordSize()-10)) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= (HGManager.getInstance().getBordSize()-10)))
                && !HGManager.getInstance().inBuild(p))
        {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if (e.getBlock().getY() >=HGManager.getInstance().getCamadalimite() && !HGManager.getInstance().inBuild(p)){
            e.setCancelled(true);
        }
        Location loc = e.getBlock().getLocation();
        Location loc2 = new Location(e.getBlock().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= (HGManager.getInstance().getBordSize()-10)) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= (HGManager.getInstance().getBordSize()-10)))
                && !HGManager.getInstance().inBuild(p))
        {
            e.setCancelled(true);
        }
    }
}
