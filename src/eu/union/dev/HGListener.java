package eu.union.dev;

import eu.union.dev.api.Icon;
import eu.union.dev.events.*;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Fentis on 10/06/2016.
 */
public class HGListener implements Listener{

    private ArrayList<String> reconect = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        KitManager km = KitManager.getManager();
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            List<String> kitNames = KitManager.getManager().getKits().stream().map(Kit::getName).collect(Collectors.toList());
            String allKits = StringUtils.join(kitNames, ", ");
            p.sendMessage("§a" + "(" + KitManager.getManager().getKits().size() + ") Kits: " + allKits + ".");// mostra todos os kits
            if (km.getPlayerKitInLobby(p) == null){//da kit suprise se n tiver escolhido um antes
                km.setPlayerKitInLobby(p,km.getKitByName("surprise"));
            }
            p.getInventory().clear();
            Util.getInstance().buildJoinIcons(p);//da os items
            if (!HGManager.getInstance().getNoScore().contains(p)){
                Util.getInstance().buildScoreboard(p);
            }
            Icon icon = km.getPlayerKitInLobby(p).getIcon();
            p.getInventory().setItem(8, KitLayout.getLayout().design(icon, km.getPlayerKitInLobby(p)));//adiciona o kit na hotbar
            p.teleport(new Location(p.getWorld(),0.5,160,0.5));//teleporta para o coliseu
            HGManager.getInstance().addPlayersVivos(p);
            Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.teleport(new Location(p.getWorld(),0.5,160,0.5));//teleporta para o coliseu novamente para desbugar
                }
            },20);
        }else{
            if (!reconect.contains(p.getName())){
                if (p.hasPermission(Perms.SPECTATOR.toString())){
                    p.setGameMode(GameMode.ADVENTURE);
                    HGManager.getInstance().isSpec(p);
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    p.getInventory().clear();
                    p.getInventory().setArmorContents(null);
                    if (km.getPlayerKitInLobby(p) == null){
                        km.setPlayerKitInLobby(p,km.getKitByName("surprise"));
                    }
                    Util.getInstance().buildScoreboard(p);
                }else{
                    p.kickPlayer(Messages.PREFIX+" §aSorry you don't have permission for spectate!");
                }
            }else{
                HGManager.getInstance().addPlayersVivos(p);
                reconect.remove(p.getName());
            }
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        e.setQuitMessage(null);
        if (HGManager.getInstance().getStatus() != HGManager.Status.LOBBY){
            if (!reconect.contains(p.getName())){
                reconect.add(p.getName());
                startReconect(p);
            }
        }else{
            HGManager.getInstance().removePlayersVivos(p);
        }
    }
    public void startReconect(Player p){
        new BukkitRunnable(){
            int i = 0;
            @Override
            public void run() {
                i++;
                for (Player ps : Bukkit.getOnlinePlayers()){
                    if (p.getName().equalsIgnoreCase(ps.getName())){
                        reconect.remove(p.getName());
                        Bukkit.broadcastMessage(Messages.PREFIX+" §a"+p.getDisplayName()+" connected in time!");
                        cancel();
                    }
                }
                if (i>60){
                    reconect.remove(p.getName());
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
            if (km.getKitAmIUsing(p,"surprise")){
                List<Kit> kits = new ArrayList<>();
                for (Kit kit : km.getKits()){
                    if (!kit.getName().equalsIgnoreCase("Surprise")){
                        kits.add(kit);
                    }
                }
                Kit kit = kits.get(new Random().nextInt(kits.size()));
                km.setPlayerKitInLobby(p,kit);
                p.sendMessage("§aYou kit surprise is §c"+kit.getName());
            }
            km.applyKit(p,km.getPlayerKitInLobby(p));
            Weapon.addWeapon(p,Weapon.COMPASS);
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
        if (HGManager.getInstance().getStatus() == HGManager.Status.INVINCIBILITY && e.getEntity() instanceof Player){
            e.setCancelled(true);
        }
        if (e.getEntity() instanceof Player){
            Player p = (Player)e.getEntity();
            if (HGManager.getInstance().isSpec(p)){
                e.setCancelled(true);
            }
            if (nodamage.contains(p.getUniqueId())){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player p = (Player)e.getDamager();
            if (HGManager.getInstance().isSpec(p)){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (e.getBlock().getY() >=135 && !HGManager.getInstance().inBuild(p)){
            e.setCancelled(true);
        }
        Location loc = e.getBlock().getLocation();
        Location loc2 = new Location(e.getBlock().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= (HGManager.getInstance().getBordSize()-10)) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= (HGManager.getInstance().getBordSize()-10)))
                && !HGManager.getInstance().inBuild(p))
        {
            e.setCancelled(true);
        }
        if (HGManager.getInstance().isSpec(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if (e.getBlock().getY() >=135 && !HGManager.getInstance().inBuild(p)){
            e.setCancelled(true);
        }
        Location loc = e.getBlock().getLocation();
        Location loc2 = new Location(e.getBlock().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= (HGManager.getInstance().getBordSize()-10)) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= (HGManager.getInstance().getBordSize()-10)))
                && !HGManager.getInstance().inBuild(p))
        {
            e.setCancelled(true);
        }
        if (HGManager.getInstance().isSpec(p)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSeconds(HGTimerSecondsEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (!HGManager.getInstance().getNoScore().contains(p)){
                Util.getInstance().updateSocoreBoard(p);
            }
            Util.getInstance().setTab(p);
            for (Player ps : Bukkit.getOnlinePlayers()){
                if (HGManager.getInstance().isSpec(ps)){
                    if (!HGManager.getInstance().isSpec(p)){
                        p.hidePlayer(ps);
                        for (Entity en : ps.getNearbyEntities(5,5,5)){
                            if (en instanceof Player){
                                Player p2 = (Player)en;
                                if (HGManager.getInstance().isSpec(p2)){
                                    Vector v = p2.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(3);
                                    p2.setVelocity(v);
                                }
                            }
                        }
                    }
                }
            }
            //teleport para o coliseu se o player cair dele
            if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
                if (p.getLocation().getY() <= 140){
                    p.teleport(new Location(p.getWorld(),0.5,155,0.5));
                }
            }else{
                //damage de altura
                if (HGManager.getInstance().getStatus() != HGManager.Status.INVINCIBILITY &&
                        p.getGameMode() == GameMode.SURVIVAL &&
                        p.getLocation().getY() >= 145 &&
                        !nodamage.contains(p.getUniqueId())){
                    p.damage(4.0);
                }
            }
            //dano ao player q sair das bordas
            Location loc = p.getLocation();
            Location loc2 = new Location(p.getWorld(), 0, 0, 0);
            if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= HGManager.getInstance().getBordSize()) ||
                    (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= HGManager.getInstance().getBordSize())))
            {//alem da borda
                if (HGManager.getInstance().isSpec(p)){//teleporte de players q estão em spec qu n seja staff para o coliseu
                    if (!Perms.isStaff(p)){
                        p.teleport(new Location(p.getWorld(),0.5,155,0.5));
                    }
                }else{
                    double dmg = 2.5D;
                    if (p.getHealth() - dmg > 0.0D) {//da dano a cada segundo se estiver alem borda
                        p.damage(dmg);
                    } else {
                        p.setHealth(0.0D);//morte pela borda
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
        if (i.getItemStack().hasItemMeta()){//se o item for um item de kit o item é removido do chão
            ItemMeta im = i.getItemStack().getItemMeta();
            if (im.spigot().isUnbreakable()){
                i.remove();
            }
        }
    }
    @EventHandler
    public void onMiniFeastSpawn(HGMiniFeastSpawnEvent e){//spawn de minieast
        StructureCreator scmf = new StructureCreator(e.getLocation(), StructureCreator.Structure.MINIFEAST);
        scmf.createStrucure();
        Bukkit.broadcastMessage(Messages.PREFIX+" §aMiniFeast has spawned in §cX:"+e.getLocation().getX()+"§a, §cZ:"+e.getLocation().getZ()+"§a!");
    }
    @EventHandler
    public void onQuest(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {//fome apenas depois da invincibilidade
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
                HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            Bukkit.dispatchCommand(p,"kit");
        }
    }
    @EventHandler
    public void onSpawnMobs(CreatureSpawnEvent e){
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            e.setCancelled(true);
        }
        if (e.getEntity() instanceof Ghast){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {//cancela explosões proximas a borda
        Location loc = e.getEntity().getLocation();
        Location loc2 = new Location(e.getEntity().getWorld(), 0, 120, 0);
        if (((Math.abs(loc.getBlockX() + loc2.getBlockX()) >= (HGManager.getInstance().getBordSize()-10)) ||
                (Math.abs(loc.getBlockZ() + loc2.getBlockZ()) >= (HGManager.getInstance().getBordSize()-10))))
        {
            e.setCancelled(true);
        }
    }
    ArrayList<UUID> respawn = new ArrayList<>();
    ArrayList<UUID> nodamage = new ArrayList<>();
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        e.setDeathMessage(null);
        if (p.hasPermission(Perms.RESPAWN.toString()) && !respawn.contains(p.getUniqueId()) &&
                HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){//deixa a pessoa reviver 1vez se tiver perm
            p.setHealth(20.0D);
            nodamage.add(p.getUniqueId());
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
                    nodamage.remove(p.getUniqueId());
                    p.sendMessage(Messages.PREFIX+" §cYou are not more invincible!");
                }
            },(2*60)*20);
            return;
        }
        if (p.hasPermission(Perms.SPECTATOR.toString())){//deixa o player spectar
            Util.getInstance().readyPlayer(p);
            p.setGameMode(GameMode.ADVENTURE);
            HGManager.getInstance().addSpec(p);
            HGManager.getInstance().removePlayersVivos(p);
            Timer.getInstace().detectWin();
            Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Util.getInstance().readyPlayer(p);
                    p.teleport(p.getLocation().add(0.5,5,0.5));
                    p.setAllowFlight(true);
                    p.setFlying(true);
                }
            },10);
            return;
        }else{
            HGManager.getInstance().removePlayersVivos(p);
            Timer.getInstace().detectWin();
            p.kickPlayer(Messages.PREFIX+" §aYou don't have permission for spectate!");
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {//chat do server
        String prefix = PermissionsEx.getUser(e.getPlayer()).getGroups()[0].getPrefix();
        e.setCancelled(true);
        if (HGManager.getInstance().isSpec(e.getPlayer())){
            for (Player p : Bukkit.getOnlinePlayers()){
                if (HGManager.getInstance().isSpec(e.getPlayer()) ||
                        Perms.isStaff(p)){
                    p.sendMessage(prefix + "§r§7»(Spec)" + e.getPlayer().getName() + ": §f" + ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                }
            }
        }else{
            Bukkit.broadcastMessage(prefix + "»§r§7" + e.getPlayer().getName() + ": §f" + ChatColor.translateAlternateColorCodes('&',e.getMessage()));
        }
    }
    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e){//deixa o player
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY ||
                HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            e.setCancelled(true);
        }
        if (HGManager.getInstance().isSpec(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDecaly(LeavesDecayEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onCompass(HGTimerSecondsEvent e){//compass para mostrar a loc do player maix proximo
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.getItemInHand().getType() == Material.COMPASS){
                String message = "§c§lNo Players!";
                List<Player> players = new ArrayList<>();
                for (Player ps : p.getWorld().getPlayers()){
                    if (!(ps.getUniqueId().equals(p.getUniqueId())) &&
                            !HGManager.getInstance().isSpec(ps) &&
                            ps.getGameMode() == GameMode.SURVIVAL){
                        players.add(ps);
                    }
                }
                Collections.sort(players, new CompassCompare(p));
                Player nearest = null;

                try {
                    nearest = players.get(0);
                }
                catch (IndexOutOfBoundsException ix){}
                if (nearest != null){
                    message = "§a§lPlayer:§f§l "+nearest.getName()+" " +
                            "§a§lDistance:§f§l "+((int)nearest.getLocation().distance(p.getLocation()));
                    p.setCompassTarget(nearest.getLocation());
                }
                Packets.getAPI().sendActionBar(p,message);
            }
        }
    }
    @EventHandler
    public void onTarget(EntityTargetEvent e){//cancela os mobs seguirem specs
        if (e.getTarget() instanceof Player){
            Player p = (Player)e.getTarget();
            if (HGManager.getInstance().isSpec(p)){
                e.setCancelled(true);
            }
        }
    }
}
