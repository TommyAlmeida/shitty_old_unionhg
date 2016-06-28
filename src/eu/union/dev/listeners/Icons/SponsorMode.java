package eu.union.dev.listeners.Icons;

import eu.union.dev.api.Icon;
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

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tomas on 29-06-2016.
 */
public class SponsorMode implements Listener {

    private Random rand = new Random();
    private int stack = rand.nextInt(16);

    private Inventory inv = Bukkit.createInventory(null, 9*4, "Sponsor");
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

        setItems(p);
        p.openInventory(inv);
    }

    private void setItems(Player p){
        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, items[i]);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if(e.getClickedInventory().getName() == "Sponsor"){
            if(e.getSlot() < 0){
                return;
            }

            //TODO: adicionar os clicks para dar os items
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
