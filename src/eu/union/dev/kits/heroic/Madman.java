package eu.union.dev.kits.heroic;

import eu.union.dev.HG;
import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.events.HGTimerSecondsEvent;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Packets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * Created by Fentis on 24/05/2016.
 */
public class Madman extends Kit implements Listener {

    HashMap<String, Integer> vitimas = new HashMap<>();

    public Madman() {//
        super("madman", false, Difficulty.LOW, Rarity.HEROIC, new Icon(Material.REDSTONE), 1000L);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }
    @EventHandler
    public void onseconds(HGTimerSecondsEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p, "madman")) {
                for (Entity en : p.getNearbyEntities(20.0, 20.0, 20.0)) {
                    if (en instanceof Player) {
                        Player p2 = (Player) en;
                        if (!HGManager.getInstance().isSpec(p2) && HGManager.getInstance().inAdminMode(p2)){
                            if (vitimas.containsKey(p2.getName())) {
                                if (vitimas.get(p2.getName()) < 100) {
                                    vitimas.put(p2.getName(), vitimas.get(p2.getName()) + 1);
                                }
                            } else {
                                vitimas.put(p2.getName(), 1);
                            }
                            if (vitimas.get(p2.getName()) % 2 == 0) {
                                msg(p2, "§6§lMadman", vitimas.get(p2.getName()) / 2, 50, vitimas.get(p2.getName()));
                            }
                            if (vitimas.get(p2.getName()) <= 50) {
                                p2.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3 * 20, 0));
                            }
                            if (vitimas.get(p2.getName()) <= 100 &&
                                    vitimas.get(p2.getName()) >= 51) {
                                p2.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3 * 20, 1));
                            }
                            if (vitimas.get(p2.getName()) <= 100 &&
                                    vitimas.get(p2.getName()) >=75){
                                p2.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 20, 0));
                            }
                        }
                    }
                }
                for (Player ps : Bukkit.getOnlinePlayers()) {
                    if (ps.getLocation().distance(p.getLocation()) > 20) {
                        if (vitimas.containsKey(ps.getName())) {
                            vitimas.remove(ps.getName());
                            Packets.getAPI().sendActionBar(ps, "§aMadman is too far away! The effect has passed!");
                        }
                    }
                }
            }
        }
    }

    public void msg(Player p, String mensagem, int time, int maxtime, int timereal) {
        String msg = mensagem + ": §a";
        char type = '▌';
        if (time >= maxtime) {
            time = maxtime;
        }
        for (int i = 0; i < time; i++) {
            msg = msg + type;
        }
        msg = msg + "§c";
        for (int i = 0; i < maxtime - time; i++) {
            msg = msg + type;
        }
        msg = msg + " §f" + timereal + "%";
        Packets.getAPI().sendActionBar(p, msg);
    }

    @EventHandler
    public void onquit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (vitimas.containsKey(p.getName())) {
            vitimas.remove(p.getName());
        }
    }

    @EventHandler
    public void ondeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (vitimas.containsKey(p.getName())) {
            vitimas.remove(p.getName());
        }
    }
}
