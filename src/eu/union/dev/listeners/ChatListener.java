package eu.union.dev.listeners;

import eu.union.dev.HGManager;
import eu.union.dev.utils.Perms;
import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.Group;
import net.alpenblock.bungeeperms.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Fentis on 21/06/2016.
 */
public class ChatListener implements Listener{

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        User user = BungeePerms.getInstance().getPermissionsManager().getUser(e.getPlayer().getName());
        Group group = BungeePerms.getInstance().getPermissionsManager().getMainGroup(user);
        String prefix = ChatColor.translateAlternateColorCodes('&',group.getPrefix())+"§7";
        e.setCancelled(true);
        if (HGManager.getInstance().isSpec(e.getPlayer())){
            for (Player p : Bukkit.getOnlinePlayers()){
                if (HGManager.getInstance().isSpec(p) ||
                        Perms.isStaff(p)){
                    p.sendMessage("§7" + prefix + "»(Spec)" + e.getPlayer().getName() + ": §f" + ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                }
            }
        }else{
            Bukkit.broadcastMessage("§7" + prefix + "»" + e.getPlayer().getName() + ": §f" + ChatColor.translateAlternateColorCodes('&',e.getMessage()));
        }
    }
}
