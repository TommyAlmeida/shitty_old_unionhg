package eu.union.dev.kits.common;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Created by Fentis on 14/05/2016.
 */
public class Fisherman extends Kit implements Listener {

    public Fisherman() {//
        super("fisherman", false, Difficulty.LOW, Rarity.COMMON,  new Icon(Material.FISHING_ROD), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.FISHERMAN_ROD);
    }

    @EventHandler
    public void onfishing(PlayerFishEvent e) {
        Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (e.getCaught() instanceof Player) {
            Player p2 = (Player) e.getCaught();
            if (km.getKitAmIUsing(p, "fisherman")) {
                p2.teleport(p.getLocation());
            }
        }
    }
}
