package eu.union.dev.commands;

import eu.union.dev.HG;
import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.Timer;
import eu.union.dev.api.Ability;
import eu.union.dev.utils.Util;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fentis on 13/06/2016.
 */
public class InfoCMD implements CommandExecutor{

    Ability cooldown = new Ability(1,10, TimeUnit.SECONDS);
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("info")){
            if (sender instanceof Player){
                Player p = (Player)sender;
                String kit = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(p).getName());
                p.sendMessage("§aStage: §c"+ Util.getInstance().getStage() +"/n"+
                        "§aTimer: §c"+ Timer.getInstace().getTimerFormated() +"/n"+
                        "§aKit: §c"+ kit +"/n"+
                        "§aPlayers: §c"+HGManager.getInstance().getPlayersVivos().size()+"/n"+
                        "§aIp: §c"+ HG.getInstance().getConfig().getString("IP")+".hg.unionnetwork.eu");
            }
        }
        return false;
    }
}
