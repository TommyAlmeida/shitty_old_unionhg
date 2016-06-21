package eu.union.dev.commands;

import eu.union.dev.utils.Perms;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Fentis on 21/06/2016.
 */
public class GmCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gamemode")){
            if (sender instanceof Player){
                Player p = (Player)sender;
                if (Perms.isStaff(p)){
                    if (args.length == 0){
                        if (p.getGameMode() != GameMode.CREATIVE){
                            p.setGameMode(GameMode.CREATIVE);
                        }else{
                            p.setGameMode(GameMode.SURVIVAL);
                        }
                    }else{
                        if (args[0].equalsIgnoreCase("0")){
                            p.setGameMode(GameMode.SURVIVAL);
                        }
                        if (args[0].equalsIgnoreCase("1")){
                            p.setGameMode(GameMode.CREATIVE);
                        }
                        if (args[0].equalsIgnoreCase("2")){
                            p.setGameMode(GameMode.ADVENTURE);
                        }
                    }
                }
            }
        }
        return false;
    }
}
