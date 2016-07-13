package eu.union.dev;

import eu.union.dev.commands.*;
import eu.union.dev.invs.CompassMenu;
import eu.union.dev.invs.KitMenu;
import eu.union.dev.invs.SponsorMenu;
import eu.union.dev.invs.TrollMenu;
import eu.union.dev.listeners.*;
import eu.union.dev.listeners.Icons.SponsorMode;
import eu.union.dev.storage.Kit;
import eu.union.dev.storage.sql.Database;
import net.minecraft.util.com.google.common.collect.ImmutableSet;
import net.minecraft.util.com.google.common.reflect.ClassPath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Random;

public class HG extends JavaPlugin implements Listener{

    private static HG instance;
    Database sql = new Database("root", "Be2Cj16M790EcI", "HG", "3306", "localhost");
    private Connection c;

    @Override
    public void onLoad() {
        Bukkit.getServer().unloadWorld("world", false);
        deleteWorld(new File("world"));
    }

    @Override
    public void onEnable() {
        instance = this;
        canConnect(true);

        HGManager hgm = HGManager.getInstance();
        hgm.setStatus(HGManager.Status.LOBBY);

        hgm.setup();
        Timer.getInstace().start();

        getConfig().addDefault("IP","A1");
        getConfig().options().copyDefaults(true);
        saveConfig();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new HGListener(), this);
        pm.registerEvents(new KitMenu(),this);
        pm.registerEvents(new SoupListener(),this);
        pm.registerEvents(new BordaListener(),this);
        pm.registerEvents(new ChatListener(),this);
        pm.registerEvents(new CompassListener(),this);
        pm.registerEvents(new SpecsListener(),this);
        pm.registerEvents(new AdminListener(),this);
        pm.registerEvents(new CompassMenu(),this);
        pm.registerEvents(new TrollMenu(),this);
        pm.registerEvents(new SponsorMenu(),this);
        pm.registerEvents(new SponsorMode(),this);
        getCommand("kit").setExecutor(new KitCMD());
        getCommand("feast").setExecutor(new FeastCMD());
        getCommand("start").setExecutor(new StartCMD());
        getCommand("fly").setExecutor(new FlyCMD());
        getCommand("build").setExecutor(new BuildCMD());
        getCommand("info").setExecutor(new InfoCMD());
        getCommand("ip").setExecutor(new IpCMD());
        getCommand("gamemode").setExecutor(new GmCMD());
        getCommand("admin").setExecutor(new AdminCMD());
        getCommand("score").setExecutor(new ScoreCMD());
        getCommand("stats").setExecutor(new StatsCMD());
        getCommand("ping").setExecutor(new PingCMD());
        getCommand("report").setExecutor(new ReportCMD());
        getCommand("check").setExecutor(new CheckCMD());
        getCommand("clearchat").setExecutor(new ClearChatCMD());
        getCommand("inv").setExecutor(new InvCMD());
        getCommand("tp").setExecutor(new TpCMD());
        getCommand("nokit").setExecutor(new NoKitCMD());

        borda();
        registerKits();
        MemoryFix();
    }

    @Override
    public void onDisable() {
        canConnect(false);
    }

    public static HG getInstance() {
        return instance;
    }

    private void deleteWorld(File file){
        if (file.isDirectory()){
            String[] list = file.list();
            for (int i = 0; i < list.length; i++) {
                deleteWorld(new File(file, list[i]));
            }
        }
        file.delete();
    }

    private void registerKits() {
        KitManager km = KitManager.getManager();

        try {
            /* Pega todas as classes do pacote de kits */
            ImmutableSet<ClassPath.ClassInfo> kitClasses = ClassPath.from(getClassLoader())
                    .getTopLevelClassesRecursive("eu.union.dev.kits");

            kitClasses.forEach(classInfo -> {
                try {
                    /* Carrega a classe do kit. */
                    Class<?> kitClass = classInfo.load();

                    /*  Cria uma nova instancia da classe carregada. */
                    Kit kit = (Kit) kitClass.newInstance();

                    /* Verifica se a classe é um Listener, e registra os eventos */
                    if (Listener.class.isAssignableFrom(kitClass)) {
                        getServer().getPluginManager().registerEvents((Listener) kit, this);
                    }

                    /* Registra o kit */
                    km.registerKit(kit);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void borda(){
        int size = HGManager.getInstance().getBordSize();
        for (int x = -size; x <= size; x++) {
            if ((x == -size) || (x == size)) {
                for (int z = -size; z <= size; z++) {
                    for (int y = 0; y <= size; y++) {
                        Location loc = new Location(Bukkit.getWorlds().get(0), x, y, z);
                        if (!loc.getChunk().isLoaded())
                            loc.getChunk().load();
                        loc.getBlock().setType(Material.QUARTZ_BLOCK);
                        loc.getBlock().setData((byte)new Random().nextInt(4));
                    }
                }
            }
        }
        for (int z = -size; z <= size; z++)
            if ((z == -size) || (z == size))
                for (int x = -size; x <= size; x++)
                    for (int y = 0; y <= size; y++) {
                        Location loc = new Location(Bukkit.getWorlds().get(0), x, y, z);
                        if (!loc.getChunk().isLoaded())
                            loc.getChunk().load();
                        loc.getBlock().setType(Material.QUARTZ_BLOCK);
                        loc.getBlock().setData((byte)new Random().nextInt(4));
                    }
    }

    public void canConnect(boolean can) {
        if (!can) {
            if (c != null) {
                c = sql.close(c);
            }
        } else {
            try {
                sql.open();
                this.c = sql.getConnection();
                sql.setupTables();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void MemoryFix() {
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Iterator<?> localIterator2;
            for (Iterator<?> localIterator1 = Bukkit.getWorlds().iterator(); localIterator1.hasNext(); localIterator2.hasNext()) {
                World world = (World) localIterator1.next();

                localIterator2 = ((CraftWorld) world).getHandle().tileEntityList.iterator();
            }
        }, 100L, 100L);
    }

    public Database getSQL() {
        return sql;
    }
}
