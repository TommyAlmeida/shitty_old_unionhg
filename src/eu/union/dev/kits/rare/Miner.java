package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Weapon;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Fentis on 17/06/2016.
 */
public class Miner extends Kit implements Listener{

    public Miner() {
        super("miner", false, Difficulty.LOW, Rarity.RARE, new Icon(Material.STONE_PICKAXE), 1000);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player,Weapon.MINER_PICKAXE, Enchantment.DIG_SPEED,2);
    }
    @EventHandler
    public void onBreack(BlockBreakEvent e){
        if (e.getPlayer() != null){
            Player p = e.getPlayer();
            KitManager km = KitManager.getManager();
            if (km.getKitAmIUsing(p,"miner") &&
                    p.getItemInHand().getType().toString().contains("_PICKAXE")){
                if (e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.COAL_ORE){
                    miner(p,e.getBlock(),e.getBlock().getType());
                }
            }
        }
    }
    public void miner(Player p, Block b, Material m)
    {
        if (b.getRelative(BlockFace.UP).getType() == m) {
            Block b2 = b.getRelative(BlockFace.UP);
            b2.breakNaturally();
            b2.getWorld().playEffect(b2.getLocation(), Effect.STEP_SOUND, m.getId());
            miner(p, b2, m);
        }
        if (b.getRelative(BlockFace.DOWN).getType() == m) {
            Block b2 = b.getRelative(BlockFace.DOWN);
            b2.breakNaturally();
            b2.getWorld().playEffect(b2.getLocation(), Effect.STEP_SOUND, m.getId());
            miner(p, b2, m);
        }
        if (b.getRelative(BlockFace.EAST).getType() == m) {
            Block b2 = b.getRelative(BlockFace.EAST);
            b2.breakNaturally();
            b2.getWorld().playEffect(b2.getLocation(), Effect.STEP_SOUND, m.getId());
            miner(p, b2, m);
        }
        if (b.getRelative(BlockFace.WEST).getType() == m) {
            Block b2 = b.getRelative(BlockFace.WEST);
            b2.breakNaturally();
            b2.getWorld().playEffect(b2.getLocation(), Effect.STEP_SOUND, m.getId());
            miner(p, b2, m);
        }
        if (b.getRelative(BlockFace.NORTH).getType() == m) {
            Block b2 = b.getRelative(BlockFace.NORTH);
            b2.breakNaturally();
            b2.getWorld().playEffect(b2.getLocation(), Effect.STEP_SOUND, m.getId());
            miner(p, b2, m);
        }
        if (b.getRelative(BlockFace.SOUTH).getType() == m) {
            Block b2 = b.getRelative(BlockFace.SOUTH);
            b2.breakNaturally();
            b2.getWorld().playEffect(b2.getLocation(), Effect.STEP_SOUND, m.getId());
            miner(p, b2, m);
        }
    }
}
