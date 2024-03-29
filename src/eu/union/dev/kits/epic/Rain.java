package eu.union.dev.kits.epic;

import eu.union.dev.HG;
import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Util;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 31/05/2016.
 */
public class Rain extends Kit implements Listener {

    public Rain() {//
        super("rain", false, Difficulty.MEDIUM, Rarity.EPIC, new Icon(Material.ARROW), 800L);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.RAIN_ARROW);
    }

    Ability cooldown = new Ability(1,15, TimeUnit.SECONDS);

    @EventHandler
    public void onclick(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p,"rain") && p.getItemInHand().getType() == Material.ARROW && e.getRightClicked() instanceof Player){
            if (cooldown.tryUse(p)){
                new BukkitRunnable() {
                    int i = 0;
                    @Override
                    public void run() {
                        if (i<=6){
                            p.getWorld().spawn(e.getRightClicked().getLocation().add(0, 2.0, 0), Arrow.class);
                        }else{
                            p.getWorld().strikeLightning(e.getRightClicked().getLocation());
                            cancel();
                        }
                        i++;
                    }
                }.runTaskTimer(HG.getInstance(), 0, 10);
            }else{
                Util.getInstance().sendCooldownMessage(p, cooldown, TimeUnit.SECONDS, true);
            }
        }
    }
}
