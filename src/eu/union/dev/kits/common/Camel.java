package eu.union.dev.kits.common;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Fentis on 17/06/2016.
 */
public class Camel extends Kit implements Listener{

    public Camel() {
        super("camel", "unkit.camel", Difficulty.LOW, Rarity.COMMON, new Icon(Material.SAND), 1000);
    }

    @Override
    public void applyKit(Player player) {
        //ganha nada
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND ||
                e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SANDSTONE){
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p,"camel")){
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0));
            }
        }
    }
}
