package eu.union.dev;

import eu.union.dev.events.*;
import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Perms;
import eu.union.dev.utils.StructureCreator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Created by Fentis on 06/06/2016.
 */
public class HGManager implements Listener{

    private static HGManager instance = new HGManager();
    public static HGManager getInstance() {
        return instance;
    }
    private Status status;
    Location feast,minifeast,coliseu = null;
    int bordsize;
    public enum Status {
        LOBBY("Lobby"),
        INVENCIBILITY("Invencibility"),
        POSINVINCIBILITY("PosInvencibility"),
        FEAST("Feast"),
        ENDGAME("EndGame");

        String d;

        Status(String d) {
            this.d = d;
        }

        public String value() {
            return this.d;
        }
    }
    public Status getStatus() {
        return status;
    }

    public int getBordSize() {
        return bordsize;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void setup(){
        bordsize = 480;
        coliseu = new Location(Bukkit.getWorlds().get(0),0,150,0);
        feast = RandomLocation(100);
        minifeast = RandomLocation(300);
        StructureCreator scc = new StructureCreator(coliseu, StructureCreator.Structure.COLISEU);
        scc.createStrucure();
    }
    public static Location RandomLocation(int raio)
    {
        Random random = new Random();
        Location startFrom = new Location(Bukkit.getWorlds().get(0),0,0,0);

        Location loc = startFrom.clone();
        loc.add((random.nextBoolean() ? 1 : -1) * random.nextInt(raio), raio,
                (random.nextBoolean() ? 1 : -1) * random.nextInt(raio));
        int newY = Bukkit.getWorlds().get(0).getHighestBlockYAt(loc);
        loc.setY(newY);
        return loc;
    }

    public Location getFeastLoc() {
        return feast;
    }

    public Location getColiseuLoc() {
        return coliseu;
    }

    public Location getMiniFeastLoc() {
        return minifeast;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        //Give de items
    }
    @EventHandler
    public void onStart(HGStartEvent e){
        //Give kit e remove items
        Bukkit.broadcastMessage(Messages.PREFIX+" §bThe game started! And may the odds be ever in your favor!");
    }
    @EventHandler
    public void onEndInv(HGEndInvencibleEvent e){
        StructureCreator scc = new StructureCreator(HGManager.getInstance().coliseu, StructureCreator.Structure.COLISEU);
        scc.removeStrucure();
        Bukkit.broadcastMessage(Messages.PREFIX+" §cYou are not more invincible!!");
    }
    @EventHandler
    public void onFeastSpawn(HGFeastSpawnEvent e){
        StructureCreator scf = new StructureCreator(e.getLocation(), StructureCreator.Structure.FEAST);
        scf.createStrucure();
        Bukkit.broadcastMessage(Messages.PREFIX+" §4The Feast appeared in §cX:"+e.getLocation().getX()+"§a, §cZ:"+e.getLocation().getZ()+"! Run or you'll lose it!");
    }
    @EventHandler
    public void onPlayerWin(HGPlayerWinEvent e){
        Player p = e.getWinner();
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setItemInHand(new ItemStack(Material.WATER_BUCKET));
        Location loc = p.getLocation();
        loc.setY(125);
        StructureCreator scf = new StructureCreator(loc, StructureCreator.Structure.CAKE);
        scf.createStrucure();
        loc.setY(130);
        p.teleport(loc);
        Bukkit.broadcastMessage(Messages.PREFIX+" §aThe "+p.getDisplayName()+" won the game! Good game!");
    }
    @EventHandler
    public void onEnd(HGEndEvent e){
        Bukkit.broadcastMessage(Messages.PREFIX+" §cNo winners! Opening next match!");
        for (Player p : Bukkit.getOnlinePlayers()){
            p.kickPlayer(Messages.PREFIX+" §cNo winners! Opening next match!");
        }
        Bukkit.getScheduler().scheduleAsyncDelayedTask(HG.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.shutdown();
            }
        },20);
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (getStatus() == Status.INVENCIBILITY || getStatus() == Status.LOBBY || getStatus() == Status.ENDGAME){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBreack(BlockBreakEvent e){
        if (e.getBlock().getY() >=145){
            e.setCancelled(true);
        }
        Location loc = e.getBlock().getLocation();
        Location loc2 = new Location(e.getBlock().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= bordsize) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= bordsize)))
        {
            e.setCancelled(true);
            if (e.getPlayer() != null){
                e.getPlayer().sendMessage("§4This very close to the Bord!");
            }
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if (e.getBlock().getY() >=145){
            e.setCancelled(true);
        }
        Location loc = e.getBlock().getLocation();
        Location loc2 = new Location(e.getBlock().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= bordsize) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= bordsize)))
        {
            e.setCancelled(true);
            if (e.getPlayer() != null){
                e.getPlayer().sendMessage("§4This very close to the Bord!");
            }
        }
    }
    @EventHandler
    public void onSeconds(HGTimerSecondsEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            Location loc = p.getLocation();
            Location loc2 = new Location(p.getWorld(), 0, 120, 0);
            if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= bordsize) ||
                    (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= bordsize)))
            {
                if ((p.getGameMode() == GameMode.SPECTATOR ||
                        p.getGameMode() == GameMode.CREATIVE) &&
                                !Perms.isStaff(p)){
                    p.sendMessage("§cSorry! Spectators don't leave the map!");
                    p.teleport(getColiseuLoc().add(0,5,0));
                }else{
                    p.sendMessage("§4Caution! You are entering the area of the bord!");
                    double dmg = 2.5D;
                    if (p.getHealth() - dmg > 0.0D) {
                        p.damage(dmg);
                    } else {
                        p.setHealth(0.0D);
                        Bukkit.broadcastMessage("§a"+p.getDisplayName()+" §ckilled by bord!");
                    }
                }
            }
        }
    }
}