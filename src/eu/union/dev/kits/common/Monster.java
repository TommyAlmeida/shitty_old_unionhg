package eu.union.dev.kits.common;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * Created by Fentis on 18/06/2016.
 */
public class Monster extends Kit implements Listener{

    public Monster() {
        super("monster", "unkit.monster", Difficulty.LOW, Rarity.COMMON, 5, new Icon(Material.SKULL_ITEM), Category.NONE, 1000);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    @EventHandler
    public void onTarget(EntityTargetEvent e){
        if (e.getTarget() instanceof Player){
            Player p = (Player)e.getTarget();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p, "monster")){
                if (e.getReason() != EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY ||
                        e.getReason() != EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER ||
                        e.getReason() != EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET){
                    e.setCancelled(true);
                }
            }
        }
    }
}
