package eu.union.dev.kits.common;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Created by Fentis on 16/05/2016.
 */
public class Viper extends Kit implements Listener {

    public Viper() {//
        super("viper", false, Difficulty.LOW, Rarity.COMMON, new Icon(Material.SPIDER_EYE), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        //Ganha nada
    }

    @EventHandler
    public void ondamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player &&
                e.getDamager() instanceof Player) {
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing((Player) e.getDamager(), "viper")) {
                if (new Random().nextInt(100) <= 15) {
                    ((Player) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, (new Random().nextInt(5) + 3) * 20, 0));
                }
            }
        }
    }
}
