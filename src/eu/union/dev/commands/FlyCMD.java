package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Perms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Fentis on 11/06/2016.
 */
public class FlyCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fly")){
            if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                if (sender instanceof Player){
                    Player p = (Player)sender;
                    if (Perms.isStaff(p) || Perms.isYoutuber(p)){
                        if (p.getAllowFlight()){
                            p.setAllowFlight(false);
                            p.sendMessage(Messages.PREFIX+" §cYour disabled Flight");
                        }else{
                            p.setAllowFlight(true);
                            p.sendMessage(Messages.PREFIX+" §aYour enable Flight");
                        }
                    }
                }
            }
        }
        return false;
    }
}
