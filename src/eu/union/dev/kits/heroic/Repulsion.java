package eu.union.dev.kits.heroic;

import eu.union.dev.HG;
import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Packets;
import eu.union.dev.utils.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 18/05/2016.
 */
public class Repulsion extends Kit implements Listener {

    Ability cooldown = new Ability(1, 15, TimeUnit.SECONDS);

    public Repulsion() {//
        super("repulsion", false, Difficulty.LOW, Rarity.HEROIC, new Icon(Material.STICK), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (p.getItemInHand().getType().toString().contains("_SWORD") &&
                km.getKitAmIUsing(p, "repulsion") &&
                (e.getAction() == Action.RIGHT_CLICK_AIR ||
                        e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (cooldown.tryUse(p)) {
                new BukkitRunnable() {
                    int i = 0;

                    @SuppressWarnings("deprecation")
                    @Override
                    public void run() {
                        i++;
                        if (i <= 10) {
                            if (!p.isBlocking() ||
                                    !p.getItemInHand().getType().toString().contains("_SWORD")) {
                                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                                for (Entity en : p.getNearbyEntities(5.0, 5.0, 5.0)) {
                                    if (en instanceof Player) {
                                        Vector v = en.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(i);
                                        en.setVelocity(v);
                                    }
                                }
                                cancel();
                            } else {
                                p.playNote(p.getLocation(), (byte) 0, (byte) (i + 10));
                                msg(p, "§b§lPower", i, 10);
                            }
                        } else {
                            for (Entity en : p.getNearbyEntities(5.0, 5.0, 5.0)) {
                                if (en instanceof Player) {
                                    Vector v = en.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(i);
                                    en.setVelocity(v);
                                }
                            }
                            cancel();
                        }
                    }
                }.runTaskTimer(HG.getInstance(), 0, 20);
            } else {
                Util.getInstance().sendCooldownMessage(p, cooldown, TimeUnit.SECONDS, true);
            }
        }
    }

    public void msg(Player p, String mensagem, int time, int maxtime) {
        String msg = mensagem + ": §a";
        char type = '▊';
        for (int i = 0; i < time; i++) {
            msg = msg + type;
        }
        msg = msg + "§c";
        for (int i = 0; i < maxtime - time; i++) {
            msg = msg + type;
        }
        Packets.getAPI().sendActionBar(p, msg);
    }
}
