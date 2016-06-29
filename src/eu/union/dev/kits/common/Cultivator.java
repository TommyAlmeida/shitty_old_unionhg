package eu.union.dev.kits.common;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Fentis on 28/06/2016.
 */
public class Cultivator extends Kit implements Listener{

    public Cultivator() {
        super("cultivator", "unkit.cultivator", Difficulty.LOW, Rarity.COMMON, new Icon(Material.SEEDS), 1000);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e)
    {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p,"cultivator")) {
            if (b.getType() == Material.SAPLING) {
                int data = b.getData();
                switch (data){
                    case 1:
                        b.getWorld().generateTree(b.getLocation(),TreeType.REDWOOD);
                        break;
                    case 2:
                        b.getWorld().generateTree(b.getLocation(),TreeType.BIRCH);
                        break;
                    case 3:
                        b.getWorld().generateTree(b.getLocation(),TreeType.SMALL_JUNGLE);
                        break;
                    case 4:
                        b.getWorld().generateTree(b.getLocation(),TreeType.REDWOOD);
                        break;
                    case 5:
                        b.getWorld().generateTree(b.getLocation(),TreeType.ACACIA);
                        break;
                    case 6:
                        b.getWorld().generateTree(b.getLocation(),TreeType.DARK_OAK);
                        break;
                }
            }
            if (b.getType() == Material.CROPS){
                b.setData((byte)7);
            }
        }
    }
}
