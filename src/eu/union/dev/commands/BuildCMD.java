package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Perms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Fentis on 13/06/2016.
 */
public class BuildCMD implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("build")){
            if (sender instanceof Player){
                Player p = (Player)sender;
                if (Perms.isStaff(p)){
                    if (HGManager.getInstance().inBuild(p)){
                        HGManager.getInstance().removeBuild(p);
                        p.sendMessage(Messages.PREFIX+" §cBuild off!");
                    }else{
                        HGManager.getInstance().addBuild(p);
                        p.sendMessage(Messages.PREFIX+" §aBuild on!");
                    }
                }
            }
        }
        return false;
    }
}
