package eu.union.dev.listeners;

import eu.union.dev.HGManager;
import eu.union.dev.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Created by Fentis on 21/06/2016.
 */
public class ChatListener implements Listener{

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String prefix = PermissionsEx.getUser(e.getPlayer()).getGroups()[0].getPrefix();
        e.setCancelled(true);
        if (HGManager.getInstance().isSpec(e.getPlayer())){
            for (Player p : Bukkit.getOnlinePlayers()){
                if (HGManager.getInstance().isSpec(e.getPlayer()) ||
                        Perms.isStaff(p)){
                    p.sendMessage("§7" + prefix + "»(Spec)" + e.getPlayer().getName() + ": §f" + ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                }
            }
        }else{
            Bukkit.broadcastMessage("§7" + prefix + "»" + e.getPlayer().getName() + ": §f" + ChatColor.translateAlternateColorCodes('&',e.getMessage()));
        }
    }
}
