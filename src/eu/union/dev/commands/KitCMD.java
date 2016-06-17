package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.invs.KitMenu;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.KitLayout;
import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by Fentis on 09/06/2016.
 */
public class KitCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kit")){
            if (sender instanceof Player){
                Player p = (Player)sender;
                if (args.length == 0){
                    if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                        Inventory inv = Bukkit.createInventory(null,6*9,"Kits");
                        new KitMenu().setItems(p,inv,"player",1);
                        p.openInventory(inv);
                    }
                }else{
                    KitManager km = KitManager.getManager();
                    String kitname = args[0];
                    if (km.getKitByName(args[0]) != null){
                        Kit kit = km.getKitByName(args[0]);
                        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                            km.setPlayerKitInLobby(p,kit);
                            Icon icon = km.getPlayerKitInLobby(p).getIcon();
                            p.getInventory().setItem(8, KitLayout.getLayout().design(icon, km.getPlayerKitInLobby(p)));
                            p.sendMessage(Messages.PREFIX+" §aYou selected kit §a"+kit.getName());
                        }
                    }
                }
            }
        }
        return false;
    }
}
