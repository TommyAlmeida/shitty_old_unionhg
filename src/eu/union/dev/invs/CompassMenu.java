package eu.union.dev.invs;

import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.utils.KitLayout;
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
 * Created by Fentis on 22/06/2016.
 */
public class CompassMenu implements Listener{

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
            inv.setItem(slot++, createSkull(p,"§cTeleport for Players!"));
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
                inv.setItem(slot++, createSkull(pa,"§a"+pa.getDisplayName()));
            }
            if (players >=45 && page ==2){
                Player pa = HGManager.getInstance().getPlayersVivos().get(i);
                inv.setItem(slot++, createSkull(pa,"§a"+pa.getDisplayName()));
            }
            players++;
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (e.getInventory().getName().equalsIgnoreCase("Teleport")) {
            if (e.getSlot() < 0) {
                return;
            }
            if (e.getSlot() >= 9){
                String ps = item.getItemMeta().getDisplayName().replace("§a", "");
                Player pl = Bukkit.getPlayer(ps);
                if (pl != null) {
                    p.teleport(pl);
                    e.getView().close();
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
