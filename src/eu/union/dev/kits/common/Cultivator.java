package eu.union.dev.kits.common;

import eu.union.dev.HG;
import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Bukkit;
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
        super("cultivator", true, Difficulty.LOW, Rarity.COMMON, new Icon(Material.SEEDS), 1000);
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
                Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        byte data = b.getData();
                        switch (data){
                            case (byte)0:
                                b.getWorld().generateTree(b.getLocation(),TreeType.TREE);
                                break;
                            case (byte)1:
                                b.getWorld().generateTree(b.getLocation(),TreeType.REDWOOD);
                                break;
                            case (byte)2:
                                b.getWorld().generateTree(b.getLocation(),TreeType.BIRCH);
                                break;
                            case (byte)3:
                                b.getWorld().generateTree(b.getLocation(),TreeType.SMALL_JUNGLE);
                                break;
                            case (byte)4:
                                b.getWorld().generateTree(b.getLocation(),TreeType.REDWOOD);
                                break;
                            case (byte)5:
                                b.getWorld().generateTree(b.getLocation(),TreeType.ACACIA);
                                break;
                            case (byte)6:
                                b.getWorld().generateTree(b.getLocation(),TreeType.DARK_OAK);
                                break;
                            default:
                                b.getWorld().generateTree(b.getLocation(),TreeType.TREE);
                                break;
                        }
                    }
                },1);
            }
            if (b.getType() == Material.CROPS){
                b.setData((byte)7);
            }
        }
    }
}
