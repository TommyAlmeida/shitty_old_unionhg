package eu.union.dev.listeners;

import eu.union.dev.HGManager;
import eu.union.dev.invs.CompassMenu;
import eu.union.dev.utils.CompassCompare;
import eu.union.dev.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fentis on 21/06/2016.
 */
public class CompassListener implements Listener{

    @EventHandler
    public void onCompass(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (p.getItemInHand().getType() == Material.COMPASS){
            String message = "§c§lNo Players Nearby!";
            List<Player> players = new ArrayList<>();
            for (Player ps : p.getWorld().getPlayers()){
                if (!(ps.getUniqueId().equals(p.getUniqueId())) &&
                        !HGManager.getInstance().isSpec(ps) &&
                        !HGManager.getInstance().inAdminMode(ps) &&
                        ps.getGameMode() == GameMode.SURVIVAL &&
                        ps.getLocation().distance(p.getLocation()) >=10.0){
                    players.add(ps);
                }
            }
            Collections.sort(players, new CompassCompare(p));
            Player nearest = null;

            try {
                nearest = players.get(0);
            }catch (IndexOutOfBoundsException ex){}
            if (nearest != null){
                message = "§aPlayer: §7"+nearest.getName()+" " +
                        "§aDistance: §7"+((int)nearest.getLocation().distance(p.getLocation()));
                p.setCompassTarget(nearest.getLocation());
            }
            p.sendMessage(Messages.PREFIX+" "+message);
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
                if (HGManager.getInstance().isSpec(p) || HGManager.getInstance().inAdminMode(p)){
                    Inventory inv = Bukkit.createInventory(null,6*9,"Teleport");
                    CompassMenu compassMenu = new CompassMenu();
                    compassMenu.setItems(p,inv,1);
                    p.openInventory(inv);
                }
            }
        }
    }

}
