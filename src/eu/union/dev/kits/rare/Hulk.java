package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Util;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 16/05/2016.
 */
public class Hulk extends Kit implements Listener {

    Ability cooldown = new Ability(1, 30, TimeUnit.SECONDS);

    public Hulk() {//
        super("hulk", false, Difficulty.MEDIUM, Rarity.RARE, new Icon(Material.SLIME_BALL), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.HULK_SLIME);
    }

    @EventHandler
    public void onclick(PlayerInteractEntityEvent e) {
        Player hulk = e.getPlayer();
        if (e.getRightClicked() instanceof Player) {
            Player pux = (Player) e.getRightClicked();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(hulk, "hulk") &&
                    hulk.getItemInHand().getType() == Material.SLIME_BALL) {
                if (cooldown.tryUse(hulk)) {
                    if (hulk.getPassenger() == null &&
                            hulk.getVehicle() != pux) {
                        hulk.setPassenger(pux);
                        pux.sendMessage("§cYou were caught by a Hulk! Sneak out of it!");
                    }
                } else {
                    Util.getInstance().sendCooldownMessage(hulk, cooldown, TimeUnit.SECONDS, true);
                }
            }
        }
    }

    @EventHandler
    public void ondeath(PlayerDeathEvent e) {
        e.getEntity().leaveVehicle();
        e.getEntity().eject();
    }
}
