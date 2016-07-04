package eu.union.dev.kits.rare;

import eu.union.dev.HG;
import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Weapon;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 28/06/2016.
 */
public class JackHammer extends Kit implements Listener{

    public JackHammer() {
        super("jackhammer", false, Difficulty.LOW, Rarity.RARE, new Icon(Material.STONE_AXE), 1000);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player,Weapon.JACK_HAMMER);
    }
    Ability cooldown = new Ability(1,15, TimeUnit.SECONDS);
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if (e.getPlayer() != null){
            Player p = e.getPlayer();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p,"jackhammer") &&
                    p.getItemInHand().getType() == Material.STONE_AXE){
                e.setCancelled(true);
                if (cooldown.tryUse(p)){
                    new BukkitRunnable(){
                        Block bu = e.getBlock();
                        Block bd = e.getBlock();
                        @Override
                        public void run() {
                            if (bu.getLocation().getY() <= HGManager.getInstance().getCamadalimite()){
                                bu.setType(Material.AIR);
                                bu.getWorld().playEffect(bu.getLocation(), Effect.STEP_SOUND,bu.getTypeId(),bu.getData());
                                bu = bu.getRelative(BlockFace.UP);
                            }
                            if (bd.getType() != Material.BEDROCK){
                                bd.setType(Material.AIR);
                                bd.getWorld().playEffect(bd.getLocation(), Effect.STEP_SOUND,bd.getTypeId(),bd.getData());
                                bd = bd.getRelative(BlockFace.DOWN);
                            }
                            if (bu.getLocation().getY() >= HGManager.getInstance().getCamadalimite() &&
                                    bd.getType() == Material.BEDROCK){
                                cancel();
                            }
                        }
                    }.runTaskTimer(HG.getInstance(),0,3);
                }
            }
        }
    }
}
