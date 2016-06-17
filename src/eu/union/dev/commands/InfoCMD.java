package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.Timer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Fentis on 13/06/2016.
 */
public class InfoCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("info")){
            String stage = "";
            if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                stage = "Waiting for Players";
            }
            if (HGManager.getInstance().getStatus() == HGManager.Status.INVINCIBILITY){
                stage = "In Invincibility";
            }
            if (HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){
                stage = "In Battle";
            }
            if (HGManager.getInstance().getStatus() == HGManager.Status.FEAST_ANNOUNCEMENT){
                stage = "In Feast Announce";
            }
            if (HGManager.getInstance().getStatus() == HGManager.Status.FEAST){
                stage = "In Feast";
            }
            if (HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
                stage = "In End";
            }
            if (sender instanceof Player){
                Player p = (Player)sender;
                String kit = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(p).getName());
                sender.sendMessage("§aStage: §c"+ stage +"\n§a" +
                        "Timer: §c"+ Timer.getInstace().getTimerFormated()+"\n§a" +
                        "Kit: §c"+ kit+"\n§a" +
                        "Players: §c"+HGManager.getInstance().getPlayersVivos().size());
            }
        }
        return false;
    }
}
