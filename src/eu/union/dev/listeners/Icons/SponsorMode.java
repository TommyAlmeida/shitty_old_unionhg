package eu.union.dev.listeners.Icons;

import eu.union.dev.HGManager;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.invs.SponsorMenu;
import eu.union.dev.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tomas on 29-06-2016.
 */
public class SponsorMode implements Listener {

    private static SponsorMode instance = new SponsorMode();

    public static SponsorMode getInstance() {
        return instance;
    }

    private Random rand = new Random();
    private int stack = rand.nextInt(16);

    private ArrayList<Player> alreadyGive = new ArrayList<>();

    Icon portableBench = new Icon(Material.WORKBENCH, "§6Portable Workbench");

    private ItemStack[] items =
            {
                    new ItemStack(Material.APPLE, stack),
                    new ItemStack(Material.GOLDEN_APPLE, 2),
                    new ItemStack(Material.IRON_SWORD, 1),
                    new ItemStack(Material.LEATHER_CHESTPLATE, 1),
                    new ItemStack(Material.MUSHROOM_SOUP, stack),
                    new ItemStack(Material.BOWL, stack),
                    new ItemStack(Material.BROWN_MUSHROOM, stack),
                    new ItemStack(Material.RED_MUSHROOM, stack),
                    portableBench.build()
            };

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();

        if (!HGManager.getInstance().isSpec(p)){
            return;
        }
        if(item == null || item.getType() == Material.AIR){
            return;
        }

        if(!(item.getType() == Material.CAKE)){
            return;
        }

        if(item.getItemMeta() == null){
            return;
        }

        if(!item.getItemMeta().hasDisplayName()){
            return;
        }

        if(!(item.getItemMeta().getDisplayName() == "§eSponsor")){
            return;
        }
        Inventory inv = Bukkit.createInventory(null, 6*9, "Sponsor Player");
        new SponsorMenu().setItems(p,inv,1);
        p.openInventory(inv);
    }

    public void setItems(Player p, Player p2,Inventory inv){
        for(int i = 0; i < inv.getSize(); i++){
            if (i<=8){
                inv.setItem(i,new ItemStack(Material.STAINED_GLASS_PANE));
            }else{
                if (i-8 <= 9){
                    inv.addItem(items[i-8]);
                }
            }
        }
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta sm = (SkullMeta) item.getItemMeta();
        sm.setOwner(p2.getName());
        sm.setDisplayName("§6"+p2.getName());
        item.setItemMeta(sm);
        inv.setItem(4,item);
    }
    Ability cooldown = new Ability(1,5, TimeUnit.MINUTES);
    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if(e.getClickedInventory().getName() == "Sponsor"){
            if(e.getSlot() < 0){
                return;
            }
            e.setCancelled(true);
            if (e.getSlot() >=9){
                if (cooldown.tryUse(p)){
                    Player p2 = null;
                    for (Player ps : Bukkit.getOnlinePlayers()){
                        if (ps.getName().equalsIgnoreCase(e.getInventory().getItem(4).getItemMeta().getDisplayName().replace("§6",""))){
                            p2 = ps;
                        }
                    }
                    switch (e.getSlot()){
                        case 9:
                            addItem(p2,items[0]);
                            break;
                        case 10:
                            addItem(p2,items[1]);
                            break;
                        case 11:
                            addItem(p2,items[2]);
                            break;
                        case 12:
                            addItem(p2,items[3]);
                            break;
                        case 13:
                            addItem(p2,items[4]);
                            break;
                        case 14:
                            addItem(p2,items[5]);
                            break;
                        case 15:
                            addItem(p2,items[6]);
                            break;
                        case 16:
                            addItem(p2,items[7]);
                            break;
                        case 17:
                            addItem(p2,items[8]);
                            break;
                        case 18:
                            addItem(p2,items[9]);
                            break;
                        case 19:
                            addItem(p2,items[10]);
                            break;
                    }
                }else{
                    Util.getInstance().sendCooldownMessage(p, cooldown, TimeUnit.SECONDS, true);
                }
            }
            //TODO: adicionar os clicks para dar os items
        }
    }
    public void addItem(Player p, ItemStack item){
        if (p.getInventory().firstEmpty() == -1){
            p.getWorld().dropItemNaturally(p.getLocation(),item);
        }else{
            p.getInventory().addItem(item);
        }
    }
    @EventHandler
    public void onGadget(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();

        if(item == null || item.getType() == Material.AIR){
            return;
        }

        if(item.getItemMeta() == null){
            return;
        }

        if(!item.getItemMeta().hasDisplayName()){
            return;
        }

        if(!(item.getItemMeta().getDisplayName() == "§6Portable Workbench")){
            return;
        }

        p.openWorkbench(null, true);
    }
}
