package eu.union.dev.utils;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.union.dev.HG;
import eu.union.dev.HGManager;
import eu.union.dev.KitManager;
import eu.union.dev.Timer;
import eu.union.dev.api.Ability;
import eu.union.dev.api.Icon;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
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
        String msg = "§c" + killed.getName() +"§7<§a"+kit+"§7>§a has died by ";
        if (killer == null){
            switch (damage){
                case BLOCK_EXPLOSION:
                    Bukkit.broadcastMessage(msg+"explosion of TNT");
                    break;
                case ENTITY_EXPLOSION:
                    Bukkit.broadcastMessage(msg+"explosion of Creeper");
                    break;
                case LAVA:
                    Bukkit.broadcastMessage(msg+"lava");
                    break;
                case FALL:
                    Bukkit.broadcastMessage(msg+"fall damage");
                    break;
                case FIRE:
                    Bukkit.broadcastMessage(msg+"fire");
                    break;
                case FIRE_TICK:
                    Bukkit.broadcastMessage(msg+"fire");
                    break;
                case MAGIC:
                    Bukkit.broadcastMessage(msg+"magic");
                    break;
                case DROWNING:
                    Bukkit.broadcastMessage(msg+"drowning");
                    break;
                case WITHER:
                    Bukkit.broadcastMessage(msg+"wither poison");
                    break;
                case POISON:
                    Bukkit.broadcastMessage(msg+"poison");
                    break;
                case FALLING_BLOCK:
                    Bukkit.broadcastMessage(msg+"falling block");
                    break;
                case LIGHTNING:
                    Bukkit.broadcastMessage(msg+"lightning");
                    break;
                case PROJECTILE:
                    Bukkit.broadcastMessage(msg+"projectile");
                    break;
                case VOID:
                    Bukkit.broadcastMessage(msg+"void");
                    break;
                case SUICIDE:
                    Bukkit.broadcastMessage(msg+"suicide");
                    break;
                case SUFFOCATION:
                    Bukkit.broadcastMessage(msg+"suffocation");
                    break;
                case STARVATION:
                    Bukkit.broadcastMessage(msg+"starvation");
                    break;
                case THORNS:
                    Bukkit.broadcastMessage(msg+"thorns");
                    break;
                case ENTITY_ATTACK:
                    Entity en = killed.getLastDamageCause().getEntity();
                    String entidade = en.toString().toLowerCase();
                    Bukkit.broadcastMessage(msg + entidade);
                    break;
                default:
                    Bukkit.broadcastMessage(msg.replace("by ",""));
                    break;
            }
        }else{
            if (km.getPlayerKitInLobby(killer).getName() != null){
                String kitk = WordUtils.capitalize(km.getPlayerKitInLobby(killer).getName());
                Bukkit.broadcastMessage("§a" + killed.getDisplayName()+ "§a<"+kit+">" + " §chas been slained by §b" + killer.getDisplayName()+"§a<"+kitk+">");
            }else{
                String kitk = "None";
                Bukkit.broadcastMessage("§a" + killed.getDisplayName()+ "§a<"+kit+">" + " §chas been slained by §b" + killer.getDisplayName()+"§a<"+kitk+">");
            }
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

    public void sendToServer(String server, Player p){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        p.sendPluginMessage(HG.getInstance(), "BungeeCord", out.toByteArray());
    }
}