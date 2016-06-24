package eu.union.dev.commands;

import eu.union.dev.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Fentis on 24/06/2016.
 */
public class InvCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true; //Retorna
        }

        Player player = (Player) sender;

        if(Perms.isStaff(player)){
            Player target = Bukkit.getPlayer(args[0]);
            if (target !=null || target.isOnline()){
                player.openInventory(target.getInventory());
            }
        }

        return true;

    }
}
