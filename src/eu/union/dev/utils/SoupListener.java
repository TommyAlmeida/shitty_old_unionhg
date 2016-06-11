package eu.union.dev.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getHealth() != 20.0D) {
            int soup = 7;
            if (((event.getAction() == Action.RIGHT_CLICK_AIR) ||
                    (event.getAction() == Action.RIGHT_CLICK_BLOCK)) &&
                    (player.getItemInHand().getType() == Material.MUSHROOM_SOUP)) {
                player.setHealth(player.getHealth() + soup > player.getMaxHealth() ? player.getMaxHealth() : player.getHealth() + soup);
                event.getPlayer().setItemInHand(new ItemStack(Material.BOWL));
            }
        }
    }
}