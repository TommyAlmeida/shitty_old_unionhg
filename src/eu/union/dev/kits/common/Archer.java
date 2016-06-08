package eu.union.dev.kits.common;

import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Perms;
import eu.union.dev.utils.Weapon;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

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
        Weapon.giveWeapon(player, Weapon.DEFAULT_SWORD);
        Weapon.giveWeapon(player, Weapon.DEFAULT_BOW, 1, Enchantment.ARROW_INFINITE, 1);
        Weapon.giveWeapon(player, Weapon.DEFAULT_ARROW, 35);
    }
}
