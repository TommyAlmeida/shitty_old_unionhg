package eu.union.dev.kits.common;

import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 28/06/2016.
 */
public class CookieMonster extends Kit implements Listener{

    public CookieMonster() {
        super("cookiemonster", false, Difficulty.LOW, Rarity.COMMON, new Icon(Material.COOKIE), 1000);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    Ability cooldown = new Ability(1,3, TimeUnit.SECONDS);
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p,"cookiemonster") &&
                e.getItem().getType() == Material.COOKIE) {
            e.setCancelled(true);
            if (cooldown.tryUse(p)){
                if (p.getHealth() < p.getMaxHealth()) {
                    double hp = p.getHealth() + 1.0D;
                    if (hp > p.getMaxHealth())
                        hp = p.getMaxHealth();
                    p.setHealth(hp);
                } else if (p.getFoodLevel() < 20) {
                    p.setFoodLevel(p.getFoodLevel() + 1);
                } else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1), true);
                }
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                if (e.getItem().getAmount() == 0) {
                    p.setItemInHand(new ItemStack(Material.AIR));
                }
            }
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {
        Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p,"cookiemonster") &&
                (e.getBlock().getType() == Material.LONG_GRASS ||
                        (e.getBlock().getType() == Material.DOUBLE_STEP &&
                                (e.getBlock().getData() == 2 ||
                                e.getBlock().getData() == 3)))){
            int porcentagem = new Random().nextInt(100);
            if (porcentagem <= 40){
                Location loc = e.getBlock().getLocation();
                loc.getWorld().dropItemNaturally(loc.add(0.5D, 0.0D, 0.5D), new ItemStack(Material.COOKIE));
            }
        }
    }
}
