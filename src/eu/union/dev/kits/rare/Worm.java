package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Weapon;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Fentis on 17/06/2016.
 */
public class Worm extends Kit implements Listener{

    public Worm() {
        super("worm", "unkit.worm", Difficulty.LOW, Rarity.COMMON, new Icon(Material.DIRT), 1000);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    @EventHandler
    public void onblockDamage(BlockDamageEvent e)
    {
        if (e.getPlayer() != null) {
            Player p = e.getPlayer();
            Damageable hp = p;
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p, "worm") &&
                    (e.getBlock().getType() == Material.DIRT))
                if (hp.getHealth() < p.getMaxHealth()) {
                    p.setHealth(hp.getHealth() + 2.0D);
                    e.getBlock().setType(Material.AIR);
                    e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, 3);
                } else {
                    e.getBlock().breakNaturally();
                    e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, 3);
                }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player)e.getEntity();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p, "worm")){
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL){
                    Location loc = e.getEntity().getLocation();
                    boolean dirt = false;
                    for (float x = -0.35F; (x <= 0.35F) && (!dirt); x += 0.35F) {
                        for (float z = -0.35F; (z <= 0.35F) && (!dirt); z += 0.35F) {
                            Block b = loc.clone().add(x, -1.0D, z).getBlock();
                            if (b.getType() == Material.DIRT)
                                dirt = true;
                        }
                    }
                    if (dirt) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
