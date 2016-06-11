package eu.union.dev;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import eu.union.dev.commands.FeastCMD;
import eu.union.dev.commands.KitCMD;
import eu.union.dev.commands.StartCMD;
import eu.union.dev.invs.KitMenu;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.SoupListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class HG extends JavaPlugin implements Listener{

    private static HG instance;

    @Override
    public void onLoad() {
        Bukkit.getServer().unloadWorld("world", false);
        deleteWorld(new File("world"));
    }

    @Override
    public void onEnable() {
        instance = this;
        HGManager hgm = HGManager.getInstance();
        hgm.setStatus(HGManager.Status.LOBBY);
        hgm.setup();
        Timer.getInstace().start();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new HGListener(), this);
        pm.registerEvents(new KitMenu(),this);
        pm.registerEvents(new SoupListener(),this);
        getCommand("kit").setExecutor(new KitCMD());
        getCommand("feast").setExecutor(new FeastCMD());
        getCommand("start").setExecutor(new StartCMD());
        borda();
        registerKits();
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
                        loc.getBlock().setType(Material.BEDROCK);
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
                        loc.getBlock().setType(Material.BEDROCK);
                    }
    }
}
