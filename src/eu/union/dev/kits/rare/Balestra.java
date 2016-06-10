package eu.union.dev.kits.rare;

import eu.union.dev.KitManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Packets;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 26/05/2016.
 */
public class Balestra extends Kit implements Listener {

    Ability cooldown = new Ability(1, 3, TimeUnit.SECONDS);

    public Balestra() {
        super("balestra", "unkit.balestra", Difficulty.PRO, Rarity.RARE, 5, new Icon(Material.ARROW), Category.LONG_DISTANCE, 1000L);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.DEFAULT_BOW, 1);
        player.getInventory().addItem(new ItemStack(Material.ARROW,32));
    }

    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p, "balestra")) {
            if (p.getItemInHand().getType() == Material.BOW) {
                e.setCancelled(true);
                if (p.getInventory().contains(Material.ARROW)){
                    if (getAmount(p.getInventory(),Material.ARROW) >=2){
                        boolean status = true;
                        for (int i = 0; i < p.getInventory().getSize(); i++) {
                            if (p.getInventory().getItem(i).getType() == Material.ARROW && status){
                                status = false;
                                p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount()-1);
                            }
                        }
                    }else{
                        p.getInventory().remove(Material.ARROW);
                    }
                }
            }
        }
    }
    public static int getAmount(Inventory inv, Material m)
    {
        int amount = 0;
        for (ItemStack item : inv.getContents()) {
            if ((item != null) && (item.getType() == m) &&
                    (item.getAmount() > 0)) {
                amount += item.getAmount();
            }
        }
        return amount;
    }
}
