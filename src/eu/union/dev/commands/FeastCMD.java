package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Fentis on 09/06/2016.
 */
public class FeastCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("feast")){
            if (sender instanceof Player){
                Player p = (Player)sender;
                if (HGManager.getInstance().getStatus() == HGManager.Status.FEAST_ANNOUNCEMENT ||
                        HGManager.getInstance().getStatus() == HGManager.Status.FEAST){
                    p.setCompassTarget(HGManager.getInstance().getFeastLoc());
                    p.sendMessage(Messages.PREFIX+" §aYou compass is now pointing for the feast");
                }else{
                    p.sendMessage(Messages.PREFIX+" §cThe Feast is not spawned!");
                }
            }
        }
        return false;
    }
}
