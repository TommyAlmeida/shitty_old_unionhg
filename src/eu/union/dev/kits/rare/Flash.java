package eu.union.dev.kits.rare;

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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 14/05/2016.
 */
public class Flash extends Kit implements Listener {

    Ability cooldown = new Ability(1, 15, TimeUnit.SECONDS);

    public Flash() {//
        super("flash", false, Difficulty.LOW, Rarity.RARE, new Icon(Material.REDSTONE_TORCH_ON), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.FLASH_TORCH);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (p.getItemInHand().getType() == Material.REDSTONE_TORCH_ON &&
                (e.getAction() == Action.RIGHT_CLICK_AIR ||
                        e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p, "flash")) {
                e.setCancelled(true);
                p.updateInventory();
                if (cooldown.tryUse(p)) {
                    Block b = p.getTargetBlock((HashSet<Byte>) null, 100);
                    if (b.getType() != Material.AIR &&
                            b.getRelative(BlockFace.UP).getType() == Material.AIR &&
                            b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.AIR) {
                        p.getWorld().strikeLightningEffect(p.getLocation());
                        p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 5);

                        Location bl = b.getRelative(BlockFace.UP).getLocation().add(0.5, 0.0, 0.5);
                        bl.setPitch(p.getLocation().getPitch());
                        bl.setYaw(p.getLocation().getYaw());
                        p.teleport(bl);
                        p.getWorld().strikeLightningEffect(p.getLocation());
                        p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 5);
                    }
                } else {
                    Util.getInstance().sendCooldownMessage(p, cooldown, TimeUnit.SECONDS, true);
                }
            }
        }
    }
}
