package eu.union.dev.invs;

import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.listeners.Icons.SponsorMode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Created by Fentis on 30/06/2016.
 */
public class SponsorMenu implements Listener{

    public void setItems(Player p, Inventory inv, int page) {
        KitManager km = KitManager.getManager();

        inv.clear();
        int slot = 0;
        int nextpage = page + 1;
        int backpage = page - 1;
        {
            inv.setItem(slot++, new Icon(Material.CARPET, "§7Back Page").build());
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 7, "§7"));
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 7, "§7"));
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 7, "§7"));
            inv.setItem(slot++, createItem(Material.CAKE,1,(byte)0,"§bSponsor"));
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 7, "§7"));
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 7, "§7"));
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 7, "§7"));
        }
        inv.setItem(slot++, new Icon(Material.CARPET, "§aNext Page", "§5Page §6" + nextpage).build());
        inv.setItem(0, new Icon(Material.CARPET, "§7Back Page", "§5Page §6" + backpage).build());
        int players = 0;
        for (int i = 0; i < HGManager.getInstance().getPlayersVivos().size(); i++) {
            if (players <=44 && page ==1){
                Player pa = HGManager.getInstance().getPlayersVivos().get(i);
                inv.setItem(slot++, createSkull(pa,"§a"+pa.getName()));
            }
            if (players >=45 && page ==2){
                Player pa = HGManager.getInstance().getPlayersVivos().get(i);
                inv.setItem(slot++, createSkull(pa,"§a"+pa.getName()));
            }
            players++;
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().equalsIgnoreCase("Sponsor Player")) {
            if (e.getSlot() < 0) {
                return;
            }
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (e.getSlot() >= 9){
                String ps = item.getItemMeta().getDisplayName().replace("§a", "");
                Player p2 = null;
                for (Player pl : Bukkit.getOnlinePlayers()){
                    if (pl.getName().equalsIgnoreCase(ps)){
                        p2 = pl;
                    }
                }
                if (p2 != null) {
                    Inventory inv = Bukkit.createInventory(null,6*9,"Sponsor");
                    new SponsorMode().setItems(p,p2,inv);
                    p.openInventory(inv);
                    e.setCancelled(true);
                }else{
                    e.setCancelled(true);
                }
            }
            if (e.getSlot() == 0){
                String nome = item.getItemMeta().getLore().get(0);
                if (nome.contains("§5Page §6")) {
                    int page = Integer.parseInt(nome.replace("§5Page §6", ""));
                    if (page >= 1) {
                        setItems(p, e.getClickedInventory(), page);
                    }
                }
            }
            if (e.getSlot() == 8){
                String name = item.getItemMeta().getLore().get(0);
                if (name.contains("§5Page §6")) {
                    int page = Integer.parseInt(name.replace("§5Page §6", ""));
                    if (page <= 2) {
                        setItems(p, e.getClickedInventory(), page);
                    }
                }
            }
            if (e.getSlot() <= 8){
                e.setCancelled(true);
            }
        }
    }

    public ItemStack createItem(Material m, int q, byte d, String name) {
        ItemStack i = new ItemStack(m, q, d);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        i.setItemMeta(im);
        return i;
    }
    public ItemStack createSkull(Player p, String nameitem){
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta sm = (SkullMeta) item.getItemMeta();
        sm.setOwner(p.getName());
        sm.setDisplayName(nameitem);
        item.setItemMeta(sm);
        return item;
    }
}
