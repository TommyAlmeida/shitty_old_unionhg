package eu.union.dev.kits.common;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Weapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Fentis on 17/06/2016.
 */
public class LumberJack extends Kit implements Listener{

    public LumberJack() {
        super("lumberjack", "unkit.lumberjack", Difficulty.LOW, Rarity.COMMON, 5, new Icon(Material.WOOD_AXE), Category.BROKEN, 1000);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player,Weapon.LUMBERJACK_AXE);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if (e.getPlayer() != null){
            Player p = e.getPlayer();
            KitManager km = KitManager.getManager();
            if (p.getItemInHand().getType().toString().contains("_AXE") &&
                    km.getKitAmIUsing(p, "lumberjack") &&
                    (e.getBlock().getType() == Material.LOG ||
                    e.getBlock().getType() == Material.LOG_2)){
                Block b = e.getBlock();
                Double y = Double.valueOf(b.getLocation().getY() + 1.0D);
                Location l = new Location(b.getWorld(), b.getLocation().getX(), y.doubleValue(), b.getLocation().getZ());
                while (l.getBlock().getType() == Material.LOG)
                {
                    l.getBlock().breakNaturally();
                    Double localDouble1 = y;
                    Double localDouble2 = y = Double.valueOf(y.doubleValue() + 1.0D);
                    l.setY(y.doubleValue());
                }
                while (l.getBlock().getType() == Material.LOG_2)
                {
                    l.getBlock().breakNaturally();
                    Double localDouble1 = y;
                    Double localDouble2 = y = Double.valueOf(y.doubleValue() + 1.0D);
                    l.setY(y.doubleValue());
                }
            }
        }
    }
}
