package eu.union.dev.kits.heroic;

import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Util;
import eu.union.dev.utils.Weapon;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 25/05/2016.
 */
public class C4 extends Kit implements Listener {

    public static Map<Player, Item> c4item = new HashMap<>();
    Ability cooldown = new Ability(1, 15, TimeUnit.SECONDS);

    public C4() {//
        super("c4", false, Difficulty.MEDIUM, Rarity.HEROIC, new Icon(Material.TNT), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.C4_SLIME);
    }

    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p, "c4")) {
            if (p.getItemInHand().getType() == Material.SLIME_BALL) {
                if (cooldown.tryUse(p)) {
                    p.getItemInHand().setType(Material.LEVER);
                    Item c4 = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(Material.TNT));
                    c4.setVelocity(e.getPlayer().getEyeLocation().getDirection().multiply(0.5D));
                    c4.setPickupDelay(10000);
                    c4item.put(p, c4);
                    return;
                } else {
                    Util.getInstance().sendCooldownMessage(p, cooldown, TimeUnit.SECONDS, true);
                }
            }
            if (p.getItemInHand().getType() == Material.LEVER) {
                e.setCancelled(true);
                if (c4item.containsKey(p)) {
                    Location loc = c4item.get(p).getLocation();
                    p.getWorld().createExplosion(loc,3.0F,true);
                    c4item.get(p).remove();
                    c4item.remove(p);
                    p.getWorld().playEffect(loc, Effect.EXPLOSION_LARGE, 10);
                    p.getWorld().playSound(loc, Sound.EXPLODE, 1, 1);
                    p.getItemInHand().setType(Material.SLIME_BALL);
                }
            }
        }
    }
}
