package eu.union.dev.kits.heroic;

import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 14/05/2016.
 */
public class Ninja extends Kit implements Listener {

    HashMap<Player, Player> ninja = new HashMap<>();
    Ability cooldown = new Ability(1, 15, TimeUnit.SECONDS);

    public Ninja() {//
        super("ninja", true, Difficulty.LOW, Rarity.HEROIC, new Icon(Material.COAL_BLOCK), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    @EventHandler
    public void hitplayer(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            final Player p = (Player) e.getDamager();
            Player d = (Player) e.getEntity();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p, "ninja")) {
                ninja.put(p, d);
            }
        }
    }

    @EventHandler
    public void onsneak(PlayerToggleSneakEvent e) {
        final Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p, "ninja") &&
                ninja.containsKey(p)) {
            Player t = ninja.get(p);
            if (t != null && !t.isDead()) {
                if (cooldown.tryUse(p)) {
                    if (p.getLocation().distance(t.getLocation()) < 30.0) {
                        p.teleport(t);
                    } else {
                        p.sendMessage("§cThe player is too far!");
                    }
                } else {
                    Util.getInstance().sendCooldownMessage(p, cooldown, TimeUnit.SECONDS, true);
                }
            }
        }
    }
}
