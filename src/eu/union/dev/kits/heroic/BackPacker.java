package eu.union.dev.kits.heroic;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Fentis on 20/06/2016.
 */
public class BackPacker extends Kit implements Listener{

    public BackPacker() {
        super("backpacker", "unkit.backpacker", Difficulty.LOW, Rarity.HEROIC, 5, new Icon(Material.ENDER_CHEST), Category.NONE, 1000);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player,Weapon.BACKPACKER,9);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        Player p = (Player)e.getWhoClicked();
        KitManager km = KitManager.getManager();
        if (km.getKitAmIUsing(p,"backpacker") && e.getCurrentItem().getType() == Material.ENDER_CHEST){
            e.setCancelled(true);
            p.openInventory(p.getEnderChest());
        }
    }
}
