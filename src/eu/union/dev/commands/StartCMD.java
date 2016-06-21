package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.Timer;
import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Perms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Fentis on 09/06/2016.
 */
public class StartCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("start")){
            if (sender.hasPermission(Perms.START.toString())){
                if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                    if (args.length == 0){
                        if (HGManager.getInstance().getPlayersVivos().size() >= 2){
                            Timer.getInstace().setForceStart(true);
                            sender.sendMessage(Messages.PREFIX+" §cGame Started!");
                        }else{
                            sender.sendMessage(Messages.PREFIX+" §4Min 2 Players!");
                        }
                    }else{
                        int time = 0;
                        time = Integer.parseInt(args[0]);
                        if (time != 0){
                            Timer.getInstace().setTime(time);
                        }
                    }
                }
            }
        }
        return false;
    }
}
