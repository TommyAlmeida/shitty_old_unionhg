package eu.union.dev;

import eu.union.dev.events.*;
import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Packets;
import eu.union.dev.utils.StructureCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Fentis on 06/06/2016.
 */
public class Timer {

    private static Timer instace = new Timer();

    public static Timer getInstace() {
        return instace;
    }
    private int time = 0;
    public void start(){
        new BukkitRunnable(){
            @Override
            public void run() {
                time++;
                int realtime = (5*60)-time;
                if (time <= 5*60 && HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                    Bukkit.getPluginManager().callEvent(new HGTimerSecondsEvent(time,timeformat(realtime)));
                    for (Player p : Bukkit.getOnlinePlayers()){
                        p.setLevel(realtime);
                        Packets.getAPI().sendActionBar(p, "§8»§a§l"+timeformat(realtime)+"§8«");
                    }
                    if (realtime == 4*60 ||
                            realtime == 3*60 ||
                            realtime == 2*60 ||
                            realtime == 60 ||
                            realtime == 30 ||
                            realtime <= 10){
                        if (realtime >=60){
                            Bukkit.broadcastMessage(Messages.PREFIX+" §aThe game begins in "+(realtime/60)+"m!");
                        }else{
                            Bukkit.broadcastMessage(Messages.PREFIX+" §aThe game begins in "+realtime+"s!");
                        }
                    }
                }
                if (time == 5*60 && HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                    if (Bukkit.getOnlinePlayers().size() >= 1){
                        for (Player p : Bukkit.getOnlinePlayers()){
                            p.setLevel(0);
                            p.setExp(0);
                        }
                        Bukkit.getPluginManager().callEvent(new HGStartEvent());
                        HGManager.getInstance().setStatus(HGManager.Status.INVENCIBILITY);
                        time = 0;
                    }else{
                        time = 4*60;
                        Bukkit.broadcastMessage("§cWe need at least §b2§c players to start!");
                    }
                }
                if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY){
                    Bukkit.getPluginManager().callEvent(new HGTimerSecondsEvent(time, timeformat(time)));
                    for (Player p : Bukkit.getOnlinePlayers()){
                        Packets.getAPI().sendActionBar(p, "§8»§c§l"+timeformat(time)+"§8«");
                    }
                }
                if (time == 2*60 && HGManager.getInstance().getStatus() == HGManager.Status.INVENCIBILITY){//remove inven
                    HGManager.getInstance().setStatus(HGManager.Status.POSINVINCIBILITY);
                    Bukkit.getPluginManager().callEvent(new HGEndInvencibleEvent());
                }
                if (time == 15*60 && HGManager.getInstance().getStatus() == HGManager.Status.POSINVINCIBILITY){
                    StructureCreator scf = new StructureCreator(HGManager.getInstance().getFeastLoc(), StructureCreator.Structure.FEASTBASE);
                    scf.createStrucure();
                    Bukkit.broadcastMessage(Messages.PREFIX+" §aThe Feast will appear on " +
                            "§cX:"+HGManager.getInstance().getFeastLoc().getX() +"§a, "+
                            "§cZ:"+HGManager.getInstance().getFeastLoc().getZ() +
                            " §ain 5m!");
                }
                if (time == 20*60 && HGManager.getInstance().getStatus() == HGManager.Status.POSINVINCIBILITY){//spawn feast
                    HGManager.getInstance().setStatus(HGManager.Status.FEAST);
                    Bukkit.getPluginManager().callEvent(new HGFeastSpawnEvent(HGManager.getInstance().getFeastLoc()));//Substituir depois
                }
                if (time == 60*60 && HGManager.getInstance().getStatus() == HGManager.Status.FEAST){//acaba o jogo
                    HGManager.getInstance().setStatus(HGManager.Status.ENDGAME);
                    Bukkit.getPluginManager().callEvent(new HGEndEvent());
                }
            }
        }.runTaskTimer(HG.getInstance(),0,20);
    }
    private String timeformat(int tempo)
    {
        int ss = tempo % 60;
        tempo /= 60;
        int min = tempo % 60;
        int hh = tempo % 24;
        if ((ss > 0) && (min == 0) && (hh == 0)) {
            return "00:" + strzero(ss);
        }
        if ((min > 0) && (hh == 0)) {
            return strzero(min) + ":" + strzero(ss);
        }
        return strzero(min) + ":" + strzero(ss);
    }

    private String strzero(int n)
    {
        if (n < 10)
            return "0" + String.valueOf(n);
        return String.valueOf(n);
    }

}