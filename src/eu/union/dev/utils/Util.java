package eu.union.dev.utils;


import eu.union.dev.*;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Util {

    private static Util instance = new Util();

    public static Util getInstance() {
        return instance;
    }


    public void sendCooldownMessage(Player player, Ability cooldown, TimeUnit timeUnit, boolean actionbar) {
        if (!actionbar) {
            player.sendMessage(Messages.PREFIX.toString() + " §7You're in cooldown of §c{time} §7seconds".replace("{time}", String.valueOf(cooldown.getStatus(player).getRemainingTime(timeUnit))));
        } else {
            player.sendMessage(Messages.PREFIX.toString() + " §7You're in cooldown of §c{time} §7seconds".replace("{time}", String.valueOf(cooldown.getStatus(player).getRemainingTime(timeUnit))));
            Packets.getAPI().sendActionBar(player, "§7You're in cooldown of §c{time} §7seconds".replace("{time}", String.valueOf(cooldown.getStatus(player).getRemainingTime(timeUnit))));
        }
    }


    public void buildJoinIcons(Player player) {
        Inventory inv = player.getInventory();
        inv.clear();
        KitManager km = KitManager.getManager();
        {
            Icon kits = new Icon(Material.CHEST, "§bKits", "§7Choose your kit");
            inv.setItem(0, kits.build());
        }

        {
            if (km.getKitDaPartidaPlayer(player) == null){
                Icon kitm = new Icon(Material.STORAGE_MINECART, "§5Kit of match", "§7Obtain a random kit in this match");
                inv.setItem(7, kitm.build());
            }else{
                Icon kitm = new Icon(Material.HOPPER_MINECART, "§cKit of match", "§5Kit:"+WordUtils.capitalize(km.getKitDaPartidaPlayer(player).getName()));
                inv.setItem(7, kitm.build());
            }
        }

        {
            Icon kang = new Icon(Material.FIREWORK, "§dJump!");
            inv.setItem(8, kang.build());
        }

        {
            Icon menu = new Icon(Material.NETHER_STAR, "§aMenu", "§7Coming soon...");
            inv.setItem(4, menu.build());
        }

        {
            ItemStack i = new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5);
            ItemMeta im = i.getItemMeta();
            im.setDisplayName("§7");
            i.setItemMeta(im);
            //add items
        }
    }

    public void buildSpecsIcons(Player player) {
        Inventory inv = player.getInventory();
        inv.clear();

        {
            Icon compass = new Icon(Material.COMPASS, "§bTeleport");
            inv.setItem(4, compass.build());
        }

        {
            Icon sponsor = new Icon(Material.CAKE, "§eSponsor");
            inv.setItem(0, sponsor.build());
        }

        {
            Icon leave = new Icon(Material.BED, "§cReturn to the hub");
            inv.setItem(8, leave.build());
        }
    }
    public void buildAdminIcons(Player player) {
        Inventory inv = player.getInventory();
        inv.clear();
        {
            Icon chest = new Icon(Material.CHEST, "§aOpen Inventory");
            inv.setItem(0, chest.build());
            Icon switchmode = new Icon(Material.SLIME_BALL, "§cSwitch AdminMode");
            inv.setItem(1, switchmode.build());
            Icon adminmenu = new Icon(Material.ENDER_CHEST, "§cAdmin menu");
            inv.setItem(4, adminmenu.build());
            Icon compass = new Icon(Material.COMPASS, "§cTeleport");
            inv.setItem(8, compass.build());
        }
    }

    public void buildScoreboard(Player p) {
        //final KPlayer profile = PlayerManager.getPlayer(p.getUniqueId());
        KitManager km = KitManager.getManager();

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective stats = board.registerNewObjective("stats", "dummy");
        stats.setDisplaySlot(DisplaySlot.SIDEBAR);
        int index = 13;
        stats.setDisplayName("     §6§lUnion-HG     ");
        stats.getScore("§a").setScore(index--);
        stats.getScore("§fStage:").setScore(index--);
        stats.getScore("§1").setScore(index--);
        stats.getScore("§fTimer:").setScore(index--);
        stats.getScore("§2").setScore(index--);
        stats.getScore("§fPlayers:").setScore(index--);
        stats.getScore("§3").setScore(index--);
        stats.getScore("§fKit:").setScore(index--);
        stats.getScore("§4").setScore(index--);
        stats.getScore("§fKills:").setScore(index--);
        stats.getScore("§5").setScore(index--);
        stats.getScore("§d").setScore(index--);
        stats.getScore("§6"+HG.getInstance().getConfig().getString("IP")).setScore(index--);
        stats.getScore("§7/score").setScore(index);

        board.registerNewTeam("stage").addEntry("§1");
        board.registerNewTeam("timer").addEntry("§2");
        board.registerNewTeam("online").addEntry("§3");
        board.registerNewTeam("kit").addEntry("§4");
        board.registerNewTeam("kills").addEntry("§5");

        final Team stage = board.getTeam("stage");
        final Team timer = board.getTeam("timer");
        final Team online = board.getTeam("online");
        final Team kit = board.getTeam("kit");
        final Team kills = board.getTeam("kills");

        String stagee = getStage();
        String timerr = "";
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            timerr = "§a"+Timer.getInstace().getTimerFormated();
        }else{
            timerr = "§c"+Timer.getInstace().getTimerFormated();
        }
        String onlinee = ""+HGManager.getInstance().getPlayersVivos().size();
        String kitt = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(p).getName());
        stage.setPrefix(stagee);
        timer.setPrefix(timerr);
        online.setPrefix("§e" + onlinee+"/"+Bukkit.getServer().getMaxPlayers());
        kit.setPrefix("§b"+kitt);
        kills.setPrefix("§c" + HGManager.getInstance().getKills(p));

        p.setScoreboard(board);
    }

    public void updateSocoreBoard(Player p){
        Scoreboard board = p.getScoreboard();
        final Team stage = board.getTeam("stage");
        final Team timer = board.getTeam("timer");
        final Team online = board.getTeam("online");
        final Team kit = board.getTeam("kit");
        final Team kills = board.getTeam("kills");

        String stagee = getStage();
        String timerr = "";
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            timerr = "§a"+Timer.getInstace().getTimerFormated();
        }else{
            timerr = "§c"+Timer.getInstace().getTimerFormated();
        }
        String onlinee = ""+HGManager.getInstance().getPlayersVivos().size();
        String kitt = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(p).getName());
        stage.setPrefix(stagee);
        timer.setPrefix(timerr);
        online.setPrefix("§e" + onlinee+"/"+Bukkit.getServer().getMaxPlayers());
        kit.setPrefix("§b"+kitt);
        kills.setPrefix("§c" + HGManager.getInstance().getKills(p));
    }

    /*public void setTab(Player p){
        String kit = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(p).getName());
        Packets.getAPI().setHeaderFooter(p,"§6§lUnion§f§l-§6§lHG","§cKills: §f"+HGManager.getInstance().getKills(p)+" §bKit: §f"+kit+" §aStage: §f"+getStage()+" §bTimer: §e"+Timer.getInstace().getTimerFormated()+" §7Ping: §f"+((CraftPlayer)p).getHandle().ping);
    }*/
    public String getStage(){
        String stage = "";
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            stage = "§aWaiting";
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.INVINCIBILITY){
            stage = "§bStart";
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.POS_INVINCIBILITY){
            stage = "§cIn Battle";
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.FEAST_ANNOUNCEMENT){
            stage = "§aPre Feast";
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.FEAST){
            stage = "§cIn Feast";
        }
        if (HGManager.getInstance().getStatus() == HGManager.Status.ENDGAME){
            stage = "§bIn End";
        }
        return stage;
    }
    public void readyPlayer(Player player) {
        player.getInventory().clear();
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setExhaustion(0f);
        player.setFallDistance(0f);
        player.setFireTicks(0);
        player.setAllowFlight(false);

        for (PotionEffect pE : player.getActivePotionEffects()) {
            player.removePotionEffect(pE.getType());
        }
    }

    public void readyPlayerNoHealth(Player player) {
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setExhaustion(0f);
        player.setFallDistance(0f);
        player.setFireTicks(0);
        player.setAllowFlight(false);

        for (PotionEffect pE : player.getActivePotionEffects()) {
            player.removePotionEffect(pE.getType());
        }

    }

    public void sendMessageOfDeath(Player killer, Player killed, EntityDamageEvent.DamageCause damage){
        KitManager km = KitManager.getManager();
        String kit = WordUtils.capitalize(km.getPlayerKitInLobby(killed).getName());
        if ((killer == null)) {
            if (damage == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ||
                    damage == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {//explosão
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" +"§c has died by explosion");
            }
            if (damage == EntityDamageEvent.DamageCause.LAVA) {//lava
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by lava");
            }
            if (damage == EntityDamageEvent.DamageCause.FALL) {//queda
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c thought it was a bird and died");
            }
            if (damage == EntityDamageEvent.DamageCause.FIRE ||
                    damage == EntityDamageEvent.DamageCause.FIRE_TICK) {//fogo
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by fire");
            }
            if (damage == EntityDamageEvent.DamageCause.MAGIC) {//magica
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by magic");
            }
            if (damage == EntityDamageEvent.DamageCause.DROWNING) {//falta de ar
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c thought it was a fish and died");
            }
            if (damage == EntityDamageEvent.DamageCause.WITHER) {//whiter potion
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by wither");
            }
            if (damage == EntityDamageEvent.DamageCause.POISON) {//veneno
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by poison");
            }
            if (damage == EntityDamageEvent.DamageCause.FALLING_BLOCK) {//bloco (bigorna)
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by falling block");
            }
            if (damage == EntityDamageEvent.DamageCause.LIGHTNING) {//raio
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by lightning");
            }
            if (damage == EntityDamageEvent.DamageCause.PROJECTILE) {//projetil
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by projectile");
            }
            if (damage == EntityDamageEvent.DamageCause.VOID) {//void
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by void");
            }
            if (damage == EntityDamageEvent.DamageCause.SUICIDE) {//se matou
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by suicide");
            }
            if (damage == EntityDamageEvent.DamageCause.SUFFOCATION){//sufocado
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by suffocation");
            }
            if (damage == EntityDamageEvent.DamageCause.STARVATION){//fome
                Bukkit.broadcastMessage("§a" + killed.getName() +"§a<"+kit+">" + "§c has died by starvation");
            }
            if (damage == EntityDamageEvent.DamageCause.THORNS){//armadura de thorns
                Bukkit.broadcastMessage("§a" + killed.getName() + "§a<"+kit+">" + "§c has died by thorns");
            }
            if (damage == EntityDamageEvent.DamageCause.ENTITY_ATTACK){//entidades
                Entity en = killed.getLastDamageCause().getEntity();
                String entidade = en.toString().toLowerCase();
                Bukkit.broadcastMessage("§a" + killed.getName() + "§a<"+kit+">" + "§c has died by thorns");
            }
        }else{
            String kitk = WordUtils.capitalize(km.getPlayerKitInLobby(killer).getName());
            Bukkit.broadcastMessage("§a" + killed.getDisplayName()+ "§a<"+kit+">" + " §chas been slained by §b" + killer.getDisplayName()+"§a<"+kitk+">");
        }
    }

    public void randomKit(Player p) {
        Random r = new Random();
        KitManager km = KitManager.getManager();
        int index = r.nextInt(km.getKits().size());

        km.applyKit(p, km.getKits().get(index));
    }

    public void addPermission(String playerName, String permission) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pex user " + playerName + " add " + permission);
    }
    public void fireworksRandom(Player p){
        Random r = new Random();
        Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        int rp = r.nextInt(2);
        fwm.setPower(rp);

        int rt = r.nextInt(5);
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;
        FireworkEffect ef = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(cores().get(r.nextInt(16))).withFade(cores().get(r.nextInt(16))).with(type).trail(r.nextBoolean()).build();
        fwm.addEffect(ef);
        fw.setFireworkMeta(fwm);
    }
    private List<Color> cores(){
        List<Color> list = new ArrayList<>();
        list.add(Color.AQUA);
        list.add(Color.BLACK);
        list.add(Color.BLUE);
        list.add(Color.FUCHSIA);
        list.add(Color.GRAY);
        list.add(Color.GREEN);
        list.add(Color.LIME);
        list.add(Color.MAROON);
        list.add(Color.NAVY);
        list.add(Color.OLIVE);
        list.add(Color.ORANGE);
        list.add(Color.PURPLE);
        list.add(Color.RED);
        list.add(Color.SILVER);
        list.add(Color.TEAL);
        list.add(Color.WHITE);
        list.add(Color.YELLOW);
        return list;
    }
}