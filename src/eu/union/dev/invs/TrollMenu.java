package eu.union.dev.invs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Created by Fentis on 30/06/2016.
 */
public class TrollMenu implements Listener{

    public void setItems(Player admin, Player hack, Inventory inv){
        for (int i = 0; i < inv.getSize(); i++) {
            inv.addItem(new ItemStack(Material.STAINED_GLASS_PANE));
        }
        ItemStack i = new ItemStack(Material.SKULL_ITEM,1,(byte)3);
        SkullMeta im = (SkullMeta) i.getItemMeta();
        im.setOwner(hack.getName());
        im.setDisplayName("§4"+hack.getName());
        i.setItemMeta(im);
        inv.setItem(4,i);
        int slot = 9;
        inv.setItem(slot++,createItem(Material.TNT,1,(byte)0,Troll.TNT.value()));
        inv.setItem(slot++,createItem(Material.BEDROCK,1,(byte)0,Troll.PRISON.value()));
        inv.setItem(slot++,createItem(Material.LAVA_BUCKET,1,(byte)0,Troll.LAVA.value()));
        inv.setItem(slot++,createItem(Material.FEATHER,1,(byte)0,Troll.LAUNCH.value()));
        inv.setItem(slot++,createItem(Material.ANVIL,1,(byte)0,Troll.ANVIL.value()));
        inv.setItem(slot++,createItem(Material.BOWL,1,(byte)0,Troll.CLEAR.value()));
        inv.setItem(slot++,createItem(Material.WEB,1,(byte)0,Troll.SLOW.value()));
        inv.setItem(slot++,createItem(Material.BEACON,1,(byte)0,Troll.ENDER.value()));
        inv.setItem(slot++,createItem(Material.STAINED_GLASS,1,(byte)15,Troll.VOID.value()));
    }
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (e.getInventory().getName().equalsIgnoreCase("Troll")) {
            if (e.getSlot() < 0) {
                return;
            }
            if (e.getSlot() >= 9 && e.getSlot() <=17){
                Troll troll = null;
                int slot = e.getSlot();
                switch (slot){
                    case 9:
                        troll = Troll.TNT;
                        break;
                    case 10:
                        troll = Troll.PRISON;
                        break;
                    case 11:
                        troll = Troll.LAVA;
                        break;
                    case 12:
                        troll = Troll.LAUNCH;
                        break;
                    case 13:
                        troll = Troll.ANVIL;
                        break;
                    case 14:
                        troll = Troll.CLEAR;
                        break;
                    case 15:
                        troll = Troll.SLOW;
                        break;
                    case 16:
                        troll = Troll.ENDER;
                        break;
                    case 17:
                        troll = Troll.VOID;
                        break;
                    default:
                        troll = null;
                        break;
                }
                for (Player p2 : Bukkit.getOnlinePlayers()){
                    if (p2.getName().equalsIgnoreCase(e.getInventory().getItem(4).getItemMeta().getDisplayName().replace("§4",""))){
                        setTroll(troll,p2);
                    }
                }
            }
        }
    }
    public void setTroll(Troll troll, Player hack){
        if (troll != null){
            switch (troll){
                case TNT:
                    hack.getWorld().createExplosion(hack.getLocation(),3.0F,false);
                    break;
                case PRISON:
                    Location loc = hack.getLocation().getBlock().getLocation().add(0,-1,0);
                    loc.getBlock().setType(Material.BEDROCK);
                    loc.clone().add(0,3,0).getBlock().setType(Material.BEDROCK);
                    loc.clone().add(0,1,1).getBlock().setType(Material.BEDROCK);
                    loc.clone().add(0,1,-1).getBlock().setType(Material.BEDROCK);
                    loc.clone().add(1,1,0).getBlock().setType(Material.BEDROCK);
                    loc.clone().add(-1,1,0).getBlock().setType(Material.BEDROCK);
                    hack.teleport(loc.clone().add(0.5,1,0.5));
                    break;
                case LAVA:
                    hack.teleport(hack.getLocation().getBlock().getLocation().add(0.5,0,0.5));
                    hack.getLocation().getBlock().setType(Material.LAVA);
                    break;
                case LAUNCH:
                    hack.setVelocity(new Vector(0,3,0));
                    break;
                case ANVIL:
                    hack.teleport(hack.getLocation().getBlock().getLocation().add(0.5,0,0.5));
                    hack.getLocation().add(0,5,0).getBlock().setType(Material.ANVIL);
                    break;
                case CLEAR:
                    hack.getInventory().setArmorContents(null);
                    hack.getInventory().clear();
                    break;
                case SLOW:
                    hack.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,10000,1,true));
                    break;
                case ENDER:
                    hack.getWorld().spawn(hack.getLocation(), EnderCrystal.class);
                    break;
                case VOID:
                    hack.teleport(new Location(hack.getWorld(),hack.getLocation().getX(),-2,hack.getLocation().getZ()));
                    break;
            }
        }
    }
    public enum Troll {
        TNT("§4TNT"),
        PRISON("§7Prison"),
        LAVA("§6Lava"),
        LAUNCH("§eLaunch"),
        ANVIL("§0Anvil"),
        CLEAR("§5Clear Items"),
        SLOW("§aSlow"),
        ENDER("§bEnder Crystal"),
        VOID("§8Void");

        String d;

        Troll(String d) {
            this.d = d;
        }

        public String value() {
            return this.d;
        }
    }
    public ItemStack createItem(Material m, int q, byte d, String name){
        ItemStack i = new ItemStack(m,q,d);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        i.setItemMeta(im);
        return i;
    }
}
