package eu.union.dev.utils;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Weapon {


    MENU_ADMIN(Material.BLAZE_POWDER, "§eMenu Admin§7 (Right Click Player)"),
    STATUS_ADMIN(Material.SKULL_ITEM, "§eStatus Player§7 (Right Click Player)"),
    SWITCH_ADMIN(Material.SLIME_BALL, "§eSwitch Admin§7 (Right Click)"),
    COMPASS(Material.COMPASS, "§cCompass"),
    DEFAULT_SWORD(Material.WOOD_SWORD, "§aDefault Sword"),
    DEFAULT_BOW(Material.BOW, "§aDefault Bow"),
    DEFAULT_ARROW(Material.ARROW, "§aArrow"),
    GRANDPA_STICK(Material.STICK, "§eGrandPa Cane"),
    PULSAR_SHOCK(Material.MAGMA_CREAM, "§ePulsar Shock"),
    ICECUBE_ITEM(Material.PACKED_ICE, "§aIceCube Item"),
    EXPLODER_TNT(Material.BOW, "§eExploder TnT"),
    ENDERMAGE_PORTAL(Material.ENDER_PORTAL_FRAME, "§5Endermage Portal"),
    FISHERMAN_ROD(Material.FISHING_ROD, "§eFisherman Rod"),
    SWITCHER_SNOW_BALL(Material.SNOW_BALL, "§7Switcher Ball"),
    KANGAROO_FIREWORK(Material.FIREWORK, "§cKangaroo Firework"),
    FLASH_TORCH(Material.REDSTONE_TORCH_ON, "§4Flash Torch"),
    PHANTOM_FEATHER(Material.FEATHER, "§7Phantom Feather"),
    THOR_HAMMER(Material.GOLD_AXE, "§6Thor Hammer"),
    SPECIALIST_BOOK(Material.ENCHANTED_BOOK, "§9Specialist Book"),
    TIMELORD_CLOCK(Material.WATCH, "§6TimeLord Clock"),
    HULK_SLIME(Material.SLIME_BALL, "§aHulk Catch"),
    MONK_ROD(Material.BLAZE_ROD, "§6Monk's Rod"),
    JUMPFALL_PAPER(Material.PAPER, "§7JumpFall Paper"),
    CHECKPOINT_FENCE(Material.NETHER_FENCE, "§4Set Position"),
    CHECKPOINT_POT(Material.FLOWER_POT_ITEM, "§5Teleport"),
    SPECTRE_SUGAR(Material.SUGAR, "§7Spectre"),
    C4_SLIME(Material.SLIME_BALL, "§4C4"),
    RIDER_SADDLE(Material.SADDLE, "§6Rider"),
    PORTAL_GUN(Material.DIAMOND_BARDING, "§6Portal §1Gun"),
    WEATHERLORD_TORNADO(Material.WATCH, "§bWeather Controller"),
    QUAKE_GUN(Material.IRON_HOE, "§bQuake Gun"),
    RAIN_ARROW(Material.ARROW, "§4Rain"),
    HEALER_ITEM(Material.RED_ROSE, "§cHealer Item"),
    BLINK_STAR(Material.NETHER_STAR, "§bBlink Star"),
    TRAVELER_ARROW(Material.ARROW, "§bTraveller Item"),
    IRON_HOOK(Material.TRIPWIRE_HOOK, "§7Iron §8Hook"),
    MINER_PICKAXE(Material.STONE_PICKAXE, "§4Miner Pickaxe"),
    PYRO_FIREBALL(Material.FIREBALL, "§6Fire §4Ball"),
    FIREMAN_BUCKET(Material.WATER_BUCKET, "§bFireman Bucket"),
    LUMBERJACK_AXE(Material.WOOD_AXE, "§4Lumberjack Axe"),
    BACKPACKER(Material.ENDER_CHEST, "§5Back-Pack"),
    GRAPPLER_HOOK(Material.LEASH, "§7Grappler Hook"),
    JACK_HAMMER(Material.STONE_AXE,"§7Jack Hammer"),
    GLADIATOR_BARS(Material.IRON_FENCE, "§7Gladiator Bars");


    Material mat;
    String name;

    Weapon(Material mat, String name) {
        this.mat = mat;
        this.name = name;
    }

    private static ItemStack makeWeapon(Weapon weapon) {
        ItemStack item = new ItemStack(weapon.mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(weapon.name);
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    /*private static ItemStack makeWeapon(Weapon weapon, Enchantment enchant, int level){
        ItemStack item = new ItemStack(weapon.mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(weapon.name);
        meta.spigot().setUnbreakable(true);
        item.addEnchantment(enchant, level);
        item.setItemMeta(meta);

        return item;
    }*/

    public static void giveWeapon(Player p, Weapon weapon) {
        ItemStack item = Weapon.makeWeapon(weapon);
        if (p.getInventory().firstEmpty() == -1){
            deleteSoup(p);
        }
        p.getInventory().addItem(item);
    }

    public static void giveWeapon(Player p, Weapon weapon, int slot) {
        ItemStack item = Weapon.makeWeapon(weapon);
        if (p.getInventory().getItem(slot) != null){
            ItemStack i = p.getInventory().getItem(slot);
            deleteSoup(p,i);
        }
        p.getInventory().setItem(slot,item);
    }

    public static void giveWeapon(Player p, Weapon weapon, Enchantment enchant, int level) {
        ItemStack item = Weapon.makeWeapon(weapon);
        item.addEnchantment(enchant,level);
        if (p.getInventory().firstEmpty() == -1){
            deleteSoup(p);
        }
        p.getInventory().addItem(item);
    }

    public String getName() {
        return this.name;
    }

    private static void deleteSoup(Player p){
        boolean s = true;
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            if (p.getInventory().getItem(i) != null){
                if (p.getInventory().getItem(i).getType() == Material.MUSHROOM_SOUP && s){
                    s = false;
                    p.getInventory().getItem(i).setType(Material.AIR);
                    p.getInventory().setItem(i,new ItemStack(Material.AIR));
                }
            }
        }
    }
    private static void deleteSoup(Player p,ItemStack item){
        boolean s = true;
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            if (p.getInventory().getItem(i) != null){
                if (p.getInventory().getItem(i).getType() == Material.MUSHROOM_SOUP && s){
                    s = false;
                    p.getInventory().getItem(i).setType(Material.AIR);
                    p.getInventory().setItem(i,item);
                }
            }
        }
    }
}
