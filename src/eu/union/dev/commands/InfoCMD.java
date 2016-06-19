package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.Timer;
import eu.union.dev.api.Ability;
import eu.union.dev.utils.Holograms;
import eu.union.dev.utils.Util;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
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
                if (cooldown.tryUse(p)){
                    Location loc = p.getTargetBlock((HashSet<Byte>)null,3).getLocation().add(0.5,0,0.5);
                    loc.setY(p.getLocation().getY()+0.3);
                    Holograms holo = new Holograms(loc,0.0,"§aStage: §c"+ Util.getInstance().getStage() ,
                            "§aTimer: §c"+ Timer.getInstace().getTimerFormated() ,
                            "§aKit: §c"+ kit ,
                            "§aPlayers: §c"+HGManager.getInstance().getPlayersVivos().size());
                    holo.spawn(8*20);
                }
            }
        }
        return false;
    }
}
