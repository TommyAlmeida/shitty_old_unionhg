package eu.union.dev.kits.common;

import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Perms;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Fentis on 08/06/2016.
 */
public class Archer extends Kit{

    public Archer() {
        super("archer", Perms.KIT_FREE.toString(), Difficulty.PRO,
                Rarity.COMMON, 0, new Icon(Material.BOW), Category.LONG_DISTANCE
                , 1000L);
    }

    @Override
    public void applyKit(Player player) {
        Weapon.giveWeapon(player, Weapon.DEFAULT_BOW);
        player.getInventory().addItem(new ItemStack(Material.ARROW,32));
    }
}
