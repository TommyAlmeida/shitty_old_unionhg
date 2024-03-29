package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Fentis on 26/05/2016.
 */
public class NoFall extends Kit implements Listener {

    public NoFall() {//
        super("nofall", false, Difficulty.LOW, Rarity.RARE, new Icon(Material.STRING), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    @EventHandler
    public void ondamagefall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p, "nofall")) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
