package eu.union.dev.utils;


import eu.union.dev.*;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import eu.union.dev.storage.KPlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Util {

    private static Util instance = new Util();

    public static Util getInstance() {
        return instance;
    }

    public String center(String msg, int length) {
        StringBuilder b = new StringBuilder("");
        int msglength = msg.length();
        int numberspaces = Math.round(length / 2) - Math.round(msglength / 2);
        for (int i = 0; i <= numberspaces; i++) {
            b.append(" ");
        }
        for (int i = 0; i < msglength; i++) {
            b.append(msg.charAt(i));
        }
        return b.toString();
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
        {
            Icon kits = new Icon(Material.CHEST, "§bKits", "§7Choose your kit");
            inv.setItem(0, kits.build());
        }

        {
            ItemStack i = new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5);
            ItemMeta im = i.getItemMeta();
            im.setDisplayName("§7");
            i.setItemMeta(im);
            //add items
        }
    }

    public void buildScoreboard(Player p) {
        //final KPlayer profile = PlayerManager.getPlayer(p.getUniqueId());
        KitManager km = KitManager.getManager();

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective stats = board.registerNewObjective("stats", "dummy");
        stats.setDisplaySlot(DisplaySlot.SIDEBAR);
        int index = 12;
        stats.setDisplayName("    §6§lUnion§f§l-§6§lHG    ");
        stats.getScore("§a").setScore(index--);
        stats.getScore("§fStage:").setScore(index--);
        stats.getScore("§1").setScore(index--);
        stats.getScore("§fTimer:").setScore(index--);
        stats.getScore("§2").setScore(index--);
        stats.getScore("§fPlayers:").setScore(index--);
        stats.getScore("§3").setScore(index--);
        stats.getScore("§fKit:").setScore(index--);
        stats.getScore("§4").setScore(index--);
        stats.getScore("§c").setScore(index--);
        stats.getScore("§5").setScore(index--);
        stats.getScore("§7/score").setScore(index--);
        stats.getScore("§fwww.unionnetwork.eu").setScore(index);

        board.registerNewTeam("stage").addEntry("§1");
        board.registerNewTeam("timer").addEntry("§2");
        board.registerNewTeam("online").addEntry("§3");
        board.registerNewTeam("kit").addEntry("§4");
        board.registerNewTeam("server").addEntry("§5");

        final Team stage = board.getTeam("stage");
        final Team timer = board.getTeam("timer");
        final Team online = board.getTeam("online");
        final Team kit = board.getTeam("kit");
        final Team server = board.getTeam("server");

        String stagee = getStage();
        String timerr = "";
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            timerr = "§a"+Timer.getInstace().getTimerFormated();
        }else{
            timerr = "§c"+Timer.getInstace().getTimerFormated();
        }
        String onlinee = ""+HGManager.getInstance().getPlayersVivos().size();
        String kitt = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(p).getName());
        String serverr = "A1";
        stage.setPrefix(stagee);
        timer.setPrefix(timerr);
        online.setPrefix("§d" + onlinee+"/60");
        kit.setPrefix("§b"+kitt);
        server.setPrefix("§6" + serverr);

        p.setScoreboard(board);
    }

    public void updateSocoreBoard(Player p){
        Scoreboard board = p.getScoreboard();
        final Team stage = board.getTeam("stage");
        final Team timer = board.getTeam("timer");
        final Team online = board.getTeam("online");
        final Team kit = board.getTeam("kit");
        final Team server = board.getTeam("server");

        String stagee = getStage();
        String timerr = "";
        if (HGManager.getInstance().getStatus() == HGManager.Status.LOBBY){
            timerr = "§a"+Timer.getInstace().getTimerFormated();
        }else{
            timerr = "§c"+Timer.getInstace().getTimerFormated();
        }
        String onlinee = ""+HGManager.getInstance().getPlayersVivos().size();
        String kitt = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(p).getName());
        String serverr = "A1";
        stage.setPrefix(stagee);
        timer.setPrefix(timerr);
        online.setPrefix("§d" + onlinee+"/60");
        kit.setPrefix("§b"+kitt);
        server.setPrefix("§6" + serverr);
    }

    public void setTab(Player p){
        String kit = WordUtils.capitalize(KitManager.getManager().getPlayerKitInLobby(p).getName());
        Packets.getAPI().setHeaderFooter(p,"§6§lUnion§f§l-§6§lHG","§cKills: §f0 §bKit: §f"+kit+" §aStage: §f"+getStage()+" §bTimer: §e"+Timer.getInstace().getTimerFormated()+" §7Ping: §f"+((CraftPlayer)p).getHandle().ping);
    }
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