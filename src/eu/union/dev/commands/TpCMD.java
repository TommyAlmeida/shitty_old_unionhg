package eu.union.dev.commands;

import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Fentis on 24/06/2016.
 */
public class TpCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tp")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (Perms.isStaff(p)) {
                    if (args.length == 0) {
                        p.sendMessage(Messages.PREFIX + " §cUse: /tp <player> or /tp <player> <player>");
                    }
                    if (args.length == 1) {
                        Player p2 = Bukkit.getPlayer(args[0]);
                        if (p2 != null) {
                            p.teleport(p2);
                            p.sendMessage(Messages.PREFIX + " §aTeleport for " + p2.getName());
                        }
                    }
                    if (args.length == 2) {
                        Player p1 = Bukkit.getPlayer(args[0]);
                        Player p2 = Bukkit.getPlayer(args[1]);
                        if (p1 != null && p2 != null) {
                            p1.teleport(p2);
                            p.sendMessage(Messages.PREFIX + " §aTeleport " + p1.getName() + " for " + p2.getName());
                        }
                    }
                }
            }
        }
        return false;
    }
}
