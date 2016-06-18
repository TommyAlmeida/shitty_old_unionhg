package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Util;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 17/06/2016.
 */
public class Pyro extends Kit implements Listener{

    public Pyro() {
        super("pyro", "unkit.pyro", Difficulty.PRO, Rarity.RARE, 5, new Icon(Material.FIREBALL), Category.LONG_DISTANCE, 1000);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.PYRO_FIREBALL);
    }
    Ability cooldown = new Ability(1,30, TimeUnit.SECONDS);
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (p.getItemInHand().getType() == Material.FIREBALL && km.getKitAmIUsing(p, "pyro")){
            e.setCancelled(true);
            p.updateInventory();
            if (cooldown.tryUse(p)){
                Fireball ball = (Fireball)p.launchProjectile(Fireball.class);
                ball.setIsIncendiary(true);
                ball.setYield(ball.getYield() * 1.5F);
            }else{
                Util.getInstance().sendCooldownMessage(p, cooldown, TimeUnit.SECONDS, true);
            }
        }
    }
}
