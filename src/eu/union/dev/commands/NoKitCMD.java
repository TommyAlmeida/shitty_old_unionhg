package eu.union.dev.commands;

import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Fentis on 13/07/2016.
 */
public class NoKitCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player)sender;
            if (Perms.isStaff(p)){
                if (args.length == 0){
                    p.sendMessage(Messages.PREFIX+" §aUse /nokit <KitName>");
                }else{
                    KitManager km = KitManager.getManager();
                    if (km.getKitByName(args[0]) != null){
                        Kit kit = km.getKitByName(args[0]);
                        if (km.isKitDisable(kit)){
                            km.removeKitDisable(kit);
                            Bukkit.broadcastMessage(Messages.PREFIX+" §aKit:"+kit.getName()+" is enabled!");
                        }else{
                            km.addKitDisable(kit);
                            if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                                Bukkit.broadcastMessage(Messages.PREFIX+" §cKit:"+kit.getName()+" is disabled! You kit is now Surprise!");
                                for (Player p2 : Bukkit.getOnlinePlayers()){
                                    if (km.getPlayerKitInLobby(p).getName().equalsIgnoreCase(kit.getName())){
                                        km.setPlayerKitInLobby(p,km.getKitByName("Surprise"));
                                    }
                                }
                            }else{
                                Bukkit.broadcastMessage(Messages.PREFIX+" §cKit:"+kit.getName()+" is disabled! You kit is now Random!");
                                for (Player p2 : Bukkit.getOnlinePlayers()){
                                    if (km.getPlayerKitInLobby(p).getName().equalsIgnoreCase(kit.getName())){
                                        km.setPlayerKitInLobby(p,km.getKitByName("Surprise"));
                                        List<Kit> kits = new ArrayList<>();
                                        for (Kit kitss : km.getKits()){
                                            if (!kit.getName().equalsIgnoreCase("Surprise")){
                                                if (!km.isKitDisable(kitss)){
                                                    kits.add(kitss);
                                                }
                                            }
                                        }
                                        Kit newkit = kits.get(new Random().nextInt(kits.size()));
                                        km.setPlayerKitInLobby(p,kit);
                                        km.applyKit(p,newkit);
                                        p.sendMessage("§aYou kit surprise is §c"+kit.getName());
                                    }
                                }
                            }
                        }
                    }else{
                        p.sendMessage(Messages.PREFIX+" §cThis kit not exist!");
                    }
                }
            }
        }
        return false;
    }
}
