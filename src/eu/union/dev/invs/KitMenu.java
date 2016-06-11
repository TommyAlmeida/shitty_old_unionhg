package eu.union.dev.invs;

import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.KitLayout;
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
 * Created by Fentis on 09/06/2016.
 */
public class KitMenu implements Listener{

    public void setItems(Player p, Inventory inv, String type, int page) {
        KitManager km = KitManager.getManager();

        inv.clear();
        int slot = 0;
        int nextpage = page + 1;
        int backpage = page - 1;
        {
            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta sm = (SkullMeta) item.getItemMeta();
            sm.setOwner(p.getName());
            sm.setDisplayName("§bYour Kits");
            item.setItemMeta(sm);
            inv.setItem(slot++, new Icon(Material.CARPET, "§7Back Page").build());
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 5, "§7"));
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 5, "§7"));
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 5, "§7"));
            inv.setItem(slot++, item);
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 5, "§7"));
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 5, "§7"));
            inv.setItem(slot++, createItem(Material.STAINED_GLASS_PANE, 1, (byte) 5, "§7"));
        }
        if (type.contains("player")) {
            inv.setItem(slot++, new Icon(Material.CARPET, "§aNext Page", "§5Page §6" + nextpage).build());
            inv.setItem(0, new Icon(Material.CARPET, "§7Back Page", "§5Page §6" + backpage).build());
            int kits = 0;
            for (int i = 0; i < km.getKits().size(); i++) {
                if (p.hasPermission(km.getKits().get(i).getPermission())) {
                    if (kits <= 44 && page == 1) {
                        Icon icon = km.getKits().get(i).getIcon();
                        inv.setItem(slot++, KitLayout.getLayout().design(icon, km.getKits().get(i)));
                    }
                    if (kits >= 45 && page == 2) {
                        Icon icon = km.getKits().get(i).getIcon();
                        inv.setItem(slot++, KitLayout.getLayout().design(icon, km.getKits().get(i)));
                    }
                    kits++;
                }
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (e.getInventory().getName().equalsIgnoreCase("Kits")) {
            if (e.getSlot() < 0) {
                return;
            }

            String kit = item.getItemMeta().getDisplayName().replace("§7Kit » ", "").replace("§7", "").replace("§b", "").replace("§d", "").replace("§6", "").replace("§5", "");

            if (KitManager.getManager().getKitByName(kit) != null) {
                offerKit(p, kit);
                e.getView().close();
                e.setCancelled(true);
            }else{
                try {
                    throw new Exception();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (e.getSlot() == 0){
                String nome = item.getItemMeta().getLore().get(0);
                if (nome.contains("§5Page §6")) {
                    int page = Integer.parseInt(nome.replace("§5Page §6", ""));
                    if (page >= 1) {
                        setItems(p, e.getClickedInventory(), "player", page);
                    }
                }
                e.setCancelled(true);
            }
            if (e.getSlot() == 8){
                String name = item.getItemMeta().getLore().get(0);
                if (name.contains("§5Page §6")) {
                    int page = Integer.parseInt(name.replace("§5Page §6", ""));
                    if (page <= 2) {
                        setItems(p, e.getClickedInventory(), "player", page);
                    }
                }
            }
        }
    }

    public void offerKit(Player p, String kit) {
        p.chat("/kit %kit".replace("%kit", kit.trim()));
    }

    public ItemStack createItem(Material m, int q, byte d, String name) {
        ItemStack i = new ItemStack(m, q, d);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        i.setItemMeta(im);
        return i;
    }
}
