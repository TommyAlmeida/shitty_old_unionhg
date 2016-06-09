package eu.union.dev.chests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
/**
 * Created by Fentis on 08/06/2016.
*/

public class ChestItems {
    public ArrayList<ChestRandomItems> randomItems = new ArrayList<>();
    public ChestItems(ChestType chesttype)
    {
        if (chesttype == ChestType.FEAST){
            randomItems.add(new ChestRandomItems(5.0D, Material.DIAMOND, 0, 1, 4));
            randomItems.add(new ChestRandomItems(5.0D, Material.DIAMOND_BOOTS, 0, 1, 1));
            randomItems.add(new ChestRandomItems(5.0D, Material.DIAMOND_CHESTPLATE, 0, 1, 1));
            randomItems.add(new ChestRandomItems(5.0D, Material.DIAMOND_HELMET, 0, 1, 1));
            randomItems.add(new ChestRandomItems(5.0D, Material.DIAMOND_LEGGINGS, 0, 1, 1));
            randomItems.add(new ChestRandomItems(5.0D, Material.DIAMOND_SWORD, 0, 1, 1));
            randomItems.add(new ChestRandomItems(5.0D, Material.DIAMOND_PICKAXE, 0, 1, 1));
            randomItems.add(new ChestRandomItems(20.0D, Material.MUSHROOM_SOUP, 0, 1, 7));
            randomItems.add(new ChestRandomItems(20.0D, Material.LAVA_BUCKET, 0, 1, 1));
            randomItems.add(new ChestRandomItems(20.0D, Material.WATER_BUCKET, 0, 1, 1));
            randomItems.add(new ChestRandomItems(20.0D, Material.BUCKET, 0, 1, 1));
            randomItems.add(new ChestRandomItems(20.0D, Material.WEB, 0, 1, 2));
            randomItems.add(new ChestRandomItems(20.0D, Material.ENDER_PEARL, 0, 1, 6));
            randomItems.add(new ChestRandomItems(20.0D, Material.FLINT_AND_STEEL, 0, 1, 1));
            randomItems.add(new ChestRandomItems(20.0D, Material.GRILLED_PORK, 0, 1, 20));
            randomItems.add(new ChestRandomItems(20.0D, Material.COOKED_BEEF, 0, 1, 20));
            randomItems.add(new ChestRandomItems(20.0D, Material.COOKED_CHICKEN, 0, 1, 20));
            randomItems.add(new ChestRandomItems(20.0D, Material.BREAD, 0, 1, 20));
            randomItems.add(new ChestRandomItems(20.0D, Material.BOW, 0, 1, 1));
            randomItems.add(new ChestRandomItems(20.0D, Material.ARROW, 0, 1, 20));
            randomItems.add(new ChestRandomItems(20.0D, Material.TNT, 0, 1, 20));
            randomItems.add(new ChestRandomItems(20.0D, Material.EXP_BOTTLE, 0, 1, 10));
            randomItems.add(new ChestRandomItems(10.0D, Material.POTION, 16420, 1, 1));
            randomItems.add(new ChestRandomItems(10.0D, Material.POTION, 16425, 1, 1));
            randomItems.add(new ChestRandomItems(10.0D, Material.POTION, 16418, 1, 1));

            randomItems.add(new ChestRandomItems(10.0D, Material.POTION, 16426, 1, 1));
            randomItems.add(new ChestRandomItems(10.0D, Material.POTION, 16428, 1, 1));
            randomItems.add(new ChestRandomItems(10.0D, Material.POTION, 16421, 1, 1));
            randomItems.add(new ChestRandomItems(10.0D, Material.POTION, 16417, 1, 1));

            randomItems.add(new ChestRandomItems(10.0D, Material.POTION, 16385, 1, 1));

            randomItems.add(new ChestRandomItems(10.0D, Material.POTION, 16451, 1, 1));

            randomItems.add(new ChestRandomItems(1.0D, Material.POTION, 16462, 1, 1));
        }
        if (chesttype == ChestType.MINIFEAST){
            randomItems.add(new ChestRandomItems(5.0D, Material.DIAMOND, 0, 1, 3));
            randomItems.add(new ChestRandomItems(5.0D, Material.IRON_INGOT, 0, 1, 6));
            randomItems.add(new ChestRandomItems(20.0D, Material.LAVA_BUCKET, 0, 1, 1));
            randomItems.add(new ChestRandomItems(20.0D, Material.WATER_BUCKET, 0, 1, 1));
            randomItems.add(new ChestRandomItems(20.0D, Material.TNT, 0, 1, 20));
            randomItems.add(new ChestRandomItems(20.0D, Material.MUSHROOM_SOUP, 0, 1, 7));
            randomItems.add(new ChestRandomItems(20.0D, Material.FLINT_AND_STEEL, 0, 1, 1));
        }
    }
    public enum ChestType {
        MINIFEAST("MiniFeast"),
        FEAST("Feast");

        String d;

        ChestType(String d) {
            this.d = d;
        }

        public String value() {
            return this.d;
        }
    }
    public void addRandomItem(ChestRandomItems item) {
        this.randomItems.add(item);
    }

    public void clearRandomItems() {
        this.randomItems.clear();
    }

    private int countItems(Inventory inv) {
        int i = 0;
        for (ItemStack item : inv.getContents())
            if ((item != null) && (item.getType() != Material.AIR))
                i++;
        return i;
    }

    public void fillChest(Inventory inv) {
        if ((inv.getHolder() instanceof DoubleChest))
            fillChest(inv, true);
        else
            fillChest(inv, false);
    }

    private void fillChest(Inventory inv, boolean doubleChest) {
        inv.clear();
        if (this.randomItems.size() > 0)
            while (countItems(inv) < (doubleChest ? 10 : 4)) {
                Collections.shuffle(this.randomItems, new Random());
                for (ChestRandomItems item : this.randomItems)
                    if (item.hasChance())
                        inv.setItem(new Random().nextInt(inv.getSize()), item.getItemStack());
            }
    }

    public ArrayList<ChestRandomItems> getRandomItems()
    {
        return this.randomItems;
    }

    public void setRandomItems(ArrayList<ChestRandomItems> items) {
        this.randomItems = items;
    }

}