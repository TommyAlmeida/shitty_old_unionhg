package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Fentis on 28/06/2016.
 */
public class Specialist extends Kit implements Listener{

    public Specialist() {
        super("specialist", false, Difficulty.MEDIUM, Rarity.RARE, new Icon(Material.ENCHANTMENT_TABLE), 1000);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player,Weapon.SPECIALIST_BOOK);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (p.getItemInHand().getType() == Material.ENCHANTED_BOOK &&
                km.getKitAmIUsing(p,"specialist")){
            p.openEnchanting(null,true);
        }
    }
    @EventHandler
    public void onKill(PlayerDeathEvent e){
        Player p = e.getEntity();
        if (p.getKiller() != null){
            if (p.getKiller() instanceof Player){
                Player p2 = p.getKiller();
                KitManager km = KitManager.getManager();
                if (km.getKitAmIUsing(p2,"specialist")){
                    p2.setLevel(p2.getLevel()+1);
                    p2.playSound(p2.getLocation(), Sound.ORB_PICKUP,1,1);
                }
            }
        }
    }
}
