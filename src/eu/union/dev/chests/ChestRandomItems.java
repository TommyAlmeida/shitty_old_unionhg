package eu.union.dev.chests;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Fentis on 08/06/2016.
 */
public class ChestRandomItems
        implements ConfigurationSerializable
{
    private double chanceOfItemStackAppearing;
    private short itemData;
    private Material itemType;
    private int minItems;
    private int maxItems;

    @SuppressWarnings("deprecation")
    public static ChestRandomItems deserialize(Map<String, Object> args)
    {
        Material type = null;
        Object obj = args.get("Item Type");
        if ((obj instanceof Integer))
            type = Material.getMaterial(((Integer)obj).intValue());
        else if ((obj instanceof String))
            type = Material.getMaterial((String)obj);
        else
            throw new RuntimeException(obj + " is not a valid item type");
        short itemData = (short)((Integer)args.get("Item Data")).intValue();
        double chancesOfItemStackAppearing = ((Double)args.get("Chances in 100 of itemstack appearing")).doubleValue();
        int minItems = ((Integer)args.get("Min Items")).intValue();
        int maxItems = ((Integer)args.get("Max Items")).intValue();
        return new ChestRandomItems(chancesOfItemStackAppearing, type, itemData, minItems, maxItems);
    }

    @SuppressWarnings("deprecation")
    public ChestRandomItems(double newChance, int newId, int newData, int newMin, int newMax)
    {
        this.chanceOfItemStackAppearing = newChance;
        this.itemType = Material.getMaterial(newId);
        this.itemData = ((short)newData);
        this.minItems = newMin;
        this.maxItems = newMax;
    }

    public ChestRandomItems(double newChance, Material mat, int newData, int newMin, int newMax)
    {
        this.chanceOfItemStackAppearing = newChance;
        this.itemType = mat;
        this.itemData = ((short)newData);
        this.minItems = newMin;
        this.maxItems = newMax;
    }

    public ChestRandomItems(Map<String, Object> args) {
        this(((Double)args.get("chancesOfItemStackAppearing")).doubleValue(), (Material)args.get("itemType"), ((Short)args.get("itemData")).shortValue(), ((Integer)args.get("minItems")).intValue(), ((Integer)args.get("maxItems")).intValue());
    }

    public ItemStack getItemStack()
    {
        int amount = Math.max(this.maxItems - this.minItems + 1, 1);
        amount = new Random().nextInt(amount) + 1;
        return new ItemStack(this.itemType, amount, this.itemData);
    }

    public boolean hasChance()
    {
        return new Random().nextInt(10000) < this.chanceOfItemStackAppearing * 100.0D;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, Object> serialize()
    {
        Map result = new LinkedHashMap<>();
        result.put("Item Type", this.itemType.name());
        result.put("Item Data", Short.valueOf(this.itemData));
        result.put("Chances in 100 of itemstack appearing", Double.valueOf(this.chanceOfItemStackAppearing));
        result.put("Min Items", Integer.valueOf(this.minItems));
        result.put("Max Items", Integer.valueOf(this.maxItems));
        return result;
    }

    public String toString() {
        return (this.chanceOfItemStackAppearing + " " + this.minItems + " " + this.maxItems + " " + this.itemType + " " + this.itemData).trim();
    }

    static
    {
        ConfigurationSerialization.registerClass(ChestRandomItems.class, ChestRandomItems.class.getSimpleName());
    }
}