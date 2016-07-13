package eu.union.dev.listeners;

import eu.union.dev.*;
import eu.union.dev.Timer;
import eu.union.dev.api.Icon;
import eu.union.dev.events.*;
import eu.union.dev.storage.KPlayer;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.*;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.DedicatedServer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Fentis on 10/06/2016.
 */
public class HGListener implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        KitManager km = KitManager.getManager();
        HG.getInstance().getSQL().createPlayerProfile(p.getUniqueId());
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            List<String> kitNames = KitManager.getManager().getKits().stream().map(Kit::getName).collect(Collectors.toList());
            String allKits = StringUtils.join(kitNames, ", ");
            p.sendMessage("§a" + "(" + KitManager.getManager().getKits().size() + ") Kits: " + allKits + ".");
            if (km.getPlayerKitInLobby(p) == null){
                km.setPlayerKitInLobby(p,km.getKitByName("surprise"));
            }
            p.getInventory().clear();
            Util.getInstance().buildJoinIcons(p);
            Util.getInstance().buildScoreboard(p);
            Icon icon = km.getPlayerKitInLobby(p).getIcon();
            p.getInventory().setItem(1, KitLayout.getLayout().design(icon, km.getPlayerKitInLobby(p)));
            //p.teleport(new Location(p.getWorld(),0.5,160,0.5));
            HGManager.getInstance().addPlayersVivos(p);
        }else{
            if (!HGManager.getInstance().inReconect(p)){
                if (p.hasPermission(Perms.SPECTATOR.toString())){
                    p.setGameMode(GameMode.ADVENTURE);
                    HGManager.getInstance().addSpec(p);
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    p.getInventory().clear();
                    p.getInventory().setArmorContents(null);
                    Util.getInstance().buildSpecsIcons(p);
                    km.setPlayerKitInLobby(p,km.getKitByName("surprise"));
                    if (!HGManager.getInstance().getNoScore().contains(p)){
                        Util.getInstance().buildScoreboard(p);
                    }
                }else{
                    p.kickPlayer(Messages.PREFIX+" §aSorry you don't have permission for spectate!");
                }
            }else{
                HGManager.getInstance().addPlayersVivos(p);
                HGManager.getInstance().removeReconect(p);
                if (!HGManager.getInstance().getNoScore().contains(p)){
                    Util.getInstance().buildScoreboard(p);
                }
            }
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        e.setQuitMessage(null);
        KPlayer kplayer = PlayerManager.getPlayer(p.getUniqueId());
        if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY){
            if (HGManager.getInstance().isAlive(p) &&
                    !HGManager.getInstance().isSpec(p) &&
                    !HGManager.getInstance().inAdminMode(p)){
                if (!HGManager.getInstance().inReconect(p)){
                    startReconect(p);
                }
            }
        }else{
            HGManager.getInstance().removePlayersVivos(p);
            p.setLevel(0);
        }
        if (kplayer != null) {
            HG.getInstance().getSQL().updatePlayerProfileSQL(kplayer);
        } else {
            System.out.println("Inexisting PlayerProfile for this Player");
        }
    }
    public void startReconect(Player p){
        HGManager.getInstance().addReconect(p);
        HGManager.getInstance().removePlayersVivos(p);
        new BukkitRunnable(){
            int i = 0;
            @Override
            public void run() {
                i++;
                for (Player ps : Bukkit.getOnlinePlayers()){
                    if (p.getName().equalsIgnoreCase(ps.getName())){
                        HGManager.getInstance().removeReconect(p);
                        HGManager.getInstance().addPlayersVivos(p);
                        Bukkit.broadcastMessage(Messages.PREFIX+" §a"+p.getDisplayName()+" has connected in time!");
                        cancel();
                    }
                }
                if (!HGManager.getInstance().inReconect(p)){
                    cancel();
                }
                if (i>60){
                    HGManager.getInstance().removeReconect(p);
                    HGManager.getInstance().removePlayersVivos(p);
                    Timer.getInstace().detectWin();
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
            if (p.getAllowFlight()){
                p.setAllowFlight(false);
            }
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL,1.0F,1.0F);
            if (km.getPlayerKitInLobby(p).getName().equalsIgnoreCase("surprise")){
                List<Kit> kits = new ArrayList<>();
                for (Kit kit : km.getKits()){
                    if (!kit.getName().equalsIgnoreCase("Surprise")){
                        if (!km.isKitDisable(kit)){
                            kits.add(kit);
                        }
                    }
                }
                Kit kit = kits.get(new Random().nextInt(kits.size()));
                km.setPlayerKitInLobby(p,kit);
                p.sendMessage("§aYou kit surprise is §c"+kit.getName());
            }
            if (!HGManager.getInstance().inAdminMode(p)){
                km.applyKit(p,km.getPlayerKitInLobby(p));
                Weapon.addWeapon(p,Weapon.COMPASS);
            }
        }
        Bukkit.broadcastMessage(Messages.PREFIX+" §bThe game started! And may the odds be ever in your favor!");
    }
    @EventHandler
    public void onEndInv(HGEndInvencibleEvent e){
        Bukkit.broadcastMessage(Messages.PREFIX+" §cYou are not more invincible!");
        for (Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(),Sound.ANVIL_LAND,1.0F,1.0F);
        }
    }
    @EventHandler
    public void onFeastSpawn(HGFeastSpawnEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(),Sound.BLAZE_DEATH,1.0F,1.0F);
        }
        StructureCreator scf = new StructureCreator(e.getLocation(), StructureCreator.Structure.FEAST);
        scf.createStrucure();
        Bukkit.broadcastMessage(Messages.PREFIX+" §4The Feast appeared in §cX:"+e.getLocation().getX()+"§a, §cZ:"+e.getLocation().getZ()+"! Run or you'll lose it!");
    }
    @EventHandler
    public void onPlayerWin(HGPlayerWinEvent e){
        Player p = e.getWinner();
        HGManager.getInstance().setStatus(HGManager.Status.ENDGAME);
        KPlayer win = PlayerManager.getPlayer(p.getUniqueId());
        win.addWins();
        p.playSound(p.getLocation(),Sound.ORB_PICKUP,1.0F,1.0F);
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
                if (i>=20){
                    for (Player ps: Bukkit.getOnlinePlayers()){
                        ps.kickPlayer(Messages.PREFIX+" §aThe "+p.getDisplayName()+" won the game! Good game!");
                    }
                }
                if (i>=22){
                    Bukkit.shutdown();
                    cancel();
                }
            }
        }.runTaskTimer(HG.getInstance(),0,20);
    }
    @EventHandler
    public void onEnd(HGEndEvent e){
        Bukkit.broadcastMessage(Messages.PREFIX+" §cNo winners! Opening next match!");
        for (Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(Messages.PREFIX+" §cNo winners! Opening next match!");
            Util.getInstance().sendToServer("hub", p);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY ||
                HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            e.setCancelled(true);
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.INVINCIBILITY && e.getEntity() instanceof Player){
            e.setCancelled(true);
        }
        if (e.getEntity() instanceof Player){
            Player p = (Player)e.getEntity();
            if (HGManager.getInstance().isNoDamage(p)){
                e.setCancelled(true);
            }
            if (HGManager.getInstance().isSpec(p)){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if ((HGManager.getInstance().isSpec(p) ||
                HGManager.getInstance().getStatus() == HGManager.Status.DEATH_MATCH) &&
                !HGManager.getInstance().inBuild(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if ((HGManager.getInstance().isSpec(p) ||
                HGManager.getInstance().getStatus() == HGManager.Status.DEATH_MATCH) &&
                !HGManager.getInstance().inBuild(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSeconds(HGTimerSecondsEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (!HGManager.getInstance().getNoScore().contains(p)){
                Util.getInstance().updateSocoreBoard(p);
            }
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY ||
                HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            e.setCancelled(true);
        }else{
            if (e.getItemDrop().getItemStack().hasItemMeta()){
                ItemMeta im = e.getItemDrop().getItemStack().getItemMeta();
                if (im.spigot().isUnbreakable()){
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onSpawnItem(ItemSpawnEvent e){
        Item i = e.getEntity();
        if (i.getItemStack().hasItemMeta()){
            ItemMeta im = i.getItemStack().getItemMeta();
            if (im.spigot().isUnbreakable()){
                i.remove();
            }
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
                HGManager.getInstance().getStatus() == HGManager.Status.INVINCIBILITY ||
                HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY &&
                !HGManager.getInstance().inBuild(p)){
            e.setCancelled(true);
        }
        if (p.getItemInHand().getType() == Material.CHEST &&
                HGManager.getInstance().getStatus() == HGManager.Status.LOBBY &&
                !HGManager.getInstance().inAdminMode(p) && e.getAction() != Action.PHYSICAL){
            Bukkit.dispatchCommand(p,"kit");
        }
        if (p.getItemInHand().getType() == Material.STORAGE_MINECART &&
                HGManager.getInstance().getStatus() == HGManager.Status.LOBBY &&
                e.getAction() != Action.PHYSICAL){
            KitManager km = KitManager.getManager();
            List<Kit> kits = new ArrayList<>();
            for (Kit kit : km.getKits()){
                if (!p.hasPermission(kit.getPermission())){
                    kits.add(kit);
                }
            }
            if (kits.size() == 0){
                p.sendMessage(Messages.PREFIX+" §4You have all Kits");
            }else{
                Kit kitrandom = kits.get(new Random().nextInt(kits.size()));
                km.addKitDaPartidaPlayer(p,kitrandom);
                p.sendMessage(Messages.PREFIX+" §bYou kit random is... "+WordUtils.capitalize(kitrandom.getName()));
                p.setItemInHand(new Icon(Material.HOPPER_MINECART,"§cKit of match","§5Kit:"+WordUtils.capitalize(kitrandom.getName())).build());
                p.updateInventory();
            }

        }
        if (HGManager.getInstance().isSpec(p) && !HGManager.getInstance().inBuild(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSpawnMobs(CreatureSpawnEvent e){
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            e.setCancelled(true);
        }
        if (e.getEntity() instanceof Ghast ||
                e.getEntity() instanceof PigZombie){
            e.setCancelled(true);
        }
    }

    //ArrayList<UUID> respawn = new ArrayList<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        e.setDeathMessage(null);
        KPlayer death = PlayerManager.getPlayer(p.getUniqueId());
        death.addDeaths(1);
        if (p.getKiller() instanceof Player){
            KPlayer killer = PlayerManager.getPlayer(p.getKiller().getUniqueId());
            killer.addKills(1);
            HGManager.getInstance().addKills(p.getKiller());
        }
        p.getWorld().strikeLightningEffect(p.getLocation());
        /*if (p.hasPermission(Perms.RESPAWN.toString()) && !respawn.contains(p.getUniqueId()) &&
                HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){
            p.setHealth(20.0D);
            HGManager.getInstance().addNoDamage(p);
            p.sendMessage(Messages.PREFIX+" §aYou came back from the ashes! You gained 2m invincibility");
            respawn.add(p.getUniqueId());
            Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Util.getInstance().readyPlayer(p);
                    p.teleport(HGManager.getInstance().getColiseuLoc().add(0.5,3,0.5));
                    KitManager km = KitManager.getManager();
                    km.applyKit(p,km.getPlayerKitInLobby(p));
                    Weapon.addWeapon(p, Weapon.COMPASS);
                }
            },10);
            Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
                @Override
                public void run() {
                    HGManager.getInstance().removeNoDamage(p);
                    p.sendMessage(Messages.PREFIX+" §cYou are not more invincible!");
                    p.playSound(p.getLocation(),Sound.ANVIL_LAND,1.0F,1.0F);
                }
            },(2*60)*20);
            return;
        }*/
        if (p.hasPermission(Perms.SPECTATOR.toString())){
            p.setHealth(20.0D);
            p.setFoodLevel(20);
            p.setGameMode(GameMode.ADVENTURE);
            HGManager.getInstance().addSpec(p);
            HGManager.getInstance().removePlayersVivos(p);
            Timer.getInstace().detectWin();
            death.addLoses();
            KitManager km = KitManager.getManager();
            km.setPlayerKitInLobby(p,km.getKitByName("surprise"));
            Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Util.getInstance().readyPlayer(p);
                    p.setGameMode(GameMode.ADVENTURE);
                    p.teleport(p.getLocation().add(0,5,0));
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    Util.getInstance().buildSpecsIcons(p);
                }
            },10);
        }else{
            HGManager.getInstance().removePlayersVivos(p);
            Timer.getInstace().detectWin();
            death.addLoses();
            if (p.getKiller() != null){
                if (p.getKiller() instanceof Player){
                    Player killer = p.getKiller();
                    String kit = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(killer).getName());
                    p.sendMessage(Messages.PREFIX+" §aYou were killed by §7"+killer.getName()+"§c<"+kit+">"+"§a!");
                }else{
                    String entity = WordUtils.capitalize(p.getKiller().getType().toString().replaceAll("_"," "));
                    p.sendMessage(Messages.PREFIX+" §aYou were killed by §7"+entity+"§a!");
                }
            }else{
                p.sendMessage(Messages.PREFIX+" §aYou died! Try again!");
            }
            Util.getInstance().sendToServer("hub",p);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e){
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY ||
                HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDecaly(LeavesDecayEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onDeathMatch(HGDeathMatchEvent e){
        StructureCreator sc = new StructureCreator(e.getLocation(), StructureCreator.Structure.DEATHMATCH);
        sc.createStrucure();
        Bukkit.broadcastMessage(Messages.PREFIX+" §4DEATH MATCH!");
        for (Player p : Bukkit.getOnlinePlayers()){
            p.setNoDamageTicks(10*20);
            p.teleport(HGManager.getInstance().getDeathMatchLoc().add(0.5,2,0.5));
        }
    }
    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        if (HGManager.getInstance().getStatus() == HGManager.Status.DEATH_MATCH){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player damager = (Player)e.getDamager();
            Player hited = (Player)e.getEntity();
            if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY){
                if (HGManager.getInstance().getStatus() != HGManager.Status.INVINCIBILITY){
                    String kit = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(hited).getName());
                    BossBarAPI.setMessage(damager,"§7"+hited.getName()+" - "+kit);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            BossBarAPI.removeBar(damager);
                        }
                    },3*20);
                }
            }
            if (damager.getItemInHand().getType().toString().contains("_SWORD")){
                e.setDamage(e.getDamage()/2);
            }
        }
    }
    @EventHandler
    public void onDeathMenssage(PlayerDeathEvent e){
        Util.getInstance().sendMessageOfDeath(e.getEntity(),e.getEntity().getKiller(),e.getEntity().getLastDamageCause().getCause());
    }
    @EventHandler
    public void onOpenInv(InventoryOpenEvent e){
        Player p = (Player)e.getPlayer();
        p.updateInventory();
    }
    @EventHandler
    public void setMotd(HGTimerSecondsEvent e){
        String motd = "§4ERROR";
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY &&
                HGManager.getInstance().getPlayersVivos().size() < Bukkit.getServer().getMaxPlayers()){
            if (Timer.getInstace().getTime() <= 10){
                motd = "§aStarting";
            }else{
                motd = "§bIn lobby";
            }
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY &&
                HGManager.getInstance().getPlayersVivos().size() >= Bukkit.getServer().getMaxPlayers()){
            if (Timer.getInstace().getTime() <= 10){
                motd = "§aStarting";
            }else{
                motd = "§5Full";
            }
        }
        if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY &&
                HGManager.getInstance().getStatus() != HGManager.Status.ENDGAME){
            motd = "§cIn game";
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            motd = "§4Restarting";
        }
        //MinecraftServer.getServer().setMotd(motd);
        MinecraftServer.getServer().ay().setMOTD(ChatSerializer.a("{\"text\": \"" + motd + "\"}"));
    }
    @EventHandler
    public void onMotd(ServerListPingEvent e){
        String ip = HG.getInstance().getConfig().getString("IP").toUpperCase();
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY &&
                HGManager.getInstance().getPlayersVivos().size() < Bukkit.getServer().getMaxPlayers()){
            if (Timer.getInstace().getTime() <= 10){
                e.setMotd("§aStarting");
            }else{
                e.setMotd("§bIn lobby");
            }
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY &&
                HGManager.getInstance().getPlayersVivos().size() >= Bukkit.getServer().getMaxPlayers()){
            if (Timer.getInstace().getTime() <= 10){
                e.setMotd("§aStarting");
            }else{
                e.setMotd("§5Full");
            }
        }
        if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY &&
                HGManager.getInstance().getStatus() != HGManager.Status.ENDGAME){
            e.setMotd("§cIn game");
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            e.setMotd("§4Restarting");
        }
    }
    @EventHandler
    public void onGuardianDeath(EntityDeathEvent e){
        if (e.getEntity() instanceof Zombie){
            if (HGManager.getInstance().getDungeonGuardian() != null){
                if (e.getEntity().getUniqueId().equals(HGManager.getInstance().getDungeonGuardian())){
                    HGManager.getInstance().scd.addChestDungeon();
                    e.getDrops().clear();
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(),Sound.ENDERDRAGON_DEATH,1,1);
                }
            }
        }
    }
    @EventHandler
    public void onDungeonSpawn(HGDungeonSpawnEvent e){
        HGManager.getInstance().scd.createStrucure();
        Bukkit.broadcastMessage(Messages.PREFIX+" §cDungeon has spawned in §cX:"+e.getLocation().getX()+"§a, §cZ:"+e.getLocation().getZ()+"§a!");
    }
}
