package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Util;
import eu.union.dev.utils.Weapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 19/05/2016.
 */
public class CheckPoint extends Kit implements Listener {

    Ability cooldown = new Ability(1, 15, TimeUnit.SECONDS);
    HashMap<Player, Location> locs = new HashMap<>();

    public CheckPoint() {//
        super("checkpoint", false, Difficulty.LOW, Rarity.RARE, new Icon(Material.NETHER_FENCE), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.CHECKPOINT_FENCE);
        Weapon.giveWeapon(player, Weapon.CHECKPOINT_POT);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p, "checkpoint")) {
            if (p.getItemInHand().getType() == Material.NETHER_FENCE) {
                locs.put(p, p.getLocation());
                p.sendMessage("§aPosition saved!");
                e.setCancelled(true);
                p.updateInventory();
            }
            if (p.getItemInHand().getType() == Material.FLOWER_POT_ITEM) {
                e.setCancelled(true);
                p.updateInventory();
                if (locs.containsKey(p)) {
                    if (p.getLocation().distance(locs.get(p)) <= 50) {
                        if (cooldown.tryUse(p)) {
                            p.teleport(locs.get(p));
                        } else {
                            Util.getInstance().sendCooldownMessage(p, cooldown, TimeUnit.SECONDS, true);
                        }
                    } else {
                        p.sendMessage("§cTeleport only 50 blocks!");
                    }
                }
            }
        }
    }
    @EventHandler
    public void ondeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        if (locs.containsKey(p)){
            locs.remove(p);
        }
    }
}
