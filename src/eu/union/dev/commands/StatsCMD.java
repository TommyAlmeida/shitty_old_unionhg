package eu.union.dev.commands;

import eu.union.dev.PlayerManager;
import eu.union.dev.storage.KPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Fentis on 24/06/2016.
 */
public class StatsCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("stats")){
            if (sender instanceof Player){
                Player p = (Player)sender;
                if (args.length == 0){
                    KPlayer kp = PlayerManager.getPlayer(p.getUniqueId());
                    if (kp != null){
                        p.sendMessage("§7§m-------------------------------");
                        p.sendMessage("§9Coins: §e%s".replace("%s", String.valueOf(kp.getCoins())));
                        p.sendMessage("§9Wins: §e%s".replace("%s", String.valueOf(kp.getWins())));
                        p.sendMessage("§9Losers: §e%s".replace("%s", String.valueOf(kp.getLoses())));
                        p.sendMessage("§9Kills: §e%s".replace("%s", String.valueOf(kp.getKills())));
                        p.sendMessage("§9Deaths: §e%s".replace("%s", String.valueOf(kp.getDeaths())));
                        p.sendMessage("§9KDR: §e%s".replace("%s", String.valueOf(kp.getKDR())));
                        p.sendMessage("§7§m-------------------------------");
                    }
                }else{
                    Player bp = Bukkit.getPlayer(args[0]);
                    if (bp!=null){
                        KPlayer kp = PlayerManager.getPlayer(bp.getUniqueId());
                        if (kp != null){
                            p.sendMessage("§7§m-------------------------------");
                            p.sendMessage("§9Coins: §e%s".replace("%s", String.valueOf(kp.getCoins())));
                            p.sendMessage("§9Wins: §e%s".replace("%s", String.valueOf(kp.getWins())));
                            p.sendMessage("§9Losers: §e%s".replace("%s", String.valueOf(kp.getLoses())));
                            p.sendMessage("§9Kills: §e%s".replace("%s", String.valueOf(kp.getKills())));
                            p.sendMessage("§9Deaths: §e%s".replace("%s", String.valueOf(kp.getDeaths())));
                            p.sendMessage("§9KDR: §e%s".replace("%s", String.valueOf(kp.getKDR())));
                            p.sendMessage("§7§m-------------------------------");
                        }
                    }
                }
            }
        }
        return false;
    }
}
