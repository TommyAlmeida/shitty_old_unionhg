package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by Fentis on 26/05/2016.
 */
public class Tank extends Kit implements Listener {

    public Tank() {//
        super("tank", "unkit.tank", Difficulty.LOW, Rarity.RARE, 2, new Icon(Material.IRON_BLOCK), Category.SWORDS, 1000L);
    }

    @Override
    public void applyKit(Player player) {
        //ganah nada
    }

    @EventHandler
    public void ondeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p, "tank")) {
            p.getWorld().createExplosion(p.getLocation(),3.0F,true);
            p.getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_LARGE, 10);
            p.getWorld().playSound(p.getLocation(), Sound.EXPLODE, 1, 1);
        }
    }
}