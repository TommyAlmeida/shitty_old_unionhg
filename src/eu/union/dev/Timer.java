package eu.union.dev;

import eu.union.dev.events.*;
import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Packets;
import eu.union.dev.utils.StructureCreator;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;
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

    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            this.time = (5*60)-time;
        }
    }
    private boolean fstart = false;
    private boolean end = false;
    public void start(){
        int minplayers = HGManager.getInstance().getMinPlayers();
        int timestart = 5;
        int invencibilidade = 2;
        int minifeast1 = 5;
        int dungeon = 7;
        int minifeast2 = 10;
        int minifeast3 = 15;
        int feastspawn = 20;
        int deathmatch = 50;
        int endgame = 60;
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!end){
                    time++;
                }
                int realtime = (timestart*60)-time;
                if (time <= timestart*60 && HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                    Bukkit.getPluginManager().callEvent(new HGTimerSecondsEvent(time,timeformat(realtime)));
                    for (Player p : Bukkit.getOnlinePlayers()){
                        p.setLevel(realtime);
                        //Packets.getAPI().sendActionBar(p, "§8»§a§l"+timeformat(realtime)+"§8«");
                    }
                    if (realtime == (timestart-1)*60 ||
                            realtime == (timestart-2)*60 ||
                            realtime == (timestart-3)*60 ||
                            realtime == 60 ||
                            realtime == 30 ||
                            realtime == 10 ||
                            (realtime <= 5 &&
                            realtime != 0)){
                        if (realtime >=60){
                            Bukkit.broadcastMessage(Messages.PREFIX+" §aThe game begins in "+(realtime/60)+"m!");
                        }else{
                            Bukkit.broadcastMessage(Messages.PREFIX+" §aThe game begins in "+realtime+"s!");
                        }
                        for (Player p : Bukkit.getOnlinePlayers()){
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 5.0F);
                        }
                    }
                }
                if ((time == timestart*60 || fstart) && HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                    if (HGManager.getInstance().getPlayersVivos().size() >= minplayers || fstart){
                        for (Player p : Bukkit.getOnlinePlayers()){
                            p.setLevel(0);
                            p.setExp(0);
                        }
                        Bukkit.getPluginManager().callEvent(new HGStartEvent());
                        HGManager.getInstance().setStatus(HGManager.Status.INVINCIBILITY);
                        time = 0;
                    }else{
                        time = (timestart-1)*60;
                        Bukkit.broadcastMessage("§cWe need at least §b"+minplayers+"§c players to start!");
                    }
                }
                if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY){
                    Bukkit.getPluginManager().callEvent(new HGTimerSecondsEvent(time, timeformat(time)));
                    detectWin();
                }
                if (time == invencibilidade*60 && HGManager.getInstance().getStatus() == HGManager.Status.INVINCIBILITY){//remove inven
                    HGManager.getInstance().setStatus(HGManager.Status.POS_INVINCIBILITY);
                    Bukkit.getPluginManager().callEvent(new HGEndInvencibleEvent());
                }
                if (time == dungeon*60 && HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){
                    Bukkit.getPluginManager().callEvent(new HGDungeonSpawnEvent(HGManager.getInstance().getDungeonLoc()));
                }
                if (time == minifeast1*60 && HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){
                    Bukkit.getPluginManager().callEvent(new HGMiniFeastSpawnEvent(HGManager.getInstance().getMiniFeastLoc(1),1));
                }
                if (time == minifeast2*60 && HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){
                    Bukkit.getPluginManager().callEvent(new HGMiniFeastSpawnEvent(HGManager.getInstance().getMiniFeastLoc(2),2));
                }
                if (time == minifeast3*60 && HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){
                    Bukkit.getPluginManager().callEvent(new HGMiniFeastSpawnEvent(HGManager.getInstance().getMiniFeastLoc(3),3));
                }
                if (time == (feastspawn-5)*60 && HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){
                    HGManager.getInstance().setStatus(HGManager.Status.FEAST_ANNOUNCEMENT);
                    StructureCreator scf = new StructureCreator(HGManager.getInstance().getFeastLoc(), StructureCreator.Structure.FEASTBASE);
                    scf.createStrucure();
                    Bukkit.broadcastMessage(Messages.PREFIX+" §aThe Feast will appear on " +
                            "§cX:"+HGManager.getInstance().getFeastLoc().getX() +"§a, "+
                            "§cZ:"+HGManager.getInstance().getFeastLoc().getZ() +
                            " §ain 5m!");
                }
                if ((time == (feastspawn-4)*60 || time == (feastspawn-3)*60 || time == (feastspawn-2)*60 || time == (feastspawn-1)*60) &&
                        HGManager.getInstance().getStatus() == HGManager.Status.FEAST_ANNOUNCEMENT){
                    Bukkit.broadcastMessage(Messages.PREFIX+" §aThe Feast will appear on " +
                            "§cX:"+HGManager.getInstance().getFeastLoc().getX() +"§a, "+
                            "§cZ:"+HGManager.getInstance().getFeastLoc().getZ() +
                            " §ain "+(feastspawn-(time/60))+"m!");
                }
                if (time == feastspawn*60 && HGManager.getInstance().getStatus() == HGManager.Status.FEAST_ANNOUNCEMENT){//spawn feast
                    HGManager.getInstance().setStatus(HGManager.Status.FEAST);
                    Bukkit.getPluginManager().callEvent(new HGFeastSpawnEvent(HGManager.getInstance().getFeastLoc()));
                }
                if ((time == (deathmatch-5)*60 || time == (deathmatch-1)*60) && HGManager.getInstance().getStatus() == HGManager.Status.FEAST){//anuncio de deathmatch
                    String tempo = "5 min";
                    if (time == (deathmatch-1)*60){
                        tempo = "1 min";
                    }
                    Bukkit.broadcastMessage(Messages.PREFIX+" §cDeathMatch in "+tempo);
                }
                if (time == deathmatch*60 && HGManager.getInstance().getStatus() == HGManager.Status.FEAST){//spawn da arena
                    HGManager.getInstance().setStatus(HGManager.Status.DEATH_MATCH);
                    Bukkit.getPluginManager().callEvent(new HGDeathMatchEvent(HGManager.getInstance().getDeathMatchLoc()));
                }
                if (time == endgame*60 && HGManager.getInstance().getStatus() == HGManager.Status.DEATH_MATCH){//acaba o jogo
                    HGManager.getInstance().setStatus(HGManager.Status.ENDGAME);
                    Bukkit.getPluginManager().callEvent(new HGEndEvent());
                }
            }
        }.runTaskTimer(HG.getInstance(),0,20);
    }

    public void setForceStart(boolean fstart) {
        this.fstart = fstart;
    }

    public String getTimerFormated(){
        int realtime = time;
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            realtime = (5*60)-time;
        }
        return timeformat(realtime);
    }
    public void detectWin(){
        if (HGManager.getInstance().getPlayersVivos().size() == 1 && !end){
            end = true;
            Bukkit.getPluginManager().callEvent(new HGPlayerWinEvent(HGManager.getInstance().getPlayersVivos().get(0)));
        }
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