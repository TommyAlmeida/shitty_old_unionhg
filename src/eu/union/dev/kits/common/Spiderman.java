package eu.union.dev.kits.common;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 * Created by Fentis on 02/06/2016.
 */
public class Spiderman extends Kit implements Listener {

    public Spiderman() {//
        super("spiderman", false, Difficulty.LOW, Rarity.COMMON, new Icon(Material.WEB), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    @EventHandler
    public void onmove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if (p.getLocation().getBlock().getRelative(BlockFace.EAST).getType() != Material.AIR ||
                p.getLocation().getBlock().getRelative(BlockFace.WEST).getType() != Material.AIR ||
                p.getLocation().getBlock().getRelative(BlockFace.NORTH).getType() != Material.AIR ||
                p.getLocation().getBlock().getRelative(BlockFace.SOUTH).getType() != Material.AIR){
            KitManager km = KitManager.getManager();
            if (p.isSneaking() && km.getKitAmIUsing(p,"spiderman")){
                p.setVelocity(new Vector(0, 0.5, 0));
            }
        }
    }
}
