package eu.union.dev.utils;

import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.inventory.ItemStack;

/**
 * Design all lore features in a fancy way
 */
public interface Layout {

    ItemStack design(Icon icon, Kit kit);
}
