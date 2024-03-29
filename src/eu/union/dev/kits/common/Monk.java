package eu.union.dev.kits.common;

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
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 16/05/2016.
 */
public class Monk extends Kit implements Listener {

    Ability cooldown = new Ability(1, 15, TimeUnit.SECONDS);

    public Monk() {//
        super("monk", true, Difficulty.LOW, Rarity.COMMON, new Icon(Material.BLAZE_ROD), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.MONK_ROD);
    }

    @EventHandler
    public void onclick(PlayerInteractEntityEvent e) {
        Player monk = e.getPlayer();
        if (e.getRightClicked() instanceof Player) {
            Player monked = (Player) e.getRightClicked();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(monk, "monk") &&
                    monk.getItemInHand().getType() == Material.BLAZE_ROD) {
                if (cooldown.tryUse(monk)) {
                    int random = new Random().nextInt(17) + 8;
                    ItemStack i1 = monked.getItemInHand();
                    ItemStack i2 = monked.getInventory().getItem(random);
                    monked.setItemInHand(i2);
                    monked.getInventory().setItem(random, i1);
                    monked.sendMessage("§aYou were Monked!");
                } else {
                    Util.getInstance().sendCooldownMessage(monk, cooldown, TimeUnit.SECONDS, true);
                }
            }
        }
    }
}
