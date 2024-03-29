package eu.union.dev.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eu.union.dev.HG;
import eu.union.dev.chests.ChestItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Fentis on 08/06/2016.
 */
public class StructureCreator implements Listener{
    Location centro;
    Structure type;
    List<Block> piston = new ArrayList<>();
    List<Block> blocks = new ArrayList<>();
    public StructureCreator(Location centro, Structure type) {
        this.centro = centro;
        this.type = type;
    }
    public enum Structure {
        COLISEU("Coliseu"),
        CAKE("Cake"),
        MINIFEAST("MiniFeast"),
        DUNGEON("Dungeon"),
        FEAST("Feast"),
        FEASTBASE("FeastBase"),
        DEATHMATCH("DeathMatch");

        String d;

        Structure(String d) {
            this.d = d;
        }

        public String value() {
            return this.d;
        }
    }
    public void createStrucure(){
        String name = type.value();
        File file = new File("plugins/"+ HG.getInstance().getName()+"/Structures/", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        load(cfg, centro);
    }
    public void removeStrucure(){
        String name = type.value();
        File file = new File("plugins/"+HG.getInstance().getName()+"/Structures/", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        unload(cfg, centro);
    }
    @SuppressWarnings("deprecation")
    private void load(FileConfiguration cfg, Location loccenter){
        List<String> blocks = cfg.getStringList("Blocks");
        for (int i = 0; i < blocks.size(); i++) {
            String[] s1 = blocks.get(i).split(",");
            //getValor(s1,":", 2) == Loc:1;2;3
            //getValor(getv[1],";", 2)[0] == Loc:1;2;3
            Material m = Material.getMaterial(getValor(s1, 0));
            byte d = Byte.parseByte(getValor(s1, 1));
            int x = Integer.parseInt(getValor(getValor(s1,":", 2),";", 1)[0]);
            int y = Integer.parseInt(getValor(getValor(s1,":", 2),";", 1)[1]);
            int z = Integer.parseInt(getValor(getValor(s1,":", 2),";", 1)[2]);
            //Location loc = new Location(loccenter.getWorld(), x, y, z);
            if (type == Structure.COLISEU) {
                Location loc = loccenter.getBlock().getLocation().add(x, y, z);
                if (!loc.getChunk().isLoaded()) {
                    loc.getChunk().load();
                }
            }
            loccenter.getBlock().getLocation().add(x, y, z).getBlock().setType(m);
            loccenter.getBlock().getLocation().add(x, y, z).getBlock().setData(d);
            if (m == Material.SIGN_POST ||
                    m == Material.WALL_SIGN){
                String l1 = getValor(getValor(s1,":", 3),";", 1)[0];
                String l2 = getValor(getValor(s1,":", 3),";", 1)[1];
                String l3 = getValor(getValor(s1,":", 3),";", 1)[2];
                String l4 = getValor(getValor(s1,":", 3),";", 1)[3];
                Sign si = (Sign)loccenter.getBlock().getLocation().add(x, y, z).getBlock().getState();
                si.setLine(0, l1.replaceAll("&", "§").replaceAll("¨", ""));
                si.setLine(1, l2.replaceAll("&", "§").replaceAll("¨", ""));
                si.setLine(2, l3.replaceAll("&", "§").replaceAll("¨", ""));
                si.setLine(3, l4.replaceAll("&", "§").replaceAll("¨", ""));
                si.update();
            }
            if (type == Structure.FEAST){
                if (m == Material.CHEST){
                    Chest chest = (Chest)loccenter.getBlock().getLocation().add(x, y, z).getBlock().getState();
                    ChestItems items = new ChestItems(ChestItems.ChestType.FEAST);
                    items.fillChest(chest.getInventory());
                }
            }
            if (type == Structure.MINIFEAST){
                if (m == Material.CHEST){
                    Chest chest = (Chest)loccenter.getBlock().getLocation().add(x, y, z).getBlock().getState();
                    ChestItems items = new ChestItems(ChestItems.ChestType.MINIFEAST);
                    items.fillChest(chest.getInventory());
                }
            }
            if (type == Structure.COLISEU){
                if (m == Material.PISTON_BASE){
                    piston.add(loccenter.getBlock().getLocation().add(x, y, z).getBlock());
                }
                if (m == Material.ENDER_CHEST || m == Material.GOLD_BLOCK){
                    this.blocks.add(loccenter.getBlock().getLocation().add(x,y,z).getBlock());
                }
            }
            if (type == Structure.DUNGEON){
                Block b = loccenter.getBlock().getLocation().add(x, y, z).getBlock();
                if (m == Material.BEDROCK){
                    Zombie a = (Zombie)b.getWorld().spawnEntity(b.getLocation().add(0.5,1,0.5), EntityType.ZOMBIE);
                    a.setCustomName("§a§lGuardian");
                    a.setCustomNameVisible(true);
                    a.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                    a.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                    a.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                    a.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                    a.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                    locg = b.getLocation().add(0.5,1,0.5);
                }
                if (m == Material.MOB_SPAWNER){
                    addMobspawner(b);
                }
            }
        }
    }
    Location locg = null;
    public void addChestDungeon(){
        if (locg != null){
            locg.getBlock().setType(Material.CHEST);
            Chest chest = (Chest)locg.getBlock().getState();
            ChestItems items = new ChestItems(ChestItems.ChestType.FEAST);
            items.fillChest(chest.getInventory());
        }
    }
    public void addMobspawner(Block b){
        List<EntityType> list = new ArrayList<>();
        list.add(EntityType.BLAZE);
        list.add(EntityType.ZOMBIE);
        list.add(EntityType.MAGMA_CUBE);
        list.add(EntityType.SKELETON);
        list.add(EntityType.WITCH);
        list.add(EntityType.CREEPER);
        list.add(EntityType.SPIDER);
        list.add(EntityType.CAVE_SPIDER);
        list.add(EntityType.ENDERMAN);
        list.add(EntityType.SILVERFISH);
        CreatureSpawner s = (CreatureSpawner) b.getState();
        s.setSpawnedType(list.get(new Random().nextInt(list.size())));
    }

    public void removePistons(){
        if (type == Structure.COLISEU){
            for (Block b : piston){
                b.setType(Material.AIR);
            }
        }
    }
    @SuppressWarnings({ "deprecation" })
    private void unload(FileConfiguration cfg, Location loccenter){
        List<String> blocks = cfg.getStringList("Blocks");
        for (int i = 0; i < blocks.size(); i++) {
            String[] s1 = blocks.get(i).split(",");
            Material m = Material.AIR;
            byte d = (byte)0;
            int x = Integer.parseInt(getValor(getValor(s1,":", 2),";", 1)[0]);
            int y = Integer.parseInt(getValor(getValor(s1,":", 2),";", 1)[1]);
            int z = Integer.parseInt(getValor(getValor(s1,":", 2),";", 1)[2]);
            loccenter.getBlock().getLocation().add(x, y, z).getBlock().setType(m);
            loccenter.getBlock().getLocation().add(x, y, z).getBlock().setData(d);
        }
    }
    public String[] getValor(String[] s1, String simbolo, int n){
        String[] s2 = s1[n].split(simbolo);
        return s2;
    }
    private String getValor(String[] s1, int n){
        String[] s2 = s1[n].split(":");
        return s2[1];
    }
}