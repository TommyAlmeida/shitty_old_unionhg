package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

/**
 * Created by Fentis on 16/06/2016.
 */
public class ScoreCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("score")){
            if (sender instanceof Player){
                Player p = (Player)sender;
                if (HGManager.getInstance().getNoScore().contains(p)){
                    HGManager.getInstance().removeNoScore(p);
                    Util.getInstance().buildScoreboard(p);
                }else{
                    HGManager.getInstance().addNoScore(p);
                    p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                }
            }
        }
        return false;
    }
}
