package eu.union.dev.utils;

import eu.union.dev.HG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fentis on 18/06/2016.
 */
public class Holograms {

    String Text = "";
    double Height = 0;
    Location loc = null;
    List<String> lines = new ArrayList<String>();
    ArrayList<Entity> holos = new ArrayList<Entity>();

    public Holograms(Location location, double Height, String... lines) {
        if (lines.length == 1){
            Text = lines[0];
        }else{
            this.lines = listbuild(lines);
        }
        this.loc = location;
        this.Height = Height;
    }
    public void spawn(){
        if (lines.size() == 0){
            loc.setY((loc.getY() + Height)-1.25);
            ArmorStand Hologram = loc.getWorld().spawn(loc, ArmorStand.class);
            Hologram.setCustomName(Text);
            Hologram.setCustomNameVisible(true);
            Hologram.setGravity(false);
            Hologram.setVisible(false);
            return;
        }
        if (lines.size() >= 1){
            loc.setY((loc.getY() + Height)-1.25);
            for(int i = lines.size();i>0;i--){
                ArmorStand Hologram = loc.getWorld().spawn(loc, ArmorStand.class);
                holos.add(Hologram);
                Hologram.setCustomName(lines.get(i-1));
                Hologram.setCustomNameVisible(true);
                Hologram.setGravity(false);
                Hologram.setVisible(false);
                loc.setY(loc.getY()+0.25);
            }
        }
    }
    public void spawn(int time){
        if (lines.size() == 0){
            loc.setY((loc.getY() + Height)-1.25);
            final ArmorStand Hologram = loc.getWorld().spawn(loc, ArmorStand.class);
            Hologram.setCustomName(Text);
            Hologram.setCustomNameVisible(true);
            Hologram.setGravity(false);
            Hologram.setVisible(false);
            Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Hologram.remove();
                }
            }, time);
            return;
        }
        if (lines.size() >= 1){
            loc.setY((loc.getY() + Height)-1.25);
            for(int i = lines.size();i>0;i--){
                final ArmorStand Hologram = loc.getWorld().spawn(loc, ArmorStand.class);
                holos.add(Hologram);
                Hologram.setCustomName(lines.get(i-1));
                Hologram.setCustomNameVisible(true);
                Hologram.setGravity(false);
                Hologram.setVisible(false);
                loc.setY(loc.getY()+0.25);
                Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Hologram.remove();
                    }
                }, time);
            }
        }
    }

    private List<String> listbuild(String... list){
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            lista.add(list[i]);
        }
        return lista;
    }
}
