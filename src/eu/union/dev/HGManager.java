package eu.union.dev;

import eu.union.dev.events.*;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.*;
import org.apache.commons.lang.StringUtils;
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
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Fentis on 06/06/2016.
 */
public class HGManager implements Listener{

    private static HGManager instance = new HGManager();
    public static HGManager getInstance() {
        return instance;
    }
    private Status status;
    private ArrayList<UUID> reconect = new ArrayList<>();
    Location feast,minifeast1,minifeast2,minifeast3,coliseu = null;
    int bordsize;
    public enum Status {
        LOBBY("Lobby"),
        INVENCIBILITY("Invencibility"),
        POSINVINCIBILITY("PosInvencibility"),
        FEAST_ANNOUNCEMENT("FeastAnnouncement"),
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
    StructureCreator scc;
    public void setup(){
        bordsize = 480;
        coliseu = new Location(Bukkit.getWorlds().get(0),0,150,0);
        feast = RandomLocation(100);
        minifeast1 = RandomLocation(300);
        minifeast2 = RandomLocation(300);
        minifeast3 = RandomLocation(300);
        scc = new StructureCreator(coliseu, StructureCreator.Structure.COLISEU);
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

    public Location getMiniFeastLoc(int n) {
        if (n == 1){
            return minifeast1;
        }
        if (n == 2){
            return minifeast2;
        }
        if (n == 3){
            return minifeast3;
        }
        return null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        KitManager km = KitManager.getManager();
        if (HGManager.getInstance().getStatus() == Status.LOBBY){
            List<String> kitNames = KitManager.getManager().getKits().stream().map(Kit::getName).collect(Collectors.toList());
            String allKits = StringUtils.join(kitNames, ", ");
            p.sendMessage("§a" + "(" + KitManager.getManager().getKits().size() + ") Kits: " + allKits + ".");
            if (km.getPlayerKitInLobby(p) == null){
                km.setPlayerKitInLobby(p,km.getKitByName("Surprise"));
            }
            p.getInventory().clear();
            Util.getInstance().buildJoinIcons(p);
            p.teleport(getColiseuLoc().add(0,5,0));
        }else{
            if (!reconect.contains(p.getUniqueId())){
                if (p.hasPermission(Perms.SPECTATOR.toString())){
                    p.setGameMode(GameMode.SPECTATOR);
                    p.getInventory().clear();
                    p.getInventory().setArmorContents(null);
                }else{
                    p.kickPlayer(Messages.PREFIX+" §aSorry you don't have permission for spectate!");
                }
            }else{
                reconect.remove(p.getUniqueId());
            }
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if (HGManager.getInstance().getStatus() != Status.LOBBY){
            if (!reconect.contains(p.getUniqueId())){
                reconect.add(p.getUniqueId());
                startReconect(p);
            }
        }
    }
    public void startReconect(Player p){
        UUID uuid = p.getUniqueId();
        new BukkitRunnable(){
            int i = 0;
            @Override
            public void run() {
                i++;
                for (Player p : Bukkit.getOnlinePlayers()){
                    if (p.getUniqueId() == uuid){
                        reconect.remove(uuid);
                        cancel();
                    }
                }
                if (i>60){
                    reconect.remove(uuid);
                    Bukkit.broadcastMessage(Messages.PREFIX+" §c"+p.getDisplayName()+" not connected in time!");
                    cancel();
                }
            }
        }.runTaskTimer(HG.getInstance(),0,20);
    }
    @EventHandler
    public void onStart(HGStartEvent e){
        scc.removePistons();
        KitManager km = KitManager.getManager();
        for (Player p : Bukkit.getOnlinePlayers()){
            km.applyKit(p,km.getPlayerKitInLobby(p));
            if (km.getKitAmIUsing(p,"surprise")){
                List<Kit> kits = new ArrayList<>();
                for (Kit kit : km.getKits()){
                    if (!kit.getName().equalsIgnoreCase("Surprise")){
                        kits.add(kit);
                    }
                }
                Kit kit = kits.get(new Random().nextInt(kits.size()));
                km.setPlayerKitInLobby(p,kit);
                km.applyKit(p,kit);
                p.sendMessage("§aYou kit surprise is §c"+kit.getName());
            }
            Weapon.addWeapon(p,Weapon.COMPASS);
        }
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
        new BukkitRunnable(){
            int i=0;
            @Override
            public void run() {
                i++;
                Util.getInstance().fireworksRandom(p);
                if (i>=10){
                    Bukkit.shutdown();
                }
            }
        }.runTaskTimer(HG.getInstance(),0,20);
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
        if (getStatus() == Status.LOBBY || getStatus() == Status.ENDGAME){
            e.setCancelled(true);
        }
        if (getStatus() == Status.INVENCIBILITY && e.getEntity() instanceof Player){
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
            if (p.getLocation().getY() <= getColiseuLoc().add(0,-10,0).getY() && getStatus() == Status.LOBBY){
                p.teleport(getColiseuLoc().add(0,5,0));
            }
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (HGManager.getInstance().getStatus() == Status.LOBBY || HGManager.getInstance().getStatus() == Status.ENDGAME){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onMiniFeastSpawn(HGMiniFeastSpawnEvent e){
        StructureCreator scmf = new StructureCreator(e.getLocation(), StructureCreator.Structure.MINIFEAST);
        scmf.createStrucure();
        Bukkit.broadcastMessage(Messages.PREFIX+" §aMiniFeast has spawned in §cX:"+e.getLocation().getX()+"§a, §cZ:"+e.getLocation().getZ()+"§a!");
    }
    @EventHandler
    public void onQuest(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY ||
                HGManager.getInstance().getStatus() == HGManager.Status.INVENCIBILITY ||
                HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            e.setCancelled(true);
        }
    }
}