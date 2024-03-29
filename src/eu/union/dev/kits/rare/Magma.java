package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

/**
 * Created by Fentis on 18/05/2016.
 */
public class Magma extends Kit implements Listener {

    public Magma() {//
        super("magma", false, Difficulty.LOW, Rarity.RARE, new Icon(Material.MAGMA_CREAM), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    @EventHandler
    public void onclick(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player &&
                e.getDamager() instanceof Player) {
            Player magma = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(magma, "magma")) {
                int porcentagem = new Random().nextInt(100);
                if (porcentagem <= 15) {
                    damager.setFireTicks((new Random().nextInt(3) + 3) * 20);
                }
            }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player)e.getEntity();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p,"magma") &&
                    (e.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                            e.getCause() == EntityDamageEvent.DamageCause.LAVA ||
                    e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)){
                e.setCancelled(true);
            }
        }
    }
}
