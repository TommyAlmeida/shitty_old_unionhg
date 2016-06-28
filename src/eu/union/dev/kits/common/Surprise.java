package eu.union.dev.kits.common;

import eu.union.dev.api.Icon;
import eu.union.dev.storage.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by Fentis on 10/06/2016.
 */
public class Surprise extends Kit{

    public Surprise() {//
        super("surprise", "unkit.surprise", Difficulty.LOW, Rarity.COMMON, new Icon(Material.CAKE), 0);
    }

    @Override
    public void applyKit(Player player) {
        //Nada se aplica ao kit
    }
}
