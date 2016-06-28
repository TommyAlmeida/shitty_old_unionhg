package eu.union.dev.kits.common;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Fentis on 17/06/2016.
 */
public class Fireman extends Kit implements Listener{

    public Fireman() {
        super("fireman", "unkit.fireman", Difficulty.LOW, Rarity.COMMON, new Icon(Material.WATER_BUCKET), 1000);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.FIREMAN_BUCKET);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            KitManager km = KitManager.getManager();
            if ((e.getCause() == EntityDamageEvent.DamageCause.LAVA ||
                    e.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                    e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) &&
                    km.getKitAmIUsing(p,"fireman")){
                e.setCancelled(true);
            }
        }
    }
}
