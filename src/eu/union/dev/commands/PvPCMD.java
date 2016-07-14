package eu.union.dev.commands;

import eu.union.dev.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Fentis on 14/07/2016.
 */
public class PvPCMD implements Listener, CommandExecutor{

    boolean pvp = true;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pvp")){
            if (sender instanceof Player){
                Player p = (Player)sender;
                if (Perms.isStaff(p)){
                    if (pvp){
                        pvp= false;
                        Bukkit.broadcastMessage("§cPvP Disabled!");
                    }else{
                        pvp = true;
                        Bukkit.broadcastMessage("§cPvP Enabled!");
                    }
                }
            }
        }
        return false;
    }
    @EventHandler
    public void pvp(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            if (!pvp){
                e.setCancelled(true);
            }
        }
    }
}
