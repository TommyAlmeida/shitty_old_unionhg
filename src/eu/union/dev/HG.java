package eu.union.dev;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HG extends JavaPlugin implements Listener{

    private static HG instance;

    @Override
    public void onLoad() {
        //Bukkit.getServer().unloadWorld("world", false);
        //deleteWorld(new File("world"));
    }

    @Override
    public void onEnable() {
        instance = this;
        HGManager hgm = HGManager.getInstance();
        hgm.setStatus(HGManager.Status.LOBBY);
        hgm.setup();
        Timer.getInstace().start();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new HGManager(), this);
    }

    public static HG getInstance() {
        return instance;
    }
    public void deleteWorld(File file){
        if (file.isDirectory()){
            String[] list = file.list();
            for (int i = 0; i < list.length; i++) {
                deleteWorld(new File(file, list[i]));
            }
        }
        file.delete();
    }
}
