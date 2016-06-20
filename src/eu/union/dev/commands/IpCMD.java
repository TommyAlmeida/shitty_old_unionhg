package eu.union.dev.commands;

import eu.union.dev.HG;
import eu.union.dev.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Fentis on 20/06/2016.
 */
public class IpCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ip")){
            sender.sendMessage(Messages.PREFIX.toString()+" §7"+HG.getInstance().getConfig().getString("IP").toLowerCase()+".hg.unionnetwork.eu");
        }
        return false;
    }
}
