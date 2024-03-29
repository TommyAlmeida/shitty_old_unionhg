package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.Timer;
import eu.union.dev.api.Icon;
import eu.union.dev.utils.KitLayout;
import eu.union.dev.utils.Perms;
import eu.union.dev.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Fentis on 22/06/2016.
 */
public class AdminCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("admin")){
            if (sender instanceof Player){
                Player p = (Player)sender;
                if (Perms.isStaff(p)){
                    if (HGManager.getInstance().inAdminMode(p)){
                        //remove
                        if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY){
                            HGManager.getInstance().addSpec(p);
                            p.setGameMode(GameMode.ADVENTURE);
                            p.setAllowFlight(true);
                            p.setFlying(true);
                            p.getInventory().clear();
                            p.getInventory().setArmorContents(null);
                            Util.getInstance().buildSpecsIcons(p);
                            KitManager km = KitManager.getManager();
                            km.setPlayerKitInLobby(p,km.getKitByName("surprise"));
                        }else{
                            Util.getInstance().buildJoinIcons(p);
                            KitManager km = KitManager.getManager();
                            Icon icon = km.getPlayerKitInLobby(p).getIcon();
                            p.getInventory().setItem(1, KitLayout.getLayout().design(icon, km.getPlayerKitInLobby(p)));
                            for (Player pl : Bukkit.getOnlinePlayers()){
                                pl.showPlayer(p);
                            }
                            p.setAllowFlight(false);
                            p.setFlying(false);
                        }
                        HGManager.getInstance().removeAdminMode(p);
                    }else{
                        //add
                        if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY){
                            HGManager.getInstance().removePlayersVivos(p);
                            Timer.getInstace().detectWin();
                            KitManager km = KitManager.getManager();
                            km.setPlayerKitInLobby(p,km.getKitByName("surprise"));
                        }
                        p.getInventory().clear();
                        p.getInventory().setArmorContents(null);
                        HGManager.getInstance().addAdminMode(p);
                        Util.getInstance().buildAdminIcons(p);
                        p.setAllowFlight(true);
                        p.setFlying(true);
                        for (Player p2 : Bukkit.getOnlinePlayers()){
                            p.showPlayer(p2);
                        }
                    }
                }
            }
        }
        return false;
    }
}
