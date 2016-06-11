package eu.union.dev;

import eu.union.dev.events.*;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Fentis on 10/06/2016.
 */
public class HGListener implements Listener{

    private ArrayList<UUID> reconect = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        KitManager km = KitManager.getManager();
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            List<String> kitNames = KitManager.getManager().getKits().stream().map(Kit::getName).collect(Collectors.toList());
            String allKits = StringUtils.join(kitNames, ", ");
            p.sendMessage("§a" + "(" + KitManager.getManager().getKits().size() + ") Kits: " + allKits + ".");
            if (km.getPlayerKitInLobby(p) == null){
                km.setPlayerKitInLobby(p,km.getKitByName("surprise"));
            }
            p.getInventory().clear();
            Util.getInstance().buildJoinIcons(p);
            p.teleport(new Location(p.getWorld(),0.5,160,0.5));
            HGManager.getInstance().addPlayersVivos(p);
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
                HGManager.getInstance().addPlayersVivos(p);
                reconect.remove(p.getUniqueId());
            }
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        e.setQuitMessage(null);
        if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY){
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
                    HGManager.getInstance().removePlayersVivos(p);
                    Bukkit.broadcastMessage(Messages.PREFIX+" §c"+p.getDisplayName()+" not connected in time!");
                    cancel();
                }
            }
        }.runTaskTimer(HG.getInstance(),0,20);
    }
    @EventHandler
    public void onStart(HGStartEvent e){
        HGManager.getInstance().scc.removePistons();
        Bukkit.getWorlds().get(0).setTime(0);
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
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY || HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            e.setCancelled(true);
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.INVENCIBILITY && e.getEntity() instanceof Player){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBreack(BlockBreakEvent e){
        if (e.getBlock().getY() >=135){
            e.setCancelled(true);
        }
        Location loc = e.getBlock().getLocation();
        Location loc2 = new Location(e.getBlock().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= (HGManager.getInstance().getBordSize()-10)) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= (HGManager.getInstance().getBordSize()-10))))
        {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if (e.getBlock().getY() >=135){
            e.setCancelled(true);
        }
        Location loc = e.getBlock().getLocation();
        Location loc2 = new Location(e.getBlock().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= (HGManager.getInstance().getBordSize()-10)) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= (HGManager.getInstance().getBordSize()-10))))
        {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSeconds(HGTimerSecondsEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                if (p.getLocation().getY() <= 140){
                    p.teleport(new Location(p.getWorld(),0.5,155,0.5));
                }
            }
            if (HGManager.getInstance().getStatus() == HGManager.Status.POSINVINCIBILITY){
                if (p.getGameMode() == GameMode.SURVIVAL && p.getLocation().getY() >= 145){
                    p.damage(4.0);
                }
            }
            Location loc = p.getLocation();
            Location loc2 = new Location(p.getWorld(), 0, 0, 0);
            if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= HGManager.getInstance().getBordSize()) ||
                    (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= HGManager.getInstance().getBordSize())))
            {
                if (p.getGameMode() == GameMode.SPECTATOR){
                    if (!Perms.isStaff(p)){
                        p.teleport(new Location(p.getWorld(),0.5,155,0.5));
                    }
                }else{
                    double dmg = 2.5D;
                    if (p.getHealth() - dmg > 0.0D) {
                        p.damage(dmg);
                    } else {
                        p.setHealth(0.0D);
                        Bukkit.broadcastMessage("§c"+p.getDisplayName()+" killed by bord!");
                    }
                }
            }
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY || HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
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
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (p.getItemInHand().getType() == Material.CHEST &&
                HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            Bukkit.dispatchCommand(p,"kit");
        }
    }
    @EventHandler
    public void onSpawnMobs(CreatureSpawnEvent e){
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        Location loc = e.getEntity().getLocation();
        Location loc2 = new Location(e.getEntity().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= (HGManager.getInstance().getBordSize()-10)) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= (HGManager.getInstance().getBordSize()-10))))
        {
            e.setCancelled(true);
        }
    }
}
